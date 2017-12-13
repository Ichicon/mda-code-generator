-- *********************************************
-- DROP TABLES FOR ORACLE
--
-- This file has been automatically generated
-- *********************************************

-- ================================
-- DROP SEQUENCE
-- ================================
DROP SEQUENCE SEQ_FUNCTION;
DROP SEQUENCE SEQ_SERVICE;
DROP SEQUENCE SEQ_APPUSER;
DROP SEQUENCE SEQ_USER_TYPE;
DROP SEQUENCE SEQ_PARAMETER;
 
-- ================================
-- DROP FOREIGN KEY
-- ================================
ALTER TABLE service DROP CONSTRAINT FK_service_service_parent;
ALTER TABLE user_function_assoc DROP CONSTRAINT FK_user_function_assoc_1;
ALTER TABLE user_function_assoc DROP CONSTRAINT FK_user_function_assoc_2;
ALTER TABLE appuser DROP CONSTRAINT FK_service_workplace_id;
ALTER TABLE appuser DROP CONSTRAINT FK_user_service;
ALTER TABLE appuser DROP CONSTRAINT FK_user_usertype;
ALTER TABLE parameter DROP CONSTRAINT FK_DOUBLE_PK;
 
-- ================================
-- DROP FOREIGN KEYS INDEXES
-- ================================
DROP INDEX IDX_service_service_parent;
DROP INDEX IDX_user_function_assoc_1;
DROP INDEX IDX_user_function_assoc_2;
DROP INDEX IDX_service_workplace_id;
DROP INDEX IDX_user_service;
DROP INDEX IDX_user_usertype;
DROP INDEX IDX_DOUBLE_PK;
 
-- ================================
-- DROP TABLES
-- ================================
DROP TABLE function;
DROP TABLE service;
DROP TABLE appuser;
DROP TABLE user_function_assoc;
DROP TABLE user_type;
DROP TABLE parameter;

-- END OF GENERATED CODE - YOU CAN EDIT THE FILE AFTER THIS LINE, DO NOT EDIT THIS LINE OR BEFORE THIS LINE
