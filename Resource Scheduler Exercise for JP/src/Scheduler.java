import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.NoSuchElementException;


public class Scheduler {
	
	// we need to know the number of available resources. If there are no resources available,
	// messages should not be send
	private long resourcesAvailableCount=1;
	
	private Gateway gateway=null;
	
	// to keep queued messages
	private LinkedList<MessageExample> queuedMessages=new LinkedList<MessageExample>();
	
	// we need to keep the groups order. We'll use a HashMap indexed by the group name to be efficient
	private HashMap<String,Integer> groupOrder=new HashMap<String,Integer>();
	
	// to keep the groups cancelled. We'll use a HashMap indexed by the group name to be efficient
	private HashMap<String,String> groupsCancelled=new HashMap<String,String>();
	
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

	protected synchronized void removeAvailableResource() {
		// to keep count of available resources;
		resourcesAvailableCount--;
	}
	
	protected long getAvailableResourceCount() {
		// to keep count of available resources;
		return resourcesAvailableCount;
	}
	
	protected void queueMessage(MessageExample message) {
		// This method queues a Message
		// The queuing algorithm can be change by overriding this method and getNextQueuedMessage
		// This method inserts a message in the queue taking into account the group order
		Iterator<MessageExample> itr=queuedMessages.iterator();
		int i=0;
		boolean inserted=false;
		// of course, the algorithm here could be something better like a binary search but
		// I'll make it simple as I guess doing a binary search is not the aim of the exercise
		while (itr.hasNext() && !inserted) {
			MessageExample currentMessage=itr.next();
			if (getGroupOrder(currentMessage.getGroupId())>getGroupOrder(message.getGroupId())) {
				queuedMessages.add(i,message);
				inserted=true;
			}	
			i++;
		}
		if (!inserted)
		 queuedMessages.addLast(message);
	}
	
	
	protected void addGroup(String groupId) {
		// We need to know the group order which is defined by the order of first message of the group  
		Integer order=groupOrder.get(groupId);
		if (order==null)
			groupOrder.put(groupId,new Integer(groupOrder.size()+1));
	}
	
	protected int getGroupOrder(String groupId) {
		return groupOrder.get(groupId).intValue();
	}
	
	protected MessageExample getNextQueuedMessage() {
		// This method gets next message from the queue
		// The queuing algorithm can be change by overriding this method and queueMessage
		MessageExample nextMessage=queuedMessages.remove();
		return nextMessage;
	}
	
	public void dispatch(MessageExample message) {
		
		// tell the message the scheduler to notify when finished
		message.setScheduler(this);
		
		// If the group is cancelled, we don't do anything
		if (!isCancelledGroup(message.getGroupId())) {
			// We need to know the group order
			addGroup(message.getGroupId());
			
			if (getAvailableResourceCount()>0) {
				// If there are available resources, we send the message as we don't want resources to be idle
				removeAvailableResource();
				gateway.send(message);
			} else {
				// if there are not available resources, queue the message
				queueMessage(message);
			}
		}
	}
	
	public void completed(MessageExample message) {
		// to notify a Message has been processed
		// so there is a new available resource and we must get the next message from the queue
		addAvailableResource();
		try {
			MessageExample nextMessage=getNextQueuedMessage();
			if (nextMessage!=null) {
				dispatch(nextMessage);
			}
		} catch (NoSuchElementException e) {
			
		}
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
