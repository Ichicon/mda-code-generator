package com.test.entities.firstpackage;

import java.io.Serializable;
import javax.persistence.GeneratedValue;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.util.Set;
import javax.persistence.Column;
import javax.persistence.SequenceGenerator;
import javax.persistence.CascadeType;
import javax.persistence.ManyToMany;
import javax.persistence.Id;
import javax.persistence.GenerationType;

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
	private Set<Appuser> appuserList;

    /**
     * No comment found in model diagram
     * @return value of functionId
     */
    @Id
    @SequenceGenerator(name="SEQ_FUNCTION", sequenceName="SEQ_FUNCTION", allocationSize=1)
    @GeneratedValue(strategy=GenerationType.SEQUENCE, generator="SEQ_FUNCTION")
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
     * Association user_function_assoc to Appuser
     * @return value of appuserList
     */
    @ManyToMany(mappedBy="myFunctionList", cascade={CascadeType.PERSIST,CascadeType.MERGE})
	public Set<Appuser> getAppuserList(){
		return appuserList;
    }  
    /**
     * Association user_function_assoc to Appuser
     * @param appuserList new value to give to appuserList
     */
	public void setAppuserList(final Set<Appuser> appuserList){
		this.appuserList = appuserList;
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