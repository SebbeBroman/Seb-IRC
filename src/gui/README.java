package gui;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.logging.Level;
import java.util.logging.Logger;

import static gui.ClientGui.initLogger;

/**
 * Simple class that reads a text file and opens a new frame to show the text.
 */

public class README
{
    private boolean read;
    private final static Logger LOGGER = Logger.getLogger(README.class.getName());

    protected README() {
	initLogger("README");
	JFrame readme = new JFrame("README.txt");
	readme.setPreferredSize(new Dimension(500, 700));
	String text = "Could not find README";
	try{
	    text = Files.readString(Paths.get("README.txt"), Charset.defaultCharset());
	    read = true;
	}catch(IOException e){
	    read = false;
	    LOGGER.log(Level.WARNING, e.toString(), e);
	    //e.printStackTrace();
	}
	JTextArea readmeTextField = new JTextArea(text);
	readmeTextField.setLineWrap(true);
	readmeTextField.setWrapStyleWord(true);
	readmeTextField.setEditable(false);
        readmeTextField.setPreferredSize(new Dimension(400,850));
        final JScrollPane scrollPane = new JScrollPane(readmeTextField);
        readme.add(scrollPane);
        readme.pack();
	readme.setVisible(true);
    }

    protected boolean isRead() {
	return read;
    }
}
