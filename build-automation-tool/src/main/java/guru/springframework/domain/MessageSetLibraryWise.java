package guru.springframework.domain;


import javax.persistence.*;


@Entity
@Table(name = "messagesetlibrarywise")
public class MessageSetLibraryWise {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;
    
    private String name;
        
    @ManyToOne()
    @JoinColumn(name="library_id")
    private MessageLibraryWithRepo messagelibrarywithrepo;

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

	public MessageLibraryWithRepo getMessagelibrarywithrepo() {
		return messagelibrarywithrepo;
	}

	public void setMessagelibrarywithrepo(
			MessageLibraryWithRepo messagelibrarywithrepo) {
		this.messagelibrarywithrepo = messagelibrarywithrepo;
	}
	    
}
