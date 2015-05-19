// The gateway example
public class GatewayExample implements Gateway {
	public void send(Message message) {
		Logger.log("Processing message "+message.getMessage());
	}
	
}
