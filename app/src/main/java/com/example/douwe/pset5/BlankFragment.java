package com.example.douwe.pset5;

import android.support.annotation.Nullable;
import android.support.v4.app.ListFragment;
import android.os.Bundle;
import android.app.Fragment;
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
public class BlankFragment extends ListFragment {
ArrayList<String> theusedlist;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_blank, container, false);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        // get data of current categories at restaurant
        super.onCreate(savedInstanceState);
        String url = "https://resto.mprog.nl/categories";
        getString(url);
    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        super.onListItemClick(l, v, position, id);
        MenuFragment menuFragment = new MenuFragment();
        // save which categorie was picked and pass this to the next fragment
        Bundle args = new Bundle();
        String s = theusedlist.get(position);
        args.putString("category", s);

        menuFragment.setArguments(args);

        getFragmentManager()
                .beginTransaction()
                .replace(R.id.fragment_container, menuFragment)
                .addToBackStack(null)
                .commit();
    }

    public String getString(String url){
        // Instantiate the RequestQueue.
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
            JSONArray arr = obj.getJSONArray("categories");

            // for listview
            ArrayList<String> arr2 = new ArrayList<>();

            for (int i = 0; i < arr.length(); i++)
            {
                String post_id = arr.getString(i);
                System.out.println("checkcheck: " + post_id);
                arr2.add(post_id);
            }
            theusedlist = arr2;
            ArrayAdapter<String> list = new ArrayAdapter<>(getContext().getApplicationContext(), android.R.layout.simple_list_item_1, arr2);

            this.setListAdapter(list);
        } catch (Exception e){

        }
    }
}
