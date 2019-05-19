# Offers

Offers is a Spring RESTFul Web Service for dealing with offers.

Offers is written in Java 8, using the following :
 * maven
 * h2 in-memory database
 * Spring initializr
 * Spring boot
 * Spring JPA
 * Spring Web
 * Spring Hateoas
 * Lombok
 
## User stories and assumptions

The following user stories have been taking into account for test creation:

 * s1: the user can create new offers
 * s2: the user can retrieve an offer 
 * s3: the user can retrieve the offers
 * s4: the user can cancel an offer

Assumptions:
 * Even if an offer refers to a product, the latter is not represented for simplification purposes and because there are
   not references to it in the problem description.
 * I decided to create a state for the offer with 4 different values (CREATED,ACTIVE,EXPIRED,CANCELLED).
   The main reason for that is to simplify the client development, as there would not be necessary to deal with dates to
   know where an offer is active or not.
   
## TDD

One test scenario has been created for each user story. Then can be found in the file OffersRestTest.java :

 * s1: createNewOfferReturnsCorrectResponse()
 * s2: getOfferReturnsCorrectResponse()
 * s3: getAllOffersReturnsCorrectResponse()
 * s4: cancelOfferReturnsCorrectResponse()

## API

The following HTTP methods have been implemented for each scenario (uk.worldpay.offers.controller.OfferController.java):

 * s1: HTTP POST /api/v1/offers with RequestBody containing an Offer object
 * s2: HTTP GET  /api/v1/offers/{id}
 * s3: HTTP GET /api/v1/offers/
 * s4: HTTP DELETE /api/v1/offers/{id}/cancel  

Examples (with curl):

 * s1:
   ```curl -X POST localhost:8080/api/v1/offers -H 'Content-type:application/json' -d '{"id":1,"itemId":1,"description":"Last units opportunity. Half Price!!!","price":10.1,"startDate":"2019-05-19T14:19:29.488","endDate":"2019-05-22T14:19:29.488"}'```
 * s2: 
     ```$ curl -v localhost:8080/api/v1/offers/1```
 * s3: 
     ```$ curl -v localhost:8080/api/v1/offers```
 * s4: 
     ```$ curl -v localhost:8080/api/v1/offers```
 * s4:
     ```$ curl -X DELETE localhost:8080/api/v1/offers/1/cancel```
## Usage

The tests can be run with :
```$ mvn test```

The application can be run with :
``` mvn clean spring-boot:run```
