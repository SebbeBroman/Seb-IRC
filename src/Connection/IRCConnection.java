package Connection;

import GUI.ClientGui;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

/**
* This class creates a connection to a server and handles the outgoing traffic to the server.
* It starts a new thread that parse incoming data from server.
*/
public class IRCConnection
{
    private static PrintWriter out = null;
    private static Scanner inputStream = null;
    private static Socket socket = null;
    private IncomingTrafficParser incomingParser;
    private final ClientGui gui;

    public IRCConnection(final ClientGui gui, final String serverName, final int port, final String nickname, final String username,
		  final String realName) throws IOException
    {
        //initialize the class
	this.gui = gui;
	createConnection(serverName,port, nickname, username, realName);
    }

    private void createConnection(String serverName, int port, String nickname, String username, String realName) throws IOException
    {
        //created the connection with given variables
	socket = new Socket(serverName, port);
	out = new PrintWriter(socket.getOutputStream(), true);
	inputStream = new Scanner(socket.getInputStream());
	sendServer("NICK " + nickname);
	sendServer("USER "+username+ " 0 * :"+  realName);
	incomingParser = new IncomingTrafficParser(out, inputStream, nickname, gui);
	new Thread(incomingParser).start();
    }

    public void disconnect()throws IOException{
        //Terminates connection
        socket.close();
        out.close();
        inputStream.close();
    }

    private void sendServer(String message){
        //Sends a message to the server, also prints the line to console for debugging.
	System.out.println(message);
	out.print(message+ "\r\n");
	out.flush();
    }

    public void inputHandler(String current, String fromUser){
        //Handles all input that goes from the user to the server.
        String input = fromUser.toLowerCase(); // to prevent mistakes with lower and uppercase letters.
	List<String> messageList = new ArrayList<>(Arrays.asList(input.split(" ")));
	String command= input.split(" ")[0];
	if(input.startsWith("/")){
	    //If the string starts with "/" we can treat it as a command.
	    switch(command){
		case "/msg": // The message command.
		    sendServer("PRIVMSG "+ messageList.get(1)+ " :" + getRest(messageList, 2));
		    break;
		case "/join": // Join a channel
		    if (messageList.size() > 1){
			sendServer("JOIN "+ messageList.get(1));
		    } else{ // if the input misses argument we prompt the user to use it correctly.
		        gui.infoToScreen(current, "USAGE /JOIN #channel");
		    }
		    break;
		case "/part": // part a channel
		    if (messageList.size() < 2){
		        sendServer("PART "+current); // if no channel is specified part current
		    }
		    else{ // else part specified
			sendServer("PART "+ messageList.get(1));
		    }
		    break;
		case "/close": // close current tab
		    gui.closeTab(current);
		    break;
		case "/names":// list names of everyone on server
		    if (messageList.size() < 2){ // if none is specified list current
		        sendServer("NAMES "+current);
		    }
		    else{ // else list specified.
			sendServer("NAMES "+ messageList.get(1));
		    }
		    break;
		case "/away":// set away status
		    if (messageList.size() < 2){ // Away with no message removes away status
		        sendServer("AWAY");
		    }else{ // sets away status with specified message
			sendServer("AWAY :" + messageList.get(1));
		    }
		    break;
		default://to support all commands we add this
		    // sends the input to the server without the /.
		    // this may require knowledge about IRC-commands.
		    // however the server will respond if the command is
		    // unavailable or doesn't exist.
		    sendServer(input.substring(1));
		    break;
	    }
	} else {
	    //If there is no "/" the server will treat input as message to current.
	    sendServer("PRIVMSG "+ current + " :" + input);
	}
    }

    private String getRest(List<String> strings, int index){ // index good to be able to reuse code
        //Simply returns a concatenated string from a list of strings where all items
	//before the index will be discarded.
        StringBuilder builder = new StringBuilder();
	for (int i = index; i < strings.size(); i++) {
	    builder.append(strings.get(i));
	}
	return builder.toString();
    }

    public void setCurrentChannel(String channel){
        incomingParser.setCurrentChannel(channel);
    }
}
