package com.example.yelp.ui.home;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.yelp.MainActivity;
import com.example.yelp.R;
import com.example.yelp.databinding.FragmentReservationBinding;

import java.util.ArrayList;

public class ReservationFragment extends Fragment {
    private FragmentReservationBinding binding;

    public ReservationFragment() {

    }

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        binding = FragmentReservationBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        binding.back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getActivity().onBackPressed();
            }
        });

        String reservation = MainActivity.preferences.getString("reserve","");
        String[] reservations = reservation.split("###");
        ArrayList<Reservation> reservationArrayList = new ArrayList<>();
        for (int i = 0; i < reservations.length; i++) {
            String res = reservations[i];
            if (res.length()<=0){
                continue;
            }
            String[] s = res.split("@@@");
            if (s.length<4){
                continue;
            }
            Reservation r = new Reservation();
            r.setName(s[0]);
            r.setDate(s[1]);
            r.setTime(s[2]);
            r.setEmail(s[3]);
            reservationArrayList.add(r);
        }

        if (reservationArrayList.size()<=0){
            binding.nobookings.setVisibility(View.VISIBLE);
        }else{
            binding.nobookings.setVisibility(View.INVISIBLE);
        }

        binding.reservationlist.setLayoutManager(new LinearLayoutManager(getContext()));
        ReservationAdapter adapter = new ReservationAdapter(this, reservationArrayList);
        binding.reservationlist.setAdapter(adapter);

        ItemTouchHelper.SimpleCallback simpleItemTouchCallback = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT | ItemTouchHelper.DOWN | ItemTouchHelper.UP) {

            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public int getMovementFlags(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
                int dragFlags = ItemTouchHelper.UP | ItemTouchHelper.DOWN;
                int swipeFlags = ItemTouchHelper.START;
                return makeMovementFlags(dragFlags, swipeFlags);
            }


            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int swipeDir) {
                new AlertDialog.Builder(ReservationFragment.this.getContext())
                        .setTitle("Tip")
                        .setMessage("Delete this booking?")
                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                int position = viewHolder.getAdapterPosition();
                                String reservation = MainActivity.preferences.getString("reserve","");
                                String[] reservations = reservation.split("###");
                                ArrayList<Reservation> reservationArrayList = new ArrayList<>();
                                for (int i = 0; i < reservations.length; i++) {
                                    if(i==position){
                                        continue;
                                    }
                                    String res = reservations[i];
                                    if (res.length()<=0){
                                        continue;
                                    }
                                    String[] s = res.split("@@@");
                                    if (s.length<4){
                                        continue;
                                    }
                                    Reservation r = new Reservation();
                                    r.setName(s[0]);
                                    r.setDate(s[1]);
                                    r.setTime(s[2]);
                                    r.setEmail(s[3]);
                                    reservationArrayList.add(r);
                                }

                                String buffer = "";
                                for (int i = 0; i < reservationArrayList.size(); i++) {
                                    Reservation reservation1 = reservationArrayList.get(i);
                                    if (buffer.length()<=0){
                                        buffer += reservation1.getName()+"@@@"+reservation1.getDate()+"@@@"+reservation1.getTime()+"@@@"+reservation1.getEmail();
                                    }else{
                                        buffer += "###"+reservation1.getName()+"@@@"+reservation1.getDate()+"@@@"+reservation1.getTime()+"@@@"+reservation1.getEmail();
                                    }
                                }
                                MainActivity.editor.putString("reserve",buffer);
                                MainActivity.editor.commit();

                                if (reservationArrayList.size()<=0){
                                    binding.nobookings.setVisibility(View.VISIBLE);
                                }else{
                                    binding.nobookings.setVisibility(View.INVISIBLE);
                                }

                                binding.reservationlist.setLayoutManager(new LinearLayoutManager(getContext()));
                                ReservationAdapter adapter = new ReservationAdapter(ReservationFragment.this, reservationArrayList);
                                binding.reservationlist.setAdapter(adapter);
                            }
                        })
                        .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                String reservation = MainActivity.preferences.getString("reserve","");
                                String[] reservations = reservation.split("###");
                                ArrayList<Reservation> reservationArrayList = new ArrayList<>();
                                for (int i = 0; i < reservations.length; i++) {
                                    String res = reservations[i];
                                    if (res.length()<=0){
                                        continue;
                                    }
                                    String[] s = res.split("@@@");
                                    if (s.length<4){
                                        continue;
                                    }
                                    Reservation r = new Reservation();
                                    r.setName(s[0]);
                                    r.setDate(s[1]);
                                    r.setTime(s[2]);
                                    r.setEmail(s[3]);
                                    reservationArrayList.add(r);
                                }

                                if (reservationArrayList.size()<=0){
                                    binding.nobookings.setVisibility(View.VISIBLE);
                                }else{
                                    binding.nobookings.setVisibility(View.INVISIBLE);
                                }

                                binding.reservationlist.setLayoutManager(new LinearLayoutManager(getContext()));
                                ReservationAdapter adapter = new ReservationAdapter(ReservationFragment.this, reservationArrayList);
                                binding.reservationlist.setAdapter(adapter);
                            }
                        })
                        .show();

            }
        };

        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(simpleItemTouchCallback);
        itemTouchHelper.attachToRecyclerView(binding.reservationlist);

        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}