package guru.springframework.domain;

import java.util.Set;

import javax.persistence.*;


@Entity
@Table(name = "queuemanager")
public class Queuemanager {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;
    
    private String name;
    private String hostname;
    private String portnumber;
    private String channelname;
    private String description;
    
    @OneToMany(mappedBy="queuemanager", cascade=CascadeType.ALL)
    private Set<Broker> brokers; 
    
    @ManyToOne()
    @JoinColumn(name="environment_id")
    private Environment environment;
    

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
	public String getHostname() {
		return hostname;
	}
	public void setHostname(String hostname) {
		this.hostname = hostname;
	}
	public String getPortnumber() {
		return portnumber;
	}
	public void setPortnumber(String portnumber) {
		this.portnumber = portnumber;
	}
	public String getChannelname() {
		return channelname;
	}
	public void setChannelname(String channelname) {
		this.channelname = channelname;
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
	public Set<Broker> getBrokers() {
		return brokers;
	}
	public void setBrokers(Set<Broker> brokers) {
		this.brokers = brokers;
	} 
    
}
