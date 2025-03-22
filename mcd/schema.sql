CREATE SCHEMA crm;

CREATE  TABLE crm.customer_login_info ( 
	id                   INT    NOT NULL AUTO_INCREMENT  PRIMARY KEY,
	password             VARCHAR(255)       ,
	username             VARCHAR(255)       ,
	token                VARCHAR(500)       ,
	password_set         BOOLEAN  DEFAULT ('0')     ,
	CONSTRAINT token UNIQUE ( token ) 
 ) ENGINE=InnoDB AUTO_INCREMENT=19 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

CREATE  TABLE crm.employee ( 
	id                   INT    NOT NULL AUTO_INCREMENT  PRIMARY KEY,
	username             VARCHAR(45)    NOT NULL   ,
	first_name           VARCHAR(45)    NOT NULL   ,
	last_name            VARCHAR(45)    NOT NULL   ,
	email                VARCHAR(45)    NOT NULL   ,
	password             VARCHAR(80)    NOT NULL   ,
	provider             VARCHAR(45)       
 ) ENGINE=InnoDB AUTO_INCREMENT=9 DEFAULT CHARSET=latin1;

CREATE  TABLE crm.roles ( 
	id                   INT    NOT NULL AUTO_INCREMENT  PRIMARY KEY,
	name                 VARCHAR(255)       
 ) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

CREATE  TABLE crm.users ( 
	id                   INT    NOT NULL AUTO_INCREMENT  PRIMARY KEY,
	email                VARCHAR(100)    NOT NULL   ,
	password             VARCHAR(255)       ,
	hire_date            DATETIME       ,
	created_at           TIMESTAMP  DEFAULT (CURRENT_TIMESTAMP)     ,
	updated_at           TIMESTAMP  DEFAULT (CURRENT_TIMESTAMP) ON UPDATE CURRENT_TIMESTAMP    ,
	username             VARCHAR(50)    NOT NULL   ,
	`status`             VARCHAR(100)       ,
	token                VARCHAR(500)       ,
	is_password_set      BOOLEAN  DEFAULT ('0')     ,
	CONSTRAINT email UNIQUE ( email ) ,
	CONSTRAINT username UNIQUE ( username ) 
 ) ENGINE=InnoDB AUTO_INCREMENT=54 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

CREATE  TABLE crm.customer ( 
	customer_id          INT UNSIGNED   NOT NULL AUTO_INCREMENT  PRIMARY KEY,
	name                 VARCHAR(255)       ,
	phone                VARCHAR(20)       ,
	address              VARCHAR(255)       ,
	city                 VARCHAR(255)       ,
	state                VARCHAR(255)       ,
	country              VARCHAR(255)       ,
	user_id              INT       ,
	description          TEXT       ,
	position             VARCHAR(255)       ,
	twitter              VARCHAR(255)       ,
	facebook             VARCHAR(255)       ,
	youtube              VARCHAR(255)       ,
	created_at           DATETIME       ,
	email                VARCHAR(255)       ,
	profile_id           INT       ,
	CONSTRAINT customer_ibfk_1 FOREIGN KEY ( user_id ) REFERENCES crm.users( id ) ON DELETE NO ACTION ON UPDATE NO ACTION,
	CONSTRAINT customer_ibfk_2 FOREIGN KEY ( profile_id ) REFERENCES crm.customer_login_info( id ) ON DELETE NO ACTION ON UPDATE NO ACTION
 ) ENGINE=InnoDB AUTO_INCREMENT=43 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

CREATE INDEX user_id ON crm.customer ( user_id );

CREATE INDEX profile_id ON crm.customer ( profile_id );

CREATE  TABLE crm.email_template ( 
	template_id          INT UNSIGNED   NOT NULL AUTO_INCREMENT  PRIMARY KEY,
	name                 VARCHAR(255)       ,
	content              TEXT       ,
	user_id              INT       ,
	json_design          TEXT       ,
	created_at           DATETIME       ,
	CONSTRAINT name UNIQUE ( name ) ,
	CONSTRAINT email_template_ibfk_1 FOREIGN KEY ( user_id ) REFERENCES crm.users( id ) ON DELETE NO ACTION ON UPDATE NO ACTION
 ) ENGINE=InnoDB AUTO_INCREMENT=35 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

CREATE INDEX user_id ON crm.email_template ( user_id );

CREATE  TABLE crm.lead_settings ( 
	id                   INT UNSIGNED   NOT NULL AUTO_INCREMENT  PRIMARY KEY,
	`status`             BOOLEAN       ,
	meeting              BOOLEAN       ,
	phone                BOOLEAN       ,
	name                 BOOLEAN       ,
	user_id              INT       ,
	status_email_template INT UNSIGNED      ,
	phone_email_template INT UNSIGNED      ,
	meeting_email_template INT UNSIGNED      ,
	name_email_template  INT UNSIGNED      ,
	customer_id          INT       ,
	CONSTRAINT lead_settings_ibfk_1 FOREIGN KEY ( user_id ) REFERENCES crm.users( id ) ON DELETE NO ACTION ON UPDATE NO ACTION,
	CONSTRAINT lead_settings_ibfk_2 FOREIGN KEY ( status_email_template ) REFERENCES crm.email_template( template_id ) ON DELETE NO ACTION ON UPDATE NO ACTION,
	CONSTRAINT lead_settings_ibfk_3 FOREIGN KEY ( phone_email_template ) REFERENCES crm.email_template( template_id ) ON DELETE NO ACTION ON UPDATE NO ACTION,
	CONSTRAINT lead_settings_ibfk_4 FOREIGN KEY ( meeting_email_template ) REFERENCES crm.email_template( template_id ) ON DELETE NO ACTION ON UPDATE NO ACTION,
	CONSTRAINT lead_settings_ibfk_5 FOREIGN KEY ( name_email_template ) REFERENCES crm.email_template( template_id ) ON DELETE NO ACTION ON UPDATE NO ACTION,
	CONSTRAINT lead_settings_ibfk_6 FOREIGN KEY ( customer_id ) REFERENCES crm.customer_login_info( id ) ON DELETE NO ACTION ON UPDATE NO ACTION
 ) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

CREATE INDEX user_id ON crm.lead_settings ( user_id );

CREATE INDEX status_email_template ON crm.lead_settings ( status_email_template );

CREATE INDEX phone_email_template ON crm.lead_settings ( phone_email_template );

CREATE INDEX meeting_email_template ON crm.lead_settings ( meeting_email_template );

CREATE INDEX name_email_template ON crm.lead_settings ( name_email_template );

CREATE INDEX customer_id ON crm.lead_settings ( customer_id );

CREATE  TABLE crm.oauth_users ( 
	id                   INT    NOT NULL AUTO_INCREMENT  PRIMARY KEY,
	user_id              INT       ,
	access_token         VARCHAR(255)    NOT NULL   ,
	access_token_issued_at DATETIME    NOT NULL   ,
	access_token_expiration DATETIME    NOT NULL   ,
	refresh_token        VARCHAR(255)    NOT NULL   ,
	refresh_token_issued_at DATETIME    NOT NULL   ,
	refresh_token_expiration DATETIME       ,
	granted_scopes       VARCHAR(255)       ,
	email                VARCHAR(255)       ,
	CONSTRAINT email UNIQUE ( email ) ,
	CONSTRAINT oauth_users_ibfk_1 FOREIGN KEY ( user_id ) REFERENCES crm.users( id ) ON DELETE NO ACTION ON UPDATE NO ACTION
 ) ENGINE=InnoDB AUTO_INCREMENT=30 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

CREATE INDEX oauth_users_ibfk_1 ON crm.oauth_users ( user_id );

CREATE  TABLE crm.ticket_settings ( 
	id                   INT UNSIGNED   NOT NULL AUTO_INCREMENT  PRIMARY KEY,
	priority             BOOLEAN       ,
	subject              BOOLEAN       ,
	description          BOOLEAN       ,
	`status`             BOOLEAN       ,
	user_id              INT       ,
	status_email_template INT UNSIGNED      ,
	subject_email_template INT UNSIGNED      ,
	priority_email_template INT UNSIGNED      ,
	description_email_template INT UNSIGNED      ,
	customer_id          INT       ,
	CONSTRAINT ticket_settings_ibfk_1 FOREIGN KEY ( user_id ) REFERENCES crm.users( id ) ON DELETE NO ACTION ON UPDATE NO ACTION,
	CONSTRAINT ticket_settings_ibfk_2 FOREIGN KEY ( status_email_template ) REFERENCES crm.email_template( template_id ) ON DELETE NO ACTION ON UPDATE NO ACTION,
	CONSTRAINT ticket_settings_ibfk_3 FOREIGN KEY ( subject_email_template ) REFERENCES crm.email_template( template_id ) ON DELETE NO ACTION ON UPDATE NO ACTION,
	CONSTRAINT ticket_settings_ibfk_4 FOREIGN KEY ( priority_email_template ) REFERENCES crm.email_template( template_id ) ON DELETE NO ACTION ON UPDATE NO ACTION,
	CONSTRAINT ticket_settings_ibfk_5 FOREIGN KEY ( description_email_template ) REFERENCES crm.email_template( template_id ) ON DELETE NO ACTION ON UPDATE NO ACTION,
	CONSTRAINT ticket_settings_ibfk_6 FOREIGN KEY ( customer_id ) REFERENCES crm.customer_login_info( id ) ON DELETE NO ACTION ON UPDATE NO ACTION
 ) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

CREATE INDEX user_id ON crm.ticket_settings ( user_id );

CREATE INDEX status_email_template ON crm.ticket_settings ( status_email_template );

CREATE INDEX phone_email_template ON crm.ticket_settings ( subject_email_template );

CREATE INDEX priority_email_template ON crm.ticket_settings ( priority_email_template );

CREATE INDEX description_email_template ON crm.ticket_settings ( description_email_template );

CREATE INDEX customer_id ON crm.ticket_settings ( customer_id );

CREATE  TABLE crm.trigger_lead ( 
	lead_id              INT UNSIGNED   NOT NULL AUTO_INCREMENT  PRIMARY KEY,
	customer_id          INT UNSIGNED   NOT NULL   ,
	user_id              INT       ,
	name                 VARCHAR(255)       ,
	phone                VARCHAR(20)       ,
	employee_id          INT       ,
	`status`             VARCHAR(50)       ,
	meeting_id           VARCHAR(255)       ,
	google_drive         BOOLEAN       ,
	google_drive_folder_id VARCHAR(255)       ,
	created_at           DATETIME       ,
	CONSTRAINT meeting_info UNIQUE ( meeting_id ) ,
	CONSTRAINT trigger_lead_ibfk_1 FOREIGN KEY ( customer_id ) REFERENCES crm.customer( customer_id ) ON DELETE NO ACTION ON UPDATE NO ACTION,
	CONSTRAINT trigger_lead_ibfk_2 FOREIGN KEY ( user_id ) REFERENCES crm.users( id ) ON DELETE NO ACTION ON UPDATE NO ACTION,
	CONSTRAINT trigger_lead_ibfk_3 FOREIGN KEY ( employee_id ) REFERENCES crm.users( id ) ON DELETE NO ACTION ON UPDATE NO ACTION
 ) ENGINE=InnoDB AUTO_INCREMENT=56 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

CREATE INDEX customer_id ON crm.trigger_lead ( customer_id );

CREATE INDEX user_id ON crm.trigger_lead ( user_id );

CREATE INDEX employee_id ON crm.trigger_lead ( employee_id );

CREATE  TABLE crm.trigger_ticket ( 
	ticket_id            INT UNSIGNED   NOT NULL AUTO_INCREMENT  PRIMARY KEY,
	subject              VARCHAR(255)       ,
	description          TEXT       ,
	`status`             VARCHAR(50)       ,
	priority             VARCHAR(50)       ,
	customer_id          INT UNSIGNED   NOT NULL   ,
	manager_id           INT       ,
	employee_id          INT       ,
	created_at           DATETIME       ,
	CONSTRAINT fk_ticket_customer FOREIGN KEY ( customer_id ) REFERENCES crm.customer( customer_id ) ON DELETE NO ACTION ON UPDATE NO ACTION,
	CONSTRAINT fk_ticket_employee FOREIGN KEY ( employee_id ) REFERENCES crm.users( id ) ON DELETE NO ACTION ON UPDATE NO ACTION,
	CONSTRAINT fk_ticket_manager FOREIGN KEY ( manager_id ) REFERENCES crm.users( id ) ON DELETE NO ACTION ON UPDATE NO ACTION
 ) ENGINE=InnoDB AUTO_INCREMENT=47 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

CREATE INDEX fk_ticket_customer ON crm.trigger_ticket ( customer_id );

CREATE INDEX fk_ticket_manager ON crm.trigger_ticket ( manager_id );

CREATE INDEX fk_ticket_employee ON crm.trigger_ticket ( employee_id );

CREATE  TABLE crm.user_profile ( 
	id                   INT UNSIGNED   NOT NULL AUTO_INCREMENT  PRIMARY KEY,
	first_name           VARCHAR(50)       ,
	last_name            VARCHAR(50)       ,
	phone                VARCHAR(50)       ,
	department           VARCHAR(255)       ,
	salary               DECIMAL(10,2)       ,
	`status`             VARCHAR(50)       ,
	oauth_user_image_link VARCHAR(255)       ,
	user_image           BLOB       ,
	bio                  TEXT       ,
	youtube              VARCHAR(255)       ,
	twitter              VARCHAR(255)       ,
	facebook             VARCHAR(255)       ,
	user_id              INT       ,
	country              VARCHAR(100)       ,
	position             VARCHAR(100)       ,
	address              VARCHAR(255)       ,
	CONSTRAINT user_profile_ibfk_1 FOREIGN KEY ( user_id ) REFERENCES crm.users( id ) ON DELETE NO ACTION ON UPDATE NO ACTION
 ) ENGINE=InnoDB AUTO_INCREMENT=35 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

CREATE INDEX user_id ON crm.user_profile ( user_id );

CREATE  TABLE crm.user_roles ( 
	user_id              INT    NOT NULL   ,
	role_id              INT    NOT NULL   ,
	CONSTRAINT pk_user_roles PRIMARY KEY ( user_id, role_id ),
	CONSTRAINT user_roles_ibfk_1 FOREIGN KEY ( user_id ) REFERENCES crm.users( id ) ON DELETE NO ACTION ON UPDATE NO ACTION,
	CONSTRAINT user_roles_ibfk_2 FOREIGN KEY ( role_id ) REFERENCES crm.roles( id ) ON DELETE NO ACTION ON UPDATE NO ACTION
 ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

CREATE INDEX role_id ON crm.user_roles ( role_id );

CREATE  TABLE crm.budget ( 
	budget_id            INT UNSIGNED   NOT NULL AUTO_INCREMENT  PRIMARY KEY,
	amount               NUMERIC(10,2) UNSIGNED DEFAULT (0)  NOT NULL   ,
	created_at           TIMESTAMP  DEFAULT (CURRENT_TIMESTAMP)  NOT NULL   ,
	start_date           TIMESTAMP  DEFAULT (CURRENT_TIMESTAMP)  NOT NULL   ,
	end_date             TIMESTAMP  DEFAULT (CURRENT_TIMESTAMP)     ,
	customer_id          INT UNSIGNED      ,
	description          TEXT       ,
	category_id          VARCHAR(50)       ,
	CONSTRAINT unq_budget_category_id UNIQUE ( category_id ) ,
	CONSTRAINT fk_budget_customer FOREIGN KEY ( customer_id ) REFERENCES crm.customer( customer_id ) ON DELETE NO ACTION ON UPDATE NO ACTION
 ) engine=InnoDB;

CREATE  TABLE crm.category_budget ( 
	category_id          VARCHAR(50)    NOT NULL   PRIMARY KEY,
	category_name        VARCHAR(255)    NOT NULL   ,
	CONSTRAINT unq_category_budget UNIQUE ( category_name ) ,
	CONSTRAINT fk_category_budget_budget FOREIGN KEY ( category_id ) REFERENCES crm.budget( category_id ) ON DELETE NO ACTION ON UPDATE NO ACTION
 ) engine=InnoDB;

CREATE  TABLE crm.contract_settings ( 
	id                   INT UNSIGNED   NOT NULL AUTO_INCREMENT  PRIMARY KEY,
	amount               BOOLEAN       ,
	subject              BOOLEAN       ,
	description          BOOLEAN       ,
	end_date             BOOLEAN       ,
	start_date           BOOLEAN       ,
	`status`             BOOLEAN       ,
	user_id              INT       ,
	status_email_template INT UNSIGNED      ,
	amount_email_template INT UNSIGNED      ,
	subject_email_template INT UNSIGNED      ,
	description_email_template INT UNSIGNED      ,
	start_email_template INT UNSIGNED      ,
	end_email_template   INT UNSIGNED      ,
	customer_id          INT       ,
	CONSTRAINT contract_settings_ibfk_1 FOREIGN KEY ( user_id ) REFERENCES crm.users( id ) ON DELETE NO ACTION ON UPDATE NO ACTION,
	CONSTRAINT contract_settings_ibfk_2 FOREIGN KEY ( status_email_template ) REFERENCES crm.email_template( template_id ) ON DELETE NO ACTION ON UPDATE NO ACTION,
	CONSTRAINT contract_settings_ibfk_3 FOREIGN KEY ( amount_email_template ) REFERENCES crm.email_template( template_id ) ON DELETE NO ACTION ON UPDATE NO ACTION,
	CONSTRAINT contract_settings_ibfk_4 FOREIGN KEY ( subject_email_template ) REFERENCES crm.email_template( template_id ) ON DELETE NO ACTION ON UPDATE NO ACTION,
	CONSTRAINT contract_settings_ibfk_5 FOREIGN KEY ( description_email_template ) REFERENCES crm.email_template( template_id ) ON DELETE NO ACTION ON UPDATE NO ACTION,
	CONSTRAINT contract_settings_ibfk_6 FOREIGN KEY ( start_email_template ) REFERENCES crm.email_template( template_id ) ON DELETE NO ACTION ON UPDATE NO ACTION,
	CONSTRAINT contract_settings_ibfk_7 FOREIGN KEY ( end_email_template ) REFERENCES crm.email_template( template_id ) ON DELETE NO ACTION ON UPDATE NO ACTION,
	CONSTRAINT contract_settings_ibfk_8 FOREIGN KEY ( customer_id ) REFERENCES crm.customer_login_info( id ) ON DELETE NO ACTION ON UPDATE NO ACTION
 ) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

CREATE INDEX user_id ON crm.contract_settings ( user_id );

CREATE INDEX status_email_template ON crm.contract_settings ( status_email_template );

CREATE INDEX amount_email_template ON crm.contract_settings ( amount_email_template );

CREATE INDEX subject_email_template ON crm.contract_settings ( subject_email_template );

CREATE INDEX description_email_template ON crm.contract_settings ( description_email_template );

CREATE INDEX start_email_template ON crm.contract_settings ( start_email_template );

CREATE INDEX end_email_template ON crm.contract_settings ( end_email_template );

CREATE INDEX customer_id ON crm.contract_settings ( customer_id );

CREATE  TABLE crm.expense ( 
	expense_id           INT UNSIGNED   NOT NULL AUTO_INCREMENT  PRIMARY KEY,
	amount               NUMERIC(10,2)  DEFAULT (0)  NOT NULL   ,
	expense_date         DATE  DEFAULT (CURRENT_DATE)  NOT NULL   ,
	budget_id            INT UNSIGNED      ,
	lead_id              INT UNSIGNED      ,
	ticket_id            INT UNSIGNED      ,
	description          TEXT       ,
	CONSTRAINT fk_expense_budget FOREIGN KEY ( budget_id ) REFERENCES crm.budget( budget_id ) ON DELETE NO ACTION ON UPDATE NO ACTION,
	CONSTRAINT fk_expense_trigger_lead FOREIGN KEY ( lead_id ) REFERENCES crm.trigger_lead( lead_id ) ON DELETE NO ACTION ON UPDATE NO ACTION,
	CONSTRAINT fk_expense_trigger_ticket FOREIGN KEY ( ticket_id ) REFERENCES crm.trigger_ticket( ticket_id ) ON DELETE NO ACTION ON UPDATE NO ACTION
 ) engine=InnoDB;

CREATE  TABLE crm.lead_action ( 
	id                   INT UNSIGNED   NOT NULL AUTO_INCREMENT  PRIMARY KEY,
	lead_id              INT UNSIGNED   NOT NULL   ,
	action               VARCHAR(255)       ,
	date_time            DATETIME       ,
	CONSTRAINT lead_action_ibfk_1 FOREIGN KEY ( lead_id ) REFERENCES crm.trigger_lead( lead_id ) ON DELETE NO ACTION ON UPDATE NO ACTION
 ) ENGINE=InnoDB AUTO_INCREMENT=13 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

CREATE INDEX lead_id ON crm.lead_action ( lead_id );

CREATE  TABLE crm.trigger_contract ( 
	contract_id          INT UNSIGNED   NOT NULL AUTO_INCREMENT  PRIMARY KEY,
	subject              VARCHAR(255)       ,
	`status`             VARCHAR(100)       ,
	description          TEXT       ,
	start_date           DATE       ,
	end_date             DATE       ,
	amount               DECIMAL(10,0)       ,
	google_drive         BOOLEAN       ,
	google_drive_folder_id VARCHAR(255)       ,
	lead_id              INT UNSIGNED      ,
	user_id              INT       ,
	customer_id          INT UNSIGNED      ,
	created_at           DATETIME       ,
	CONSTRAINT trigger_contract_ibfk_1 FOREIGN KEY ( lead_id ) REFERENCES crm.trigger_lead( lead_id ) ON DELETE NO ACTION ON UPDATE NO ACTION,
	CONSTRAINT trigger_contract_ibfk_2 FOREIGN KEY ( user_id ) REFERENCES crm.users( id ) ON DELETE NO ACTION ON UPDATE NO ACTION,
	CONSTRAINT trigger_contract_ibfk_3 FOREIGN KEY ( customer_id ) REFERENCES crm.customer( customer_id ) ON DELETE NO ACTION ON UPDATE NO ACTION
 ) ENGINE=InnoDB AUTO_INCREMENT=19 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

CREATE INDEX lead_id ON crm.trigger_contract ( lead_id );

CREATE INDEX user_id ON crm.trigger_contract ( user_id );

CREATE INDEX customer_id ON crm.trigger_contract ( customer_id );

CREATE  TABLE crm.file ( 
	file_id              INT    NOT NULL AUTO_INCREMENT  PRIMARY KEY,
	file_name            VARCHAR(100)       ,
	file_data            BLOB       ,
	file_type            VARCHAR(255)       ,
	lead_id              INT UNSIGNED      ,
	contract_id          INT UNSIGNED      ,
	CONSTRAINT file_ibfk_1 FOREIGN KEY ( lead_id ) REFERENCES crm.trigger_lead( lead_id ) ON DELETE NO ACTION ON UPDATE NO ACTION,
	CONSTRAINT file_ibfk_2 FOREIGN KEY ( contract_id ) REFERENCES crm.trigger_contract( contract_id ) ON DELETE NO ACTION ON UPDATE NO ACTION
 ) ENGINE=InnoDB AUTO_INCREMENT=140 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

CREATE INDEX lead_id ON crm.file ( lead_id );

CREATE INDEX contract_id ON crm.file ( contract_id );

CREATE  TABLE crm.google_drive_file ( 
	id                   INT UNSIGNED   NOT NULL AUTO_INCREMENT  PRIMARY KEY,
	drive_file_id        VARCHAR(255)       ,
	drive_folder_id      VARCHAR(255)       ,
	lead_id              INT UNSIGNED      ,
	contract_id          INT UNSIGNED      ,
	CONSTRAINT google_drive_file_ibfk_1 FOREIGN KEY ( lead_id ) REFERENCES crm.trigger_lead( lead_id ) ON DELETE NO ACTION ON UPDATE NO ACTION,
	CONSTRAINT google_drive_file_ibfk_2 FOREIGN KEY ( contract_id ) REFERENCES crm.trigger_contract( contract_id ) ON DELETE NO ACTION ON UPDATE NO ACTION
 ) ENGINE=InnoDB AUTO_INCREMENT=52 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

CREATE INDEX lead_id ON crm.google_drive_file ( lead_id );

CREATE INDEX contract_id ON crm.google_drive_file ( contract_id );
