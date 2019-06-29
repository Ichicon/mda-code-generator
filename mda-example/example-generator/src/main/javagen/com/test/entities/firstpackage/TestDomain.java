package com.test.entities.firstpackage;

import java.io.Serializable;
import javax.persistence.GeneratedValue;
import java.time.LocalDateTime;
import javax.persistence.Entity;
import java.sql.Blob;
import javax.persistence.Table;
import javax.persistence.Column;
import javax.persistence.SequenceGenerator;
import javax.persistence.Id;
import java.time.LocalDate;
import javax.persistence.GenerationType;

/**
 * No comment found in model diagram
 *
 * This file has been automatically generated
 */
@Entity
@Table(name="test_domain")
public class TestDomain implements Serializable{
	/** Serial ID */
	private static final long serialVersionUID = 1L;

	private Long id;
	private String code;
	private String libelle;
	private LocalDate aDoDate;
	private String aDoCommentaire;
	private Blob aDoFichier;
	private String aDoLibelleCourt;
	private String aDoLibelleLong;
	private String aDoMotPasse;
	private String aDoNom;
	private Integer aDoNombreCourt;
	private Long aDoNombreLong;
	private String aDoOrdreRepartition;
	private Boolean aDoOuiNon;
	private String aDoTexteRiche;
	private LocalDateTime aDoDateHeure;

    /**
     * Identifiant
     * @return value of id
     */
    @Id
    @SequenceGenerator(name="S_TEST_DOMAIN", sequenceName="S_TEST_DOMAIN", allocationSize=1)
    @GeneratedValue(strategy=GenerationType.SEQUENCE, generator="S_TEST_DOMAIN")
    @Column(name="id", nullable=false)
	public Long getId(){
		return id;
    }  
    /**
     * Identifiant
     * @param id new value to give to id
     */
	public void setId(final Long id){
		this.id = id;
    }  
    /**
     * Code
     * @return value of code
     */
    @Column(name="code", nullable=false)
	public String getCode(){
		return code;
    }  
    /**
     * Code
     * @param code new value to give to code
     */
	public void setCode(final String code){
		this.code = code;
    }  
    /**
     * Libellé
     * @return value of libelle
     */
    @Column(name="libelle", nullable=true, updatable=false)
	public String getLibelle(){
		return libelle;
    }  
    /**
     * Libellé
     * @param libelle new value to give to libelle
     */
	public void setLibelle(final String libelle){
		this.libelle = libelle;
    }  
    /**
     * No comment found in model diagram
     * @return value of aDoDate
     */
    @Column(name="a_do_date", nullable=true, updatable=false)
	public LocalDate getADoDate(){
		return aDoDate;
    }  
    /**
     * No comment found in model diagram
     * @param aDoDate new value to give to aDoDate
     */
	public void setADoDate(final LocalDate aDoDate){
		this.aDoDate = aDoDate;
    }  
    /**
     * No comment found in model diagram
     * @return value of aDoCommentaire
     */
    @Column(name="a_do_commentaire", nullable=true, updatable=false)
	public String getADoCommentaire(){
		return aDoCommentaire;
    }  
    /**
     * No comment found in model diagram
     * @param aDoCommentaire new value to give to aDoCommentaire
     */
	public void setADoCommentaire(final String aDoCommentaire){
		this.aDoCommentaire = aDoCommentaire;
    }  
    /**
     * No comment found in model diagram
     * @return value of aDoFichier
     */
    @Column(name="a_do_fichier", nullable=true, updatable=false)
	public Blob getADoFichier(){
		return aDoFichier;
    }  
    /**
     * No comment found in model diagram
     * @param aDoFichier new value to give to aDoFichier
     */
	public void setADoFichier(final Blob aDoFichier){
		this.aDoFichier = aDoFichier;
    }  
    /**
     * No comment found in model diagram
     * @return value of aDoLibelleCourt
     */
    @Column(name="a_do_libelle_court", nullable=true, updatable=false)
	public String getADoLibelleCourt(){
		return aDoLibelleCourt;
    }  
    /**
     * No comment found in model diagram
     * @param aDoLibelleCourt new value to give to aDoLibelleCourt
     */
	public void setADoLibelleCourt(final String aDoLibelleCourt){
		this.aDoLibelleCourt = aDoLibelleCourt;
    }  
    /**
     * No comment found in model diagram
     * @return value of aDoLibelleLong
     */
    @Column(name="a_do_libelle_long", nullable=true, updatable=false)
	public String getADoLibelleLong(){
		return aDoLibelleLong;
    }  
    /**
     * No comment found in model diagram
     * @param aDoLibelleLong new value to give to aDoLibelleLong
     */
	public void setADoLibelleLong(final String aDoLibelleLong){
		this.aDoLibelleLong = aDoLibelleLong;
    }  
    /**
     * No comment found in model diagram
     * @return value of aDoMotPasse
     */
    @Column(name="a_do_mot_passe", nullable=true, updatable=false)
	public String getADoMotPasse(){
		return aDoMotPasse;
    }  
    /**
     * No comment found in model diagram
     * @param aDoMotPasse new value to give to aDoMotPasse
     */
	public void setADoMotPasse(final String aDoMotPasse){
		this.aDoMotPasse = aDoMotPasse;
    }  
    /**
     * No comment found in model diagram
     * @return value of aDoNom
     */
    @Column(name="a_do_nom", nullable=true, updatable=false)
	public String getADoNom(){
		return aDoNom;
    }  
    /**
     * No comment found in model diagram
     * @param aDoNom new value to give to aDoNom
     */
	public void setADoNom(final String aDoNom){
		this.aDoNom = aDoNom;
    }  
    /**
     * No comment found in model diagram
     * @return value of aDoNombreCourt
     */
    @Column(name="a_do_nombre_court", nullable=true, updatable=false)
	public Integer getADoNombreCourt(){
		return aDoNombreCourt;
    }  
    /**
     * No comment found in model diagram
     * @param aDoNombreCourt new value to give to aDoNombreCourt
     */
	public void setADoNombreCourt(final Integer aDoNombreCourt){
		this.aDoNombreCourt = aDoNombreCourt;
    }  
    /**
     * No comment found in model diagram
     * @return value of aDoNombreLong
     */
    @Column(name="a_do_nombre_long", nullable=true, updatable=false)
	public Long getADoNombreLong(){
		return aDoNombreLong;
    }  
    /**
     * No comment found in model diagram
     * @param aDoNombreLong new value to give to aDoNombreLong
     */
	public void setADoNombreLong(final Long aDoNombreLong){
		this.aDoNombreLong = aDoNombreLong;
    }  
    /**
     * No comment found in model diagram
     * @return value of aDoOrdreRepartition
     */
    @Column(name="a_do_ordre_repartition", nullable=true, updatable=false)
	public String getADoOrdreRepartition(){
		return aDoOrdreRepartition;
    }  
    /**
     * No comment found in model diagram
     * @param aDoOrdreRepartition new value to give to aDoOrdreRepartition
     */
	public void setADoOrdreRepartition(final String aDoOrdreRepartition){
		this.aDoOrdreRepartition = aDoOrdreRepartition;
    }  
    /**
     * No comment found in model diagram
     * @return value of aDoOuiNon
     */
    @Column(name="a_do_oui_non", nullable=true, updatable=false)
	public Boolean getADoOuiNon(){
		return aDoOuiNon;
    }  
    /**
     * No comment found in model diagram
     * @param aDoOuiNon new value to give to aDoOuiNon
     */
	public void setADoOuiNon(final Boolean aDoOuiNon){
		this.aDoOuiNon = aDoOuiNon;
    }  
    /**
     * No comment found in model diagram
     * @return value of aDoTexteRiche
     */
    @Column(name="a_do_texte_riche", nullable=true, updatable=false)
	public String getADoTexteRiche(){
		return aDoTexteRiche;
    }  
    /**
     * No comment found in model diagram
     * @param aDoTexteRiche new value to give to aDoTexteRiche
     */
	public void setADoTexteRiche(final String aDoTexteRiche){
		this.aDoTexteRiche = aDoTexteRiche;
    }  
    /**
     * No comment found in model diagram
     * @return value of aDoDateHeure
     */
    @Column(name="a_do_date_heure", nullable=false, updatable=false)
	public LocalDateTime getADoDateHeure(){
		return aDoDateHeure;
    }  
    /**
     * No comment found in model diagram
     * @param aDoDateHeure new value to give to aDoDateHeure
     */
	public void setADoDateHeure(final LocalDateTime aDoDateHeure){
		this.aDoDateHeure = aDoDateHeure;
    }  

	@Override
	public int hashCode(){
	 	// Start with a non-zero constant. Prime is preferred
	    int result = 17;
	
		// Calculating hashcode with all "primitives" attributes
		result = 31 * result + (id == null? 0 : id.hashCode());
		result = 31 * result + (code == null? 0 : code.hashCode());
		result = 31 * result + (libelle == null? 0 : libelle.hashCode());
		result = 31 * result + (aDoDate == null? 0 : aDoDate.hashCode());
		result = 31 * result + (aDoCommentaire == null? 0 : aDoCommentaire.hashCode());
		result = 31 * result + (aDoFichier == null? 0 : aDoFichier.hashCode());
		result = 31 * result + (aDoLibelleCourt == null? 0 : aDoLibelleCourt.hashCode());
		result = 31 * result + (aDoLibelleLong == null? 0 : aDoLibelleLong.hashCode());
		result = 31 * result + (aDoMotPasse == null? 0 : aDoMotPasse.hashCode());
		result = 31 * result + (aDoNom == null? 0 : aDoNom.hashCode());
		result = 31 * result + (aDoNombreCourt == null? 0 : aDoNombreCourt.hashCode());
		result = 31 * result + (aDoNombreLong == null? 0 : aDoNombreLong.hashCode());
		result = 31 * result + (aDoOrdreRepartition == null? 0 : aDoOrdreRepartition.hashCode());
		result = 31 * result + (aDoOuiNon == null? 0 : aDoOuiNon.hashCode());
		result = 31 * result + (aDoTexteRiche == null? 0 : aDoTexteRiche.hashCode());
		result = 31 * result + (aDoDateHeure == null? 0 : aDoDateHeure.hashCode());
			
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
	    TestDomain otherTestDomain = (TestDomain) other;
	    
		return (id == null ?  (otherTestDomain.id == null) : id.equals(otherTestDomain.id))
			&& (code == null ?  (otherTestDomain.code == null) : code.equals(otherTestDomain.code))
			&& (libelle == null ?  (otherTestDomain.libelle == null) : libelle.equals(otherTestDomain.libelle))
			&& (aDoDate == null ?  (otherTestDomain.aDoDate == null) : aDoDate.equals(otherTestDomain.aDoDate))
			&& (aDoCommentaire == null ?  (otherTestDomain.aDoCommentaire == null) : aDoCommentaire.equals(otherTestDomain.aDoCommentaire))
			&& (aDoFichier == null ?  (otherTestDomain.aDoFichier == null) : aDoFichier.equals(otherTestDomain.aDoFichier))
			&& (aDoLibelleCourt == null ?  (otherTestDomain.aDoLibelleCourt == null) : aDoLibelleCourt.equals(otherTestDomain.aDoLibelleCourt))
			&& (aDoLibelleLong == null ?  (otherTestDomain.aDoLibelleLong == null) : aDoLibelleLong.equals(otherTestDomain.aDoLibelleLong))
			&& (aDoMotPasse == null ?  (otherTestDomain.aDoMotPasse == null) : aDoMotPasse.equals(otherTestDomain.aDoMotPasse))
			&& (aDoNom == null ?  (otherTestDomain.aDoNom == null) : aDoNom.equals(otherTestDomain.aDoNom))
			&& (aDoNombreCourt == null ?  (otherTestDomain.aDoNombreCourt == null) : aDoNombreCourt.equals(otherTestDomain.aDoNombreCourt))
			&& (aDoNombreLong == null ?  (otherTestDomain.aDoNombreLong == null) : aDoNombreLong.equals(otherTestDomain.aDoNombreLong))
			&& (aDoOrdreRepartition == null ?  (otherTestDomain.aDoOrdreRepartition == null) : aDoOrdreRepartition.equals(otherTestDomain.aDoOrdreRepartition))
			&& (aDoOuiNon == null ?  (otherTestDomain.aDoOuiNon == null) : aDoOuiNon.equals(otherTestDomain.aDoOuiNon))
			&& (aDoTexteRiche == null ?  (otherTestDomain.aDoTexteRiche == null) : aDoTexteRiche.equals(otherTestDomain.aDoTexteRiche))
			&& (aDoDateHeure == null ?  (otherTestDomain.aDoDateHeure == null) : aDoDateHeure.equals(otherTestDomain.aDoDateHeure))
		;
	}



// END OF GENERATED CODE - YOU CAN EDIT THE FILE AFTER THIS LINE, DO NOT EDIT THIS LINE OR BEFORE THIS LINE
}