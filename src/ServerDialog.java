import javax.swing.*;
import javax.swing.text.MaskFormatter;

public class ServerDialog extends JFrame
{
    private String serverName;
    private int port;
    private String nickname;
    private String username;
    private String realName;

    //private JFormattedTextField portArea = new JFormattedTextField();
    private JTextField serverArea = new JTextField();
    private JTextField portArea = new JTextField();
    private JTextField nicknameArea = new JTextField();
    private JTextField usernameArea = new JTextField();
    private JTextField realNameArea = new JTextField();
    private final JComponent[] inputs = new JComponent[] {
	    new JLabel("Enter server name"), serverArea,
	    new JLabel("Enter port number"), portArea,
	    new JLabel("Enter nickname"), nicknameArea,
	    new JLabel("Enter username"), usernameArea,
	    new JLabel("Enter real name*"), realNameArea
    };
    public ServerDialog(){
	int result = JOptionPane.showConfirmDialog(null, inputs, "New connection", JOptionPane.PLAIN_MESSAGE);
	if(result == JOptionPane.OK_OPTION){
	    System.out.println("You entered: " + serverArea.getText() + ", "
			       + portArea.getText() + ", " + nicknameArea.getText() + ", "
			       + usernameArea.getText() + ", " + realNameArea.getText());
	    serverName = serverArea.getText();
	    port = Integer.parseInt(portArea.getText());
	    nickname = nicknameArea.getText();
	    username = usernameArea.getText();
	    realName = realNameArea.getText();
	} else{
	    System.out.println("Cancelled");
	}
    }

    public String getServerName() {
	return serverName;
    }

    public int getPort() {
	return port;
    }

    public String getNickname() {
	return nickname;
    }

    public String getUsername() {
	return username;
    }

    public String getRealName() {
	return realName;
    }


    protected MaskFormatter createFormatter(String s) {
        /*
        Taken from https://docs.oracle.com/javase/tutorial/uiswing/components/formattedtextfield.html
        Mask to format text to specify inputs.
         */
        MaskFormatter formatter = null;
        try {
            formatter = new MaskFormatter(s);
        } catch (java.text.ParseException exc) {
            System.err.println("formatter is bad: " + exc.getMessage());
            System.exit(-1);
        }
        return formatter;
    }
}
