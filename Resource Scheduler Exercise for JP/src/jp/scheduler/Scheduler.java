package jp.scheduler;

import java.util.HashMap;
import java.util.NoSuchElementException;
import java.util.TreeSet;


public class Scheduler {
	
	// we need to know the number of available resources. If there are no resources available,
	// messages should not be send
	private long resourcesAvailableCount=1;
	
	private Gateway gateway=null;
	
	// we need to keep the groups order. We'll use a HashMap indexed by the group name to be efficient
	private HashMap<String,GroupInfo> groups=new HashMap<String,GroupInfo>();
	
	// to keep the groups cancelled. We'll use a HashMap indexed by the group name to be efficient
	private HashMap<String,String> groupsCancelled=new HashMap<String,String>();
	
	private TreeSet<GroupInfo> queuedGroups=new TreeSet<GroupInfo>();
	
	private Scheduler() {
		// The constructor is private as the number of resources and the gateway are required
		super();
	}
	
	public Scheduler(long resourcesCount,Gateway gateway) {
		super();
		this.resourcesAvailableCount=resourcesCount;
		this.gateway=gateway;
	}
	
	protected synchronized void addAvailableResource() {
		// to keep count of available resources
		resourcesAvailableCount++;
	}

	protected synchronized boolean getAvailableResource() {
		// to keep count of available resources;
		if (resourcesAvailableCount>0) {
			resourcesAvailableCount--;
			return true;
		} else
			return false;
	}
	
	protected void queueMessage(MessageExample message) {
		GroupInfo group=groups.get(message.getGroupId());
		if (group.size()==0)
			queuedGroups.add(group);
		group.queueMessage(message);
	}
	
	protected void addGroup(String groupId) {
		// We need to know the group order which is defined by the order of first message of the group  
		GroupInfo group=groups.get(groupId);
		if (group==null) {
			groups.put(groupId,new GroupInfo(groups.size()+1));
		}
	}		
	
	protected MessageExample getNextQueuedMessage() throws NoSuchElementException {
		// This method gets next message from the queue
		// The queuing algorithm can be change by overriding this method and queueMessage
		GroupInfo firstGroup=queuedGroups.first();
		if (firstGroup==null)
			throw new NoSuchElementException();
		else {
			MessageExample next=firstGroup.pollMessage();
			if (firstGroup.size()==0)
				queuedGroups.pollFirst();
			return next;
		}
	}
	
	public MessageExample dispatch(MessageExample message) {
		// returns next sent message
		
		// tell the message the scheduler to notify when finished
		message.setScheduler(this);
		
		// If the group is cancelled, we don't do anything
		if (!isCancelledGroup(message.getGroupId())) {
			// We need to know the group order
			addGroup(message.getGroupId());
			
			if (getAvailableResource()) {
				// If there are available resources, we send the message as we don't want resources to be idle
				gateway.send(message);
				return message;
			} else {
				// if there are not available resources, queue the message
				queueMessage(message);
			}
		}
		return null;
	}
	
	public MessageExample completed(MessageExample message) {
		// returns next sent message
		// to notify a Message has been processed
		// so there is a new available resource and we must get the next message from the queue
		// returns next dispatched message
		addAvailableResource();
		try {
			MessageExample nextMessage=getNextQueuedMessage();
			if (nextMessage==null) {
				return null;
			} else {
				return dispatch(nextMessage);
			}
		} catch (NoSuchElementException e) {
			
		}
		
		return null;
	}
	
	public void cancelGroup(String groupId) {
		// Simple, to keep cancelled groups
		groupsCancelled.put(groupId, "This group has been cancelled...");
	}
	
	protected boolean isCancelledGroup(String groupId) {
		// Simple ...
		return groupsCancelled.get(groupId)!=null;
	}
	
	
}
