package edu.brandeis.cs.walkingfoodies.walkingfood.fragments;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.database.DataSetObserver;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.NumberPicker;
import android.widget.ProgressBar;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import edu.brandeis.cs.walkingfoodies.walkingfood.R;
import edu.brandeis.cs.walkingfoodies.walkingfood.adapters.ListViewAdapter;
import edu.brandeis.cs.walkingfoodies.walkingfood.backend.CaloriesBackend;
import edu.brandeis.cs.walkingfoodies.walkingfood.backend.MainBackend;
import edu.brandeis.cs.walkingfoodies.walkingfood.database.DatabaseOperations;
import edu.brandeis.cs.walkingfoodies.walkingfood.database.HistoryData;
import edu.brandeis.cs.walkingfoodies.walkingfood.database.MealData;
import edu.brandeis.cs.walkingfoodies.walkingfood.database.ProfileData;
import edu.brandeis.cs.walkingfoodies.walkingfood.database.StepData;
import edu.brandeis.cs.walkingfoodies.walkingfood.frontend.MainActivity;
import edu.brandeis.cs.walkingfoodies.walkingfood.utils.FoodEntry;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * Created by 聂聂聂 on 11/29/2016.
 */
public class FoodFragment extends Fragment {
    private EditText input;
    private ListView lv;
    private ImageView preSearch;
    private List<FoodEntry> foodsList;
    private ListViewAdapter myAdapter;
    private View footer;
    private View header;
    private ProgressBar loadingBar;
    private TextView totalCalories;
    private int currentTotal = 0;
    private DatabaseOperations dbo;
    private SimpleDateFormat format;
    private TextView foodType;
    private String[] types = new String[]{"Breakfast", "Lunch", "Dinner", "Snack"};
    private int currentType = 0;
    private int currentTime = 0;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.food_fragment, container, false);

        String pattern = "yyyy-MM-dd'T'HH:mm:ss";
        format = new SimpleDateFormat(pattern);

        input = (EditText) view.findViewById(R.id.editText);
        lv = (ListView) view.findViewById(R.id.listview);
        preSearch = (ImageView) view.findViewById(R.id.pre_search);
        loadingBar = (ProgressBar) view.findViewById(R.id.loading_circle);
        loadingBar.setVisibility(View.GONE);
        foodsList = new ArrayList<>();
        myAdapter = new ListViewAdapter(getActivity(), foodsList);

        dbo = new DatabaseOperations(getContext());

        myAdapter.registerDataSetObserver(new DataSetObserver() {
            @Override
            public void onChanged() {
                updateTotalCalories();
            }
        });
        lv.setAdapter(myAdapter);
        header = getActivity().getLayoutInflater().inflate(R.layout.listview_header, null);
        footer = getActivity().getLayoutInflater().inflate(R.layout.listview_footer, null);
        totalCalories = (TextView) footer.findViewById(R.id.total_calories);
        lv.addFooterView(footer);
        lv.addHeaderView(header);
        lv.setVisibility(View.GONE);
        foodType = (TextView) footer.findViewById(R.id.food_type);

        ImageButton search = (ImageButton) view.findViewById(R.id.search);
        ImageButton save = (ImageButton) footer.findViewById(R.id.save);
        ImageButton cancel = (ImageButton) footer.findViewById(R.id.cancel);
        Button editType = (Button) footer.findViewById(R.id.edit_food_type);

        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                InputMethodManager inputManager = (InputMethodManager)
                        getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                if(getActivity().getCurrentFocus() != null)
                    inputManager.hideSoftInputFromWindow(getActivity().getCurrentFocus().
                            getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
                FoodSearchTask task = new FoodSearchTask(input.getText().toString());
                task.execute();

            }
        });
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                double oldCalories = MainBackend.getTotalCalories();
                MainBackend.setTotalCalories(currentTotal + oldCalories);
                MainActivity.appBar.setExpanded(true);
                dbo.insertToMeal(String.valueOf(currentTime), currentType, currentTotal);
                MainActivity.myPagerAdapter.notifyDataSetChanged();
            }
        });
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                foodsList.clear();
                myAdapter.notifyDataSetChanged();
                lv.setVisibility(View.GONE);
                preSearch.setVisibility(View.VISIBLE);
                input.setText("");
                //dbo.deleteAll();
                //MainActivity.myPagerAdapter.notifyDataSetChanged();
            }
        });
        editType.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                show();
            }
        });

        return view;
    }

    private void show() {
        final Dialog d = new Dialog(this.getContext());
        d.setContentView(R.layout.food_type_dialog);
        TextView bView = (TextView)d.findViewById(R.id.breakfast_text);
        bView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                foodType.setText("Breakfast");
                currentType = 0;
                d.dismiss();
            }
        });

        TextView lView = (TextView)d.findViewById(R.id.lunch_text);
        lView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                foodType.setText("Lunch");
                currentType = 1;
                d.dismiss();
            }
        });
        TextView dView = (TextView)d.findViewById(R.id.dinner_text);
        dView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                foodType.setText("Dinner");
                currentType = 2;
                d.dismiss();
            }
        });

        TextView sView = (TextView)d.findViewById(R.id.snack_text);
        sView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                foodType.setText("Snack");
                currentType = 3;
                d.dismiss();
            }
        });
        d.show();

        Window window = d.getWindow();

        window.setLayout(ActionBar.LayoutParams.MATCH_PARENT, ActionBar.LayoutParams.WRAP_CONTENT);
    }

    private void updateTotalCalories() {
        int total = 0;
        for (FoodEntry fe : foodsList){
            total += fe.q * fe.unitCalorie;
        }
        totalCalories.setText(String.valueOf("TOTAL CALORIES: " + total));
        currentTotal = total;
    }

    private class FoodSearchTask extends AsyncTask<Void, Void, String> {
        String query = "";

        public FoodSearchTask(String s) {
            if (!s.equals("")){
                query = s;
            }
        }

        @Override
        protected void onPreExecute() {
            foodsList.clear();
            myAdapter.notifyDataSetChanged();
            loadingBar.setVisibility(View.VISIBLE);
            lv.setVisibility(View.GONE);
            preSearch.setVisibility(View.GONE);
        }

        @Override
        protected String doInBackground(Void... urls) {
            // we use the OkHttp library from https://github.com/square/okhttp

            OkHttpClient client = new OkHttpClient();

            final MediaType JSON
                    = MediaType.parse("application/json; charset=utf-8");

            JSONObject json = new JSONObject();
            try {
                json.put("query", query);
                //json.put("timezone", "US/Eastern");
            } catch (JSONException e) {
                e.printStackTrace();
            }
            RequestBody body = RequestBody.create(JSON, json.toString());
            Request request =
                    new Request.Builder()
                            .addHeader("Content-Type", "application/json")
                            .addHeader("x-app-id", "a9d1eac6")
                            .addHeader("x-app-key", "623e10cbf69e78728ab862aa0b74dc8c")
                            .url("https://trackapi.nutritionix.com/v2/natural/nutrients")
                            .post(body)
                            .build();
            Response response = null;
            try {
                response = client.newCall(request).execute();
                if (response.isSuccessful()) {
                    return response.body().string();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            return "Download failed";
        }
        @Override
        protected void onPostExecute(String result) {
            if (result.equals("Download failed")){
                preSearch.setVisibility(View.VISIBLE);
                loadingBar.setVisibility(View.GONE);
                Toast.makeText(getContext(), "Oops, no result. Try again!", Toast.LENGTH_LONG).show();
                return;
            }
            try {
                JSONObject object = (JSONObject) new JSONTokener(result).nextValue();
                JSONArray foods = object.getJSONArray("foods");
                int size = foods.length();
                for (int i = 0; i < size; i++) {
                    JSONObject food = foods.getJSONObject(i);
                    String food_name = food.getString("food_name");
                    String pic = food.getJSONObject("photo").getString("thumb");
                    int calorie = food.getInt("nf_calories");
                    int quantity = food.getInt("serving_qty");
                    String time = food.getString("consumed_at");
                    Date date = new Date();
                    try {
                        date = format.parse(time.substring(0,time.indexOf('+')));
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }

                    int hour = date.getHours() - 5;

                    foodsList.add(new FoodEntry(pic, food_name, calorie, quantity, hour));
                    Log.d("Check hour:", "" + hour);
                }
//                textView.setText(food_name);
//                new DownloadImageTask(iv).execute(pic);
//                tv_Calories.setText("" + calorie);
                myAdapter.notifyDataSetChanged();
                updateTotalCalories();
                if (foodsList.size()>0){
                    currentTime = foodsList.get(0).hour;
                    currentType = foodsList.get(0).defaultFoodType();
                    foodType.setText(types[currentType]);
                }
                lv.setVisibility(View.VISIBLE);
                loadingBar.setVisibility(View.GONE);
            } catch (JSONException e) {
                e.printStackTrace();
                input.setText("JSONException");
            }
        }
    }
}
