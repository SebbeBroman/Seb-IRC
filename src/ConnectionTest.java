import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

public class ConnectionTest
{
    private static String server = "chat.freenode.net";
    private static int port = 6667;
    private static PrintWriter out;
    private static Scanner in;



    public static void main(String[] args) throws IOException {
        Scanner console = new Scanner(System.in);
	System.out.println("Enter nick");
        String nick = console.nextLine();
	System.out.println("Enter username");
        String username = console.nextLine();
	System.out.println("Enter real name");
        String realName = console.nextLine();

	Socket socket = new Socket(server, port);
	out = new PrintWriter(socket.getOutputStream(), true);
	in = new Scanner(socket.getInputStream());

	write("NICK", nick);
	write("USER", username+ " 0 * :"+  realName);
	write("JOIN", "#reddit-dailyprogrammer");

	while(in.hasNext()){
	    String serverMessage = in.nextLine();
	    System.out.println("<<< " + serverMessage);
	    if(serverMessage.startsWith("PING")){
	        String pingMessage = serverMessage.split(" ", 2)[1];
	        write("PONG", pingMessage);
	        write("NAMES", "#reddit-dailyprogrammer");
	    }
	}

	in.close();
	out.close();
	socket.close();

	System.out.println("Done :)");


    }
    private static void write(String command, String message){
	String fullMessage = command + " " + message;
	System.out.println(">>> "+ fullMessage);
	out.print(fullMessage+"\r\n");
	out.flush();
    }
}
