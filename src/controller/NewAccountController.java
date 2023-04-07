package controller;

import com.jfoenix.controls.JFXPasswordField;
import com.jfoenix.controls.JFXTextField;
import db.DBConnection;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Paint;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;

public class NewAccountController {
    public JFXPasswordField txtNewPassword;
    public JFXPasswordField txtConfirmPassword;
    public Label lblPswrdntmatch1;
    public Label lblPswrdntmtch2;
    public JFXTextField txtUserName;
    public JFXTextField txtEmail;
    public AnchorPane root;

    public void initialize(){
        setPwrdMtch(false);
    }

    public void btnRegisterOnAction(ActionEvent actionEvent) {
        String nwPwrd = txtNewPassword.getText();
        String cnfrmPswrd = txtConfirmPassword.getText();


        if(nwPwrd.equals(cnfrmPswrd)){
            setColor("#ffffff");
            setPwrdMtch(false);

            register();
        }
        else{
            setColor("#e84118");

            txtNewPassword.requestFocus();

            setPwrdMtch(true);

        }

        Connection connection = DBConnection.getInstance().getConnection();
        System.out.println(connection);
    }

    public void setColor(String color){
        txtNewPassword.setFocusColor(Paint.valueOf(color));
        txtConfirmPassword.setFocusColor(Paint.valueOf(color));
        txtNewPassword.setUnFocusColor(Paint.valueOf(color));
        txtConfirmPassword.setUnFocusColor(Paint.valueOf(color));
    }
    public void setPwrdMtch(Boolean visible){
        lblPswrdntmatch1.setVisible(visible);
        lblPswrdntmtch2.setVisible(visible);
    }

    public void register(){
        String uName = txtUserName.getText();
        String email = txtEmail.getText();
        String pswrd = txtNewPassword.getText();

        Connection connection = DBConnection.getInstance().getConnection();

        try {
            Statement statement = connection.createStatement();
            PreparedStatement preparedStatement = connection.prepareStatement("insert into user values(?,?,?)");
            preparedStatement.setObject(1,uName);
            preparedStatement.setObject(2,email);
            preparedStatement.setObject(3,pswrd);

            preparedStatement.executeUpdate();

            Parent parent =FXMLLoader.load(this.getClass().getResource("../view/LogInForm.fxml"));
            Scene scene = new Scene(parent);
            Stage primaryStage = (Stage) root.getScene().getWindow();
            primaryStage.setScene(scene);
            primaryStage.setTitle("Login");
            primaryStage.centerOnScreen();


        } catch (SQLException | IOException e) {
            e.printStackTrace();
        }
    }
}
