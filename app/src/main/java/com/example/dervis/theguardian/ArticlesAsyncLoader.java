package com.example.dervis.theguardian;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.AsyncTaskLoader;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;

class ArticlesAsyncLoader extends AsyncTaskLoader<ArrayList<Article>> {

    //the url pointing to the resources to be downloaded
    private final String mUrl;

    public ArticlesAsyncLoader(@NonNull Context context, String mUrl) {
        super(context);
        this.mUrl = mUrl;
    }

    @Nullable
    @Override
    public ArrayList<Article> loadInBackground() {
        ArrayList<Article> articles = new ArrayList<>();

        URL url = createURL(mUrl);

        String response = getResponse(url);

        articles = extractArticles(response);

        return articles;
    }

    @Override
    protected void onStartLoading() {
        forceLoad();
    }

    private URL createURL(String url) {
        try {
            return new URL(url);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        return null;
    }

    private String getResponse(URL url) {
        if (url == null)
            return null;

        String response = "";
        HttpURLConnection httpURLConnection = null;
        InputStream inputStream = null;

        try {
            httpURLConnection = (HttpURLConnection) url.openConnection();

            httpURLConnection.setRequestMethod("GET");

            httpURLConnection.connect();

            inputStream = httpURLConnection.getInputStream();

            response = convertInputStreamToString(inputStream);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (httpURLConnection != null)
                httpURLConnection.disconnect();

            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        return response;
    }

    private String convertInputStreamToString(InputStream inputStream) throws IOException {
        InputStreamReader inputStreamReader = new InputStreamReader(inputStream, Charset.forName("UTF-8"));

        BufferedReader bufferedReader = new BufferedReader(inputStreamReader);

        StringBuilder stringBuilder = new StringBuilder();

        String line = bufferedReader.readLine();

        while (line != null) {
            stringBuilder.append(line);
            line = bufferedReader.readLine();
        }

        return stringBuilder.toString();
    }

    private ArrayList<Article> extractArticles(String response) {
        if (response == null)
            return null;

        ArrayList<Article> articles = new ArrayList<>();

        try {
            JSONArray results = new JSONObject(response)
                    .getJSONObject("response")
                    .getJSONArray("results");

            for (int i = 0; i < results.length(); i++) {

                JSONObject resultBody = results.getJSONObject(i);

                String section = resultBody.getString("sectionName");


                String date = resultBody.getString("webPublicationDate");
                String[] split = date.split("T");
                date = split[0];

                String webUrl = resultBody.getString("webUrl");

                JSONObject fields = resultBody.getJSONObject("fields");

                String title = fields.getString("headline");
                String author = fields.getString("byline");

                articles.add(new Article(section, title, author, date, webUrl));
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

        return articles;
    }
}
