package com.test.entities.secondpackage;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Embeddable;

/**
 * Composite Key for DoubleKey
 *
 * This file has been automatically generated
 */
@Embeddable
public class DoubleKeyId implements Serializable{
	/** Serial ID */
	private static final long serialVersionUID = 1L;

	private Long pkOne;
	private Long pkTwo;

    /**
     * No comment found in model diagram
     * @return value of pkOne
     */
    @Column(name="pk_one", nullable=false)
	public Long getPkOne(){
		return pkOne;
    }  
    /**
     * No comment found in model diagram
     * @param pkOne new value to give to pkOne
     */
	public void setPkOne(final Long pkOne){
		this.pkOne = pkOne;
    }  
    /**
     * No comment found in model diagram
     * @return value of pkTwo
     */
    @Column(name="pk_two", nullable=false)
	public Long getPkTwo(){
		return pkTwo;
    }  
    /**
     * No comment found in model diagram
     * @param pkTwo new value to give to pkTwo
     */
	public void setPkTwo(final Long pkTwo){
		this.pkTwo = pkTwo;
    }  

	@Override
	public int hashCode(){
	 	// Start with a non-zero constant. Prime is preferred
	    int result = 17;
	
		// Calculating hashcode with all "primitives" attributes
		result = 31 * result + (pkOne == null? 0 : pkOne.hashCode());
		result = 31 * result + (pkTwo == null? 0 : pkTwo.hashCode());
			
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
	    DoubleKeyId otherDoubleKeyId = (DoubleKeyId) other;
	    
		return (pkOne == null ?  (otherDoubleKeyId.pkOne == null) : pkOne.equals(otherDoubleKeyId.pkOne))
			&& (pkTwo == null ?  (otherDoubleKeyId.pkTwo == null) : pkTwo.equals(otherDoubleKeyId.pkTwo))
		;
	}



// END OF GENERATED CODE - YOU CAN EDIT THE FILE AFTER THIS LINE, DO NOT EDIT THIS LINE OR BEFORE THIS LINE
}