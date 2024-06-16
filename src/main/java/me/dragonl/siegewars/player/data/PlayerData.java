package me.dragonl.siegewars.player.data;

import javax.persistence.criteria.CriteriaBuilder;
import java.text.DecimalFormat;

public class PlayerData {
    private Integer kills = 0;
    private Integer deaths = 0;
    private Integer money = 0;
    private Integer score = 0;
    private Integer assist = 0;

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

    public String getFormatDamage(){
        DecimalFormat format = new DecimalFormat("#.#");
        return format.format(new Double(this.totalDamage));
    }
}
