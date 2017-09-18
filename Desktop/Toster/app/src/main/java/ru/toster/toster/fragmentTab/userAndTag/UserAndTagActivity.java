package ru.toster.toster.fragmentTab.userAndTag;

import android.content.Intent;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import ru.toster.toster.NewsActivity;
import ru.toster.toster.R;
import ru.toster.toster.fragmentTab.QuestionFragment;
import ru.toster.toster.fragmentTab.allTags.AllTagsFragment;
import ru.toster.toster.fragmentTab.users.UsersFragment;
import ru.toster.toster.http.DowlandImage;
import ru.toster.toster.http.HTTP;
import ru.toster.toster.http.ParsingPage;
import ru.toster.toster.objects.NameAndTagFullInfoObject;

public class UserAndTagActivity extends AppCompatActivity
        implements AppBarLayout.OnOffsetChangedListener, NavigationView.OnNavigationItemSelectedListener {//


    private String url;
    private boolean tagAndUser;
    private TabLayout tabLayout;
    private NameAndTagFullInfoObject fullName;
    public static final String TAG = "UserAndTagFragment";
    private static Request request;
    private ViewPager viewPager;
    private static OkHttpClient client = HTTP.getClient();


    private static final int PERCENTAGE_TO_ANIMATE_AVATAR = 20;
    private boolean mIsAvatarShown = true;
    private Toolbar toolbar;
    private DrawerLayout drawer;

    private int mMaxScrollSize;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.collapsing_toolbar_layout);

        this.url = getIntent().getStringExtra("url");
        this.tagAndUser = getIntent().getBooleanExtra("tag_and_user", true);//user=true tag=false

        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        System.out.println(tagAndUser);

        if (tagAndUser) {
            getHttpUser(url);
        }else{
            getHttpTag(url);
        }
        tabLayout = (TabLayout) findViewById(R.id.materialup_tabs);
        viewPager  = (ViewPager) findViewById(R.id.materialup_viewpager);
        AppBarLayout appbarLayout = (AppBarLayout) findViewById(R.id.materialup_appbar);

        toolbar = (Toolbar) findViewById(R.id.materialup_toolbar);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) {
                onBackPressed();
            }
        });

        setSupportActionBar(toolbar);

        appbarLayout.addOnOffsetChangedListener(this);
        mMaxScrollSize = appbarLayout.getTotalScrollRange();

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        onBackPressed();
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onOffsetChanged(AppBarLayout appBarLayout, int i) {
        if (mMaxScrollSize == 0)
            mMaxScrollSize = appBarLayout.getTotalScrollRange()-toolbar.getHeight();

        int percentage = (Math.abs(i)) * 100 / mMaxScrollSize;

        if (percentage >= PERCENTAGE_TO_ANIMATE_AVATAR && mIsAvatarShown) {
            mIsAvatarShown = false;
        }

        if (percentage <= PERCENTAGE_TO_ANIMATE_AVATAR && !mIsAvatarShown) {
            mIsAvatarShown = true;
        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {//onNavigationItemSelected
        if (item.getItemId()==R.id.users){
            onBackPressed();
        }else{
            Intent intent = new Intent(getApplicationContext(), NewsActivity.class);
            intent.putExtra("id", item.getItemId());
            startActivity(intent);
        }
        return super.onOptionsItemSelected(item);
    }

    private synchronized void getHttpUser(final String urlGet){

        request = new Request.Builder()
                .url(urlGet + "/info")
                .build();

        client.newCall(request).enqueue(new Callback() {

            @Override
            public void onFailure(Call call, IOException e) {
                call.cancel();
            }

            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onResponse(Call call, Response response) throws IOException {
                fullName = (ParsingPage.parsFullName(response.body().string()));
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        getViewUser();
                        TabsAdapter adapter = new TabsAdapter(getSupportFragmentManager());
                        adapter.addFragment(new InfoFragment(fullName), "Информация");
//                        adapter.addFragment(,"Ответы");
                        adapter.addFragment(new QuestionFragment(urlGet, QuestionFragment.EnumQuestion.QUESTION),"Вопросы");
                        adapter.addFragment(new QuestionFragment(urlGet, QuestionFragment.EnumQuestion.IQUESTION),"Подписан");
                        adapter.addFragment(new AllTagsFragment(url),"Теги");
//                        adapter.addFragment(,"Нравится");
                        viewPager.setAdapter(adapter);
                        tabLayout.setupWithViewPager(viewPager);
                    }
                });
            }
        });
    }

    private synchronized void getHttpTag(final String urlGet){

        request = new Request.Builder()
                .url(urlGet + "/info")
                .build();

        client.newCall(request).enqueue(new Callback() {

            @Override
            public void onFailure(Call call, IOException e) {
                call.cancel();
            }

            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onResponse(Call call, Response response) throws IOException {
                fullName = ParsingPage.parsFullTag(response.body().string());
                System.out.println();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        getViewTage();
                        TabsAdapter adapter = new TabsAdapter(getSupportFragmentManager());
                        adapter.addFragment(new InfoFragment(fullName), "Информация");
                        adapter.addFragment(new QuestionFragment(urlGet, QuestionFragment.EnumQuestion.QUESTION),"Новые вопросы");
                        adapter.addFragment(new QuestionFragment(urlGet, QuestionFragment.EnumQuestion.InterestQuestions),"Интересные");
                        adapter.addFragment(new QuestionFragment(urlGet, QuestionFragment.EnumQuestion.QuestionsWoAnswer),"Без ответа");
                        adapter.addFragment(new UsersFragment(urlGet, QuestionFragment.EnumQuestion.QuestionsWoAnswer),"Подписчики");
                        viewPager.setAdapter(adapter);
                        tabLayout.setupWithViewPager(viewPager);
                    }
                });
            }
        });
    }

    private void getViewUser() {
        new DowlandImage((ImageView)findViewById(R.id.user_tag_image)).execute(fullName.getUrlPhoto());
        ((TextView)findViewById(R.id.user_tag_name)).setText(fullName.getName());
        if (fullName.getDopName()!=null){
            ((TextView)findViewById(R.id.user_tag_job_title)).setText(fullName.getDopName());
        }//  user_tag_subscribe
        ((TextView)findViewById(R.id.user_tag_contribution)).setText(fullName.getContribution());
        ((TextView)findViewById(R.id.user_tag_question)).setText(fullName.getQuestion());
        ((TextView)findViewById(R.id.user_tag_answer)).setText(fullName.getAnswer());
        ((TextView)findViewById(R.id.user_tag_solutions)).setText(fullName.getDecisions());
    }
    private void getViewTage() {
        new DowlandImage((ImageView)findViewById(R.id.user_tag_image)).execute(fullName.getUrlPhoto());
        ((TextView)findViewById(R.id.user_tag_name)).setText(fullName.getName());
        if (fullName.getDopName()!=null){
            ((TextView)findViewById(R.id.user_tag_job_title)).setText(fullName.getDopName());
        }//  user_tag_subscribe
        ((View)findViewById(R.id.user_tag_view)).setVisibility(View.GONE);
        ((TextView)findViewById(R.id.user_tag_contribution)).setText("");
        ((TextView)findViewById(R.id.user_tag_question)).setText(fullName.getQuestion());
        ((TextView)findViewById(R.id.user_tag_answer)).setText(fullName.getAnswer());
        ((TextView)findViewById(R.id.user_tag_solutions)).setText(fullName.getDecisions());
    }


    private static class TabsAdapter extends FragmentPagerAdapter {
        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();


        TabsAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }

        public void addFragment(Fragment fragment, String title) {
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }

        @Override

        public CharSequence getPageTitle(int position) {
            return mFragmentTitleList.get(position);
        }
    }
    private static class InfoFragment extends Fragment{
        NameAndTagFullInfoObject fullName;

        public InfoFragment(NameAndTagFullInfoObject fullName) {
            this.fullName = fullName;
        }

        @Nullable
        @Override
        public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
            View view = inflater.inflate(R.layout.info_fragment, container, false);//fragment_all_tags
            if (fullName.getTextInfo()==null){
                ((TextView)view.findViewById(R.id.text_view)).setText("Пользователь пока ничего не рассказал о себе");
            }else{
                ((TextView)view.findViewById(R.id.text_view)).setText(fullName.getTextInfo());
            }
            return view;
        }
    }
}
