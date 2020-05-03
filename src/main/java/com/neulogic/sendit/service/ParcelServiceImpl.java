package com.neulogic.sendit.service;



import java.util.List;
import java.util.Optional;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.neulogic.sendit.exception.ParcelNotFoundException;
import com.neulogic.sendit.models.Parcel;
import com.neulogic.sendit.models.User;
import com.neulogic.sendit.repository.ParcelRepository;
import com.neulogic.sendit.repository.UserRepository;
import com.neulogic.sendit.utils.UserUtil;

@Service
public class ParcelServiceImpl {

	@Autowired
	private ParcelRepository repository;

	@Autowired
	private UserRepository userRepo;
	
	@Autowired
	private UserUtil userUtil;
	
//	@Autowired
//	private EmailService emailService;

	// GET/api/v1/parcels
	
		public List<Parcel> getAllParcels() throws Exception{
		
			try {
				List<Parcel> allParcels = 	repository.findAll();
				return allParcels;
			}catch(Exception exc) {
				throw new Exception("No parcels found");
			}
		
		}
		
		//	GET/api/v1/parcels/{parcelId}
		
		public Parcel getParceById(long parcelId) throws Exception{
			
			Optional<Parcel> parcel = repository.findById(parcelId);
			
			if(parcel.isPresent()) {
				return parcel.get();
			}else {
				throw new ParcelNotFoundException("Parcel with parcel Id "+ parcelId + " Not found");
			}
 }
		
		// POST/api/v1/parcels
		
		public Parcel createParcelOrder(Parcel newParcel,HttpServletRequest req) throws Exception {
		
			User user =  userUtil.getCurrentUser(req);
			 newParcel.setUser(user);
			 newParcel.setPresentLocation(newParcel.getPickupLocation());
			
			try {
				
				Parcel parcel = repository.save(newParcel);
				
				return parcel;
				
			}catch(Exception exc) {
				throw new Exception("Order creation failed internally");
			}
 }
		
		// GET/api/v1/users/{userId}/parcels
		
		public List<Parcel> getParcelsByUserId(long userId) throws Exception{
				//Find the user by user id
			User user = userRepo.findById(userId).orElseThrow(()->new Exception("User does not exist"));
				//Get the  parcels for user
		
			return user.getParcels();
 }
		
		// PUT/api/v1/parcels/{parcelId}/status
		
		public Parcel updateParcelStatus(Parcel parcelForUpdate,long parcelId) throws Exception{
			Parcel parcel = repository.findById(parcelId)
					.orElseThrow(()->new ParcelNotFoundException("Parcel to cancel not found!!"));
			if("delivered".equalsIgnoreCase(parcel.getStatus())) {
				throw new Exception("Cannot changed status of parcel already delivered");
			}
			// Update the status
            parcel.setStatus(parcelForUpdate.getStatus());
            
            Parcel updatedParcel = repository.save(parcel);
            
//            if(updatedParcel != null) {
//            	
//            	emailService.sendEmail("ryanucheka@gmail.com", "UPDATES FROM SENDIT COURIER", "Your parcel has been " +updatedParcel.getStatus());
//            }
			return updatedParcel;
 }
		
		// PUT/api/v1/parcels/{parcelId}/cancel
		
		public Parcel cancelParcelOrder( long parcelId, HttpServletRequest req) throws Exception  {
			Parcel dbParcel = repository.findById(parcelId)
					.orElseThrow(()->new ParcelNotFoundException("Parcel to cancel not found!!"));
			User authUser = userUtil.getCurrentUser(req);
			User parcelOwner = dbParcel.getUser();
			
			if(!parcelOwner.equals(authUser)) {
				throw new Exception("Only user who created parcel order can cancel order");
		}
			
			if("delivered".equals(dbParcel.getStatus())) {
				throw new Exception("Can not cancel parcel order that has been delivered");
			}else {
			dbParcel.setStatus("cancelled");
			return repository.save(dbParcel);
		}
		
 }
	
	// PUT/api/v1/parcels/{parcelId}present_location
		
	public Parcel updateParcelLocation(Parcel parcelForUpdate,long parcelId) throws Exception{
		//Fetch the parcel with particular Id
		Optional<Parcel> parcel = repository.findById(parcelId);
		//Check if parcel exist
		if(parcel.isPresent()) 
        {
            Parcel updatedParcel = parcel.get();
            // Update the parcel location
            updatedParcel.setPresentLocation(parcelForUpdate.getPresentLocation());
            updatedParcel = repository.save(updatedParcel);
            return updatedParcel;
        } else {
        	throw new ParcelNotFoundException("Parcel to change location doen not exist");
        }
		
	}
	
	
	//PUT/api/v1/parcels/{parcelId}/destination
	
	public Parcel updateParcelDestination(Parcel parcelForUpdate,long parcelId) throws Exception{
		
		//Fetch the parcel with particular Id
		Optional<Parcel> parcel = repository.findById(parcelId);
		//Check if parcel exist
		try {
			if(parcel.isPresent()) 
	        {
	            Parcel updatedParcel = parcel.get();
	            if("delivered".equalsIgnoreCase(updatedParcel.getStatus())) {
	            	throw new Exception("Cannot change destination of already delivered parcel");
	            }
	            // Update the parcel destination
	            updatedParcel.setDestination(parcelForUpdate.getDestination());
	            updatedParcel = repository.save(updatedParcel);
//	            if(updatedParcel != null) {
//	            	emailService.sendEmail("ryanucheka@gmail.com", "UPDATES FROM SENDIT COURIER", "Your parcel destination now at " +updatedParcel.getDestination());
//	            }
	            return updatedParcel;
	        } else {
	        	throw new ParcelNotFoundException("Parcel to change destination doen not exist");
	        }
			
		}catch(Exception exc) {
			throw new Exception(exc.getMessage());
		}
		
	}

	public void deleteAllParcels() {
		repository.deleteAll();
		
	}
}
