package com.test.entities.secondpackage;

import java.io.Serializable;
import javax.persistence.GeneratedValue;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.JoinColumn;
import javax.persistence.Column;
import javax.persistence.FetchType;
import javax.persistence.SequenceGenerator;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.GenerationType;

/**
 * Contient la version de l'application et d'autres paramètres technique non modifiables.
 * 
 * Cette table ne doit contenir qu'une seule ligne
 *
 * This file has been automatically generated
 */
@Entity
@Table(name="parameter")
public class Parameter implements Serializable{
	/** Serial ID */
	private static final long serialVersionUID = 1L;

	private Long paramId;
	private Integer version;
	private DoubleKey doubleKey;

    /**
     * Identifiant de la ligne de parametrage
     * @return value of paramId
     */
    @Id
    @SequenceGenerator(name="S_PARAMETER", sequenceName="S_PARAMETER", allocationSize=1)
    @GeneratedValue(strategy=GenerationType.SEQUENCE, generator="S_PARAMETER")
    @Column(name="param_id", nullable=false)
	public Long getParamId(){
		return paramId;
    }  
    /**
     * Identifiant de la ligne de parametrage
     * @param paramId new value to give to paramId
     */
	public void setParamId(final Long paramId){
		this.paramId = paramId;
    }  
    /**
     * Version de l'application
     * @return value of version
     */
    @Column(name="version", nullable=false)
	public Integer getVersion(){
		return version;
    }  
    /**
     * Version de l'application
     * @param version new value to give to version
     */
	public void setVersion(final Integer version){
		this.version = version;
    }  
    /**
     * Association DOUBLE_PK to DoubleKey
     * @return value of doubleKey
     */
    @ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="double_keyId", referencedColumnName="double_keyId")
	public DoubleKey getDoubleKey(){
		return doubleKey;
    }  
    /**
     * Association DOUBLE_PK to DoubleKey
     * @param doubleKey new value to give to doubleKey
     */
	public void setDoubleKey(final DoubleKey doubleKey){
		this.doubleKey = doubleKey;
    }  

	@Override
	public int hashCode(){
	 	// Start with a non-zero constant. Prime is preferred
	    int result = 17;
	
		// Calculating hashcode with all "primitives" attributes
		result = 31 * result + (paramId == null? 0 : paramId.hashCode());
		result = 31 * result + (version == null? 0 : version.hashCode());
			
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
	    Parameter otherParameter = (Parameter) other;
	    
		return (paramId == null ?  (otherParameter.paramId == null) : paramId.equals(otherParameter.paramId))
			&& (version == null ?  (otherParameter.version == null) : version.equals(otherParameter.version))
		;
	}



// END OF GENERATED CODE - YOU CAN EDIT THE FILE AFTER THIS LINE, DO NOT EDIT THIS LINE OR BEFORE THIS LINE
}