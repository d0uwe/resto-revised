package com.example.douwe.pset5;


import android.database.Cursor;
import android.os.Bundle;
import android.app.Fragment;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONObject;

import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 */
public class OrderFragment  extends DialogFragment  {

    static Boolean empty;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        System.out.println("In oncreateview");

        return inflater.inflate(R.layout.fragment_order, container, false);
    }


    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        int total = 0;
        System.out.println("im in orderfragment!");
        //View v = getActivity().getLayoutInflater().inflate(R.layout.fragment_order, null);
        View v = getView();
        ListView listylistview = (ListView)v.findViewById(R.id.listt);

        // put all info in array and create listview
        restoDatabase db = restoDatabase.getInstance(getContext().getApplicationContext());
        Cursor selectedall = db.selectAll();
        selectedall.moveToFirst();
        ArrayList<String> arr = new ArrayList<>(10);
        // go trough every entry in the database and create string
        while(!selectedall.isAfterLast()) {
            String multiplicant = " * " + Integer.toString(selectedall.getInt(selectedall.getColumnIndex("amount")));
            total += selectedall.getInt(selectedall.getColumnIndex("amount")) * selectedall.getInt(selectedall.getColumnIndex("price"));
            arr.add("€" + Integer.toString(selectedall.getInt(selectedall.getColumnIndex("price"))) + multiplicant + " " + selectedall.getString(selectedall.getColumnIndex("name"))); //add the item
            selectedall.moveToNext();
        }

        // info to make order button disappear
        if (arr.size() == 0) {
            empty = true;
        } else {
            empty = false;
        }
        ArrayAdapter<String> list = new ArrayAdapter<>(getContext().getApplicationContext(), android.R.layout.simple_list_item_1, arr);
        System.out.println(arr.toString());
        listylistview.setAdapter(list);

        // set button listeners and visibility
        Button b = (Button) getView().findViewById(R.id.clear);
        b.setOnClickListener(new onAddButton());

        b = (Button) getView().findViewById(R.id.order);
        b.setText("Order for: " + Integer.toString(total));
        b.setOnClickListener(new onAddButtonOrder());

        if(empty) {
            b.setVisibility(View.INVISIBLE);
        } else {
            b.setVisibility(View.VISIBLE);
        }

    }

    // handle clicks on the clear button
    private class onAddButton implements View.OnClickListener {
        @Override
        public void onClick(View view){
            // get db, and empty it.
            restoDatabase db = restoDatabase.getInstance(getContext().getApplicationContext());
            db.clear(getContext().getApplicationContext());
            dismiss();
        }
    }

    // handle click on order button
    private class onAddButtonOrder implements View.OnClickListener {
        @Override
        public void onClick(View view){
            // get db and clear it and post order to server
            restoDatabase db = restoDatabase.getInstance(getContext().getApplicationContext());
            db.clear(getContext().getApplicationContext());
            getString("https://resto.mprog.nl/order");
            // cant order again, so invisible
            Button b = (Button) getView().findViewById(R.id.order);
            b.setVisibility(View.INVISIBLE);
        }
    }

    // post order
    public String getString(String url){
        RequestQueue queue = Volley.newRequestQueue(getContext().getApplicationContext());
        url = "https://resto.mprog.nl/order";

        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
//                        parseResponse(response);
                        try {
                            parseResponse(response);
                        }
                        catch (Exception e) {
                            parseResponse("");
                            System.out.println(e.toString());
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                System.out.println(error.toString());
            }
        });

        queue.add(stringRequest);
        return "no";
    }

    // display time left untill order arrives
    public void parseResponse(String response){
        String time;
        try{
            JSONObject prep_time = new JSONObject(response);
            time = "Order will be delivered in: " + Integer.toString(prep_time.getInt("preparation_time")) + " minutes";
        } catch (Exception e) {
            System.out.println(e.toString());
            time = "Due to an error the food will never be delivered.";
        }
        ArrayList <String> ordertime = new ArrayList<>(1);
        ordertime.add(time);
        View v = getView();
        ListView listylistview = (ListView)v.findViewById(R.id.listt);
        ArrayAdapter<String> list = new ArrayAdapter<>(getContext().getApplicationContext(), android.R.layout.simple_list_item_1, ordertime);
        listylistview.setAdapter(list);
    }

}
