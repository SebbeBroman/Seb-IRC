import javax.swing.*;
import javax.swing.text.NumberFormatter;
import java.awt.*;
import java.text.NumberFormat;
import java.util.Objects;

/**
 * This is a DialogWindow that gives has the needed fields to connect to a server.
 * it has a function isSucceeded that tells you if the dialog was entered properly
 * but not if the connection to a server was valid.
 *
 */


class ServerDialog extends JFrame
{
    private String serverName = null;
    private int port;
    private String nickname = null;
    private String username = null;
    private String realName = null;
    private JFormattedTextField portArea = null;
    private JTextField serverArea = null;
    private JTextField nicknameArea = null;
    private JTextField usernameArea = null;
    private JTextField realNameArea = null;
    private volatile boolean succeeded;

    boolean isSucceeded() {
	return succeeded;
    }



    ServerDialog(){
        succeeded = false;
	boolean firstTime = true;
        while(!succeeded){
	    int dialogResponse = makeDialog(firstTime);
	    if (dialogResponse != JOptionPane.OK_OPTION){
	        break;
	    }
	    handleDialog();
	    firstTime = false;
	}
        if (!succeeded){
	    System.out.println("Cancelled");
	}
    }

    private void handleDialog(){
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

    private int makeDialog(boolean firstTime) {
	final NumberFormat format = NumberFormat.getInstance();
	format.setGroupingUsed(false);
	final NumberFormatter numFormatter = new NumberFormatter(format);
	numFormatter.setValueClass(Integer.class);
	//noinspection AutoBoxing
	numFormatter.setMinimum(0);
	// noinspection AutoBoxing
	numFormatter.setMaximum(Integer.MAX_VALUE);
	numFormatter.setAllowsInvalid(false);
	numFormatter.setCommitsOnValidEdit(true);
	portArea = new JFormattedTextField(numFormatter);
	serverArea = new JTextField();
	nicknameArea = new JTextField();
	usernameArea = new JTextField();
	realNameArea = new JTextField();
	JLabel serverLabel = new JLabel("Enter server name");
	JLabel portLabel = new JLabel("Enter port number");
	JLabel nickLabel =  new JLabel("Enter nickname");
	JLabel userLabel = new JLabel("Enter username");
	if(!firstTime){
	    serverLabel.setForeground(Color.RED);
	    portLabel.setForeground(Color.RED);
	    nickLabel.setForeground(Color.RED);
	    userLabel.setForeground(Color.RED);
	}
	JComponent[] inputs = new JComponent[] {
		serverLabel, serverArea,
	    	portLabel, portArea,
		nickLabel, nicknameArea,
		userLabel, usernameArea,
	    new JLabel("Enter real name*"), realNameArea
	};
	return JOptionPane.showConfirmDialog(null, inputs, "New connection", JOptionPane.DEFAULT_OPTION);
    }

    String getServerName() {
	return serverName;
    }

    int getPort() {
	return port;
    }

    String getNickname() {
	return nickname;
    }

    String getUsername() {
	return username;
    }

    String getRealName() {
	return realName;
    }

}
