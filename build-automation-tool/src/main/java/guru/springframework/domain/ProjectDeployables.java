package guru.springframework.domain;

import javax.persistence.*;

@Entity
@Table(name = "projectdeployables")
public class ProjectDeployables {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;    
    
    @ManyToOne()
    @JoinColumn(name="project_id")
    private ProjectInfo projectinfo;
    
    @ManyToOne()
    @JoinColumn(name="library_id")
    private MessageLibraryWithRepo messagelibrarywithrepo;
    
    @ManyToOne()
    @JoinColumn(name="application_id")
    private MessageApplicationWithRepo messageapplicationwithrepo;
    
	public ProjectDeployables(){
		super();
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public ProjectInfo getProjectinfo() {
		return projectinfo;
	}

	public void setProjectinfo(ProjectInfo projectinfo) {
		this.projectinfo = projectinfo;
	}

	public MessageLibraryWithRepo getMessagelibrarywithrepo() {
		return messagelibrarywithrepo;
	}

	public void setMessagelibrarywithrepo(
			MessageLibraryWithRepo messagelibrarywithrepo) {
		this.messagelibrarywithrepo = messagelibrarywithrepo;
	}

	public MessageApplicationWithRepo getMessageapplicationwithrepo() {
		return messageapplicationwithrepo;
	}

	public void setMessageapplicationwithrepo(
			MessageApplicationWithRepo messageapplicationwithrepo) {
		this.messageapplicationwithrepo = messageapplicationwithrepo;
	}
}
