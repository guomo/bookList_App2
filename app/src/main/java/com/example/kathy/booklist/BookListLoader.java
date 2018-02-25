package com.example.kathy.booklist;

import android.content.AsyncTaskLoader;
import android.content.Context;

import java.util.List;

/**
 * Created by Kathy on 11/22/2017.
 */

public class BookListLoader extends AsyncTaskLoader<List<bookInfo>> {


    /** Tag for log messages */
    private static final String LOG_TAG = BookListAdapter.class.getName();

    /** Query URL */
    private String mUrl;

    /**
     * Constructs a new {@link BookListLoader}.
     *
     * @param context of the activity
     * @param url to load data from
     */
    public BookListLoader(Context context, String url) {
        super(context);
        mUrl = url;
    }

    @Override
    protected void onStartLoading() {
        forceLoad();
    }

    @Override
    public List<bookInfo> loadInBackground() {
        if(mUrl==null){ return null;}

        List<bookInfo> bookInfos = QueryUtils.fetchbookInfoData(mUrl);

        return bookInfos;
    }

}


