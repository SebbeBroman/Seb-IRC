package gui;

import javax.swing.*;
import javax.swing.text.NumberFormatter;
import java.awt.*;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
/**
 * Abstract common class between connection dialogs.
 * Following fields change depending on answer in subclass so inspection complains
 * "Reports any assignments to fields from a superclass from within a constructor.
 * It is usually better to call a constructor of the superclass to initialize the fields."
 * But since the assignment is dynamic it's probably the best solution.
 *  protected String serverName = null;
 *     protected int port;
 *     protected String nickname = null;
 *     protected String username = null;
 *     protected String realName = null;
 *     protected String password = "";
 *     protected boolean succeeded;
 *     protected boolean cancelled;
 */
abstract class ConnectWindow
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
    protected boolean cancelled;
    protected JLabel serverLabel = null;
    protected JLabel portLabel = null;
    protected JLabel nickLabel = null;
    protected JLabel userLabel = null;
    protected JLabel passLabel = null;
    protected JLabel realnameLabel = null;
    protected JLabel error = null;

    public boolean isCancelled() {
	return cancelled;
    }

    void setLabelColor(Color color, Iterable<JLabel> labels){
            for(JLabel label : labels){
                label.setForeground(color);
    	}
    }

    Iterable<JLabel> makeStandardFields(){
	final NumberFormat format = NumberFormat.getInstance();
	format.setGroupingUsed(false);
	final NumberFormatter numFormatter = new NumberFormatter(format);
	numFormatter.setValueClass(Integer.class);
	numFormatter.setMinimum(Integer.valueOf(0));
	numFormatter.setMaximum(Integer.valueOf(Integer.MAX_VALUE));
	numFormatter.setAllowsInvalid(false);
	numFormatter.setCommitsOnValidEdit(true);
	portArea = new JFormattedTextField(numFormatter);
	serverLabel = new JLabel("Enter server name");
	portLabel = new JLabel("Enter port number");
	nickLabel =  new JLabel("Enter nickname");
	userLabel = new JLabel("Enter username");
	passLabel = new JLabel("Enter password: ");
	realnameLabel = new JLabel("Enter real name");
	error = new JLabel("Please enter all red fields");
	serverArea = new JTextField();
	nicknameArea = new JTextField();
	usernameArea = new JTextField();
	realNameArea = new JTextField();
	passwordArea = new JTextField();
	List<JLabel> labels = new ArrayList<>();
	labels.add(serverLabel);
	labels.add(portLabel);
	labels.add(nickLabel);
	labels.add(userLabel);
	labels.add(passLabel);
	labels.add(realnameLabel);
	labels.add(error);

	return labels;
    }
    protected void handleDialog() {
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

    protected String getServerName() {
	return serverName;
    }

    protected int getPort() {
	return port;
    }
    protected String getNickname() {
	return nickname;
    }

    protected String getUsername() {
	return username;
    }
    protected String getRealName() {
	return realName;
    }

    protected boolean isSucceeded() {
        return succeeded;
    }
    protected String getPassword() {
    	return password;
        }
}