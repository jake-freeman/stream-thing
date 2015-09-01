/**
 * STFrame is an extension of JFrame that contains
 * the logic required for the StreamThing program.
 *
 * @author jake-freeman
 *
 */

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;
import java.io.*;

public class STFrame extends JFrame
{
    private JButton[] colors1;
    private JButton[] colors2;

    private JComboBox p1;
    private JComboBox p2;

    public STFrame()
    {
        super("Stream Thing");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false);

        ArrayList<File>   fileList = getFileList("chars");
        ArrayList<String> charList = getCharList(fileList);

        colors1 = new JButton[6];
        colors2 = new JButton[6];
        p1      = new JComboBox(charList.toArray());
        p2      = new JComboBox(charList.toArray());

        addActionListeners();
        addComponentsToPane(getContentPane());

        pack();
        setVisible(true);
    }

    /**
    * Adds all required action listeners to combo boxes
    * and buttons.
    */
    private void addActionListeners()
    {
        p1.addActionListener
        (
            new ActionListener()
            {
                public void actionPerformed(ActionEvent e)
                {
                    System.out.println("Selected: " + (String)((JComboBox)e.getSource()).getSelectedItem());
                    charSelect((String)((JComboBox)e.getSource()).getSelectedItem());
                }
            }
        );
    }

    /**
    * Function to add components to the main content area.
    *
    * @param pane the content area to add UI items to
    * 
    */
    private void addComponentsToPane(Container pane)
    {
        pane.setLayout(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();

        c.fill = GridBagConstraints.HORIZONTAL;
        c.insets = new Insets(20,10,20,10);  // padding
        c.gridx = 0;
        c.gridy = 0;
        pane.add(p1, c);

        c.gridx = 6;
        pane.add(p2, c);
    }

    /**
    * Creates a list of character names based on file list
    *
    * @param  files an ArrayList of files to extract names from
    * @return       a list of character names from file list
    * @see          ArrayList
    */
    private ArrayList<String> getCharList(ArrayList<File> files)
    {
        ArrayList<String> chars = new ArrayList<String>();
        for (File f: files)
        {
            String character = f.getName().replace(".png", "");
            // this effectively picks the default character file for a name
            if (character.charAt(character.length() - 1) == 'A')
            {
                // omits 'A' at the begining of the string
                character = character.substring(0, character.length() - 1);
                // quick and dirty translation table for longer character names
                switch (character)
                {
                    case "Captain":
                        character = "Captain-Falcon";
                        break;
                    case "Donkey":
                        character = "Donkey-Kong";
                        break;
                    case "Dr":
                        character = "Dr-Mario";
                        break;
                    case "Ice":
                        character = "Ice-Climbers";
                        break;
                    case "Mr":
                        character = "Mr-Game-and-Watch";
                        break;
                    case "Young":
                        character = "Young-Link";
                        break;
                }
                chars.add(character);
            }
        }
        // sorts by numerical value
        Collections.sort(chars);
        return chars;
    }

    /**
    * Gets a list of the image files in the form of an
    * abstract file list
    *
    * @param path the directory to create the file list of
    * @return     a list of files in the given directory
    * @see        ArrayList
    */
    private ArrayList<File> getFileList(String path)
    {
        File dir = new File(path);
        File[] fileListArray = dir.listFiles();
        return new ArrayList<File>(Arrays.asList(fileListArray));
    }
}
