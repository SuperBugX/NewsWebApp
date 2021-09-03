package murraco.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

@Entity
public class BlacklistedSource {
	
	  @Id
	  @GeneratedValue(strategy = GenerationType.IDENTITY)
	  private Integer id;

	  @Column(name="blacklisted_domain")
	  private String name;

	  @Column(name="blacklisted_source_name")
	  private String blacklistedSourceName;
	  
	  @ManyToOne
	  @JoinColumn(name="blacklisted_source_fk")
	  private Subscription subscription;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getBlacklistedSourceName() {
		return blacklistedSourceName;
	}

	public void setBlacklistedSourceName(String blacklistedSourceName) {
		this.blacklistedSourceName = blacklistedSourceName;
	}

	public Subscription getSubscription() {
		return subscription;
	}

	public void setSubscription(Subscription subscription) {
		this.subscription = subscription;
	}
}
