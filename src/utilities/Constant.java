/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package utilities;

import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;

/**
 *
 * @author Vidyasagar
 */
public class Constant {
        
    private final SimpleStringProperty key;
    private final SimpleObjectProperty val;
 
    public Constant(String key, Object val) {
        this.key = new SimpleStringProperty(key);
        this.val = new SimpleObjectProperty(val);
    }
 
    public String getKey() {
        return key.get();
    }
    public void setKey(String fName) {
        key.set(fName);
    }
    public Object getVal() {
        return val.get();
    }
    public void setVal(Object value) {
        val.set(value);
    }       
}