package com.example.razi.wikipediaclient;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.LoaderManager;
import android.content.AsyncTaskLoader;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.Loader;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.Settings;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.razi.wikipediaclient.dialogs.NetworkErrorDialog;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class WikipediaActivity
        extends Activity
        implements Handler.Callback,
        LoaderManager.LoaderCallbacks<String>,
        NetworkErrorDialog.NetworkErrorDialogListener {

    private static final String KEY_MSG_QUERY_RESULT = "result";
    private static final String KEY_LOADER_QUERY = "query";
    private static final int ID_LOADER_QUERY = 1000;

    private enum SearchMode { READY, SEARCHING, RESULTS_READY, NO_RESULTS };
    private SearchMode searchMode;

    private EditText editTextQuery;
    private TextView textViewQueryResults;
    private TextView textViewNoResults;
    private ProgressBar progressBar;

    private boolean isActivityVisible = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setView();
        IntentFilter filter = new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION);
        registerReceiver(networkReceiver, filter);
    }

    @Override
    protected void onStart() {
        super.onStart();
        checkNetworkState();
        isActivityVisible = true;
    }

    @Override
    protected void onStop() {
        super.onStop();
        isActivityVisible = false;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(networkReceiver);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_wikipedia, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    /** Handler. */

    @Override
    public boolean handleMessage(Message msg) {
        if (msg != null) {
            String result = msg.getData().getString(KEY_MSG_QUERY_RESULT);
            updateResults(result);
        }
        return false;
    }

    /** Loader. */

    @Override
    public Loader<String> onCreateLoader(int id, Bundle args) {
        setSearchMode(SearchMode.SEARCHING);
        return (new QueryAsyncTaskLoader(this,
                editTextQuery.getText().toString()));
    }

    @Override
    public void onLoadFinished(Loader<String> loader, String data) {
        updateResults(data);
        getLoaderManager().destroyLoader(loader.getId());
    }

    @Override
    public void onLoaderReset(Loader<String> loader) {
        /// TODO
    }

    /**
     * NetworkErrorDialogListener.
     */

    @Override
    public void onConnect() {
        startActivity(new Intent(Settings.ACTION_SETTINGS));
    }

    @Override
    public void onDismiss() {
        finish();
    }

    private void updateResults(String queryResult) {
        if (!TextUtils.isEmpty(queryResult)) {
            textViewQueryResults.setText(queryResult);
            setSearchMode(SearchMode.RESULTS_READY);
        } else {
            setSearchMode(SearchMode.NO_RESULTS);
        }
    }

    private void setView() {

        setContentView(R.layout.activity_wikipedia);

        editTextQuery = (EditText) findViewById(R.id.wikipedia_query_text);
        textViewQueryResults = (TextView) findViewById(R.id.wikipedia_query_result);
        textViewNoResults = (TextView) findViewById(R.id.wikipedia_query_no_results);
        progressBar = (ProgressBar) findViewById(R.id.wikipedia_query_progress);

        editTextQuery.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {

                    //searchQueryRunnable(editTextQuery.getText().toString());

                    // searchQueryAsyncTask(editTextQuery.getText().toString());

//                    searchQueryLoader(editTextQuery.getText().toString());

					final WikiApi wikiApi = Connection.createApi();
					wikiApi.wikiSearch("Joseph_Fourier", new Callback<MainResponseModel>() {
						@Override public void success(MainResponseModel s, Response response) {
							Toast.makeText(WikipediaActivity.this, s.toString(),
									Toast.LENGTH_SHORT).show();
						}

						@Override public void failure(RetrofitError error) {
							Toast.makeText(WikipediaActivity.this, "ERROR: " + error, Toast.LENGTH_SHORT).show();
						}
					});

                    InputMethodManager imm =
                            (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(
                            editTextQuery.getWindowToken(), 0);

                    return (true);

                } else {

                    return (false);

                }
            }
        });

        setSearchMode(SearchMode.READY);
    }

    private void setSearchMode(SearchMode searchMode) {
        this.searchMode = searchMode;
        switch (searchMode) {
            case READY:
                textViewQueryResults.setVisibility(View.INVISIBLE);
                textViewNoResults.setVisibility(View.INVISIBLE);
                progressBar.setVisibility(View.INVISIBLE);
                break;
            case SEARCHING:
                textViewQueryResults.setVisibility(View.INVISIBLE);
                textViewNoResults.setVisibility(View.INVISIBLE);
                progressBar.setVisibility(View.VISIBLE);
                break;
            case RESULTS_READY:
                textViewQueryResults.setVisibility(View.VISIBLE);
                textViewNoResults.setVisibility(View.INVISIBLE);
                progressBar.setVisibility(View.INVISIBLE);
                break;
            case NO_RESULTS:
                textViewQueryResults.setVisibility(View.INVISIBLE);
                textViewNoResults.setVisibility(View.VISIBLE);
                progressBar.setVisibility(View.INVISIBLE);
                break;
        }
    }

    /**
     * Search methods.
     */

    private Handler handler = new Handler(this);

    private void searchQueryRunnable(final String query) {
        setSearchMode(SearchMode.SEARCHING);
        new Thread(new Runnable() {
            @Override
            public void run() {

                String resultJSON = NetworkUtils.sendWikiGetRequest(query);
                final String result = NetworkUtils.parseWikiJSON(resultJSON);

                /*Message msg = Message.obtain();

                if (result != null) {
                    Bundle bundle = new Bundle();
                    bundle.putString(KEY_MSG_QUERY_RESULT, result);
                    msg.setData(bundle);
                }

                handler.sendMessage(msg);*/

                textViewQueryResults.post(new Runnable() {
                    @Override
                    public void run() {
                        updateResults(result);
                    }
                });
            }
        }).start();
    }

    private void searchQueryAsyncTask(String query) {
        QueryAsyncTask task = new QueryAsyncTask();
        task.execute(query);
    }

    private void searchQueryLoader(String query) {
        getLoaderManager().initLoader(ID_LOADER_QUERY, null, this);
    }

    private void checkNetworkState() {
        if (!isNetworkAvailable()) {
            new NetworkErrorDialog().show(getFragmentManager(), "dialog");
        }
    }

    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager =
                (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        return ((networkInfo != null) && networkInfo.isConnected());
    }

    /**
     * Internal classes.
     */

    private class QueryAsyncTask extends AsyncTask<String, Integer, String> {

        @Override
        protected void onPreExecute() {
            setSearchMode(SearchMode.SEARCHING);
        }

        @Override
        protected String doInBackground(String... params) {
            String resultJSON = NetworkUtils.sendWikiGetRequest(params[0]);
            return (NetworkUtils.parseWikiJSON(resultJSON));
        }

        @Override
        protected void onPostExecute(String s) {
            updateResults(s);
        }
    }

    private BroadcastReceiver networkReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (isActivityVisible) {
                checkNetworkState();
            }
        }
    };
}
