package com.fjodor.fjodor_leagueoflegendsapp.helper;

import android.content.Context;
import android.widget.ImageView;

import com.fjodor.fjodor_leagueoflegendsapp.ApiRequest;
import com.squareup.picasso.Picasso;

import org.json.JSONException;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

/**
 * Created by Fjodor on 2016/12/07.
 */

public class Helper {

    public static void setImageItems(Context context, int item, ImageView image){

        if(item != 0){

            Picasso.with(context).load("http://ddragon.leagueoflegends.com/cdn/6.24.1/img/item/"+item+".png").error(android.support.design.R.drawable.abc_ic_star_black_16dp).into(image);

        }
        else{
            Picasso.with(context).load(android.support.design.R.drawable.abc_ic_star_black_16dp).into(image);
        }

    }

    public static String convertDate(long creation) {

        Date date = new Date(creation);
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yy");
        sdf.setTimeZone(TimeZone.getTimeZone("GMT+1"));
        String formattedDate = sdf.format(date);
        return formattedDate;
    }

    public static String convertDuration(long duration) {

        double total = duration / 60f;
        String formattedDuration;

        if (total < 60) {
            double minute = total;
            int minuteInt = (int) minute;
            String finalMinute = String.valueOf(minuteInt);
            if (minuteInt < 10) {
                finalMinute = "0" + minuteInt;
            }
            double second = minute - Math.floor(minute);
            second = Math.round(second * 60f);
            int secondInt = (int) second;
            String finalSecond = String.valueOf(secondInt);
            if (secondInt < 10) {
                finalSecond = "0" + secondInt;
            }
            formattedDuration = finalMinute + ":" + finalSecond;

        } else {
            double hour = total / 60f;
            int hourInt = (int) hour;
            double minute = total - (hourInt * 60f);
            int minuteInt = (int) minute;
            String finalMinute = String.valueOf(minuteInt);
            if (minuteInt < 10) {
                finalMinute = "0" + minuteInt;
            }
            double second = minute - Math.floor(minute);
            second = Math.round(second * 60f);
            int secondInt = (int) second;
            String finalSecond = String.valueOf(secondInt);
            if (secondInt < 10) {
                finalSecond = "0" + secondInt;
            }
            formattedDuration = String.valueOf(hourInt) + ":" + finalMinute + ":" + finalSecond;
        }
        return formattedDuration;
    }

    public static void setImagePortrait(Context context, int champId, ImageView image, ApiRequest request){

        String portrait = null;

        try {
            portrait = request.getChampionName(champId);
            Picasso.with(context).load("http://ddragon.leagueoflegends.com/cdn/6.24.1/img/champion/"+portrait).into(image);
        } catch (JSONException e) {
            e.printStackTrace();
        }


    }

}