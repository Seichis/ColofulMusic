package com.jupiter.on.tetsuo.colofulmusic;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.util.TypedValue;
import android.view.Menu;
import android.widget.Switch;

import com.astuetz.PagerSlidingTabStrip;

import fragment.adapter.FragmentAdapter;

public class MainActivity extends FragmentActivity {

    private PagerSlidingTabStrip tabs;
    private ViewPager pager;
    private FragmentAdapter adapter;
    private boolean isServiceRunning;
    private static MainActivity mainActivity;

    public static MainActivity getMainActivity() {
        return mainActivity;
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mainActivity=this;
        tabs = (PagerSlidingTabStrip) findViewById(R.id.tabs);
        pager = (ViewPager) findViewById(R.id.pager);
        adapter = new FragmentAdapter(getSupportFragmentManager());
        isServiceRunning=false;
        pager.setAdapter(adapter);

        final int pageMargin = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 4, getResources()
                .getDisplayMetrics());
        pager.setPageMargin(pageMargin);

        tabs.setViewPager(pager);
        //changeColor(currentColor);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }


    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);

    }

    public void startAudioClassificationService(){
        startService(new Intent(getMainActivity(), AudioClassificationService.class));
    }
    public void stopAudioClassificationService(){
        stopService(new Intent(getMainActivity(), AudioClassificationService.class));
    }
    public boolean isServiceRunning() {
        return isServiceRunning;
    }

    public void setIsServiceRunning(boolean isServiceRunning) {
        this.isServiceRunning = isServiceRunning;
    }
}