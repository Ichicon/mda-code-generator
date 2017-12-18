package com.test.entities.firstpackage;

import javax.persistence.Entity;
import javax.persistence.Table;
import java.util.Set;
import javax.persistence.SequenceGenerator;
import com.test.entities.firstpackage.Service;
import javax.persistence.OneToMany;
import javax.persistence.ManyToOne;
import javax.persistence.GenerationType;
import javax.persistence.GeneratedValue;
import javax.persistence.JoinColumn;
import javax.persistence.Column;
import com.test.entities.firstpackage.Appuser;
import javax.persistence.Id;

/**
 * No comment found in model diagram
 *
 * This file has been automatically generated
 */
@Entity
@Table(name="service")
public class Service {
	private Long serviceId;
	private String serviceName;
	private Service parentService;
	private Set<Service> serviceList;
	private Set<Appuser> usersOnSiteList;
	private Set<Appuser> membersList;

    /**
     * No comment found in model diagram
     * @return value of serviceId
     */
    @Id
    @SequenceGenerator(name="SEQ_SERVICE", sequenceName="SEQ_SERVICE", allocationSize=20)
    @GeneratedValue(strategy=GenerationType.SEQUENCE, generator="SEQ_SERVICE")
    @Column(name="service_id", nullable=false)
	public Long getServiceId(){
		return serviceId;
    }  
    /**
     * No comment found in model diagram
     * @param serviceId new value to give to serviceId
     */
	public void setServiceId(final Long serviceId){
		this.serviceId = serviceId;
    }  
    /**
     * No comment found in model diagram
     * @return value of serviceName
     */
    @Column(name="service_name", nullable=false)
	public String getServiceName(){
		return serviceName;
    }  
    /**
     * No comment found in model diagram
     * @param serviceName new value to give to serviceName
     */
	public void setServiceName(final String serviceName){
		this.serviceName = serviceName;
    }  
    /**
     * Association service_service_parent to Service
     * @return value of parentService
     */
    @ManyToOne
    @JoinColumn(name="parent_service_id", referencedColumnName="service_id")
	public Service getParentService(){
		return parentService;
    }  
    /**
     * Association service_service_parent to Service
     * @param parentService new value to give to parentService
     */
	public void setParentService(final Service parentService){
		this.parentService = parentService;
    }  
    /**
     * Association service_service_parent to Service
     * @return value of serviceList
     */
    @OneToMany(mappedBy="parentService", orphanRemoval=true)
	public Set<Service> getServiceList(){
		return serviceList;
    }  
    /**
     * Association service_service_parent to Service
     * @param serviceList new value to give to serviceList
     */
	public void setServiceList(final Set<Service> serviceList){
		this.serviceList = serviceList;
    }  
    /**
     * Association service_workplace_id to Appuser
     * @return value of usersOnSiteList
     */
    @OneToMany(mappedBy="workplaceService", orphanRemoval=true)
	public Set<Appuser> getUsersOnSiteList(){
		return usersOnSiteList;
    }  
    /**
     * Association service_workplace_id to Appuser
     * @param usersOnSiteList new value to give to usersOnSiteList
     */
	public void setUsersOnSiteList(final Set<Appuser> usersOnSiteList){
		this.usersOnSiteList = usersOnSiteList;
    }  
    /**
     * Association user_service to Appuser
     * @return value of membersList
     */
    @OneToMany(mappedBy="userService", orphanRemoval=true)
	public Set<Appuser> getMembersList(){
		return membersList;
    }  
    /**
     * Association user_service to Appuser
     * @param membersList new value to give to membersList
     */
	public void setMembersList(final Set<Appuser> membersList){
		this.membersList = membersList;
    }  

	@Override
	public int hashCode(){
	 	// Start with a non-zero constant. Prime is preferred
	    int result = 17;
	
		result = 31 * result + (serviceId == null? 0 : serviceId.hashCode());
		result = 31 * result + (serviceName == null? 0 : serviceName.hashCode());
		result = 31 * result + (parentService == null? 0 : parentService.hashCode());
		result = 31 * result + (serviceList == null? 0 : serviceList.hashCode());
		result = 31 * result + (usersOnSiteList == null? 0 : usersOnSiteList.hashCode());
		result = 31 * result + (membersList == null? 0 : membersList.hashCode());
			
		return result;
	}

	@Override
	public boolean equals(Object other){
		// Same object
	    if (this == other) {
	        return true;
	    }
	
		// Wrong type
	    if (!(other instanceof Service)) {
	        return false;
	    }
	
		// Test all attributes
	    Service otherService = (Service) other;
	    
		return (serviceId == null ? serviceId == null:serviceId.equals(otherService.serviceId))
			&& (serviceName == null ? serviceName == null:serviceName.equals(otherService.serviceName))
			&& (parentService == null ? parentService == null:parentService.equals(otherService.parentService))
			&& (serviceList == null ? serviceList == null:serviceList.equals(otherService.serviceList))
			&& (usersOnSiteList == null ? usersOnSiteList == null:usersOnSiteList.equals(otherService.usersOnSiteList))
			&& (membersList == null ? membersList == null:membersList.equals(otherService.membersList))
		;
	}



// END OF GENERATED CODE - YOU CAN EDIT THE FILE AFTER THIS LINE, DO NOT EDIT THIS LINE OR BEFORE THIS LINE
}