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
@Table(name="function_body")
public class FunctionBody implements Serializable{
	/** Serial ID */
	private static final long serialVersionUID = 1L;

	private Long functionBodyId;
	private String bodyContent;

    /**
     * No comment found in model diagram
     * @return value of functionBodyId
     */
    @Id
    @SequenceGenerator(name="S_FUNCTION_BODY", sequenceName="S_FUNCTION_BODY", allocationSize=1)
    @GeneratedValue(strategy=GenerationType.SEQUENCE, generator="S_FUNCTION_BODY")
    @Column(name="function_body_id", nullable=false)
	public Long getFunctionBodyId(){
		return functionBodyId;
    }  
    /**
     * No comment found in model diagram
     * @param functionBodyId new value to give to functionBodyId
     */
	public void setFunctionBodyId(final Long functionBodyId){
		this.functionBodyId = functionBodyId;
    }  
    /**
     * No comment found in model diagram
     * @return value of bodyContent
     */
    @Column(name="body_content", nullable=false, updatable=false)
	public String getBodyContent(){
		return bodyContent;
    }  
    /**
     * No comment found in model diagram
     * @param bodyContent new value to give to bodyContent
     */
	public void setBodyContent(final String bodyContent){
		this.bodyContent = bodyContent;
    }  

	@Override
	public int hashCode(){
	 	// Start with a non-zero constant. Prime is preferred
	    int result = 17;
	
		// Calculating hashcode with all "primitives" attributes
		result = 31 * result + (functionBodyId == null? 0 : functionBodyId.hashCode());
		result = 31 * result + (bodyContent == null? 0 : bodyContent.hashCode());
			
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
	    FunctionBody otherFunctionBody = (FunctionBody) other;
	    
		return (functionBodyId == null ?  (otherFunctionBody.functionBodyId == null) : functionBodyId.equals(otherFunctionBody.functionBodyId))
			&& (bodyContent == null ?  (otherFunctionBody.bodyContent == null) : bodyContent.equals(otherFunctionBody.bodyContent))
		;
	}



// END OF GENERATED CODE - YOU CAN EDIT THE FILE AFTER THIS LINE, DO NOT EDIT THIS LINE OR BEFORE THIS LINE
}