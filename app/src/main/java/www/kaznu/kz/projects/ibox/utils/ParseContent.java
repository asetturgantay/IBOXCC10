package www.kaznu.kz.projects.ibox.utils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class ParseContent {

    public ParseContent() {

    }

    public ArrayList<DeviceModel> getInfo(String response) {
        ArrayList<DeviceModel> deviceModelArrayList = new ArrayList<>();
        try {
            JSONArray dataArray = new JSONArray(response);

            for (int i = 0; i < dataArray.length(); i++) {
                DeviceModel deviceModel = new DeviceModel();
                JSONObject dataObj = dataArray.getJSONObject(i);
                deviceModel.setLongitude(dataObj.getDouble(Constants.LONGITUDE));
                deviceModel.setLatitude(dataObj.getDouble(Constants.LATITUDE));
                deviceModel.setAqi(dataObj.getInt(Constants.AQI));
                deviceModel.setHumidity(dataObj.getInt(Constants.HUMIDITY));
                deviceModel.setPressure(dataObj.getInt(Constants.PRESSURE));
                deviceModel.setTemperature(dataObj.getInt(Constants.TEMPERATURE));
                deviceModel.setPm01(dataObj.getInt(Constants.PM01));
                deviceModel.setPm10(dataObj.getInt(Constants.PM10));
                deviceModel.setPm25(dataObj.getInt(Constants.PM25));

                deviceModelArrayList.add(deviceModel);
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return deviceModelArrayList;
    }

}