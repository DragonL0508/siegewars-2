package me.dragonl.siegewars.player.data;

import me.dragonl.siegewars.game.shop.Commodity;

import javax.persistence.criteria.CriteriaBuilder;
import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.Map;

public class PlayerData {
    private Integer kills = 0;
    private Integer deaths = 0;
    private Integer money = 999999;
    private Integer score = 0;
    private Integer assist = 0;
    private Map<Commodity, Integer> buyCounts = new HashMap<>();

    public Map<Commodity, Integer> getBuyCountsMap() {
        return buyCounts;
    }

    public Integer getBuyCounts(Commodity item){
        return buyCounts.getOrDefault(item, 0);
    }

    public void addBuyCounts(Commodity item, Integer counts) {
        buyCounts.put(item, buyCounts.getOrDefault(item, 0) + counts);
    }

    public Integer getAssist() {
        return assist;
    }

    public void setAssist(Integer assist) {
        this.assist = assist;
    }

    public Integer getScore() {
        return score;
    }

    public void setScore(Integer score) {
        this.score = score;
    }

    private double totalDamage = 0;

    public Integer getKills() {
        return kills;
    }

    public void setKills(Integer kills) {
        this.kills = kills;
    }

    public Integer getDeaths() {
        return deaths;
    }

    public void setDeaths(Integer deaths) {
        this.deaths = deaths;
    }

    public Integer getMoney() {
        return money;
    }

    public void setMoney(Integer money) {
        this.money = money;
    }

    public double getTotalDamage() {
        return totalDamage;
    }

    public void setTotalDamage(double totalDamage) {
        this.totalDamage = totalDamage;
    }

    public String getFormatDamage() {
        DecimalFormat format = new DecimalFormat("#.#");
        return format.format(new Double(this.totalDamage));
    }
}
