package com.fjodor.fjodor_leagueoflegendsapp;

/**
 * Fjodor van Rijsselberg
 * Student number: 11409231
 *
 * This activity was made with help of the "[Android] Tuto Application League Of Legends" guide:
 *
 *      https://www.youtube.com/watch?v=W_WVYiY-uII&list=PLEubh3Rmu4tlbFDyhgO943Ewp4GPIjYqW
 *
 * In this activity you can look for other player's match history, it remembers the last player
 * you looked for.
 */

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;

public class SearchActivity extends AppCompatActivity {

    private EditText nameEdit;
    private ProgressBar pbSearch;
    private ApiRequest request;
    private Handler handler;
    private SharedPreferences.Editor spEditor;
    private String recentSearch;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        SharedPreferences sharedPreferences = this.getSharedPreferences("lolPrefs", 0);
        spEditor = sharedPreferences.edit();

        recentSearch = sharedPreferences.getString("RECENT", null);

        RequestQueue queue = MySingleton.getInstance(this).getRequestQueue();
        request = new ApiRequest(queue, this);
        handler = new Handler();

        nameEdit = (EditText) findViewById(R.id.name_edit);
        Button btnSearch = (Button) findViewById(R.id.btn_search);
        pbSearch = (ProgressBar) findViewById(R.id.pb_search);
        TextView recent = (TextView) findViewById(R.id.recent);

        if(recentSearch != null){
            recent.setText(recentSearch);

            recent.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {

                            request.checkPlayerName(recentSearch, new ApiRequest.CheckPlayerCallback() {
                                @Override
                                public void onSuccess(String name, long id) {
                                    pbSearch.setVisibility(View.INVISIBLE);
                                    Intent intent = new Intent(getApplicationContext(), MatchHistoryActivity.class);
                                    Bundle extras = new Bundle();
                                    extras.putString("NAME", name);
                                    extras.putLong("ID", id);
                                    intent.putExtras(extras);

                                    startActivity(intent);
                                }
                                @Override
                                public void dontExist(String message) {
                                    pbSearch.setVisibility(View.INVISIBLE);
                                    Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
                                }
                                @Override
                                public void onError(String message) {
                                    pbSearch.setVisibility(View.INVISIBLE);
                                    Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
                                }
                            });
                        }
                    }, 2000);
                }
            });
        }
        btnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String name = nameEdit.getText().toString().trim();

                if(name.length() > 0){
                    pbSearch.setVisibility(View.VISIBLE);

                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {

                            request.checkPlayerName(name, new ApiRequest.CheckPlayerCallback() {
                                @Override
                                public void onSuccess(String name, long id) {
                                    pbSearch.setVisibility(View.INVISIBLE);
                                    Intent intent = new Intent(getApplicationContext(), MatchHistoryActivity.class);
                                    Bundle extras = new Bundle();
                                    extras.putString("NAME", name);
                                    extras.putLong("ID", id);
                                    intent.putExtras(extras);

                                    spEditor.putString("RECENT", name);
                                    spEditor.commit();
                                    startActivity(intent);
                                }

                                @Override
                                public void dontExist(String message) {
                                    pbSearch.setVisibility(View.INVISIBLE);
                                    Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
                                }

                                @Override
                                public void onError(String message) {
                                    pbSearch.setVisibility(View.INVISIBLE);
                                    Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
                                }
                            });

                        }
                    }, 2000);
                }else{
                    Toast.makeText(getApplicationContext(), "Fill in the name of a player", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
