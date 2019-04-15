import net.miginfocom.swing.MigLayout;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Vector;

public class ClientGui extends JFrame
{
    private JFrame frame;
    private String nickname;
    private JTextArea messageBox = new JTextArea();
    private JScrollPane messagePane = new JScrollPane(messageBox);
    private JList userList = new JList();
    private Vector users = new Vector();
    private JButton sendButton = new JButton("Send");
    private JScrollPane userPane = new JScrollPane(userList);
    private JTextField userInputField = new JTextField();
    private Runnable currentParser;
    private JLabel status = new JLabel("Connected to: None..");

    public ClientGui() {
        updateStatus("None");
        userPane.setPreferredSize(new Dimension(100, 100));
	messagePane.setPreferredSize(new Dimension(380, 100));
	messageBox.setLineWrap(true);
	messageBox.setWrapStyleWord(true);
	messageBox.setEditable(false);
	messagePane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        this.frame = new JFrame("IRC-Client");
        createMenuBar();
	JTabbedPane tb = new JTabbedPane();
	tb.add("No Connection",messagePane);
        frame.setLayout(new MigLayout("","[][][][][grow][][]", "[grow][][]"));
        frame.add(tb, "span 5,grow");
        frame.add(userPane, "span 2,grow, wrap");
        frame.add(userInputField, "span 5, grow");
        frame.add(sendButton, "span 2, wrap");
	frame.add(status,"span 7,grow, wrap");
	frame.pack();
	frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	frame.setVisible(true);
	userInputField.addActionListener(this::sendMessage);
	sendButton.addActionListener(this::sendMessage);
    }

    public void addUser(String username){
        users.addElement(username);
    }
    public void updateUsers(){
        userList.setListData(users);
    }
    public void updateStatus(String server){
	status.setText("Connected to: " + server);
    }
    public void updateNick(String nickname){
        this.nickname = nickname;
    }

    private void sendMessage(ActionEvent event){
	String fromUser = userInputField.getText();
	if (fromUser != null) {
	    //Send.inputHandler(fromUser);
	    messageToScreen(nickname, fromUser);
	    userInputField.setText("");
	}
    }
    private void newConnection(ActionEvent event) {
        ServerDialog connectionDialog = new ServerDialog();
        if(connectionDialog.getServerName() != null){
            updateStatus(connectionDialog.getServerName());
	    nickname = connectionDialog.getNickname();
	    try{
		currentParser = new IncomingTrafficParser(this,connectionDialog.getServerName(), connectionDialog.getPort(), connectionDialog.getNickname(), connectionDialog.getUsername(), connectionDialog.getRealName());
		new Thread(currentParser).start();
	    }
	    catch (IOException e) {
		System.out.println("error");
	    }

	}
    }


    public void messageToScreen(String sender, String message) {
	messageBox.append("[" + new SimpleDateFormat("HH:mm").format(new Date()) + "] <" + sender + "> " + message + "\n");
	messageBox.setCaretPosition(messageBox.getDocument().getLength());
    }

    public void infoToScreen(String message) {
	messageBox.append("[" + new SimpleDateFormat("HH:mm").format(new Date()) + "] " + message + "\n");
	messageBox.setCaretPosition(messageBox.getDocument().getLength());
    }


    private void createMenuBar(){
        JMenuBar menu = new JMenuBar();
        JMenu connect = new JMenu("Connect");
	JMenuItem connectTo = new JMenuItem(" Conncet to");
        connectTo.addActionListener(this::newConnection);
        connect.add(connectTo);
        menu.add(connect);
        frame.setJMenuBar(menu);
    }

    public static void main(String[] args) {
	new ClientGui();
    }

}
