package com.example.yelp.ui.home;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.model.GlideUrl;
import com.bumptech.glide.load.model.LazyHeaders;
import com.bumptech.glide.request.RequestOptions;
import com.example.yelp.R;

import java.util.ArrayList;
import java.util.List;

public class ReservationAdapter extends RecyclerView.Adapter<ReservationAdapter.ViewHolder> {

    private ArrayList<Reservation> reservations;
    private LayoutInflater mInflater;
    private Fragment fragment;

    ReservationAdapter(Fragment fragment, ArrayList<Reservation> reservations) {
        this.mInflater = LayoutInflater.from(fragment.getContext());
        this.reservations = reservations;
        this.fragment = fragment;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.reservation_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Reservation reservation = reservations.get(position);
        holder.number.setText(String.valueOf(position+1));
        holder.name.setText(reservation.getName());
        holder.date.setText(reservation.getDate());
        holder.time.setText(reservation.getTime());
        holder.email.setText(reservation.getEmail());
        holder.reservation = reservation;
    }

    @Override
    public int getItemCount() {
        return reservations.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        TextView number;
        TextView name;
        TextView date;
        TextView time;
        TextView email;
        Reservation reservation;

        ViewHolder(View itemView) {
            super(itemView);
            number = itemView.findViewById(R.id.number);
            name = itemView.findViewById(R.id.name);
            date = itemView.findViewById(R.id.date);
            time = itemView.findViewById(R.id.time);
            email = itemView.findViewById(R.id.email);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override public void onClick(View v) {
                }
            });
        }
    }
}