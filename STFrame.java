/**
 * STFrame is an extension of JFrame that contains
 * the logic required for the StreamThing program.
 *
 * @author jake-freeman
 *
 */

// UI components
import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.awt.event.*;

// Image handling
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;

// File and Array handling
import java.util.*;
import java.io.File;
import java.nio.file.Files;
import java.nio.file.Paths;

// Copy option enum for Files.copy
import static java.nio.file.StandardCopyOption.*;

public class STFrame extends JFrame
{
    private final String[] CHAR_PATH  = {"chars/left","chars/right"};
    private final int      MAX_COLORS = 6;

    private JButton[]   colors;
    private JComboBox[] p;

    public STFrame()
    {
        super("Stream Thing");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false);

        ArrayList<File>   fileList = getFileList(CHAR_PATH[0]);
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

        // So the initial characters begin as selected
        charSelect(0, 0, CHAR_PATH[0]);
        charSelect(1, 0, CHAR_PATH[1]);

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
                        charSelect(j, 0, CHAR_PATH[j]);
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
                        charSelect(j / 6, j % MAX_COLORS, CHAR_PATH[j / 6]);
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
    * @param pIndex   index of the selected player
    * @param cIndex   index of the selected color
    * @param charPath path to the character folder
    */
    private void charSelect(int pIndex, int cIndex, String charPath)
    {
        String charFileName = ((String)p[pIndex].getSelectedItem()).replace(" ", "-");

        colorButtons(pIndex, cIndex, getColorsForChar(charFileName));

        try
        {
            Files.copy(getFileList(charPath, charFileName).get(cIndex).toPath(), 
                       Paths.get("Active" + (pIndex + 1) + ".png"), REPLACE_EXISTING);
        }
        catch (java.io.IOException e)
        {
            System.out.println("File copy failed!");
        }
    }

    /**
    * Colors the buttons and enables or disables the
    * buttons based on the color list and player
    * index.
    *
    * @param pIndex    the index of the player being updated
    * @param cIndex    the index of the currently selected color
    * @param colorList a list of RGB values to get the button
    *                  color to
    */
    private void colorButtons(int pIndex, int cIndex, int[] colorList)
    {
        int startIndex = pIndex * 6;

        for (int i = startIndex; i < startIndex + MAX_COLORS; i++)
        {
            if (i < colorList.length + startIndex)
            {
                colors[i].setBackground(new Color(colorList[i - startIndex]));
                if (i == cIndex + startIndex)
                {
                    Border border = new LineBorder(Color.BLACK, 3);
                    colors[i].setBorder(border);
                }
                else
                {
                    colors[i].setBorder(UIManager.getBorder("Button.border"));
                }
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
                // omits 'A' at the end of the string and replaces dashes with spaces
                chars.add(character.substring(0, character.length() - 1).replace("-", " "));
            }
        }
        // sorts by numerical value
        Collections.sort(chars);

        //removes sorting char at the beginning of each Melee character name
        for(int i=0;i<chars.size();i++)
        {
            String name=chars.get(i);
            chars.set(i,name.substring(1,name.length()));
        }

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
        ArrayList<File> fileList = getFileList(CHAR_PATH[0], charFileName);
        int[] colorList = new int[fileList.size()];

        int i = 0;
        for (File f: fileList)
        {
            try
            {
                BufferedImage img = ImageIO.read(f);
                // get color from bottom left corner
                int charColor = img.getRGB(0, img.getHeight() - 1);
                colorList[i] = charColor;
                i++;
            }
            catch (java.io.IOException e)
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
            String no=it.next().getName();
            if (!no.substring(1,no.length()).startsWith(charFileName))
                it.remove();
        }
        Collections.sort(fileList);
        return fileList;
    }
}
