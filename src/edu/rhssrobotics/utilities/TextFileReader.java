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

/**
 *
 * @author Vidyasagar
 */
public class TextFileReader {
    
  private final Path filePath;
  private final static Charset ENCODING = StandardCharsets.UTF_8;  
  
 /*
  * Constructor.
  * @param aFileName full name of an existing, readable file.
  */
  public TextFileReader(String fileName){
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
      String name = scanner.next();
      String value = scanner.next();
      log(name.trim() + value.trim());
    }
    else {
      log("Empty or invalid line. Unable to process.");
    }
  }
  
  private static void log(Object aObject){
    System.out.println(String.valueOf(aObject));
  }

} 
    
