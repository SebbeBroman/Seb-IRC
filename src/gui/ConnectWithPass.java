package gui;

import javax.swing.*;

/**
 * connection dialog that lets user enter info
 */

public class ConnectWithPass extends ConnectWindow
{
    public void show(){
	boolean firstTime = true;
	succeeded = false;

	while (!succeeded) {
	    int dialogResponse = makeDialog(!firstTime, true);
	    if (dialogResponse == JOptionPane.OK_OPTION) {
	    	handleDialog();
		succeeded = succeeded && !passwordArea.getText().isEmpty();
	    	firstTime = false;
	    } else {
	    	break; // lämna loopen utan att sätta succeeded
	    }
	}
    }
}
