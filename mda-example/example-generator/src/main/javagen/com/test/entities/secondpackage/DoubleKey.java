package com.test.entities.secondpackage;

import java.io.Serializable;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;

/**
 * No comment found in model diagram
 *
 * This file has been automatically generated
 */
@Entity
@Table(name="double_key")
public class DoubleKey implements Serializable{
	/** Serial ID */
	private static final long serialVersionUID = 1L;

	@EmbeddedId 
	DoubleKeyId doubleKeyId;
	private String stringData;

    /**
     * @return value of doubleKeyId
     */
	public DoubleKeyId getDoubleKeyId(){
		return doubleKeyId;
    }  
    /**
     * @param doubleKeyId new value to give to doubleKeyId
     */
	public void setDoubleKeyId(final DoubleKeyId doubleKeyId){
		this.doubleKeyId = doubleKeyId;
    }  
    /**
     * No comment found in model diagram
     * @return value of stringData
     */
    @Column(name="string_data", nullable=false)
	public String getStringData(){
		return stringData;
    }  
    /**
     * No comment found in model diagram
     * @param stringData new value to give to stringData
     */
	public void setStringData(final String stringData){
		this.stringData = stringData;
    }  

	@Override
	public int hashCode(){
	 	// Start with a non-zero constant. Prime is preferred
	    int result = 17;
	
		// Calculating hashcode with all "primitives" attributes
		result = 31 * result + (stringData == null? 0 : stringData.hashCode());
			
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
	    DoubleKey otherDoubleKey = (DoubleKey) other;
	    
		return (stringData == null ?  (otherDoubleKey.stringData == null) : stringData.equals(otherDoubleKey.stringData))
		;
	}



// END OF GENERATED CODE - YOU CAN EDIT THE FILE AFTER THIS LINE, DO NOT EDIT THIS LINE OR BEFORE THIS LINE
}