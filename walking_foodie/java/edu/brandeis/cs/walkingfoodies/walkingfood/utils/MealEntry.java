package edu.brandeis.cs.walkingfoodies.walkingfood.utils;


/**
 * Created by 聂聂聂 on 11/15/2016.
 * 这个类目前用来填充左边的viewPager
 */

public class MealEntry {
    public int image;
    public String name;
    public double calory;
    public boolean checked;
    public MealEntry(int i, String n, double c, boolean checked) {
        image = i;
        name = n;
        calory = c;
        this.checked = checked;
    }
}
