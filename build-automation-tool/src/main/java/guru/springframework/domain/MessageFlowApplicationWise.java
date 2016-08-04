package guru.springframework.domain;


import javax.persistence.*;


@Entity
@Table(name = "messageflowapplicationwise")
public class MessageFlowApplicationWise {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;
    
    private String name;
        
    @ManyToOne()
    @JoinColumn(name="application_id")
    private MessageApplicationWithRepo messageapplicationwithrepo;

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

	public MessageApplicationWithRepo getMessageapplicationwithrepo() {
		return messageapplicationwithrepo;
	}

	public void setMessageapplicationwithrepo(
			MessageApplicationWithRepo messageapplicationwithrepo) {
		this.messageapplicationwithrepo = messageapplicationwithrepo;
	}
	
}
