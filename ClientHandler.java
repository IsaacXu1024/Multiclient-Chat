//This is a support class that extends Thread, runs the client thread
//and sends and receives messages
import java.io.*;
import java.net.*;
import java.util.*;
public class ClientHandler extends Thread{
	private Socket client;
	private BufferedReader in;
	private PrintWriter out;
	// Hashtable for name and printer writer
	private static Hashtable<String, PrintWriter> writers = new Hashtable<>();
	// For client ID and message - I didn't find this one all that useful
	private static Hashtable<Integer, String> client_names = new Hashtable<>();
	// For client name and ID
	private static Hashtable<Integer, String> client_map = new Hashtable<>();
	public ClientHandler(Socket socket){
		client = socket;
		try{
			in = new BufferedReader(new InputStreamReader(client.getInputStream())); // in is input from client
			out = new PrintWriter(client.getOutputStream(), true); // out goes to client
		}
		catch(IOException e){
			e.printStackTrace();
		}
	}
	public void run(){
		try{
			int counter = 0;
			int id;
			PrintWriter writer;
			String broadcast_message;
			String name;
			String received;
			do{
				received = in.readLine();
				id = client.getPort();
				client_names.put(id, received);
				if (counter == 0) { // first message
					name = received;
					writer = out;
					writers.put(name, writer);
					client_map.put(id, name);
					broadcast_message = name+" has joined";
					}
				else {
					name = client_map.get(id); // gets name from id - id is also port number
					broadcast_message = "Message from "+name+": "+received;
				}
				System.out.println(broadcast_message);
				Set<String> names = writers.keySet();
				for (String key_name: names) { // loops through hashtable of clients and writers
					if (!key_name.equals(name)) { // if the name is the same as the client that just sent the message, no message is sent
						writers.get(key_name).println(broadcast_message);
					}
				}
				if (received.equals("BYE")) { // remove the client from the hashtable if they are leaving
					writers.remove(name);
				}
				counter++;
			} while (!received.equals("BYE"));
		}
		catch(IOException e){
			e.printStackTrace();
		}
		finally{
			try{
				if(client!=null){
					System.out.println("Closing down connection");
					client.close();
				}
			}
			catch(IOException e){
				e.printStackTrace();
			}
		}
	}//end run
}//end ClientHandler class
