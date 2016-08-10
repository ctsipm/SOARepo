package guru.springframework.domain;

import javax.persistence.*;

import java.util.Set;

@Entity
@Table(name = "manualbarcreationappwiseinfo")
public class ManualBARCreationAppWiseInfo {
    
	@Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;
	
	private String name;
	private String version;
	private String repolocation; 
        
	@ManyToOne()
    @JoinColumn(name="application_id")
    private MessageApplicationWithRepo messageapplicationwithrepo;
	
	@ManyToOne()
    @JoinColumn(name="manualbuild_id")
    private BuildManually buildmanually;

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

	public String getVersion() {
		return version;
	}

	public void setVersion(String version) {
		this.version = version;
	}

	public MessageApplicationWithRepo getMessageapplicationwithrepo() {
		return messageapplicationwithrepo;
	}

	public void setMessageapplicationwithrepo(
			MessageApplicationWithRepo messageapplicationwithrepo) {
		this.messageapplicationwithrepo = messageapplicationwithrepo;
	}

	public BuildManually getBuildmanually() {
		return buildmanually;
	}

	public void setBuildmanually(BuildManually buildmanually) {
		this.buildmanually = buildmanually;
	}

	public String getRepolocation() {
		return repolocation;
	}

	public void setRepolocation(String repolocation) {
		this.repolocation = repolocation;
	}
	
}
