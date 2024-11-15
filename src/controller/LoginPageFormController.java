package controller;

import db.DBConnection;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class LoginPageFormController {

    public AnchorPane root;
    public TextField txtUserName;
    public TextField txtPassword;

    public static String loginUserID;
    public static String loginUserName;

    public void lblCreateNewAccountOnMouseClick(MouseEvent mouseEvent) throws IOException {
        Parent parent = FXMLLoader.load(this.getClass().getResource("../view/CreateNewAccountForm.fxml"));

        Scene scene = new Scene(parent);

        Stage primaryStage = (Stage) root.getScene().getWindow();
        primaryStage.setScene(scene);
        primaryStage.setTitle("Create New Account");
        primaryStage.getIcons().add(new Image(getClass().getResourceAsStream("../image/logo.png")));
        primaryStage.centerOnScreen();
        primaryStage.setResizable(false);
    }


    public void btnLoginOnAction(ActionEvent actionEvent) {
        String userName = txtUserName.getText();
        String password = txtPassword.getText();

        if (userName.trim().isEmpty()){
            txtUserName.requestFocus();
        }else if (password.trim().isEmpty()){
            txtPassword.requestFocus();
        }else {
            Connection connection = DBConnection.getInstance().getConnection();

            try {
                PreparedStatement preparedStatement = connection.prepareStatement("select * from class_teacher where teacher_name = ? and password = ?");
                preparedStatement.setObject(1,userName);
                preparedStatement.setObject(2,password);

                ResultSet resultSet = preparedStatement.executeQuery();

                if (resultSet.next()){

                    loginUserID = resultSet.getString(1);
                    loginUserName = resultSet.getString(2);

                    Parent parent = FXMLLoader.load(this.getClass().getResource("../view/StudentRecordBookForm.fxml"));

                    Scene scene = new Scene(parent);

                    Stage primaryStage = (Stage) root.getScene().getWindow();
                    primaryStage.setScene(scene);
                    primaryStage.setTitle("Student Record Book");
                    primaryStage.getIcons().add(new Image(getClass().getResourceAsStream("../image/logo.png")));
                    primaryStage.centerOnScreen();
                }else {
                    Alert alert = new Alert(Alert.AlertType.ERROR, "User Name or Password Does not matched!");
                    alert.showAndWait();

                    txtUserName.clear();
                    txtPassword.clear();

                }

            } catch (SQLException | IOException throwables) {
                throwables.printStackTrace();
            }

        }
    }
}
