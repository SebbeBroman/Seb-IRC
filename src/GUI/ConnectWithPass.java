package GUI;

import javax.swing.*;
import java.awt.*;

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
		while(!succeeded){
		    errorWindow = new ConnectWithPassError();
		    succeeded = errorWindow.isSucceeded();
		}
		password = errorWindow.getPassword();
		serverName = errorWindow.getServerName();
		port = errorWindow.getPort();
		username = errorWindow.getUsername();
		nickname = errorWindow.getNickname();
		realName = errorWindow.getRealName();
	    }
	}
    }

    private int makeDialog() {
	succeeded = false;
	setLabelColor(Color.BLACK, makeStandardFields());
	JComponent[] inputs = new JComponent[] {
		passLabel, passwordArea,
		serverLabel, serverArea,
		portLabel, portArea,
		nickLabel, nicknameArea,
		userLabel, usernameArea,
	new JLabel("Enter real name*"), realNameArea};
	return JOptionPane.showConfirmDialog(null, inputs, "New connection, password protected", JOptionPane.DEFAULT_OPTION);
    }
}
