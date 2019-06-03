package com.example.project8;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.Map;

public class MainActivity extends AppCompatActivity {
    private EditText email;
    private EditText password;
    private JSONObject body = new JSONObject();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void login(View view){
        email = findViewById(R.id.email);
        password = findViewById(R.id.password);

        try {
            body.put("email", email.getText().toString());
            body.put("password", password.getText().toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }

        final RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
        ObjectMapper mapper = new ObjectMapper();

        StringRequest request = new StringRequest(Request.Method.POST, "http://128.195.65.55:2853/api/idm/login",
                response ->{
                    if(response != null && !response.equals("") ){
                        Map<String, Object> map = null;
                        try {
                            map = mapper.readValue(response, Map.class);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        System.out.println(map.get("resultCode"));

                        if((Integer)map.get("resultCode") == 120){
                            Intent myIntent = new Intent(MainActivity.this, SearchActivity.class);
                            MainActivity.this.startActivity(myIntent);

                        }else{
                            Toast.makeText(getApplicationContext(),(String)map.get("message"),Toast.LENGTH_SHORT).show();
                        }

                    }else{
                        Toast.makeText(getApplicationContext(), "Login Fail", Toast.LENGTH_SHORT).show();
                    }
                }, error ->{
            Toast.makeText(getApplicationContext(), error.toString(), Toast.LENGTH_LONG).show();

        }){
            @Override
            public byte[] getBody() throws AuthFailureError {
                return body.toString().getBytes();
            }

            @Override
            public String getBodyContentType() {
                return "application/json";
            }
        };

        queue.add(request);


    }
}
