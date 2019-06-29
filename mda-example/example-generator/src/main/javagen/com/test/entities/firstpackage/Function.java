package com.test.entities.firstpackage;

import java.io.Serializable;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.util.Set;
import javax.persistence.FetchType;
import javax.persistence.SequenceGenerator;
import javax.persistence.ManyToMany;
import javax.persistence.GenerationType;
import javax.persistence.GeneratedValue;
import javax.persistence.OneToOne;
import javax.persistence.JoinColumn;
import javax.persistence.Column;
import javax.persistence.CascadeType;
import javax.persistence.Id;
import javax.persistence.JoinTable;

/**
 * No comment found in model diagram
 *
 * This file has been automatically generated
 */
@Entity
@Table(name="function")
public class Function implements Serializable{
	/** Serial ID */
	private static final long serialVersionUID = 1L;

	private Long functionId;
	private String functionName;
	private FunctionBody functionBody;
	private Set<User> userList;

    /**
     * No comment found in model diagram
     * @return value of functionId
     */
    @Id
    @SequenceGenerator(name="S_FUNCTION", sequenceName="S_FUNCTION", allocationSize=1)
    @GeneratedValue(strategy=GenerationType.SEQUENCE, generator="S_FUNCTION")
    @Column(name="function_id", nullable=false)
	public Long getFunctionId(){
		return functionId;
    }  
    /**
     * No comment found in model diagram
     * @param functionId new value to give to functionId
     */
	public void setFunctionId(final Long functionId){
		this.functionId = functionId;
    }  
    /**
     * No comment found in model diagram
     * @return value of functionName
     */
    @Column(name="function_name", nullable=false)
	public String getFunctionName(){
		return functionName;
    }  
    /**
     * No comment found in model diagram
     * @param functionName new value to give to functionName
     */
	public void setFunctionName(final String functionName){
		this.functionName = functionName;
    }  
    /**
     * Association function_function_body to FunctionBody
     * @return value of functionBody
     */
    @OneToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="function_body_id", referencedColumnName="function_body_id")
	public FunctionBody getFunctionBody(){
		return functionBody;
    }  
    /**
     * Association function_function_body to FunctionBody
     * @param functionBody new value to give to functionBody
     */
	public void setFunctionBody(final FunctionBody functionBody){
		this.functionBody = functionBody;
    }  
    /**
     * Association user_function_assoc to User
     * @return value of userList
     */
    @ManyToMany(cascade={CascadeType.PERSIST,CascadeType.MERGE})
    @JoinTable(name="user_function_assoc", joinColumns=@JoinColumn(name = "my_function_id"), inverseJoinColumns=@JoinColumn(name = "user_id"))
	public Set<User> getUserList(){
		return userList;
    }  
    /**
     * Association user_function_assoc to User
     * @param userList new value to give to userList
     */
	public void setUserList(final Set<User> userList){
		this.userList = userList;
    }  

	@Override
	public int hashCode(){
	 	// Start with a non-zero constant. Prime is preferred
	    int result = 17;
	
		// Calculating hashcode with all "primitives" attributes
		result = 31 * result + (functionId == null? 0 : functionId.hashCode());
		result = 31 * result + (functionName == null? 0 : functionName.hashCode());
			
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
	    Function otherFunction = (Function) other;
	    
		return (functionId == null ?  (otherFunction.functionId == null) : functionId.equals(otherFunction.functionId))
			&& (functionName == null ?  (otherFunction.functionName == null) : functionName.equals(otherFunction.functionName))
		;
	}



// END OF GENERATED CODE - YOU CAN EDIT THE FILE AFTER THIS LINE, DO NOT EDIT THIS LINE OR BEFORE THIS LINE
}