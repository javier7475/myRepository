// The message example
// We use the scheduler no notify when the message processing is complete
public class MessageExample implements Message {
	
		private Scheduler scheduler=null;
		public Scheduler getScheduler() {
			return scheduler;
		}

		public void setScheduler(Scheduler scheduler) {
			this.scheduler = scheduler;
		}

		private String groupId=null;
		private String message=null;
		
		public String getMessage() {
			return message;
		}

		public void setMessage(String message) {
			this.message = message;
		}

		public String getGroupId() {
			return groupId;
		}

		public void setGroupId(String groupId) {
			this.groupId = groupId;
		}

		private MessageExample(){
			super();
		}
		
		public MessageExample(String groupId,String message) {
			super();
			this.groupId=groupId;
			this.message=message;
		}
		
		public void completed() {
			//  notify the scheduler	
			if (scheduler!=null)
				scheduler.completed(this);
		}
}
