package guru.springframework.domain;

import javax.persistence.*;


@Entity
@Table(name = "librarydeployedegwise")
public class LibraryDeployedEGWise {
    
	@Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;
    
    @ManyToOne()
    @JoinColumn(name="eg_id")
    private EGDetail egdetail;
    
    @ManyToOne()
    @JoinColumn(name="lib_id")
    private MessageLibraryWithRepo messagelibrarywithrepo;   
    
    
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

	public MessageLibraryWithRepo getMessagelibrarywithrepo() {
		return messagelibrarywithrepo;
	}

	public void setMessagelibrarywithrepo(
			MessageLibraryWithRepo messagelibrarywithrepo) {
		this.messagelibrarywithrepo = messagelibrarywithrepo;
	}

}
