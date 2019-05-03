package GUI;

import net.miginfocom.swing.MigLayout;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


/**
 * This class creates tabs on a JTabbedPane, cause each tab contain so much separate information
 * it's easier to handle in a separate class. All tabs function in the same way but contains
 * separate messages and user lists.
 */

class ChannelTab
{
    private final String channel;

    String getChannel() {
	return channel;
    }

    private final JTabbedPane parentPane;
    private final JTextArea messageBox = new JTextArea();
    private final JScrollPane messagePane = new JScrollPane(messageBox);
    private final List<String> users;
    private JTextArea topic;

    void setTopic(final String topic) {
	this.topic.setText(topic);
	System.out.println("Ran setTopic in " + channel);
    }

    ChannelTab(final String channel, final JTabbedPane tb) {
	this.channel = channel;
	this.parentPane = tb;
	users = new ArrayList<>();
	setSettings();
	restoreTab();
    }

    @SuppressWarnings("unused") private void closeTab(final ActionEvent actionEvent) {
	//should do this no matter what event, event needed for the listener to work
	removeTab();
    }

    void writeToTab(String message){
	messageBox.append("[" + new SimpleDateFormat("HH:mm").format(new Date()) + "] " + message + "\n");
	messageBox.setCaretPosition(messageBox.getDocument().getLength());
    }

    void addUser(String username){
        if (!users.contains(username)){
	    users.add(username);
	}
    }

    void removeTab(){
        parentPane.remove( parentPane.indexOfTab(this.channel));
    }

    List<String> getUsers() {
	return users;
    }
    void restoreTab(){
	JPanel tabWindow = new JPanel(new MigLayout("","[grow]","[]10[grow]"));
	tabWindow.add(topic, "Span 1, grow, wrap");
	tabWindow.add(messagePane, "Span 1, grow");
	parentPane.addTab(channel, tabWindow);
	JButton closeButton = new JButton("x");
	closeButton.setMargin(new Insets(0, 0, 0, 0));
	closeButton.setBorder(BorderFactory.createEmptyBorder());
	closeButton.setToolTipText("close this tab");
	closeButton.addActionListener(this::closeTab);
	JPanel panelTab = new JPanel(new MigLayout("","[grow]10[]", "[][]"));
	panelTab.add(new JLabel(channel),"span 1 2, grow");
	panelTab.add(closeButton,"span 1 1");
	panelTab.setOpaque(false);
	parentPane.setTabComponentAt(parentPane.indexOfTab(channel),panelTab);
    }
    private void setSettings(){
	messagePane.setPreferredSize(new Dimension(600, 300));
	messageBox.setLineWrap(true);
	messageBox.setWrapStyleWord(true);
	messageBox.setEditable(false);
	messagePane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
	topic = new JTextArea("");
	topic.setEditable(false);
	topic.setWrapStyleWord(true);
	topic.setLineWrap(true);
    }
}
