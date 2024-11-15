package controller;

import com.sun.org.apache.xpath.internal.objects.XString;
import db.DBConnection;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.*;

public class CreateNewAccountFormController {
    public AnchorPane root;
    public PasswordField txtNewPassword;
    public PasswordField txtConfirmPassword;
    public Label lblNewPassword;
    public Label lblConfirmPassword;
    public TextField txtUserName;
    public TextField txtEmail;
    public Label lblUserID;
    public Button btnRegister;
    public TextField txtClassNewAcc;


    public void initialize(){
        setVisibility(false);
        setDisableCommon(true);
    }

    public void btnAlreadyHaveAnAccount(ActionEvent actionEvent) throws IOException {
        Parent parent = FXMLLoader.load(this.getClass().getResource("../view/LoginPageForm.fxml"));

        Scene scene = new Scene(parent);

        Stage primaryStage = (Stage) root.getScene().getWindow();
        primaryStage.setScene(scene);
        primaryStage.setTitle("Login");
        primaryStage.getIcons().add(new Image(getClass().getResourceAsStream("../image/logo.png")));
        primaryStage.centerOnScreen();
        primaryStage.setResizable(false);

    }

    public void btnRegisterOnAction(ActionEvent actionEvent) {
        String newPassword = txtNewPassword.getText();
        String confirmPassword = txtConfirmPassword.getText();
        String username = txtUserName.getText();

        if (isValidUsername(username)) {
            // Username is valid, continue with registration
            if (newPassword.equals(confirmPassword)) {
                setBorderColour("transparent");
                setVisibility(false);
                register();
            } else {
                setBorderColour("red");
                setVisibility(true);
                txtNewPassword.requestFocus();
            }
        } else {
            // Username is not valid, display an error message
            Alert alert = new Alert(Alert.AlertType.ERROR, "Invalid username format!");
            alert.showAndWait();
            txtUserName.requestFocus();
        }
    }


    public void setBorderColour(String color){
        txtNewPassword.setStyle("-fx-border-color: "+color);
        txtConfirmPassword.setStyle("-fx-border-color: "+color);
    }

    public void setVisibility(boolean isVisible){
        lblNewPassword.setVisible(isVisible);
        lblConfirmPassword.setVisible(isVisible);
    }

    public void autoGenerateID(){
        Connection connection = DBConnection.getInstance().getConnection();

        try {
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery("select teacher_id from class_teacher order by teacher_id desc limit 1");

            boolean isExist = resultSet.next();

            if (isExist){
                String userID = resultSet.getString(1);

                userID = userID.substring(1,4);

                int intId = Integer.parseInt(userID);

                intId++;

                if (intId<10){
                    lblUserID.setText("U00" + intId);
                }else if (intId<100){
                    lblUserID.setText("U0"+intId);
                }else if (intId<1000){
                    lblUserID.setText("U"+intId);
                }


            }else {
                lblUserID.setText("U001");
            }


        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }


    public void register(){

        String stringTeacherID = lblUserID.getText();
        String teacher_name = txtUserName.getText();
        String stringClass = txtClassNewAcc.getText();
        String email = txtEmail.getText();
        String password = txtConfirmPassword.getText();

        if(teacher_name.trim().isEmpty()){
            txtUserName.requestFocus();
        }else if(email.trim().isEmpty()) {
            txtEmail.requestFocus();
        }else if(stringClass.trim().isEmpty()) {
            txtClassNewAcc.requestFocus();
        }else if (password.trim().isEmpty()){
                txtNewPassword.requestFocus();

        }else {
            Connection connection = DBConnection.getInstance().getConnection();

            try {
                PreparedStatement preparedStatement1 = connection.prepareStatement("select teacher_name from class_teacher");
                ResultSet resultSet = preparedStatement1.executeQuery();
                while (resultSet.next()){
                    if (teacher_name.equals(resultSet.getString(1))){
                        Alert alert = new Alert(Alert.AlertType.WARNING, "User already Registered!");
                        alert.showAndWait();
                        txtUserName.clear();
                        txtUserName.requestFocus();
                        return;
                    }
                }

            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }

            try {
                PreparedStatement preparedStatement = connection.prepareStatement("insert into class_teacher values(?,?,?,?,?)");

                preparedStatement.setObject(1, stringTeacherID);
                preparedStatement.setObject(2, teacher_name);
                preparedStatement.setObject(3, stringClass);
                preparedStatement.setObject(4, email);
                preparedStatement.setObject(5, password);

                preparedStatement.executeUpdate();


                Parent parent = FXMLLoader.load(this.getClass().getResource("../view/LoginPageForm.fxml"));
                Scene scene = new Scene(parent);

                Stage primaryStage = (Stage) root.getScene().getWindow();

                primaryStage.setScene(scene);
                primaryStage.setTitle("Login");
                primaryStage.centerOnScreen();


            } catch (SQLException | IOException throwables) {
                throwables.printStackTrace();
            }

        }
    }

    public void btnAddNewUserOnAction(ActionEvent actionEvent) {
        setDisableCommon(false);

        txtUserName.requestFocus();
        autoGenerateID();
    }

    public void setDisableCommon(boolean isDisable){
        txtUserName.setDisable(isDisable);
        txtEmail.setDisable(isDisable);
        txtNewPassword.setDisable(isDisable);
        txtConfirmPassword.setDisable(isDisable);
        btnRegister.setDisable(isDisable);
        txtClassNewAcc.setDisable(isDisable);
    }

    public boolean isValidUsername(String username) {
        // Define the regular expression pattern
        String regex = "^[a-zA-Z]+_\\d{4}_\\d+$";

        // Use the matches method to check if the username matches the pattern
        boolean isMatch = username.matches(regex);

        return isMatch;
    }


}
