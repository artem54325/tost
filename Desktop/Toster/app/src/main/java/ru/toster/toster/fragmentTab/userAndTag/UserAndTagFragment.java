package ru.toster.toster.fragmentTab.userAndTag;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.TabLayout;
import android.app.Fragment;
import android.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import ru.toster.toster.NewsActivity;
import ru.toster.toster.R;
import ru.toster.toster.ViewPagerAdapter;

public class UserAndTagFragment extends Fragment {

    public static final String TAG = "UserAndTagFragment";
    private int mMaxScrollSize;
    private AppBarLayout.OnOffsetChangedListener mList = new AppBarLayout.OnOffsetChangedListener() {
        @Override
        public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {

        }
    };

    public UserAndTagFragment(String href, int i) {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_user_and_tag, container, false);

        TabLayout tabLayout = (TabLayout) view.findViewById(R.id.user_tag_tabs);
        ViewPager viewPager = (ViewPager) view.findViewById(R.id.user_tag_layout);
        AppBarLayout appbarLayout = (AppBarLayout) view.findViewById(R.id.user_tag_appbar);

        Toolbar toolbar = (Toolbar) view.findViewById(R.id.user_tag_toolbar);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) {
                getActivity().onBackPressed();
            }
        });

        appbarLayout.addOnOffsetChangedListener(mList);
        mMaxScrollSize = appbarLayout.getTotalScrollRange();

        viewPager.setAdapter(new ViewPagerAdapter(((AppCompatActivity) getActivity()).getSupportFragmentManager()));//getSupportFragmentManager() getChildFragmentManager()
        tabLayout.setupWithViewPager(viewPager);
        return view;
    }
}
