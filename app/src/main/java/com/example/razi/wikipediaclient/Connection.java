package com.example.razi.wikipediaclient;

import retrofit.RestAdapter;

/**
 * Jacek Modrakowski
 * modrakowski.pl
 * 17/09/2015.
 */
public class Connection {

	private static String URL ="https://en.wikipedia.org/";

	public static WikiApi createApi() {
		RestAdapter restAdapter = new RestAdapter.Builder()
				.setLogLevel(RestAdapter.LogLevel.FULL)
				.setEndpoint(URL)
				.build();

		return restAdapter.create(WikiApi.class);
	}
}
