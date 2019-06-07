package gui;

import javax.swing.*;

/**
 * connection dialog that lets user enter info
 */

public class ConnectStandard extends ConnectWindow
{
    public void show(){
	boolean firstTime = true;
	succeeded = false;

	while (!succeeded) {
	    int dialogResponse = makeDialog(!firstTime, false);
	    if (dialogResponse == JOptionPane.OK_OPTION) {
	        handleDialog();
		firstTime = false;
	    } else {
	        break; // lämna loopen utan att sätta succeeded
	    }
	}

    }



}
