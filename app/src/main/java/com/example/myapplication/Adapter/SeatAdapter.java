package com.example.myapplication.Adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.Model.Seat;
import com.example.myapplication.R;
import com.example.myapplication.databinding.SeatItemBinding;

import java.util.ArrayList;
import java.util.List;

public class SeatAdapter extends RecyclerView.Adapter<SeatAdapter.SeatViewholder> {

    private final List<Seat> seatList;
    private final Context context;
    private final ArrayList<String> selectedSeatNames = new ArrayList<>();
    private final SelectedSeat selectedSeat;

    public SeatAdapter(List<Seat> seatList, Context context, SelectedSeat selectedSeat) {
        this.seatList = seatList;
        this.context = context;
        this.selectedSeat = selectedSeat;
    }

    @NonNull
    @Override
    public SeatAdapter.SeatViewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        SeatItemBinding binding = SeatItemBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new SeatViewholder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull SeatAdapter.SeatViewholder holder, int position) {
        Seat seat = seatList.get(position);
        holder.binding.SeatImageView.setText(seat.getName());

        // Handle seat status (Available, Selected, Unavailable, Empty)
        switch (seat.getStatus()) {
            case AVAILABLE:
                holder.binding.SeatImageView.setBackgroundResource(R.drawable.ic_seat_available);
                holder.binding.SeatImageView.setTextColor(context.getResources().getColor(R.color.white));
                break;

            case SELECTED:
                holder.binding.SeatImageView.setBackgroundResource(R.drawable.ic_seat_selected);
                holder.binding.SeatImageView.setTextColor(context.getResources().getColor(R.color.black));
                break;

            case UNAVAILABLE:
                holder.binding.SeatImageView.setBackgroundResource(R.drawable.ic_seat_unavailable);
                holder.binding.SeatImageView.setTextColor(context.getResources().getColor(R.color.grey));
                break;

            case EMPTY:
                holder.binding.SeatImageView.setBackgroundResource(R.drawable.ic_seat_empty);
                // Fixed the error: removed the extra space from the hex color string
                holder.binding.SeatImageView.setTextColor(Color.parseColor("#00000000")); // Fully transparent black
                break;
        }

        // Handle seat click event
        holder.binding.SeatImageView.setOnClickListener(v -> {
            if (seat.getStatus() == Seat.SeatStatus.AVAILABLE) {
                seat.setStatus(Seat.SeatStatus.SELECTED);
                selectedSeatNames.add(seat.getName());
            } else if (seat.getStatus() == Seat.SeatStatus.SELECTED) {
                seat.setStatus(Seat.SeatStatus.AVAILABLE);
                selectedSeatNames.remove(seat.getName());
            }
            notifyItemChanged(position);

            // Format selected seats for display
            String selected = String.join(", ", selectedSeatNames);
            selectedSeat.Return(selected, selectedSeatNames.size());
        });
    }

    @Override
    public int getItemCount() {
        return seatList.size();
    }

    public class SeatViewholder extends RecyclerView.ViewHolder {
        SeatItemBinding binding;

        public SeatViewholder(@NonNull SeatItemBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }

    public interface SelectedSeat {
        void Return(String selectedName, int num);
    }
}
