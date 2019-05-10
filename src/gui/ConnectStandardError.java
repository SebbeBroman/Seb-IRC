package gui;

import javax.swing.*;
import java.awt.*;

/**
 * connection dialog that prompts user enter right info
 */

public class ConnectStandardError extends ConnectWindow
{
    ConnectStandardError(){
	while(!succeeded && !cancelled){
	    int dialogResponse = makeDialog();
	    if (dialogResponse != JOptionPane.OK_OPTION){
	        break;
	    }
	    handleDialog();
	}
	if (!succeeded){
	    System.out.println("Cancelled");
	    cancelled = true;
	}
    }


    private int makeDialog() {
	succeeded = false;
	cancelled = false;
	setLabelColor(Color.RED, makeStandardFields());
	JComponent[] inputs = new JComponent[] {error,
	serverLabel, serverArea,
	portLabel, portArea,
	nickLabel, nicknameArea,
	userLabel, usernameArea,
	realnameLabel, realNameArea
	};
	return JOptionPane.showConfirmDialog(null, inputs, "New connection", JOptionPane.DEFAULT_OPTION);
    }
}
