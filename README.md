# AddresBook
Sunbit Home Assignment


# Testing

1. Use your IDE to clone the repository to your local machine 
1. update DB credentials in `/AddresBook/src/main/resources/application.properties`
   and `/AddresBook/src/test/resources/application.properties` to local DB.
   i used Oracle , if your using a different DB you will need to add relevant dependecys to pom.xml
1. run application
1. import `/AddresBook/Contacts.postman_collection.json` to Postman 
1. run Rest API calls


# To Do

1. improve performance: use caching mechanism on top of Hibernate first level cache (automatically enabled)
1. handle large get requests: use pagination when there are a lot of records
1. add security token to rest API requests
