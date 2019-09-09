-- *********************************************
-- CREATE TABLES FOR ORACLE
--
-- This file has been automatically generated
-- *********************************************

-- ================================
-- SEQUENCES
-- ================================
CREATE SEQUENCE S_FUNCTION START WITH 1000 INCREMENT BY 1 NOCACHE NOCYCLE;
CREATE SEQUENCE S_FUNCTION_BODY START WITH 1000 INCREMENT BY 1 NOCACHE NOCYCLE;
CREATE SEQUENCE S_SERVICE START WITH 1000 INCREMENT BY 1 NOCACHE NOCYCLE;
CREATE SEQUENCE S_TEST_DOMAIN START WITH 1000 INCREMENT BY 1 NOCACHE NOCYCLE;
CREATE SEQUENCE S_USER START WITH 1000 INCREMENT BY 1 NOCACHE NOCYCLE;
CREATE SEQUENCE S_USER_TYPE START WITH 1000 INCREMENT BY 1 NOCACHE NOCYCLE;
CREATE SEQUENCE S_PARAMETER START WITH 1000 INCREMENT BY 1 NOCACHE NOCYCLE;
 
 
-- ================================
-- TABLES
-- ================================
CREATE TABLE function (
	function_id NUMBER(12) not null,
	function_name VARCHAR2(50) not null,
	function_body_id NUMBER(12) not null,
	CONSTRAINT PK_FUNCTION PRIMARY KEY (function_id)
);
COMMENT ON COLUMN function.function_body_id IS 'OneToOne FK function_body';

CREATE TABLE function_body (
	function_body_id NUMBER(12) not null,
	body_content VARCHAR2(50) not null,
	CONSTRAINT PK_FUNCTION_BODY PRIMARY KEY (function_body_id)
);

CREATE TABLE service (
	service_id NUMBER(12) not null,
	service_name VARCHAR2(50) not null,
	parent_service_id NUMBER(12) not null,
	CONSTRAINT PK_SERVICE PRIMARY KEY (service_id)
);
COMMENT ON COLUMN service.parent_service_id IS 'ManyToOne FK service';

CREATE TABLE test_domain (
	id NUMBER(12) not null,
	code VARCHAR2(50) not null,
	libelle VARCHAR2(255),
	a_do_date DATE,
	a_do_commentaire VARCHAR2(2000),
	a_do_fichier BLOB,
	a_do_libelle_court VARCHAR2(50),
	a_do_libelle_long VARCHAR2(250),
	a_do_mot_passe VARCHAR2(50),
	a_do_nom VARCHAR2(50),
	a_do_nombre_court NUMBER(4),
	a_do_nombre_long NUMBER(12),
	a_do_ordre_repartition VARCHAR2(50),
	a_do_oui_non NUMBER(1),
	a_do_texte_riche VARCHAR2(255),
	a_do_date_heure DATE not null,
	CONSTRAINT PK_TEST_DOMAIN PRIMARY KEY (id)
);
COMMENT ON COLUMN test_domain.id IS 'Identifiant';
COMMENT ON COLUMN test_domain.code IS 'Code';
COMMENT ON COLUMN test_domain.libelle IS 'Libellé';

CREATE TABLE user (
	user_id NUMBER(12) not null,
	user_name VARCHAR2(250) not null,
	user_surname VARCHAR2(250) not null,
	workplace_service_id NUMBER(12) not null,
	service_id NUMBER(12) not null,
	type_id NUMBER(12) not null,
	CONSTRAINT PK_USER PRIMARY KEY (user_id)
);
COMMENT ON COLUMN user.workplace_service_id IS 'ManyToOne FK service';
COMMENT ON COLUMN user.service_id IS 'ManyToOne FK service';
COMMENT ON COLUMN user.type_id IS 'ManyToOne FK user_type';

CREATE TABLE user_function_assoc (
	user_id NUMBER(12) not null,
	my_function_id NUMBER(12) not null,
	CONSTRAINT PK_USER_FUNCTION_ASSOC PRIMARY KEY (user_id,my_function_id)
);
COMMENT ON TABLE user_function_assoc IS 'ManyToMany user / function';
COMMENT ON COLUMN user_function_assoc.user_id IS 'ManyToMany FK user';
COMMENT ON COLUMN user_function_assoc.my_function_id IS 'ManyToMany FK function';

CREATE TABLE user_type (
	type_id NUMBER(12) not null,
	name VARCHAR2(250) not null,
	CONSTRAINT PK_USER_TYPE PRIMARY KEY (type_id)
);

CREATE TABLE double_key (
	pk_one NUMBER(12) not null,
	pk_two NUMBER(12) not null,
	string_data VARCHAR2(2000) not null,
	CONSTRAINT PK_DOUBLE_KEY PRIMARY KEY (pk_one, pk_two)
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
ALTER TABLE function ADD CONSTRAINT FK_function_function_body FOREIGN KEY (function_body_id) REFERENCES function_body(function_body_id);
ALTER TABLE service ADD CONSTRAINT FK_service_service_parent FOREIGN KEY (parent_service_id) REFERENCES service(service_id);
ALTER TABLE user_function_assoc ADD CONSTRAINT FK_user_function_assoc_1 FOREIGN KEY (user_id) REFERENCES user(user_id);
ALTER TABLE user_function_assoc ADD CONSTRAINT FK_user_function_assoc_2 FOREIGN KEY (my_function_id) REFERENCES function(function_id);
ALTER TABLE user ADD CONSTRAINT FK_service_workplace_id FOREIGN KEY (workplace_service_id) REFERENCES service(service_id);
ALTER TABLE user ADD CONSTRAINT FK_user_service FOREIGN KEY (service_id) REFERENCES service(service_id);
ALTER TABLE user ADD CONSTRAINT FK_user_usertype FOREIGN KEY (type_id) REFERENCES user_type(type_id);
ALTER TABLE parameter ADD CONSTRAINT FK_DOUBLE_PK FOREIGN KEY (pk_one, pk_two) REFERENCES double_key(pk_one, pk_two);


-- ================================
-- FOREIGN KEYS INDEXES
-- ================================
CREATE INDEX IDX_function_function_body ON function (function_body_id);
CREATE INDEX IDX_service_service_parent ON service (parent_service_id);
CREATE INDEX IDX_user_function_assoc_1 ON user_function_assoc (user_id);
CREATE INDEX IDX_user_function_assoc_2 ON user_function_assoc (my_function_id);
CREATE INDEX IDX_service_workplace_id ON user (workplace_service_id);
CREATE INDEX IDX_user_service ON user (service_id);
CREATE INDEX IDX_user_usertype ON user (type_id);
CREATE INDEX IDX_DOUBLE_PK ON parameter (pk_one, pk_two);

-- END OF GENERATED CODE - YOU CAN EDIT THE FILE AFTER THIS LINE, DO NOT EDIT THIS LINE OR BEFORE THIS LINE
