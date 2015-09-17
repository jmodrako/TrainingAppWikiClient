package com.example.razi.wikipediaclient;

import retrofit.Callback;
import retrofit.http.GET;
import retrofit.http.Path;
import retrofit.http.Query;

/**
 * Jacek Modrakowski
 * modrakowski.pl
 * 17/09/2015.
 */
public interface WikiApi {

	@GET("/w/api.php?format=json&action=query&prop=extracts|pageimages|images|info&exintro=&explaintext=&formatversion=2")
	void wikiSearch(@Query("titles") String query, Callback<MainResponseModel> callback);

	@GET("/w/api.php?format=json&action=query&prop=extracts|pageimages|images|info&exintro=&explaintext=&formatversion=2")
	MainResponseModel wikiSearch(@Query("titles") String query);
}
