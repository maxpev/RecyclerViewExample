package com.maxpevnitskiy.newsfeed;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

public class NewsDescription extends AppCompatActivity {

    private TextView mHeaderTextView;
    private TextView mDescriptionTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news_description);

        mHeaderTextView = (TextView) findViewById(R.id.tv_header);
        mDescriptionTextView = (TextView) findViewById(R.id.tv_description);

        Intent intent = getIntent();
        mHeaderTextView.setText(intent.getStringExtra(MainActivity.NEWS_HEADER));
        mDescriptionTextView.setText(intent.getStringExtra(MainActivity.NEWS_BODY));

    }
}
