/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package constantsviewer;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

import utilities.ConstantsFileReaderWriter;
import java.io.IOException;
import javafx.geometry.Insets;
import javafx.scene.Group;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;


/**
 *
 * @author Vidyasagar
 */
public class ConstantsViewer extends Application {
    
    String fileName = "constants.txt";
    ConstantsFileReaderWriter consts = new ConstantsFileReaderWriter("C:\\" + fileName);
    private TableView table = new TableView();
    
    
    @Override
    public void start(Stage stage) {
        
        try {
            consts.processLineByLine();
        } catch (IOException e) {
            System.out.println("Error while reading file");
        }
        
        Scene scene = new Scene(new Group());
        stage.setTitle(fileName);
        stage.setWidth(300);
        stage.setHeight(500);
 
        final Label label = new Label("Constants File Editor");
        label.setFont(new Font("Arial", 20));
 
        table.setEditable(true);
 
        TableColumn firstCol = new TableColumn("Constant");
        TableColumn secondCol = new TableColumn("Value");
        
        table.getColumns().addAll(firstCol, secondCol);
        table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY); //so that only two columns get made
        final VBox vbox = new VBox();
        vbox.setSpacing(5);
        vbox.setPadding(new Insets(10, 0, 0, 10));
        vbox.getChildren().addAll(label, table);
 
        ((Group) scene.getRoot()).getChildren().addAll(vbox);
 
        stage.setScene(scene);
        stage.show();
        
    }

    /**
     * The main() method is ignored in correctly deployed JavaFX application.
     * main() serves only as fallback in case the application can not be
     * launched through deployment artifacts, e.g., in IDEs with limited FX
     * support. NetBeans ignores main().
     *
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }
}
