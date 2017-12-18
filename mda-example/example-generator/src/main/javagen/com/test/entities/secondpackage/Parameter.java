package com.test.entities.secondpackage;

import javax.persistence.GeneratedValue;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.JoinColumn;
import javax.persistence.Column;
import javax.persistence.SequenceGenerator;
import com.test.entities.secondpackage.DoubleKey;
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
public class Parameter {
	private Long paramId;
	private Integer version;
	private DoubleKey doubleKey;

    /**
     * Identifiant de la ligne de parametrage
     * @return value of paramId
     */
    @Id
    @SequenceGenerator(name="SEQ_PARAMETER", sequenceName="SEQ_PARAMETER", allocationSize=20)
    @GeneratedValue(strategy=GenerationType.SEQUENCE, generator="SEQ_PARAMETER")
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
    @ManyToOne
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
	
		result = 31 * result + (paramId == null? 0 : paramId.hashCode());
		result = 31 * result + (version == null? 0 : version.hashCode());
		result = 31 * result + (doubleKey == null? 0 : doubleKey.hashCode());
			
		return result;
	}

	@Override
	public boolean equals(Object other){
		// Same object
	    if (this == other) {
	        return true;
	    }
	
		// Wrong type
	    if (!(other instanceof Parameter)) {
	        return false;
	    }
	
		// Test all attributes
	    Parameter otherParameter = (Parameter) other;
	    
		return (paramId == null ? paramId == null:paramId.equals(otherParameter.paramId))
			&& (version == null ? version == null:version.equals(otherParameter.version))
			&& (doubleKey == null ? doubleKey == null:doubleKey.equals(otherParameter.doubleKey))
		;
	}



// END OF GENERATED CODE - YOU CAN EDIT THE FILE AFTER THIS LINE, DO NOT EDIT THIS LINE OR BEFORE THIS LINE
}