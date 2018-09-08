package com.example.dervis.theguardian;

import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<ArrayList<Article>> {

    private ListView mListView;

    private final static String URL_NEWS = "https://content.guardianapis.com/search?show-fields=byline%2Cheadline&section=technology&page-size=25&api-key=673bf9cb-9e2c-4e56-ae91-517e016388de";

    private final static int ARTICLES_ASYNC_LOADER = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mListView = ((ListView) findViewById(R.id.listView));

        if (isConnected())
            getSupportLoaderManager().initLoader(ARTICLES_ASYNC_LOADER, null, this);
        else
            showDisconnectedMessage();
    }

    @NonNull
    @Override
    public Loader<ArrayList<Article>> onCreateLoader(int i, @Nullable Bundle bundle) {
        return new ArticlesAsyncLoader(this, URL_NEWS);
    }

    @Override
    public void onLoadFinished(@NonNull Loader<ArrayList<Article>> loader, ArrayList<Article> articles) {
        if (articles != null) {
            hideProgressBar();
            mListView.setAdapter(new ArticleArrayAdapter(this, articles));
        } else {
            displayErrorMessage();
        }
    }

    @Override
    public void onLoaderReset(@NonNull Loader<ArrayList<Article>> loader) {

    }

    public boolean isConnected() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE);

        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();

        return activeNetworkInfo != null && activeNetworkInfo.isConnected();

    }

    private void showDisconnectedMessage() {
        hideProgressBar();
        TextView messagesTextView = showMessagesTextView();
        messagesTextView.setText(getString(R.string.no_connection));
    }

    private void displayErrorMessage() {
        hideProgressBar();
        TextView messagesTextView = showMessagesTextView();
        messagesTextView.setText(getString(R.string.an_error_occurred));
    }

    @NonNull
    private TextView showMessagesTextView() {
        TextView viewById = findViewById(R.id.messagesTextView);
        viewById.setVisibility(View.VISIBLE);
        return viewById;
    }

    private void hideProgressBar() {
        findViewById(R.id.progressBar).setVisibility(View.GONE);
    }
}

