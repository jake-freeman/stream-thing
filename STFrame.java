/**
 * STFrame is an extension of JFrame that contains
 * the logic required for the StreamThing program.
 *
 * @author jake-freeman
 *
 */

// [HACK]: make imports more specific
import javax.swing.*;
import javax.imageio.*;

import java.awt.*;
import java.awt.event.*;
import java.awt.image.*;

import java.util.*;
import java.io.*;

public class STFrame extends JFrame
{
    private final String CHAR_PATH  = "chars";
    private final int    MAX_COLORS = 6;

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
        {
            colors[i] = new JButton();
            colors[i].setPreferredSize(new Dimension(30,15));
        }

        p       = new JComboBox[2];
        p[0]    = new JComboBox<Object>(charList.toArray());
        p[1]    = new JComboBox<Object>(charList.toArray());

        p[0].setPreferredSize(new Dimension(200, 20));
        p[1].setPreferredSize(new Dimension(200, 20));

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
                        String character = (String)((String)((JComboBox)e.getSource()).getSelectedItem()).replace(" ", "-");
                        System.out.println("Selected(" + j + "): " + character);
                        colorButtons(j, getColorsForChar(character));
                        charSelect(character, 'A', CHAR_PATH);
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
                        System.out.println("Selected(c" + j % MAX_COLORS + "): " + (String)((JButton)e.getSource()).getText());
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
    * @param charFileName name of character file currently selected
    * @param color        [HACK]: This may change form
    * @param charPath     path to the character folder
    */
    private void charSelect(String charFileName, char color, String charPath)
    {

    }

    /**
    * Colors the buttons and enables or disables the
    * buttons based on the color list and player
    * index.
    *
    * @param pIndex    the index of the player being updated
    * @param colorList a list of RGB values to get the button
    *                  color to
    */
    private void colorButtons(int pIndex, int[] colorList)
    {
        int startIndex = pIndex * 6;

        for (int i = startIndex; i < startIndex + MAX_COLORS; i++)
        {
            System.out.println("i is: " + i);
            if (i < colorList.length + startIndex)
            {
                System.out.println("Button Color: " + Integer.toHexString(colorList[i - startIndex]));
                colors[i].setBackground(new Color(colorList[i - startIndex]));
                colors[i].setEnabled(true);
                colors[i].setOpaque(true);
                colors[i].setContentAreaFilled(true);
                colors[i].setBorderPainted(true);
            }
            else
            {
                colors[i].setOpaque(false);
                colors[i].setContentAreaFilled(false);
                colors[i].setBorderPainted(false);
                colors[i].setEnabled(false);
            }
        }
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
    * Gets the available colors for a certain character.
    *
    * @param  charFileName the partial file name for the char
    * @return              an array of RGB colors
    */
    private int[] getColorsForChar(String charFileName)
    {
        ArrayList<File> fileList = getFileList(CHAR_PATH, charFileName);
        int[] colorList = new int[fileList.size()];

        int i = 0;
        for (File f: fileList)
        {
            try
            {
                System.out.println("Current file: " + f.getName());
                BufferedImage img = ImageIO.read(f);
                // get color from bottom left corner
                int charColor = img.getRGB(0, img.getHeight() - 1);
                System.out.println("Color: " + Integer.toHexString(charColor));
                colorList[i] = charColor;
                i++;
            }
            catch (IOException e)
            {
                System.out.println("Failed to read file: " + f.getName());
            }
        }
        return colorList;
    }

    /**
    * Gets a list of the image files in the form of an
    * abstract file list.
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

    /**
    * Gets a list of the image files for a character in the
    * form of an abstract file list.
    *
    * @param path         the directory to create the file
    *                     list of
    * @param charFileName the character to make the list from
    * @return             a list of files in the given directory
    *                     for the given character
    * @see                ArrayList
    */
    private ArrayList<File> getFileList(String path, String charFileName)
    {
        ArrayList<File> fileList = getFileList(path);
        for (Iterator<File> it = fileList.iterator(); it.hasNext();)
        {
            if (!it.next().getName().startsWith(charFileName))
                it.remove();
        }
        Collections.sort(fileList);
        return fileList;
    }
}
