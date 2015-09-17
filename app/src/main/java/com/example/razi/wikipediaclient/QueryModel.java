package com.example.razi.wikipediaclient;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

/**
 * Jacek Modrakowski
 * modrakowski.pl
 * 17/09/2015.
 */
public class QueryModel {

	@JsonProperty List<PageModel> pages;

	public List<PageModel> getPages() {
		return pages;
	}
}
