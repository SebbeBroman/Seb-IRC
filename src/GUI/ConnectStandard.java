package GUI;

import javax.swing.*;
import java.awt.*;

public class ConnectStandard extends ConnectWindow
{

    ConnectStandard(){
        int dialogResponse = makeDialog();
        if(dialogResponse == JOptionPane.OK_OPTION){
	    handleDialog();

	    if (succeeded){
		System.out.println("Success");
	    }else{
		ConnectStandardError errorWindow = new ConnectStandardError();
		succeeded = errorWindow.isSucceeded();
		while(!succeeded){
		    errorWindow = new ConnectStandardError();
		    succeeded = errorWindow.isSucceeded();
		}
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
		serverLabel, serverArea,
	    	portLabel, portArea,
		nickLabel, nicknameArea,
		userLabel, usernameArea,
	    new JLabel("Enter real name*"), realNameArea
	};
	return JOptionPane.showConfirmDialog(null, inputs, "New connection", JOptionPane.DEFAULT_OPTION);
    }
}
