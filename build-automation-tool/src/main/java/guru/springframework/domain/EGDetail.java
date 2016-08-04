package guru.springframework.domain;

import javax.persistence.*;

import java.util.Set;

@Entity
@Table(name = "egdetail")
public class EGDetail {
    
	@Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;
    private String name;
        
    @ManyToOne()
    @JoinColumn(name="broker_id")
    private Broker broker;
    
    @OneToMany(mappedBy="egdetail", cascade=CascadeType.ALL)
    private Set<ApplicationDeployedEGWise> applicationdeployedegwise;
    
    @OneToMany(mappedBy="egdetail", cascade=CascadeType.ALL)
    private Set<LibraryDeployedEGWise> messagelibraryegwise;

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

	public Broker getBroker() {
		return broker;
	}

	public void setBroker(Broker broker) {
		this.broker = broker;
	}

	public Set<LibraryDeployedEGWise> getMessagelibraryegwise() {
		return messagelibraryegwise;
	}

	public void setMessagelibraryegwise(
			Set<LibraryDeployedEGWise> messagelibraryegwise) {
		this.messagelibraryegwise = messagelibraryegwise;
	}

	public Set<ApplicationDeployedEGWise> getApplicationdeployedegwise() {
		return applicationdeployedegwise;
	}

	public void setApplicationdeployedegwise(
			Set<ApplicationDeployedEGWise> applicationdeployedegwise) {
		this.applicationdeployedegwise = applicationdeployedegwise;
	}      
    
}
