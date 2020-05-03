package com.neulogic.sendit.models;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.neulogic.sendit.audit.AuditModel;


@Entity
@Table(name="parcels")
public class Parcel extends AuditModel{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long parcelId;
	
	@NotBlank
	@Size(max=255)
	private String pickupLocation;
	
	@NotBlank
	@Size(max=255)
	private String destination;
	
	private Double price;
	
	@Column(columnDefinition = "varchar(255) default 'AT_PICKUP'")
	private String status;
	
	private String presentLocation;
	
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id", nullable = false)
    @JsonBackReference
    private User user;

    public Parcel() {
    	
    }
    
    
	public Parcel(String pickupLocation,String destination, Double price, String status, String presentLocation,User user) {
		
		this.pickupLocation = pickupLocation;
		this.destination = destination;
		this.price = price;
		this.status = status;
		this.presentLocation = presentLocation;
		this.user = user;
	}

	public Long getParcelId() {
		return parcelId;
	}

	public void setParcelId(Long parcelId) {
		this.parcelId = parcelId;
	}

	public String getPickupLocation() {
		return pickupLocation;
	}

	public void setPickupLocation(String pickupLocation) {
		this.pickupLocation = pickupLocation;
	}

	public String getDestination() {
		return destination;
	}

	public void setDestination(String destination) {
		this.destination = destination;
	}

	public Double getPrice() {
		return price;
	}

	public void setPrice(Double price) {
		this.price = price;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getPresentLocation() {
		return presentLocation;
	}

	public void setPresentLocation(String presentLocation) {
		this.presentLocation = presentLocation;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}


//	@Override
//	public String toString() {
//		return "Parcel [parcelId=" + parcelId + ", pickupLocation=" + pickupLocation + ", destination=" + destination
//				+ ", price=" + price + ", status=" + status + ", presentLocation=" + presentLocation + ", user=" + user
//				+ "]";
//	}
	
	
}
