package GUI;

import javax.swing.*;
import java.awt.*;

public class ConnectWithPassError extends ConnectWindow
{

    ConnectWithPassError(){
    	while(!succeeded){
   	    int dialogResponse = makeDialog();
   	    if (dialogResponse != JOptionPane.OK_OPTION){
   	        break;
   	    }
   	    handleDialog();
	    succeeded = succeeded && !passwordArea.getText().isEmpty();
   	}
   	if (!succeeded){
   	    System.out.println("Cancelled");
   	}
    }


    private int makeDialog() {
	succeeded = false;
	setLabelColor(Color.RED, makeStandardFields());
	JComponent[] inputs = new JComponent[] { error,
		passLabel, passwordArea,
		serverLabel, serverArea,
		portLabel, portArea,
		nickLabel, nicknameArea,
		userLabel, usernameArea,
	new JLabel("Enter real name*"), realNameArea
	};
	return JOptionPane.showConfirmDialog(null, inputs, "New connection, password protected", JOptionPane.DEFAULT_OPTION);
    }
}
