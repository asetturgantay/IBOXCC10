package www.kaznu.kz.projects.ibox.activities;

import android.Manifest;
import android.annotation.SuppressLint;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.github.javiersantos.materialstyleddialogs.MaterialStyledDialog;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.mikepenz.community_material_typeface_library.CommunityMaterial;
import com.mikepenz.iconics.IconicsDrawable;
import com.mikepenz.iconics.typeface.IIcon;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;

import www.kaznu.kz.projects.ibox.R;
import www.kaznu.kz.projects.ibox.adapter.DeviceListAdapter;
import www.kaznu.kz.projects.ibox.utils.Constants;
import www.kaznu.kz.projects.ibox.utils.Utils;



public class MainActivity extends AppCompatActivity implements
        NavigationView.OnNavigationItemSelectedListener,
        BottomNavigationView.OnNavigationItemSelectedListener,
        Toolbar.OnMenuItemClickListener {

    private String wholeString;
    public static String signin;

    FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
    FirebaseUser currentUser = firebaseAuth.getCurrentUser();


    ListView lvNewDevices;
    SwipeRefreshLayout swagLayout;
    DrawerLayout drawerLayout;
    ActionBarDrawerToggle toggle;
    NavigationView navigationView;
    MaterialStyledDialog materialStyledDialog;
    static final UUID myUUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");
    BluetoothAdapter mBluetoothAdapter;
    BluetoothSocket btSocket = null;
    private boolean isBtConnected = false;
    String deviceAddress;
    public static String EXTRA_ADDRESS = "device_address";
    public ArrayList<BluetoothDevice> mBTDevices = new ArrayList<>();

    public DeviceListAdapter mDeviceListAdapter;

    private RequestQueue requestQueue;

    private LocationManager locationManager;
    public Location gpsNetworkLocation;
    private SimpleDateFormat simpleDateFormat;

    StringBuilder messages;
    InputStream mmInStream = null;
    String incomingMessage;

    int permissionCheck = PackageManager.PERMISSION_DENIED;
    FirebaseAuth mAuth;





    private final BroadcastReceiver mBroadcastReceiver1 = new BroadcastReceiver() {
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            // When discovery finds a device
            if (action.equals(mBluetoothAdapter.ACTION_STATE_CHANGED)) {
                final int state = intent.getIntExtra(BluetoothAdapter.EXTRA_STATE, mBluetoothAdapter.ERROR);

                switch (state) {
                    case BluetoothAdapter.STATE_OFF:
                        Log.d(Constants.TAG, "onReceive: STATE OFF");
                        break;
                    case BluetoothAdapter.STATE_TURNING_OFF:
                        Log.d(Constants.TAG, "mBroadcastReceiver1: STATE TURNING OFF");
                        break;
                    case BluetoothAdapter.STATE_ON:
                        Log.d(Constants.TAG, "mBroadcastReceiver1: STATE ON");
                        break;
                    case BluetoothAdapter.STATE_TURNING_ON:
                        Log.d(Constants.TAG, "mBroadcastReceiver1: STATE TURNING ON");
                        break;
                }
            }
        }
    };

    private final BroadcastReceiver mBroadcastReceiver2 = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {
            final String action = intent.getAction();

            if (action.equals(BluetoothAdapter.ACTION_SCAN_MODE_CHANGED)) {

                int mode = intent.getIntExtra(BluetoothAdapter.EXTRA_SCAN_MODE, BluetoothAdapter.ERROR);

                switch (mode) {
                    //Device is in Discoverable Mode
                    case BluetoothAdapter.SCAN_MODE_CONNECTABLE_DISCOVERABLE:
                        Log.d(Constants.TAG, "mBroadcastReceiver2: Discoverability Enabled.");
                        break;
                    //Device not in discoverable mode
                    case BluetoothAdapter.SCAN_MODE_CONNECTABLE:
                        Log.d(Constants.TAG, "mBroadcastReceiver2: Discoverability Disabled. Able to receive connections.");
                        break;
                    case BluetoothAdapter.SCAN_MODE_NONE:
                        Log.d(Constants.TAG, "mBroadcastReceiver2: Discoverability Disabled. Not able to receive connections.");
                        break;
                    case BluetoothAdapter.STATE_CONNECTING:
                        Log.d(Constants.TAG, "mBroadcastReceiver2: Connecting....");
                        break;
                    case BluetoothAdapter.STATE_CONNECTED:
                        Log.d(Constants.TAG, "mBroadcastReceiver2: Connected.");
                        break;
                }

            }
        }
    };

    private final BroadcastReceiver mBroadcastReceiver3 = new BroadcastReceiver() {
        @RequiresApi(api = Build.VERSION_CODES.S)
        @Override
        public void onReceive(Context context, Intent intent) {
            final String action = intent.getAction();
            Log.d(Constants.TAG, "onReceive: ACTION FOUND.");

            assert action != null;
            if (action.equals(BluetoothDevice.ACTION_FOUND)) {
                final BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                mBTDevices.add(device);

                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
                    permissionCheck = checkSelfPermission(Manifest.permission.BLUETOOTH_CONNECT);
                }

                if (permissionCheck == PackageManager.PERMISSION_GRANTED) {
                    // Permission has been granted. You can proceed with your Bluetooth operation.
                } else {
                    // Permission has not been granted. Handle it accordingly, e.g., request the permission.
                }

                assert device != null;
                Log.d(Constants.TAG, "onReceive: " + device.getName() + ": " + device.getAddress());
                mDeviceListAdapter = new DeviceListAdapter(context, R.layout.activity_discover, mBTDevices);
                lvNewDevices.setAdapter(mDeviceListAdapter);

                lvNewDevices.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @RequiresApi(api = Build.VERSION_CODES.S)
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {

                        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
                            permissionCheck = checkSelfPermission(Manifest.permission.BLUETOOTH_CONNECT);
                        }

                        // Permission has been granted. You can proceed with your Bluetooth operation.
                        // Permission has not been granted. Handle it accordingly, e.g., request the permission.

                        mBluetoothAdapter.cancelDiscovery();

                        String deviceName = mBTDevices.get(position).getName();
                        deviceAddress = mBTDevices.get(position).getAddress();
                        mBTDevices.get(position).createBond();

                        new ConnectBT().execute(deviceAddress);

                    }
                });
            }
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
        drawerLayout = findViewById(R.id.drawer);
        navigationView = findViewById(R.id.navigation);

        mAuth = FirebaseAuth.getInstance();

        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();

        if (currentUser != null) {
            String uid = currentUser.getUid();

        }

        Intent intent = getIntent();

        String receivedText = intent.getStringExtra("phone");






        navigationView.setNavigationItemSelectedListener(this);
        toggle = new ActionBarDrawerToggle(this, drawerLayout, R.string.nav_open, R.string.nav_close);
        drawerLayout.addDrawerListener(toggle);

        toggle.syncState();

        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();

        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);

        loadFragment(new HomeFragment());

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setOnNavigationItemSelectedListener(this);
        intDrawerLayout(bottomNavigationView);

        requestQueue = Volley.newRequestQueue(this.getApplicationContext());
        simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());

        locationManager = (LocationManager) getApplicationContext().getSystemService(LOCATION_SERVICE);
        mBTDevices = new ArrayList<>();
    }

    Thread ListenInput = new Thread() {
        @Override
        public void run() {
            try {
                mmInStream = btSocket.getInputStream();
                byte[] buffer = new byte[1024];
                int bytes;
                while (true) {
                    // Read from the InputStream
                    try {
                        if (mmInStream == null) {
                            Log.d(Constants.TAG, "InputStream is null");
                        } else {
                            bytes = mmInStream.read(buffer);
                            incomingMessage = new String(buffer, 0, bytes);
                            messages.append(incomingMessage);
                            onSerialReceived(incomingMessage);
                            Log.d(Constants.TAG, "Incoming: " + incomingMessage);
                        }

                    } catch (IOException e) {
                        Log.d(Constants.TAG, "InputStream is null");
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }


        }
    };

    private class ConnectBT extends AsyncTask<String, String, String>  // UI thread
    {

        private boolean ConnectSuccess = true; //if it's here, it's almost connected

        @Override
        protected void onPreExecute() {
            messages = new StringBuilder();
            Toast.makeText(getApplicationContext(), "Подключение....", Toast.LENGTH_LONG).show();
        }

        @RequiresApi(api = Build.VERSION_CODES.S)
        @Override
        protected String doInBackground(String... devices) //while the progress dialog is shown, the connection is done in background
        {
            try {
                if (btSocket == null || !isBtConnected) {
                    ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.BLUETOOTH}, 1);
                    ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.BLUETOOTH_ADMIN}, 1);
                    ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.BLUETOOTH_PRIVILEGED}, 1);

                    if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
                        permissionCheck = checkSelfPermission(Manifest.permission.BLUETOOTH_CONNECT);
                    }

                    if (permissionCheck == PackageManager.PERMISSION_GRANTED) {
                        // Permission has been granted. You can proceed with your Bluetooth operation.
                    } else {
                        // Permission has not been granted. Handle it accordingly, e.g., request the permission.
                    }

                    BluetoothDevice dispositivo = mBluetoothAdapter.getRemoteDevice(devices[0]);

                    //connects to the device's address and checks if it's available
                    btSocket = dispositivo.createInsecureRfcommSocketToServiceRecord(myUUID);//create a RFCOMM (SPP) connection
                    BluetoothAdapter.getDefaultAdapter().cancelDiscovery();
                    btSocket.connect();//start connection
                }
            } catch (IOException e) {
                ConnectSuccess = false;//if the try failed, you can check the exception here
            }
            return null;
        }

        @Override
        protected void onPostExecute(String result) //after the doInBackground, it checks if everything went fine
        {
            super.onPostExecute(result);

            if (!ConnectSuccess) {
                Toast.makeText(getApplicationContext(), "Ошибка подключения", Toast.LENGTH_SHORT).show();
                materialStyledDialog.dismiss();
            } else {
                Toast.makeText(getApplicationContext(), "Подключение установлено", Toast.LENGTH_SHORT).show();
                isBtConnected = true;
                materialStyledDialog.dismiss();
                if (ListenInput.getState() == Thread.State.NEW) {
                    ListenInput.start();
                } else {
                    Toast.makeText(getApplicationContext(), "Ошибка запуска потока",
                            Toast.LENGTH_SHORT).show();
                }
            }

        }
    }

    private final LocationListener locationListener = new LocationListener() {
        @Override
        public void onLocationChanged(Location location) {
            gpsNetworkLocation = location;
        }

        @Override
        public void onStatusChanged(String s, int i, Bundle bundle) {
            if (s.equals(LocationManager.GPS_PROVIDER)) {
                Log.d(Constants.TAG, "GPS Location Status: " + i);
            } else if (s.equals(LocationManager.NETWORK_PROVIDER)) {
                Log.d(Constants.TAG, "Network Location Status: " + i);
            } else {
                Log.d(Constants.TAG, "GPS Error: " + i);
            }
        }

        @Override
        public void onProviderEnabled(String s) {
            boolean gps = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
            boolean network = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
            Log.d("info", "GPS enabled: " + gps + " NetworkLoc enabled: " + network);
        }

        @Override
        public void onProviderDisabled(String s) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                if (checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                        && checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(getApplicationContext(), "Please, provide all necessary permissions!", Toast.LENGTH_SHORT).show();
                    return;
                }
            }
            gpsNetworkLocation = locationManager.getLastKnownLocation(s);
        }
    };


    @Override
    public void onPause() {
        super.onPause();
        locationManager.removeUpdates(locationListener);
    }

    @RequiresApi(api = Build.VERSION_CODES.S)
    @SuppressLint("InflateParams")
    @Override
    public boolean onOptionsItemSelected(MenuItem menuItem) {


        if (menuItem != null) {
            if (toggle.onOptionsItemSelected(menuItem)) {
                return true;
            }


            if (menuItem.getItemId() == R.id.action_bluetooth) {
                if(mBluetoothAdapter == null){
                    Log.d(Constants.TAG, "enableDisableBT: Does not have BT capabilities.");
                }
                if(!mBluetoothAdapter.isEnabled()){

                    if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
                        permissionCheck = checkSelfPermission(Manifest.permission.BLUETOOTH_CONNECT);
                    }

                    // Permission has been granted. You can proceed with your Bluetooth operation.
                    // Permission has not been granted. Handle it accordingly, e.g., request the permission.

                    Log.d(Constants.TAG, "enableDisableBT: enabling BT.");
                    Intent enableBTIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                    startActivity(enableBTIntent);

                    IntentFilter BTIntent = new IntentFilter(BluetoothAdapter.ACTION_STATE_CHANGED);
                    registerReceiver(mBroadcastReceiver1, BTIntent);
                }

                LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                if (inflater != null) {
                    View customView = inflater.inflate(R.layout.custom_view, null);

                    init(customView);



                }
            }

            return super.onOptionsItemSelected(menuItem);
        }

        return false;
    }

    private boolean loadFragment(Fragment fragment) {

        if (fragment != null) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.fragment_container, fragment)
                    .commit();
            return true;
        }
        return false;
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {

        Fragment fragment = null;
        switch (item.getItemId()) {
            case R.id.action_home:
                fragment = new HomeFragment();
                break;
            case R.id.action_maps:
                Bundle bundle = new Bundle();

                if (gpsNetworkLocation != null) {
                    bundle.putDouble(Constants.LATITUDE, gpsNetworkLocation.getLatitude());
                    bundle.putDouble(Constants.LONGITUDE, gpsNetworkLocation.getLongitude());
                } else {
                    Toast.makeText(getApplicationContext(), "Ошибка получения текущего положения",
                            Toast.LENGTH_SHORT).show();
                }

                fragment = new MapsFragment();
                fragment.setArguments(bundle);
                break;
            case R.id.action_values:
                fragment = new DataFragment();
                break;
            case R.id.action_payment:
                Intent intent = new Intent(this, PaymentActivity.class);
                startActivity(intent);
                break;
            case R.id.action_message:
                 intent = new Intent(this, Message.class);
                startActivity(intent);
                break;
            case R.id.action_exit:
                //destroyData();
                mAuth.signOut();
                intent = new Intent(this, SMSnumber.class);
                startActivity(intent);
                finish();

            case R.id.action_menu:
                drawerLayout.openDrawer(Gravity.LEFT);
                break;
        }
        return loadFragment(fragment);
    }

    @Override
    public boolean onMenuItemClick(MenuItem item) {
        return false;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        FirebaseAuth auth = FirebaseAuth.getInstance();
        FirebaseUser user = auth.getCurrentUser();


        TextView email_header = findViewById(R.id.email);
        TextView phone_header = findViewById(R.id.phone);
        email_header.setText(user.getPhoneNumber());
        phone_header.setText(user.getPhoneNumber());

        getMenuInflater().inflate(R.menu.top_nav_menu, menu);

        IIcon[] topIcons = {
                CommunityMaterial.Icon.cmd_bluetooth_connect
        };

        renderMenuIcons(menu, topIcons, Color.WHITE, 22);

        return true;
    }

    private void intDrawerLayout(BottomNavigationView bottomNavigationView) {

        IIcon[] iconsBottom = {
                CommunityMaterial.Icon2.cmd_home,
                CommunityMaterial.Icon.cmd_google_maps,
                CommunityMaterial.Icon.cmd_chart_bell_curve,
                CommunityMaterial.Icon2.cmd_menu_open
        };

        renderMenuIcons(bottomNavigationView.getMenu(), iconsBottom, Color.WHITE, 22);

    }

    private void renderMenuIcons(Menu menu, IIcon[] icons, int color, int size) {

        for (int i = 0; i < menu.size(); i++) {
            MenuItem menuItem = menu.getItem(i);
            if (!menuItem.hasSubMenu()) {
                menuItem.setVisible(true);
                menuItem.setIcon(new IconicsDrawable(this)
                        .icon(icons[i])
                        .color(color)
                        .sizeDp(size));
            }
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.S)
    private void init(View view) {

        lvNewDevices = view.findViewById(R.id.lv_deviceList);
        lvNewDevices.setEmptyView(view.findViewById(R.id.pb_empty));
        swagLayout = view.findViewById(R.id.swagLayout);
        swagLayout.setVisibility(View.VISIBLE);
        swagLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {

            @Override
            public void onRefresh() {
                try {

                    if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
                        permissionCheck = checkSelfPermission(Manifest.permission.BLUETOOTH_CONNECT);
                    }

                    if (permissionCheck == PackageManager.PERMISSION_GRANTED) {
                        // Permission has been granted. You can proceed with your Bluetooth operation.
                    } else {
                        // Permission has not been granted. Handle it accordingly, e.g., request the permission.
                    }
                    mDeviceListAdapter.clear();
                    mBluetoothAdapter.startDiscovery();
                    swagLayout.setRefreshing(false);
                } catch (NullPointerException ex) {
                    Log.d(Constants.TAG, "Refresh NULL Exception");
                }
            }
        });
        if (mBluetoothAdapter.isDiscovering()) {

            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
                permissionCheck = checkSelfPermission(Manifest.permission.BLUETOOTH_CONNECT);
            }

            if (permissionCheck == PackageManager.PERMISSION_GRANTED) {
                // Permission has been granted. You can proceed with your Bluetooth operation.
            } else {
                // Permission has not been granted. Handle it accordingly, e.g., request the permission.
            }
            mBluetoothAdapter.cancelDiscovery();
            Log.d(Constants.TAG, "btnDiscover: Canceling discovery.");
            ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
            ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.ACCESS_COARSE_LOCATION}, 1);

            //check BT permissions in manifest

            mBluetoothAdapter.startDiscovery();

        }
        if (!mBluetoothAdapter.isDiscovering()) {
            ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
            ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.ACCESS_COARSE_LOCATION}, 1);

            //check BT permissions in manifest
            mBluetoothAdapter.startDiscovery();

        }
        IntentFilter discoverDevicesIntent = new IntentFilter(BluetoothDevice.ACTION_FOUND);
        registerReceiver(mBroadcastReceiver3, discoverDevicesIntent);
    }

    @RequiresApi(api = Build.VERSION_CODES.S)
    @Override
    protected void onDestroy() {
        super.onDestroy();
        try {

            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
                permissionCheck = checkSelfPermission(Manifest.permission.BLUETOOTH_CONNECT);
            }

            if (permissionCheck == PackageManager.PERMISSION_GRANTED) {
                // Permission has been granted. You can proceed with your Bluetooth operation.
            } else {
                // Permission has not been granted. Handle it accordingly, e.g., request the permission.
            }
            mBluetoothAdapter.cancelDiscovery();
            mDeviceListAdapter.clear();
            unregisterReceiver(mBroadcastReceiver3);

        } catch (NullPointerException ex) {
            Log.d(Constants.TAG, ex.toString());
        }
    }

    void destroyData(){
        Log.i("bb", "close");
        /*signin = "SignOut";

        File file= new File("/data/data/www.kaznu.kz.projects.ibox/shared_prefs/activities.LoginActivity.xml");
        file.delete();
        startActivity(new Intent(getBaseContext(), LoginActivity.class));*/

        mAuth.signOut();
        startActivity(new Intent(MainActivity.this, AuthenticationActivity.class));
        finish();

    }

    public void onSerialReceived(String theString) {
        //Log.d("info", theString);
        wholeString = wholeString + theString;

        try {
            if (wholeString.length() < 54) return;
            String json = parseAJson(wholeString);

            JSONObject jsonObject = new JSONObject(json);

            changeViewData(jsonObject);

            sendData(jsonObject, gpsNetworkLocation);

            wholeString = wholeString.substring(wholeString.indexOf(json) + json.length());
        } catch (Exception ex) {
            Log.d(Constants.TAG, ex.toString());
        }
    }

    private String parseAJson(String data) {
        List<Character> chars = new ArrayList<>();
        chars.add('{');

        for (int i = 0; i < data.length(); i++) {
            if (data.charAt(i) == '{') {
                i++;
                for (; i < data.length(); i++) {
                    chars.add(data.charAt(i));

                    if (data.charAt(i) == '{') {
                        chars = new ArrayList<>();
                        chars.add('{');
                    } else if (data.charAt(i) == '}') {
                        break;
                    }
                }
            }
        }

        StringBuilder json = new StringBuilder();
        for (int i = 0; i < chars.size(); i++) {
            json.append(chars.get(i));
        }

        Log.d(Constants.TAG, "Parsed data: " + json.toString());

        return json.toString();
    }

    private void sendData(final JSONObject jsonObject, final Location location) {

        Log.d(Constants.TAG, "sendData: location is: " + location);

        if (jsonObject == null || location == null) return;

        final Date currentTime = Calendar.getInstance().getTime();

        final Map<String, String> params = new HashMap<>();
        try {
            params.put("device", LoginActivity.Ldev);
            params.put("temperature", (int) jsonObject.getDouble("T") + "");
            params.put("altitude", (int) jsonObject.getDouble("A") + "");
            params.put("pressure", (int) jsonObject.getDouble("P") + "");
            params.put("humidity", (int) jsonObject.getDouble("H") + "");
            params.put("aqi", (int) jsonObject.getDouble("AQI") + "");
            params.put("pm1", (int) jsonObject.getDouble("PM1.0") + "");
            params.put("pm25", (int) jsonObject.getDouble("PM2.5") + "");
            params.put("pm10", (int) jsonObject.getDouble("PM10") + "");
            params.put("time", simpleDateFormat.format(currentTime));
            params.put("longitude", location.getLongitude() + "");
            params.put("latitude", location.getLatitude() + "");

        } catch (JSONException e) {
            Log.d(Constants.TAG, "getParams: " + e.getMessage());
        }

        if (params.size() < 11) return;

        StringRequest stringRequest = new StringRequest(
                Request.Method.POST,
                Constants.POST_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d(Constants.TAG, "onResponse: " + response);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d("info", "onErrorResponse code: " + error);
                    }
                }) {
            @Override
            protected Map<String, String> getParams() {
                return params;
            }
        };
        requestQueue.add(stringRequest);
    }

    @Override
    public void onResume() {
        super.onResume();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                    && checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

                Toast.makeText(getApplicationContext(), "Please, provide all necessary permissions!", Toast.LENGTH_SHORT).show();
                //requestAllPermission();
                return;
            }
        }
        locationManager.requestLocationUpdates(
                LocationManager.NETWORK_PROVIDER,
                1000 * 10,
                10,
                locationListener);
    }

    private void changeViewData(JSONObject jsonObject) {
        try {
            final double T = jsonObject.getDouble("T");
            final double P = jsonObject.getDouble("P");
            final double H = jsonObject.getDouble("H");
            double A = jsonObject.getDouble("A");
            final int AQI = jsonObject.getInt("AQI");
            final double PM_1_0 = jsonObject.getDouble("PM1.0");
            final double PM_2_5 = jsonObject.getDouble("PM2.5");
            final double PM_10 = jsonObject.getDouble("PM10");

            final DecimalFormat df = new DecimalFormat("#.##");
            final DecimalFormat dfOne = new DecimalFormat("#");

            final HomeFragment homeFragment = (HomeFragment) getSupportFragmentManager().
                    findFragmentById(R.id.fragment_container);

            String aqiState = "Умеренно";
            final int aqiColor;

            if(AQI > -1 && AQI <= 51) {
                aqiState = "Хорошо";
                aqiColor = this.getResources().getColor(R.color.aqiGood);
            }
            else if(AQI > 50 && AQI < 101) {
                aqiState = "Умеренно";
                aqiColor = this.getResources().getColor(R.color.aqiModerate);
            }
            else if(AQI > 100 && AQI < 151) {
                aqiState = "Умеренно\nвредный";
                aqiColor = this.getResources().getColor(R.color.aqiModerateUnhealthy);
            }else if(AQI > 150 && AQI < 201) {
                aqiState = "Вредный";
                aqiColor = this.getResources().getColor(R.color.aqiUnhealthy);
            }else if(AQI > 200 && AQI < 301) {
                aqiState = "Очень\nвредный";
                aqiColor = this.getResources().getColor(R.color.aqiVeryUnhealthy);
            }else {
                aqiState = "Опасный";
                aqiColor = this.getResources().getColor(R.color.aqiHazardous);
            }

            if (homeFragment != null) {
                final String finalAqiState = aqiState;
                MainActivity.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        homeFragment.aqiMessage.setMinLines(2);
                        homeFragment.aqiMessage.setTextColor(aqiColor);
                        homeFragment.aqiValue.setTextColor(aqiColor);
                        homeFragment.aqiMessage.setText(finalAqiState);
                        homeFragment.aqiValue.setText(String.valueOf(AQI));
                        homeFragment.tempValue.setText(df.format(T) + " °C");
                        homeFragment.humidityValue.setText(df.format(H) + " %");
                        homeFragment.pm25Value.setText(dfOne.format(PM_2_5));
                        homeFragment.pm10Value.setText(dfOne.format(PM_10));
                        homeFragment.pm01Value.setText(dfOne.format(PM_1_0));
                        homeFragment.pressureValue.setText(dfOne.format(P / 10.0));
                    }
                });

            }


        } catch (Exception ex) {
            Log.d(Constants.TAG, "changeViewData: " + ex.getMessage());
        }
    }
}
