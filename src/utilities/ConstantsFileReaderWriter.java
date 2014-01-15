/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package utilities;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Scanner;
import java.util.Hashtable;

/**
 *
 * @author Vidyasagar
 */
public class ConstantsFileReaderWriter {
    
  private final Path filePath; //"C:\\Temp\\test.txt" in this form
  private final String sFilePath; //"C:\\Temp\\test.txt" in this form
  private final static Charset ENCODING = StandardCharsets.UTF_8;  
  private  static Hashtable constants = new Hashtable();
  
 /*
  * Constructor.
  * @param aFileName full name of an existing, readable file.
  */
  public ConstantsFileReaderWriter(String fileName){
    filePath = Paths.get(fileName);
    sFilePath = fileName;
  }
  
  public final void processLineByLine() throws IOException {
    try (Scanner scanner =  new Scanner(filePath, ENCODING.name())){
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
    Scanner scanner = new Scanner(aLine);
    scanner.useDelimiter("=");
    if (scanner.hasNext()){
      //assumes the line has a certain structure
      String key = scanner.next();
      String value = scanner.next();
      log(key.trim() + " = " + value.trim());
      
      constants.put(key.trim(), value.trim());
    }
    else {
      log("Empty or invalid line. Unable to process.");
    }
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
    
