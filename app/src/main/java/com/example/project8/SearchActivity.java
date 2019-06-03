package com.example.project8;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SearchView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.json.JSONObject;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SearchActivity extends AppCompatActivity {
    private SearchView simpleSearchView;
    private ListView movieListView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        simpleSearchView = (SearchView) findViewById(R.id.movieSearch);
        movieListView = (ListView) findViewById(R.id.movieList);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        String query = "";

        requestMovies("");
        simpleSearchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                requestMovies(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {

                return false;
            }
        });

    }

    public void requestMovies(String query){
        final RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
        ObjectMapper mapper = new ObjectMapper();

        StringRequest sr = new StringRequest(Request.Method.GET, "http://128.195.65.55:5444/api/movies/search?title=" +query,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Map<String, Object> map = null;
                        Log.e("HttpClient", "success! response: " + response.toString());
                        try{
                            map = mapper.readValue(response, Map.class);
                        }catch (Exception e){
                            e.printStackTrace();
                        }
                        ItemAdapter itemAdapter = new ItemAdapter(getApplicationContext(),(ArrayList<Map<String,Object>>)map.get("movies"));
                        movieListView.setAdapter(itemAdapter);

                        final Map<String, Object> finalMap = map;
                        movieListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                            @Override
                            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                                Intent myIntent = new Intent(SearchActivity.this, MovieActivity.class);
                                System.out.println("hello");
                                myIntent.putExtra("movieId",(String) ((ArrayList<Map<String,Object>>)finalMap.get("movies")).get(i).get("movieId"));
                                System.out.println("hello2");
                                SearchActivity.this.startActivity(myIntent);

                            }
                        });
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("HttpClient", "error: " + error.toString());
                    }
                })
        {
            @Override
            protected Map<String,String> getParams(){
                Map<String,String> params = new HashMap<String, String>();
                params.put("title",query);
                return params;
            }
        };
        queue.add(sr);

    }

    public void reset(View view){
        requestMovies("");
    }
}
