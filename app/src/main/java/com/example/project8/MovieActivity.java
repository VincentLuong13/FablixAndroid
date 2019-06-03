package com.example.project8;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

public class MovieActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        System.out.println("HHAHAAH");
        setContentView(R.layout.activity_movie);
        String movieId = getIntent().getStringExtra("movieId");
        Log.e("TEST", movieId);
        System.out.println("HHAHAAH");
        getMovie();
    }

    public void getMovie(){
        String movieId = getIntent().getStringExtra("movieId");
        ObjectMapper mapper = new ObjectMapper();
        final RequestQueue queue = Volley.newRequestQueue(getApplicationContext());

        StringRequest sr = new StringRequest(Request.Method.GET, "http://128.195.65.55:5444/api/movies/get/" + movieId,
                response -> {
                    Map<String, Object> map = null;
                    Log.e("HttpClient", "success! response: " + response.toString());
                    try{
                        map = mapper.readValue(response, Map.class);
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                    setPage((Map<String,Object>)map.get("movie"));

                },
                error -> Log.e("HttpClient", "error: " + error.toString()))
        {
        };
        queue.add(sr);
    }


    public void setPage(Map<String, Object> map){
        TextView titleView = findViewById(R.id.title);
        TextView infoView = findViewById(R.id.movieInfo);
        infoView.setMovementMethod(new ScrollingMovementMethod());

        titleView.setText((String)map.get("title"));
        String info = "";
        for( String key : map.keySet()){
            switch(key){
                case "director":
                    info = "Directed by: " + (String)map.get("director") + "\n\n";
                    break;
                case "year":
                    info += "Release date: " + (Integer)map.get("year") + "\n\n";
                    break;
                case "overview":
                    info += "Description: " + (String)map.get("overview") + "\n\n";
                    break;
                case "revenue":
                    info += "Revenue: " + (Integer)map.get("revenue") + "\n\n";
                    break;
//                case "rating":
//                    info += "Rating: " + (String)map.get("rating") + "\n\n";
//                    break;
                case "numVotes":
                    info += "Number of Votes: " + (Integer)map.get("numVotes") + "\n\n";
                    break;
                case "genres":
                    info += "Genres: ";
                    for(Map<String,Object> genre : (ArrayList<Map<String,Object>>)map.get("genres")){
                        info += (String)genre.get("name") + ", ";
                    }
                    info+= "\n\n";
                    break;
                case "stars":
                    info += "Stars: ";
                    for(Map<String,Object> genre : (ArrayList<Map<String,Object>>)map.get("stars")){
                        info += (String)genre.get("name") + ", ";
                    }
                    info+= "\n\n";
                    break;
            }

        }


        infoView.setText(info);
    }


    public void back(View view){
        Intent myIntent = new Intent(MovieActivity.this, SearchActivity.class);
        MovieActivity.this.startActivity(myIntent);
    }
}
