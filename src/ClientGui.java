import net.miginfocom.swing.MigLayout;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.io.IOException;
import java.util.Vector;

public class ClientGui extends JFrame
{
    private JFrame frame;
    private String nickname;
    private JList userList = new JList();
    private Vector users = new Vector();
    private JButton sendButton = new JButton("Send");
    private JScrollPane userPane = new JScrollPane(userList);
    private JTextField userInputField = new JTextField();
    private JTabbedPane tabbedPane = new JTabbedPane();
    private JLabel status = new JLabel("Connected to: None..");
    private IRCConnection connection;
    private ChannelTabList channels;
    private String currentTab;

    public ClientGui() {
        channels = new ChannelTabList(tabbedPane);
        updateStatus("None");
        userPane.setPreferredSize(new Dimension(100, 100));
        this.frame = new JFrame("IRC-Client");
        createMenuBar();
	channels.addChannel(new ChannelTab("Standard", tabbedPane));
        frame.setLayout(new MigLayout("","[][][][][grow][][]", "[grow][][]"));
        frame.add(tabbedPane, "span 5,grow");
        frame.add(userPane, "span 2,grow, wrap");
        frame.add(userInputField, "span 5, grow");
        frame.add(sendButton, "span 2, wrap");
	frame.add(status,"span 7,grow, wrap");
	frame.pack();
	frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	frame.setVisible(true);
	tabbedPane.addChangeListener(this::switchedTab);
	userInputField.addActionListener(this::sendMessage);
	sendButton.addActionListener(this::sendMessage);
    }

    public void addUser(String username, String channel){
        channels.addUser(username, channel);
    }
    public void updateUsers(){
        userList.setListData(users);
    }
    public void disconnectUser(String username){
        Boolean removed = users.remove(username);
        if(removed){
	    System.out.println(username + " was removed from online");
	} else{
	    System.out.println(username + " not found");
	}
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
	    connection.inputHandler(tabbedPane.getTitleAt(tabbedPane.getSelectedIndex()),fromUser);
	    infoToScreen(tabbedPane.getTitleAt(tabbedPane.getSelectedIndex()),"<"+nickname + "> "+ fromUser);
	    userInputField.setText("");
	}
    }

    private void switchedTab(ChangeEvent event){
	currentTab = tabbedPane.getTitleAt(tabbedPane.getSelectedIndex());
	users = channels.getUsers(currentTab);
	updateUsers();
    }

    private void newConnection(ActionEvent event) {
        ServerDialog connectionDialog = new ServerDialog();
        if(connectionDialog.getServerName() != null){
            updateStatus(connectionDialog.getServerName());
	    updateNick(connectionDialog.getNickname());
	    try{
	        connection = new IRCConnection(this, connectionDialog.getServerName(), connectionDialog.getPort(), connectionDialog.getNickname(), connectionDialog.getUsername(), connectionDialog.getRealName());
	    }
	    catch (IOException e) {
		System.out.println("error");
	    }

	}
    }
    private void connectFreenode(ActionEvent event) {
	updateStatus("chat.freenode.net");
	try{
	    connection = new IRCConnection(this,"chat.freenode.net",6667, "Seb__XD", "Seb__XD", "Seb b" );
	    updateNick("Seb__XD");
	}
	catch (IOException e) {
	    System.out.println("error");
	}
    }

    public void infoToScreen(String channel, String message) {
        channels.writeToChannel(channel,message);
    }


    private void createMenuBar(){
        JMenuBar menu = new JMenuBar();
        JMenu connect = new JMenu("Connect");
	JMenuItem connectTo = new JMenuItem(" Connect to");
	JMenuItem connectToFreenode = new JMenuItem(" Connect to Freenode");
        connectTo.addActionListener(this::newConnection);
	connectToFreenode.addActionListener(this::connectFreenode);
        connect.add(connectTo);
        connect.add(connectToFreenode);
        menu.add(connect);
        frame.setJMenuBar(menu);
    }

    public void closeTab(String tabName){
        channels.removeChannel(tabName);
    }

    public static void main(String[] args) {
	new ClientGui();
    }

}
