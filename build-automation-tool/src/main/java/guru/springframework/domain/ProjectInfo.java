package guru.springframework.domain;


import java.util.Set;

import javax.persistence.*;

@Entity
@Table(name = "projectinfo")
public class ProjectInfo {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;    

    private String name;
    private String shortdescription;
    private String description;
    private String implementationflag;
    
    @OneToMany(mappedBy="projectinfo", cascade=CascadeType.ALL)
    private Set<ProjectDeployables> projectdeployables;
    
    @OneToMany(mappedBy="projectinfo", cascade=CascadeType.ALL)
    private Set<ProjectDeployments> projectdeployments;
	
    @OneToMany(mappedBy="projectinfo", cascade=CascadeType.ALL)
    private Set<ReleaseWiseProjects> releasewiseprojects;
    
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
	public String getShortdescription() {
		return shortdescription;
	}
	public void setShortdescription(String shortdescription) {
		this.shortdescription = shortdescription;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getImplementationflag() {
		return implementationflag;
	}
	public void setImplementationflag(String implementationflag) {
		this.implementationflag = implementationflag;
	}
	public Set<ProjectDeployables> getProjectdeployables() {
		return projectdeployables;
	}
	public void setProjectdeployables(Set<ProjectDeployables> projectdeployables) {
		this.projectdeployables = projectdeployables;
	}
	public Set<ProjectDeployments> getProjectdeployments() {
		return projectdeployments;
	}
	public void setProjectdeployments(Set<ProjectDeployments> projectdeployments) {
		this.projectdeployments = projectdeployments;
	}
	public Set<ReleaseWiseProjects> getReleasewiseprojects() {
		return releasewiseprojects;
	}
	public void setReleasewiseprojects(Set<ReleaseWiseProjects> releasewiseprojects) {
		this.releasewiseprojects = releasewiseprojects;
	}
	
}
