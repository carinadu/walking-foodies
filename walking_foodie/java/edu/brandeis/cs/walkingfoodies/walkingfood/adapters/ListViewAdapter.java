package edu.brandeis.cs.walkingfoodies.walkingfood.adapters;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.daimajia.swipe.SimpleSwipeListener;
import com.daimajia.swipe.SwipeLayout;
import com.daimajia.swipe.adapters.BaseSwipeAdapter;

import java.io.InputStream;
import java.util.List;

import edu.brandeis.cs.walkingfoodies.walkingfood.R;
import edu.brandeis.cs.walkingfoodies.walkingfood.utils.FoodEntry;

public class ListViewAdapter extends BaseSwipeAdapter {

    private final List<FoodEntry> foodsList;
    private Context mContext;

    public ListViewAdapter(Context mContext, List<FoodEntry> foodsList) {
        this.mContext = mContext;
        this.foodsList = foodsList;
    }

    @Override
    public int getSwipeLayoutResourceId(int position) {
        return R.id.swipe;
    }

    @Override
    public View generateView(final int position, final ViewGroup parent) {
        View v = LayoutInflater.from(mContext).inflate(R.layout.listview_item, null);
        //final int pos = position;
        Button up_btn = (Button) v.findViewById(R.id.num_up);
        Button down_btn = (Button) v.findViewById(R.id.num_down);
        up_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                foodsList.get(position).q++;
                notifyDataSetChanged();
            }
        });
        down_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FoodEntry foodEntry = foodsList.get(position);
                if (foodEntry.q!=1) {
                    foodEntry.q--;
                    notifyDataSetChanged();
                }
            }
        });

        final SwipeLayout swipeLayout = (SwipeLayout)v.findViewById(getSwipeLayoutResourceId(position));
        //swipeLayout.addDrag(SwipeLayout.DragEdge.Left, v.findViewById(R.id.bottom_wrapper));
        swipeLayout.addSwipeListener(new SimpleSwipeListener() {
            @Override
            public void onOpen(SwipeLayout layout) {
                YoYo.with(Techniques.Tada).duration(500).delay(100).playOn(layout.findViewById(R.id.trash));
            }
        });
        v.findViewById(R.id.delete).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(mContext, "Item deleted", Toast.LENGTH_SHORT).show();
                foodsList.remove(position);
                swipeLayout.close();
                if (foodsList.size()==0) {
                    parent.setVisibility(View.GONE);
                }
                notifyDataSetChanged();
            }
        });
        return v;
    }

    @Override
    public void fillValues(int position, View convertView) {
        //TextView t = (TextView)convertView.findViewById(R.id.position);
        //t.setText((position + 1) + ".");
        TextView foodName = (TextView)convertView.findViewById(R.id.food_name);
        ImageView iv = (ImageView)convertView.findViewById(R.id.iv);
        TextView calories = (TextView)convertView.findViewById(R.id.calorie);
        TextView quantities = (TextView) convertView.findViewById(R.id.quantities);
        FoodEntry fe = foodsList.get(position);
        foodName.setText(fe.name.toUpperCase());
        new DownloadImageTask(iv).execute(fe.img);
        calories.setText(String.valueOf(fe.unitCalorie * fe.q + " CALORIES"));
        quantities.setText(String.valueOf(fe.q));
    }

    @Override
    public int getCount() {
        return foodsList.size();
    }

    @Override
    public Object getItem(int position) {
        return foodsList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    private class DownloadImageTask extends AsyncTask<String, Void, Bitmap> {
        ImageView bmImage;

        public DownloadImageTask(ImageView bmImage) {
            this.bmImage = bmImage;
        }

        protected Bitmap doInBackground(String... urls) {
            String urldisplay = urls[0];
            Bitmap mIcon11 = null;
            try {
                InputStream in = new java.net.URL(urldisplay).openStream();
                mIcon11 = BitmapFactory.decodeStream(in);
            } catch (Exception e) {
                Log.e("Error", e.getMessage());
                e.printStackTrace();
            }
            return mIcon11;
        }

        protected void onPostExecute(Bitmap result) {
            bmImage.setImageBitmap(result);
        }
    }
}
