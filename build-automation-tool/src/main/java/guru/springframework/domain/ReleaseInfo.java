package guru.springframework.domain;


import java.util.Set;

import javax.persistence.*;

@Entity
@Table(name = "releaseinfo")
public class ReleaseInfo {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;    

    private String name;
    private String shortdescription;
    private String description;
    private String implementationflag;
    
    @OneToMany(mappedBy="releaseinfo", cascade=CascadeType.ALL)
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
	public Set<ReleaseWiseProjects> getReleasewiseprojects() {
		return releasewiseprojects;
	}
	public void setReleasewiseprojects(Set<ReleaseWiseProjects> releasewiseprojects) {
		this.releasewiseprojects = releasewiseprojects;
	}
    
}
