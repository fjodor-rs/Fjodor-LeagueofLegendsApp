package com.fjodor.fjodor_leagueoflegendsapp;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;

public class MainActivity extends AppCompatActivity {




//    Log in met een account en koppel die aan je username van je league account
//    Dit haalt je stats op, hierna kun je zoeken op de stats van andere mensen
//    dit zet ze naast elkaar waarna je duidelijk kunt zien waar je beter of slechter in bent


    private EditText nameEdit;
    private Button btnSearch;
    private ProgressBar pbSearch;
    private TextView recent;
    private RequestQueue queue;
    private ApiRequest request;
    private Handler handler;
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor spEditor;
    private String recentSearch;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        sharedPreferences = this.getSharedPreferences("lolPrefs", 0);
        spEditor = sharedPreferences.edit();

        recentSearch = sharedPreferences.getString("RECENT", null);

        queue = MySingleton.getInstance(this).getRequestQueue();
        request = new ApiRequest(queue, this);
        handler = new Handler();

        nameEdit = (EditText) findViewById(R.id.name_edit);
        btnSearch = (Button) findViewById(R.id.btn_search);
        pbSearch = (ProgressBar) findViewById(R.id.pb_search);
        recent = (TextView) findViewById(R.id.recent);

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
                                    Intent intent = new Intent(getApplicationContext(), PlayerInfoActivity.class);
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
                                    Intent intent = new Intent(getApplicationContext(), PlayerInfoActivity.class);
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
                }
                else{
                    Toast.makeText(getApplicationContext(), "Fill in the name of a player", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
