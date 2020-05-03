package com.neulogic.sendit.models;


import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.neulogic.sendit.audit.AuditModel;

@Entity
@Table(	name = "users", 
		uniqueConstraints = { 
			@UniqueConstraint(columnNames = "username"),
			@UniqueConstraint(columnNames = "email") 
		})
public class User extends AuditModel{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long userId;

	@NotBlank
	@Size(max = 20)
	private String username;

	@NotBlank
	@Size(max = 50)
	@Email
	private String email;

	@NotBlank
	@Size(max = 120)
	private String password;

	@ManyToMany(fetch = FetchType.LAZY)
	@JoinTable(	name = "user_roles", 
				joinColumns = @JoinColumn(name = "user_id"), 
				inverseJoinColumns = @JoinColumn(name = "role_id"))
	private Set<Role> roles  = new HashSet<>();
	
	
	@OneToMany(cascade = CascadeType.ALL,
            fetch = FetchType.LAZY,
            mappedBy = "user")
	@JsonManagedReference
    private List<Parcel> parcels = new ArrayList<>();


		
	public User() {
		
	}
	

	public User( String username,String email,String password) {
		this.username = username;
		this.email = email;
		this.password = password;
	}




	public Long getUserId() {
		return userId;
	}


	public void setUserId(Long userId) {
		this.userId = userId;
	}


	public String getUsername() {
		return username;
	}


	public void setUsername(String username) {
		this.username = username;
	}


	public String getEmail() {
		return email;
	}


	public void setEmail(String email) {
		this.email = email;
	}


	public String getPassword() {
		return password;
	}


	public void setPassword(String password) {
		this.password = password;
	}


	public Set<Role> getRoles() {
		return roles;
	}


	public void setRoles(Set<Role> roles) {
		this.roles = roles;
	}


	public List<Parcel> getParcels() {
		return parcels;
	}


	public void setParcels(List<Parcel> parcels) {
		this.parcels = parcels;
	}
	
public void addParcel(Parcel parcel) {
		
		parcels.add(parcel);
		parcel.setUser(this);
	}
	
	public void removeParcel(Parcel parcel) {
		parcels.remove(parcel);
		parcel.setUser(null);
	}


//	@Override
//	public String toString() {
//		return "User [userId=" + userId + ", username=" + username + ", email=" + email + ", password=" + password
//				+ ", roles=" + roles + ", parcels=" + parcels + "]";
//	}

	

}