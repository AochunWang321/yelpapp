package com.example.yelp.ui.home;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.example.yelp.R;
import java.util.ArrayList;

public class ReviewAdapter extends RecyclerView.Adapter<ReviewAdapter.ViewHolder> {

    private ArrayList<Review> reviews;
    private LayoutInflater mInflater;
    private Fragment fragment;

    ReviewAdapter(Fragment fragment, ArrayList<Review> reviews) {
        this.mInflater = LayoutInflater.from(fragment.getContext());
        this.reviews = reviews;
        this.fragment = fragment;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.review_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Review review = reviews.get(position);
        holder.name.setText(review.getName());
        holder.rating.setText("Rating:"+review.getRating()+"/5");
        holder.comment.setText(review.getComment());
        String time = review.getDate();
        int index = time.indexOf(" ");
        if (index>=0){
            time = time.substring(0,index);
        }
        holder.date.setText(time);
    }

    @Override
    public int getItemCount() {
        return reviews.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        TextView name;
        TextView rating;
        TextView comment;
        TextView date;

        ViewHolder(View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.name);
            rating = itemView.findViewById(R.id.rating);
            comment = itemView.findViewById(R.id.comment);
            date = itemView.findViewById(R.id.date);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override public void onClick(View v) {
//                    FragmentManager fragmentManager = fragment.getActivity().getSupportFragmentManager();
//                    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
//                    Fragment fragment = new ResultDetailsFragment();
//                    fragmentTransaction.replace(R.id.nav_host_fragment_activity_main, fragment);
//                    fragmentTransaction.addToBackStack(null);
//                    fragmentTransaction.commit();
                }
            });
        }
    }
}