package GUI;

import javax.swing.*;
import javax.swing.text.NumberFormatter;
import java.awt.*;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Objects;

public class ConnectStandard extends ConnectWindow
{

    ConnectStandard(){
        int dialogResponse = makeDialog();
        handleDialog();

    }


    @Override String getServerName() {
	return null;
    }

    @Override int getPort() {
	return 0;
    }

    @Override String getNickname() {
	return null;
    }

    @Override String getUsername() {
	return null;
    }

    @Override String getRealName() {
	return null;
    }

    @Override void handleDialog() {
	if (portArea.getText().isEmpty()){
	    port = 0;
	}else{
	    port = Integer.parseInt(portArea.getText());
	}
	serverName = serverArea.getText();
	nickname = nicknameArea.getText();
	username = usernameArea.getText();
	realName = realNameArea.getText();
        succeeded = !(Objects.equals(serverName, "") || port == 0 ||
		      Objects.equals(nickname, "") || Objects.equals(username, ""));
    }

    @Override int makeDialog() {
        succeeded = false;
	makeStandardFields();
	ArrayList<JLabel> labels = new ArrayList<>();
	labels.add(serverLabel);
	labels.add(portLabel);
	labels.add(nickLabel);
	labels.add(userLabel);
	setLabelColor(Color.BLACK, labels);

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
