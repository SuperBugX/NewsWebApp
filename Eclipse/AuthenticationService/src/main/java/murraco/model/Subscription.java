package murraco.model;

import java.util.List;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

@Entity
public class Subscription {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

	@ManyToOne
	@JoinColumn(name = "accountFK")
	private Account accountFK;

	@ManyToOne
	@JoinColumn(name = "news_category_fk")
	private NewsCategory newsCategoryFK;

	@OneToMany(mappedBy = "subscription")
	private List<BlacklistedSource> blacklistedSources;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Account getAccountFK() {
		return accountFK;
	}

	public void setAccountFK(Account accountFK) {
		this.accountFK = accountFK;
	}

	public NewsCategory getNewsCategoryFK() {
		return newsCategoryFK;
	}

	public void setNewsCategoryFK(NewsCategory newsCategoryFK) {
		this.newsCategoryFK = newsCategoryFK;
	}

	public List<BlacklistedSource> getBlacklistedSources() {
		return blacklistedSources;
	}

	public void setBlacklistedSources(List<BlacklistedSource> blacklistedSources) {
		this.blacklistedSources = blacklistedSources;
	}
}
