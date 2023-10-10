package www.kaznu.kz.projects.ibox.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import www.kaznu.kz.projects.ibox.R;

public class SMSnumber extends AppCompatActivity {


    Button sentSMS;
    EditText number;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_smsnumber);

        sentSMS = findViewById(R.id.btn_sentSMS);
        number = findViewById(R.id.number_phone);


        sentSMS.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

               if (number != null){
                   Intent intent = new Intent(SMSnumber.this, AuthenticationActivity.class);

                   intent.putExtra("phone", number.getText().toString());

                   startActivity(intent);
               }else {
                   Toast.makeText(SMSnumber.this, "Напишите номер телефона", Toast.LENGTH_SHORT).show();
               }

            }
        });


    }

    @Override
    protected void onStart() {
        super.onStart();
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        if (currentUser!=null){
            startActivity(new Intent(SMSnumber.this, MainActivity.class));
            finish();
        }
    }
}