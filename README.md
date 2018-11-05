# Digital Parking

This is a digital parking management system.

## How to build and run

mvn install

java -jar target/digitalparking-0.0.1-SNAPSHOT.jar

## Features

### Creating a new asset

curl --data '{"address":"Example St.","active":false}' --header "Content-Type:application/json" -X POST localhost:8080/pms/v1/assets

### Start parking

curl --data '{"licensePlateNumber":"A111"}' --header "Content-Type:application/json" -X POST localhost:8080/pms/v1/assets/1/sessions

### End parking

curl --data '{"status":"stopped"}' --header "Content-Type:application/json" -X POST localhost:8080/pms/v1/assets/1/vehicle/A111/session

## Sending E-Mail

On the application startup you can see in the log:

Email has been sent to zoe@test.com: <html><div>Parking time: 2018-01-01T10:00 - 2018-01-01T15:00</div><div>Vehicle: Z333</div><div>Cost: 5</div></html>

That means the e-mail was suposed to be sent. The real e-mail sending feature was not implemented.

The system sends e-mail when an invoice is made and also when the system is starting up, it re-sends the not sent invoices.

## Next steps

Next implementations would include:

* Exception handling on the GlobalExceptionHandler for each case of negative response
* Unit tests
* Other resources / operations for the next features
* Swagger documentation
* Removing the last code smells I've left
