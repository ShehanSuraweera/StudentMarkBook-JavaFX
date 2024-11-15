package controller;

import db.DBConnection;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import tm.StudentTM;

import javax.lang.model.type.NullType;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;


import static controller.LoginPageFormController.loginUserID;

public class StudentRecordBookFormController {

    public TextField txtStudentName;
    public TextField txtStudentNo;
    public TextField txtReligion;
    public TextField txtFirstLanguage;
    public TextField txtEnglish;
    public TextField txtMaths;
    public TextField txtScience;
    public TextField txtHistory;
    public TextField txtCommerce;
    public TextField txtArt;
    public TextField txtIT;
    public Label lblPosition;
    public Label lblStudentPosition;
    public Label lblAverage;
    public Label lblStudentAverage;
    public Label lblNumberInClass;
    public Label lblStudentsInClass;
    public Label lblTotalMarks;
    public Label lblStudentMarks;
    public TableView<StudentTM> tblStudent;
    public Label lblTeacherID;


    public static String teacherClass;
    public static String teacherName;
    public Label lblClass;
    public Label lblTeacherName;
    public int count = 0;
    public boolean  bool = true;
    public AnchorPane root;


    public void initialize(){

        lblTeacherID.setText(loginUserID);

        loadTable();
        loadColumns();
        getTeacherClass();

        tblStudent.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<StudentTM>() {
            @Override
            public void changed(ObservableValue<? extends StudentTM> observable, StudentTM oldValue, StudentTM newValue) {

                if (tblStudent.getSelectionModel().getSelectedItem() == null){
                    return;
                }
                txtStudentName.setText(tblStudent.getSelectionModel().getSelectedItem().getStudentName());
                txtStudentNo.setText(Integer.toString(tblStudent.getSelectionModel().getSelectedItem().getStudentNo()));
                txtReligion.setText(Integer.toString(tblStudent.getSelectionModel().getSelectedItem().getReligion()));
                txtFirstLanguage.setText(Integer.toString(tblStudent.getSelectionModel().getSelectedItem().getFirstLanguage()));
                txtEnglish.setText(Integer.toString(tblStudent.getSelectionModel().getSelectedItem().getEnglish()));
                txtMaths.setText(Integer.toString(tblStudent.getSelectionModel().getSelectedItem().getMathematics()));
                txtScience.setText(Integer.toString(tblStudent.getSelectionModel().getSelectedItem().getScience()));
                txtHistory.setText(Integer.toString(tblStudent.getSelectionModel().getSelectedItem().getHistory()));
                txtCommerce.setText(Integer.toString(tblStudent.getSelectionModel().getSelectedItem().getCommerce()));
                txtArt.setText(Integer.toString(tblStudent.getSelectionModel().getSelectedItem().getArt()));
                txtIT.setText(Integer.toString(tblStudent.getSelectionModel().getSelectedItem().getIT()));

                lblStudentMarks.setText(Integer.toString(tblStudent.getSelectionModel().getSelectedItem().getTotalMarks()));
                lblStudentPosition.setText(Integer.toString(tblStudent.getSelectionModel().getSelectedItem().getPosition()));
                lblStudentAverage.setText(Double.toString(tblStudent.getSelectionModel().getSelectedItem().getAverage()));
                setVisible(true);
            }
        });

    }


    public void loadColumns(){
        tblStudent.getColumns().get(0).setCellValueFactory(new PropertyValueFactory<>("studentName"));
        tblStudent.getColumns().get(1).setCellValueFactory(new PropertyValueFactory<>("studentNo"));
        tblStudent.getColumns().get(2).setCellValueFactory(new PropertyValueFactory<>("religion"));
        tblStudent.getColumns().get(3).setCellValueFactory(new PropertyValueFactory<>("firstLanguage"));
        tblStudent.getColumns().get(4).setCellValueFactory(new PropertyValueFactory<>("english"));
        tblStudent.getColumns().get(5).setCellValueFactory(new PropertyValueFactory<>("mathematics"));
        tblStudent.getColumns().get(6).setCellValueFactory(new PropertyValueFactory<>("science"));
        tblStudent.getColumns().get(7).setCellValueFactory(new PropertyValueFactory<>("history"));
        tblStudent.getColumns().get(8).setCellValueFactory(new PropertyValueFactory<>("commerce"));
        tblStudent.getColumns().get(9).setCellValueFactory(new PropertyValueFactory<>("art"));
        tblStudent.getColumns().get(10).setCellValueFactory(new PropertyValueFactory<>("IT"));
        tblStudent.getColumns().get(11).setCellValueFactory(new PropertyValueFactory<>("totalMarks"));
        tblStudent.getColumns().get(12).setCellValueFactory(new PropertyValueFactory<>("average"));
        tblStudent.getColumns().get(13).setCellValueFactory(new PropertyValueFactory<>("position"));
    }

    public void loadTable(){
        ObservableList<StudentTM> studentList = tblStudent.getItems();

        studentList.clear();
        Connection connection = DBConnection.getInstance().getConnection();

        try {
            PreparedStatement preparedStatement = connection.prepareStatement("select * from student where class_teacherID = ? ORDER BY average DESC");
            preparedStatement.setObject(1,loginUserID);

            ResultSet resultSet = preparedStatement.executeQuery();
            int index =0;
            int actualPosition =0;
            double preAverage=0.0;
            while (resultSet.next()){
                int studentID = resultSet.getInt(1);
                String studentName = resultSet.getString(2);
                String studentClass = resultSet.getString(3);
                String teacherID = resultSet.getString(4);
                int total = resultSet.getInt(5);
                double average = resultSet.getDouble(6);
                //int actualPosition = resultSet.getInt(7);
                int religion = resultSet.getInt(8);
                int firstLanguage = resultSet.getInt(9);
                int english = resultSet.getInt(10);
                int maths = resultSet.getInt(11);
                int science = resultSet.getInt(12);
                int history = resultSet.getInt(13);
                int commerce = resultSet.getInt(14);
                int art = resultSet.getInt(15);
                int it = resultSet.getInt(16);


                double temp = preAverage;
                preAverage = average;


                index++;

                if(temp == average){

                }else {
                    actualPosition++;
                    while(index>actualPosition){
                        actualPosition++;
                    }
                }

                updatePositionInDatabase(studentID,actualPosition,teacherClass);
                studentList.add(new StudentTM(studentID,studentName,studentClass,teacherID,total,average,actualPosition,religion,firstLanguage,english,maths,science,history,commerce,art,it));


                count++;
            }
            tblStudent.refresh();

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

        lblStudentsInClass.setText(Integer.toString(count));
        count =0;
    }


    public void btnAddNewStudentOnAction(ActionEvent actionEvent) {

        if(isEmpty()){
            StudentTM student = new StudentTM();

            String studentName = txtStudentName.getText();
            int studentNo = Integer.parseInt(txtStudentNo.getText());
            String studentClass = lblClass.getText();
            int religion = Integer.parseInt(txtReligion.getText());
            int firstLanguage = Integer.parseInt(txtFirstLanguage.getText());
            int english = Integer.parseInt(txtEnglish.getText());
            int maths = Integer.parseInt(txtMaths.getText());
            int science = Integer.parseInt(txtScience.getText());
            int history = Integer.parseInt(txtHistory.getText());
            int commerce = Integer.parseInt(txtCommerce.getText());
            int art = Integer.parseInt(txtArt.getText());
            int  it = Integer.parseInt(txtIT.getText());

            student.setStudentName(studentName);
            student.setStudentClass(studentClass);
            student.setTeacherID(loginUserID);
            student.setReligion(religion);
            student.setFirstLanguage(firstLanguage);
            student.setEnglish(english);
            student.setMathematics(maths);
            student.setScience(science);
            student.setHistory(history);
            student.setCommerce(commerce);
            student.setArt(art);
            student.setIT(it);
            student.setTotalMarks(calculateTotalMarks(student));
            student.setAverage(calculateAverage(calculateTotalMarks(student)));

            if(!isStudentExist(studentNo)){
                student.setStudentNo(studentNo);
                addValuesToDatabase(student);
                loadTable();
                clearSelection();
            }else{
                txtStudentNo.requestFocus();
            }
        }else {
            Alert alert = new Alert(Alert.AlertType.WARNING, "Please fill empty ones!");
            alert.showAndWait();
        }
    }

    public void addValuesToDatabase(StudentTM student){

        int studentNo = student.getStudentNo();
        String studentName = student.getStudentName();
        String studentClass = student.getStudentClass();
        String teacherID = student.getTeacherID();
        int totalMarks = student.getTotalMarks();
        double average = student.getAverage();

        int religion = student.getReligion();
        int firstLanguage = student.getFirstLanguage();
        int english = student.getEnglish();
        int mathematics = student.getMathematics();
        int science = student.getScience();
        int history = student.getHistory();
        int commerce = student.getCommerce();
        int art = student.getArt();
        int it = student.getIT();


        Connection connection = DBConnection.getInstance().getConnection();

        try {
            PreparedStatement preparedStatement = connection.prepareStatement("insert into student values (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)");

            preparedStatement.setInt(1,studentNo);
            preparedStatement.setString(2,studentName);
            preparedStatement.setString(3,studentClass);
            preparedStatement.setString(4,teacherID);
            preparedStatement.setInt(5,totalMarks);
            preparedStatement.setDouble(6,average);
            preparedStatement.setInt(7,2);
            preparedStatement.setInt(8,religion);
            preparedStatement.setInt(9,firstLanguage);
            preparedStatement.setInt(10,english);
            preparedStatement.setInt(11,mathematics);
            preparedStatement.setInt(12,science);
            preparedStatement.setInt(13,history);
            preparedStatement.setInt(14,commerce);
            preparedStatement.setInt(15,art);
            preparedStatement.setInt(16,it);

            preparedStatement.executeUpdate();

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

    }

    public int calculateTotalMarks(StudentTM student){
        int total = 0;

        int religion = student.getReligion();
        int firstLanguage = student.getFirstLanguage();
        int english = student.getEnglish();
        int mathematics = student.getMathematics();
        int science = student.getScience();
        int history = student.getHistory();
        int commerce = student.getCommerce();
        int art = student.getArt();
        int it = student.getIT();

        total = religion + firstLanguage + english + mathematics + science + history + commerce + art + it;

        return total;
    }

    public double calculateAverage(int total){
        double average =0;

        average = (double) total /9;

        average = Math.round(average * 100.0) / 100.0; // Rounding to 2 decimal places
        return average;
    }


    public void btnDeleteOnAction(ActionEvent actionEvent) {

        String studentNoText = txtStudentNo.getText();

        if(txtStudentNo.getText().trim().isEmpty()){
            Alert alert = new Alert(Alert.AlertType.WARNING, "Please enter valid Student Number to delete!");
            alert.showAndWait();
            txtStudentNo.requestFocus();
            return;
        }
        try {
            int studentNo = Integer.parseInt(studentNoText);
            if (studentNo <1){
                Alert alert = new Alert(Alert.AlertType.WARNING, "Please enter valid Student Number to delete!");
                alert.showAndWait();
                txtStudentNo.requestFocus();
            }else {
                Connection connection = DBConnection.getInstance().getConnection();

                try {
                    PreparedStatement preparedStatement = connection.prepareStatement("delete from student where student_no=? AND class=?");
                    preparedStatement.setInt(1, studentNo);
                    preparedStatement.setString(2, teacherClass);
                    preparedStatement.executeUpdate();

                    loadTable();
                    clearSelection();

                } catch (SQLException throwables) {
                    throwables.printStackTrace();
                }
            }
        }catch (NumberFormatException e){
            Alert alert = new Alert(Alert.AlertType.WARNING, "Please enter valid Student Number to delete!");
            alert.showAndWait();
            txtStudentNo.requestFocus();
        }

    }

    public void btnUpdateOnAction(ActionEvent actionEvent) {

        if (isEmpty()){
            StudentTM student = new StudentTM();


            String studentName = txtStudentName.getText();
            int studentNo = Integer.parseInt(txtStudentNo.getText());
            String studentClass = lblClass.getText();
            int religion = Integer.parseInt(txtReligion.getText());
            int firstLanguage = Integer.parseInt(txtFirstLanguage.getText());
            int english = Integer.parseInt(txtEnglish.getText());
            int maths = Integer.parseInt(txtMaths.getText());
            int science = Integer.parseInt(txtScience.getText());
            int history = Integer.parseInt(txtHistory.getText());
            int commerce = Integer.parseInt(txtCommerce.getText());
            int art = Integer.parseInt(txtArt.getText());
            int  it = Integer.parseInt(txtIT.getText());


            student.setStudentName(studentName);
            student.setStudentNo(studentNo);
            student.setStudentClass(studentClass);
            student.setTeacherID(loginUserID);
            student.setReligion(religion);
            student.setFirstLanguage(firstLanguage);
            student.setEnglish(english);
            student.setMathematics(maths);
            student.setScience(science);
            student.setHistory(history);
            student.setCommerce(commerce);
            student.setArt(art);
            student.setIT(it);

            student.setTotalMarks(calculateTotalMarks(student));
            student.setAverage(calculateAverage(calculateTotalMarks(student)));

            Connection connection = DBConnection.getInstance().getConnection();

            try {

                if(Integer.toString(studentNo).equals(Integer.toString(tblStudent.getSelectionModel().getSelectedItem().getStudentNo()))){
                    PreparedStatement preparedStatement = connection.prepareStatement("UPDATE student SET student_name=?, student_no=?, class=?, class_teacherID=?, total_marks=?, average=?, position =?, religion=?, first_language=?,english=?,mathematics=?,science=?,history=?,commerce=?,art=?,IT=? WHERE student_no =? AND class=?");
                    preparedStatement.setString(1,student.getStudentName());
                    preparedStatement.setInt(2,student.getStudentNo());
                    preparedStatement.setString(3,student.getStudentClass());
                    preparedStatement.setString(4,student.getTeacherID());
                    preparedStatement.setInt(5,student.getTotalMarks());
                    preparedStatement.setDouble(6,student.getAverage());
                    preparedStatement.setInt(7,student.getPosition());
                    preparedStatement.setInt(8,student.getReligion());
                    preparedStatement.setInt(9,student.getFirstLanguage());
                    preparedStatement.setInt(10,student.getEnglish());
                    preparedStatement.setInt(11,student.getMathematics());
                    preparedStatement.setInt(12,student.getScience());
                    preparedStatement.setInt(13,student.getHistory());
                    preparedStatement.setInt(14,student.getCommerce());
                    preparedStatement.setInt(15,student.getArt());
                    preparedStatement.setInt(16,student.getIT());
                    preparedStatement.setInt(17,student.getStudentNo());
                    preparedStatement.setString(18,teacherClass);

                    preparedStatement.executeUpdate();
                    loadTable();
                    clearSelection();
                }else if(!isStudentExist(studentNo)){
                    PreparedStatement preparedStatement = connection.prepareStatement("insert into student values (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)");

                    preparedStatement.setInt(1,student.getStudentNo());
                    preparedStatement.setString(2,student.getStudentName());
                    preparedStatement.setString(3,student.getStudentClass());
                    preparedStatement.setString(4,student.getTeacherID());
                    preparedStatement.setInt(5,student.getTotalMarks());
                    preparedStatement.setDouble(6,student.getAverage());
                    preparedStatement.setInt(7,student.getPosition());
                    preparedStatement.setInt(8,student.getReligion());
                    preparedStatement.setInt(9,student.getFirstLanguage());
                    preparedStatement.setInt(10,student.getEnglish());
                    preparedStatement.setInt(11,student.getMathematics());
                    preparedStatement.setInt(12,student.getScience());
                    preparedStatement.setInt(13,student.getHistory());
                    preparedStatement.setInt(14,student.getCommerce());
                    preparedStatement.setInt(15,student.getArt());
                    preparedStatement.setInt(16,student.getIT());

                    preparedStatement.executeUpdate();
                    loadTable();
                    clearSelection();
                }
                else {
                    Alert alert = new Alert(Alert.AlertType.WARNING, "You cannot change the student Number!");
                    alert.showAndWait();
                    txtStudentNo.requestFocus();
                }

            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
        }


    }

    public void clearSelection(){
        txtStudentName.clear();
        txtStudentNo.clear();
        txtReligion.clear();
        txtFirstLanguage.clear();
        txtEnglish.clear();
        txtMaths.clear();
        txtScience.clear();
        txtHistory.clear();
        txtCommerce.clear();
        txtArt.clear();
        txtIT.clear();
        setVisible(false);
        

    }

    public void btnClearOnAction(ActionEvent actionEvent) {
        clearSelection();
        loadTable();
        loadColumns();
        getTeacherClass();
    }

    public void setVisible(boolean bool){
        lblStudentPosition.setVisible(bool);
        lblStudentAverage.setVisible(bool);
        lblStudentMarks.setVisible(bool);
    }

    public void getTeacherClass(){
        Connection connection = DBConnection.getInstance().getConnection();

        try {
            PreparedStatement preparedStatement = connection.prepareStatement("select class,teacher_name from class_teacher WHERE teacher_id = ?");
            preparedStatement.setObject(1,loginUserID);

            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()){
                teacherClass = resultSet.getString(1);
                teacherName = resultSet.getString(2);
            }
            lblClass.setText(teacherClass);
            lblTeacherName.setText(teacherName);

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

    }

    public void updatePositionInDatabase(int studentID, int pos, String classNo){
        Connection connection = DBConnection.getInstance().getConnection();
        try {
            PreparedStatement preparedStatement = connection.prepareStatement("UPDATE student SET position = ? WHERE student_no=? AND class=?");
            preparedStatement.setInt(1,pos);
            preparedStatement.setInt(2,studentID);
            preparedStatement.setString(3,classNo);
            preparedStatement.executeUpdate();

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    public void btnLogOutOnAction(ActionEvent actionEvent) throws IOException {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Do you want to Log Out...?", ButtonType.YES, ButtonType.NO);
        Optional<ButtonType> buttonType = alert.showAndWait();
        if (buttonType.get().equals(ButtonType.YES)){
            Parent parent = FXMLLoader.load(this.getClass().getResource("../view/LoginPageForm.fxml"));
            Scene scene = new Scene(parent);

            Stage primaryStage = (Stage)root.getScene().getWindow();
            primaryStage.setScene(scene);
            primaryStage.centerOnScreen();
            primaryStage.setTitle("Login");
        }
    }

    public void btnExitOnAction(ActionEvent actionEvent) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Do you want to exit...?", ButtonType.YES, ButtonType.NO);
        Optional<ButtonType> buttonType = alert.showAndWait();
        if (buttonType.get().equals(ButtonType.YES)){
            Platform.exit();
        }
    }

    public boolean isStudentExist(int studentNo){

        Connection connection = DBConnection.getInstance().getConnection();
        try {
            PreparedStatement preparedStatement = connection.prepareStatement("select student_no from student where student_no=? AND class=?");
            preparedStatement.setInt(1,studentNo);
            preparedStatement.setString(2,teacherClass);
            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()){
                if(Integer.toString(studentNo).equals(Integer.toString(resultSet.getInt("student_no")))){

                    Alert alert = new Alert(Alert.AlertType.ERROR, "Student Number already Exist!");
                    alert.showAndWait();

                    txtStudentNo.clear();
                    bool = true;

                    return bool;
                }
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

        bool = false;
        return bool;
    }

    public boolean isEmpty(){
        if(txtStudentName.getText().trim().isEmpty()){
            txtStudentName.requestFocus();
            return false;
        }else if(txtStudentNo.getText().trim().isEmpty()){
            txtStudentNo.requestFocus();
            return false;
        }else if(txtReligion.getText().trim().isEmpty()){
            txtReligion.requestFocus();
            return false;
        }else if (txtFirstLanguage.getText().trim().isEmpty()){
            txtFirstLanguage.requestFocus();
            return false;
        }else if (txtEnglish.getText().trim().isEmpty()){
            txtEnglish.requestFocus();
            return false;
        }else if(txtMaths.getText().trim().isEmpty()){
            txtMaths.requestFocus();
            return false;
        }else if(txtScience.getText().trim().isEmpty()){
            txtScience.requestFocus();
            return false;
        }else if (txtHistory.getText().trim().isEmpty()){
            txtHistory.requestFocus();
            return false;
        }else if (txtCommerce.getText().trim().isEmpty()){
            txtCommerce.requestFocus();
            return false;
        }else if (txtArt.getText().trim().isEmpty()){
            txtArt.requestFocus();
            return false;
        }else if(txtIT.getText().trim().isEmpty()){
            txtIT.requestFocus();
            return false;
        }
        return true;
    }
}
