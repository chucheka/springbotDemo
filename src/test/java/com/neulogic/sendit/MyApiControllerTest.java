package com.neulogic.sendit;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import com.neulogic.sendit.models.Parcel;
import com.neulogic.sendit.models.User;
import com.neulogic.sendit.repository.UserRepository;

import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;

import static io.restassured.RestAssured.*;
//import static io.restassured.matcher.RestAssuredMatchers.*;
import static org.hamcrest.Matchers.*;




import org.json.JSONException;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
public class MyApiControllerTest extends AbstractTestNGSpringContextTests {

	String token;
	
	String unauthorizedUserToken;
	
	Parcel parcelOne,parcelTwo,parcelThree;
	
	@Autowired
	private UserRepository userRepo;	

	User dbUser;
	
	RequestSpecification requestSpec;
		
	

	// POST /api/v1/parcels 								priority 0     1 & 2 
	// GET  /api/v1/parcels/{parcelId} 						priority 1       1
	// GET  /api/v1/users/{userId} /parcels					priority 5		 1		 
	// PUT  /api/v1/parcels/{parcelId}/status 				priority 3		 1
	// PUT  /api/v1/parcels/{parcelId}/cancel 				priority 4       2
	// PUT  /api/v1/parcels/{parcelId}/destination 			priority 2       3
	// PUT  /api/v1/parcels/{parcelId}/presentLocation 		priority 6       
	
	
	
	@BeforeClass
	public void authorization() throws JSONException {
		
		
		RestAssured.baseURI = "http://localhost:8080/api/v1";
		
		dbUser = userRepo.findByUsername("Ucheka Chike");
		
		User user = new User();
		user.setUsername("Ucheka Chike");
		user.setPassword("chike22ucheka");
		
		
		User unauthorizedUser = new User();
		unauthorizedUser.setUsername("John Doe");
		unauthorizedUser.setPassword("john22doe");
		
		// Create the parcel for test
		parcelOne = new Parcel();
		parcelOne.setPickupLocation("New Owerri,Imo State");
		parcelOne.setDestination("Ojo,Lagos State");
		parcelOne.setPrice(25020.690);
		
		parcelTwo = new Parcel();
		parcelTwo.setPickupLocation("Lekki,Lagos State");
		parcelTwo.setDestination("Abeokuta,Ogun State");
		parcelTwo.setPrice(35020.690);
		
		parcelThree = new Parcel();
		parcelThree.setPickupLocation("Calabar,Cross River");
		parcelThree.setDestination("Benin,Edo State");
		parcelThree.setPrice(45020.690);
		
		 token = 
				given()
					.contentType(ContentType.JSON)
					.body(user)
				.when()
					.post("/auth/signin")
				.then()
					.contentType(ContentType.JSON)
					.statusCode(200)
				.extract()
					.path("accessToken");
		
		 unauthorizedUserToken = 
					given()
						.contentType(ContentType.JSON)
						.body(unauthorizedUser)
					.when()
						.post("/auth/signin")
					.then()
						.contentType(ContentType.JSON)
						.statusCode(200)
					.extract()
						.path("accessToken");
		 
		 
		 RequestSpecBuilder builder = new RequestSpecBuilder()
					.setContentType(ContentType.JSON)
					.addHeader("Authorization", "Bearer "+token);
	
		requestSpec =  builder.build();
		
		
		//Create Parcels one and two for further testing
							given()
								.spec(requestSpec)
								.body(parcelOne)
							.when()
								.post("/parcels")   
							.then()
								.statusCode(201);
				
							given()
								.spec(requestSpec)
								.body(parcelTwo)
							.when()
								.post("/parcels")   
							.then()
								.statusCode(201);
	}
	
//	@AfterTest()
//	public void clearUpDB(){
//			
//			given()
//				.spec(requestSpec)
//			.when()
//				.delete("/parcels")
//			.then()
//				.statusCode(200);
//	}
	
	// POST /api/v1/parcels
	@Test(priority = 0)
	public void postParcelTest() {	
		given()
			.spec(requestSpec)
			.body(parcelThree)
		.when()
			.post("/parcels")   
		.then()
			.statusCode(201)
			.body("pickupLocation",equalTo(parcelThree.getPickupLocation()))
			.body("destination",equalTo(parcelThree.getDestination()))
			.body("parcelId", is(notNullValue()));
		
	}
	
	@Test(priority = 1)
	public void getParcelByIdTest() {	
		int parcelId = 1;
		given()
			.pathParam("parcelId", parcelId)
			.spec(requestSpec)
		.when()
			.get("/parcels/{parcelId}")
		.then()
			.statusCode(200)
			.body("parcelId",equalTo(parcelId));
	}
	
	@Test(priority = 2)
	public void getAllParcelOrders_andItShould_returnAnArray_ofParcelsTest() {
		
		given()
			.spec(requestSpec)
		.when()
			.get("/parcels")
		.then()
			.statusCode(200)
			.body("size()", is(notNullValue()));
	}
	
	
	@Test(priority = 3)
	public void getParcelOrdersBy_A_SpecificUser() {
		
		given()
			.pathParam("userId", dbUser.getUserId())
			.spec(requestSpec)
		.when()
			.get("/users/{userId}/parcels")
		.then()
			.statusCode(200);
		
		//** Test for invalid user
		
		long invalidUserId = 2344;
		
		given()
			.pathParam("userId", invalidUserId)
			.spec(requestSpec)
		.when()
			.get("/users/{userId}/parcels")
		.then()
			.statusCode(400)
			.body("message",equalTo( "User does not exist"));
	}
	
	@Test(priority = 4)
	public void changeParcelStatusTest() {
		int parcelId = 1;
		Parcel parcelToChangeStatus = new Parcel();
		parcelToChangeStatus.setStatus("delivered");
		
		given()
			.pathParam("parcelId", parcelId)
			.spec(requestSpec)
			.body(parcelToChangeStatus)
		.when()
			.put("/parcels/{parcelId}/status")
		.then()
			.statusCode(200)
			.body("parcelId", equalTo(parcelId))
			.body("status", equalTo(parcelToChangeStatus.getStatus()));
	}

	@Test(priority = 5,enabled = false)
	public void changeParcelOrder_DestinationTest() {
		int parcelId = 2;
		Parcel parcelToChangeDestination = new Parcel();
		parcelToChangeDestination.setDestination("Ketu,Lagos state");
		
		given()
			.pathParam("parcelId", parcelId)
			.spec(requestSpec)
			.body(parcelToChangeDestination)
		.when()
			.put("/parcels/{parcelId}/status")
		.then()
			.statusCode(200)
			.body("destination", equalTo(parcelToChangeDestination.getDestination()));
	}

	@Test(priority = 6)
	public void cannotChangeDestinationOfParcelOrderMarked_DELIVEREDTest() {
		int parcelId = 1;
		Parcel parcelToChangeDestination = new Parcel();
		parcelToChangeDestination.setDestination("Ketu,Lagos state");
		
		given()
			.pathParam("parcelId", parcelId)
			.spec(requestSpec)
			.body(parcelToChangeDestination)
		.when()
			.put("/parcels/{parcelId}/status")
		.then()
			.statusCode(400);
	}
	@Test(priority = 7)
	public void cancelAParcelOrderTest() {
		int parcelId = 3;
		
		given()
			.pathParam("parcelId", parcelId)
			.spec(requestSpec)
		.when()
			.put("/parcels/{parcelId}/cancel")
		.then()
			.statusCode(200)
			.body("parcelId", equalTo(parcelId))
			.body("status",equalTo("cancelled"));
	}
	
	@Test(priority = 8)
	public void cannot_CancelAParcelOrderMarked_DELIVERED_Test() {
		int parcelId = 1;
		
		given()
			.pathParam("parcelId", parcelId)
			.spec(requestSpec)
		.when()
			.put("/parcels/{parcelId}/cancel")
		.then()
			.contentType(ContentType.JSON)
			.statusCode(400)
			.body("message",equalTo("Can not cancel parcel order that has been delivered"));
	}

	@Test(priority = 9)
	public void onlyCreatorOfParcelOrder_canCancellOrderTest() {
		int parcelId = 3;
		
		given()
			.pathParam("parcelId", parcelId)
			.header("Authorization","Bearer "+unauthorizedUserToken)
		.when()
			.put("/parcels/{parcelId}/cancel")
		.then()
			.statusCode(400)
			.body("message",equalTo("Only user who created parcel order can cancel order"));
	}
	
	
	
	@Test(priority = 10)
	public void changeParcelPresentLocationTest() {
		int parcelId = 1;
		Parcel parcelToChangePresentLocation = new Parcel();
		parcelToChangePresentLocation.setPresentLocation("Asaba,Delta state");
		
		given()
			.pathParam("parcelId", parcelId)
			.spec(requestSpec)
			.body(parcelToChangePresentLocation)
		.when()
			.put("/parcels/{parcelId}/present_location")
		.then()
			.statusCode(200);
	}
	
}
	

