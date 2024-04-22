package com.example.yelp.ui.home;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.yelp.databinding.FragmentReviewBinding;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class ReviewFragment extends Fragment {
    Result result;
    private FragmentReviewBinding binding;

    public ReviewFragment(Result result) {
        this.result = result;
    }

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        binding = FragmentReviewBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        RequestQueue queue = Volley.newRequestQueue(getContext());
        String url = "http://34.28.96.102:3022/reviews?id="+result.getId();
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        String result = response;
                        try {
                            JSONObject json = new JSONObject(result);
                            System.out.println(json);
                            json = (JSONObject) json.get("message");
                            JSONArray reviewArrayList = json.getJSONArray("reviews");
                            ArrayList<Review> reviews = new ArrayList<>();
                            for (int i = 0; i < reviewArrayList.length(); i++) {
                                json = reviewArrayList.getJSONObject(i);
                                Review review = new Review();
                                JSONObject jsonObject = json.getJSONObject("user");
                                review.setName(jsonObject.getString("name"));
                                review.setRating(json.getString("rating"));
                                review.setComment(json.getString("text"));
                                review.setDate(json.getString("time_created"));
                                reviews.add(review);
                            }
                            getActivity().runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    binding.reviewlist.setLayoutManager(new LinearLayoutManager(getContext()));
                                    ReviewAdapter adapter = new ReviewAdapter(ReviewFragment.this,reviews);
                                    binding.reviewlist.setAdapter(adapter);
                                }
                            });

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                System.out.println(error);
            }
        });
        queue.add(stringRequest);
        return root;
    }
}