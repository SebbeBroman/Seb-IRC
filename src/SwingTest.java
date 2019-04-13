import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class SwingTest extends JFrame implements ActionListener
{
    JTextField txtdata;
    JButton calbtn = new JButton("send");

    public SwingTest()
    {
        JPanel myPanel = new JPanel();
        add(myPanel);
        myPanel.setLayout(new GridLayout(3, 2));
        myPanel.add(calbtn);
        calbtn.addActionListener(this);
        txtdata = new JTextField();
        myPanel.add(txtdata);
    }

    public void actionPerformed(ActionEvent e)
    {
        if (e.getSource() == calbtn) {
            String data = txtdata.getText(); //perform your operation
            //System.out.println(data);
        }
    }
}