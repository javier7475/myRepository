package jp.scheduler;

import java.util.LinkedList;
import java.util.Queue;

public class GroupInfo implements Comparable<GroupInfo> {

	private Integer order=new Integer(0);

	private GroupInfo() {
	}
	
	public GroupInfo(int order) {
		super();
		this.order=new Integer(order);
	}
	
	private Queue<MessageExample> queuedMessageExamples=new LinkedList<MessageExample>(); 
	
	public void queueMessage(MessageExample m) {
		queuedMessageExamples.add(m);
	}
	
	public int size() {
		return queuedMessageExamples.size();
	}
	
	public MessageExample pollMessage() {
		return queuedMessageExamples.poll();
	}
	
	public int compareTo(GroupInfo other) {
		return this.order.compareTo(other.order);
	}
	
	public int getOrder() {
		return order.intValue();
	}
}
