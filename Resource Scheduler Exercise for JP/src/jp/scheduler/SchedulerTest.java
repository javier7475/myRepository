package jp.scheduler;

import junit.framework.TestCase;

public class SchedulerTest extends TestCase {

	public static void test1() {
		MyLogger.getLog().debug("\nTest 1");
		
		GatewayExample gateway=new GatewayExample();
		Scheduler scheduler=new Scheduler(1,gateway);

		MessageExample message1=new MessageExample("group1","message 1"); 
		MessageExample message2=new MessageExample("group1","message 2");
		MessageExample message3=new MessageExample("group1","message 3");
		
		MessageExample current=scheduler.dispatch(message1);
		assertNotNull(current);
		assertEquals(current.getMessage(), "message 1");
		
		current=scheduler.completed(message1);
		assertNull(current);
		
		current=scheduler.dispatch(message2);
		assertNotNull(current);
		assertEquals(current.getMessage(), "message 2");
		
		current=scheduler.dispatch(message3);
		assertNull(current);
	}
	
	public static void test2() {
		MyLogger.getLog().debug("\nTest 2");
		GatewayExample gateway=new GatewayExample();
		Scheduler scheduler=new Scheduler(2,gateway);

		MessageExample message1=new MessageExample("group1","message 1"); 
		MessageExample message2=new MessageExample("group1","message 2");
		
		MessageExample current=scheduler.dispatch(message1);
		assertNotNull(current);
		assertEquals(current.getMessage(), "message 1");
		
		current=scheduler.dispatch(message2);
		assertNotNull(current);
		assertEquals(current.getMessage(), "message 2");
	}


	public static void test3() {
		MyLogger.getLog().debug("\nTest 3");
		GatewayExample gateway=new GatewayExample();
		Scheduler scheduler=new Scheduler(1,gateway);

		MessageExample message1=new MessageExample("group1","message 1"); 
		MessageExample message2=new MessageExample("group1","message 2");
		MessageExample message3=new MessageExample("group1","message 3");
		
		MessageExample current=scheduler.dispatch(message1);
		assertNotNull(current);
		assertEquals(current.getMessage(), "message 1");
		
		current=scheduler.completed(message1);
		assertNull(current);
		
		current=scheduler.dispatch(message2);
		assertNotNull(current);
		assertEquals(current.getMessage(), "message 2");
		
		current=scheduler.dispatch(message3);
		assertNull(current);
		
		current=scheduler.completed(message2);
		assertNotNull(current);
		assertEquals(current.getMessage(), "message 3");
	}

	public static void test4() {
		MyLogger.getLog().debug("\nTest 4");
		GatewayExample gateway=new GatewayExample();
		Scheduler scheduler=new Scheduler(1,gateway);

		MessageExample message1=new MessageExample("group2","message 1"); 
		MessageExample message2=new MessageExample("group1","message 2");
		MessageExample message3=new MessageExample("group2","message 3");

		MessageExample current=scheduler.dispatch(message1);
		assertNotNull(current);
		assertEquals(current.getMessage(), "message 1");
		
		current=scheduler.dispatch(message2);
		assertNull(current);

		current=scheduler.dispatch(message3);
		assertNull(current);
		
		current=scheduler.completed(message1);
		assertNotNull(current);
		assertEquals(current.getMessage(), "message 3");
		
		current=scheduler.completed(message3);
		assertNotNull(current);
		assertEquals(current.getMessage(), "message 2");
		
		current=scheduler.completed(message2);
		assertNull(current);
		
		

	}
	
	public static void test5() {
		MyLogger.getLog().debug("\nTest 5");
		GatewayExample gateway=new GatewayExample();
		Scheduler scheduler=new Scheduler(2,gateway);
		
		MessageExample message1=new MessageExample("group2","message 1"); 
		MessageExample message2=new MessageExample("group1","message 2");
		MessageExample message3=new MessageExample("group2","message 3");

		MessageExample current=scheduler.dispatch(message1);
		assertNotNull(current);
		assertEquals(current.getMessage(), "message 1");
		
		current=scheduler.dispatch(message2);
		assertNotNull(current);
		assertEquals(current.getMessage(), "message 2");
		
		current=scheduler.dispatch(message3);
		assertNull(current);
		
		current=scheduler.completed(message1);
		assertNotNull(current);
		assertEquals(current.getMessage(), "message 3");
		
		current=scheduler.completed(message2);
		assertNull(current);
		
		current=scheduler.completed(message3);
		assertNull(current);
	}
	
	
	public static void test6() {
		MyLogger.getLog().debug("\nTest 6");
		GatewayExample gateway=new GatewayExample();
		Scheduler scheduler=new Scheduler(1,gateway);
		
		MessageExample message1=new MessageExample("group1","message 1"); 
		MessageExample message2=new MessageExample("group2","message 2");
		MessageExample message3=new MessageExample("group1","message 3");
		MessageExample message4=new MessageExample("group2","message 4");
		MessageExample message5=new MessageExample("group1","message 5");
		
		MessageExample current=scheduler.dispatch(message1);
		assertNotNull(current);
		assertEquals(current.getMessage(), "message 1");
		
		current=scheduler.completed(message1);
		assertNull(current);

		current=scheduler.dispatch(message2);
		assertNotNull(current);
		assertEquals(current.getMessage(), "message 2");
		
		current=scheduler.dispatch(message3);
		assertNull(current);
		
		current=scheduler.dispatch(message4);
		assertNull(current);

		current=scheduler.dispatch(message5);
		assertNull(current);

		current=scheduler.completed(message2);
		assertNotNull(current);
		assertEquals(current.getMessage(), "message 3");
		
		current=scheduler.completed(message3);
		assertNotNull(current);
		assertEquals(current.getMessage(), "message 5");
		
		current=scheduler.completed(message5);
		assertNotNull(current);
		assertEquals(current.getMessage(), "message 4");
		
	}

	public static void test7() {
		MyLogger.getLog().debug("\nTest 7");
		GatewayExample gateway=new GatewayExample();
		Scheduler scheduler=new Scheduler(1,gateway);
		
		MessageExample message1=new MessageExample("group2","message 1"); 
		MessageExample message2=new MessageExample("group1","message 2");
		MessageExample message3=new MessageExample("group2","message 3");
		MessageExample message4=new MessageExample("group3","message 4");

		MessageExample current=scheduler.dispatch(message1);
		assertNotNull(current);
		assertEquals(current.getMessage(), "message 1");

		scheduler.cancelGroup("group2");
		
		current=scheduler.dispatch(message2);
		assertNull(current);
		
		current=scheduler.dispatch(message3);
		assertNull(current);
		
		current=scheduler.dispatch(message4);
		assertNull(current);
		
		current=scheduler.completed(message1);
		assertNotNull(current);
		assertEquals(current.getMessage(), "message 2");
		
		current=scheduler.completed(message2);
		assertNotNull(current);
		assertEquals(current.getMessage(), "message 4");
		
		current=scheduler.completed(message4);
		assertNull(current);
	}

}
