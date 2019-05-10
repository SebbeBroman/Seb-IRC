package gui;

import javax.swing.*;
import java.awt.*;

/**
 * connection dialog that lets user enter info
 */

public class ConnectWithPass extends ConnectWindow
{

    ConnectWithPass(){
        int dialogResponse = makeDialog();
        if(dialogResponse == JOptionPane.OK_OPTION){
	    handleDialog();
	    succeeded = succeeded && !passwordArea.getText().isEmpty();
	    if (succeeded){
		System.out.println("Success");
	    }else{
		ConnectWithPassError errorWindow = new ConnectWithPassError();
		succeeded = errorWindow.isSucceeded();
		cancelled = errorWindow.isCancelled();
		while(!succeeded && !cancelled){
		    errorWindow = new ConnectWithPassError();
		    succeeded = errorWindow.isSucceeded();
		    cancelled = errorWindow.isCancelled();
		}
		password = errorWindow.getPassword();
		serverName = errorWindow.getServerName();
		port = errorWindow.getPort();
		username = errorWindow.getUsername();
		nickname = errorWindow.getNickname();
		realName = errorWindow.getRealName();
	    }
	}else{
	    cancelled = true;
	}
    }

    private int makeDialog() {
	succeeded = false;
	cancelled = false;
	setLabelColor(Color.BLACK, makeStandardFields());
	JComponent[] inputs = new JComponent[] {
		passLabel, passwordArea,
		serverLabel, serverArea,
		portLabel, portArea,
		nickLabel, nicknameArea,
		userLabel, usernameArea,
		realnameLabel, realNameArea};
	return JOptionPane.showConfirmDialog(null, inputs, "New connection, password protected", JOptionPane.DEFAULT_OPTION);
    }
}
