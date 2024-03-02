package me.dragonl.siegewars.player.data;

public class PlayerData {
    private Integer kills = 0;
    private Integer deaths = 0;
    private Integer money = 0;
    private Integer score = 0;

    public Integer getScore() {
        return score;
    }

    public void setScore(Integer score) {
        this.score = score;
    }

    private float totalDamage = 0.0F;

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

    public float getTotalDamage() {
        return totalDamage;
    }

    public void setTotalDamage(float totalDamage) {
        this.totalDamage = totalDamage;
    }
}
