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
class IncomingTrafficParser implements Runnable
{
    private final PrintWriter out;
    private final Scanner in;
    private final String nick;
    private final ClientGui gui;

    IncomingTrafficParser(final PrintWriter out, final Scanner in, final String nick, final ClientGui gui) {
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
		switch (messageList.get(1)) {
		    case "376": //END of MOTD, now you can start to join channels.
			//sendServer("JOIN", "#botters-test");
			break;
		    case "332":
			// Server sends topic of channel, message comes in format ":server 332 username channel :topic", using substring to get second ":"
			gui.setTopicOfChannel(messageList.get(3), serverMessage.substring(serverMessage.substring(1).indexOf(":")+1));
			break;
		    case "330":
			// info about user
			writeToScreen(serverMessage.substring(1).substring(serverMessage.substring(1).indexOf(":")).substring(1)+
				      " " + messageList.get(3), messageList.get(3));
			break;
		    case "353": //Server sends a list of users.
			String userString = serverMessage.split(":")[2];
			List<String> users = new ArrayList<>(Arrays.asList(userString.split(" ")));
			for(String user : users){
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
			writeToScreen(sender + " joined " , messageList.get(2));
			gui.addUser(sender,messageList.get(2));
			gui.updateUsers();
			break;
		    case "PART": // someone left a channel.
			writeToScreen(sender + " parted ", messageList.get(2));
			gui.disconnectUser(sender);
			gui.updateUsers();
			break;
		    case "QUIT": // someone quit. If this is user, exit client.
			writeToScreen(sender + " has quit ", messageList.get(2));
			gui.disconnectUser(sender);
			gui.updateUsers();
			if(sender.equals(nick)){
			    System.exit(0);
			}
			break;
		    case "PRIVMSG": // messages
			if (messageList.get(2).equals(nick)) {// PM to user
			    writeToScreen("<" + sender + "> " + serverMessage.split(":")[2], sender);
			}else{ // message to channel
			    writeToScreen("<" + sender + "> " + serverMessage.split(":")[2], messageList.get(2));
			}
			break;
		    default:
		        // if none of the above, print incoming in current window. This allows user to get relevant info.
			if (serverMessage.split(":").length > 2){
			    //serverMessage.substring(1).substring(serverMessage.substring(1).indexOf(":")).substring(1)
			    // doesnt exist in all cases so cant be a variable.
			    writeToScreen("- " + serverMessage.substring(1).substring(serverMessage.substring(1).indexOf(":")).substring(1)+ ": "+
					  IRCConnection.getRest(Arrays.asList((serverMessage.split(":")[1].split(" "))), 3),
					  gui.getCurrentTab());

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
