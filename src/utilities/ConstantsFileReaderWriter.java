/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package utilities;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.lang.reflect.Array;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Scanner;
import java.util.Hashtable;

/**
 *
 * @author Vidyasagar
 */
public class ConstantsFileReaderWriter {
    
  private final Path filePath; //"C:\\Temp\\test.txt" in this form
  private final String sFilePath; //"C:\\Temp\\test.txt" in this form
  private final String sFileName;
  private final static Charset ENCODING = StandardCharsets.UTF_8;  
  public static String newLine = System.getProperty("line.separator");
  private static Hashtable constants = new Hashtable();
  
  Constant[] constArray;
  
 /*
  * Constructor.
  * @param aFileName full name of an existing, readable file.
  */
  public ConstantsFileReaderWriter(String fileName){
    filePath = Paths.get("C:\\" + fileName + ".txt");
    sFilePath = "C:\\" + fileName + ".txt";
    sFileName = fileName;
  }
  
  public final void processLineByLine() throws IOException {
    try (Scanner scanner =  new Scanner(filePath)){ //, ENCODING.name()
      while (scanner.hasNextLine()){
        processLine(scanner.nextLine());
      }      
    }
  }
  
  /*
   * Overridable method for processing lines in different ways.
   * This simple default implementation expects simple name-value pairs, separated by an 
   * '=' sign just like the format of the constants file on the robot.
   */
  protected void processLine(String aLine){
    //use a second Scanner to parse the content of each line 
//    Scanner scanner = new Scanner(aLine);
//    scanner.useDelimiter("=");
//    if (scanner.hasNext()){
//      //assumes the line has a certain structure
//      String key = scanner.next();
//      String value = scanner.next();
//      log(key.trim() + " = " + value.trim());
//      
//      constants.put(key.trim(), value.trim());
//    }
//    else {
//      log("Empty or invalid line. Unable to process.");
//    }
  }
  
  public Constant[] hashToConstantArray() {
      ArrayList<String> keys = Collections.list(constants.keys());
      ArrayList<Object> vals = Collections.list(constants.elements());
      constArray = new Constant[keys.size()];

      for (int i = 0; i < keys.size(); i++) {
          constArray[i] = new Constant(keys.get(i), vals.get(i));
      }
      
      return constArray;
  }
  
  public Constant getConstArrayAtIndex(int index)
  {
      return constArray[index];
  }
  
  public int getArrayLength()
  {
      return constArray.length;
  }

  public ArrayList<Object> valsToArrayList() {
      ArrayList<Object> arr = Collections.list(constants.elements());
      return arr;
  }
  
  /*
   * Simple wrapper function for System.out.println()
   */
  private static void log(Object aObject){
    System.out.println(String.valueOf(aObject));
  }
  
    public void writeConstant(String key, Object val) {
      try {
          PrintWriter out = new PrintWriter(new BufferedWriter(new FileWriter(sFilePath, true)));
          out.println(key + " = " + val);
          constants.put(key, val);
          out.close();
      } catch (IOException e) {} //exception handling left as an exercise for the reader
    }
    
    public void deleteConstant(String constToRemove){
        
        try {

        File inFile = new File("C:\\" + sFileName + ".txt");

        if (!inFile.isFile()) {
          System.out.println("Parameter is not an existing file");
          return;
        }

        //Construct the new file that will later be renamed to the original filename.
        File tempFile = new File("C:\\tmp" + sFileName + ".txt");

        BufferedReader br = new BufferedReader(new FileReader(inFile.getAbsolutePath()));
        PrintWriter pw = new PrintWriter(new FileWriter(tempFile));

        String line = null;

        //Read from the original file and write to the new
        //unless content matches data to be removed.
        while ((line = br.readLine()) != null) {
            if (!line.trim().contains(constToRemove)) {
                pw.println(line);
                pw.flush();
            }
        }
        pw.close();
        br.close();

        //Delete the original file
        if (!inFile.delete()) {
          System.out.println("Could not delete file");
          return;
        }

        //Rename the new file to the filename the original file had.
        if (!tempFile.renameTo(inFile))
          System.out.println("Could not rename file");

        }
        catch (FileNotFoundException ex) {
            ex.printStackTrace();
        }
        catch (IOException ex) {
            ex.printStackTrace();
        }
    }
    
    public void editConstantKey(String oldKey, String newKey) {
        try {

        File inFile = new File("C:\\" + sFileName + ".txt");

        if (!inFile.isFile()) {
          System.out.println("Parameter is not an existing file");
          return;
        }

        BufferedReader br = new BufferedReader(new FileReader(inFile.getAbsolutePath()));
        String newFileLines = "";
        String line = null;
        
        while ((line = br.readLine()) != null) {
            newFileLines += line + newLine;
        }
        
        BufferedReader br2 = new BufferedReader(new FileReader(inFile.getAbsolutePath()));
        
        while ((line = br2.readLine()) != null){
            
        if (line.trim().contains(oldKey)) {
            newFileLines = newFileLines.replace(oldKey, newKey);
        }
        
        }
        
        BufferedWriter writer = new BufferedWriter(new FileWriter("C:\\" + sFileName + ".txt"));
        
        String[] constants = newFileLines.split(newLine);
        for (String constant: constants) {
            writer.write(constant);
            writer.write(newLine);
        }
        
        writer.close();
        br.close();
        br2.close();
        
        }
        catch (FileNotFoundException ex) {
            ex.printStackTrace();
        }
        catch (IOException ex) {
            ex.printStackTrace();
        }
    }
    
     public void editConstantVal(String key, Object newVal) {
        try {

        File inFile = new File("C:\\" + sFileName + ".txt");

        if (!inFile.isFile()) {
          System.out.println("Parameter is not an existing file");
          return;
        }

        BufferedReader br = new BufferedReader(new FileReader(inFile.getAbsolutePath()));
        String newFileLines = "";
        String line = null;
        String replacedLine = null;
        
        while ((line = br.readLine()) != null) {
            newFileLines += line + newLine;
        }
        
        BufferedReader br2 = new BufferedReader(new FileReader(inFile.getAbsolutePath()));
        
        while ((line = br2.readLine()) != null){
            
            if (line.trim().contains(key)) {
                replacedLine = line.replace(String.valueOf(constants.get(key)), String.valueOf(newVal));
                
                newFileLines = newFileLines.replace(line, replacedLine);
            }
        
        }
        
        BufferedWriter writer = new BufferedWriter(new FileWriter("C:\\" + sFileName + ".txt"));
        
        String[] constants = newFileLines.split(newLine);
        for (String constant: constants) {
            writer.write(constant);
            writer.write(newLine);
        }
        
        writer.close();
        br.close();
        br2.close();
        
        }
        catch (FileNotFoundException ex) {
            ex.printStackTrace();
        }
        catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    public String getString(String constName) {
        Object val = constants.get(constName);
        System.out.println("Reading String From Hash: " + val);
        if (val == null){
          System.out.println("Failed to return constant: " + constName);
          return "";
        }else{
          return (String) val;
        }
    }

    public double getDouble (String constName) {
        try {
            double val = Double.parseDouble(getString(constName));
            return val;
        } catch (Exception e) {
            return 0;
        }
    }
    
    public int getInteger(String constName) {
        try {
            int val = Integer.parseInt(getString(constName));
            return val;
        } catch (Exception e) {
            return 0;
        }
    }
    
    public long getLong(String constName) {
        try {
            long val = Long.parseLong(getString(constName));
            return val;
        } catch(NumberFormatException e){
            return 0;
        }
    }
    
    public boolean getBoolean (String constName) {
        try {
            boolean val = getString(constName).toLowerCase().equals("true");
            if(getString(constName).toLowerCase().equals("false")){
                return val;
            }else{
                return true;
            }
        } catch(NumberFormatException e) {
            return false;
        }
    }
} 
    
