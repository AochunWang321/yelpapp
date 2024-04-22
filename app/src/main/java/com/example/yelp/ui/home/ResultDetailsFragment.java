package com.example.yelp.ui.home;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import com.example.yelp.R;
import com.example.yelp.databinding.FragmentDetailsBinding;
import com.google.android.material.tabs.TabLayout;

public class ResultDetailsFragment extends Fragment {
    Result result;
    private FragmentDetailsBinding binding;
    MyViewPagerAdapter myViewPagerAdapter;
    ViewPager viewPager;

    public ResultDetailsFragment(Result result) {
        this.result = result;
    }

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        binding = FragmentDetailsBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        binding.title.setText(result.getName());
        binding.back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getActivity().onBackPressed();
            }
        });

        binding.fb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.facebook.com/sharer/sharer.php?u="+result.getUrl()+"&quote="+result.getName()+" on Yelp"));
                startActivity(browserIntent);
            }
        });

        binding.twitter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://twitter.com/intent/tweet?text="+result.getName()+" on Yelp&url="+result.getUrl()));
                startActivity(browserIntent);
            }
        });
        return root;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        myViewPagerAdapter = new MyViewPagerAdapter(getChildFragmentManager(),result);
        TabLayout tabLayout = view.findViewById(R.id.tab_layout);
        viewPager = view.findViewById(R.id.viewpager);
        viewPager.setAdapter(myViewPagerAdapter);
        tabLayout.setupWithViewPager(viewPager);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}