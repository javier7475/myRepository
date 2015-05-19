
public class Test {
	
	// This class runs several examples to test the Scheduler

	
	public static void test1() {
		GatewayExample gateway=new GatewayExample();
		Scheduler scheduler=new Scheduler(1,gateway);

		MessageExample message1g2=new MessageExample("group2","message 1"); 
		MessageExample message2g1=new MessageExample("group1","message 2");
		MessageExample message3g2=new MessageExample("group2","message 3");
		MessageExample message4g3=new MessageExample("group3","message 4");
		
		Logger.log("Example 1 --------------------------------------------");
		Logger.log("Test Case:'Where possible, the message groups should not be interleaved'");
		Logger.log("This is the first test. The example in the excercise");
		Logger.log("Just one resource. Message 1 and 3 belong to group 2.");
		Logger.log("Message 3 belongs to group2 which has already been started and there are not resources available when message 2 is recived so mesage 3 must be processed before message 2");
		Logger.log("So de order should be 1,3,2,4");
		Logger.log("\n");
		scheduler.dispatch(message1g2);
		scheduler.dispatch(message2g1);
		scheduler.dispatch(message3g2);
		scheduler.dispatch(message4g3);
		message1g2.completed();
		message3g2.completed();
		message2g1.completed();
		message4g3.completed();
		
	}
	
	public static void test2() {
		GatewayExample gateway=new GatewayExample();
		Scheduler scheduler=new Scheduler(1,gateway);
		
		MessageExample message1g2=new MessageExample("group2","message 1"); 
		MessageExample message2g1=new MessageExample("group1","message 2");
		MessageExample message3g2=new MessageExample("group2","message 3");
		MessageExample message4g3=new MessageExample("group3","message 4");

		Logger.log("\n");
		Logger.log("\n");
		Logger.log("Example 2 --------------------------------------------");
		Logger.log("Test Case:'we want to make sure that they are not idle when messages are waiting to be processed'");
		Logger.log("This is same example as before but message 1 is completed before message 3 is received.");
		Logger.log("So, as we don't want resources to be idle when messages are waiting to be processed, message 2 should be processed before message 3");
		Logger.log("So de order should be 1,2,3,4");
		Logger.log("\n");
		
		scheduler.dispatch(message1g2);
		scheduler.dispatch(message2g1);
		message1g2.completed();
		scheduler.dispatch(message3g2);
		scheduler.dispatch(message4g3);
		message2g1.completed();
		message3g2.completed();
		message4g3.completed();		
	}
	
	public static void test3() {
		
		GatewayExample gateway=new GatewayExample();
		Scheduler scheduler=new Scheduler(2,gateway);
		
		MessageExample message1g2=new MessageExample("group2","message 1"); 
		MessageExample message2g1=new MessageExample("group1","message 2");
		MessageExample message3g2=new MessageExample("group2","message 3");
		MessageExample message4g3=new MessageExample("group3","message 4");
		
		Logger.log("\n");
		Logger.log("\n");
		Logger.log("Example 3 --------------------------------------------");
		Logger.log("Test Case:'we want to make sure that they are not idle when messages are waiting to be processed'");
		Logger.log("This is the first test. The example in the excercise");
		Logger.log("But now, there are 2 resources, so when message 2 is received, it must be processed as we don't want resources to be idle when messages are waiting to be processed");
		Logger.log("So de order should be 1,2,3,4");
		Logger.log("\n");
		
		scheduler.dispatch(message1g2);
		scheduler.dispatch(message2g1);
		scheduler.dispatch(message3g2);
		scheduler.dispatch(message4g3);
		message1g2.completed();
		message2g1.completed();
		message3g2.completed();
		message4g3.completed();		
	}
	
	
	public static void test4() {
		GatewayExample gateway=new GatewayExample();
		Scheduler scheduler=new Scheduler(1,gateway);
		
		MessageExample message1g2=new MessageExample("group2","message 1"); 
		MessageExample message2g1=new MessageExample("group1","message 2");
		MessageExample message3g2=new MessageExample("group2","message 3");
		MessageExample message4g3=new MessageExample("group3","message 4");
		
		Logger.log("\n");
		Logger.log("\n");
		Logger.log("Example 4 --------------------------------------------");
		Logger.log("Test Case:'If there are messages belonging to multiple groups in the queue, as resources become available, we want to prioritise messages from groups already started. The priority in which to process groups is defined by the order in which you receive the first message from the group.'");
		Logger.log("In this case, message 4 is received before message 3 but it must be processed after message 3 because group 2 has a higher priority than group 3.");
		Logger.log("So de order should be 1,3,2,4");
		Logger.log("\n");

		scheduler.dispatch(message1g2);
		scheduler.dispatch(message2g1);
		scheduler.dispatch(message4g3);
		scheduler.dispatch(message3g2);
		message1g2.completed();
		message3g2.completed();
		message2g1.completed();
		message4g3.completed();
		
	}
	
	public static void test5() {
		GatewayExample gateway=new GatewayExample();
		Scheduler scheduler=new Scheduler(1,gateway);
		
		MessageExample message1g2=new MessageExample("group2","message 1"); 
		MessageExample message2g1=new MessageExample("group1","message 2");
		MessageExample message3g2=new MessageExample("group2","message 3");
		MessageExample message4g3=new MessageExample("group3","message 4");
		MessageExample message5g3=new MessageExample("group3","message 5");
		
		
		Logger.log("\n");
		Logger.log("\n");
		Logger.log("Example 5 --------------------------------------------");
		Logger.log("Test Case:'If there are messages belonging to multiple groups in the queue, as resources become available, we want to prioritise messages from groups already started. The priority in which to process groups is defined by the order in which you receive the first message from the group.'");
		Logger.log("In this case, message 4 is received before message 3 but it must be processed after message 3 because group 2 has a higher priority than group 3.");
		Logger.log("So de order should be 1,3,5,4,2");
		Logger.log("\n");
		
		scheduler.dispatch(message1g2);
		scheduler.dispatch(message5g3);
		scheduler.dispatch(message2g1);
		scheduler.dispatch(message4g3);
		scheduler.dispatch(message3g2);
		message1g2.completed();
		message3g2.completed();
		message5g3.completed();
		message4g3.completed();
		message2g1.completed();
	}
	
	public static void test6() {
		GatewayExample gateway=new GatewayExample();
		Scheduler scheduler=new Scheduler(1,gateway);
		
		MessageExample message1g2=new MessageExample("group2","message 1"); 
		MessageExample message2g1=new MessageExample("group1","message 2");
		MessageExample message3g2=new MessageExample("group2","message 3");
		MessageExample message4g3=new MessageExample("group3","message 4");
		MessageExample message5g3=new MessageExample("group3","message 5");
		
		
		Logger.log("\n");
		Logger.log("\n");
		Logger.log("Example 6 --------------------------------------------");
		Logger.log("Test Case:'If there are messages belonging to multiple groups in the queue, as resources become available, we want to prioritise messages from groups already started. The priority in which to process groups is defined by the order in which you receive the first message from the group. But we don't want resources to be idle while there are messages in the queue'");
		Logger.log("In this case, message 1 finishes before receiving any other message from the group and we don't want resources to be idle.");
		Logger.log("So de order should be 1,5,3,4,2");
		Logger.log("\n");
		
		scheduler.dispatch(message1g2);
		scheduler.dispatch(message5g3);
		scheduler.dispatch(message2g1);
		scheduler.dispatch(message4g3);
		message1g2.completed();
		scheduler.dispatch(message3g2);
		message5g3.completed();
		message3g2.completed();
		message4g3.completed();
		message2g1.completed();
	}
	
	public static void test7() {
		GatewayExample gateway=new GatewayExample();
		Scheduler scheduler=new Scheduler(4,gateway);
		
		MessageExample message1g2=new MessageExample("group2","message 1"); 
		MessageExample message2g1=new MessageExample("group1","message 2");
		MessageExample message3g2=new MessageExample("group2","message 3");
		MessageExample message4g3=new MessageExample("group3","message 4");
		MessageExample message5g3=new MessageExample("group3","message 5");
		
		
		Logger.log("\n");
		Logger.log("\n");
		Logger.log("Example 7 --------------------------------------------");
		Logger.log("Test Case:'If there are messages belonging to multiple groups in the queue, as resources become available, we want to prioritise messages from groups already started. The priority in which to process groups is defined by the order in which you receive the first message from the group. But we don't want resources to be idle while there are messages in the queue'");
		Logger.log("In this case,there are four resources and we don't want resources to be idle");  
		Logger.log("So de order should be 1,5,2,4,3");
		Logger.log("\n");
		
		scheduler.dispatch(message1g2);
		scheduler.dispatch(message5g3);
		scheduler.dispatch(message2g1);
		scheduler.dispatch(message4g3);
		scheduler.dispatch(message3g2);
		message1g2.completed();
		message5g3.completed();
		message2g1.completed();
		message4g3.completed();
		message3g2.completed();
		
	}
	
	public static void test8() {
		GatewayExample gateway=new GatewayExample();
		Scheduler scheduler=new Scheduler(1,gateway);
		
		MessageExample message1g2=new MessageExample("group2","message 1"); 
		MessageExample message2g1=new MessageExample("group1","message 2");
		MessageExample message3g2=new MessageExample("group2","message 3");
		MessageExample message4g3=new MessageExample("group3","message 4");
		
		Logger.log("\n");
		Logger.log("\n");
		Logger.log("Example 8 --------------------------------------------");
		Logger.log("Test Case:'It should be possible to tell the scheduler that a group of messages has now been cancelled. Once cancelled, no further messages from that group should sent to the Gateway.'");
		Logger.log("This is the first test. The example in the excercise");
		Logger.log("But group2 is cancelled after sending message 1 so, message 3 should not be processed");
		Logger.log("So de order should be 1,2,4");
		Logger.log("\n");
		scheduler.dispatch(message1g2);
		scheduler.cancelGroup("group2");
		scheduler.dispatch(message2g1);
		scheduler.dispatch(message3g2);
		scheduler.dispatch(message4g3);
		message1g2.completed();
		message2g1.completed();
		message4g3.completed();
		
	}
	
	public static void main(String[] args) {
		
			// I use a method for each test. 
			// I think this is usually better practice to isolate tests and states are not mixed between tests 
			
			test1();
			
			test2();
			
			test3();
			
			test4();
			
			test5();
			
			test6();
			
			test7();
			
			test8();
		
		
	}

}
