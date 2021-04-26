package murraco.model;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

@Entity
public class NewsTopic {
	
	  @Id
	  @GeneratedValue(strategy = GenerationType.IDENTITY)
	  private Integer id;

	  @Column(unique = true, nullable = false)
	  private String name;

	  @Column(name="code_name", unique = true, nullable = false)
	  private String code_name;
	  
	  @OneToMany(mappedBy="userFK")
	  private List<Subscription> subscriptions;
	  
}
