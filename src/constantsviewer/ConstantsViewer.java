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
import javafx.stage.Stage;
import utilities.ConstantsFileReaderWriter;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Bounds;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableColumn.CellEditEvent;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import utilities.Constant;

/**
 *
 * @author Vidyasagar
 */
public class ConstantsViewer extends Application {
    
    private TableView table = new TableView();
    
    final String fileName = "constants";
    
    final HBox hb = new HBox();
    final HBox hb2 = new HBox();
    
    Image img = new Image("file:1241-1285 Logo Mash.jpg");
    ImageView imagView = new ImageView(img);
    final CenteredRegion imgView = new CenteredRegion(new ImageView(img));
            
    @Override
    public void start(final Stage stage) {
            
            GridPane grid = new GridPane();
            grid.setAlignment(Pos.CENTER);
            grid.setHgap(10);
            grid.setVgap(10);
            grid.setPadding(new Insets(25, 25, 25, 25));

            Scene initScene = new Scene(grid, 300, 275);
            stage.setScene(initScene);
            
            stage.setTitle("Constants File Editor"); 
            stage.setScene(initScene); 
            stage.setWidth(340);
            stage.setHeight(505);
            
            stage.show(); 
            
            Text initSceneTitle = new Text("Constants File Editor");
            initSceneTitle.setFont(Font.font("Tahoma", FontWeight.NORMAL, 20));
            grid.add(initSceneTitle, 0, 0, 2, 1);

            Label pathToFile = new Label("Local File Path");
            grid.add(pathToFile, 0, 1);

            final TextField pathField = new TextField();
            grid.add(pathField, 1, 1);

            Label IP = new Label("Robot Target IP");
            grid.add(IP, 0, 2);

            final TextField IPField = new TextField();
            grid.add(IPField, 1, 2);

            Button btn = new Button("Continue");
            HBox hbBtn = new HBox(10);
            hbBtn.setAlignment(Pos.BOTTOM_RIGHT);
            hbBtn.getChildren().add(btn);
            grid.add(hbBtn, 1, 4);

            btn.setOnAction(new EventHandler<ActionEvent>() {

                @Override
                public void handle(ActionEvent e) {
                    
                    final ConstantsFileReaderWriter consts = new ConstantsFileReaderWriter(fileName, pathField.getText(), IPField.getText());

                    consts.processLineByLine();

                    consts.hashToConstantArray();

                    final ObservableList<Constant> data = FXCollections.observableArrayList();

                    for (int i = 0; i < consts.getArrayLength(); i++) {
                        data.add(consts.getConstArrayAtIndex(i));
                    } //populate table with inital values

                    Scene mainScene = new Scene(new Group());
                    stage.setScene(mainScene);
                    stage.setTitle(fileName + ".txt");

                    stage.setResizable(false);
                    mainScene.getStylesheets().add
                    (ConstantsViewer.class.getResource("ConstantsViewer.css").toExternalForm());

                    final Label label = new Label("Constants File Editor");
                    label.setId("Constants-File-Editor");

                    table.setEditable(true);

                    final TableColumn firstCol = new TableColumn("Constant");
                        firstCol.setMinWidth(220);
                        firstCol.setCellValueFactory(
                        new PropertyValueFactory<Constant, String>("key"));

                    final TableColumn secondCol = new TableColumn("Value");
                            secondCol.setMinWidth(67);
                        secondCol.setCellValueFactory(
                        new PropertyValueFactory<Constant, Object>("val"));
                        secondCol.setResizable(false);

                    table.getColumns().addAll(firstCol, secondCol);

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

                            String inputTxt = addConstant.getText();
                            String inputVal = addValue.getText();
                            if (inputTxt.length() < 1 || inputVal.length() < 1){

                            }   
                            else{
                                data.add(new Constant(addConstant.getText(), String.valueOf(addValue.getText())));
                                consts.writeConstant(addConstant.getText(), String.valueOf(addValue.getText()));
                                addConstant.clear();
                                addValue.clear();
                            }

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

                            if (delConst != null) {
                                consts.deleteConstant(delConst.getKey() + " = " + delConst.getVal(), delConst.getKey());
                                table.getSelectionModel().clearSelection();
                            }
                            else{

                            }

                        }
                    });

                    hb.getChildren().addAll(addConstant, addValue, addButton, delButton);
                    hb.setSpacing(3);

                    final Button reloadButton = new Button("Reload Constants");
                    reloadButton.setId("Reload-Button");

                    reloadButton.getStylesheets().add
                    (ConstantsViewer.class.getResource("ConstantsViewer.css").toExternalForm());
                    reloadButton.setOnAction(new EventHandler<ActionEvent>() {

                        @Override
                        public void handle(ActionEvent e) {

                            data.removeAll(data);

                            consts.processLineByLine();

                            consts.hashToConstantArray();

                            for (int i = 0; i < consts.getArrayLength(); i++) {
                                data.add(consts.getConstArrayAtIndex(i));
                            } //populate table with reloaded values

                        }
                    });


                    hb2.getChildren().addAll(imgView);
                    hb2.setSpacing(75);
                    hb2.setAlignment(Pos.CENTER);

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

                    final HBox vbox1 = new HBox();
                    vbox1.setSpacing(6);
                    vbox1.setPadding(new Insets(9, 0, 0, 10));
                    vbox1.getChildren().addAll(reloadButton);
                    vbox1.setLayoutX(200);

                    final VBox vbox = new VBox();
                    vbox.setSpacing(6);
                    vbox.setPadding(new Insets(10, 0, 0, 10));
                    vbox.getChildren().addAll(label, table, hb);

                    ((Group) mainScene.getRoot()).getChildren().addAll(vbox,vbox1);

                    stage.setScene(mainScene);
                    stage.show();

                    //stage.getIcons().add(new Image("file:icon.png"));
               }
          });

        
        
    }
    
    class CenteredRegion extends Region {
    private Node content;

    CenteredRegion(Node content) {
      this.content = content;
      getChildren().add(content);
    }

    @Override protected void layoutChildren() {
      content.relocate(
        Math.round(getWidth()  / 2 - content.prefWidth(USE_PREF_SIZE)  / 2), 
        Math.round(getHeight() / 2 - content.prefHeight(USE_PREF_SIZE) / 2)
      );

      System.out.println("crisp content relocated to: " +
        getLayoutX() + "," + getLayoutY()
      );
    }

    public Node getContent() {
      return content;
    }
  }

  class BoundsReporter implements ChangeListener<Bounds> {
    final String logPrefix;

    BoundsReporter(String logPrefix) {
      this.logPrefix = logPrefix;
    }

    @Override public void changed(ObservableValue<? extends Bounds> ov, Bounds oldBounds, Bounds newBounds) {
      System.out.println(logPrefix + ": " + 
        "New Bounds: " + newBounds
      );
      double xDisplacement = newBounds.getMinX() - Math.floor(newBounds.getMinX());
      double yDisplacement = newBounds.getMinY() - Math.floor(newBounds.getMinY());
      System.out.println(logPrefix + ": " + 
        "xDisplacement: " + xDisplacement + ", " + 
        "yDisplacement: " + yDisplacement
      );
    }
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
