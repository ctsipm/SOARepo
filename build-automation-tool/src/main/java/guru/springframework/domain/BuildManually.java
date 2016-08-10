package guru.springframework.domain;


import java.sql.Date;
import java.util.Set;

import javax.persistence.*;

@Entity
@Table(name = "buildmanually")
public class BuildManually {
    
	@Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;    

    private String name;
    private String version;
    private Date creationdate;
    private String deleteflag;
    
    @OneToMany(mappedBy="buildmanually", cascade=CascadeType.ALL)
    private Set<ManualBARCreationAppWiseInfo> manualbarcreationappwiseinfo;
    
    @OneToMany(mappedBy="buildmanually", cascade=CascadeType.ALL)
    private Set<ManualBARCreationLibWiseInfo> manualbarcreationlibwiseinfo;
	
    
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
	public Date getCreationdate() {
		return creationdate;
	}
	public void setCreationdate(Date creationdate) {
		this.creationdate = creationdate;
	}
	public String getDeleteflag() {
		return deleteflag;
	}
	public void setDeleteflag(String deleteflag) {
		this.deleteflag = deleteflag;
	}
	
	public Set<ManualBARCreationAppWiseInfo> getManualbarcreationappwiseinfo() {
		return manualbarcreationappwiseinfo;
	}
	public void setManualbarcreationappwiseinfo(
			Set<ManualBARCreationAppWiseInfo> manualbarcreationappwiseinfo) {
		this.manualbarcreationappwiseinfo = manualbarcreationappwiseinfo;
	}
	public Set<ManualBARCreationLibWiseInfo> getManualbarcreationlibwiseinfo() {
		return manualbarcreationlibwiseinfo;
	}
	public void setManualbarcreationlibwiseinfo(
			Set<ManualBARCreationLibWiseInfo> manualbarcreationlibwiseinfo) {
		this.manualbarcreationlibwiseinfo = manualbarcreationlibwiseinfo;
	}
       
}
