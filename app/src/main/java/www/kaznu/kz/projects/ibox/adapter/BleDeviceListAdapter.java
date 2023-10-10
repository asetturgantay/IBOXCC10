package www.kaznu.kz.projects.ibox.adapter;

import static com.yandex.runtime.Runtime.getApplicationContext;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.content.pm.PackageManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import java.util.ArrayList;

import www.kaznu.kz.projects.ibox.R;
import www.kaznu.kz.projects.ibox.utils.Utils;

public class BleDeviceListAdapter extends BaseAdapter {

    private final LayoutInflater mInflater;
    private final ArrayList<BluetoothDevice> mLeDevices;
    private final ArrayList<Integer> RSSIs;
    private final ArrayList<String> scanRecords;

    private static final int REQUEST_BLUETOOTH_PERMISSION = 1; // Use any unique integer value






    public BleDeviceListAdapter(Context context) {
        mLeDevices = new ArrayList<>();
        RSSIs = new ArrayList<>();
        scanRecords = new ArrayList<>();
        this.mInflater = LayoutInflater.from(context);
    }

    public void addDevice(BluetoothDevice device, int RSSI, String scanRecord) {
        if (!mLeDevices.contains(device)) {
            this.mLeDevices.add(device);
            this.RSSIs.add(RSSI);
            this.scanRecords.add(scanRecord);
        } else {
            for (int i = 0; i < mLeDevices.size(); i++) {
                BluetoothDevice d = mLeDevices.get(i);
                if (device.getAddress().equals(d.getAddress())) {
                    RSSIs.set(i, RSSI);
                    scanRecords.set(i, scanRecord);
                }
            }
        }
    }

    public BluetoothDevice getDevice(int position) {
        return mLeDevices.get(position);
    }

    @Override
    public int getCount() {
        return mLeDevices.size();
    }

    @Override
    public Object getItem(int arg0) {
        return mLeDevices.get(arg0);
    }

    @Override
    public long getItemId(int arg0) {
        return 0;
    }

    @SuppressLint({"InflateParams", "SetTextI18n"})
    @Override
    public View getView(int position, View view, ViewGroup arg2) {

        ViewHolder viewholder;
        if (view == null) {
            view = mInflater.inflate(R.layout.item_devicelist, null);
            viewholder = new ViewHolder();
            viewholder.deviceName = view.findViewById(R.id.tv_devicelist_name);
            viewholder.deviceAddress = view.findViewById(R.id.tv_devicelist_address);
            viewholder.deviceRSSI = view.findViewById(R.id.tv_devicelist_rssi);
            viewholder.deviceRecord = view.findViewById(R.id.tv_devicelist_scanRecord);
            viewholder.deviceRecordName = view.findViewById(R.id.tv_devicelist_scanRecord_name);
            view.setTag(viewholder);
        } else {
            viewholder = (ViewHolder) view.getTag();
        }

        // Check if the app has the necessary Bluetooth permissions
        if (ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.BLUETOOTH) == PackageManager.PERMISSION_GRANTED) {
            // Permission is granted, you can proceed with accessing the Bluetooth device's name
            String name = mLeDevices.get(position).getName();
            // Do something with the device name
        } else {
            // Permission is not granted, request it from the user
            ActivityCompat.requestPermissions((Activity) getApplicationContext(), new String[]{Manifest.permission.BLUETOOTH}, REQUEST_BLUETOOTH_PERMISSION);
        }


        String name = mLeDevices.get(position).getName();


        if (name != null)
            viewholder.deviceName.setText(name);
        else
            viewholder.deviceName.setText(mInflater.getContext().getResources().getString(R.string.bluetooth_unknown_device));

        viewholder.deviceAddress.setText(mInflater.getContext().getResources().getString(R.string.address)
                + mLeDevices.get(position).getAddress());
        viewholder.deviceRSSI.setText(mInflater.getContext().getResources().getString(R.string.rssi) + RSSIs.get(position).toString());
        viewholder.deviceRecord.setText(mInflater.getContext().getResources().getString(R.string.broadcast_packet) + "\n"
                +":"+ scanRecords.get(position));
        viewholder.deviceRecordName.setText(
                mInflater.getContext().getResources().getString(R.string.broadcast_racket_name)
                + ":"+Utils.ParseScanRecord(scanRecords.get(position)));
        return view;
    }

    static class ViewHolder {
        TextView deviceName;
        TextView deviceAddress;
        TextView deviceRSSI;
        TextView deviceRecord;
        TextView deviceRecordName;
    }

    public void clear() {
        mLeDevices.clear();
        RSSIs.clear();
        scanRecords.clear();
        this.notifyDataSetChanged();
    }





}