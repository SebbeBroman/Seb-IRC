package gui;

import javax.swing.*;
import java.util.ArrayList;
import java.util.List;

/**
 * This is a class that handles all of the gui.ChannelTab's.
 */

public class ChannelTabList
{
    private final List<ChannelTab> tabs;
    private final List<String> channelNames;
    private final JTabbedPane parentPane;

    protected ChannelTabList(JTabbedPane tb) {
	this.tabs = new ArrayList<>(); // Unchecked assignment, but since its empty there is no problem
	this.channelNames = new ArrayList<>(); //Same "issue"
        this.parentPane = tb;
    }

    protected void addChannel(ChannelTab tab){
        tabs.add(tab);
        channelNames.add(tab.getChannel());
    }

    protected void setTopicOf(String channel, String topic){
        checkIfExist(channel);
        tabs.get(channelNames.indexOf(channel)).setTopic(topic);
    }

    private void checkIfExist(final String channel) {
        if(parentPane.indexOfTab(channel) == -1){ // -1 is index used by JTabbedPane to specify its not currently there
            if(channelNames.contains(channel)){
                tabs.get(channelNames.indexOf(channel)).restoreTab();
            }else{
                this.addChannel(new ChannelTab(channel, parentPane));
            }
        }
    }

    protected void removeChannel(String name){
        int index = channelNames.indexOf(name);
        tabs.get(index).removeTab();
        tabs.remove(index);
        channelNames.remove(index);
    }

    protected void writeToChannel(String channel, String message){
        checkIfExist(channel);
        tabs.get(channelNames.indexOf(channel)).writeToTab(message);
    }

    protected void addUser(String user, String channel){
        tabs.get(channelNames.indexOf(channel)).addUser(user);
    }

    protected List<String> getUsers(String channel){
        return tabs.get(channelNames.indexOf(channel)).getUsers();
    }

}
