package com.example.myapplication.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import androidx.recyclerview.widget.GridLayoutManager;

import com.example.myapplication.Adapter.SeatAdapter;
import com.example.myapplication.Model.Flight;
import com.example.myapplication.Model.Seat;
import com.example.myapplication.databinding.ActivitySeatListBinding;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class SeatListActivity extends BaseActivity {

    private ActivitySeatListBinding binding;
    private Flight flight;
    private double price = 0.0;
    private int num = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySeatListBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        getIntentExtra();
        if (flight != null) {
            initSeatList();
        }
        setVariable();
    }

    private void initSeatList() {
        // Initialize GridLayoutManager with 7 columns
        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 7);
        gridLayoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(int position) {
                return 1;  // 1:1 seat span size
            }
        });

        binding.seatRecycleView.setLayoutManager(gridLayoutManager);

        // Create seat list and configure the seat grid layout
        List<Seat> seatList = new ArrayList<>();
        int row = 0;
        int numberSeat = flight.getNumberSeat() + (flight.getNumberSeat() / 7);

        // Map for seat alphabets (A, B, C, D, etc.)
        Map<Integer, String> seatAlphabetMap = new HashMap<>();
        seatAlphabetMap.put(0, "A");
        seatAlphabetMap.put(1, "B");
        seatAlphabetMap.put(2, "C");
        seatAlphabetMap.put(4, "D");
        seatAlphabetMap.put(5, "E");
        seatAlphabetMap.put(6, "F");

        // Loop to generate seat grid
        for (int i = 0; i < numberSeat; i++) {
            if (i % 7 == 0) {
                row++;
            }
            if (i % 7 == 3) {
                // Empty seat in the middle aisle
                seatList.add(new Seat(Seat.SeatStatus.EMPTY, String.valueOf(row)));
            } else {
                String seatName = seatAlphabetMap.get(i % 7) + row;
                Seat.SeatStatus seatStatus = flight.getReservedSeats() != null && flight.getReservedSeats().contains(seatName)
                        ? Seat.SeatStatus.UNAVAILABLE
                        : Seat.SeatStatus.AVAILABLE;
                seatList.add(new Seat(seatStatus, seatName));
            }
        }

        // Set adapter for RecyclerView
        SeatAdapter seatAdapter = new SeatAdapter(seatList, this, (selectedName, selectedNum) -> {
            num = selectedNum;  // Update num when seats are selected
            binding.numberSelectText.setText(num + " Seats Selected");
            binding.nameSeatCelectedText.setText(selectedName);

            // Format the price properly with Locale
            DecimalFormat df = new DecimalFormat("#,##0.00", DecimalFormatSymbols.getInstance(Locale.US));
            price = num * flight.getPrice();

            // Handle the comma separator issue here
            String formattedPrice = df.format(price).replace(",", ".");

            binding.priceText.setText("$" + formattedPrice);  // Display the formatted price
        });

        binding.seatRecycleView.setAdapter(seatAdapter);
        binding.seatRecycleView.setNestedScrollingEnabled(false);
    }

    private void setVariable() {
        binding.backBtn.setOnClickListener(v -> finish());
        binding.confirmBtn.setOnClickListener(v -> {
            if (num > 0) {
                flight.setPassenger(binding.nameSeatCelectedText.getText().toString());
                flight.setPrice(price);
                Intent intent = new Intent(SeatListActivity.this, TickerDetailActivity.class);
                intent.putExtra("flight", flight);
                startActivity(intent);
            } else {
                Toast.makeText(SeatListActivity.this, "Please select your seat", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void getIntentExtra() {
        if (getIntent() != null && getIntent().hasExtra("flight")) {
            flight = (Flight) getIntent().getSerializableExtra("flight");
        } else {
            flight = null; // Handle this case in initSeatList
        }
    }
}
