package com.example.project8;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Map;

public class ItemAdapter extends BaseAdapter {
    LayoutInflater mInflater;
    private ArrayList<Map<String,Object>> movies;

    public ItemAdapter(Context c, ArrayList<Map<String,Object>> movies){
        this.movies = movies;

        mInflater = (LayoutInflater) c.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return movies.size();
    }

    @Override
    public Object getItem(int i) {
        return movies.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        View v = mInflater.inflate(R.layout.charity_info,null);
        TextView nameTextView = (TextView) v.findViewById(R.id.movieName);
//        String image = "<img src= \"https://www.flowte.me/style/img/default_event_img.png?1553754272\" width = 75px height = 75px vertical-align = middle>";
//        WebView imageView = (WebView) v.findViewById(R.id.movieImage);
//        imageView.loadData(image, "text/html", null);
//        imageView.setFocusable(false);
//        imageView.setFocusableInTouchMode(false);
//        imageView.setClickable(false);

        nameTextView.setText((String)movies.get(i).get("title"));
//        descriptionTextView.setText(charities.get(i).get(2));
        return v;
    }
}
