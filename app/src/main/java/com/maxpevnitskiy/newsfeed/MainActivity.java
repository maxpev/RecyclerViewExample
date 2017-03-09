package com.maxpevnitskiy.newsfeed;

import android.content.Intent;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Scanner;

import javax.net.ssl.HttpsURLConnection;

public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<String[]>, NewsAdapter.ListItemClickListener {

    private static final int LOADER_ID = 1;
    private static final String URL_LINK = "https://newsapi.org/v1/articles?source=the-new-york-times&sortBy=top&apiKey=c8626eb1d81345f88e5d34c77c7619ae";

    public static final String NEWS_HEADER = "news-header";
    public static final String NEWS_BODY = "news-body";

    String[] mTitles;
    String[] mDescription;

    RecyclerView mNewsFeed;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mNewsFeed = (RecyclerView) findViewById(R.id.rv_news_feed);
        mNewsFeed.setHasFixedSize(true);

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        mNewsFeed.setLayoutManager(layoutManager);

        NewsAdapter adapter = new NewsAdapter(new String[]{"1","2"}, this);
        mNewsFeed.setAdapter(adapter);

        getSupportLoaderManager().initLoader(LOADER_ID, new Bundle(), this);


    }

    @Override
    public Loader<String[]> onCreateLoader(int id, final Bundle args) {
        return new AsyncTaskLoader<String[]>(this) {
            @Override
            public String[] loadInBackground() {
                String response = null;
                String[] data = null;

                try {
                    URL url = new URL(URL_LINK);
                    HttpsURLConnection urlConnection = (HttpsURLConnection) url.openConnection();
                    InputStream inputStream = urlConnection.getInputStream();
                    Scanner scanner = new Scanner(inputStream);
                    scanner.useDelimiter("\\A");
                    if (scanner.hasNext()) {
                        response = scanner.next();
                    }
                    urlConnection.disconnect();
                } catch (IOException e) {
                    e.printStackTrace();
                }

                if (response.length() > 0) {
                    try {
                        JSONObject jsonData = new JSONObject(response);
                        JSONArray articles = jsonData.getJSONArray("articles");
                        data = new String[articles.length() * 2];
                        for (int i = 0; i < articles.length(); i++) {
                            JSONObject article = articles.getJSONObject(i);
                            String title = article.getString("title");
                            String description = article.getString("description");
                            data[i] = title;
                            data[articles.length() + i] = description;
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                return data;

            }

            @Override
            protected void onStartLoading() {
                super.onStartLoading();
                forceLoad();
            }
        };
    }

    @Override
    public void onLoadFinished(Loader<String[]> loader, String[] data) {
        int halfLength = data.length / 2;
        mTitles = new String[halfLength];
        mDescription = new String[halfLength];
        for (int i = 0; i < halfLength; i++) {
            mTitles[i] = data[i];
            mDescription[i] = data[halfLength + i];
        }
        mNewsFeed.setAdapter(new NewsAdapter(mTitles, this));
    }


    @Override
    public void onLoaderReset(Loader loader) {

    }

    @Override
    public void onListItemClicked(int clickIndex) {
        Intent intent = new Intent(this, NewsDescription.class);
        intent.putExtra(NEWS_HEADER, mTitles[clickIndex]);
        intent.putExtra(NEWS_BODY, mDescription[clickIndex]);
        startActivity(intent);

    }
}
