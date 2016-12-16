package com.fjodor.fjodor_leagueoflegendsapp.helper;

/**
 * Fjodor van Rijsselberg
 * Student number: 11409231
 *
 * This class was made with help of the "[Android] Tuto Application League Of Legends" guide:
 *
 *      https://www.youtube.com/watch?v=W_WVYiY-uII&list=PLEubh3Rmu4tlbFDyhgO943Ewp4GPIjYqW
 *
 * Used to help display images, time and date of matches in different parts of the app.
 */

import android.content.Context;
import android.widget.ImageView;

import com.fjodor.fjodor_leagueoflegendsapp.ApiRequest;
import com.fjodor.fjodor_leagueoflegendsapp.R;
import com.squareup.picasso.Picasso;

import org.json.JSONException;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

public class Helper {

    /**
     * Uses league's URL for item images, inserting the name of the item and then displaying it
     * in the give ImageView.
     */

    public static void setImageItems(Context context, int item, ImageView image){

        if(item != 0){

            Picasso.with(context).load("http://ddragon.leagueoflegends.com/cdn/6.24.1/img/item/"+item+".png").error(R.drawable.empty).into(image);

        }
        else{
            Picasso.with(context).load(R.drawable.empty).into(image);
        }
    }

    /**
     * Converts the date of a match to a readable format.
     */

    public static String convertDate(long creation) {

        Date date = new Date(creation);
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yy");
        sdf.setTimeZone(TimeZone.getTimeZone("GMT+1"));
        return sdf.format(date);
    }

    /**
     * Converts the duration of the match to a readable format.
     */

    public static String convertDuration(long duration) {

        double total = duration / 60f;
        String formattedDuration;

        if (total < 60) {
            int minuteInt = (int) total;
            String finalMinute = String.valueOf(minuteInt);
            if (minuteInt < 10) {
                finalMinute = "0" + minuteInt;
            }
            double second = total - Math.floor(total);
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
