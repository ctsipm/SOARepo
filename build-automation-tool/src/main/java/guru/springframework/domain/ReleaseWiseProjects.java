package guru.springframework.domain;

import javax.persistence.*;

import java.util.Set;

@Entity
@Table(name = "releasewiseprojects")
public class ReleaseWiseProjects {
    
	@Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;
        
	@ManyToOne()
    @JoinColumn(name="project_id")
    private ProjectInfo projectinfo;
	
	@ManyToOne()
    @JoinColumn(name="release_id")
    private ReleaseInfo releaseinfo;

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

	public ReleaseInfo getReleaseinfo() {
		return releaseinfo;
	}

	public void setReleaseinfo(ReleaseInfo releaseinfo) {
		this.releaseinfo = releaseinfo;
	}
	
	
	
}
