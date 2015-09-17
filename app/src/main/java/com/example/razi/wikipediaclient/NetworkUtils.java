package com.example.razi.wikipediaclient;

import android.text.TextUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Iterator;

public class NetworkUtils {
    /**
     * Sends wiki request.
     *
     * @param query
     * @return
     */
    public static String sendWikiGetRequest(String query)  {
        URL url = null;
        HttpURLConnection urlConnection = null;
        String result = null;
        try {
            url = new URL("https://en.wikipedia.org/w/api.php?format=json&action=query&prop=extracts&exintro=&explaintext=&titles=" + query);
            urlConnection = (HttpURLConnection) url.openConnection();
            InputStream in = new BufferedInputStream(urlConnection.getInputStream());
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(in));
            String line;
            StringBuffer buffer = new StringBuffer();
            while ((line = bufferedReader.readLine()) != null) {
                buffer.append(line);
            }
            result = buffer.toString();
        }
        catch (IOException e) {
        }
        finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
        }
        return (result);
    }

    /**
     * Parses wiki JSON.
     *
     * @param json
     * @return
     */
    public static String parseWikiJSON(String json) {
        String result = null;
        try {
            JSONObject mainWikiObject = new JSONObject(json);
            if (mainWikiObject.has("query")) {
                JSONObject queryObject = mainWikiObject.getJSONObject("query");
                if (queryObject.has("pages")) {
                    JSONObject pagesObject = queryObject.getJSONObject("pages");
                    for(Iterator<String> iter = pagesObject.keys();iter.hasNext();) {
                        String pageId = iter.next();
                        if (pagesObject.has(pageId)) {
                            JSONObject pageObject = pagesObject.getJSONObject(pageId);
                            result = pageObject.getString("extract");
                            break;
                        }
                    }
                }
            }
        }
        catch (JSONException e)
        {
        }
        return (result);
    }
}
