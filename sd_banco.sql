	CREATE DATABASE projeto_sd;
    USE projeto_sd;
    
    CREATE TABLE IF NOT EXISTS candidate(
    id int NOT NULL UNIQUE AUTO_INCREMENT PRIMARY KEY,
    Email varchar(100),
    Nome varchar(60),
    senha varchar(20)
    );
    
    
    CREATE TABLE IF NOT EXISTS recruiter(
    id int NOT NULL UNIQUE AUTO_INCREMENT PRIMARY KEY,
    email varchar(100),
    nome varchar(60),
    senha varchar(20),
    industry varchar(60),
    description varchar(255)
    );
    
    CREATE TABLE IF NOT EXISTS skillDataset(
    idSkill int NOT NULL UNIQUE AUTO_INCREMENT PRIMARY KEY,
    nameSkill varchar(20)
    );
    
    CREATE TABLE IF NOT EXISTS skills(
    idCandidate int NOT NULL,
    idSkillDataset int NOT NULL,
    experiencia int NOT NULL,
    FOREIGN KEY (idCandidate) REFERENCES candidate(id),
    FOREIGN KEY (idSkillDataset) REFERENCES skillDataset(idSkill),
    PRIMARY KEY(idCandidate, idSkillDataset)
    );
    
    INSERT INTO skillDataset (nameSkill)
    VALUES ('NodeJs'),('JavaScript'),('Java'),('C'),('HTML'),('CSS'),('React'),('ReactNative'),('TypeScript'),('Ruby');
    
    
	CREATE TABLE IF NOT EXISTS jobs(
	idJob int NOT NULL AUTO_INCREMENT PRIMARY KEY,
	idRecruiter int NOT NULL,
	idSkillDataset int NOT NULL,
	experiencia int,
    avaliable boolean,
    searchable boolean,
	FOREIGN KEY (idRecruiter) REFERENCES recruiter(id),
	FOREIGN KEY (idSkillDataset) REFERENCES skillDataset(idSkill)
	);

    
    
    