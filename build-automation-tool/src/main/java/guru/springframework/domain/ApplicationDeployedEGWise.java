package guru.springframework.domain;

import javax.persistence.*;


@Entity
@Table(name = "applicationdeployedegwise")
public class ApplicationDeployedEGWise {
    
	@Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;
        
    @ManyToOne()
    @JoinColumn(name="eg_id")
    private EGDetail egdetail;
    
    @ManyToOne()
    @JoinColumn(name="app_id")
    private MessageApplicationWithRepo messageapplicationwithrepo;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public EGDetail getEgdetail() {
		return egdetail;
	}

	public void setEgdetail(EGDetail egdetail) {
		this.egdetail = egdetail;
	}

	public MessageApplicationWithRepo getMessageapplicationwithrepo() {
		return messageapplicationwithrepo;
	}

	public void setMessageapplicationwithrepo(
			MessageApplicationWithRepo messageapplicationwithrepo) {
		this.messageapplicationwithrepo = messageapplicationwithrepo;
	}       
    
	
}
