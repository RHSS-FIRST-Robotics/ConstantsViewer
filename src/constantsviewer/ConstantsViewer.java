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
import javafx.geometry.Pos;
import javafx.geometry.Rectangle2D;
import javafx.scene.Group;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableColumn.CellEditEvent;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.effect.BlendMode;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.TilePane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.stage.Screen;
import utilities.Constant;


/**
 *
 * @author Vidyasagar
 */
public class ConstantsViewer extends Application {
    
    String fileName = "constants";
    String filePath = "C:\\" + fileName + ".txt";
    ConstantsFileReaderWriter consts = new ConstantsFileReaderWriter(fileName, filePath);
    private TableView table = new TableView();
    final HBox hb = new HBox();
    final HBox labelHB = new HBox();
    
    @Override
    public void start(Stage stage) {

        consts.processLineByLine();
        
        consts.hashToConstantArray();
        
        final ObservableList<Constant> data = FXCollections.observableArrayList();
        
        for (int i = 0; i < consts.getArrayLength(); i++) {
            data.add(consts.getConstArrayAtIndex(i));
        } //populate table with inital values
        
        Scene scene = new Scene(new Group());
        stage.setScene(scene);
        stage.setTitle(fileName + ".txt");
        stage.setResizable(false);

        //stage.setWidth(350);
        //stage.setHeight(540);
        scene.getStylesheets().add
        (ConstantsViewer.class.getResource("ConstantsViewer.css").toExternalForm());
 
        final Label label = new Label("Constants File Editor");
        label.setId("Constants-File-Editor");
 
        table.setEditable(true);
 
        TableColumn firstCol = new TableColumn("Constant");
            firstCol.setMinWidth(220);
            firstCol.setCellValueFactory(
            new PropertyValueFactory<Constant, String>("key"));
            
        TableColumn secondCol = new TableColumn("Value");
                secondCol.setMinWidth(67);
            secondCol.setCellValueFactory(
            new PropertyValueFactory<Constant, Object>("val"));
            secondCol.setResizable(false);
        
        table.getColumns().addAll(firstCol, secondCol);
        //table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY); //so that only two columns get made
        
        table.setItems(data);
        
        final TextField addConstant = new TextField();
        addConstant.setPromptText("Constant");
        addConstant.setMaxWidth(firstCol.getPrefWidth());
        
        final TextField addValue = new TextField();
        addValue.setMaxWidth(secondCol.getPrefWidth());
        addValue.setPromptText("Value");

        final Button addButton = new Button("Add");
                addButton.setId("Add-Button");
        addButton.getStylesheets().add
        (ConstantsViewer.class.getResource("ConstantsViewer.css").toExternalForm());
        addButton.setOnAction(new EventHandler<ActionEvent>() {
            
            @Override 
            public void handle(ActionEvent e) {
                data.add(new Constant( addConstant.getText(), String.valueOf(addValue.getText())));
                consts.writeConstant(addConstant.getText(), String.valueOf(addValue.getText()));
                addConstant.clear();
                addValue.clear();

            }       
        }
        );
        
        
        final Button delButton = new Button("Delete Selected");
        delButton.setId("Delete-Button");
        delButton.getStylesheets().add
        (ConstantsViewer.class.getResource("ConstantsViewer.css").toExternalForm());
        delButton.setOnAction(new EventHandler<ActionEvent>() {

            @Override
            public void handle(ActionEvent e) {
                Constant delConst = (Constant)table.getSelectionModel().getSelectedItem();
                data.remove(delConst);

                consts.deleteConstant(delConst.getKey() + " = " + delConst.getVal(), delConst.getKey());
                table.getSelectionModel().clearSelection();
                
            }
        });
        
        hb.getChildren().addAll(addConstant, addValue, addButton, delButton);
        hb.setSpacing(3);

        firstCol.setCellFactory(TextFieldTableCell.forTableColumn());
        firstCol.setOnEditCommit(
        new EventHandler<CellEditEvent<Constant, String>>() {
            @Override
            public void handle(CellEditEvent<Constant, String> t) {

                consts.editConstantKey(t.getRowValue().getKey(), t.getNewValue());

                ((Constant) t.getTableView().getItems().get(
                    t.getTablePosition().getRow())
                    ).setKey(t.getNewValue());
            }
        }
        );
        
        secondCol.setCellFactory(TextFieldTableCell.forTableColumn());
        secondCol.setOnEditCommit(
        new EventHandler<CellEditEvent<Constant, Object>>() {
            @Override
            public void handle(CellEditEvent<Constant, Object> t) {

                consts.editConstantVal(t.getRowValue().getKey(), t.getNewValue());
                System.out.println("edited " + t.getRowValue().getKey() + " = " + t.getNewValue());
                ((Constant) t.getTableView().getItems().get(
                    t.getTablePosition().getRow())
                    ).setVal(t.getNewValue());

            }
        }
        );
        
        final VBox vbox = new VBox();
        vbox.setSpacing(5);
        vbox.setPadding(new Insets(10, 0, 0, 10));
        vbox.getChildren().addAll(label, table, hb);
        
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
