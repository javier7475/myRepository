package jp.scheduler;

// The gateway example
public class GatewayExample implements Gateway {
	public void send(Message message) {
		MyLogger.getLog().debug("Sending message "+message);
	}
	
}
