package com.example.douwe.pset5;

import android.database.Cursor;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    public restoDatabase databaas;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        FragmentManager fm = getSupportFragmentManager();
        BlankFragment fragment = new BlankFragment();
        FragmentTransaction ft = fm.beginTransaction();
        ft.replace(R.id.fragment_container, fragment, "categories");
        ft.commit();
    }

    public void addItem(String stringy, int completed){
        restoDatabase db = restoDatabase.getInstance(this);
        db.insert(stringy, completed);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.actions, menu);
        return super.onCreateOptionsMenu(menu);
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.item:

                restoDatabase db = restoDatabase.getInstance(this);
                System.out.println(db.selectAll().toString());

                Cursor selectedall = db.selectAll();
                selectedall.moveToFirst();
                ArrayList<String> arr = new ArrayList<>(10);
                while(!selectedall.isAfterLast()) {
                    arr.add("€" + selectedall.getString(selectedall.getColumnIndex("name"))); //add the item
                    selectedall.moveToNext();
                }

                System.out.println(arr.toString());

                Bundle args = new Bundle();
                args.putStringArrayList("arlist", arr);

                FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
                OrderFragment fragment = new OrderFragment();

                fragment.setArguments(args);
                fragment.show(ft, "dialog");

                break;
        }
        return true;
    }
}