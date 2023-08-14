package com.example.calculator;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.content.Intent;
import android.widget.CheckBox;
import androidx.appcompat.app.AppCompatActivity;

public class CreateActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create);

        CheckBox maleCheck = findViewById(R.id.malecheck);
        CheckBox femaleCheck = findViewById(R.id.femalecheck);

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

        Button sendButton = findViewById(R.id.send);
        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String enteredName = ((EditText) findViewById(R.id.entername)).getText().toString();
                // Retrieve age, weight, height, gender values
                int age = Integer.parseInt(((EditText) findViewById(R.id.enterage)).getText().toString());
                double weight = Double.parseDouble(((EditText) findViewById(R.id.enterweight)).getText().toString());
                double height = Double.parseDouble(((EditText) findViewById(R.id.enterheight)).getText().toString());
                int gender = ((CheckBox) findViewById(R.id.malecheck)).isChecked() ? 1 : 0; // 1 for Male, 0 for Female

                // Calculate BMR
                double bmr = 9.99 * weight + 6.25 * height - 4.92 * age + (166 * gender - 161);
                double bmi = weight / Math.pow(height / 100, 2);

                Intent intent = new Intent(CreateActivity.this, ResultActivity.class);
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

        Button cancelButton = findViewById(R.id.cancelbutton);
        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Simply finish the current activity to navigate back
                finish();
            }
        });

    }
}