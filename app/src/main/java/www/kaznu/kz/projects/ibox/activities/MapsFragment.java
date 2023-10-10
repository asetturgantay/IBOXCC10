package www.kaznu.kz.projects.ibox.activities;

import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.core.content.res.ResourcesCompat;
import androidx.fragment.app.Fragment;

import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.yandex.mapkit.Animation;
import com.yandex.mapkit.MapKitFactory;
import com.yandex.mapkit.geometry.Point;
import com.yandex.mapkit.map.CameraPosition;
import com.yandex.mapkit.map.MapObject;
import com.yandex.mapkit.map.MapObjectTapListener;
import com.yandex.mapkit.map.PlacemarkMapObject;
import com.yandex.mapkit.mapview.MapView;
import com.yandex.runtime.image.ImageProvider;
import com.yandex.runtime.ui_view.ViewProvider;

import java.util.ArrayList;
import java.util.Random;

import es.dmoral.toasty.Toasty;
import www.kaznu.kz.projects.ibox.R;
import www.kaznu.kz.projects.ibox.utils.Constants;
import www.kaznu.kz.projects.ibox.utils.ParseContent;
import www.kaznu.kz.projects.ibox.utils.DeviceModel;

public class MapsFragment extends Fragment {

    private MapView mapview;
    private ParseContent parseContent;
    TextView tvMessage;
    private ArrayList<DeviceModel> deviceModelArrayList;
    private Handler animationHandler;

    public MapsFragment() {
        // Required empty public constructor
    }

    double latitude = 43.2557; // Широта города Алматы
    double longitude = 76.9451; // Долгота города Алматы


    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        MapKitFactory.setApiKey("0c19b044-7b1e-4190-8682-4118f672110d");

        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.fragment_maps, container, false);


        MapKitFactory.initialize(rootView.getContext());

        RequestQueue queue = Volley.newRequestQueue(rootView.getContext());

        mapview = rootView.findViewById(R.id.map_view);

        mapview.getMap().set2DMode(true);

        animationHandler = new Handler();

        if (getArguments() != null) {
            /*mapview.getMap().move(
                    new CameraPosition(
                            new Point(
                                    getArguments().getDouble(Constants.LATITUDE),
                                    getArguments().getDouble(Constants.LONGITUDE)
                            ), 17.0f, 0.0f, 0.0f),
                    new Animation(Animation.Type.SMOOTH, 0),
                    null
            );*/

            mapview.getMap().move(
                    new CameraPosition(
                            new Point(latitude, longitude), // Координаты Алматы
                            12.0f, // Масштаб (уровень приближения)
                            0.0f, 0.0f

                    ),
                    new Animation(Animation.Type.SMOOTH, 0),
                    null
            );

        }

        parseContent = new ParseContent();

        StringRequest jsObjRequest = new StringRequest(Request.Method.GET,
                Constants.GET_URL, new Response.Listener<String>() {

            @Override
            public void onResponse(final String response) {

                deviceModelArrayList = parseContent.getInfo(response);

                for (int i = 0; i < deviceModelArrayList.size(); i++) {
                    final int aqiColor;
                    int AQI = deviceModelArrayList.get(i).getAqi();

                    if(AQI > -1 && AQI <= 51) {
                        aqiColor = getActivity().getResources().getColor(R.color.aqiGood);
                    }
                    else if(AQI > 50 && AQI < 101) {
                        aqiColor = getActivity().getResources().getColor(R.color.aqiModerate);
                    }
                    else if(AQI > 100 && AQI < 151) {
                        aqiColor = getActivity().getResources().getColor(R.color.aqiModerateUnhealthy);
                    }else if(AQI > 150 && AQI < 201) {
                        aqiColor = getActivity().getResources().getColor(R.color.aqiUnhealthy);
                    }else if(AQI > 200 && AQI < 301) {
                        aqiColor = getActivity().getResources().getColor(R.color.aqiVeryUnhealthy);
                    }else {
                        aqiColor = getActivity().getResources().getColor(R.color.aqiHazardous);
                    }
                    final ImageView imageView = new ImageView(getContext());
                    ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                    imageView.setLayoutParams(params);

                    imageView.setImageResource(R.drawable.ic_place);
                    imageView.setColorFilter(aqiColor);

                    final ViewProvider viewProvider = new ViewProvider(imageView);

                    final PlacemarkMapObject placemarkMapObject = mapview.getMap().getMapObjects().addPlacemark(
                            new Point(
                                    deviceModelArrayList.get(i).getLatitude(),
                                    deviceModelArrayList.get(i).getLongitude()
                            ), viewProvider
                    );

                    placemarkMapObject.addTapListener(new MapObjectTapListener() {
                        @Override
                        public boolean onMapObjectTap(@NonNull MapObject mapObject, @NonNull Point point) {
                            String message = getMessage(deviceModelArrayList, point.getLatitude(), point.getLongitude());
                            final View customView = (View) inflater.inflate(R.layout.dialog_map_data, null);

                            tvMessage = customView.findViewById(R.id.tv_message);

                            tvMessage.setText(message);
                            BottomSheetDialog dialog = new BottomSheetDialog(getActivity());
                            dialog.setContentView(customView);
                            dialog.show();
                            Log.d(Constants.TAG, message);
                            return false;
                        }
                    });

                }
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Log.v(Constants.TAG, error.toString());
            }
        });

        queue.add(jsObjRequest);

        return rootView;
    }

    public double round(double data) {
        double roundOff = Math.round(data * 1000.0) / 1000.0;
        return roundOff;
    }

    public String getMessage(ArrayList<DeviceModel> deviceModels, double latitude, double longitude) {
        String message = "Пусто";

        for(int i = 0; i < deviceModels.size(); i++) {
            if(round(deviceModels.get(i).getLatitude()) == round(latitude) &&
                    round(deviceModels.get(i).getLongitude()) == round(longitude)) {
                message = "AQI: " + deviceModels.get(i).getAqi() +
                        "\nPM 2.5: " + deviceModels.get(i).getPm25() +
                        "\nPM 10 : " + deviceModels.get(i).getPm10() +
                        "\nPM 01: " + deviceModels.get(i).getPm01() +
                        "\nТемпература: " + deviceModels.get(i).getTemperature() +
                        "\nВлажность: " + deviceModels.get(i).getHumidity() +
                        "\nДавление: " + deviceModels.get(i).getPressure();
                break;
            }
        }
        return message;
    }
    @Override
    public void onStart() {
        super.onStart();
        mapview.onStart();
        MapKitFactory.getInstance().onStart();
    }

    @Override
    public void onStop() {
        super.onStop();
        mapview.onStop();
        MapKitFactory.getInstance().onStop();

    }

    @Override
    public void onPause() {
        super.onPause();

    }

}



