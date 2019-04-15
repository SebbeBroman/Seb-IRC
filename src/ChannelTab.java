import javax.swing.*;
import java.awt.*;
import java.text.SimpleDateFormat;
import java.util.Date;

public class ChannelTab
{
    private String channel;
    private JTabbedPane parentPane;
    private JTextArea messageBox = new JTextArea();
    private JScrollPane messagePane = new JScrollPane(messageBox);

    public ChannelTab(final String channel, final JTabbedPane tb) {
	this.channel = channel;
	this.parentPane = tb;
	messagePane.setPreferredSize(new Dimension(380, 100));
	messageBox.setLineWrap(true);
	messageBox.setWrapStyleWord(true);
	messageBox.setEditable(false);
	messagePane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
	parentPane.addTab(channel, messagePane);
    }

    public void writeToTab(String message){
	messageBox.append("[" + new SimpleDateFormat("HH:mm").format(new Date()) + "] " + message + "\n");
	messageBox.setCaretPosition(messageBox.getDocument().getLength());
    }


}
