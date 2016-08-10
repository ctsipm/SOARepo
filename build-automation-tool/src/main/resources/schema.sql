	
	/*
	drop table if exists environment; 
	create table environment(
	      id int unsigned not null AUTO_INCREMENT,
	      name varchar_ignorecase(50) not null,      
	      description varchar_ignorecase(200) not null,      
	      PRIMARY KEY(id)
	);
	
	drop table if exists queuemanager; 
	create table queuemanager(
	      id int unsigned not null AUTO_INCREMENT,
	      name varchar_ignorecase(50) not null,
	      hostname varchar_ignorecase(50) not null,
	      portnumber varchar_ignorecase(4) not null,
	      channelname varchar_ignorecase(50) not null,
	      description varchar_ignorecase(200) not null,
	      environment_id int unsigned DEFAULT NULL,
		  PRIMARY KEY(id),
		  foreign key (environment_id) references environment(id)
		  ); 
	 
	drop table if exists broker;  
	create table broker(
	      id int unsigned not null AUTO_INCREMENT,
	      name varchar_ignorecase(50) not null,
	      environment_id int unsigned DEFAULT NULL,
	      queuemanager_id int unsigned DEFAULT NULL,     
		PRIMARY KEY(id),
		foreign key (environment_id) references environment(id),
		foreign key (queuemanager_id) references queuemanager(id)
	);
	 
	drop table if exists EGDetail;      
	create table EGDetail(
	      id int not null AUTO_INCREMENT,
	      name varchar_ignorecase(50) not null,
	      broker_id int unsigned DEFAULT NULL,
	      PRIMARY KEY(id),
		  foreign key (broker_id) references broker(id)      
	      );
	      
	drop table if exists MessageApplicationWithRepo;       
	create table MessageApplicationWithRepo(
	      id int not null AUTO_INCREMENT,
	      name varchar_ignorecase(100) not null,      
	      reponame varchar_ignorecase(500) not null, 
	      repolocation varchar_ignorecase(500) not null,    
	      PRIMARY KEY(id) 
	      );
	      
	drop table if exists MessageFlowApplicationWise;       
	create table MessageFlowApplicationWise(
	      id int not null AUTO_INCREMENT,
	      name varchar_ignorecase(100) not null,            
	      application_id int unsigned DEFAULT NULL,
	      PRIMARY KEY(id),
	      foreign key (application_id) references MessageApplicationWithRepo(id)       
	      );
	 
	drop table if exists ApplicationDeployedEGWise;       
	create table ApplicationDeployedEGWise(
	      id int not null AUTO_INCREMENT,
	      app_id int unsigned DEFAULT NULL,           
	      eg_id int unsigned DEFAULT NULL,
	      PRIMARY KEY(id),
	      foreign key (app_id) references MessageApplicationWithRepo(id),
	      foreign key (eg_id) references EGDetail(id)
	      );
	  
	drop table if exists MessageLibraryWithRepo;       
	create table MessageLibraryWithRepo(
	      id int not null AUTO_INCREMENT,
	      name varchar_ignorecase(100) not null,      
	      reponame varchar_ignorecase(500) not null, 
	      repolocation varchar_ignorecase(500) not null,    
	      PRIMARY KEY(id) 
	      );
		  
	drop table if exists MessageSetLibraryWise;       
	create table MessageSetLibraryWise(
	      id int not null AUTO_INCREMENT,
	      name varchar_ignorecase(100) not null,            
	      library_id int unsigned DEFAULT NULL,
	      PRIMARY KEY(id),
	      foreign key (library_id) references MessageLibraryWithRepo(id)       
	      );
	      
	drop table if exists LibraryDeployedEGWise;       
	create table LibraryDeployedEGWise(
	      id int not null AUTO_INCREMENT,
	      eg_id int unsigned DEFAULT NULL,
	      lib_id int unsigned DEFAULT NULL,      
	      PRIMARY KEY(id),
	      foreign key (lib_id) references MessageLibraryWithRepo(id),
	      foreign key (eg_id) references EGDetail(id)      
	      );
	      
	drop table if exists ProjectInfo;	  
	create table ProjectInfo(
		  id int not null AUTO_INCREMENT,
		  name varchar_ignorecase(100) not null,
		  shortDescription varchar_ignorecase(300),
		  description varchar_ignorecase(1000),
		  implementationflag varchar_ignorecase(1) not null	  
		  );      
	
	drop table if exists ProjectDeployables;	  
	create table ProjectDeployables(
		  id int not null AUTO_INCREMENT,
		  project_id int unsigned DEFAULT NULL,
		  application_id int unsigned DEFAULT NULL,
		  -- messageflow_id int unsigned DEFAULT NULL,
		  library_id int unsigned DEFAULT NULL,
		  -- messageset_id int unsigned DEFAULT NULL,
		  --environment_id int unsigned DEFAULT NULL,
		  PRIMARY KEY(id),	  
		  foreign key (project_id) references ProjectInfo(id),
		  --foreign key (environment_id) references environment(id),
		  foreign key (application_id) references MessageApplicationWithRepo(id),
		  foreign key (library_id) references MessageLibraryWithRepo(id)	  
		  );
		  
	drop table if exists ProjectDeployments;	  
	create table ProjectDeployments(
		  id int not null AUTO_INCREMENT,
		  project_id int unsigned DEFAULT NULL,	 
		  environment_id int unsigned DEFAULT NULL,
		  PRIMARY KEY(id),	  
		  foreign key (project_id) references ProjectInfo(id),
		  foreign key (environment_id) references environment(id),
		  );	  
		  
	drop table if exists ReleaseInfo;	  
	 create table ReleaseInfo(
	 	  id int not null AUTO_INCREMENT,
		  name varchar_ignorecase(100) not null,
		  shortDescription varchar_ignorecase(300),
		  description varchar_ignorecase(1000),
		  PRIMARY KEY(id),
		  implementationflag varchar_ignorecase(1) not null
		  );
	
	drop table if exists ReleaseWiseProjects;	  
	 create table ReleaseWiseProjects(
	 	  id int not null AUTO_INCREMENT,
		  project_id int not null,
		  release_id int not null,
		  PRIMARY KEY(id),
		  foreign key (project_id) references ProjectInfo(id),
		  foreign key (release_id) references ReleaseInfo(id)
		  );
	
	drop table if exists BuildManually;	  
	 create table BuildManually(
	 	  id int not null AUTO_INCREMENT,
	 	  name varchar_ignorecase(100) not null,
	 	  version varchar_ignorecase(20) not null,
	 	  creationdate date,
	 	  deleteflag varchar_ignorecase(1) not null,
	 	  PRIMARY KEY(id)
		  );		  
		  
	drop table if exists ManualBARCreationAppWiseInfo;	  
	 create table ManualBARCreationAppWiseInfo(
	 	  id int not null AUTO_INCREMENT,
	 	  name varchar_ignorecase(100) not null,
	 	  version varchar_ignorecase(20) not null,
	 	  repolocation varchar_ignorecase(200) not null,
	 	  application_id int not null,
	 	  manualbuild_id int not null,
		  PRIMARY KEY(id),
		  foreign key (application_id) references MessageApplicationWithRepo(id),
		  foreign key (manualbuild_id) references BuildManually(id)
		  );
		  
	drop table if exists ManualBARCreationLibWiseInfo;	  
	 create table ManualBARCreationLibWiseInfo(
	 	  id int not null AUTO_INCREMENT,
	 	  name varchar_ignorecase(100) not null,
	 	  version varchar_ignorecase(20) not null,
	 	  repolocation varchar_ignorecase(200) not null,
	 	  library_id int not null,
	 	  manualbuild_id int not null,
		  PRIMARY KEY(id),
		  foreign key (library_id) references MessageLibraryWithRepo(id),
		  foreign key (manualbuild_id) references BuildManually(id)
		  );
	*/
	  
	
	  
		