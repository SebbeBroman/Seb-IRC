package GUI;

import javax.swing.*;
import java.awt.*;

public class ConnectStandardError extends ConnectWindow
{
    ConnectStandardError(){
	while(!succeeded){
	    int dialogResponse = makeDialog();
	    if (dialogResponse != JOptionPane.OK_OPTION){
	        break;
	    }
	    handleDialog();
	}
	if (!succeeded){
	    System.out.println("Cancelled");
	}
    }


    private int makeDialog() {
	succeeded = false;
	setLabelColor(Color.RED, makeStandardFields());
	JComponent[] inputs = new JComponent[] {error,
	serverLabel, serverArea,
	portLabel, portArea,
	nickLabel, nicknameArea,
	userLabel, usernameArea,
	new JLabel("Enter real name*"), realNameArea
	};
	return JOptionPane.showConfirmDialog(null, inputs, "New connection", JOptionPane.DEFAULT_OPTION);
    }
}
