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
import java.io.InputStreamReader;
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
  private final String sFilePath; 
  private final String sFileName;

  public static String newLine = System.getProperty("line.separator");
  private static Hashtable constants = new Hashtable();
  
  Constant[] constArray;
  
 /*
  * Constructor.
  * @param aFileName full name of an existing, readable file.
  */
  public ConstantsFileReaderWriter(String fileName, String filePath){
    this.filePath = Paths.get(filePath);
    sFilePath = filePath;
    sFileName = fileName;
  }
  
  /*
   * Overridable method for processing lines in different ways.
   * This simple default implementation expects simple name-value pairs, separated by an 
   * '=' sign just like the format of the constants file on the robot.
   */
    public void processLineByLine(){

        try {
            BufferedReader br = new BufferedReader(new FileReader("C:\\" + sFileName + ".txt"));
            PrintWriter out = new PrintWriter(new BufferedWriter(new FileWriter(sFilePath, true)));

            String line = null;
            String prevLine = null;
            int lineNum = 0;
            while((line = br.readLine()) != null){
                lineNum ++;
                if(line.length() != 0 && line.charAt(0) != '#'){
                    int numSignPos = line.indexOf("#");
                    if(numSignPos > 0){
                        line = line.substring(0, numSignPos);
                    }

                    int equalsSignPos = line.indexOf("=");
                    if(equalsSignPos <= 1){
                        log("INVALID SETTING LINE: " + line + " (" + lineNum + ")");
                    }else{
                        String key = line.substring(0, equalsSignPos - 1).trim();
                        String value = line.substring(equalsSignPos + 1).trim();

                        if(key.length() > 0 && value.length() > 0){
                            log("putting to HashTable: " + key + " = " + value);
                            constants.put(key, value);
                        }
                    }
                }
                prevLine = line;
            }
            if (prevLine == ""){
                out.println();
            }
        }
        catch(Exception e) {
            e.printStackTrace();
        }
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
    
    public void deleteConstant(String constToRemove, String constKey) {
       
//        File inputFile = new File(sFilePath);
//        File tempFile = new File("C://" + "temp" + sFileName + ".txt");
//        try {
//            BufferedReader reader = new BufferedReader(new FileReader(inputFile));
//            BufferedWriter writer = new BufferedWriter(new FileWriter(tempFile));
//
//        String currentLine = null;
//
//        while((currentLine = reader.readLine()) != null) {
//            // trim newline when comparing with lineToRemove
//            String trimmedLine = currentLine.trim();
//            if(trimmedLine.equals(constToRemove)) continue;
//            writer.write(currentLine);
//            writer.write(newLine);
//        }
//        
//        reader.close();
//        writer.close();
//        
//          //Delete the original file
////    if (!inputFile.delete()) {
////      System.out.println("Could not delete file");
////      return;
////    }
//        //inputFile.delete();
//
//    //Rename the new file to the filename the original file had.
////    if (!tempFile.renameTo(inputFile))
////      System.out.println("Could not rename file");
//    //tempFile.renameTo(inputFile);
//        inputFile.delete();
//        constants.remove(constKey);
//    }
//        catch (IOException e){}
        
        
        try {

          File inFile = new File(sFilePath);

          if (!inFile.isFile()) {
            System.out.println("Parameter is not an existing file");
            return;
          }

          //Construct the new file that will later be renamed to the original filename.
          File tempFile = new File("C://" + "temp" + sFileName + ".txt");

          BufferedReader br = new BufferedReader(new FileReader(sFilePath));
          PrintWriter pw = new PrintWriter(new FileWriter(tempFile));

          String line = null;

          //Read from the original file and write to the new
          //unless content matches data to be removed.
          while ((line = br.readLine()) != null) {

            if (!line.trim().equals(constToRemove)) {

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

            File inFile = new File(sFilePath);

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

            String[] newConstants = newFileLines.split(newLine);
            for (String constant: newConstants) {
                writer.write(constant);
                writer.write(newLine);
            }
            constants.put(newKey, constants.get(oldKey));
            br.close();
            br2.close();
            writer.close();
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
                System.out.println("in first while");
                newFileLines += line + newLine;
            }

            BufferedReader br2 = new BufferedReader(new FileReader(inFile.getAbsolutePath()));

            while ((line = br2.readLine()) != null){

                if (line.trim().contains(key)) {
                    replacedLine = line.replace(String.valueOf(constants.get(key)), String.valueOf(newVal)); //fix issue here with strings String.valueOf(newVal)

                    newFileLines = newFileLines.replace(line, replacedLine);
                }
                System.out.println(replacedLine + " " + String.valueOf(newVal) + " " + line + " " + String.valueOf(constants.get(key)));
            }

            BufferedWriter writer = new BufferedWriter(new FileWriter(sFilePath));

            String[] newConstants = newFileLines.split(newLine);
            for (String constant: newConstants) {
                writer.write(constant);
                writer.write(newLine);
            }
            constants.put(key, newVal);
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
    
