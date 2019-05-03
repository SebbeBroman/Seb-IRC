package GUI;

import Connection.IRCConnection;
import net.miginfocom.swing.MigLayout;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

/**
 * This class handles the GUI and all interactions with the user.
 */


final public class ClientGui extends JFrame
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
    private final String FREENODESERVER = "chat.freenode.net";
    private final int FREENODEPORT = 6667;

    private ClientGui() {
	users = new ArrayList<>();
	tabbedPane = new JTabbedPane();
	channels = new ChannelTabList(tabbedPane);
	userList = new JList<>();
	final JScrollPane userPane = new JScrollPane(userList);
	userPane.setPreferredSize(new Dimension(100, 100));
	userPane.setMinimumSize(new Dimension(50, 50));
	this.frame = new JFrame("Seb IRC -Client");
	try{
		ImageIcon img = new ImageIcon(ClassLoader.getSystemResource("ChatBubble.png"));
		frame.setIconImage(img.getImage());
	} catch (NullPointerException e){
		// Not a mandatory feature for the program so just log the error
		// and move on.
		e.printStackTrace();
	}
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

    public void addUser(String username, String channel){
        channels.addUser(username, channel);
    }
    public void updateUsers(){
        userList.setListData(users.toArray()); //converts a ArrayList<String> of username to a format JList accepts
    }
    public void disconnectUser(String username){
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
	System.out.println(event.getActionCommand() );
	ConnectWindow connectionDialog;
	if(event.getActionCommand().equals("ConnectStandard")){
	    connectionDialog = new ConnectStandard();
	}else{
	    connectionDialog = new ConnectWithPass();
	}
        if(connectionDialog.isSucceeded()){
            updateStatus(connectionDialog.getServerName());
	    updateNick(connectionDialog.getNickname());
	    try{
	        connection = new IRCConnection(this, connectionDialog.getServerName(), connectionDialog.getPort(), connectionDialog.getNickname(), connectionDialog.getUsername(), connectionDialog.getRealName(),connectionDialog.getPassword());
	    }
	    catch (IOException e) {
	        channels.writeToChannel(tabbedPane.getTitleAt(tabbedPane.getSelectedIndex()),"Could not connect to Host" );
			updateStatus("Failed to connect to host: " + connectionDialog.getServerName());
			System.out.println("error");
			e.printStackTrace();
	    }

	}
    }
    @SuppressWarnings("unused") private void connectFreenode(ActionEvent event) {
		//should do this no matter what event, event needed for the listener to work

		try{
			connection = new IRCConnection(this,FREENODESERVER,FREENODEPORT, "Seb__XD", "Seb__XD", "Seb b" ,"");
			updateNick("Seb__XD");
			updateStatus("chat.freenode.net");
		}
		catch (IOException e) {
			System.out.println("error");
			updateStatus("Failed to connect to host: " + "chat.freenode.net");
			e.printStackTrace();
		}
    }

    public void infoToScreen(String channel, String message) {
        channels.writeToChannel(channel,message);
    }


    private void createMenuBar(){
		JMenuBar menu = new JMenuBar();
		JMenu connect = new JMenu("Connection");
		JMenu help = new JMenu("Help");
		JMenuItem connectTo = new JMenuItem(" Connect to");
		JMenuItem connectToProtected = new JMenuItem(" Connect to protected server");
		JMenuItem disconnect = new JMenuItem(" Disconnect");
		JMenuItem connectToFreenode = new JMenuItem(" Connect to Freenode");
		JMenuItem getHelp = new JMenuItem("Help");
		connectToProtected.setToolTipText("Connects to a password protected server of your choice");
		connectTo.setToolTipText("Connects to server of your choice");
		disconnect.setToolTipText("Disconnects current connection");
		getHelp.setToolTipText("Click here to open readme file of how it works");
		connectTo.addActionListener(this::newConnection);
		connectTo.setActionCommand("ConnectStandard");
		connectToProtected.addActionListener(this::newConnection);
		connectToProtected.setActionCommand("ConnectProtected");
		disconnect.addActionListener(this::disconnectEvent);
		connectToFreenode.addActionListener(this::connectFreenode);
		getHelp.addActionListener(this::openREADME);
		connect.add(connectTo);
		connect.add(connectToProtected);
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

    public void closeTab(String tabName){
        channels.removeChannel(tabName);
    }



    private final class ActionJList extends MouseAdapter //changed to private and final
    {
	/**
	 * Copied from
	 * https://www.rgagnon.com/javadetails/java-0219.html
	 * and modified to send /WHOIS to the username that was clicked on.
	 * It changes the selected tab to be a PM to the person clicked on.
	 */
	private final JList<Object> list; //changed to private and final
        private ActionJList(JList<Object> l) { //changed to private
    		list = l;
        }
        public void mouseClicked(MouseEvent e) {
	    if (e.getClickCount() == 2) {
		//Enters when double click.
		int index = list.locationToIndex(e.getPoint());
		ListModel<Object> dlm = list.getModel();
		String item = dlm.getElementAt(index).toString();
		list.ensureIndexIsVisible(index);
		channels.writeToChannel(item, "/WHOIS " + item); //Added
		tabbedPane.setSelectedIndex(tabbedPane.indexOfTab(item)); // Added
		connection.inputHandler(tabbedPane.getTitleAt(tabbedPane.getSelectedIndex()),"/WHOIS " + item); //Added
	    }
        }
    }

    public void setTopicOfChannel(String channel, String topic){
       channels.setTopicOf(channel,topic);
	System.out.println(topic);
    }

	public static void main(String[] args) {
		new ClientGui();
	}
}
