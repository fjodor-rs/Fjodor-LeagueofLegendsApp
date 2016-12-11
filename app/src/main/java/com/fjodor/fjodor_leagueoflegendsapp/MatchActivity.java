package com.fjodor.fjodor_leagueoflegendsapp;

import android.content.Intent;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.fjodor.fjodor_leagueoflegendsapp.entity.MatchEntity;
import com.fjodor.fjodor_leagueoflegendsapp.helper.Helper;
import com.squareup.picasso.Picasso;

import java.util.HashMap;
import java.util.Iterator;

public class MatchActivity extends AppCompatActivity {

    private RelativeLayout relativeLayout;
    private TextView typeMatch, level, gold, cs, kda, duration, creation;
    private ImageView portrait, sum1, sum2, item1, item2, item3, item4, item5, item6, item7, winner1, winner2, winner3, winner4, winner5, loser1, loser2, loser3, loser4, loser5;
    private TableLayout statistics;
    private RequestQueue queue;
    private ApiRequest request;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_match);

        initialize();

        Intent intent = getIntent();
        if(intent.hasExtra("ONE_MATCH")){

            MatchEntity oneMatch = (MatchEntity) intent.getSerializableExtra("ONE_MATCH");

            queue = MySingleton.getInstance(this).getRequestQueue();
            request = new ApiRequest(queue, this);

            if(oneMatch.isWinner()){
                int color_win = ContextCompat.getColor(this, R.color.win_row_bg);
                relativeLayout.setBackgroundColor(color_win);
            }
            else{
                int color_lose = ContextCompat.getColor(this, R.color.lose_row_bg);
                relativeLayout.setBackgroundColor(color_lose);
            }

            Picasso.with(this).load("http://ddragon.leagueoflegends.com/cdn/6.24.1/img/champion/"+oneMatch.getChampName()).into(portrait);
            if(oneMatch.getSum1().equals("Default")){
                Picasso.with(this).load(R.drawable.common_google_signin_btn_icon_dark).into(sum1);
            }
            else{
                Picasso.with(this).load("http://ddragon.leagueoflegends.com/cdn/6.24.1/img/spell/"+oneMatch.getSum1()).into(sum1);
            }

            if(oneMatch.getSum1().equals("Default")){
                Picasso.with(this).load(R.drawable.common_google_signin_btn_icon_dark).into(sum2);
            }
            else{
                Picasso.with(this).load("http://ddragon.leagueoflegends.com/cdn/6.24.1/img/spell/"+oneMatch.getSum2()).into(sum2);
            }

            Helper.setImageItems(this, oneMatch.getItems()[0], item1);
            Helper.setImageItems(this, oneMatch.getItems()[1], item2);
            Helper.setImageItems(this, oneMatch.getItems()[2], item3);
            Helper.setImageItems(this, oneMatch.getItems()[3], item4);
            Helper.setImageItems(this, oneMatch.getItems()[4], item5);
            Helper.setImageItems(this, oneMatch.getItems()[5], item6);
            Helper.setImageItems(this, oneMatch.getItems()[6], item7);

            typeMatch.setText(oneMatch.getTypeMatch());
            level.setText("Level " + String.valueOf(oneMatch.getChamplevel()));
            String money = String.valueOf(Math.round(oneMatch.getGold()/1000.0))+"K";
            gold.setText(money);
            cs.setText(String.valueOf(oneMatch.getCs()));
            kda.setText(oneMatch.getKills() + "/" + oneMatch.getDeaths() + "/" + oneMatch.getAssists());
            duration.setText(Helper.convertDuration(oneMatch.getMatchDuration()));
            creation.setText(Helper.convertDate(oneMatch.getMatchCreation()));

            float density = getResources().getDisplayMetrics().density;
            int size = (int) (70 * density);

            Helper.setImagePortrait(this, oneMatch.getTeamWinner().get(0), winner1, request);
            Helper.setImagePortrait(this, oneMatch.getTeamWinner().get(1), winner2, request);
            Helper.setImagePortrait(this, oneMatch.getTeamWinner().get(2), winner3, request);
            Helper.setImagePortrait(this, oneMatch.getTeamWinner().get(3), winner4, request);


            if(oneMatch.getChampId() == oneMatch.getTeamWinner().get(4) && oneMatch.isWinner() == true){
                winner5.getLayoutParams().height = size;
                winner5.getLayoutParams().width = size;
            }
            Helper.setImagePortrait(this, oneMatch.getTeamWinner().get(4), winner5, request);


            Helper.setImagePortrait(this, oneMatch.getTeamLoser().get(0), loser1, request);
            Helper.setImagePortrait(this, oneMatch.getTeamLoser().get(1), loser2, request);
            Helper.setImagePortrait(this, oneMatch.getTeamLoser().get(2), loser3, request);
            Helper.setImagePortrait(this, oneMatch.getTeamLoser().get(3), loser4, request);


            if(oneMatch.getChampId() == oneMatch.getTeamLoser().get(4) && oneMatch.isWinner() == false){
                loser5.getLayoutParams().height = size;
                loser5.getLayoutParams().width = size;
            }
            Helper.setImagePortrait(this, oneMatch.getTeamLoser().get(4), loser5, request);

            Iterator iterator = oneMatch.getStats().entrySet().iterator();
            while(iterator.hasNext()){

                HashMap.Entry key = (HashMap.Entry) iterator.next();

                TableRow row = new TableRow(this);
                TextView tv_key = new TextView(this);
                tv_key.setTextSize(15);
                TextView tv_value = new TextView(this);
                tv_value.setTextSize(15);
                row.addView(tv_key);
                row.addView(tv_value);

                LinearLayout.LayoutParams key_params = (LinearLayout.LayoutParams) tv_key.getLayoutParams();
                key_params.width = 0;
                key_params.height = 1;
                key_params.height = (int)(20*density);
                tv_key.setLayoutParams(key_params);

                LinearLayout.LayoutParams value_params = (LinearLayout.LayoutParams) tv_value.getLayoutParams();
                value_params.gravity = Gravity.RIGHT;
                value_params.height = (int)(20*density);
                tv_value.setLayoutParams(value_params);

                tv_key.setText(key.getKey().toString());
                tv_value.setText(key.getValue().toString());

                statistics.addView(row, new TableLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
            }
        }
    }

    public void initialize(){

        relativeLayout = (RelativeLayout) findViewById(R.id.relative_layout);
        portrait = (ImageView) findViewById(R.id.details_portrait);
        sum1 = (ImageView) findViewById(R.id.details_sum1);
        sum2 = (ImageView) findViewById(R.id.details_sum2);
        typeMatch = (TextView) findViewById(R.id.detail_match);
        level = (TextView) findViewById(R.id.details_level);
        gold = (TextView) findViewById(R.id.tv_details_gold);
        cs = (TextView) findViewById(R.id.tv_details_cs);
        kda = (TextView) findViewById(R.id.tv_details_kda);
        duration = (TextView) findViewById(R.id.tv_details_duration);
        creation = (TextView) findViewById(R.id.tv_details_creation);
        item1 = (ImageView) findViewById(R.id.iv_details_item1);
        item2 = (ImageView) findViewById(R.id.iv_details_item2);
        item3 = (ImageView) findViewById(R.id.iv_details_item3);
        item4 = (ImageView) findViewById(R.id.iv_details_item4);
        item5 = (ImageView) findViewById(R.id.iv_details_item5);
        item6 = (ImageView) findViewById(R.id.iv_details_item6);
        item7 = (ImageView) findViewById(R.id.iv_details_item7);
        winner1 = (ImageView) findViewById(R.id.iv_winner1);
        winner2 = (ImageView) findViewById(R.id.iv_winner2);
        winner3 = (ImageView) findViewById(R.id.iv_winner3);
        winner4 = (ImageView) findViewById(R.id.iv_winner4);
        winner5 = (ImageView) findViewById(R.id.iv_winner5);
        loser1 = (ImageView) findViewById(R.id.iv_loser1);
        loser2 = (ImageView) findViewById(R.id.iv_loser2);
        loser3 = (ImageView) findViewById(R.id.iv_loser3);
        loser4 = (ImageView) findViewById(R.id.iv_loser4);
        loser5 = (ImageView) findViewById(R.id.iv_loser5);
        statistics = (TableLayout) findViewById(R.id.table_details);


    }

}
