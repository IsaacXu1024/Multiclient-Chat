import java.io.*;
import java.net.*;
import java.util.*;
public class ChatClient{
	private static final int PORT = 1234;
	private static Socket link;
	private static BufferedReader in;
	private static PrintWriter out;
	private static Scanner kbd;
	
	public static Thread outputThread = new Thread() { // New thread to prevent blocking between two BufferedReader/Scanners
		public void run() {
			try{
				in = new BufferedReader(new InputStreamReader(link.getInputStream()));
				String response;
				int chat_ongoing = 1;
				while (chat_ongoing == 1) {
							response = in.readLine();
							System.out.println("\n"+response);
							System.out.print("Enter message (BYE to exit): ");
						}
			}
			catch(UnknownHostException e){System.exit(1);}
			catch(IOException e){System.exit(1);}
		}
	};

	public static void main(String[] args) throws Exception{
		try{
			link = new Socket("127.0.0.1", PORT);
			in = new BufferedReader(new InputStreamReader(link.getInputStream()));
			out = new PrintWriter(link.getOutputStream(), true);
			kbd = new Scanner(System.in); // I switched this to a Scanner during experimentation, I don't have a good reason to be honest, but it still works!
			int counter = 0;
			String message = "";
			do{
				if (counter == 0){ // First message
					System.out.print("Enter name: ");
					message = kbd.nextLine();
					out.println(message);
					counter++;
					outputThread.start(); // starts the thread that outputs messages from the server
				}
				else {	
					System.out.print("Enter message (BYE to exit): ");
					message = kbd.nextLine(); // standard get keyboard input, etc
					if (!message.equals("")) { // in case of accidental enters, it isn't sent to the server
						out.println(message);
						counter++;
					}
				}
			}while (!message.equals("BYE"));
		}
		catch(UnknownHostException e){System.exit(1);}
		catch(IOException e){System.exit(1);}
		finally{
			try{
				if (link!=null){
					System.out.print("Enter message (BYE to exit): Connection shut down");
					link.close();
				}
			}
			catch(IOException e){System.exit(1);}
		}
	}//end main
}//end class MultiEchoClient
	
	
