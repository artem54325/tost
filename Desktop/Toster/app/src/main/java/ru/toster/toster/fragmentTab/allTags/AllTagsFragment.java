package ru.toster.toster.fragmentTab.allTags;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import ru.toster.toster.R;
import ru.toster.toster.fragmentTab.QuestionFragment;
import ru.toster.toster.fragmentTab.userAndTag.UserAndTagActivity;
import ru.toster.toster.http.DowlandImage;
import ru.toster.toster.http.HTTP;
import ru.toster.toster.http.ParsingPage;
import ru.toster.toster.objects.CardObject;


public class AllTagsFragment extends Fragment {

    private List<CardObject>listCard = new ArrayList<>();
    private QuestionFragment.EnumQuestion enumQuestion = null;
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private static OkHttpClient client = HTTP.getClient();
    private boolean dowlandPage = false;
    private ScrollView scrollView;
    private static Request request;
    private LinearLayout layout;
    private int number=1;//Номер страницы сбора данных
    private LayoutInflater inflater;
    private String url;

    public AllTagsFragment(String url) {
        this.url = url;
    }

    public AllTagsFragment(QuestionFragment.EnumQuestion questionsLatest) {
        this.enumQuestion = questionsLatest;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getHttp(enumQuestion);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_question, container, false);//fragment_all_tags

        layout = (LinearLayout) view.findViewById(R.id.scroll_container);
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

    private synchronized void getHttp(QuestionFragment.EnumQuestion enumQuestion) {
        String url = null;
        if (enumQuestion!=null) {
            switch (enumQuestion) {
                case TagsByQuestion:
                    url = "https://toster.ru/tags/by_questions";
                    break;
                case TagsBySubscribers:
                    url = "https://toster.ru/tags/by_watchers";
                    break;
                case Toster:
                    url = "https://toster.ru";
                    break;
            }
        }else {
            url = this.url + "/tags";
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
                listCard = (ParsingPage.parsAlltag(response.body().string()));
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        layout.removeAllViews();
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
                case TagsByQuestion:
                    url = "https://toster.ru/tags/by_questions";
                    break;
                case TagsBySubscribers:
                    url = "https://toster.ru/tags/by_watchers";
                    break;
                case Toster:
                    url = "https://toster.ru";
                    break;
            }
        }else {
            url = this.url + "/tags";
        }

        if (number>=10)
            return;
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
                final List<CardObject> listCards = (ParsingPage.parsAlltag(response.body().string()));
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
                    @Override
                    public void run() {
                        views(list);
                    }
                });
            }
        });
    }

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
            new DowlandImage((ImageView)item.findViewById(R.id.tag_url_image)).execute(list.get(i).getUrlImage());//((ImageView)item.findViewById(R.id.tag_url_image)
            ((TextView)item.findViewById(R.id.tag_name)).setText(list.get(i).getTag());
            ((TextView)item.findViewById(R.id.tag_question)).setText(list.get(i).getQuestion());
            ((TextView)item.findViewById(R.id.tag_subscriptions)).setText("Подписаться "+list.get(i).getSubscribeNumber());
            ((LinearLayout)item.findViewById(R.id.users_layout)).setVisibility(View.GONE);

            Display display = getActivity().getWindowManager().getDefaultDisplay();

            final int finalI = i;
            item.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(getActivity(), UserAndTagActivity.class);
                    intent.putExtra("url", list.get(finalI).getHref());
                    intent.putExtra("tag_and_user", false);
                    startActivity(intent);
                }
            });

            item.getLayoutParams().width = (int) ((display.getWidth()/2)-2.5);

            layoutVert.addView(item);
            if (i%2==1){
                layout.addView(layoutVert, layoutParams);
            }
        }
        dowlandPage = false;
        mSwipeRefreshLayout.setRefreshing(false);
    }
}
