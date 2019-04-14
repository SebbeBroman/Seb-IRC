import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Scanner;

public class Send implements Runnable
{
    private static PrintWriter out;
    private ClientGui gui;
    Scanner console = new Scanner(System.in);

    public Send(PrintWriter output, ClientGui GUI) {
	out = output;
	gui = GUI;
    }

    public void run(){
	    while(console.hasNext()){
		String command = console.nextLine();
		inputHandler(command);
	}
    }

    public void inputHandler(String input){
    List<String> messageList = new ArrayList<String>(Arrays.asList(input.split(" ")));
    String command= input.split(" ")[0];
    switch(command){
	case "/msg":
	    sendServer("PRIVMSG "+ messageList.get(1)+ " " + getRest(messageList, 2));
	    break;
	case "/join":
	    sendServer("JOIN "+ messageList.get(1));
	    break;
	case "/part":
	    sendServer("PART "+ messageList.get(1));
	    break;
	case "/names":
	    sendServer("NAMES " + messageList.get(1));
	    break;
	default:
	    sendServer(input);
	    break;
    	}
    }

    private void sendServer(String message){
	writeToScreen(message);
	out.print(message+ "\r\n");
	out.flush();
    }

    private void writeToScreen(String message) {
	gui.infoToScreen(message);
    	System.out.println("[" + new SimpleDateFormat("HH:mm").format(new Date()) + "] " + message);
    }
    private String getRest(List<String> strings, int index){
        StringBuilder builder = new StringBuilder();
	for (int i = index; i < strings.size(); i++) {
	    builder.append(strings.get(i));
	}
	return builder.toString();
    }
}
