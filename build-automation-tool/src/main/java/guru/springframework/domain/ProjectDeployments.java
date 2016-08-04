package guru.springframework.domain;

import javax.persistence.*;

@Entity
@Table(name = "projectdeployments")
public class ProjectDeployments {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;    
    
    @ManyToOne()
    @JoinColumn(name="project_id")
    private ProjectInfo projectinfo;
    
    @ManyToOne()
    @JoinColumn(name="environment_id")
    private Environment environment;
    
	public ProjectDeployments(){
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

	public Environment getEnvironment() {
		return environment;
	}

	public void setEnvironment(Environment environment) {
		this.environment = environment;
	}

	
}
