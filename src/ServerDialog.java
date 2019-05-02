import javax.swing.*;
import javax.swing.text.NumberFormatter;
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
    private final NumberFormat format = NumberFormat.getInstance();
    private final NumberFormatter numFormatter = new NumberFormatter(format);
    private JFormattedTextField portArea = new JFormattedTextField(numFormatter);
    private JTextField serverArea = new JTextField();
    private JTextField nicknameArea = new JTextField();
    private JTextField usernameArea = new JTextField();
    private JTextField realNameArea = new JTextField();
    private boolean succeeded;


    boolean isSucceeded() {
	return succeeded;
    }



    ServerDialog(){
        succeeded = (Objects.equals(serverArea.getText(), "") || portArea == null ||
		     Objects.equals(nicknameArea.getText(), "") || Objects.equals(usernameArea.getText(), ""));
	if((makeDialog() == JOptionPane.OK_OPTION) && succeeded){
	    serverName = serverArea.getText();
	    port = Integer.parseInt(portArea.getText());
	    nickname = nicknameArea.getText();
	    username = usernameArea.getText();
	    realName = realNameArea.getText();
	    System.out.println("You entered: " + serverName + ", "
			       + port + ", " + nickname + ", "
			       + username + ", " + realName);

	} else{
	    System.out.println("Cancelled");
	    succeeded = false;
	}
    }

    private int makeDialog() {
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
	JComponent[] inputs = new JComponent[] {
	    new JLabel("Enter server name"), serverArea,
	    new JLabel("Enter port number"), portArea,
	    new JLabel("Enter nickname"), nicknameArea,
	    new JLabel("Enter username"), usernameArea,
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
