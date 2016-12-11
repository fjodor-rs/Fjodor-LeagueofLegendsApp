package com.fjodor.fjodor_leagueoflegendsapp;

import android.content.Intent;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.fjodor.fjodor_leagueoflegendsapp.adapter.MyAdapter;
import com.fjodor.fjodor_leagueoflegendsapp.entity.MatchEntity;

import java.util.ArrayList;
import java.util.List;

public class PlayerInfoActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{

    private String playerName;
    private Long playerId;
    private Toolbar toolbar;
    private DrawerLayout drawerLayout;
    private NavigationView navigationView;
    private RecyclerView recycleView;
    private MyAdapter adapter;
    private ApiRequest request;
    private RequestQueue queue;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_player_info);

        queue = MySingleton.getInstance(this).getRequestQueue();
        request = new ApiRequest(queue, this);

        String json = request.getJsonFile(this, "champion.json");
        Log.d("APP", json);

        Intent intent = getIntent();
        Bundle extras = intent.getExtras();

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        if(extras.getString("NAME") != null && extras.getLong("ID") > 0){

            playerName = extras.getString("NAME");
            playerId = extras.getLong("ID");
            setTitle(playerName);
        }
        else{
            //redirect to MainActivity
        }

        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        navigationView = (NavigationView) findViewById(R.id.navigation_view);
        navigationView.setItemIconTintList(null);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.open, R.string.close){

            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
            }

            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);
            }
        };

        drawerLayout.addDrawerListener (toggle);
        toggle.syncState();
        navigationView.setNavigationItemSelectedListener(this);


        recycleView = (RecyclerView) findViewById(R.id.recycle_view);
        recycleView.setLayoutManager(new LinearLayoutManager(this));
        recycleView.setHasFixedSize(true);

        request.getHistoryMatches(playerId, new ApiRequest.HistoryCallback() {
            @Override
            public void onSucces(List<MatchEntity> matches) {
                adapter = new MyAdapter(getApplicationContext(), matches);
                recycleView.setAdapter(adapter);
            }

            @Override
            public void noMatch(String message) {
                Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onError(String message) {
                Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {

        if(item.isChecked()){
            item.setChecked(false);

        }
        else{
            item.setChecked(true);
        }

        drawerLayout.closeDrawers();

        switch (item.getItemId()){
            case R.id.main_menu:
                Toast.makeText(getApplicationContext(), "Main Menu", Toast.LENGTH_SHORT).show();
                return true;
            case R.id.search:
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intent);
                finish();
                return true;
            case R.id.stats :
                Toast.makeText(getApplicationContext(), "Stats", Toast.LENGTH_SHORT).show();
                return true;
        }
        return true;
    }
}
