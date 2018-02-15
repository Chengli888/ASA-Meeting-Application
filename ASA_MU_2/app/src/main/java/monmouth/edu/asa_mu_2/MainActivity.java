package monmouth.edu.asa_mu_2;

import android.os.Message;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


import com.h6ah4i.android.tablayouthelper.TabLayoutHelper;

import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends FragmentActivity  {

    private List<Fragment> mFragments;
    private TabLayoutHelper mTabLayoutHelper;
    private String[] Title = {"Meeting INFO","schedule","My Events","Group Chat"};
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ViewPager viewPager = (ViewPager) findViewById(R.id.viewpager);
        TabLayout tabLayout = (TabLayout) findViewById(R.id.tab_layout);

        mFragments = new ArrayList<Fragment>();

        mFragments.add(new Tab01());
        mFragments.add(new Tab03());
        mFragments.add(new Tab04());
        mFragments.add(new Tab02());
        PagerAdapter adapter = new FragmentPagerAdapter(getSupportFragmentManager()) {
            @Override
            public Fragment getItem(int position) {
                return mFragments.get(position);
            }

            @Override
            public int getCount() {
                return mFragments.size();
            }

            @Override
            public CharSequence getPageTitle(int position) {
                return Title[position];
            }
        };

        viewPager.setAdapter(adapter);

// initialize the TabLayoutHelper instance
        mTabLayoutHelper = new TabLayoutHelper(tabLayout, viewPager);

// [Optional] enables auto tab mode adjustment
        mTabLayoutHelper.setAutoAdjustTabModeEnabled(true);

    }


}
