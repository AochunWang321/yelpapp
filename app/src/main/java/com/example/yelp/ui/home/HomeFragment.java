package com.example.yelp.ui.home;

import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.os.Bundle;
import android.text.Editable;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.CompoundButton;
import android.widget.Spinner;
import android.widget.TimePicker;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.yelp.R;
import com.example.yelp.databinding.FragmentHomeBinding;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Calendar;

import kotlin.text.Charsets;

public class HomeFragment extends Fragment {

    private FragmentHomeBinding binding;
    private AutoSuggestAdapter autoSuggestAdapter;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        binding = FragmentHomeBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        CenteredImageSpan imageHint = new CenteredImageSpan(getContext(), R.drawable.redstart);
        String ss = "KeyWord ";
        SpannableString spannableString = new SpannableString(ss);
        spannableString.setSpan(imageHint, ss.length() - 1, ss.length(), Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
        binding.etKeyword.setHint(spannableString);

        imageHint = new CenteredImageSpan(getContext(), R.drawable.redstart);
        ss = "Location ";
        spannableString = new SpannableString(ss);
        spannableString.setSpan(imageHint, ss.length() - 1, ss.length(), Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
        binding.etLocation.setHint(spannableString);

        autoSuggestAdapter = new AutoSuggestAdapter(getContext(), android.R.layout.simple_dropdown_item_1line);
        binding.etKeyword.setThreshold(1);
        binding.etKeyword.setAdapter(autoSuggestAdapter);

        binding.etKeyword.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() <= 0) {
                    return;
                }
                RequestQueue queue = Volley.newRequestQueue(getContext());
                String url = null;
                try {
                    url = "http://34.28.96.102:3022/autocomplete?text=" + URLEncoder.encode(s.toString(), Charsets.UTF_8.name());
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
                StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                String result = response;
                                ArrayList<String> texts = new ArrayList<>();
                                try {
                                    JSONObject json = new JSONObject(result);
                                    json = json.getJSONObject("message");
                                    JSONArray jsonarray = json.getJSONArray("terms");
                                    JSONArray jsonarray2 = json.getJSONArray("categories");
                                    for (int i = 0; i < jsonarray.length(); i++) {
                                        json = jsonarray.getJSONObject(i);
                                        texts.add(json.getString("text"));
                                    }
                                    for (int i = 0; i < jsonarray2.length(); i++) {
                                        json = jsonarray2.getJSONObject(i);
                                        texts.add(json.getString("title"));
                                    }
                                    getActivity().runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            autoSuggestAdapter = new AutoSuggestAdapter(getContext(), android.R.layout.simple_dropdown_item_1line);
                                            autoSuggestAdapter.setData(texts);
                                            binding.etKeyword.setThreshold(1);
                                            binding.etKeyword.setAdapter(autoSuggestAdapter);
                                            autoSuggestAdapter.notifyDataSetChanged();
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
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.toString().length() > 0) {
                    binding.warning.setVisibility(View.INVISIBLE);
                    binding.popver1.setVisibility(View.INVISIBLE);
                }
            }
        });

        binding.etKeyword.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean hasFocus) {
                if (hasFocus) {
                    if (binding.etKeyword.getText().toString().length()<=0){
                        if (binding.warning.getVisibility()==View.VISIBLE) {
                            binding.popver1.setVisibility(View.VISIBLE);
                        }
                    }
                }else{
                    binding.popver1.setVisibility(View.INVISIBLE);
                }
            }
        });

        binding.etDistance.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean hasFocus) {
                if (hasFocus) {
                    if (binding.etDistance.getText().toString().length()<=0){
                        if (binding.warningdistance.getVisibility()==View.VISIBLE) {
                            binding.popverdistance.setVisibility(View.VISIBLE);
                        }
                    }
                }else{
                    binding.popverdistance.setVisibility(View.INVISIBLE);
                }
            }
        });

        binding.etLocation.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean hasFocus) {
                if (hasFocus) {
                    if (binding.etLocation.getText().toString().length()<=0){
                        if (binding.warning2.getVisibility()==View.VISIBLE) {
                            binding.popver2.setVisibility(View.VISIBLE);
                        }
                    }
                }else{
                    binding.popver2.setVisibility(View.INVISIBLE);
                }
            }
        });

        binding.etDistance.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.toString().length() > 0) {
                    binding.warningdistance.setVisibility(View.INVISIBLE);
                    binding.popverdistance.setVisibility(View.INVISIBLE);
                }
            }
        });

        binding.etLocation.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.toString().length() > 0) {
                    binding.warning2.setVisibility(View.INVISIBLE);
                    binding.popver2.setVisibility(View.INVISIBLE);
                }
            }
        });



        Spinner category = root.findViewById(R.id.category);
        ArrayList<String> categorylist = new ArrayList<>();
        categorylist.add("Default");
        categorylist.add("Art & Entertainment");
        categorylist.add("Health & Medical");
        categorylist.add("Hotels & Travel");
        categorylist.add("Food");
        categorylist.add("Professional Services");
        MySpinAdaptor adapter = new MySpinAdaptor(getContext(), android.R.layout.simple_spinner_dropdown_item, categorylist);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        category.setAdapter(adapter);

        binding.checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if (isChecked) {
                        binding.etLocationLayout.setVisibility(View.INVISIBLE);
                        binding.etLocation.setText("");
                        binding.popver2.setVisibility(View.INVISIBLE);
                    }else{
                        binding.etLocationLayout.setVisibility(View.VISIBLE);
                        binding.etLocation.setText("");
                    }
                }
            }
        );

        binding.clear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                binding.warning.setVisibility(View.INVISIBLE);
                binding.warning2.setVisibility(View.INVISIBLE);
                InputMethodManager imm = (InputMethodManager)getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(view.getApplicationWindowToken(), 0);
                binding.etKeyword.setText("");
                binding.etDistance.setText("");
                binding.etLocation.setText("");
                binding.checkBox.setChecked(false);
                binding.etLocationLayout.setVisibility(View.VISIBLE);
                binding.category.setSelection(0);

                ArrayList<Result> results = new ArrayList<>();
                binding.resultList.setLayoutManager(new LinearLayoutManager(getContext()));
                ResultAdapter adapter = new ResultAdapter(HomeFragment.this, results);
                binding.resultList.setAdapter(adapter);
            }
        });

        binding.submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                InputMethodManager imm = (InputMethodManager)getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(view.getApplicationWindowToken(), 0);

                binding.warning.setVisibility(View.INVISIBLE);
                binding.warning2.setVisibility(View.INVISIBLE);
                binding.warningdistance.setVisibility(View.INVISIBLE);
                boolean requrefield = false;
                if (binding.etKeyword.getText().length()<=0){
                    binding.warning.setVisibility(View.VISIBLE);
                    //binding.popver1.setVisibility(View.VISIBLE);
                    if (binding.etKeyword.hasFocus()){
                        binding.popver1.setVisibility(View.VISIBLE);
                    }
                    requrefield = true;
                } else if (binding.etDistance.getText().length()<=0){
                    binding.warningdistance.setVisibility(View.VISIBLE);
                    //binding.popver2.setVisibility(View.VISIBLE);
                    if (binding.etDistance.hasFocus()){
                        binding.popverdistance.setVisibility(View.VISIBLE);
                    }
                    requrefield = true;
                } else if (binding.etLocation.getText().length()<=0){
                    binding.warning2.setVisibility(View.VISIBLE);
                    if (binding.etLocation.hasFocus()){
                        binding.popver2.setVisibility(View.VISIBLE);
                    }
                    if (binding.etLocationLayout.getVisibility()==View.VISIBLE) {
                        requrefield = true;
                    }
                }
                if (requrefield){
                    return;
                }
                ProgressDialog progressDialog = ProgressDialog.show(getContext(),"","Loading",true,true);
                if (!binding.checkBox.isChecked()){
                    if (binding.etLocation.getText().length()<=0){
                        binding.warning2.setVisibility(View.VISIBLE);
                        return;
                    }

//                    String distance = binding.etDistance.getText().toString();
//                    if (distance.length()<=0){
//                        distance = "609.344";
//                    }else{
//                        double v = Integer.valueOf(distance) * 609.344;
//                        distance = String.valueOf((int)v);
//                    }
//                    String url = "http://34.28.96.102:3022/searchin?keyword="+binding.etKeyword.getText().toString()+"&distance="+distance+"&category="+category.getSelectedItem().toString()+"&lat="+"34.0522342"+"&lng="+"-118.2436849";
//                    requestResults(url,progressDialog);

                    RequestQueue queue = Volley.newRequestQueue(getContext());
                    String url = null;
                    try {
                        url = "https://maps.googleapis.com/maps/api/geocode/json?address="+ URLEncoder.encode(binding.etLocation.getText().toString(), Charsets.UTF_8.name())+"&key=AIzaSyCDJHOpT6euCZH1Z0dcghjxEigUxUj8oII";
                    } catch (UnsupportedEncodingException e) {
                        e.printStackTrace();
                    }
                    System.out.println(url);
                    // url = "https://maps.googleapis.com/maps/api/geocode/json?address=los%20angles&key=AIzaSyCDJHOpT6euCZH1Z0dcghjxEigUxUj8oII";
                    StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                            new Response.Listener<String>() {
                                @Override
                                public void onResponse(String response) {
                                    String result = response;
                                    try {
                                        JSONObject json = new JSONObject(result);
                                        JSONArray jsonArray = json.getJSONArray("results");
                                        if (jsonArray.length()>0){
                                            json = jsonArray.getJSONObject(0);
                                        }
                                        //.getJSONObject("geometry").getJSONObject("location");
                                        json = json.getJSONObject("geometry").getJSONObject("location");
                                        String lat = json.getString("lat");
                                        String lng = json.getString("lng");


                                        String distance = binding.etDistance.getText().toString();
                                        if (distance.length()<=0){
                                            distance = "609.344";
                                        }else{
                                            double v = Integer.valueOf(distance) * 609.344;
                                            distance = String.valueOf((int)v);
                                        }
                                        String url = "http://34.28.96.102:3022/searchin?keyword="+binding.etKeyword.getText().toString()+"&distance="+distance+"&category="+category.getSelectedItem().toString()+"&lat="+lat+"&lng="+lng;
                                        //System.out.println(url);
                                        requestResults(url,progressDialog);
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                        progressDialog.dismiss();
                                    }
                                }
                            }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            System.out.println(error);
                            progressDialog.dismiss();
                        }
                    });
                    queue.add(stringRequest);
                }else{
                    RequestQueue queue = Volley.newRequestQueue(getContext());
                    String url = "http://ipinfo.io/?token=92851fd3cfa06e";
                    StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                            new Response.Listener<String>() {
                                @Override
                                public void onResponse(String response) {
                                    String result = response;
                                    try {
                                        JSONObject json = new JSONObject(result);
                                        String loc = json.getString("loc");
                                        System.out.println(loc);
                                        String[] ss = loc.split(",");
                                        if (ss.length<2){
                                            progressDialog.dismiss();
                                            return;
                                        }
                                        String distance = binding.etDistance.getText().toString();
                                        if (distance.length()<=0){
                                            distance = "609.344";
                                        }else{
                                            double v = Integer.valueOf(distance) * 609.344;
                                            distance = String.valueOf((int)v);
                                        }
                                        String url = "http://34.28.96.102:3022/searchin?keyword="+URLEncoder.encode(binding.etKeyword.getText().toString(), Charsets.UTF_8.name())+"&distance="+URLEncoder.encode(distance, Charsets.UTF_8.name())+"&category="+URLEncoder.encode(category.getSelectedItem().toString(), Charsets.UTF_8.name())+"&lat="+ss[0]+"&lng="+ss[1];
                                        System.out.println(url);
                                        requestResults(url,progressDialog);
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                        progressDialog.dismiss();
                                    } catch (UnsupportedEncodingException e) {
                                        e.printStackTrace();
                                    }
                                }
                            }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            progressDialog.dismiss();
                            System.out.println(error);
                        }
                    });
                    queue.add(stringRequest);
                }
            }
        });

        binding.reservation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                Fragment fragment = new ReservationFragment();
                fragmentTransaction.replace(R.id.nav_host_fragment_activity_main, fragment);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
            }
        });

        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
        Fragment fragment_byID = fragmentManager.findFragmentById(R.id.nav_host_fragment_activity_main);
        if (fragment_byID!=null) {

        }

        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    public void requestResults(String url,ProgressDialog progressDialog){
        RequestQueue queue = Volley.newRequestQueue(getContext());
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        progressDialog.dismiss();
                        String result = response;
                        try {
                            JSONObject json = new JSONObject(result);
                            System.out.println(json);
                            json = (JSONObject) json.get("message");
                            JSONArray businesses = json.getJSONArray("businesses");
                            ArrayList<Result> results = new ArrayList<>();
                            for (int i = 0; i < businesses.length(); i++) {
                                if (results.size()>=10){
                                    break;
                                }
                                json = businesses.getJSONObject(i);
                                Result result1 = new Result();
                                result1.setId(json.getString("id"));
                                result1.setName(json.getString("name"));
                                result1.setImage(json.getString("image_url"));
                                result1.setRating(json.getString("rating"));
                                result1.setPhone(json.getString("display_phone"));
                                result1.setIs_closed(json.getBoolean("is_closed"));
                                result1.setDistance(String.valueOf(json.getInt("distance")));
                                result1.setUrl(json.getString("url"));
                                JSONObject jsonObject = json.getJSONObject("location");
                                JSONArray jsonArray = jsonObject.getJSONArray("display_address");
                                String address = "";
                                for (int j = 0; j < jsonArray.length(); j++) {
                                    if (address.length()<=0){
                                        address += jsonArray.getString(j);
                                    }else{
                                        address += ","+jsonArray.getString(j);
                                    }
                                }
                                result1.setAddress(address);

                                jsonArray = json.getJSONArray("categories");
                                String categories = "";
                                for (int j = 0; j < jsonArray.length(); j++) {
                                    JSONObject jsonObject1 = jsonArray.getJSONObject(j);
                                    String title = jsonObject1.getString("title");
                                    if (categories.length()<=0){
                                        categories += title;
                                    }else{
                                        categories += "|"+title;
                                    }
                                }
                                result1.setCategory(categories);

                                jsonObject = json.getJSONObject("coordinates");
                                result1.setLat(jsonObject.getDouble("latitude"));
                                result1.setLng(jsonObject.getDouble("longitude"));

                                results.add(result1);
                            }
                            getActivity().runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    binding.noresult.setVisibility(View.INVISIBLE);
                                    if (results.size()<=0){
                                        binding.noresult.setVisibility(View.VISIBLE);
                                    }else {
                                        binding.resultList.setLayoutManager(new LinearLayoutManager(getContext()));
                                        ResultAdapter adapter = new ResultAdapter(HomeFragment.this, results);
                                        binding.resultList.setAdapter(adapter);
                                    }
                                }
                            });

                        } catch (JSONException e) {
                            e.printStackTrace();
                            progressDialog.dismiss();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                System.out.println(error);
            }
        });
        queue.add(stringRequest);
    }
}