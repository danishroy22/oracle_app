package com.example.cloning.Adapter;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.example.cloning.Fragments.ChatsFragment;
import com.example.cloning.Fragments.CommunityFragment;

public class FragmentAdapter extends FragmentPagerAdapter {


    public FragmentAdapter(@NonNull FragmentManager fm) {
        super(fm);
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        switch(position){
            default:return new ChatsFragment();
            case 1: return new CommunityFragment();



        }


    }

    @Override
    public int getCount() {
        return 2;
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {

        String title = null;
        if (position == 0)
        {
            title = "CHATS";

        }
        if (position == 1)
        {
            title = "COMMUNITY";

        }

        return title;
    }
}
