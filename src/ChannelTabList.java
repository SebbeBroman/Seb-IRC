import javax.swing.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

public class ChannelTabList
{
    private List<ChannelTab> tabs;
    private List<String> channelNames;
    private JTabbedPane parentPane;

    public ChannelTabList(JTabbedPane tb) {
	this.tabs = new ArrayList();
	this.channelNames = new ArrayList();
        this.parentPane = tb;
    }

    public void addChannel(ChannelTab tab){
        tabs.add(tab);
        channelNames.add(tab.getChannel());
    }

    public void removeChannel(String name){
        int index = channelNames.indexOf(name);
        tabs.get(index).removeTab();
        tabs.remove(index);
        channelNames.remove(index);
    }

    public void writeToChannel(String channel, String message){
        if(channelNames.contains(channel)){
            tabs.get(channelNames.indexOf(channel)).writeToTab(message);
        } else{
            this.addChannel(new ChannelTab(channel, parentPane));
            writeToChannel(channel,message);
        }

    }

    public void addUser(String user, String channel){
        tabs.get(channelNames.indexOf(channel)).addUser(user);
    }

    public Vector getUsers(String channel){
        return tabs.get(channelNames.indexOf(channel)).getUsers();
    }

}
