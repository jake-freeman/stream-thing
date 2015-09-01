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
    private final String CHAR_PATH = "chars";

    //private char[]   currentColors;

    private JButton[]   colors;
    private JComboBox[] p;

    public STFrame()
    {
        super("Stream Thing");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false);

        ArrayList<File>   fileList = getFileList(CHAR_PATH);
        ArrayList<String> charList = getCharList(fileList);

        colors  = new JButton[12];
        for (int i = 0; i < colors.length; i++)
            colors[i] = new JButton();

        p       = new JComboBox[2];
        p[0]    = new JComboBox<Object>(charList.toArray());
        p[1]    = new JComboBox<Object>(charList.toArray());

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
        for (int i = 0; i < p.length; i++)
        {
            final int j = i;
            p[i].addActionListener
            (
                new ActionListener()
                {
                    public void actionPerformed(ActionEvent e)
                    {
                        System.out.println("Selected(" + j + "): " + (String)((JComboBox)e.getSource()).getSelectedItem());
                        charSelect((String)((JComboBox)e.getSource()).getSelectedItem(), 'A', CHAR_PATH);
                    }
                }
            );
        }

        for (int i = 0; i < colors.length; i++)
        {
            final int j = i;
            colors[i].addActionListener
            (
                new ActionListener()
                {
                    public void actionPerformed(ActionEvent e)
                    {
                        System.out.println("Selected(p" + j + "): " + (String)((JButton)e.getSource()).getText());
                        charSelect((String)((JButton)e.getSource()).getText(), 'A'/*[HACK]*/, CHAR_PATH);
                    }
                }
            );
        }
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
        c.gridwidth = 6;
        c.insets = new Insets(20,10,5,10);  // padding
        c.gridx = 0;
        c.gridy = 0;
        pane.add(p[0], c);

        c.gridx = 6;
        pane.add(p[1], c);

        c.gridy = 1;
        c.gridwidth = 1;
        c.insets = new Insets(5,5,10,5);
        for (int i = 0; i < colors.length; i++)
        {
            c.gridx = i;
            pane.add(colors[i],c);
        }
    }

    /**
    * Include code to handle character changes to
    * update files and button colors.
    *
    * @param charName name of character currently selected
    * @param color    
    * @param charPath path to the character folder
    */
    private void charSelect(String charName, char color, String charPath)
    {
        String fileNameBase = charName.replace(" ", "-");
        // select character
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
                // omits 'A' at the beginning of the string and replaces dashes with spaces
                chars.add(character.substring(0, character.length() - 1).replace("-", " "));
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
