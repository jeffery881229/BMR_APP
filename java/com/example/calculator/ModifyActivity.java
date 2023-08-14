package com.example.calculator;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ListView;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import android.content.DialogInterface;
import android.widget.TextView;

public class ModifyActivity extends AppCompatActivity {
    private EditText nameTextView;
    private EditText enterHeight;
    private EditText enterAge;
    private EditText enterWeight;
    private CheckBox maleCheck;
    private CheckBox femaleCheck;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modify);

        nameTextView = findViewById(R.id.entername2);
        enterHeight = findViewById(R.id.enterheight2);
        enterAge = findViewById(R.id.enterage2);
        enterWeight = findViewById(R.id.enterweight2);
        maleCheck = findViewById(R.id.malecheck2);
        femaleCheck = findViewById(R.id.femalecheck2);

        // Set listeners for checkboxes to manage exclusivity
        maleCheck.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (maleCheck.isChecked()) {
                    femaleCheck.setChecked(false);
                }
            }
        });

        femaleCheck.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (femaleCheck.isChecked()) {
                    maleCheck.setChecked(false);
                }
            }
        });

        // Get the data passed from MainActivity
        Intent intent = getIntent();
        String selectedName = intent.getStringExtra("name");
        String selectedBmr = intent.getStringExtra("bmr");

        // Display the data in the appropriate views
        nameTextView.setText(selectedName);

        // Fetch additional data from MySQL based on the selectedName
        fetchDataFromServer(selectedName);

        Button sendButton = findViewById(R.id.send2);
        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String enteredName = ((EditText) findViewById(R.id.entername2)).getText().toString();
                // Retrieve age, weight, height, gender values
                int age = Integer.parseInt(((EditText) findViewById(R.id.enterage2)).getText().toString());
                double weight = Double.parseDouble(((EditText) findViewById(R.id.enterweight2)).getText().toString());
                double height = Double.parseDouble(((EditText) findViewById(R.id.enterheight2)).getText().toString());
                int gender = ((CheckBox) findViewById(R.id.malecheck2)).isChecked() ? 1 : 0; // 1 for Male, 0 for Female

                // Calculate BMR
                double bmr = 9.99 * weight + 6.25 * height - 4.92 * age + (166 * gender - 161);
                double bmi = weight / Math.pow(height / 100, 2);

                Intent intent = new Intent(ModifyActivity.this, ResultActivity.class);
                intent.putExtra("name", enteredName);
                intent.putExtra("bmr", bmr);
                intent.putExtra("bmi", bmi);
                intent.putExtra("age", age);
                intent.putExtra("weight", weight);
                intent.putExtra("height", height);
                intent.putExtra("gender", gender);
                startActivity(intent);
            }
        });

        Button cancelButton = findViewById(R.id.button2);
        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Simply finish the current activity to navigate back
                finish();
            }
        });
    }

    private void fetchDataFromServer(String name) {
        String url = "http://192.168.50.252/read_record.php"; // URL to retrieve data
        JsonArrayRequest request = new JsonArrayRequest(
                Request.Method.GET, url, null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        try {
                            for (int i = 0; i < response.length(); i++) {
                                JSONObject item = response.getJSONObject(i);
                                String itemName = item.getString("name");
                                if (itemName.equals(name)) {
                                    // Extract data for the selected item
                                    String gender = item.getString("gender");
                                    String age = item.getString("age");
                                    String height = item.getString("height");
                                    String weight = item.getString("weight");

                                    // Set data to the views
                                    enterAge.setText(age);
                                    enterHeight.setText(height);
                                    enterWeight.setText(weight);

                                    // Convert gender string to integer
                                    int genderValue = Integer.parseInt(gender);
                                    // Update gender checkboxes
                                    if (genderValue == 1) {
                                        maleCheck.setChecked(true);
                                        femaleCheck.setChecked(false);
                                    } else if (genderValue == 0) {
                                        maleCheck.setChecked(false);
                                        femaleCheck.setChecked(true);
                                    }

                                    break; // No need to continue searching
                                }
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        error.printStackTrace();
                    }
                }
        );

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(request);
    }

}

