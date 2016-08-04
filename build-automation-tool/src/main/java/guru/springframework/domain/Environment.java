package guru.springframework.domain;

import java.util.Set;

import javax.persistence.*;

@Entity
@Table(name = "environment")
public class Environment {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;    

    private String name;
    private String description;
    
    @OneToMany(mappedBy="environment", cascade=CascadeType.ALL)
    private Set<Queuemanager> queuemanagers;   
    
    @OneToMany(mappedBy="environment", cascade=CascadeType.ALL)
    private Set<Broker> brokers;  
    
    @OneToMany(mappedBy="environment", cascade=CascadeType.ALL)
    private Set<ProjectDeployments> projectdeployments;
    
   
	public Environment(){
		super();
	}
	
	public Environment(String name, String description) {
		super();
		this.name = name;
		this.description = description;
	}
    
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
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}

	public Set<Queuemanager> getQueuemanagers() {
		return queuemanagers;
	}

	public void setQueuemanagers(Set<Queuemanager> queuemanagers) {
		this.queuemanagers = queuemanagers;
	}

	public Set<Broker> getBrokers() {
		return brokers;
	}

	public void setBrokers(Set<Broker> brokers) {
		this.brokers = brokers;
	}

	public Set<ProjectDeployments> getProjectdeployments() {
		return projectdeployments;
	}

	public void setProjectdeployments(Set<ProjectDeployments> projectdeployments) {
		this.projectdeployments = projectdeployments;
	}	
    
}
