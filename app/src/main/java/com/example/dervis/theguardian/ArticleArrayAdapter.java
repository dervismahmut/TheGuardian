package com.example.dervis.theguardian;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

class ArticleArrayAdapter extends ArrayAdapter<Article> {

    public ArticleArrayAdapter(Context context, ArrayList<Article> articles) {
        super(context, 0, articles);
    }

    @SuppressLint("ViewHolder")
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // this is the root view that contains all of the other views
        View rootView;

        // the current article based on the position given by the adapter
        final Article item = getItem(position);

        // checking if a view is provided for recycling
        if (convertView != null) {
            rootView = convertView;
        } else {
            rootView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.article_item, parent, false);
        }


        // set the text of the section
        ((TextView) rootView.findViewById(R.id.section)).setText(item.getSection() + "/");
        // set the text of the title
        ((TextView) rootView.findViewById(R.id.title)).setText(item.getTitle());
        // set the text of the author
        ((TextView) rootView.findViewById(R.id.author)).setText(item.getAuthor());
        // set the text of the date
        ((TextView) rootView.findViewById(R.id.date)).setText(item.getDate());

        // set the onClickListener to open the article's url when the rootView is clicked
        rootView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_VIEW,
                        Uri.parse(item.getWebUrl()));

                getContext().startActivity(intent);
            }
        });

        return rootView;
    }
}
