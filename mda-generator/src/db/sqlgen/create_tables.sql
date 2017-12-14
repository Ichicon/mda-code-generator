-- *********************************************
-- CREATE TABLES FOR ORACLE
--
-- This file has been automatically generated
-- *********************************************

-- ================================
-- SEQUENCES
-- ================================
CREATE SEQUENCE SEQ_FUNCTION START WITH 1000 INCREMENT BY 1 NOCACHE NOCYCLE;
CREATE SEQUENCE SEQ_SERVICE START WITH 1000 INCREMENT BY 1 NOCACHE NOCYCLE;
CREATE SEQUENCE SEQ_APPUSER START WITH 1000 INCREMENT BY 1 NOCACHE NOCYCLE;
CREATE SEQUENCE SEQ_USER_TYPE START WITH 1000 INCREMENT BY 1 NOCACHE NOCYCLE;
CREATE SEQUENCE SEQ_PARAMETER START WITH 1000 INCREMENT BY 1 NOCACHE NOCYCLE;
 
 
-- ================================
-- TABLES
-- ================================
CREATE TABLE function (
	function_id NUMBER(12) not null,
	function_name VARCHAR2(50) not null,
	CONSTRAINT PK_FUNCTION PRIMARY KEY (function_id)
);

CREATE TABLE service (
	service_id NUMBER(12) not null,
	service_name VARCHAR2(50) not null,
	parent_service_id NUMBER(12) not null,
	CONSTRAINT PK_SERVICE PRIMARY KEY (service_id)
);
COMMENT ON COLUMN service.parent_service_id IS 'ManyToOne FK service';

CREATE TABLE appuser (
	user_id NUMBER(12) not null,
	user_name VARCHAR2(250) not null,
	user_surname VARCHAR2(250) not null,
	workplace_service_id NUMBER(12) not null,
	service_id NUMBER(12) not null,
	type_id NUMBER(12) not null,
	CONSTRAINT PK_APPUSER PRIMARY KEY (user_id)
);
COMMENT ON COLUMN appuser.workplace_service_id IS 'ManyToOne FK service';
COMMENT ON COLUMN appuser.service_id IS 'ManyToOne FK service';
COMMENT ON COLUMN appuser.type_id IS 'ManyToOne FK user_type';

CREATE TABLE user_function_assoc (
	user_id NUMBER(12) not null,
	my_function_id NUMBER(12) not null,
	CONSTRAINT PK_USER_FUNCTION_ASSOC PRIMARY KEY (user_id,my_function_id)
);
COMMENT ON TABLE user_function_assoc IS 'ManyToMany appuser / function';
COMMENT ON COLUMN user_function_assoc.user_id IS 'ManyToMany FK appuser';
COMMENT ON COLUMN user_function_assoc.my_function_id IS 'ManyToMany FK function';

CREATE TABLE user_type (
	type_id NUMBER(12) not null,
	name VARCHAR2(250) not null,
	CONSTRAINT PK_USER_TYPE PRIMARY KEY (type_id)
);

CREATE TABLE parameter (
	param_id NUMBER(12) not null,
	version NUMBER(4) not null,
	pk_one NUMBER(12) not null,
	pk_two NUMBER(12) not null,
	CONSTRAINT PK_PARAMETER PRIMARY KEY (param_id)
);
COMMENT ON TABLE parameter IS 'Contient la version de l''application et d''autres paramètres technique non modifiables.

Cette table ne doit contenir qu''une seule ligne';
COMMENT ON COLUMN parameter.param_id IS 'Identifiant de la ligne de parametrage';
COMMENT ON COLUMN parameter.version IS 'Version de l''application';
COMMENT ON COLUMN parameter.pk_one IS 'ManyToOne FK double_key';
COMMENT ON COLUMN parameter.pk_two IS 'ManyToOne FK double_key';



-- ================================
-- FOREIGN KEYS
-- ================================
ALTER TABLE service ADD CONSTRAINT FK_service_service_parent FOREIGN KEY (parent_service_id) REFERENCES service(service_id);
ALTER TABLE user_function_assoc ADD CONSTRAINT FK_user_function_assoc_1 FOREIGN KEY (user_id) REFERENCES appuser(user_id);
ALTER TABLE user_function_assoc ADD CONSTRAINT FK_user_function_assoc_2 FOREIGN KEY (my_function_id) REFERENCES function(my_function_id);
ALTER TABLE appuser ADD CONSTRAINT FK_service_workplace_id FOREIGN KEY (workplace_service_id) REFERENCES service(service_id);
ALTER TABLE appuser ADD CONSTRAINT FK_user_service FOREIGN KEY (service_id) REFERENCES service(service_id);
ALTER TABLE appuser ADD CONSTRAINT FK_user_usertype FOREIGN KEY (type_id) REFERENCES user_type(type_id);
ALTER TABLE parameter ADD CONSTRAINT FK_DOUBLE_PK FOREIGN KEY (pk_one, pk_two) REFERENCES double_key(pk_one, pk_two);


-- ================================
-- FOREIGN KEYS INDEXES
-- ================================
CREATE INDEX IDX_service_service_parent ON service (parent_service_id);
CREATE INDEX IDX_user_function_assoc_1 ON user_function_assoc (user_id);
CREATE INDEX IDX_user_function_assoc_2 ON user_function_assoc (my_function_id);
CREATE INDEX IDX_service_workplace_id ON appuser (workplace_service_id);
CREATE INDEX IDX_user_service ON appuser (service_id);
CREATE INDEX IDX_user_usertype ON appuser (type_id);
CREATE INDEX IDX_DOUBLE_PK ON parameter (pk_one, pk_two);

-- END OF GENERATED CODE - YOU CAN EDIT THE FILE AFTER THIS LINE, DO NOT EDIT THIS LINE OR BEFORE THIS LINE
