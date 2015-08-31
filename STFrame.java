/**
 * STFrame is an extension of JFrame that contains
 * the logic required for the StreamThing program.
 *
 * @author jake-freeman
 *
 */

import javax.swing.*;
import java.awt.*;
import java.util.*;
import java.io.*;

public class STFrame extends JFrame
{
    public STFrame()
    {
        super("Stream Thing");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        setVisible(true);
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
            // [HACK]: Files should be named differently
            String character = f.getName().replace(".png", "");
            if (!Character.isUpperCase(character.charAt(character.length() - 1)))
                chars.add(character);
        }
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
