package ru.toster.toster.fragmentTab.users;


import android.app.FragmentTransaction;
import android.content.Intent;
import android.nfc.Tag;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.annotation.RequiresApi;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.TextView;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import ru.toster.toster.R;
import ru.toster.toster.fragmentTab.PostFragment;
import ru.toster.toster.fragmentTab.QuestionFragment;
import ru.toster.toster.fragmentTab.userAndTag.UserAndTagActivity;
import ru.toster.toster.fragmentTab.userAndTag.UserAndTagFragment;
import ru.toster.toster.http.DowlandImage;
import ru.toster.toster.http.HTTP;
import ru.toster.toster.http.ParsingPage;
import ru.toster.toster.objects.CardObject;

public class UsersFragment extends Fragment {
    private List<CardObject> listCard = new ArrayList<>();
    private String url;
    private QuestionFragment.EnumQuestion enumQuestion;
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private static OkHttpClient client = HTTP.getClient();
    private boolean dowlandPage = false;
    private ScrollView scrollView;
    private static Request request;
    private LinearLayout layout;
    private int number=1;//Номер страницы сбора данных
    private LayoutInflater inflater;
    public static final String TAG = "UsersFragment";

    public UsersFragment(String url, QuestionFragment.EnumQuestion enumQuestion) {
        this.url = url;
        this.enumQuestion = enumQuestion;
    }

    public UsersFragment(QuestionFragment.EnumQuestion questionsLatest) {
        this.enumQuestion = questionsLatest;
    }

    public UsersFragment(String url) {
        this.url = url;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getHttp(enumQuestion);
    }

    @Override
    public void onStop() {
        super.onStop();
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_question, container, false);//fragment_all_tags

//        layout = (LinearLayout) getActivity().findViewById(R.id.viewLinear);
        layout = (LinearLayout) view.findViewById(R.id.scroll_container);
        layout.removeAllViews();
        this.inflater = inflater;

        mSwipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.activity_main_swipe_refresh_layout);
        scrollView = (ScrollView)view.findViewById(R.id.scroll_question_fragment);

        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getHttp(enumQuestion);
            }
        });

        scrollView.getViewTreeObserver().addOnScrollChangedListener(new ViewTreeObserver.OnScrollChangedListener() {

            @Override
            public void onScrollChanged() {
                if (!dowlandPage) {
                    if (layout.getHeight() - (scrollView.getHeight() + scrollView.getScrollY())<=1500&&scrollView.getScrollY()>=500) {
                        dowlandPage = true;
                        number++;
                        getHttp(enumQuestion, number);
                    }
                }
            }
        });
        views(listCard);
        return view;
    }

    private synchronized void getHttp(QuestionFragment.EnumQuestion Users) {
        String url = null;
        switch (enumQuestion) {
            case Users:
                url = "https://toster.ru/users/main";//https://toster.ru/tags/by_questions?page=2
                break;
            default:
                url=this.url + "/users";
        }
        request = new Request.Builder()
                .url(url)
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                call.cancel();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                listCard = ParsingPage.parsAllUsers(response.body().string());
                System.out.println("\n\n" + listCard.size());
                getActivity().runOnUiThread(new Runnable() {
                    @RequiresApi(api = Build.VERSION_CODES.M)
                    @Override
                    public void run() {
                        views(listCard);
                    }
                });
            }
        });
    }

    private void getHttp(QuestionFragment.EnumQuestion enumQuestion, int number) {
        String url = null;
        if (enumQuestion!=null) {
            switch (enumQuestion) {
                case Users:
                    url = "https://toster.ru/users/main";//https://toster.ru/tags/by_questions?page=2
                    break;
            }
        }else{
            url=this.url + "/users";
        }

        request = new Request.Builder()
                .url(url+"?page="+number)
                .build();

        client.newCall(request).enqueue(new Callback() {

            @Override
            public void onFailure(Call call, IOException e) {
                call.cancel();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                final List<CardObject> listCards = (ParsingPage.parsAllUsers(response.body().string()));
                final List<CardObject> list = new ArrayList<CardObject>();
                for (int i=0;i<listCards.size();i++){
                    boolean replay=false;
                    for (int a=0;a<listCard.size();a++){
                        if (listCards.get(i).getQuestion().equals(listCard.get(a).getQuestion())){
                            replay = true;
                            break;
                        }
                    }
                    if (!replay) {
                        list.add(listCards.get(i));
                        listCard.add(listCards.get(i));
                    }
                }
                getActivity().runOnUiThread(new Runnable() {
                    @RequiresApi(api = Build.VERSION_CODES.M)
                    @Override
                    public void run() {
                        views(list);
                    }
                });
            }
        });
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    public void views(final List<CardObject> list) {
        LinearLayout layoutVert = null;
        LinearLayout.LayoutParams layoutParams = null;
        for (int i=0;i<list.size();i++){
            if (i%2==0){
                layoutVert = new LinearLayout(getContext());
                layoutVert.setOrientation(LinearLayout.HORIZONTAL);
                layoutParams = new LinearLayout.LayoutParams(DrawerLayout.LayoutParams.MATCH_PARENT, DrawerLayout.LayoutParams.WRAP_CONTENT);
            }

            final View item = inflater.inflate(R.layout.el_card, this.layout, false);
            if (!(list.get(i).getUrlImage() == null))
                new DowlandImage((ImageView)item.findViewById(R.id.tag_url_image)).execute(list.get(i).getUrlImage());//((ImageView)item.findViewById(R.id.tag_url_image)
            ((TextView)item.findViewById(R.id.tag_name)).setText(list.get(i).getTag());
            ((TextView)item.findViewById(R.id.tag_question)).setText(list.get(i).getQuestion());
//            ((TextView)item.findViewById(R.id.tag_subscriptions)).setText("Подписаться "+list.get(i).getSubscribeNumber());
            ((TextView)item.findViewById(R.id.tag_subscriptions)).setVisibility(View.GONE);

            ((TextView)item.findViewById(R.id.users_overall_contribution)).setText(list.get(i).getSubscribeNumber());

            ((ProgressBar)item.findViewById(R.id.progressBar)).setProgress((int) list.get(i).getProgressBar());
            ((ProgressBar)item.findViewById(R.id.progressBar)).setMax(100);

            Display display = getActivity().getWindowManager().getDefaultDisplay();

            item.getLayoutParams().width= (int) ((display.getWidth()/2)-2.5);

            final int finalI = i;
            item.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(getActivity(), UserAndTagActivity.class);
                    intent.putExtra("url", list.get(finalI).getHref());
                    startActivity(intent);
                }
            });

            layoutVert.addView(item);
            if (i%2==1){
                layout.addView(layoutVert, layoutParams);
            }
        }
        dowlandPage = false;
        mSwipeRefreshLayout.setRefreshing(false);
    }
}
