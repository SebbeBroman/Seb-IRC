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
public abstract class ConnectWindow
{
    protected String serverName;
    protected int port;
    protected String nickname;
    protected String username;
    protected String realName;
    protected String password;
    protected JFormattedTextField portArea;
    protected JTextField serverArea;
    protected JTextField nicknameArea;
    protected JTextField usernameArea;
    protected JTextField realNameArea;
    protected JTextField passwordArea;
    protected boolean succeeded;
    protected JLabel serverLabel;
    protected JLabel portLabel;
    protected JLabel nickLabel;
    protected JLabel userLabel;
    protected JLabel passLabel;
    protected JLabel realnameLabel;
    protected JLabel error;

    


    protected void setLabelColor(Color color, Iterable<JLabel> labels){
            for(JLabel label : labels){
                label.setForeground(color);
    	}
    }
    
    protected ConnectWindow(){
	final NumberFormat format = NumberFormat.getInstance();
	format.setGroupingUsed(false);
	final NumberFormatter numFormatter = new NumberFormatter(format);
	numFormatter.setValueClass(Integer.class);
	numFormatter.setMinimum(Integer.valueOf(0));
	numFormatter.setMaximum(Integer.valueOf(Integer.MAX_VALUE));
	numFormatter.setAllowsInvalid(false);
	numFormatter.setCommitsOnValidEdit(true);
	serverArea = new JTextField();
	nicknameArea = new JTextField();
	usernameArea = new JTextField();
	realNameArea = new JTextField();
	passwordArea = new JTextField();
	portArea = new JFormattedTextField(numFormatter);
	serverName = "";
	username = "";
	nickname = "";
	realName = "";
	password = "";
	serverLabel = new JLabel("Enter server name");
	portLabel = new JLabel("Enter port number");
	nickLabel =  new JLabel("Enter nickname");
	userLabel = new JLabel("Enter username");
	passLabel = new JLabel("Enter password: ");
	realnameLabel = new JLabel("Enter real name");
	error = new JLabel("Please enter all red fields");
	
    }

    protected Iterable<JLabel> makeStandardFields(){
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
	succeeded = !(Objects.equals(serverName, "") || port == 0 || Objects.equals(nickname, "") ||
		      Objects.equals(username, ""));

    }
    
    protected int makeDialog(boolean hasError, boolean hasPass) {
 	JComponent[] inputs;
         succeeded = false;
 	if(hasError){
 	    setLabelColor(Color.RED, makeStandardFields());
 	}else{
 	    setLabelColor(Color.BLACK, makeStandardFields());
 	}
 	if (hasPass){
 	    inputs = new JComponent[] {
 	    		passLabel, passwordArea,
 	    		serverLabel, serverArea,
 	    		portLabel, portArea,
 	    		nickLabel, nicknameArea,
 	    		userLabel, usernameArea,
 	    		realnameLabel, realNameArea
 	    };
 	}
 	else{
 	    inputs = new JComponent[] {
 	   		serverLabel, serverArea,
 	   	    	portLabel, portArea,
 	   		nickLabel, nicknameArea,
 	   		userLabel, usernameArea,
 	   	    realnameLabel, realNameArea
 	    };
 	}
 	return JOptionPane.showConfirmDialog(null, inputs, "New connection", JOptionPane.DEFAULT_OPTION);
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

    public abstract void show();
}
