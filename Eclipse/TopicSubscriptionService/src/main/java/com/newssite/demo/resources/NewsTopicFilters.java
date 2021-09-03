package com.newssite.demo.resources;

import java.time.LocalDate;
import java.util.Arrays;

import lombok.Builder;

@Builder
public class NewsTopicFilters {

	// Attributes
	private String[] includedSources;
	private String[] excludedSources;
	private String[] includedDomains;
	private String[] excludedDomains;
	private String[] includedCountries;
	private String[] includedLanguages;
	private String[] includedKeyWords;
	private String[] excludedKeyWords;
	// supported sortBy values are: "popularity", "latest",
	private String sortBy;
	private int paginationLimit;
	private int paginationOffset;
	private LocalDate from;
	private LocalDate until;

	// Constructors
	public NewsTopicFilters(String[] includedSources, String[] excludedSources, String[] includedDomains,
			String[] excludedDomains, String[] includedCountries, String[] includedLanguages, String[] includedKeyWords,
			String[] excludedKeyWords, String sortBy, int paginationLimit, int paginationOffset, LocalDate from,
			LocalDate until) {
		super();
		this.includedSources = includedSources;
		this.excludedSources = excludedSources;
		this.includedDomains = includedDomains;
		this.excludedDomains = excludedDomains;
		this.includedCountries = includedCountries;
		this.includedLanguages = includedLanguages;
		this.includedKeyWords = includedKeyWords;
		this.excludedKeyWords = excludedKeyWords;
		this.sortBy = sortBy;
		this.paginationLimit = paginationLimit;
		this.paginationOffset = paginationOffset;
		this.from = from;
		this.until = until;
	}

	public NewsTopicFilters() {
		super();
	}

	// Getters And Setters
	public String[] getIncludedSources() {
		return includedSources;
	}

	public void setIncludedSources(String[] includedSources) {
		this.includedSources = includedSources;
	}

	public String[] getExcludedSources() {
		return excludedSources;
	}

	public void setExcludedSources(String[] excludedSources) {
		this.excludedSources = excludedSources;
	}

	public String[] getIncludedDomains() {
		return includedDomains;
	}

	public void setIncludedDomains(String[] includedDomains) {
		this.includedDomains = includedDomains;
	}

	public String[] getExcludedDomains() {
		return excludedDomains;
	}

	public void setExcludedDomains(String[] excludedDomains) {
		this.excludedDomains = excludedDomains;
	}

	public String[] getIncludedCountries() {
		return includedCountries;
	}

	public void setIncludedCountries(String[] includedCountries) {
		this.includedCountries = includedCountries;
	}

	public String[] getIncludedLanguages() {
		return includedLanguages;
	}

	public void setIncludedLanguages(String[] includedLanguages) {
		this.includedLanguages = includedLanguages;
	}

	public String[] getIncludedKeyWords() {
		return includedKeyWords;
	}

	public void setIncludedKeyWords(String[] includedKeyWords) {
		this.includedKeyWords = includedKeyWords;
	}

	public String[] getExcludedKeyWords() {
		return excludedKeyWords;
	}

	public void setExcludedKeyWords(String[] excludedKeyWords) {
		this.excludedKeyWords = excludedKeyWords;
	}

	public String getSortBy() {
		return sortBy;
	}

	public void setSortBy(String sortBy) {
		this.sortBy = sortBy;
	}

	public int getPaginationLimit() {
		return paginationLimit;
	}

	public void setPaginationLimit(int paginationLimit) {
		this.paginationLimit = paginationLimit;
	}

	public int getPaginationOffset() {
		return paginationOffset;
	}

	public void setPaginationOffset(int paginationOffset) {
		this.paginationOffset = paginationOffset;
	}

	public LocalDate getFrom() {
		return from;
	}

	public void setFrom(LocalDate from) {
		this.from = from;
	}

	public LocalDate getUntil() {
		return until;
	}

	public void setUntil(LocalDate until) {
		this.until = until;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + Arrays.hashCode(excludedDomains);
		result = prime * result + Arrays.hashCode(excludedKeyWords);
		result = prime * result + Arrays.hashCode(excludedSources);
		result = prime * result + ((from == null) ? 0 : from.hashCode());
		result = prime * result + Arrays.hashCode(includedCountries);
		result = prime * result + Arrays.hashCode(includedDomains);
		result = prime * result + Arrays.hashCode(includedKeyWords);
		result = prime * result + Arrays.hashCode(includedLanguages);
		result = prime * result + Arrays.hashCode(includedSources);
		result = prime * result + paginationLimit;
		result = prime * result + paginationOffset;
		result = prime * result + ((sortBy == null) ? 0 : sortBy.hashCode());
		result = prime * result + ((until == null) ? 0 : until.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		NewsTopicFilters other = (NewsTopicFilters) obj;
		if (!Arrays.equals(excludedDomains, other.excludedDomains))
			return false;
		if (!Arrays.equals(excludedKeyWords, other.excludedKeyWords))
			return false;
		if (!Arrays.equals(excludedSources, other.excludedSources))
			return false;
		if (from == null) {
			if (other.from != null)
				return false;
		} else if (!from.equals(other.from))
			return false;
		if (!Arrays.equals(includedCountries, other.includedCountries))
			return false;
		if (!Arrays.equals(includedDomains, other.includedDomains))
			return false;
		if (!Arrays.equals(includedKeyWords, other.includedKeyWords))
			return false;
		if (!Arrays.equals(includedLanguages, other.includedLanguages))
			return false;
		if (!Arrays.equals(includedSources, other.includedSources))
			return false;
		if (paginationLimit != other.paginationLimit)
			return false;
		if (paginationOffset != other.paginationOffset)
			return false;
		if (sortBy == null) {
			if (other.sortBy != null)
				return false;
		} else if (!sortBy.equals(other.sortBy))
			return false;
		if (until == null) {
			if (other.until != null)
				return false;
		} else if (!until.equals(other.until))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "NewsTopicFilters [includedSources=" + Arrays.toString(includedSources) + ", excludedSources="
				+ Arrays.toString(excludedSources) + ", includedDomains=" + Arrays.toString(includedDomains)
				+ ", excludedDomains=" + Arrays.toString(excludedDomains) + ", includedCountries="
				+ Arrays.toString(includedCountries) + ", includedLanguages=" + Arrays.toString(includedLanguages)
				+ ", includedKeyWords=" + Arrays.toString(includedKeyWords) + ", excludedKeyWords="
				+ Arrays.toString(excludedKeyWords) + ", sortBy=" + sortBy + ", paginationLimit=" + paginationLimit
				+ ", paginationOffset=" + paginationOffset + ", from=" + from + ", until=" + until + "]";
	}
}
