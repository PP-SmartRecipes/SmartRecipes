package com.example.smartrecipes;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ExpandableListView;
import android.widget.ExpandableListView.OnChildClickListener;
import android.widget.ExpandableListView.OnGroupClickListener;
import android.widget.ExpandableListView.OnGroupCollapseListener;
import android.widget.ExpandableListView.OnGroupExpandListener;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class ShoppingList extends AppCompatActivity {

    ExpandableListAdapter listAdapter;
    ExpandableListView expListView;
    static List<String> listDataHeader;
    static HashMap<String, List<String>> listDataChild;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()) {
            case R.id.account:
                Intent intent = new Intent(this, UserSettings.class);
                this.startActivity(intent);
                break;
            default:
                return super.onOptionsItemSelected(item);
        }
        return true;
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shopping_list);

        try
        {
            if (listDataHeader.isEmpty()){
            }
        }
        catch(NullPointerException e)
        {
            listDataHeader = new ArrayList<>();
            listDataChild = new HashMap<>();
        }

        // get the listview
        expListView = (ExpandableListView) findViewById(R.id.lvExp);

        // preparing list data
//        prepareListData();

        listAdapter = new ExpandableListAdapter(this, listDataHeader, listDataChild);

        // setting list adapter

        expListView.setAdapter(listAdapter);

        // Listview Group click listener
        expListView.setOnGroupClickListener(new OnGroupClickListener() {

            @Override
            public boolean onGroupClick(ExpandableListView parent, View v,
                                        int groupPosition, long id) {
                // Toast.makeText(getApplicationContext(),
                // "Group Clicked " + listDataHeader.get(groupPosition),
                // Toast.LENGTH_SHORT).show();
                return false;
            }
        });

        // Listview Group expanded listener
        expListView.setOnGroupExpandListener(new OnGroupExpandListener() {

            @Override
            public void onGroupExpand(int groupPosition) {
            }
        });

        // Listview Group collasped listener
        expListView.setOnGroupCollapseListener(new OnGroupCollapseListener() {

            @Override
            public void onGroupCollapse(int groupPosition) {

            }
        });

        // Listview on child click listener
        expListView.setOnChildClickListener(new OnChildClickListener() {

            @Override
            public boolean onChildClick(ExpandableListView parent, View v,
                                        int groupPosition, int childPosition, long id) {
                List<String> deleted = listDataChild.get(listDataHeader.get(groupPosition));
                deleted.remove(childPosition);
                listDataChild.replace(listDataHeader.get(groupPosition),deleted);
                listAdapter.notifyDataSetChanged();
                listDataChild.replace(listDataHeader.get(groupPosition),deleted);
                if(deleted.size()==0){
                    listDataHeader.remove(groupPosition);
                }
                listAdapter.notifyDataSetChanged();
                return false;
            }
        });


        //Inizialize and assign variable
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);

        //Set Home selected
        bottomNavigationView.setSelectedItemId(R.id.shopping);

        //ItemSelectedListener
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                switch (menuItem.getItemId()) {
                    case R.id.category:
                        startActivity(new Intent(getApplicationContext(),Category.class));
                        overridePendingTransition(0,0);
                        return true;
                    case R.id.home:
                        startActivity(new Intent(getApplicationContext(),MainActivity.class));
                        overridePendingTransition(0,0);
                        return true;
                    case R.id.history:
                        startActivity(new Intent(getApplicationContext(),History.class));
                        overridePendingTransition(0,0);
                        return true;
                    case R.id.shopping:
                        return true;
                    case R.id.favorite:
                        startActivity(new Intent(getApplicationContext(),History.class));
                        overridePendingTransition(0,0);
                        return true;
                }
                return false;
            }
        });

    }

    public static void setListDataHeader(List<String> listaDataHeader){
        listDataHeader = listaDataHeader;
    }

    public static List<String> getListDataHeader(){
        return listDataHeader;
    }

    public static void setListDataChild(HashMap<String, List<String>> listaDataChild){
        listDataChild = listaDataChild;
    }

    public static HashMap<String, List<String>> getListDataChild() {
        return listDataChild;
    }

}
