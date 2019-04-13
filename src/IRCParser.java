import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Scanner;

public class IRCParser
{
    private static PrintWriter out;
    private static Scanner in;
    private String nick = "SebbesClientBot";
    private String username = "SebbesClientBot";
    private String realname = "Sebastian b";

    public IRCParser(String server, int port) throws IOException {
	Socket socket = new Socket(server, port);
	out = new PrintWriter(socket.getOutputStream(), true);
	in = new Scanner(socket.getInputStream());
	//Scanner console = new Scanner(System.in);
	//System.out.println("Enter nick: ");
	//nick = console.nextLine();
	//System.out.println("Enter username: ");
	//username = console.nextLine();
	//System.out.println("Enter real name: ");
	//realname = console.nextLine();
	//console.close();
	sendServer("NICK", nick);
	sendServer("USER", username+ " 0 * :"+  realname);
	parseConnection();
    }

    void parseConnection(){
	while(in.hasNext()) {
	    String serverMessage = in.nextLine();
	    System.out.println("<<< " + serverMessage);
	    List<String> messageList = new ArrayList<String>(Arrays.asList(serverMessage.split(" ")));
	    String sender = serverMessage.split("!")[0].substring(1);
	    if (messageList.get(0).equals("PING")) {
		sendServer("PONG", messageList.get(1));
	    }
	    else{
		switch (messageList.get(1)) {
		    case "376": //END of MOTD, time to join channel
			sendServer("JOIN", "#botters-test");
			Runnable send = new Send(out);
			new Thread(send).start();
			break;
		    case "433":
			System.out.println("Nickname taken!");
			break;
		    case "JOIN":
			writeToScreen(sender + " joined " , messageList.get(2));
			break;
		    case "PART":
			writeToScreen(sender + " parted ", messageList.get(2));
			break;
		    case "QUIT":
			writeToScreen(sender + " has quit ", messageList.get(2));
			if(sender.equals(nick)){
			    System.exit(0);
			}
			break;
		    case "PRIVMSG":
			//UPDATE THIS
			writeToScreen("<"+sender + "> "+ serverMessage.split(":")[2],
				      messageList.get(2));
			break;
		    default:
		        if (serverMessage.split(":").length > 2){
			    writeToScreen("- "+serverMessage.substring(1).substring(serverMessage.substring(1).indexOf(":")).substring(1), messageList.get(2));

			}
		}
	    }
	}
    }

    private void writeToScreen(String message, final String toServer) {
    	//Update this with gui
	//System.out.println("From " + toServer);
	System.out.println("["+new SimpleDateFormat("HH:mm").format(new Date())+"] "+ message);
    }

    public void sendServer(String command, String message) {
	String fullMessage = command + " " + message;
	//System.out.println(">>> " + fullMessage);
	writeToScreen(fullMessage,"Connection");
	out.print(fullMessage + "\r\n");
	out.flush();
    }
}
