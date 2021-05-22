export class NewsTopicFilters {
	// Attributes
	includedSources: string[];
	excludedSources: string[];
	includedDomains: string[];
	excludedDomains: string[];
	includedCountries: string[];
	includedLanguages: string[];
	includedKeyWords: string[];
	excludedKeyWords: string[];
	sortBy: string;
  paginationLimit: number;
	paginationOffset: number;
	from: any;
	until: any;

  constructor(){
    this.includedSources = null;
    this.excludedSources = null;
    this.includedDomains = null;
    this.excludedDomains = null;
    this.includedCountries = null;
    this.includedLanguages = null;
    this.includedKeyWords = null;
    this.excludedKeyWords = null;
    this.sortBy = null;
    this.paginationLimit = 0;
    this.paginationOffset = 0;
    this.from = null;
    this.until = null;
  }
}
