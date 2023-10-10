package www.kaznu.kz.projects.ibox.activities;


import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Random;
import java.util.Timer;


import www.kaznu.kz.projects.ibox.R;


/**
 * A simple {@link Fragment} subclass.
 */
public class DataFragment extends Fragment {


    public DataFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.fragment_data, container, false);

        final LineChart chart = rootView.findViewById(R.id.timelinechart);
        RequestQueue mQueue = Volley.newRequestQueue(rootView.getContext());

        String url = "http://a0868210.xsph.ru/AndroidApi/volley/get_text.php";

        JsonArrayRequest request = new JsonArrayRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {

                        try {

                            ArrayList<Entry> temperature = new ArrayList<>();
                            ArrayList<Entry> aqi = new ArrayList<>();
                            ArrayList<Entry> pm1 = new ArrayList<>();
                            ArrayList<Entry> pm10 = new ArrayList<>();
                            ArrayList<Entry> pm25 = new ArrayList<>();

                            Log.i("bb", String.valueOf(response));

                            for (int i = 0; i < response.length(); i++) {
                                JSONObject value = response.getJSONObject(i);

                                int temp = value.getInt("tempValue");
                                int aqival = value.getInt("AQI");

                                int pm1val = value.getInt("PM1");
                                int pm1val0 = value.getInt("PM10");
                                int pm1va25 = value.getInt("PM25");

                                temperature.add(new Entry(i, temp));
                                aqi.add(new Entry(i, aqival));
                                pm1.add(new Entry(i, pm1val));
                                pm25.add(new Entry(i, pm1va25));
                                pm10.add(new Entry(i, pm1val0));

                            }

                            ArrayList<ILineDataSet> lines = new ArrayList<ILineDataSet> ();

                            LineDataSet dataSet0 = new LineDataSet(temperature, "Температура");
                            dataSet0.setColor(Color.RED);
                            dataSet0.setCircleColor(Color.RED);
                            lines.add(dataSet0);

                            LineDataSet dataSet1 = new LineDataSet(aqi, "AQI");
                            dataSet1.setColor(Color.BLUE);
                            dataSet1.setCircleColor(Color.BLUE);

                            lines.add(dataSet1);


                            LineDataSet dataSet2 = new LineDataSet(pm1, "PM 1");
                            dataSet2.setColor(Color.GREEN);
                            dataSet2.setCircleColor(Color.GREEN);
                            lines.add(dataSet2);

                            LineDataSet dataSet3 = new LineDataSet(pm25, "PM 2.5");
                            dataSet3.setColor(Color.DKGRAY);
                            dataSet3.setCircleColor(Color.DKGRAY);
                            lines.add(dataSet3);

                            LineDataSet dataSet4 = new LineDataSet(pm10, "PM 10");
                            dataSet4.setColor(Color.MAGENTA);
                            dataSet4.setCircleColor(Color.MAGENTA);
                            lines.add(dataSet4);

                            chart.setData(new LineData(lines));
                            XAxis xAxis = chart.getXAxis();
                            xAxis.setDrawAxisLine(true);
                            xAxis.setDrawGridLines(true);
                            xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);

                            Description description = chart.getDescription();
                            description.setEnabled(false);




                        } catch (JSONException e) {
                            e.printStackTrace();
                            Log.i("bb", "Error response");
                            Log.d("TAG", "Response: " + response.toString());

                        }

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        error.printStackTrace();
                    }
                });
        mQueue.add(request);
//        chart.invalidate();
        return rootView;
    }

}
