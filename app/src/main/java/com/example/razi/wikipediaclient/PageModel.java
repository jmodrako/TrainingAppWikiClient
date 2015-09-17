package com.example.razi.wikipediaclient;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

/**
 * Jacek Modrakowski
 * modrakowski.pl
 * 17/09/2015.
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
//@JsonPropertyOrder({
//		"description",
//		"id",
//		"imageUrl",
//		"subtitle",
//		"title"
//})
public class PageModel {
	@JsonProperty private Integer pageid;
	@JsonProperty private Integer ns;
	@JsonProperty private String title;
	@JsonProperty private String extract;

	@JsonProperty("pageid") public Integer getPageid() {
		return pageid;
	}

	@JsonProperty("ns") public Integer getNs() {
		return ns;
	}

	@JsonProperty("title") public String getTitle() {
		return title;
	}

	@JsonProperty("extract") public String getExtract() {
		return extract;
	}

	@JsonProperty("pageid") public void setPageid(Integer pageid) {
		this.pageid = pageid;
	}

	@JsonProperty("ns") public void setNs(Integer ns) {
		this.ns = ns;
	}

	@JsonProperty("title") public void setTitle(String title) {
		this.title = title;
	}

	@JsonProperty("extract") public void setExtract(String extract) {
		this.extract = extract;
	}
}
