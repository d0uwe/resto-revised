package com.example.douwe.pset5;

import android.os.Bundle;
import android.app.Fragment;
import android.support.annotation.Nullable;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 */
public class MenuFragment extends ListFragment {
    ArrayList<String> usedList;
    ArrayList<Integer> prices = new ArrayList<Integer>(10);

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle arguments = this.getArguments();
        String menu = arguments.getString("category");
        String url = "https://resto.mprog.nl/menu?category=" + menu;
        getString(url);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_menu, container, false);
    }

    public String getString(String url){
        // Instantiate the RequestQueue.
        System.out.println("getting string");

        RequestQueue queue = Volley.newRequestQueue(getContext().getApplicationContext());
        String result;
// Request a string response from the provided URL.
        StringRequest stringRequest = new StringRequest(com.android.volley.Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // Display the first 500 characters of the response string.

                        parseResponse(response);
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
            }
        });
        // Add the request to the RequestQueue.
        queue.add(stringRequest);

        return "failed";
    }
    void parseResponse(String response){
        try{
            JSONObject obj = new JSONObject(response);
            JSONArray arr = obj.getJSONArray("items");

            // for listview
            ArrayList<String> arr2 = new ArrayList<>();


            // Get all foodsies and add them to arr2, for the view.
            for (int i = 0; i < arr.length(); i++)
            {
                JSONObject entry = arr.getJSONObject(i);
                prices.add(entry.getInt("price"));
                String post_id = entry.getString("name");
                arr2.add(post_id);
            }
            ArrayAdapter<String> list = new ArrayAdapter<>(getContext().getApplicationContext(), android.R.layout.simple_list_item_1, arr2);
            usedList = arr2;
            this.setListAdapter(list);

        } catch (Exception e){
            System.out.println(e.toString());
        }

    }


    @Override
    // add clicked item to db of on order items
    public void onListItemClick(ListView l, View v, int position, long id) {
        super.onListItemClick(l, v, position, id);

        restoDatabase db = restoDatabase.getInstance(v.getContext().getApplicationContext());
        db.insert(usedList.get(position), prices.get(position));
    }
}
