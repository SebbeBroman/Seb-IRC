import javax.swing.*;
import java.awt.event.*;
import java.awt.*;
import java.util.*;

public class DClickJList extends JPanel {
 public DClickJList() {
  JList list = new JList();;
  Vector data = new Vector();;
  data.addElement("line 1");
  data.addElement("line 2");
  data.addElement("line 3");
  list.setListData(data);
  list.setSelectedIndex(0);
  list.addMouseListener(new ActionJList(list));
  add(new JScrollPane(list));
  }

 public Dimension getPreferredSize() {
  return new Dimension(100, 100);
  }
    
 public static void main(String s[]){
  JFrame frame = new JFrame("DClickJList");
  DClickJList panel = new DClickJList();
        
  frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
  frame.getContentPane().add(panel,"Center");
  frame.setSize(panel.getPreferredSize());
  frame.setVisible(true);
  }
}

class ActionJList extends MouseAdapter{
  protected JList list;
    
  public ActionJList(JList l){
   list = l;
   }
    
  public void mouseClicked(MouseEvent e){
   if(e.getClickCount() == 2){
     int index = list.locationToIndex(e.getPoint());
     ListModel dlm = list.getModel();
     Object item = dlm.getElementAt(index);;
     list.ensureIndexIsVisible(index);
     System.out.println("Double clicked on " + item);
     }
   }
}