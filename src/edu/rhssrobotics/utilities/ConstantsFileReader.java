/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.rhssrobotics.utilities;

import java.io.IOException;
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
public class ConstantsFileReader {
    
  private final Path filePath;
  private final static Charset ENCODING = StandardCharsets.UTF_8;  
  private  static Hashtable constants = new Hashtable();
  
 /*
  * Constructor.
  * @param aFileName full name of an existing, readable file.
  */
  public ConstantsFileReader(String fileName){
    filePath = Paths.get(fileName);
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
      
      constants.put(key, value);
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

   public static String getString(String constName) {
        Object val = constants.get(constName);
        System.out.println("Reading String From Hash: " + val);
        if (val == null){
            System.out.println("Failed to return constant: " + constName);
            return "";
        }else{
            return (String) val;
        }
    }
    
    public static double getDouble (String constName) {
        try {
            double val = Double.parseDouble(getString(constName));
            return val;
        } catch (Exception e) {
            return 0;
        }
    }
    
    public static int getInteger(String constName) {
        try {
            int val = Integer.parseInt(getString(constName));
            return val;
        } catch (Exception e) {
            return 0;
        }
    }
    
    public static long getLong(String constName) {
        try {
            long val = Long.parseLong(getString(constName));
            return val;
        } catch(NumberFormatException e){
            return 0;
        }
    }
    
    public static boolean getBoolean (String constName) {
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
    
