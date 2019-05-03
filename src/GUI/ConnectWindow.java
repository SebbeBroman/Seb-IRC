package GUI;

import javax.swing.*;
import javax.swing.text.NumberFormatter;
import java.awt.*;
import java.text.NumberFormat;
import java.util.ArrayList;

public abstract class ConnectWindow
{
    protected String serverName = null;
    protected int port;
    protected String nickname = null;
    protected String username = null;
    protected String realName = null;
    protected JFormattedTextField portArea = null;
    protected JTextField serverArea = null;
    protected JTextField nicknameArea = null;
    protected JTextField usernameArea = null;
    protected JTextField realNameArea = null;
    protected boolean succeeded;
    protected JLabel serverLabel = new JLabel("Enter server name");
    protected JLabel portLabel = new JLabel("Enter port number");
    protected JLabel nickLabel =  new JLabel("Enter nickname");
    protected JLabel userLabel = new JLabel("Enter username");


    void setLabelColor(Color color, ArrayList<JLabel> labels){
            for(JLabel label : labels){
                label.setForeground(color);
    	}
    }

    void makeStandardFields(){
	final NumberFormat format = NumberFormat.getInstance();
	format.setGroupingUsed(false);
	final NumberFormatter numFormatter = new NumberFormatter(format);
	numFormatter.setValueClass(Integer.class);
	numFormatter.setMinimum(0);
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
    }

    abstract String getServerName();

    abstract int getPort();

    abstract String getNickname();

    abstract String getUsername();

    abstract String getRealName();

    abstract void handleDialog();

    abstract int makeDialog();

}
