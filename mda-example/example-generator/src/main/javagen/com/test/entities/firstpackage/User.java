package com.test.entities.firstpackage;

import java.io.Serializable;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.util.Set;
import javax.persistence.FetchType;
import javax.persistence.SequenceGenerator;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.GenerationType;
import javax.persistence.GeneratedValue;
import javax.persistence.JoinColumn;
import javax.persistence.Column;
import javax.persistence.CascadeType;
import javax.persistence.Id;

/**
 * No comment found in model diagram
 *
 * This file has been automatically generated
 */
@Entity
@Table(name="user")
public class User implements Serializable{
	/** Serial ID */
	private static final long serialVersionUID = 1L;

	private Long userId;
	private String userName;
	private String userSurname;
	private Set<Function> myFunctionList;
	private Service workplaceService;
	private Service userService;
	private UserType userType;

    /**
     * No comment found in model diagram
     * @return value of userId
     */
    @Id
    @SequenceGenerator(name="S_USER", sequenceName="S_USER", allocationSize=1)
    @GeneratedValue(strategy=GenerationType.SEQUENCE, generator="S_USER")
    @Column(name="user_id", nullable=false)
	public Long getUserId(){
		return userId;
    }  
    /**
     * No comment found in model diagram
     * @param userId new value to give to userId
     */
	public void setUserId(final Long userId){
		this.userId = userId;
    }  
    /**
     * No comment found in model diagram
     * @return value of userName
     */
    @Column(name="user_name", nullable=false)
	public String getUserName(){
		return userName;
    }  
    /**
     * No comment found in model diagram
     * @param userName new value to give to userName
     */
	public void setUserName(final String userName){
		this.userName = userName;
    }  
    /**
     * No comment found in model diagram
     * @return value of userSurname
     */
    @Column(name="user_surname", nullable=false)
	public String getUserSurname(){
		return userSurname;
    }  
    /**
     * No comment found in model diagram
     * @param userSurname new value to give to userSurname
     */
	public void setUserSurname(final String userSurname){
		this.userSurname = userSurname;
    }  
    /**
     * Association user_function_assoc to Function
     * @return value of myFunctionList
     */
    @ManyToMany(mappedBy="userList", cascade={CascadeType.PERSIST,CascadeType.MERGE})
	public Set<Function> getMyFunctionList(){
		return myFunctionList;
    }  
    /**
     * Association user_function_assoc to Function
     * @param myFunctionList new value to give to myFunctionList
     */
	public void setMyFunctionList(final Set<Function> myFunctionList){
		this.myFunctionList = myFunctionList;
    }  
    /**
     * Association service_workplace_id to Service
     * @return value of workplaceService
     */
    @ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="workplace_service_id", referencedColumnName="service_id")
	public Service getWorkplaceService(){
		return workplaceService;
    }  
    /**
     * Association service_workplace_id to Service
     * @param workplaceService new value to give to workplaceService
     */
	public void setWorkplaceService(final Service workplaceService){
		this.workplaceService = workplaceService;
    }  
    /**
     * Association user_service to Service
     * @return value of userService
     */
    @ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="service_id", referencedColumnName="service_id")
	public Service getUserService(){
		return userService;
    }  
    /**
     * Association user_service to Service
     * @param userService new value to give to userService
     */
	public void setUserService(final Service userService){
		this.userService = userService;
    }  
    /**
     * Association user_usertype to UserType
     * @return value of userType
     */
    @ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="type_id", referencedColumnName="type_id")
	public UserType getUserType(){
		return userType;
    }  
    /**
     * Association user_usertype to UserType
     * @param userType new value to give to userType
     */
	public void setUserType(final UserType userType){
		this.userType = userType;
    }  

	@Override
	public int hashCode(){
	 	// Start with a non-zero constant. Prime is preferred
	    int result = 17;
	
		// Calculating hashcode with all "primitives" attributes
		result = 31 * result + (userId == null? 0 : userId.hashCode());
		result = 31 * result + (userName == null? 0 : userName.hashCode());
		result = 31 * result + (userSurname == null? 0 : userSurname.hashCode());
			
		return result;
	}

	@Override
	public boolean equals(Object other){
		// Null object
	    if(other == null){
	    	return false;
	    }
	
		// Same object
	    if (this == other) {
	        return true;
	    }
	    	
		// Wrong type
	    if (this.getClass() !=  other.getClass()) {
	        return false;
	    }
	
		// Test all "primitives" attributes
	    User otherUser = (User) other;
	    
		return (userId == null ?  (otherUser.userId == null) : userId.equals(otherUser.userId))
			&& (userName == null ?  (otherUser.userName == null) : userName.equals(otherUser.userName))
			&& (userSurname == null ?  (otherUser.userSurname == null) : userSurname.equals(otherUser.userSurname))
		;
	}



// END OF GENERATED CODE - YOU CAN EDIT THE FILE AFTER THIS LINE, DO NOT EDIT THIS LINE OR BEFORE THIS LINE
}