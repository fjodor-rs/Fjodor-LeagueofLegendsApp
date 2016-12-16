package com.fjodor.fjodor_leagueoflegendsapp.entity;

/**
 * Fjodor van Rijsselberg
 * Student number: 11409231
 *
 * This activity was made with help of the "[Android] Tuto Application League Of Legends" guide:
 *
 *      https://www.youtube.com/watch?v=W_WVYiY-uII&list=PLEubh3Rmu4tlbFDyhgO943Ewp4GPIjYqW
 *
 * Stores all the data of one match in one place, and makes it easy to acquire the
 * data with 'get' functions for all the different stats
 */

import java.io.Serializable;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;


public class MatchEntity implements Serializable {

    private boolean winner;
    private long matchId, matchCreation, matchDuration;
    private int champId, kills, deaths, assists, gold, cs, champlevel;
    private LinkedHashMap<String, Integer> stats = new LinkedHashMap<>();
    private Integer[] items = new Integer[7];
    private String sum1, sum2, champName, typeMatch;
    private List<Integer> teamWinner = new ArrayList<>();
    private List<Integer> teamLoser = new ArrayList<>();


    public MatchEntity(boolean winner, long matchId, long matchCreation, long matchDuration, int champId, int kills, int gold, int deaths, int assists, int champlevel, int cs, Integer[] items, LinkedHashMap<String, Integer> stats, String sum1, String sum2, String champName, String typeMatch, List<Integer> teamWinner, List<Integer> teamLoser) {
        this.winner = winner;
        this.matchId = matchId;
        this.matchCreation = matchCreation;
        this.matchDuration = matchDuration;
        this.champId = champId;
        this.kills = kills;
        this.gold = gold;
        this.deaths = deaths;
        this.assists = assists;
        this.champlevel = champlevel;
        this.cs = cs;
        this.items = items;
        this.stats = stats;
        this.sum1 = sum1;
        this.sum2 = sum2;
        this.champName = champName;
        this.typeMatch = typeMatch;
        this.teamWinner = teamWinner;
        this.teamLoser = teamLoser;
    }

    public long getMatchId() {
        return matchId;
    }

    public long getMatchCreation() {
        return matchCreation;
    }

    public boolean isWinner() {
        return winner;
    }

    public long getMatchDuration() {
        return matchDuration;
    }

    public int getChampId() {
        return champId;
    }

    public int getDeaths() {
        return deaths;
    }

    public int getKills() {
        return kills;
    }

    public int getAssists() {
        return assists;
    }

    public int getGold() {
        return gold;
    }

    public int getCs() {
        return cs;
    }

    public int getChamplevel() {
        return champlevel;
    }

    public Integer[] getItems() {
        return items;
    }

    public String getSum1() {
        return sum1;
    }

    public LinkedHashMap<String, Integer> getStats() {
        return stats;
    }

    public String getSum2() {
        return sum2;
    }

    public String getTypeMatch() {
        return typeMatch;
    }

    public String getChampName() {
        return champName;
    }

    public List<Integer> getTeamWinner() {
        return teamWinner;
    }

    public List<Integer> getTeamLoser() {
        return teamLoser;
    }
}
