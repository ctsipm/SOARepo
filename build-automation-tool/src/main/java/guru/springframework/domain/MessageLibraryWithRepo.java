package guru.springframework.domain;

import java.util.Set;

import javax.persistence.*;


@Entity
@Table(name = "messagelibrarywithrepo")
public class MessageLibraryWithRepo {
    
	@Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;
    
    private String name;
        
    private String reponame;
	    
    private String repolocation;
    
    @OneToMany(mappedBy="messagelibrarywithrepo", cascade=CascadeType.ALL)
    private Set<MessageSetLibraryWise> messagesetlibrarywise;
    
    @OneToMany(mappedBy="messagelibrarywithrepo", cascade=CascadeType.ALL)
    private Set<LibraryDeployedEGWise> librarydeployedegwise;
    
    @OneToMany(mappedBy="messagelibrarywithrepo", cascade=CascadeType.ALL)
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

	public Set<MessageSetLibraryWise> getMessagesetlibrarywise() {
		return messagesetlibrarywise;
	}

	public void setMessagesetlibrarywise(
			Set<MessageSetLibraryWise> messagesetlibrarywise) {
		this.messagesetlibrarywise = messagesetlibrarywise;
	}

	public Set<LibraryDeployedEGWise> getLibrarydeployedegwise() {
		return librarydeployedegwise;
	}

	public void setLibrarydeployedegwise(
			Set<LibraryDeployedEGWise> librarydeployedegwise) {
		this.librarydeployedegwise = librarydeployedegwise;
	}

	public Set<ProjectDeployables> getProjectdeployables() {
		return projectdeployables;
	}

	public void setProjectdeployables(Set<ProjectDeployables> projectdeployables) {
		this.projectdeployables = projectdeployables;
	}
	
}
