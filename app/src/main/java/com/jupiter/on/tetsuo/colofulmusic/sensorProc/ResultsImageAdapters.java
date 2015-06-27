package com.jupiter.on.tetsuo.colofulmusic.sensorProc;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.jupiter.on.tetsuo.colofulmusic.R;

import data.StaticMusicType;
import data.operations.DataOperations;

public class ResultsImageAdapters extends BaseAdapter {
    private final String[] mobileValues;
    private Context context;


    public ResultsImageAdapters(Context context, String[] mobileValues) {
        this.context = context;
        this.mobileValues = mobileValues;

    }

    public static void customView(View v, int backgroundColor, int borderColor) {
        GradientDrawable shape = new GradientDrawable();
        shape.setShape(GradientDrawable.OVAL);
        shape.setCornerRadii(new float[]{8, 8, 8, 8, 0, 0, 0, 0});
        shape.setColor(backgroundColor);
        shape.setStroke(3, borderColor);
        v.setBackgroundDrawable(shape);
    }

    public View getView(int position, View convertView, ViewGroup parent) {

        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View gridView;

        if (convertView == null) {


            gridView = inflater.inflate(R.layout.bubbles, null);
            // set value into textview
            TextView textView = (TextView) gridView
                    .findViewById(R.id.item_text);
            textView.setText(mobileValues[position]);

            // set image based on selected text
            TextView bubble = (TextView) gridView
                    .findViewById(R.id.textview1);

            String mobile = mobileValues[position];

            for (int i = 0; i < 9; i++) {
                switch (mobile) {
                    case StaticMusicType.dance:
                        bubble.setText(DataOperations.getInstance().getAggregateMusicGenreData(StaticMusicType.dance));
                        customView(bubble, Color.parseColor("#E91E63"), Color.parseColor("#E91E63"));
                        break;
                    case StaticMusicType.folk:
                        customView(bubble, Color.parseColor("#FFC107"), Color.parseColor("#FFC107"));
                        bubble.setText(DataOperations.getInstance().getAggregateMusicGenreData(StaticMusicType.folk));
                        break;
                    case StaticMusicType.hipHop:
                        bubble.setText(DataOperations.getInstance().getAggregateMusicGenreData(StaticMusicType.hipHop));
                        customView(bubble, Color.parseColor("#9C27B0"), Color.parseColor("#9C27B0"));
                        break;
                    case StaticMusicType.punk:
                        bubble.setText(DataOperations.getInstance().getAggregateMusicGenreData(StaticMusicType.punk));
                        customView(bubble, Color.parseColor("#00B8D4"), Color.parseColor("#00B8D4"));
                        break;
                    case StaticMusicType.pop:
                        bubble.setText(DataOperations.getInstance().getAggregateMusicGenreData(StaticMusicType.pop));
                        customView(bubble, Color.parseColor("#4CAF50"), Color.parseColor("#4CAF50"));
                        break;
                    case StaticMusicType.classical:
                        bubble.setText(DataOperations.getInstance().getAggregateMusicGenreData(StaticMusicType.classical));
                        customView(bubble, Color.parseColor("#009688"), Color.parseColor("#009688"));
                        break;
                    case StaticMusicType.metal:
                        bubble.setText(DataOperations.getInstance().getAggregateMusicGenreData(StaticMusicType.metal));
                        customView(bubble, Color.parseColor("#E57373"), Color.parseColor("#E57373"));
                        break;
                    case StaticMusicType.jazz:
                        bubble.setText(DataOperations.getInstance().getAggregateMusicGenreData(StaticMusicType.jazz));
                        customView(bubble, Color.parseColor("#D1C4E9"), Color.parseColor("#D1C4E9"));
                        break;
                    case StaticMusicType.rock:
                        bubble.setText(DataOperations.getInstance().getAggregateMusicGenreData(StaticMusicType.rock));
                        customView(bubble, Color.parseColor("#9E9D24"), Color.parseColor("#9E9D24"));
                        break;
                    case StaticMusicType.reggae:
                        bubble.setText(DataOperations.getInstance().getAggregateMusicGenreData(StaticMusicType.reggae));
                        customView(bubble, Color.parseColor("#64DD17"), Color.parseColor("#64DD17"));
                        break;
                    case "Overview":
                        bubble.setText("All your music");
                        customView(bubble, Color.parseColor("#FF5722"), Color.parseColor("#FF5722"));
                        break;

                }

            }

        } else {
            gridView = (View) convertView;
        }

        return gridView;
    }

    @Override
    public int getCount() {
        return mobileValues.length;
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }


}