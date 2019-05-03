package GUI;

import javax.swing.*;
import javax.swing.text.NumberFormatter;
import java.awt.*;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Objects;

public abstract class ConnectWindow
{
    protected String serverName = null;
    protected int port;
    protected String nickname = null;
    protected String username = null;
    protected String realName = null;
    protected String password = "";
    protected JFormattedTextField portArea = null;
    protected JTextField serverArea = null;
    protected JTextField nicknameArea = null;
    protected JTextField usernameArea = null;
    protected JTextField realNameArea = null;
    protected JTextField passwordArea = null;
    protected boolean succeeded;
    protected JLabel serverLabel = null;
    protected JLabel portLabel = null;
    protected JLabel nickLabel =  null;
    protected JLabel userLabel = null;
    protected JLabel passLabel = null;
    protected JLabel error = null;


    void setLabelColor(Color color, Iterable<JLabel> labels){
            for(JLabel label : labels){
                label.setForeground(color);
    	}
    }

    ArrayList<JLabel> makeStandardFields(){
	final NumberFormat format = NumberFormat.getInstance();
	format.setGroupingUsed(false);
	final NumberFormatter numFormatter = new NumberFormatter(format);
	numFormatter.setValueClass(Integer.class);
	numFormatter.setMinimum(0);
	numFormatter.setMaximum(Integer.MAX_VALUE);
	numFormatter.setAllowsInvalid(false);
	numFormatter.setCommitsOnValidEdit(true);
	portArea = new JFormattedTextField(numFormatter);
	serverLabel = new JLabel("Enter server name");
	portLabel = new JLabel("Enter port number");
	nickLabel =  new JLabel("Enter nickname");
	userLabel = new JLabel("Enter username");
	passLabel = new JLabel("Enter password: ");
	error = new JLabel("Please enter all red fields");
	serverArea = new JTextField();
	nicknameArea = new JTextField();
	usernameArea = new JTextField();
	realNameArea = new JTextField();
	passwordArea = new JTextField();
	ArrayList<JLabel> labels = new ArrayList<>();
	labels.add(serverLabel);
	labels.add(portLabel);
	labels.add(nickLabel);
	labels.add(userLabel);
	labels.add(passLabel);
	labels.add(error);

	return labels;
    }
    void handleDialog() {
	if (portArea.getText().isEmpty()) {
	    port = 0;
	} else {
	    port = Integer.parseInt(portArea.getText());
	}
	serverName = serverArea.getText();
	nickname = nicknameArea.getText();
	username = usernameArea.getText();
	realName = realNameArea.getText();
	succeeded = !(Objects.equals(serverName, "") || port == 0 || Objects.equals(nickname, "") || Objects.equals(username, ""));
    }

    String getServerName() {
	return serverName;
    }

    int getPort() {
	return port;
    }
    String getNickname() {
	return nickname;
    }

    String getUsername() {
	return username;
    }
    String getRealName() {
	return realName;
    }

    public boolean isSucceeded() {
        return succeeded;
    }
    public String getPassword() {
    	return password;
        }
}
