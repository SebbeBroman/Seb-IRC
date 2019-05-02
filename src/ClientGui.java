import net.miginfocom.swing.MigLayout;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

final class ClientGui extends JFrame
{
    private final JFrame frame;
    private String nickname = null;
    private final JList<Object> userList;
    private List<String> users;
    private final JTextField userInputField;
    private final JTabbedPane tabbedPane;
    private final JLabel status;
    private IRCConnection connection = null;
    private final ChannelTabList channels;

    private ClientGui() {
        users = new ArrayList<>();
	tabbedPane = new JTabbedPane();
	channels = new ChannelTabList(tabbedPane);
	userList = new JList<>();
	final JScrollPane userPane = new JScrollPane(userList);
	userPane.setPreferredSize(new Dimension(100, 100));
        userPane.setMinimumSize(new Dimension(50, 50));
        this.frame = new JFrame("Seb IRC -Client");
	ImageIcon img = new ImageIcon(ClassLoader.getSystemResource("ChatBubble.png"));
	frame.setIconImage(img.getImage());
        createMenuBar();
	channels.addChannel(new ChannelTab("Standard", tabbedPane));
        frame.setLayout(new MigLayout("","[][][][][grow][][]", "[grow][][]"));
        frame.add(tabbedPane, "span 5,grow");
        frame.add(userPane, "span 2,grow, wrap");
	userInputField = new JTextField();
	frame.add(userInputField, "span 5, grow");
	final JButton sendButton = new JButton("Send");
	frame.add(sendButton, "span 2, wrap");
	status = new JLabel("Connected to: None..");
	frame.add(status, "span 7,grow, wrap");
	frame.pack();
	frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
	frame.setVisible(true);
	tabbedPane.addChangeListener(this::switchedTab);
	userInputField.addActionListener(this::sendMessage);
	sendButton.addActionListener(this::sendMessage);
	userList.addMouseListener(new ActionJList(userList));
    }

    void addUser(String username, String channel){
        channels.addUser(username, channel);
    }
    void updateUsers(){
        userList.setListData(users.toArray()); //converts a ArrayList<String> of username to a format JList accepts
    }
    void disconnectUser(String username){
        boolean removed = users.remove(username);
        if(removed){
	    System.out.println(username + " was removed from online");
	} else{
	    System.out.println(username + " not found");
	}
    }
    private void updateStatus(String server){
	status.setText("Connected to: " + server);
    }
    private void updateNick(String nickname){
        this.nickname = nickname;
    }

    @SuppressWarnings("unused") private void sendMessage(ActionEvent event){
	//should do this no matter what event, event needed for the listener to work
	String fromUser = userInputField.getText();
	if (fromUser != null) {
	    connection.inputHandler(tabbedPane.getTitleAt(tabbedPane.getSelectedIndex()),fromUser);
	    infoToScreen(tabbedPane.getTitleAt(tabbedPane.getSelectedIndex()),"<"+nickname + "> "+ fromUser);
	    userInputField.setText("");
	}
    }

    @SuppressWarnings("unused") private void switchedTab(ChangeEvent event){
	//should do this no matter what event, event needed for the listener to work
	if(tabbedPane.getTabCount()<1){ // all tabs closed, disconnect
	    disconnect();
	    updateStatus("Disconnected");
	}else{
	    final String currentTab = tabbedPane.getTitleAt(tabbedPane.getSelectedIndex());
	    connection.setCurrentChannel(currentTab);
	    users = channels.getUsers(currentTab);
	    updateUsers();
	}

    }

    @SuppressWarnings("unused") private void newConnection(ActionEvent event) {
	//should do this no matter what event, event needed for the listener to work
        ServerDialog connectionDialog = new ServerDialog();
        if(connectionDialog.isSucceeded()){
            updateStatus(connectionDialog.getServerName());
	    updateNick(connectionDialog.getNickname());
	    try{
	        connection = new IRCConnection(this, connectionDialog.getServerName(), connectionDialog.getPort(), connectionDialog.getNickname(), connectionDialog.getUsername(), connectionDialog.getRealName());
	    }
	    catch (IOException e) {
		System.out.println("error");
		e.printStackTrace();
	    }

	}
    }
    @SuppressWarnings("unused") private void connectFreenode(ActionEvent event) {
	//should do this no matter what event, event needed for the listener to work
	updateStatus("chat.freenode.net");
	try{
	    //noinspection MagicNumber
	    connection = new IRCConnection(this,"chat.freenode.net",6667, "Seb__XD", "Seb__XD", "Seb b" );
	    updateNick("Seb__XD");
	}
	catch (IOException e) {
	    System.out.println("error");
	    e.printStackTrace();
	}
    }

    void infoToScreen(String channel, String message) {
        channels.writeToChannel(channel,message);
    }


    private void createMenuBar(){
        JMenuBar menu = new JMenuBar();
        JMenu connect = new JMenu("Connection");
        JMenu help = new JMenu("Help");
	JMenuItem connectTo = new JMenuItem(" Connect to");
	JMenuItem disconnect = new JMenuItem(" Disconnect");
	JMenuItem connectToFreenode = new JMenuItem(" Connect to Freenode");
	JMenuItem getHelp = new JMenuItem("Help");
	connectTo.setToolTipText("Connects to server of your choice");
	disconnect.setToolTipText("Disconnects current connection");
	getHelp.setToolTipText("Click here to open readme file of how it works");
        connectTo.addActionListener(this::newConnection);
        disconnect.addActionListener(this::disconnectEvent);
	connectToFreenode.addActionListener(this::connectFreenode);
	getHelp.addActionListener(this::openREADME);
        connect.add(connectTo);
        connect.add(disconnect);
        connect.add(connectToFreenode);
        help.add(getHelp);
        menu.add(connect);
        menu.add(help);
        frame.setJMenuBar(menu);
    }

    @SuppressWarnings("unused") private void openREADME(final ActionEvent actionEvent){
	//should do this no matter what event, event needed for the listener to work
	JFrame readme = new JFrame("README.txt");
	readme.setPreferredSize(new Dimension(500,700));
	String text = "Could not find README";
	try{
	    text = Files.readString(Paths.get("README.txt"), Charset.defaultCharset());
	}catch(IOException e){
	    e.printStackTrace();
	}
	JTextArea readmeTextField = new JTextArea(text);
	readmeTextField.setLineWrap(true);
	readmeTextField.setWrapStyleWord(true);
	readmeTextField.setEditable(false);
        readmeTextField.setPreferredSize(new Dimension(400,850));
        final JScrollPane scrollPane = new JScrollPane(readmeTextField);
        readme.add(scrollPane);
        readme.pack();
	readme.setVisible(true);
    }

    @SuppressWarnings("unused") private void disconnectEvent(final ActionEvent actionEvent){
        //should do this no matter what event, event needed for the listener to work
        disconnect();
    }

    private void disconnect() {
	try {
	    if (connection != null) {
		connection.disconnect();
		updateStatus("Disconnected");
	    }
	} catch (IOException e) {
	    e.printStackTrace(); // Should not get here so just print stacktrace if error occurs
	}
    }

    void closeTab(String tabName){
        channels.removeChannel(tabName);
    }

    public static void main(String[] args) {
	new ClientGui();
    }

    private final class ActionJList extends MouseAdapter
    {
        private final JList<Object> list;

        private ActionJList(JList<Object> l) {
    	list = l;
        }

        public void mouseClicked(MouseEvent e) {
    	if (e.getClickCount() == 2) {
	    //Enters when dblclick
    	    int index = list.locationToIndex(e.getPoint());
    	    ListModel<Object> dlm = list.getModel();
    	    String item = dlm.getElementAt(index).toString();
    	    list.ensureIndexIsVisible(index);
    	    System.out.println("Double clicked on " + item);
    	    channels.writeToChannel(item, "/WHOIS " + item);
    	    tabbedPane.setSelectedIndex(tabbedPane.indexOfTab(item));
	    connection.inputHandler(tabbedPane.getTitleAt(tabbedPane.getSelectedIndex()),"/WHOIS " + item);
    	}
        }
    }

    void setTopicOfChannel(String channel, String topic){
       channels.setTopicOf(channel,topic);
	System.out.println(topic);
    }

}
