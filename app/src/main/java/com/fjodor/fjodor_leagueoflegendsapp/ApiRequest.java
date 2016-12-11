package com.fjodor.fjodor_leagueoflegendsapp;

import android.content.Context;
import android.util.Log;

import com.android.volley.NetworkError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.ServerError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.fjodor.fjodor_leagueoflegendsapp.entity.MatchEntity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.security.Key;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

/**
 * Created by Fjodor on 2016/12/06.
 */

public class ApiRequest {

    private RequestQueue queue;
    private Context context;
    private final static String key = "RGAPI-af358aaf-46c5-4646-9f13-c0d348ec5c2d";
    private String region_key = "euw";

    public ApiRequest(RequestQueue queue, Context context){
        this.queue =  queue;
        this.context = context;
    }

    public void checkPlayerName(final String name, final CheckPlayerCallback callback){

        String url = "https://"+region_key+".api.pvp.net/api/lol/"+region_key+"/v1.4/summoner/by-name/"+name+"?api_key="+key;

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {

                Log.d("APP", response.toString());

                try {
                    JSONObject json = response.getJSONObject(name.toLowerCase());
                    String name = json.getString("name");
                    long id = json.getLong("id");
                    callback.onSuccess(name, id);


                } catch (JSONException e) {
                    Log.d("APP", "EXCEPTION =" + e);
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                if(error instanceof NetworkError){
                    callback.onError("Cannot connect to network");
                }else if(error instanceof ServerError){
                    callback.dontExist("The player does not exist");
                }
                Log.d("APP", "ERROR = " + error);
            }
        });

        queue.add(request);
    }

    public interface CheckPlayerCallback{
        void onSuccess(String name, long id);
        void dontExist(String message);
        void onError(String message);
    }

    public String getJsonFile(Context context, String filename){

        String json = null;

        try {
            InputStream inputstream = context.getAssets().open(filename);
            int size = inputstream.available();
            byte[] buffer = new byte[size];
            inputstream.read(buffer);
            json = new String(buffer, "UTF-8");


        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }

        return json;
    }

    public String getChampionName(int champId) throws JSONException{

        String json = getJsonFile(context, "champion.json");

        JSONObject champ = new JSONObject(json);
        JSONObject data = champ.getJSONObject("data");
        JSONObject champInfo = null;
        if(data.has(String.valueOf(champId))){
            champInfo = data.getJSONObject(String.valueOf(champId));
        }
        String champName = "Default";
        if(champInfo != null){
            JSONObject image = champInfo.getJSONObject("image");
            champName = image.getString("full");
        }

        return champName;

    }

    public String getSummonerName(int spellId) throws JSONException{

        String json = getJsonFile(context, "champion.json");

        JSONObject summoner = new JSONObject(json);
        JSONObject data = summoner.getJSONObject("data");
        JSONObject summonerInfo = null;
        if(data.has(String.valueOf(spellId))){
            summonerInfo = data.getJSONObject(String.valueOf(spellId));
        }
        String summonerName = "Default";
        if(summonerInfo != null){
            JSONObject image = summonerInfo.getJSONObject("image");
            summonerName = image.getString("full");
        }
        return summonerName;
    }

    public void getHistoryMatches(long id, final HistoryCallback callback){

        String url = "https://"+region_key+".api.pvp.net/api/lol/"+region_key+"/v1.3/game/by-summoner/"+id+"/recent?api_key="+key;

        JsonObjectRequest request = new JsonObjectRequest (Request.Method.GET, url, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {


                List<Integer> teamWinners = new ArrayList<>();
                List<Integer> teamLosers = new ArrayList<>();
                Integer[] items = new Integer[7];
                LinkedHashMap<String, Integer> statistics = new LinkedHashMap<>();
                List<MatchEntity> historyMatches = new ArrayList<>();


                if(response.length() > 0){

                    try {
                        JSONArray games = response.getJSONArray("games");

                        for(int i = 0; i < games.length(); i++){

                            JSONObject oneMatch = games.getJSONObject(i);
                            long matchId = oneMatch.getLong("gameId");
                            long matchCreation = oneMatch.getLong("createDate");
                            String typeMatch = oneMatch.getString("subType");
                            typeMatch = typeMatch.replace("_", " ");
                            int champId = oneMatch.getInt("championId");
                            int sum1 = oneMatch.getInt("spell1");
                            int sum2 = oneMatch.getInt("spell2");
                            int teamId = oneMatch.getInt("teamId");
                            JSONObject stats = oneMatch.getJSONObject("stats");
                            int champLevel = stats.getInt("level");
                            boolean win = stats.getBoolean("win");
                            JSONArray fellowPlayers = oneMatch.getJSONArray("fellowPlayers");

                            for(int j = 0; j < fellowPlayers.length(); j++){

                                JSONObject player = fellowPlayers.getJSONObject(j);

                                if(win){
                                    if(player.getInt("teamId") == teamId){
                                        teamWinners.add(player.getInt("championId"));
                                    }
                                    else{
                                        teamLosers.add(player.getInt("championId"));
                                    }
                                }
                                else{
                                    if(player.getInt("teamId") != teamId){
                                        teamWinners.add(player.getInt("championId"));
                                    }
                                    else{
                                        teamLosers.add(player.getInt("championId"));
                                    }
                                }
                            }

                            if(win){
                                teamWinners.add(champId);
                            }
                            else{
                                teamLosers.add(champId);
                            }

                            int kills = 0;
                            if(stats.has("championsKilled")){
                                kills = stats.getInt("championsKilled");
                            }
                            int deaths = 0;
                            if(stats.has("numDeaths")){
                                deaths = stats.getInt("numDeaths");
                            }
                            int assists = 0;
                            if(stats.has("assists")){
                                assists = stats.getInt("assists");
                            }
                            int gold = 0;
                            if(stats.has("goldEarned")){
                                gold = stats.getInt("goldEarned");
                            }
                            int minionsKilled = 0;
                            int neutralMinionsKilled = 0;
                            if(stats.has("minionsKilled")){
                                minionsKilled = stats.getInt("minionsKilled");
                            }
                            if(stats.has("neutralMinionsKilled")){
                                neutralMinionsKilled = stats.getInt("neutralMinionsKilled");
                            }
                            int cs = minionsKilled + neutralMinionsKilled;
                            long matchDuration = stats.getLong("timePlayed");

                            int damageDealt = stats.getInt("totalDamageDealt");
                            int damageTaken = stats.getInt("totalDamageTaken");
                            int turretsKilled = 0, physicalDamageDealt = 0, magicDamageDealt = 0, physicalDamageTaken = 0, magicDamageTaken = 0;
                            if(stats.has("turretsKilled")){
                                turretsKilled = stats.getInt("turretsKilled");
                            }
                            if(stats.has("physicalDamageDealtPlayer")){
                                physicalDamageDealt = stats.getInt("physicalDamageDealtPlayer");
                            }
                            if(stats.has("magicDamageDealtPlayer")){
                                magicDamageDealt = stats.getInt("magicDamageDealtPlayer");
                            }
                            if(stats.has("physicalDamageTaken")){
                                physicalDamageTaken = stats.getInt("physicalDamageTaken");
                            }
                            if(stats.has("magicDamageTaken")){
                                magicDamageTaken = stats.getInt("magicDamageTaken");
                            }

                            statistics.put("Damage dealt", damageDealt);
                            statistics.put("Damage taken", damageTaken);
                            statistics.put("Physical damage dealt", physicalDamageDealt);
                            statistics.put("Magical damage dealt", magicDamageDealt);
                            statistics.put("Physical damage taken", physicalDamageTaken);
                            statistics.put("Magical damage taken", magicDamageTaken);
                            statistics.put("Turrets killed", turretsKilled);

                            for(int j = 0; j <= 6; j++){
                                String item = "item"+j;
                                if(stats.has(item)){
                                    items[j] = stats.getInt(item);
                                }
                                else{
                                    items[j] = 0;
                                }
                            }

                            String champName = getChampionName(champId);
                            String sum1Name = getSummonerName(sum1);
                            String sum2Name = getSummonerName(sum2);

                            MatchEntity singleMatch = new MatchEntity(win, matchId, matchCreation, matchDuration, champId, kills, gold, deaths, assists, champLevel, cs, items, statistics, sum1Name, sum2Name, champName, typeMatch, teamWinners, teamLosers);

                            historyMatches.add(singleMatch);
                            items = new Integer[7];
                            teamWinners = new ArrayList<>();
                            teamLosers = new ArrayList<>();
                            statistics = new LinkedHashMap<>();
                        }

                        callback.onSucces(historyMatches);


                    } catch (JSONException e) {
                         Log.d("APP", "EXCEPTION HISTORY = " + e);
                        e.printStackTrace();
                    }


                }

                else{
                    callback.noMatch("No Match");
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                if(error instanceof NetworkError) {
                    callback.onError("Cannot connect to network");
                }

            }
        });

        queue.add(request);

    }

    public interface HistoryCallback{
        void onSucces(List<MatchEntity> matches);
        void noMatch(String message);
        void onError(String message);

    }



}