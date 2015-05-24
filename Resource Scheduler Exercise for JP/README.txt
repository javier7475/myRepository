Let's start. Let's use Test Driven Development.

Let's start with the simplest requirement. 

Test 1 "When no resources are available, messages should not be sent to the Gateway."
====================================================================================

there is 1 resource
message 1 is received by the scheduler
message 1 should be sent to the gateway
message 1 finishes
message 2 is received by the scheduler
message 2 should be sent to the gateway
message 3 is received by the scheduler
message 3 should no be sent to the gateway as there are no free resources

To be simple, I will use a variable to count the number or free resources. The initial value of this variable should be "1" as we suppose the resource if free before start working. 
When a message is sent to the gateway, there is one less free resource. When a message finishes there is on more free resource. I create two methods, "getAvailableResource" and "addAvailableResource". They must be synchronized for preventing thread interference.
When a message finishes the scheduler should be notified to increase the number of available resources. So the message "completed()" method will call the "completed()" method of the scheduler so I will add a "scheduler" field in the message for this purpose. When a message is dispatched by the scheduler, this field must be set.
To run the test, the "dispatch" and "completed" method of the scheduler return the message sent to the queue or "null" otherwise.


Test 2 "the scheduler can be configured with the number of resources available. We want to make sure that they are no idle resources when messages are waiting to be processed"
=======================================================================================================================

there are 2 resources
message 1 is received by the scheduler
message 1 should be sent to the gateway
message 2 is received by the scheduler
message 2 should be sent to the gateway as there is one free resource

This test fails as there is just one resource.
I just added a parameter to the scheduler's constructor no initialize the number of available resources. No more changes should be made to the code.


Test 3 "As messages are completed, if there are queued messages, they should be processed"
==========================================================================================

there is 1 resource
message 1 is received by the scheduler
message 1 should be sent to the gateway
message 1 finishes
message 2 is received by the scheduler
message 2 should be sent to the gateway
message 3 is received by the scheduler
message 3 should be queued
message 2 finishes
message 3 should be sent to the gateway

This test fails a I didn't queue messages.
I created a "queueMessage" method to queue messages when there are no free resources and "getNextQueuedMessage" to get next queued message. To queue messages I used a Queue field.
When a message is received, if there are no free resources, it is queued. When a message is completed, the scheduler get next message from the queue.


Test 4:"Messages have a logical grouping. Where possible, the message groups should not be interleaved"
=======================================================================================================

there is one resource
message 1 of group 2 is received by the scheduler
message 1 should be sent to the gateway
message 2 of group 1 is received by the scheduler
message 2 should be queued as there are no free resources, no message should be sent to the gateway
message 3 of group 2 is received by the scheduler
message 3 should be queued, no message should be sent to the gateway
message 1 finishes
message 3 should be sent to the gateway as belongs to group 2 that has already started and message groups should not be interleaved
message 3 finishes
message 2 should be sent to the gateway  as it must be the first in the queue
message 3 finishes
message 2 finishes

I added a "groupId" to the "MessageExample" class to run this test.
This test fails as I am not taking into account grouping.
I need to know if a group has already started. I will use a HashMap indexed by groupId to be efficient. When the scheduler receives a message from a group, a value is inserted in the HashMap. When the scheduler queues a message, if its group has already started, the messsage should be the first in the queue.


Test 5:"the message groups should not be interleaved...except where resources are idle and other work can be done"
==================================================================================================================

there are two resources
message 1 of group 2 is received by the scheduler
message 1 should be sent to the gateway
message 2 of group 1 is received by the scheduler
message 2 should be sent to the gateway as we don't want resources to be idle
message 3 of group 2 is received by the scheduler
message 3 should be queued, no message should be sent to the gateway
message 1 finishes
message 3 should be sent to the gateway 
message 2 finishes
message 3 finishes

This test works as it is covered by previous implementations.


Test 6:"The priority in which to process groups is defined by the order in which you receive the first message from the group "
=======================================================================================================================

there is 1 resource
message 1 of group 1 is received by the scheduler
message 1 should be sent to the gateway
message 1 finishes
message 2 of group 2 is received by the scheduler
message 2 should be sent to the gateway
message 3 of group 1 is received by the scheduler
message 3 should be queued, no message should be sent to the gateway
message 4 of group 2 is received by the scheduler
message 4 should be queued, no message should be sent to the gateway
message 5 of group 1 is received by the scheduler
message 5 should be queued, no message should be sent to the gateway
message 2 finishes
message 3 should be sent to the gateway
message 3 finishes
message 5 should be sent to the gateway as group 1 has higher priority than group 2
message 5 finishes
message 4 should be sent to the gateway


This test fail as i am not taking into account group priority.
I need to know the order of each group. I will use a HashMap indexed by groupId to be efficient. I will use the HashMap I already had and I will save the group order.
But messages should be processed based on its group priority. So I will have a queue of groups rather than a queue o messages to be more efficient. Each group should have a queue of messages waiting to be processed. So when I queue message, I append it to its group queue and if the group is not queued I will queue it.
To get next message from the queue, I will get the first message from the first group of the queue. If there are no more messages in the group's queue, I will remove the group from the group's queue.
Group's queue should be ordered by group's order so I made a GroupInfo class to allocate both group's order and group's queued messages. To allocate group's queue I must use a sorted structure like TreeSet. GroupInfo implements a compareTo method to compare two GroupInfo based on its order.


Test 7:"It should be possible to tell the scheduler that a group of messages has now been cancelled. Once cancelled, no further messages from that group should sent to the Gateway."
=======================================================================================================================

there is 1 resource
message 1 of group 2 is received by the scheduler
message 1 should be sent to the gateway
group 2 is cancelled
message 2 of group 1 is received by the scheduler
message 2 should be queued
message 3 of group 2 is received by the scheduler
message 3 should be ignored as group 2 has been cancelled
message 4 of group 3 is received by the scheduler
message 1 finishes
message 2 should be sent to the gateway
message 2 finishes
message 4 should be sent to the gateway

I need to add a "cancelGroup" method.
I need to know which groups have been cancelled. I will use a HashMap indexed by groupId to be more efficient.
When the scheduler receives a message, if its group has been cancelled, the message is ignored.



Additional extensions
======================

Alternative Message Prioritisation:
To extend the solution to "Alternative Message Prioritisation" overriding "queueMessage" and/or "getNextQueuedMessage" should be enough. Depending on the algorithm coluld be necessary to override "dipatch" and/or "addGropp" methods. We could also use an external MessageManager class to manage the prioritisation and pass it to the scheduler. To use different prioritisation algorithms that class could be extended.


Termination Messages:
The solution could look like the one for "Cancellation". When a termination message is received, the group should be marked as terminated and if more messages of that group are received, an exception should be raised.


Running the tests
=================

Just run "SchedulerTest" as Junit Test in the Eclipse environment.

Building
========

An eclipse project is provided.





























