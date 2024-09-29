package com.example.myapplication.Activity;

import android.os.Bundle;

import com.example.myapplication.Model.Flight;
import com.example.myapplication.databinding.ActivityTickerDetailBinding;

public class TickerDetailActivity extends BaseActivity {
    private ActivityTickerDetailBinding binding;
    private Flight flight;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding=ActivityTickerDetailBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        
        getIntentExtra();
        setVariable();
    }

    private void setVariable() {
        binding.backBtn.setOnClickListener(v -> finish());
        binding.fromText.setText(flight.getFromShort());
        binding.fromSmallText.setText(flight.getFrom());
        binding.toText.setText(flight.getTo());
        binding.toShortText.setText(flight.getToShort());
        binding.toSmallText.setText(flight.getTo());
        binding.dateTex.setText(flight.getDate());
        binding.timeText.setText(flight.getTime());
        binding.arrivalText.setText(flight.getArriveTime());
        binding.priceText.setText("$"+flight.getPrice());
        binding.airlineText.setText(flight.getAirlineName());
        binding.seatText.setText(flight.getPassenger());
    }

    private void getIntentExtra() {
        flight=(Flight) getIntent().getSerializableExtra("flight");
    }
}