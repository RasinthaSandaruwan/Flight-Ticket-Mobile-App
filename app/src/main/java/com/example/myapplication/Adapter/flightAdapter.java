package com.example.myapplication.Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.Activity.SeatListActivity;
import com.example.myapplication.Model.Flight;
import com.example.myapplication.databinding.ViewholderFlightBinding;

import java.util.ArrayList;

public class flightAdapter extends RecyclerView.Adapter<flightAdapter.Viewholder> {
    public flightAdapter(ArrayList<Flight> flights) {
        this.flights = flights;
    }

    private final ArrayList<Flight> flights;
    private Context context;

    @NonNull
    @Override
    public flightAdapter.Viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context=parent.getContext();
        ViewholderFlightBinding binding=ViewholderFlightBinding.inflate(LayoutInflater.from(context),parent,false);
        return new Viewholder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull flightAdapter.Viewholder holder, int position) {
        Flight flight=flights.get(position);

        holder.binding.fromText.setText(flight.getFrom());
        holder.binding.fromShortText.setText(flight.getFromShort());
        holder.binding.toText.setText(flight.getTo());
        holder.binding.toShortText.setText(flight.getToShort());
        holder.binding.arrivalText.setText(flight.getArriveTime());
        holder.binding.priceText.setText("$"+flight.getPrice());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(context, SeatListActivity.class);
                intent.putExtra("flight",flight);
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return flights.size();
    }

    public class Viewholder extends RecyclerView.ViewHolder {
        private final ViewholderFlightBinding binding;
        public Viewholder(ViewholderFlightBinding binding) {
            super(binding.getRoot());
            this.binding=binding;
        }
    }
}
