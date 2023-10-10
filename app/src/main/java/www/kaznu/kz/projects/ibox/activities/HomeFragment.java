package www.kaznu.kz.projects.ibox.activities;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import www.kaznu.kz.projects.ibox.R;
import www.kaznu.kz.projects.ibox.databinding.FragmentHomeBinding;
import www.kaznu.kz.projects.ibox.fragments.HomeDialogFragment;
import www.kaznu.kz.projects.ibox.utils.Constants;

import static android.content.Context.MODE_PRIVATE;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Objects;

public class HomeFragment extends Fragment implements View.OnClickListener {

    TextView aqiValue, tempValue, humidityValue;
    TextView pm01Value, pm25Value, pm10Value, pressureValue;
    TextView aqiMessage;
    SharedPreferences sPref;
    ViewGroup rootView;

    String pm25;

    FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
    FirebaseUser currentUser = firebaseAuth.getCurrentUser();



    private ImageView img1;



    public HomeFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);






    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView = (ViewGroup) inflater.inflate(R.layout.fragment_home, container, false);

        aqiValue = rootView.findViewById(R.id.aqi_value);
        tempValue = rootView.findViewById(R.id.temperature_value);
        humidityValue = rootView.findViewById(R.id.humidity_value);
        pm01Value = rootView.findViewById(R.id.pm01_value);
        pm25Value = rootView.findViewById(R.id.pm25_value);
        pm10Value = rootView.findViewById(R.id.pm10_value);
        pressureValue = rootView.findViewById(R.id.pressure_value);



        aqiMessage = rootView.findViewById(R.id.aqi_message);

        img1 = rootView.findViewById(R.id.imgAQI);



        img1.setOnClickListener(this);
        tempValue.setOnClickListener(this);
        humidityValue.setOnClickListener(this);
        pm01Value.setOnClickListener(this);
        pm10Value.setOnClickListener(this);
        pm25Value.setOnClickListener(this);
        pressureValue.setOnClickListener(this);




        /*sPref = getActivity().getPreferences(MODE_PRIVATE);
        SharedPreferences.Editor ed = sPref.edit();
        ed.putString("AQI_VALUE", "_ _");
        ed.putString("AQI_MESSAGE", "--------");
        ed.putString("TEMPERATURE", "_ _ °C");
        ed.putString("HUMIDITY", "_ _ %");
        ed.putString("PM10", "_ _");
        ed.putString("PM01", "_ _");
        ed.putString("PM25", "_ _");
        ed.putString("PRESSURE", "_ _");
        ed.putInt("AQI_COLOR", getActivity().getResources().getColor(R.color.colorGrayPrimary));
        ed.commit();*/
        return rootView;








    }

    @Override
    public void onClick(View view) {

        HomeDialogFragment dialog = null;

        switch (view.getId()) {
            case R.id.imgAQI:
                dialog = HomeDialogFragment.newInstance(true,"AQI",  "    Индекс качества воздуха информирует насколько загрязнен воздух.");
                assert this.getFragmentManager() != null;
                dialog.show(this.getFragmentManager(), "tag");
                break;
            case R.id.pm01_value:
                dialog = HomeDialogFragment.newInstance(false,"PM 1.0", "     Ультрадисперсные частицы (УДЧ) — наномасштабные частицы, размеры которых менее 100 нанометров.");
                assert this.getFragmentManager() != null;
                dialog.show(this.getFragmentManager(), "tag");
                break;
            case R.id.pm10_value:
                dialog = HomeDialogFragment.newInstance(false,"PM 10", "     Ультрадисперсные частицы (УДЧ) — наномасштабные частицы, размеры которых менее 100 нанометров.");
                assert this.getFragmentManager() != null;
                dialog.show(this.getFragmentManager(), "tag");
                break;
            case R.id.pm25_value:
                dialog = HomeDialogFragment.newInstance(false,"PM 2.5", "     Ультрадисперсные частицы (УДЧ) — наномасштабные частицы, размеры которых менее 100 нанометров.");
                assert this.getFragmentManager() != null;
                dialog.show(this.getFragmentManager(), "tag");
                break;
            case R.id.humidity_value:
                dialog = HomeDialogFragment.newInstance(false,"Влажность", "        Оптимальный уровень – это 45-65% относительной влажности");
                assert this.getFragmentManager() != null;
                dialog.show(this.getFragmentManager(), "tag");
                break;
            case R.id.temperature_value:
                dialog = HomeDialogFragment.newInstance(false,"Температура", "      Температура — физическая величина, характеризующая термодинамическую систему и количественно выражающая интуитивное понятие о различной степени нагретости тел.");
                assert this.getFragmentManager() != null;
                dialog.show(this.getFragmentManager(), "tag");
                break;
            case R.id.pressure_value:
                dialog = HomeDialogFragment.newInstance(false,"Давление", "     Абсолютной нормой считается АД 120/80 мм.рт.ст, слегка повышенным — 130/85 мм.рт.ст, повышенным нормальным — 139/89 мм.рт.ст.");
                assert this.getFragmentManager() != null;
                dialog.show(this.getFragmentManager(), "tag");
                break;
        }
    }

    @Override
    public void onPause() {
        Log.d(Constants.TAG, "onPause");
        sPref = requireActivity().getPreferences(MODE_PRIVATE);
        SharedPreferences.Editor ed = sPref.edit();
        ed.putString("AQI_VALUE", aqiValue.getText().toString());
        ed.putString("AQI_MESSAGE", aqiMessage.getText().toString());
        ed.putString("TEMPERATURE", tempValue.getText().toString());
        ed.putString("HUMIDITY", humidityValue.getText().toString());
        ed.putString("PM10", pm10Value.getText().toString());
        ed.putString("PM01", pm01Value.getText().toString());
        ed.putString("PM25", pm25Value.getText().toString());
        ed.putString("PRESSURE", pressureValue.getText().toString());
        ed.putInt("AQI_COLOR", aqiValue.getTextColors().getDefaultColor());
        ed.apply();
        super.onPause();
    }

    @Override
    public void onResume() {

        sPref = requireActivity().getPreferences(MODE_PRIVATE);
        String aqiSavedValue = sPref.getString("AQI_VALUE", "");
        String aqiSavedMessage = sPref.getString("AQI_MESSAGE", "");
        String temperatureSavedValue = sPref.getString("TEMPERATURE", "");
        String humiditySavedValue = sPref.getString("HUMIDITY", "");
        String pm10SavedValue = sPref.getString("PM10", "");
        String pm01SavedValue = sPref.getString("PM01", "");
        String pm25SavedValue = sPref.getString("PM25", "");
        String pressureSavedValue = sPref.getString("PRESSURE", "");
        aqiMessage.setTextColor(sPref.getInt("AQI_COLOR", 0));
        aqiValue.setTextColor(sPref.getInt("AQI_COLOR", 0));
        aqiValue.setText(aqiSavedValue);
        aqiMessage.setText(aqiSavedMessage);
        tempValue.setText(temperatureSavedValue);
        humidityValue.setText(humiditySavedValue);
        pm10Value.setText(pm10SavedValue);
        pm01Value.setText(pm01SavedValue);
        pm25Value.setText(pm25SavedValue);
        pressureValue.setText(pressureSavedValue);

        Log.d(Constants.TAG, "onResume");




        /*String url = "http://a0868210.xsph.ru/AndroidApi/volley/insert_data.php";
        RequestQueue queue = Volley.newRequestQueue(requireContext());






        JSONObject postData = new JSONObject();
        try {
            postData.put("aqi", aqiValue.getText().toString());
            postData.put("pm25", pm25Value.getText().toString());
            postData.put("pm10", pm10Value.getText().toString());
            postData.put("pm1", pm01Value.getText().toString());
            postData.put("pressure", pressureValue.getText().toString());
            postData.put("tempValue", tempValue.getText().toString());
            postData.put("humidityValue", humidityValue.getText().toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, url, postData,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        // Обработка ответа от сервера
                        try {
                            String message = response.getString("message");
                            // Ваш код обработки ответа
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e(Constants.TAG, "Ошибка" + Objects.requireNonNull(error.getMessage()));
                    }
                });

        queue.add(jsonObjectRequest);*/


        super.onResume();



    }

    @Override
    public void onDestroy() {
        Log.d(Constants.TAG, "onDestroy");
        sPref = requireActivity().getPreferences(MODE_PRIVATE);
        SharedPreferences.Editor ed = sPref.edit();
        ed.putString("AQI_VALUE", aqiValue.getText().toString());
        ed.putString("AQI_MESSAGE", aqiMessage.getText().toString());
        ed.putString("TEMPERATURE", tempValue.getText().toString());
        ed.putString("HUMIDITY", humidityValue.getText().toString());
        ed.putString("PM10", pm10Value.getText().toString());
        ed.putString("PM01", pm01Value.getText().toString());
        ed.putString("PM25", pm25Value.getText().toString());
        ed.putString("PRESSURE", pressureValue.getText().toString());
        ed.putInt("AQI_COLOR", aqiValue.getTextColors().getDefaultColor());
        ed.apply();


//        tempValue = rootView.findViewById(R.id.temperature_value);
//        humidityValue = rootView.findViewById(R.id.humidity_value);
//        pm01Value = rootView.findViewById(R.id.pm01_value);
//        pm25Value = rootView.findViewById(R.id.pm25_value);
//        pm10Value = rootView.findViewById(R.id.pm10_value);
//        pressureValue = rootView.findViewById(R.id.pressure_value);
//
//        aqiMessage = rootView.findViewById(R.id.aqi_message);
        super.onDestroy();





    }
}
