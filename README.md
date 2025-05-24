# NordeaTextParser
1) In this project we are going to parse the text as per the sentences and words and create a final output file in XML/CSV.

2) Project has been implementated using java17,maven3 and IDE minimum requirement to run the code.

3) To test this application, input/output file path need to be mentioned in application.properties file, 
   we can implement other approches as well like reading from sharedrive location etc. but due to a time contraint haven't got chance for it, we will implement in future.

4) We have use spring batch+multithreading approch without database to implement this project due to a time contraint.
   For now normal exception handling is done, but we can improve it with the help of database configuration
   so that all the spring batch metadata details like job, job excution time etc will store it in DB and we can use it to create a fault-tolerant/fine-grain implementation.
