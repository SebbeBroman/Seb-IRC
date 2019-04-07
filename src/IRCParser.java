import java.io.PrintWriter;
import java.util.Scanner;

public class IRCParser
{
    private static PrintWriter out;
    private static Scanner in;

    void parseConnection(PrintWriter out, Scanner in){
	while(in.hasNext()){
	    String serverMessage = in.nextLine();
	    System.out.println("<<< " + serverMessage);
	    if(serverMessage.startsWith("PING")){
	        String pingMessage = serverMessage.split(" ", 2)[1];
	        write("PONG", pingMessage);
	    }
	}
    }

    void write(String command, String message) {
	String fullMessage = command + " " + message;
	System.out.println(">>> " + fullMessage);
	out.print(fullMessage + "\r\n");
	out.flush();
    }
}
