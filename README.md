# msf4j-queueing
A sample demonstrating how two MSF4J microservices communicate with each other via JMS queues.

![Architecture](images/architecture.png) 

## Scenario
Requests come into the Purchasing microservice via HTTP. If the items in stock fall below the reorder level,
a reorder request is placed via JMS to the Reordering microservice. There are two queues, Reorder Request Queue &
Reorder Response Queue which are created in WSO2 Message Broker(MB). Once the reorder request is received via the
Reorder Request Queue, the Reordering microservice will process the request, and send out a Reorder Response 
Message to the Reorder Response Queue.
