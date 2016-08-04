package guru.springframework.domain;

import java.util.Set;

import javax.persistence.*;


@Entity
@Table(name = "messageapplicationwithrepo")
public class MessageApplicationWithRepo {
    
	@Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;
    
    private String name;
        
    private String reponame;
	    
    private String repolocation;
    
    @OneToMany(mappedBy="messageapplicationwithrepo", cascade=CascadeType.ALL)
    private Set<MessageFlowApplicationWise> messageflowapplicationwise;
    
    @OneToMany(mappedBy="messageapplicationwithrepo", cascade=CascadeType.ALL)
    private Set<ApplicationDeployedEGWise> applicationdeployedegwise;
    
    @OneToMany(mappedBy="messageapplicationwithrepo", cascade=CascadeType.ALL)
    private Set<ProjectDeployables> projectdeployables;

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

	public String getReponame() {
		return reponame;
	}

	public void setReponame(String reponame) {
		this.reponame = reponame;
	}

	public String getRepolocation() {
		return repolocation;
	}

	public void setRepolocation(String repolocation) {
		this.repolocation = repolocation;
	}

	public Set<MessageFlowApplicationWise> getMessageflowapplicationwise() {
		return messageflowapplicationwise;
	}

	public void setMessageflowapplicationwise(
			Set<MessageFlowApplicationWise> messageflowapplicationwise) {
		this.messageflowapplicationwise = messageflowapplicationwise;
	}

	public Set<ApplicationDeployedEGWise> getApplicationdeployedegwise() {
		return applicationdeployedegwise;
	}

	public void setApplicationdeployedegwise(
			Set<ApplicationDeployedEGWise> applicationdeployedegwise) {
		this.applicationdeployedegwise = applicationdeployedegwise;
	}

	public Set<ProjectDeployables> getProjectdeployables() {
		return projectdeployables;
	}

	public void setProjectdeployables(Set<ProjectDeployables> projectdeployables) {
		this.projectdeployables = projectdeployables;
	}    
    
}
