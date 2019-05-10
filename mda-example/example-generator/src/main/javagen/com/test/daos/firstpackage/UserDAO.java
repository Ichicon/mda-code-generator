package com.test.daos.firstpackage;

import org.springframework.data.repository.CrudRepository;
import com.test.entities.firstpackage.User;

/**
 * Data Access Object for User using Spring CrudRepository interface
 *
 * This file has been automatically generated
 */
public interface UserDAO extends CrudRepository<User,  Long> {

}
// STOP GENERATION -> Comment used to prevent generator from generate the file again, DO NOT REMOVE IT