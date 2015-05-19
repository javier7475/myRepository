Commentary
========================================================

First of all, I created the Message and Gateway interfaces as defined in the exercise.

Messages are delivered in groups, so I added a "groupId" field to the MessageExample class to know each message's group.

The priority in which TO process groups is defined by the order in which you receive the first message from the group so I need to know each group's order. I will use a HashMap indexed by the group name. When I receive the first message of a group, I will add to that HashMap the group's order.

When no resources are available, messages should not be sent to the Gateway so I have to keep track of available resources. To be simple, I 'll just use a variable to count the number of available resources. When I send a message to the gateway, there is one less available resource. When a message is completed, there is one more available resource. At first, all the resources are available. I pass the number of resources to the scheduler's constructor.

When the scheduler receives a message, if there are available resources, I send the message to the gateway as we want to make sure that resources are not idle when messages are waiting to be processed.

I there are no available resources, I queue the message. The messages should be sorted, first of all, by the message's group order and then by the order the message is received.

When a message is completed, the scheduler should be notified. Then, the scheduler get next message from the queue and dispatches it as any other message.

To include cancellation of groups, I'll use a HashMap. When a group is cancelled, I just put the group's name in the HashMap. Before dispatching a Message I just see if the message's group is cancelled, and If cancelled, nothing is done.

The solution for "Termination Messages" would be quite similiar to the one for "Cancellation".

To extend the solution to "Alternative Message Prioritisation" overriding "queueMessage" and/or "getNextQueuedMessage" should be enough. We could also use a external MessageManager class to manage the prioritisation and pass it to the scheduler. To use different prioritisation algorithms that class could be extended.


Solution 'production quality'
========================================================
In a production environment the work could be balanced between all resources. Some commercial tools could be used to achieve this aim. I suppose calls made to he gateway are asynchronous, otherwise, threads, for example, could be used. Maybe it would also be necessary to keep track of messages received and/or sent to the gateway to ensure no messages are lost. Some kind of redundancy could be used to ensure the system is still working even if a machine falls down.


Tests
========================================================
To run the tests just run the Test class.


Instructions to build the example
========================================================
An eclipse project called "Resource Scheduler Exercise for JP" is provided. Just open it in the eclipse environment and link the "JRE System Library [JavaSE-1.8]" in the Java Build Path of the project.





