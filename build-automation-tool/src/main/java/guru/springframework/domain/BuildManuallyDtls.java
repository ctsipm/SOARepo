package guru.springframework.domain;


public class BuildManuallyDtls {
    
	private BuildManually buildManually;
	private MessageApplicationWithRepo applicationWithRepo;
	private MessageLibraryWithRepo libraryWithRepo;
	private ManualBARCreationAppWiseInfo barCreationAppWiseInfo;
	private ManualBARCreationLibWiseInfo barCreationLibWiseInfo;
	private Iterable<MessageApplicationWithRepo> applicationWithRepoList;
	private Iterable<MessageLibraryWithRepo> libraryWithRepoList;
	private Iterable<ManualBARCreationAppWiseInfo> barCreationAppWiseInfoList;
	private Iterable<ManualBARCreationLibWiseInfo> barCreationLibWiseInfoList;
	
	
	public BuildManually getBuildManually() {
		return buildManually;
	}
	public void setBuildManually(BuildManually buildManually) {
		this.buildManually = buildManually;
	}
	public MessageApplicationWithRepo getApplicationWithRepo() {
		return applicationWithRepo;
	}
	public void setApplicationWithRepo(
			MessageApplicationWithRepo applicationWithRepo) {
		this.applicationWithRepo = applicationWithRepo;
	}
	public MessageLibraryWithRepo getLibraryWithRepo() {
		return libraryWithRepo;
	}
	public void setLibraryWithRepo(MessageLibraryWithRepo libraryWithRepo) {
		this.libraryWithRepo = libraryWithRepo;
	}
	public ManualBARCreationAppWiseInfo getBarCreationAppWiseInfo() {
		return barCreationAppWiseInfo;
	}
	public void setBarCreationAppWiseInfo(
			ManualBARCreationAppWiseInfo barCreationAppWiseInfo) {
		this.barCreationAppWiseInfo = barCreationAppWiseInfo;
	}
	public ManualBARCreationLibWiseInfo getBarCreationLibWiseInfo() {
		return barCreationLibWiseInfo;
	}
	public void setBarCreationLibWiseInfo(
			ManualBARCreationLibWiseInfo barCreationLibWiseInfo) {
		this.barCreationLibWiseInfo = barCreationLibWiseInfo;
	}
	public Iterable<MessageApplicationWithRepo> getApplicationWithRepoList() {
		return applicationWithRepoList;
	}
	public void setApplicationWithRepoList(
			Iterable<MessageApplicationWithRepo> applicationWithRepoList) {
		this.applicationWithRepoList = applicationWithRepoList;
	}
	public Iterable<MessageLibraryWithRepo> getLibraryWithRepoList() {
		return libraryWithRepoList;
	}
	public void setLibraryWithRepoList(
			Iterable<MessageLibraryWithRepo> libraryWithRepoList) {
		this.libraryWithRepoList = libraryWithRepoList;
	}
	public Iterable<ManualBARCreationAppWiseInfo> getBarCreationAppWiseInfoList() {
		return barCreationAppWiseInfoList;
	}
	public void setBarCreationAppWiseInfoList(
			Iterable<ManualBARCreationAppWiseInfo> barCreationAppWiseInfoList) {
		this.barCreationAppWiseInfoList = barCreationAppWiseInfoList;
	}
	public Iterable<ManualBARCreationLibWiseInfo> getBarCreationLibWiseInfoList() {
		return barCreationLibWiseInfoList;
	}
	public void setBarCreationLibWiseInfoList(
			Iterable<ManualBARCreationLibWiseInfo> barCreationLibWiseInfoList) {
		this.barCreationLibWiseInfoList = barCreationLibWiseInfoList;
	}
	
	
	
}
