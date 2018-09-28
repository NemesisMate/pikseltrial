## The Project
The project consist on a fully asynchronous reactive server **POC (Proof Of Concept)**, that tracks the viewings performed by a client of studio's episodes with the purpose of knowing the owing money to the former. To summarize, we have:

* **Studio/Customer**: Used indistinctly, refers to an episodes owner. A studio can have many listed episodes.
* **Episode**: Each of the products provided by an studio. Each episode for a given studio has the same royalty fee.
* **Views**: An API-client can view (send a view track request) an episode, that will be added to the owner's (studio) viewings count.
  

The services we provide to the requester (API-client) can be separated in three main blocks:

* **management**: Perform system management actions. Actually, it can only reset the whole system (clear tracking).
> POST /royaltymanager/reset
* **tracking**: Track some episode viewings.
> POST /royaltymanager/viewing
* **consulting**: Get information about the payments and royalties owed to an studio or all of them.
> GET /royaltymanager/payments

> GET /royaltymanager/payments/{Rights Owner GUID}


## The technologies

The main tech-stack is: **Java 10 + SpringBoot + WebFlux**

Additionally, some tools are being used:

* H2 (for an in-memory db)


## Try-it yourself

#### Tests

You can launch the test with ease:
```
$ ./gradlew test
```


#### Execution

>For the sake of the example, the viewings persistence is being done every 5 seconds (logging it to the console)

You can try the project executing the server. To execute it there are several ways:

* **Build-it yourself**: You can build the project with ease, for it you must open a console in the root folder of the project and execute:
```
$ ./gradlew bootRun
```
> (This order will automatically build and execute the server.)

* **Execute the [provided jar](https://github.com/NemesisMate/pikseltrial/releases/)**
```
java -jar <JAR_NAME>.jar
```

#### Access

Now, (once the server is up) to manually test the project you can attack the different endpoints on: `http://localhost:8080`

The full provided specification is:
```
POST /royaltymanager/reset

No req body

No validation

Response

HTTP 202 with empty body

----

POST /royaltymanager/viewing

Req Body

{ 
  "episode": "GUID",
  "customer": "GUID"
}
  
Validation
  
  episode GUID mandatory and must exist in system
  customer GUID mandatory, no further validation
  
Response

HTTP 202 with empty body

----

GET /royaltymanager/payments

Response

HTTP 200

Response Body

{ [
	{
	  "rightsownerId": "GUID",
	  "rightsowner": "Name"
	  "royalty:" Value in GBP£ e.g 15000.23,
	  "viewings": number of viewings
	},
	...
] }

----

GET /royaltymanager/payments/{Rights Owner GUID}

Response

HTTP 200

Response Body

	{
	  "rightsowner: "Name",
	  "royalty": Value in £ e.g 15000.23,
	  "viewings": number of viewings
	}
	
No GUID validation, return 404 on GUID not found.
```
