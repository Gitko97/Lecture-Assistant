package Sound;

public class Main {
	public static void main(String[] args) {
		CallAPI callAPI = new CallAPI("C:\\Users\\xcvds\\eclipse-workspace\\SoundRecord\\key\\key.json");
		Thread API = new Thread(callAPI);
		API.run();
	}
}
