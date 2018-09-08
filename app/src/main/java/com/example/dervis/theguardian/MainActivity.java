package com.example.dervis.theguardian;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<ArrayList<Article>> {

    private static final String TAG = MainActivity.class.getSimpleName();

    private ListView mListView;

    private final static String URL_NEWS = "https://content.guardianapis.com/search";

    private final static int ARTICLES_ASYNC_LOADER = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mListView = findViewById(R.id.listView);

        if (isConnected())
            getSupportLoaderManager().initLoader(ARTICLES_ASYNC_LOADER, null, this);
        else
            showDisconnectedMessage();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_manu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.settings:
                Intent intent = new Intent(this, SettingsActivity.class);
                startActivity(intent);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @NonNull
    @Override
    public Loader<ArrayList<Article>> onCreateLoader(int i, @Nullable Bundle bundle) {
        return new ArticlesAsyncLoader(this, buildTailoredURL());
    }

    @Override
    public void onLoadFinished(@NonNull Loader<ArrayList<Article>> loader, ArrayList<Article> articles) {
        if (articles != null && !articles.isEmpty()) {
            hideProgressBar();
            mListView.setAdapter(new ArticleArrayAdapter(this, articles));
        } else {
            displayErrorMessage();
        }
    }

    @Override
    public void onLoaderReset(@NonNull Loader<ArrayList<Article>> loader) {

    }

    private boolean isConnected() {
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

    private TextView showMessagesTextView() {
        TextView viewById = findViewById(R.id.messagesTextView);
        viewById.setVisibility(View.VISIBLE);
        return viewById;
    }

    private void hideProgressBar() {
        findViewById(R.id.progressBar).setVisibility(View.GONE);
    }

    private String buildTailoredURL() {
        Uri.Builder baseUri = Uri.parse(URL_NEWS).buildUpon();

        baseUri.appendQueryParameter("section", getSectionPreference());
        baseUri.appendQueryParameter("page-size", getHeadlinesLimitPreference());
        baseUri.appendQueryParameter("order-by", getOrderByPreference());
        baseUri.appendQueryParameter("show-fields", "all");
        baseUri.appendQueryParameter("api-key", "673bf9cb-9e2c-4e56-ae91-517e016388de");

        return baseUri.toString();
    }

    private SharedPreferences getSharedPreferences() {
        return PreferenceManager.getDefaultSharedPreferences(this);
    }

    private String getOrderByPreference() {
        return getSharedPreferences().getString(
                getString(R.string.order_by_key),
                getString(R.string.order_by_default_value)
        );
    }

    private String getHeadlinesLimitPreference() {
        return getSharedPreferences().getString(
                getString(R.string.settings_headlines_limit_key),
                getString(R.string.settings_headlines_limit_default_value)
        );
    }

    private String getSectionPreference() {
        return getSharedPreferences().getString(
                getString(R.string.settings_section_key),
                getString(R.string.settings_section_default_value)
        );
    }
}

