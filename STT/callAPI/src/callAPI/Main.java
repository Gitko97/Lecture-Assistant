package callAPI;

public class Main {
	public static void main(String[] args) {
		CallAPI callAPI = new CallAPI("key\\key.json");
		Thread API = new Thread(callAPI);
		API.run();
		
	}
}
