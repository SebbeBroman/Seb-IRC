import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

class IncomingTrafficParser implements Runnable
{
    private final PrintWriter out;
    private final Scanner in;
    private final String nick;
    private final ClientGui gui;
    private String currentChannel;

    void setCurrentChannel(final String currentChannel) {
	this.currentChannel = currentChannel;
    }

    IncomingTrafficParser(final PrintWriter out, final Scanner in, final String nick, final ClientGui gui) {
	this.out = out;
	this.in = in;
	this.nick = nick;
	this.gui = gui;
	currentChannel = "Standard";
    }

    @Override public void run() {
	parseIncoming();
    }

    private void parseIncoming() {
	while(in.hasNext()) {
	    String serverMessage = in.nextLine();
	    System.out.println(serverMessage);
	    List<String> messageList = new ArrayList<>(Arrays.asList(serverMessage.split(" ")));
	    String sender = serverMessage.split("!")[0].substring(1);
	    if (messageList.get(0).equals("PING")) {
		sendServer("PONG", messageList.get(1));
	    }
	    else{
		switch (messageList.get(1)) {
		    case "376": //END of MOTD, time to join channel
			sendServer("JOIN", "#botters-test");
			break;
		    case "332":
			gui.setTopicOfChannel(messageList.get(3), serverMessage.substring(serverMessage.substring(1).indexOf(":")+1));
			//set topic of channel, message comes in format ":server 332 username channel :topic", using substring to get second ":"
			break;
		    case "353": //List of users
			String userString = serverMessage.split(":")[2];
			List<String> users = new ArrayList<>(Arrays.asList(userString.split(" ")));
			for(String user : users){
			    gui.addUser(user, messageList.get(4));
			}
			gui.updateUsers();
			break;
		    case "366":
			System.out.println("end of names list");
			break;
		    case "433": // nickname taken
			writeToScreen("Nickname taken!","");
			System.out.println("Nickname taken!");
			break;
		    case "JOIN":
			writeToScreen(sender + " joined " , messageList.get(2));
			break;
		    case "PART":
			writeToScreen(sender + " parted ", messageList.get(2));
			gui.disconnectUser(sender);
			break;
		    case "QUIT":
			writeToScreen(sender + " has quit ", messageList.get(2));
			gui.disconnectUser(sender);
			if(sender.equals(nick)){
			    System.exit(0);
			}
			break;
		    case "PRIVMSG":
			//UPDATE THIS
			if (messageList.get(2).equals(nick)) {
			    writeToScreen("<" + sender + "> " + serverMessage.split(":")[2], sender);
			}else{
			    writeToScreen("<" + sender + "> " + serverMessage.split(":")[2], messageList.get(2));
			}
			break;
		    default:
			if (serverMessage.split(":").length > 2){
			    writeToScreen("- "+serverMessage.substring(1).substring(serverMessage.substring(1).indexOf(":")).substring(1), currentChannel);

			}
		}
	    }
	}
    }

    private void writeToScreen(final String message, final String channel) {
	gui.infoToScreen(channel,message);
    }

    private void sendServer(final String command, final String parameters) {
	String fullMessage = command + " " + parameters;
	out.print(fullMessage + "\r\n");
	out.flush();
    }
}
