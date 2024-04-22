package com.example.yelp.ui.home;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Paint;
import android.net.Uri;
import android.os.Bundle;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.yelp.MainActivity;
import com.example.yelp.R;
import com.example.yelp.databinding.FragmentBussinessDetailsBinding;
import com.google.android.material.tabs.TabLayout;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

public class BussinessDetailsFragment extends Fragment {

    private FragmentBussinessDetailsBinding binding;
    Result result;

    public BussinessDetailsFragment(Result result) {
        this.result = result;
    }

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        binding = FragmentBussinessDetailsBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        binding.address.setText(result.getAddress());
        binding.phonenumber.setText(result.getPhone());
        binding.phonenumber.setText(result.getPhone());
        binding.category.setText(result.getCategory());
        String status = "Open Now";
        if (result.isIs_closed()){
            status = "Closed Now";
        }
        binding.status.setText(status);
        binding.link.setPaintFlags(binding.link.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
        binding.link.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(result.getUrl()));
                startActivity(browserIntent);
            }
        });

        binding.reserve.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder alert = new AlertDialog.Builder(getContext());
                View titleView = getLayoutInflater().inflate(R.layout.dialog_title, null);
                TextView title = titleView.findViewById(R.id.dialogtitle);
                title.setText(result.getName());
                alert.setCustomTitle(titleView);

                LinearLayout layout = new LinearLayout(getContext());
                layout.setOrientation(LinearLayout.VERTICAL);

                TextView tv = new TextView(getContext());
                tv.setText("Email");
                tv.setTextColor(ContextCompat.getColor(getContext(), R.color.red));
                layout.addView(tv);

                EditText emailET = new EditText(getContext());
                emailET.setSingleLine();
                emailET.setHint("Email Id");
                layout.addView(emailET);

                tv = new TextView(getContext());
                tv.setText("Date");
                tv.setTextColor(ContextCompat.getColor(getContext(), R.color.red));
                layout.addView(tv);

                EditText dateET = new EditText(getContext());
                dateET.setSingleLine();
                dateET.setHint("mm-dd-yyyy");
                dateET.setClickable(true);
                dateET.setLongClickable(false);
                dateET.setKeyListener(null);
                dateET.setFocusable(false);
                dateET.setInputType(InputType.TYPE_NULL);
                Calendar myCalendar= Calendar.getInstance();
                DatePickerDialog.OnDateSetListener date2 =new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int day) {
                        myCalendar.set(Calendar.YEAR, year);
                        myCalendar.set(Calendar.MONTH,month);
                        myCalendar.set(Calendar.DAY_OF_MONTH,day);
                        String myFormat="MM-dd-yyyy";
                        SimpleDateFormat dateFormat=new SimpleDateFormat(myFormat);
                        dateET.setText(dateFormat.format(myCalendar.getTime()));
                    }
                };
                dateET.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        DatePickerDialog datePickerDialog = new DatePickerDialog(getContext(),date2,myCalendar.get(Calendar.YEAR),myCalendar.get(Calendar.MONTH),myCalendar.get(Calendar.DAY_OF_MONTH));
                        datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);
                        datePickerDialog.show();
                    }
                });
                layout.addView(dateET);

                tv = new TextView(getContext());
                tv.setText("Start Time");
                tv.setTextColor(ContextCompat.getColor(getContext(), R.color.red));
                layout.addView(tv);

                EditText timeET = new EditText(getContext());
                timeET.setSingleLine();
                timeET.setHint("hh:mm");
                layout.addView(timeET);
                timeET.setClickable(true);
                timeET.setLongClickable(false);
                timeET.setKeyListener(null);
                timeET.setFocusable(false);
                timeET.setInputType(InputType.TYPE_NULL);
                timeET.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Calendar mcurrentTime = Calendar.getInstance();
                        int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
                        int minute = mcurrentTime.get(Calendar.MINUTE);
                        TimePickerDialog mTimePicker;
                        mTimePicker = new TimePickerDialog(getContext(), new TimePickerDialog.OnTimeSetListener() {
                            @Override
                            public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                                timeET.setText( selectedHour + ":" + selectedMinute);
                            }
                        }, hour, minute, false);//Yes 24 hour time
                        mTimePicker.setTitle("Select Time");
                        mTimePicker.show();
                    }
                });

                layout.setPadding(50, 40, 50, 10);
                alert.setView(layout);
                alert.setPositiveButton("SUBMIT", (dialogInterface, i) -> {
                    String email = emailET.getText().toString();
                    String date = dateET.getText().toString();
                    String starttime = timeET.getText().toString();
                    if (!isEmail(email)){
                        Toast.makeText(getActivity(), "InValid Email Address.",
                                Toast.LENGTH_LONG).show();
                        return;
                    }
                    System.out.println(starttime);
                    String[] time = starttime.split(":");
                    if (time.length<2){
                        Toast.makeText(getActivity(), "InValid time.",
                                Toast.LENGTH_LONG).show();
                        return;
                    }
                    if (Integer.valueOf(time[0])<10 || Integer.valueOf(time[0])>17){
                        Toast.makeText(getActivity(), "Time should be between 10AM AND 5PM",
                                Toast.LENGTH_LONG).show();
                        return;
                    }
                    String reserve = MainActivity.preferences.getString("reserve", "");
                    if (reserve.length() <= 0) {
                        reserve += result.getName()+"@@@"+date+"@@@"+starttime+"@@@"+email;
                    } else {
                        reserve += "###"+result.getName()+"@@@"+date+"@@@"+starttime+"@@@"+email;
                    }
                    MainActivity.editor.putString("reserve", reserve);
                    MainActivity.editor.commit();

                    Toast.makeText(getActivity(), "Reservation Booked",
                            Toast.LENGTH_LONG).show();
                });
                alert.setNegativeButton("CANCEL", (dialogInterface, i) -> {

                });

                alert.create();
                alert.show();
            }
        });

        RequestQueue queue = Volley.newRequestQueue(getContext());
        String url = "http://34.28.96.102:3022/busi?id=" + result.getId();
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        String result2 = response;
                        System.out.println(result2);
                        ArrayList<String> photos = new ArrayList<>();
                        ArrayList<OpenTime> openTimes = new ArrayList<>();
                        try {
                            JSONObject json = new JSONObject(result2);
                            json = json.getJSONObject("message");
                            JSONArray jsonarray = json.getJSONArray("photos");
                            JSONArray jsonarray2 = json.getJSONArray("hours");
                            if (jsonarray2.length()>0){
                                json = jsonarray2.getJSONObject(0);
                                jsonarray2 = json.getJSONArray("open");

                                for (int i = 0; i < jsonarray2.length(); i++) {
                                    OpenTime ot = new OpenTime();
                                    json = jsonarray2.getJSONObject(i);
                                    ot.setIs_overnight(json.getBoolean("is_overnight"));
                                    ot.setStart(json.getString("start"));
                                    ot.setEnd(json.getString("end"));
                                    ot.setDay(json.getInt("day"));
                                    openTimes.add(ot);
                                }
                                result.setOpenTimes(openTimes);
                            }
                            for (int i = 0; i < jsonarray.length(); i++) {
                                String photo = jsonarray.getString(i);
                                photos.add(photo);
                            }
                            result.setPhotos(photos);
                            getActivity().runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    ViewPager viewPager = binding.viewpager;
                                    ImageAdapter adapter = new ImageAdapter(BussinessDetailsFragment.this.getContext(),photos);
                                    viewPager.setAdapter(adapter);
                                    viewPager.setCurrentItem(0);
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

    public boolean isEmail(String s) {
        return s.matches("^[-0-9a-zA-Z.+_]+@[-0-9a-zA-Z.+_]+\\.[a-zA-Z]{2,4}");
    }

}