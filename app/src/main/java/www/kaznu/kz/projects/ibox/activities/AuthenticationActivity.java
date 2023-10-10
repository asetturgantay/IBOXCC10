package www.kaznu.kz.projects.ibox.activities;

import static www.kaznu.kz.projects.ibox.utils.Constants.TAG;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseException;
import com.google.firebase.FirebaseTooManyRequestsException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthMissingActivityForRecaptchaException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthOptions;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;


import java.util.concurrent.TimeUnit;

import www.kaznu.kz.projects.ibox.R;

public class AuthenticationActivity extends AppCompatActivity {

    Button btn_sentSMS, btn_submit;
    String number_phone;
    EditText SMScode;

    FirebaseAuth mAuth;

    TextView textSMS, textSet;



    String verificationID;


    FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
    FirebaseUser currentUser = firebaseAuth.getCurrentUser();








    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_authentication);

        FirebaseApp.initializeApp(this);


        if (currentUser != null) {
            String uid = currentUser.getUid();
        }






        FirebaseApp.initializeApp(this);

        mAuth = FirebaseAuth.getInstance();

        Intent intent = getIntent();

        String receivedText = intent.getStringExtra("phone");



        btn_submit = findViewById(R.id.btn_submit);
        btn_sentSMS = findViewById(R.id.btn_sentSMS);
        number_phone = receivedText.toString();
        SMScode = findViewById(R.id.SMScode);
        textSMS = findViewById(R.id.textSMS);
        textSet = findViewById(R.id.textSet);

        textSet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(AuthenticationActivity.this, SMSnumber.class));
            }
        });




        if (number_phone != null){
            String number = number_phone;
            textSMS.setText("Мы отправили вам CMC код: " + number);
            sentVertificationcode(number);

        }else{
            startActivity(new Intent(AuthenticationActivity.this, SMSnumber.class));
        }




        btn_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (TextUtils.isEmpty(SMScode.getText().toString()))
                {
                    Toast.makeText(AuthenticationActivity.this, "Напишите SMS code", Toast.LENGTH_SHORT).show();
                }else {
                    vertificode(SMScode.getText().toString());
                }

            }
        });
    }

    private void sentVertificationcode(String number){


        PhoneAuthOptions options =
                PhoneAuthOptions.newBuilder(mAuth)
                        .setPhoneNumber(number)       // Phone number to verify
                        .setTimeout(60L, TimeUnit.SECONDS) // Timeout and unit
                        .setActivity(this)                 // (optional) Activity for callback binding
                        // If no activity is passed, reCAPTCHA verification can not be used.
                        .setCallbacks(mCallbacks)          // OnVerificationStateChangedCallbacks
                        .build();
        PhoneAuthProvider.verifyPhoneNumber(options);
    }

    private final PhoneAuthProvider.OnVerificationStateChangedCallbacks
    mCallbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

        @Override
        public void onVerificationCompleted(@NonNull PhoneAuthCredential credential)
        {
            final String code = credential.getSmsCode();

            if (code!= null){
                vertificode(code);
            }
        }

        @Override
        public void onVerificationFailed(@NonNull FirebaseException e) {

            Toast.makeText(AuthenticationActivity.this, "Vertification Failed", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(AuthenticationActivity.this, SMSnumber.class));

        }

        @Override
        public void onCodeSent(@NonNull String s,
                @NonNull PhoneAuthProvider.ForceResendingToken token) {

            super.onCodeSent(s, token);
            verificationID = s;
        }
    };


    private void vertificode(String code){

        PhoneAuthCredential credential = PhoneAuthProvider.getCredential(verificationID, code);
        signinbyCredential(credential);

    }

    private void signinbyCredential(PhoneAuthCredential credential) {
        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        firebaseAuth.signInWithCredential(credential)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task)
                    {
                        if (task.isSuccessful()){
                            Toast.makeText(AuthenticationActivity.this, "Login Succesful", Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(AuthenticationActivity.this, MainActivity.class));
                        }
                    }
                });
    }


    @Override
    protected void onStart() {
        super.onStart();

        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        if (currentUser!=null){
            startActivity(new Intent(AuthenticationActivity.this, MainActivity.class));
            finish();
        }
    }


}