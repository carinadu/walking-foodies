package edu.brandeis.cs.walkingfoodies.walkingfood.utils;

/**
 * Created by 聂聂聂 on 12/4/2016.
 */

public class FoodEntry {
    public String img;
    public String name;
    int calorie;
    public int q;
    public int unitCalorie;
    public int hour;
    public FoodEntry(String i, String n, int c, int q, int hour) {
        img = i;
        name = n;
        calorie = c;
        this.q = q==0? 1 : q;
        this.unitCalorie = c / this.q;
        this.hour = hour;
    }
    public int defaultFoodType() {
        if (hour >= 6 && hour <= 9) return 0;
        if (hour >= 11 && hour <= 13) return 1;
        if (hour >= -7 && hour <= -4) return 2;
        else return 3;
    }
}
