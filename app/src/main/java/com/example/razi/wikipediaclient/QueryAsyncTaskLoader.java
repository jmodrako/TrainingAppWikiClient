package com.example.razi.wikipediaclient;

import android.content.AsyncTaskLoader;
import android.content.Context;

public class QueryAsyncTaskLoader extends AsyncTaskLoader<String> {

    private String query;
    private String result;

    public QueryAsyncTaskLoader(Context context, String query) {
        super(context);
        this.query = query;
    }

    @Override
    public String loadInBackground() {
        String resultJSON = NetworkUtils.sendWikiGetRequest(query);
        result = NetworkUtils.parseWikiJSON(resultJSON);
        return (result);
    }

    @Override
    protected void onStartLoading() {
        if (result != null) {
            deliverResult(result);
        }

        if (takeContentChanged() || result == null) {
            forceLoad();
        }
    }

    @Override
    protected void onStopLoading() {
        cancelLoad();
    }
}
