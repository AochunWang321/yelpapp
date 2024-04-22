package com.example.yelp.ui.home;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import com.example.yelp.R;

import java.util.ArrayList;

public class MySpinAdaptor extends ArrayAdapter<String>
{
    private ArrayList<String> mCategories = new ArrayList<>();
    //private int[] mIcons;

    public MySpinAdaptor(Context context, int layoutResourceId, ArrayList<String> categories)
    {
        super(context, layoutResourceId, categories);
        mCategories = categories;

        // Add the same icon to all items, just for testing.
//        mIcons = new int[mCategories.size()];
//        for (int i = 0; i < mIcons.length; i++)
//        {
//            mIcons[i] = R.drawable.add;
//        }
    }

    /**
     * View for a dropdown item.
     * */
    @Override
    public View getDropDownView(int position, View convertView, ViewGroup parent)
    {
        View rowView = convertView;

        if (rowView == null)
        {
            LayoutInflater inflater = LayoutInflater.from(parent.getContext());
            rowView = inflater.inflate(R.layout.my_spinner_dropdown_item, parent, false);
        }

        TextView categoryText = (TextView) rowView.findViewById(R.id.my_spinner_dropdown_item_text);
        categoryText.setText(mCategories.get(position));

        return rowView;
    }

    /**
     * The Spinner View that is selected and shown in the *Spinner*, i.e. not the dropdown item.
     * */
    @Override
    public View getView(int position, View convertView, ViewGroup parent)
    {
        View spinnerView = convertView;

        if (spinnerView == null)
        {
            LayoutInflater inflater = LayoutInflater.from(parent.getContext());
            spinnerView = inflater.inflate(R.layout.my_spinner_item, parent, false);
        }

        TextView categoryText = (TextView) spinnerView.findViewById(R.id.my_spinner_item_text);
        categoryText.setText(mCategories.get(position));

        return spinnerView;
    }

}