import java.util.ArrayList;
import java.util.List;

public class ChannelTabList
{
    List<ChannelTab> tabs;

    public ChannelTabList() {
	this.tabs = new ArrayList();
    }

    public void addToList(ChannelTab tab){
        tabs.add(tab);
    }

    public void removeFromList(int index){
        tabs.remove(index);
    }
}
