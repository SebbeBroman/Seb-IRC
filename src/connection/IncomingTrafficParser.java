package connection;

import gui.ClientGui;

import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;
/**
 * This is a runnable class that parse the incoming traffic from a scanner
 * and responds in an appropriate manner.
 *
 * The class will give the gui information to print.
 * It will also respond to IRC PINGs.
*/
public class IncomingTrafficParser implements Runnable
{
    private final PrintWriter out;
    private final Scanner in;
    private final String nick;
    private final ClientGui gui;

    protected IncomingTrafficParser(final PrintWriter out, final Scanner in, final String nick, final ClientGui gui) {
	this.out = out;
	this.in = in;
	this.nick = nick;
	this.gui = gui;
    }

    @Override public void run() {
	parseIncoming();
    }

    private void parseIncoming() {
        //The function that does the parsing.
	while(in.hasNext()) { //runs as long as there is a scanner to read from.
	    String serverMessage = in.nextLine();
	    System.out.println(serverMessage); // print all incoming for debugging.
	    List<String> messageList = new ArrayList<>(Arrays.asList(serverMessage.split(" ")));
	    String sender = serverMessage.split("!")[0].substring(1);
	    if (messageList.get(0).equals("PING")) {
	        // when the server send a ping the client must respond with pong
		// to not lose the connection.
		sendServer("PONG", messageList.get(1));
	    }
	    else{
	        String command = messageList.get(1);
	        String channel;
		String info = "";
		if (serverMessage.split(":").length > 2){ //make sure info exists
		    info = serverMessage.substring(1).substring(serverMessage.substring(1).indexOf(":")).substring(1);
		}
		switch (command) {
		    case "376": //END of MOTD, now you can start to join channels.
			//sendServer("JOIN", "#botters-test");
			break;
		    case "332":
			// Server sends topic of channel, message comes in format ":server 332 username channel :topic", using substring to get second ":"
			channel = messageList.get(3);
			String topic = serverMessage.substring(serverMessage.substring(1).indexOf(":")+1);
			gui.setTopicOfChannel(channel, topic);
			break;
		    case "330":
			// info about user
			String aUser = messageList.get(3);
			writeToScreen(info + " " + aUser, aUser); // info about aUser to aUser
			break;
		    case "353": //Server sends a list of users.
			String userString = serverMessage.split(":")[2];
			// splits incoming message into list of users
			List<String> users = new ArrayList<>(Arrays.asList(userString.split(" ")));
			for(String user : users){  // for each user
			    gui.addUser(user, messageList.get(4));
			}
			gui.updateUsers();
			break;
		    case "366": // we do not need to show this.
			System.out.println("end of names list");
			break;
		    case "433": // nickname taken
			writeToScreen("Nickname taken!, use /NICK nick",gui.getCurrentTab());
			System.out.println("Nickname taken!");
			break;
		    case "JOIN": // someone joined a channel.
			channel = messageList.get(2);
			writeToScreen(sender + " joined " , channel);
			gui.addUser(sender,channel);
			gui.updateUsers();
			break;
		    case "PART": // someone left a channel.
			channel = messageList.get(2);
			writeToScreen(sender + " parted ", channel);
			gui.disconnectUser(sender);
			gui.updateUsers();
			break;
		    case "QUIT": // someone quit. If this is user, exit client.
			channel = messageList.get(2);
			writeToScreen(sender + " has quit ", channel);
			gui.disconnectUser(sender);
			gui.updateUsers();
			if(sender.equals(nick)){
			    System.exit(0);
			}
			break;
		    case "PRIVMSG": // messages
			String privMessage = serverMessage.split(":")[2];
			if (messageList.get(2).equals(nick)) {// PM to user
			    writeToScreen("<" + sender + "> " + privMessage, sender);
			}else{ // message to channel
			    writeToScreen("<" + sender + "> " + privMessage, messageList.get(2));
			}
			break;
		    default:
		        // if none of the above, print incoming in current window. This allows user to get relevant info.
			if (serverMessage.split(":").length > 2){ //make sure it contains info relevant to user.
			    String prefixes = IRCConnection.getRest(Arrays.asList((serverMessage.split(":")[1].split(" "))), 3);
			    writeToScreen(prefixes +" "+ info, gui.getCurrentTab());

			}
		}
	    }
	}
    }

    private void writeToScreen(final String message, final String channel) {
        //promps the gui to put info in right place.
	gui.infoToScreen(channel,message);
    }

    private void sendServer(final String command, final String parameters) {
    	//Sends command to server, needed for PONG reply.
	String fullMessage = command + " " + parameters;
	out.print(fullMessage + "\r\n");
	out.flush();
    }
}
