package com.test.entities.firstpackage;

import java.io.Serializable;
import javax.persistence.GeneratedValue;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Column;
import javax.persistence.SequenceGenerator;
import javax.persistence.Id;
import javax.persistence.GenerationType;

/**
 * No comment found in model diagram
 *
 * This file has been automatically generated
 */
@Entity
@Table(name="user_type")
@org.hibernate.annotations.Cache(usage = org.hibernate.annotations.CacheConcurrencyStrategy.READ_ONLY)
public class UserType implements Serializable{
	/** Serial ID */
	private static final long serialVersionUID = 1L;

	private Long typeId;
	private String name;

    /**
     * No comment found in model diagram
     * @return value of typeId
     */
    @Id
    @SequenceGenerator(name="SEQ_USER_TYPE", sequenceName="SEQ_USER_TYPE", allocationSize=1)
    @GeneratedValue(strategy=GenerationType.SEQUENCE, generator="SEQ_USER_TYPE")
    @Column(name="type_id", nullable=false)
	public Long getTypeId(){
		return typeId;
    }  
    /**
     * No comment found in model diagram
     * @param typeId new value to give to typeId
     */
	public void setTypeId(final Long typeId){
		this.typeId = typeId;
    }  
    /**
     * No comment found in model diagram
     * @return value of name
     */
    @Column(name="name", nullable=false)
	public String getName(){
		return name;
    }  
    /**
     * No comment found in model diagram
     * @param name new value to give to name
     */
	public void setName(final String name){
		this.name = name;
    }  

	@Override
	public int hashCode(){
	 	// Start with a non-zero constant. Prime is preferred
	    int result = 17;
	
		// Calculating hashcode with all "primitives" attributes
		result = 31 * result + (typeId == null? 0 : typeId.hashCode());
		result = 31 * result + (name == null? 0 : name.hashCode());
			
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
	    UserType otherUserType = (UserType) other;
	    
		return (typeId == null ?  (otherUserType.typeId == null) : typeId.equals(otherUserType.typeId))
			&& (name == null ?  (otherUserType.name == null) : name.equals(otherUserType.name))
		;
	}



// END OF GENERATED CODE - YOU CAN EDIT THE FILE AFTER THIS LINE, DO NOT EDIT THIS LINE OR BEFORE THIS LINE
}