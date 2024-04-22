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

import java.text.DecimalFormat;
import java.util.List;

public class ResultAdapter extends RecyclerView.Adapter<ResultAdapter.ViewHolder> {

    private List<Result> mResults;
    private LayoutInflater mInflater;
    private Fragment fragment;

    ResultAdapter(Fragment fragment, List<Result> results) {
        this.mInflater = LayoutInflater.from(fragment.getContext());
        this.mResults = results;
        this.fragment = fragment;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.result_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Result result = mResults.get(position);
        holder.number.setText(String.valueOf(position+1));
        holder.name.setText(result.getName());
        holder.rating.setText(result.getRating());
        double dis = Double.valueOf(result.getDistance());
        dis = dis/609.344;
        DecimalFormat df = new DecimalFormat("######0.00");
        String s = df.format(dis);
        holder.distance.setText(s);
        holder.result = result;
        RequestOptions options = new RequestOptions()
                .centerCrop()
                .placeholder(R.drawable.defaultimage)
                .error(R.drawable.defaultimage);

        GlideUrl glideUrl = new GlideUrl(result.getImage(), new LazyHeaders.Builder()
                .addHeader("User-Agent",
                        "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_11_2) AppleWebKit / 537.36(KHTML, like Gecko) Chrome  47.0.2526.106 Safari / 537.36")
                .build());

        Glide.with(fragment.getContext()).load(glideUrl).apply(options).into(holder.image);
        //MyGlideModule.with(fragment.getContext()).load(result.getImage()).into(holder.image);
    }

    @Override
    public int getItemCount() {
        return mResults.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        TextView number;
        ImageView image;
        TextView name;
        TextView rating;
        TextView distance;
        Result result;

        ViewHolder(View itemView) {
            super(itemView);
            number = itemView.findViewById(R.id.number);
            image = itemView.findViewById(R.id.image);
            name = itemView.findViewById(R.id.name);
            rating = itemView.findViewById(R.id.rating);
            distance = itemView.findViewById(R.id.distance);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override public void onClick(View v) {
                    FragmentManager fragmentManager = fragment.getActivity().getSupportFragmentManager();
                    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                    Fragment fragment = new ResultDetailsFragment(result);
                    fragmentTransaction.replace(R.id.nav_host_fragment_activity_main, fragment);
                    fragmentTransaction.addToBackStack(null);
                    fragmentTransaction.commit();
                }
            });
        }
    }
}