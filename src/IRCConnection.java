import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

public class IRCConnection
{
    private String serverName;
    private int port;
    private String nickname;
    private String username;
    private String realName;
    private static PrintWriter out;
    private static Scanner in;
    private static Socket socket;
    private IncomingTrafficParser2 incomingParser;
    private ClientGui gui;

    public IRCConnection(final ClientGui gui, final String serverName, final int port, final String nickname, final String username,
			 final String realName) throws IOException
    {
	this.serverName = serverName;
	this.port = port;
	this.nickname = nickname;
	this.username = username;
	this.realName = realName;
	this.gui = gui;
	socket = new Socket(serverName, port);
	out = new PrintWriter(socket.getOutputStream(), true);
	in = new Scanner(socket.getInputStream());
	sendServer("NICK " + nickname);
	sendServer("USER "+username+ " 0 * :"+  realName);
	incomingParser = new IncomingTrafficParser2(out, in, nickname, gui);
	new Thread(incomingParser).start();
    }

    public static PrintWriter getOut() {
	return out;
    }

    public static Scanner getIn() {
	return in;
    }

    public String getServerName() {
	return serverName;
    }

    public String getNickname() {
	return nickname;
    }


    public void disconnect()throws IOException{
        socket.close();
        out.close();
        in.close();
    }

    private void sendServer(String message){
	System.out.println(message);
	out.print(message+ "\r\n");
	out.flush();
    }

    public void inputHandler(String current, String input){
	List<String> messageList = new ArrayList<String>(Arrays.asList(input.split(" ")));
	String command= input.split(" ")[0];
	if(input.startsWith("/")){
	    switch(command){
		case "/msg":
		    sendServer("PRIVMSG "+ messageList.get(1)+ " :" + getRest(messageList, 2));
		    break;
		case "/join":
		    sendServer("JOIN "+ messageList.get(1));
		    break;
		case "/part":
		    sendServer("PART "+ messageList.get(1));
		case "/close":
		    gui.closeTab(current);
		    break;
		case "/names":
		    sendServer("NAMES " + messageList.get(1));
		    break;
		default:
		    sendServer(input.substring(1));
		    break;
	    }
	} else {
	    sendServer("PRIVMSG "+ current + " :" + input);
	}
    }

    private String getRest(List<String> strings, int index){
        StringBuilder builder = new StringBuilder();
	for (int i = index; i < strings.size(); i++) {
	    builder.append(strings.get(i));
	}
	return builder.toString();
    }
}
