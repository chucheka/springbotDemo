package com.neulogic.sendit.controller;



import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.neulogic.sendit.models.Parcel;
import com.neulogic.sendit.service.ParcelServiceImpl;



@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/v1")
public class ParcelController {
	@Autowired
	private ParcelServiceImpl service;
	

	@PutMapping("/parcels/{parcelId}/cancel")
	@PreAuthorize("hasRole('USER')")
	public Parcel cancelParcelOrder(@PathVariable("parcelId") Long parcelId,HttpServletRequest req) throws Exception {
		return service.cancelParcelOrder(parcelId,req);
	}
	
	@PutMapping("/parcels/{parcelId}/present_location")
	@PreAuthorize("hasRole('ADMIN')")
	public Parcel updateParcelLocation(@RequestBody Parcel parcel,@PathVariable("parcelId") Long parcelId) throws Exception {
		return service.updateParcelLocation(parcel, parcelId);
	}

	@PutMapping("/parcels/{parcelId}/destination")
	@ResponseStatus(HttpStatus.CREATED)
	@PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
	public Parcel updateParcelDestination(@RequestBody Parcel parcel,@PathVariable("parcelId") Long parcelId) throws Exception {
		return service.updateParcelDestination(parcel, parcelId);
	}
	
	@PutMapping("/parcels/{parcelId}/status")
	@PreAuthorize("hasRole('ADMIN')")
	public Parcel updateParcelStatus(@RequestBody Parcel parcel,@PathVariable("parcelId") Long parcelId) throws Exception {
		return service.updateParcelStatus(parcel,parcelId);
	}
	//CREATE PARCEL ORDER
	@PostMapping("/parcels")
	@PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
	public ResponseEntity<Parcel> createParcelOrder(@Valid @RequestBody Parcel newParcel,HttpServletRequest req) {
		
		try {
			Parcel parcel = service.createParcelOrder(newParcel, req);
		
			return new ResponseEntity<Parcel>(parcel,HttpStatus.CREATED);
		}catch(Exception exc) {
			return new ResponseEntity<>(null,HttpStatus.EXPECTATION_FAILED);
		}
	}
	
	@GetMapping("/parcels")
	@ResponseStatus(HttpStatus.OK)
	@PreAuthorize("hasRole('ADMIN')")
	public List<Parcel> getAllParcels() throws Exception{
		List<Parcel> parcels = service.getAllParcels();
		System.out.println(parcels);
	return parcels;
	}
	
	@GetMapping("/parcels/{parcelId}")
	@PreAuthorize("hasRole('USER') or hasRole('MODERATOR') or hasRole('ADMIN')")
	public Parcel getParcelById(@PathVariable("parcelId") long parcelId) throws Exception{
		Parcel parcel = service.getParceById(parcelId);
		return parcel;
		}
	
	@GetMapping("/users/{userId}/parcels")
	@PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
	public List<Parcel> getAllUserParcels(@PathVariable("userId") long userId) throws Exception {
		return service.getParcelsByUserId(userId);
	}
	
	
	@DeleteMapping("/parcels")
	@ResponseStatus(HttpStatus.OK)
	@PreAuthorize("hasRole('ADMIN')")
	public void deleteAllParcels() throws Exception{
	
		service.deleteAllParcels();
	
	}

	
}
