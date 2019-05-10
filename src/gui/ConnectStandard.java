package gui;

import javax.swing.*;
import java.awt.*;

/**
 * connection dialog that lets user enter info
 */

class ConnectStandard extends ConnectWindow
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
		cancelled = errorWindow.isCancelled();
		while(!succeeded && !cancelled){
		    errorWindow = new ConnectStandardError();
		    succeeded = errorWindow.isSucceeded();
		    cancelled = errorWindow.isCancelled();
		}
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
		serverLabel, serverArea,
	    	portLabel, portArea,
		nickLabel, nicknameArea,
		userLabel, usernameArea,
	    realnameLabel, realNameArea
	};
	return JOptionPane.showConfirmDialog(null, inputs, "New connection", JOptionPane.DEFAULT_OPTION);
    }
}
