package guru.springframework.domain;

import javax.persistence.*;

import java.util.Set;

@Entity
@Table(name = "broker")
public class Broker {
    
	@Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;

    private String name;
    private String description;
    
    @ManyToOne()
    @JoinColumn(name="environment_id")
    private Environment environment;
    
    @ManyToOne()
    @JoinColumn(name="queuemanager_id")
    private Queuemanager queuemanager;
    
    @OneToMany(mappedBy="broker", cascade=CascadeType.ALL)
    private Set<EGDetail> egdetails;
    
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
	
	public Environment getEnvironment() {
		return environment;
	}
	public void setEnvironment(Environment environment) {
		this.environment = environment;
	}
	
	public Queuemanager getQueuemanager() {
		return queuemanager;
	}
	public void setQueuemanager(Queuemanager queuemanager) {
		this.queuemanager = queuemanager;
	}
	
	public Set<EGDetail> getEgdetails() {
		return egdetails;
	}
	public void setEgdetails(Set<EGDetail> egdetails) {
		this.egdetails = egdetails;
	}
}
