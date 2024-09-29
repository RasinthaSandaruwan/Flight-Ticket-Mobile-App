package com.example.myapplication.Activity;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.example.myapplication.LocationData;
import com.example.myapplication.R;
import com.example.myapplication.databinding.ActivityMainBinding;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

public class MainActivity extends BaseActivity {

    private ActivityMainBinding binding;
    private DatabaseReference database;
    private ArrayList<LocationData> locationList;

    private int adultPassenger = 1, childPassenger = 1;
    private SimpleDateFormat dateFormat = new SimpleDateFormat("d,MM,yyyy", Locale.ENGLISH);
    private final Calendar calendar = Calendar.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Initialize Firebase
        database = FirebaseDatabase.getInstance().getReference();

        // Initialize the UI components and data
        initLocations();
        initPassengers();
        initDatePickup();
        setVariable();
    }

    private void setVariable() {
        binding.SearchBtn.setOnClickListener(v -> {
            if (locationList != null && !locationList.isEmpty()) {
                Intent intent = new Intent(MainActivity.this, SearchActivity.class);
                intent.putExtra("from", ((LocationData) binding.fromsp.getSelectedItem()).getName());
                intent.putExtra("to", ((LocationData) binding.Tosp.getSelectedItem()).getName());
                intent.putExtra("date", binding.textView21.getText().toString());
                intent.putExtra("numPassenger", adultPassenger + childPassenger);
                startActivity(intent);
            } else {
                Toast.makeText(MainActivity.this, "Location data not available", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void initDatePickup() {
        Calendar calendarToday = Calendar.getInstance();
        String currentDate = dateFormat.format(calendarToday.getTime());
        binding.textView21.setText(currentDate);

        Calendar calendarTomorrow = Calendar.getInstance();
        calendarTomorrow.add(Calendar.DAY_OF_YEAR, 1);
        String tomorrowDate = dateFormat.format(calendarTomorrow.getTime());
        binding.textView24.setText(tomorrowDate);

        binding.textView21.setOnClickListener(v -> showDatePickerDialog(binding.textView21));
        binding.textView24.setOnClickListener(v -> showDatePickerDialog(binding.textView24));
    }

    private void initPassengers() {
        binding.textView14.setOnClickListener(v -> {
            adultPassenger++;
            binding.textView12.setText(adultPassenger + " Adult");
        });

        binding.textView13.setOnClickListener(v -> {
            if (adultPassenger > 1) {
                adultPassenger--;
                binding.textView12.setText(adultPassenger + " Adult");
            }
        });

        binding.textView15.setOnClickListener(v -> {
            childPassenger++;
            binding.textView10.setText(childPassenger + " Child");
        });

        binding.textView16.setOnClickListener(v -> {
            if (childPassenger > 1) {
                childPassenger--;
                binding.textView10.setText(childPassenger + " Child");
            }
        });
    }

    private void initLocations() {
        // Show progress bars while data is loading
        binding.progressBar.setVisibility(View.VISIBLE);
        binding.progressBar2.setVisibility(View.VISIBLE);

        DatabaseReference myRef = database.child("Locations");
        locationList = new ArrayList<>();

        // Fetch data from Firebase
        myRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    for (DataSnapshot issue : snapshot.getChildren()) {
                        try {
                            LocationData locationData = issue.getValue(LocationData.class);
                            if (locationData != null) {
                                locationList.add(locationData);
                            }
                        } catch (Exception e) {
                            Log.e("MainActivity", "Error parsing location data", e);
                        }
                    }

                    // Populate the spinners with the location data
                    ArrayAdapter<LocationData> adapter = new ArrayAdapter<>(MainActivity.this, R.layout.sp_item, locationList);
                    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    binding.fromsp.setAdapter(adapter);
                    binding.Tosp.setAdapter(adapter);

                    // Select the first item in both spinners if list is not empty
                    if (!locationList.isEmpty()) {
                        binding.fromsp.setSelection(0);
                        binding.Tosp.setSelection(0);
                    }
                } else {
                    Toast.makeText(MainActivity.this, "No locations found", Toast.LENGTH_SHORT).show();
                }

                // Hide the progress bars after data is loaded
                binding.progressBar.setVisibility(View.GONE);
                binding.progressBar2.setVisibility(View.GONE);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Handle Firebase error and hide progress bars
                binding.progressBar.setVisibility(View.GONE);
                binding.progressBar2.setVisibility(View.GONE);
                Toast.makeText(MainActivity.this, "Failed to load locations: " + error.getMessage(), Toast.LENGTH_SHORT).show();
                Log.e("MainActivity", "Database error: " + error.getMessage());
            }
        });
    }

    private void showDatePickerDialog(TextView textView) {
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(this, (view, selectedYear, selectedMonth, selectedDay) -> {
            calendar.set(selectedYear, selectedMonth, selectedDay);
            String selectedDate = dateFormat.format(calendar.getTime());
            textView.setText(selectedDate);
        }, year, month, day);
        datePickerDialog.show();
    }
}
