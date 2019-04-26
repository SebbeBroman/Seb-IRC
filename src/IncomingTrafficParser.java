/*
import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Scanner;

public class IncomingTrafficParser implements Runnable
{
    private static PrintWriter out;
    private static Scanner in;
    private String nick;
    private String username;
    private ClientGui gui;
    private Socket socket;

    public IncomingTrafficParser(ClientGui gui, String server, int port, String nickname, String username, String realName) throws IOException {
        this.nick = nickname;
        this.gui = gui;
	socket = new Socket(server, port);
	out = new PrintWriter(socket.getOutputStream(), true);
	in = new Scanner(socket.getInputStream());
	sendServer("NICK", nick);
	sendServer("USER", username+ " 0 * :"+  realName);
    }

    @Override public void run() {
	parseIncoming();
    }

    private void parseIncoming() {
	while(in.hasNext()) {
		    String serverMessage = in.nextLine();
		    List<String> messageList = new ArrayList<String>(Arrays.asList(serverMessage.split(" ")));
		    String sender = serverMessage.split("!")[0].substring(1);
		    if (messageList.get(0).equals("PING")) {
			sendServer("PONG", messageList.get(1));
		    }
		    else{
			switch (messageList.get(1)) {
			    case "376": //END of MOTD, time to join channel
				sendServer("JOIN", "#botters-test");
			    case "433": //Nickname taken
			        writeToScreen("Nickname taken!","");
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

    private void writeToScreen(final String message, final String channel) {
	//Update this with gui
	//System.out.println("From " + channel);
	gui.infoToScreen(message);
	System.out.println("[" + new SimpleDateFormat("HH:mm").format(new Date()) + "] " + message);
    }

    public void sendServer(final String command, final String parameters) {
	String fullMessage = command + " " + parameters;
	out.print(fullMessage + "\r\n");
	out.flush();
    }


}
*/