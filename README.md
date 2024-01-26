## Url Shortener

App to shorten urls and then redirect to original urls starting from the short url
# How to run

* Clone this project
* Run `docker-compose up`
* There are 2 endpoints that you can interact with from tools like Postman or using the command line as shown below:

```
curl -X POST -H "Content-Type: application/json" -d '{"url": "http://example.com"}' http://localhost:8080/shortenUrl
```

```
curl http://localhost:8080/redirect?url=shortUrl
```

# Considerations
* For a given url there can only be one short url* 
* The db is already bootstrapped with the following data:
```
Short Url      Original Url
abc123         https://example.com
xyz789         https://example.org
```
* The app will load db properties from the application.properties file, if you prefer to connect to your own db please adjust the values in there.
* The app is split in 3 layers:
  * Controllers: the location for the endpoints
  * Services: the main domain logic
  * Repository: the data access layer where the app will connect to the db to store and retrieve data.
* There is also a db panel included in the docker-compose file in case you want to use that to manipulate the db directly, it is accessible on localhost:8081 (user: user, password: password)
  