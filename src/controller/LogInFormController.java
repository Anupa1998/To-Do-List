package controller;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXPasswordField;
import com.jfoenix.controls.JFXTextField;
import db.DBConnection;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class LogInFormController {
    public AnchorPane root;
    public JFXTextField txtUsrNm;
    public JFXPasswordField txtPswrd;

    public static String userName;

    public void lblCreateNewAccountOnMouseClicked(MouseEvent mouseEvent) throws IOException {
        Parent parent = FXMLLoader.load(this.getClass().getResource("../view/NewAccountForm.fxml"));
        Scene scene = new Scene(parent);
        Stage primaryStage = (Stage) root.getScene().getWindow();
        primaryStage.setScene(scene);
        primaryStage.setTitle("Create New Account");
        primaryStage.centerOnScreen();
    }

    public void btnLgnOnAction(ActionEvent actionEvent) {
        String uName = txtUsrNm.getText();
        String pswrd = txtPswrd.getText();

        Connection connection = DBConnection.getInstance().getConnection();

        try {
            PreparedStatement preparedStatement = connection.prepareStatement("select * from user where uName = ? and pWord = ?");
            preparedStatement.setObject(1,uName);
            preparedStatement.setObject(2,pswrd);

            ResultSet resultSet = preparedStatement.executeQuery();

            if(resultSet.next()){

                userName = resultSet.getString(1);


                Parent parent = FXMLLoader.load(this.getClass().getResource("../view/ToDoForm.fxml"));
                Scene scene = new Scene(parent);
                Stage primaryStage = (Stage) root.getScene().getWindow();
                primaryStage.setScene(scene);
                primaryStage.setTitle("TO DO");
                primaryStage.centerOnScreen();
            }else{
                Alert alert = new Alert(Alert.AlertType.ERROR,"User name and Password does not match");
                alert.showAndWait();

                txtUsrNm.clear();
                txtPswrd.clear();
            }

        } catch (SQLException | IOException e) {
            e.printStackTrace();
        }


    }
}
