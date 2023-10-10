package www.kaznu.kz.projects.ibox.activities;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Objects;

import es.dmoral.toasty.Toasty;
import www.kaznu.kz.projects.ibox.R;

public class RegistrationActivity extends AppCompatActivity implements View.OnClickListener {

    Button btnLogin;
    TextView btnRegistration;
    EditText etLogin, etPassword, etEmail, etPhoneNum, etName;
    private RequestQueue mQueue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);


        btnLogin   = findViewById(R.id.login_btn);
        etLogin    = findViewById(R.id.et_login);
        etPassword = findViewById(R.id.et_password);
        etEmail = findViewById(R.id.et_email);
        etName = findViewById(R.id.et_name);
        etPhoneNum = findViewById(R.id.et_phoneNum);

        btnRegistration = findViewById(R.id.login_registration);


        btnLogin.setOnClickListener(this);
        mQueue = Volley.newRequestQueue(this);



        btnRegistration.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getBaseContext(), LoginActivity.class));
            }
        });




    }
    @Override
    public void onClick(View v) {
        if(v.getId() == R.id.login_btn)
        {
            if(!TextUtils.isEmpty(etLogin.getText().toString()) &&
                    !TextUtils.isEmpty(etPassword.getText().toString())&&
                    !TextUtils.isEmpty(etName.getText().toString())&&
                    !TextUtils.isEmpty(etEmail.getText().toString())&&
                    !TextUtils.isEmpty(etPhoneNum.getText().toString()))
            {
                JSONObject jsonBody = new JSONObject();
                try {
                    jsonBody.put("login", etLogin.getText().toString());
                    jsonBody.put("name", etName.getText().toString());
                    jsonBody.put("password", etPassword.getText().toString());
                    jsonBody.put("phoneNum", etPhoneNum.getText().toString());
                    jsonBody.put("email", etEmail.getText().toString());
                } catch (Exception e) {
                    e.printStackTrace();
                    Log.e("bb", "Error creating JSON object");
                }

                RequestQueue mQueue = Volley.newRequestQueue(this);


                String url = "http://a0868210.xsph.ru/AndroidApi/volley/insertdata.php";


                JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, url, jsonBody,
                        new Response.Listener<JSONObject>() {
                            @Override
                            public void onResponse(JSONObject response) {
                                // Handle a successful response here
                                Log.i("bb", String.valueOf(response));
                                Toasty.warning(getBaseContext(), "Успешно зарегистрированы", Toast.LENGTH_SHORT, true).show();
                                startActivity(new Intent(getBaseContext(), LoginActivity.class));
                            }
                        },
                        new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                // Handle an error response here
                                error.printStackTrace();
                                Toasty.warning(getBaseContext(), Objects.requireNonNull(error.getMessage()), Toast.LENGTH_SHORT, true).show();

                                Toasty.warning(getBaseContext(), "Успешно зарегистрированы", Toast.LENGTH_SHORT, true).show();
                                startActivity(new Intent(getBaseContext(), LoginActivity.class));
                            }
                        });



                mQueue.add(request);
            }
            else {
                Toasty.warning(this, "Введите логин, пароль, имя, номер, email!", Toast.LENGTH_SHORT, true).show();
            }
        }



    }



}
