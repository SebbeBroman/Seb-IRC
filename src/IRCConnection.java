import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

class IRCConnection
{
    private static PrintWriter out = null;
    private static Scanner inputStream = null;
    private static Socket socket = null;
    private IncomingTrafficParser incomingParser;
    private final ClientGui gui;

    IRCConnection(final ClientGui gui, final String serverName, final int port, final String nickname, final String username,
		  final String realName) throws IOException
    {
	this.gui = gui;
	createConnection(serverName,port, nickname, username, realName);
    }

    private void createConnection(String serverName, int port, String nickname, String username, String realName) throws IOException{
	socket = new Socket(serverName, port);
	out = new PrintWriter(socket.getOutputStream(), true);
	inputStream = new Scanner(socket.getInputStream());
	sendServer("NICK " + nickname);
	sendServer("USER "+username+ " 0 * :"+  realName);
	incomingParser = new IncomingTrafficParser(out, inputStream, nickname, gui);
	new Thread(incomingParser).start();
    }

    void disconnect()throws IOException{
        socket.close();
        out.close();
        inputStream.close();
    }

    private void sendServer(String message){
	System.out.println(message);
	out.print(message+ "\r\n");
	out.flush();
    }

    void inputHandler(String current, String fromUser){
        String input = fromUser.toLowerCase();
	List<String> messageList = new ArrayList<>(Arrays.asList(input.split(" ")));
	String command= input.split(" ")[0];
	if(input.startsWith("/")){
	    switch(command){
		case "/msg":
		    sendServer("PRIVMSG "+ messageList.get(1)+ " :" + getRest(messageList, 2));
		    break;
		case "/join":
		    if (messageList.size() > 1){
			sendServer("JOIN "+ messageList.get(1));
		    } else{
		        gui.infoToScreen(current, "USAGE /JOIN #channel");
		    }
		    break;
		case "/part":
		    if (messageList.size() < 2){
		        sendServer("PART "+current);
		    }
		    else{
			sendServer("PART "+ messageList.get(1));
		    }
		    break;
		case "/close":
		    gui.closeTab(current);
		    break;
		case "/names":
		    if (messageList.size() < 2){
		        sendServer("NAMES "+current);
		    }
		    else{
			sendServer("NAMES "+ messageList.get(1));
		    }
		    break;
		case "/away":
		    if (messageList.size() < 2){
		        sendServer("AWAY");
		    }else{
			sendServer("AWAY :" + messageList.get(1));
		    }
		    break;
		default:
		    sendServer(input.substring(1));
		    break;
	    }
	} else {
	    sendServer("PRIVMSG "+ current + " :" + input);
	}
    }

    private String getRest(List<String> strings, int index){ // index good to be able to reuse code
        StringBuilder builder = new StringBuilder();
	for (int i = index; i < strings.size(); i++) {
	    builder.append(strings.get(i));
	}
	return builder.toString();
    }


    void setCurrentChannel(String channel){
        incomingParser.setCurrentChannel(channel);
    }
}
