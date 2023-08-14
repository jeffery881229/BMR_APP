package com.example.calculator;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
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

import java.util.ArrayList;


public class MainActivity extends AppCompatActivity {
    private Button create_btn;
    private ListView listView;
    private CustomListAdapter adapter;
    private final ArrayList<String> names = new ArrayList<>();
    private final ArrayList<String> bmrs = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        listView = findViewById(R.id.listView);

        // Initialize the adapter and set it to the ListView
        adapter = new CustomListAdapter(this, names, bmrs);
        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // Handle item click event
                String selectedName = names.get(position);
                String selectedBmr = bmrs.get(position);

                // Create an Intent to open ModifyActivity
                Intent intent = new Intent(MainActivity.this, ModifyActivity.class);
                intent.putExtra("name", selectedName);
                intent.putExtra("bmr", selectedBmr);
                startActivity(intent);
            }
        });


        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                // Show an AlertDialog to confirm the delete action
                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                builder.setTitle("Confirm Delete")
                        .setMessage("Are you sure you want to delete this item?")
                        .setPositiveButton("Delete", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                String nametodelete = names.get(position);

                                // Call a method to delete the corresponding data from MySQL
                                deleteDataFromServer(nametodelete);

                                // Call the removeItem method of the adapter to remove the selected item
                                adapter.removeItem(position);
                            }
                        })
                        .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                // User clicked Cancel, do nothing
                            }
                        })
                        .show();

                return true;
            }
        });



        create_btn = findViewById(R.id.create_btn);
        create_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openCreateActivity();
            }
        });

        fetchDataFromServer();
    }

    private void fetchDataFromServer() {
        String url = "http://192.168.50.252/read_record.php";

        JsonArrayRequest request = new JsonArrayRequest(
                Request.Method.GET, url, null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        try {
                            for (int i = 0; i < response.length(); i++) {
                                JSONObject item = response.getJSONObject(i);
                                String name = item.getString("name");
                                String bmr = item.getString("bmr");
                                names.add(name);
                                bmrs.add(bmr);
                            }
                            adapter.notifyDataSetChanged();
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

    private void deleteDataFromServer(String nameToDelete) {
        int position = names.indexOf(nameToDelete);
        System.out.println("Name to delete: " + position + " " + nameToDelete);
        if (position >= 0) {
            String url = "http://192.168.50.252/delete_record.php"; // URL for deleting record
            JSONObject jsonObject = new JSONObject();

            try {
                jsonObject.put("name", nameToDelete);
            } catch (JSONException e) {
                e.printStackTrace();
            }

            JsonObjectRequest request = new JsonObjectRequest(
                    Request.Method.POST, url, jsonObject,
                    response -> {
                        // Handle successful response
                        System.out.println("Response: " + response.toString()); // Print the response
                    },
                    error -> {
                        // Handle error
                        error.printStackTrace(); // Print the error stack trace
                    }
            );

            RequestQueue requestQueue = Volley.newRequestQueue(this);
            requestQueue.add(request);
        }
    }



    public void openCreateActivity() {
        Intent intent = new Intent(this, CreateActivity.class);
        startActivity(intent);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && requestCode == 1) {
            String name = data.getStringExtra("name");
            String bmr = data.getStringExtra("bmr");

            if (name != null && bmr != null) {
                names.add(name);
                bmrs.add(bmr);

                // Check if the adapter is not null before calling notifyDataSetChanged
                if (adapter != null) {
                    adapter.notifyDataSetChanged();
                }
            }
        }
    }
}