package com.example.razi.wikipediaclient;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Jacek Modrakowski
 * modrakowski.pl
 * 17/09/2015.
 */
public class MainResponseModel {

	@JsonProperty boolean batchcomplete;
	@JsonProperty QueryModel query;


}
