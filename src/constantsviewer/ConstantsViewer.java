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
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.Group;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import utilities.Constant;


/**
 *
 * @author Vidyasagar
 */
public class ConstantsViewer extends Application {
    
    String fileName = "constants";
    String folderPath;
    ConstantsFileReaderWriter consts = new ConstantsFileReaderWriter(fileName);
    private TableView table = new TableView();
    final HBox hb = new HBox();
    
    @Override
    public void start(Stage stage) {
        //populateTable();

        try {
            consts.processLineByLine();
        } catch (IOException e) {
            System.out.println("Error while reading file");
        }
        
        consts.hashToConstantArray();
        
        final ObservableList<Constant> data = FXCollections.observableArrayList();
        
        for (int i = 0; i < consts.getArrayLength(); i++) {
            data.add(consts.getConstArrayAtIndex(i));
        } //populate table with inital values
                    
        Scene scene = new Scene(new Group());
        stage.setTitle(fileName);
        stage.setWidth(285);
        stage.setHeight(515);
 
        final Label label = new Label("Constants File Editor");
        label.setFont(new Font("Arial", 20));
 
        table.setEditable(true);
 
        TableColumn firstCol = new TableColumn("Constant");
            firstCol.setMinWidth(100);
            firstCol.setCellValueFactory(
            new PropertyValueFactory<Constant, String>("key"));
            
        TableColumn secondCol = new TableColumn("Value");
            secondCol.setMinWidth(100);
            secondCol.setCellValueFactory(
            new PropertyValueFactory<Constant, Object>("val"));
        
        table.getColumns().addAll(firstCol, secondCol);
        table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY); //so that only two columns get made
        
        table.setItems(data);
        
        final TextField addConstant = new TextField();
        addConstant.setPromptText("Constant");
        addConstant.setMaxWidth(firstCol.getPrefWidth());
        
        final TextField addValue = new TextField();
        addValue.setMaxWidth(secondCol.getPrefWidth());
        addValue.setPromptText("Value");

        final Button addButton = new Button("Add");
        addButton.setOnAction(new EventHandler<ActionEvent>() {
            
        @Override 
        public void handle(ActionEvent e) {
            data.add(new Constant( addConstant.getText(), Double.valueOf(addValue.getText())));
            consts.writeConstant(addConstant.getText(), Double.valueOf(addValue.getText()));
            addConstant.clear();
            addValue.clear();

        }       
        }
        );
        
        hb.getChildren().addAll(addConstant, addValue, addButton);
        hb.setSpacing(3);
        
        final Button delButton = new Button("Delete");
        delButton.setOnAction(new EventHandler<ActionEvent>() {

            @Override
            public void handle(ActionEvent e) {
                Constant delConst = (Constant)table.getSelectionModel().getSelectedItem();
                data.remove(delConst);

                consts.deleteConstant(delConst.getKey());
                table.getSelectionModel().clearSelection();
                
            }
        });
        
        
        final VBox vbox = new VBox();
        vbox.setSpacing(5);
        vbox.setPadding(new Insets(10, 0, 0, 10));
        vbox.getChildren().addAll(label, table, hb, delButton);
 
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
