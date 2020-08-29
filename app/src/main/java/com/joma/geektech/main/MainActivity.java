package com.joma.geektech.main;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.joma.geektech.R;

public class MainActivity extends AppCompatActivity {

    public static void start(Context context) {
        context.startActivity(new Intent(context, MainActivity.class));
    }
    private ViewPager viewPager;
    private BottomNavigationView bnv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        findView();
        listener();
    }

    private void findView(){
        viewPager = findViewById(R.id.main_view_pager);
        viewPager.setAdapter(new ViewPagerAdapter(getSupportFragmentManager()));
        viewPager.setOffscreenPageLimit(5);
        bnv = findViewById(R.id.main_bottom_nav);
    }

    private void listener(){
        bnv.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){
                    case R.id.menu_chat:
                        viewPager.setCurrentItem(0, false);
                        break;
                    case R.id.menu_calendar:
                        viewPager.setCurrentItem(1, false);
                        break;
                    case R.id.menu_homework:
                        viewPager.setCurrentItem(2, false);
                        break;
                    case R.id.menu_stack:
                        viewPager.setCurrentItem(3, false);
                        break;
                    case R.id.menu_profile:
                        viewPager.setCurrentItem(4, false);
                        break;
                }
                return true;
            }
        });

        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                switch (position){
                    case 0:
                        bnv.setSelectedItemId(R.id.menu_chat);
                        break;
                    case 1:
                        bnv.setSelectedItemId(R.id.menu_calendar);
                        break;
                    case 2:
                        bnv.setSelectedItemId(R.id.menu_homework);
                        break;
                    case 3:
                        bnv.setSelectedItemId(R.id.menu_stack);
                        break;
                    case 4:
                        bnv.setSelectedItemId(R.id.menu_profile);
                        break;
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }
}