package www.kaznu.kz.projects.ibox.activities;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.content.ContextCompat;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import www.kaznu.kz.projects.ibox.R;

public class Message extends AppCompatActivity {

    private static final int SMS_PERMISSION_REQUEST_CODE = 101;
    Button btn;

    Integer temp;

    TextView temperature;




    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message);

        temperature = findViewById(R.id.temperature);


        RequestQueue requestQueue = Volley.newRequestQueue(this);
        String url = "http://a0868210.xsph.ru/AndroidApi/volley/get_last_id.php"; // Замените на URL вашего сервера и скрипта PHP

        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @SuppressLint("SetTextI18n")
                    @Override
                    public void onResponse(String response) {
                        try {

                            double lastTempValue = Integer.parseInt(response);
                            System.out.println("Последнее значение tempValue: " + lastTempValue);
                            Log.d("tag", String.valueOf(lastTempValue));

                            temp = (int) lastTempValue;


                            temperature.setText(temp + "°C");

                            Toast.makeText(Message.this, temp.toString(), Toast.LENGTH_SHORT).show();



                            mess();

                        } catch (NumberFormatException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                // Обработка ошибки запроса
                error.printStackTrace();
            }
        });

        requestQueue.add(stringRequest);











    }

    private void mess() {
        if (temp <= 17) {

            if (ContextCompat.checkSelfPermission(this, Manifest.permission.SEND_SMS)
                    != PackageManager.PERMISSION_GRANTED) {

                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.SEND_SMS},
                        SMS_PERMISSION_REQUEST_CODE);
            } else {

                sendSMS();
                notificat();

            }
        }
    }



    private void notificat() {

            NotificationChannel channel = null;
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                channel = new NotificationChannel(
                        "TEST_CHANEl",
                        "TEST_DESCRIPTION",
                        NotificationManager.IMPORTANCE_DEFAULT);
                NotificationManager notificationManager = getSystemService(NotificationManager.class);
                notificationManager.createNotificationChannel(channel);

                Notification notification = new NotificationCompat.Builder(Message.this, "TEST_CHANEl")
                        .setContentTitle("Уведомление")
                        .setContentText("Температура ровна " + temp + "°C")
                        .setSmallIcon(R.drawable.ic_teamwork)
                        .build();
                notificationManager.notify(42, notification);
            }
        }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == SMS_PERMISSION_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                sendSMS();
            } else {

                Toast.makeText(this, "Разрешение на отправку SMS не предоставлено", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void sendSMS() {
        FirebaseAuth auth = FirebaseAuth.getInstance();
        FirebaseUser user = auth.getCurrentUser();

        String phoneNumber = user.getPhoneNumber();
        String message = "Температура ровна " + temp + " °C";

        try {
            SmsManager smsManager = SmsManager.getDefault();
            smsManager.sendTextMessage(phoneNumber, null, message, null, null);
            Toast.makeText(this, "SMS успешно отправлено", Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            Toast.makeText(this, "Ошибка при отправке SMS: " + e.getMessage(), Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
    }
}
