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
    
    CREATE TABLE IF NOT EXISTS messages(
    idRecruiter int NOT NULL,
    idCandidate int NOT NULL,
    FOREIGN KEY (idRecruiter) REFERENCES candidate(id),
    FOREIGN KEY (idCandidate) REFERENCES recruiter(id),
    PRIMARY KEY (idRecruiter, idCandidate)
    );

	ALTER TABLE jobs
	ADD CONSTRAINT fk_jobs_recruiter FOREIGN KEY (idRecruiter) REFERENCES recruiter(id) ON DELETE CASCADE;
    
    ALTER TABLE messages
	ADD CONSTRAINT fk_messages_recruiter FOREIGN KEY (idRecruiter) REFERENCES recruiter(id) ON DELETE CASCADE;
    
	ALTER TABLE skills
	ADD CONSTRAINT fk_skills_candidate FOREIGN KEY (idCandidate) REFERENCES candidate(id) ON DELETE CASCADE;
    
	ALTER TABLE messages
	ADD CONSTRAINT fk_messages_candidate FOREIGN KEY (idCandidate) REFERENCES candidate(id) ON DELETE CASCADE;
    
    
    