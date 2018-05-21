package edu.brandeis.cs.walkingfoodies.walkingfood.backend;


import java.util.ArrayList;
import java.util.List;

import edu.brandeis.cs.walkingfoodies.walkingfood.utils.MealEntry;
import edu.brandeis.cs.walkingfoodies.walkingfood.R;

/**
 * Created by 聂聂聂 on 11/13/2016.
 */
public class CaloriesBackend {
    public List<MealEntry> breakfast;

    public void setup() {
        this.breakfast = setUpBreakfast();
    }

    private List<MealEntry> setUpBreakfast() {
        List<MealEntry> list = new ArrayList<>();
        list.add(new MealEntry(R.drawable.egg_trans, "Egg", 78.0, false));
        list.add(new MealEntry(R.drawable.milk_trans, "Milk", 103.0, false));
        list.add(new MealEntry(R.drawable.eat, "Test", 10.0, false));

        return list;
    }
}
