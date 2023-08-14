package com.example.calculator;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.content.Intent;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import org.json.JSONException;
import org.json.JSONObject;

import androidx.appcompat.app.AppCompatActivity;

public class ResultActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);

        Intent intent = getIntent();
        String name = intent.getStringExtra("name");
        int gender = intent.getIntExtra("gender", 0);
        int age = intent.getIntExtra("age", 0);
        double height = intent.getDoubleExtra("height", 0.0);
        double weight = intent.getDoubleExtra("weight", 0.0);
        double bmr = intent.getDoubleExtra("bmr", 0.0);
        double bmi = intent.getDoubleExtra("bmi", 0.0);

        TextView nameTextView = findViewById(R.id.nametext);
        TextView bmrTextView = findViewById(R.id.bmrtext);
        TextView bmiTextView = findViewById(R.id.bmitext);
        Button button4 = findViewById(R.id.button4);

        nameTextView.setText(name);
        bmrTextView.setText(String.format("%.2f", bmr)); // Display BMR with two decimal places
        bmiTextView.setText(String.format("%.2f", bmi)); // Display BMR with two decimal places

        button4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                uploadDataToServer(name, gender, weight, height, age, bmi, bmr);
                // Start MainActivity or any other desired action
                Intent intent = new Intent(ResultActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });
    }

    private void uploadDataToServer(String name, int gender, double weight, double height, int age, double bmi, double bmr) {
        String url = "http://192.168.50.252/insert_record.php";

        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("name", name);
            jsonObject.put("gender", gender);
            jsonObject.put("weight", weight);
            jsonObject.put("height", height);
            jsonObject.put("age", age);
            jsonObject.put("bmi", bmi);
            jsonObject.put("bmr", bmr);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        JsonObjectRequest request = new JsonObjectRequest(
                Request.Method.POST, url, jsonObject,
                response -> {
                    // Handle successful response
                    try {
                        String responseMessage = response.getString("message");
                        if ("success".equals(responseMessage)) {
                            // Successfully inserted or updated
                        } else {
                            // Handle other responses or errors
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                },
                error -> Log.e("VolleyError", error.toString())
        );

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(request);
    }
}