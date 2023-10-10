package www.kaznu.kz.projects.ibox.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import es.dmoral.toasty.Toasty;
import www.kaznu.kz.projects.ibox.R;
import www.kaznu.kz.projects.ibox.databinding.ActivityLoginBinding;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {


    Button btnLogin;
    TextView btnRegistration;
    EditText etLogin, etPassword;
    public String login, password, email, phoneNum, dev;
    public int devInt;
    public static String Llogin, Lpassword, Lemail, Lphone, Ldev;

    SharedPreferences sPref;
    final String P_LOGIN = "login";
    final String P_PASSWORD = "password";
    final String P_EMAIL = "email";
    final String P_PHONE = "phone";
    final String P_DEV = "dev";

    private RequestQueue mQueue;

    ActivityLoginBinding binding;
    SharedPreferences sharedPreferences ;
    SharedPreferences.Editor editor;

    // you have to use your IP address


    private Button loginButton;
    private static final String LOGIN_URL = "http://a0868210.xsph.ru/AndroidApi/volley/login.php"; // Replace with your PHP server URL

    String url = "http://192.161.44.11/AndroidApi/volley/login.php";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        btnRegistration = findViewById(R.id.login_registration);
        loginButton = findViewById(R.id.login_btn);

        etLogin = findViewById(R.id.et_login);
        etPassword = findViewById(R.id.et_password);


        btnRegistration.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(LoginActivity.this , RegistrationActivity.class));
            }
        });

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                    userLogin();

            }
        });


    }

    private void userLogin() {
        final String username = etLogin.getText().toString();
        final String password = etPassword.getText().toString();

        StringRequest stringRequest = new StringRequest(Request.Method.POST, LOGIN_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonResponse = new JSONObject(response);
                            boolean success = jsonResponse.getBoolean("success");

                            if (success) {
                                // Login successful
                                Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                                startActivity(intent);
                            } else {
                                // Login failed
                                Toast.makeText(LoginActivity.this, "Не верный логин или пароль", Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(LoginActivity.this, "JSON Error: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(LoginActivity.this, "Volley Error: " + error.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("username", username);
                params.put("password", password);
                return params;
            }
        };

        Volley.newRequestQueue(this).add(stringRequest);
    }


    @Override
    public void onClick(View v) {

    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {
        super.onPointerCaptureChanged(hasCapture);
    }
}

