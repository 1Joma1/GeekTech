package com.joma.geektech.main;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.joma.geektech.calendar.CalendarFragment;
import com.joma.geektech.chat.ChatFragment;
import com.joma.geektech.homework.HomeworkFragment;
import com.joma.geektech.profile.ProfileFragment;
import com.joma.geektech.stack.StackFragment;

public class ViewPagerAdapter extends FragmentPagerAdapter {

    public ViewPagerAdapter(@NonNull FragmentManager fm) {
        super(fm);
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 1:
                return new CalendarFragment();
            case 2:
                return new HomeworkFragment();
            case 3:
                return new StackFragment();
            case 4:
                return new ProfileFragment();
            default:
                return new ChatFragment();
        }
    }

    @Override
    public int getCount() {
        return 5;
    }
}