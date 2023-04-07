package controller;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextField;
import db.DBConnection;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import tm.ToDoTM;

import java.io.IOException;
import java.sql.*;
import java.util.Optional;

public class ToDoFormController {
    public Label lblUsrNm;
    public Pane subRoot;
    public AnchorPane root;
    public JFXTextField txtDescription;
    public ListView<ToDoTM> lsttd;
    public JFXButton btnDelete;
    public TextField txtSlctdTD;
    public JFXButton btnUpdate;


    public void initialize(){
        autoLOad();

        lblUsrNm.setText("Hi " + LogInFormController.userName + " Welcome to TO-DO list");

        subRoot.setVisible(false);

        setDisableCommon(true);

        lsttd.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<ToDoTM>() {
            @Override
            public void changed(ObservableValue<? extends ToDoTM> observableValue, ToDoTM toDoTM, ToDoTM t1) {
                if(lsttd.getSelectionModel().getSelectedItem() == null){
                    return;
                }

                setDisableCommon(false);

                subRoot.setVisible(false);

                txtSlctdTD.setText(lsttd.getSelectionModel().getSelectedItem().getDescription());
            }
        });
    }

    public void setDisableCommon(boolean disabel){
        txtSlctdTD.setDisable(disabel);
        btnDelete.setDisable(disabel);
        btnUpdate.setDisable(disabel);
        txtSlctdTD.clear();
    }

    public void btnLgtOnAction(ActionEvent actionEvent) throws IOException {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION,"Do you want Log out?", ButtonType.YES, ButtonType.NO);

        Optional<ButtonType> buttonType = alert.showAndWait();

        if(buttonType.get().equals(ButtonType.YES)){

            Parent parent = FXMLLoader.load(this.getClass().getResource("../view/LogInForm.fxml"));
            Scene scene = new Scene(parent);
            Stage primaryStage = (Stage) root.getScene().getWindow();
            primaryStage.setScene(scene);
            primaryStage.setTitle("Log in");
            primaryStage.centerOnScreen();
        }
    }

    public void btnAddnwtdOnAction(ActionEvent actionEvent) {

        lsttd.getSelectionModel().clearSelection();
        setDisableCommon(true);
        subRoot.setVisible(true);
        txtDescription.requestFocus();

    }

    public void adTLstOnAction(ActionEvent actionEvent) {
        String todoId = autoGenerateId();
        String description = txtDescription.getText();
        String user = LogInFormController.userName;

        Connection connection = DBConnection.getInstance().getConnection();

        try {
            PreparedStatement preparedStatement = connection.prepareStatement("insert into todo value (?,?,?)");
            preparedStatement.setObject(1,todoId);
            preparedStatement.setObject(2,description);
            preparedStatement.setObject(3,user);

            preparedStatement.executeUpdate();

            txtDescription.clear();
            subRoot.setVisible(false);

            autoLOad();

        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    public String autoGenerateId() {
        Connection connection = DBConnection.getInstance().getConnection();

        String id = "";
        try {
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery("select todo_id from todo order by todo_id desc limit 1");

            boolean isExist = resultSet.next();

            if (isExist) {
                String todoID = resultSet.getString(1);

                todoID = todoID.substring(1, todoID.length());

                int intID = Integer.parseInt(todoID);

                intID++;

                if (intID < 10) {
                    id = "T00" + intID;
                } else if (intID < 100) {
                    id = "T0" + intID;
                } else {
                    id = "T" + intID;
                }

            } else {
                id = "T001";
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return id;
    }

    public void autoLOad(){
        String uName = LogInFormController.userName;

        ObservableList<ToDoTM> todos = lsttd.getItems();
        todos.clear();

        Connection connection = DBConnection.getInstance().getConnection();

        try {
            PreparedStatement preparedStatement = connection.prepareStatement("select  * from todo where uName = ?");
            preparedStatement.setObject(1,uName);

            ResultSet resultSet = preparedStatement.executeQuery();

            while(resultSet.next()){
                String id = resultSet.getString(1);
                String description = resultSet.getString(2);
                String userName = resultSet.getString(3);

                todos.add(new ToDoTM(id,description,userName));

            }
            lsttd.refresh();

        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    public void bgnDltOnAction(ActionEvent actionEvent) {
        String id = lsttd.getSelectionModel().getSelectedItem().getTodo_ID();

        Connection connection = DBConnection.getInstance().getConnection();

        try {
            PreparedStatement preparedStatement = connection.prepareStatement("delete from todo where todo_ID = ?");
            preparedStatement.setObject(1,id);
            preparedStatement.executeUpdate();

            autoLOad();

            setDisableCommon(true);



        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void btnUpdtOnAction(ActionEvent actionEvent) {
        String description = txtSlctdTD.getText();
        String id = lsttd.getSelectionModel().getSelectedItem().getTodo_ID();

        Connection connection = DBConnection.getInstance().getConnection();

        try {
            PreparedStatement preparedStatement = connection.prepareStatement("update todo set description = ? where todo_ID = ?");
            preparedStatement.setObject(1,description);
            preparedStatement.setObject(2,id);

            preparedStatement.executeUpdate();

            autoLOad();

            setDisableCommon(true);

        } catch (SQLException e) {
            e.printStackTrace();
        }


    }
}
