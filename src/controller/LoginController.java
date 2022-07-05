package controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Region;
import javafx.stage.Stage;
import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class LoginController {

    Stage LogIn;

    public Region ccu;
    public TextField usernameTxt;
    public Region ticketor;
    public PasswordField pwdField;
    public ImageView CCUTick;
    public ImageView ticketorTick;


    String cssOnExited = "-fx-background-color: none;-fx-border-color: white; -fx-border-radius: 5; -fx-background-radius: 5;";
    String cssOnEntered = "-fx-background-color: white; -fx-border-radius: 5; -fx-background-radius: 5;";
    String forValid = "-fx-border-color: green; -fx-border-width: 2";
    String forInvalid = "-fx-border-color: yellow; -fx-border-width: 2";
    String usrRegex =  "^([a-z]|[0-9]){3,8}$";
    String pwdRegex = "^([A-Z]|[a-z]|[0-9]|){8,15}$";

    boolean isValidated = false;

    boolean CCUSelected = false;
    boolean ticketorSelected = false;

    public boolean validator(String pattern, String matcher){
        Pattern pat = Pattern.compile(pattern);
        Matcher mat = pat.matcher(matcher);
        boolean matchFound = mat.find();
        if(matchFound) {
            System.out.println(matchFound);
            isValidated = true;
        } else {
            System.out.println(matchFound);
            isValidated = false;
        }
        return matchFound;
    }

    public void initialize(){
    }

    public void OnMouseEntered(MouseEvent mouseEvent) {
        if(mouseEvent.getSource()==ccu){
            ccu.setStyle(cssOnEntered);
        }
        else{
            ticketor.setStyle(cssOnEntered);
        }
    }

    public void OnMouseExited(MouseEvent mouseEvent) {
        if(mouseEvent.getSource()==ccu){
            ccu.setStyle(cssOnExited);
        }
        else{
            ticketor.setStyle(cssOnExited);
        }
    }

    public void OnCcuOptionSelected(MouseEvent mouseEvent) {
        if(ticketorSelected){
            ticketorSelected=false;
            ticketorTick.setStyle("visibility: false");
        }
        CCUSelected=true;
        CCUTick.setStyle("visibility: true");
    }

    public void LogInOnAction(ActionEvent actionEvent) throws IOException {

        LogIn = (Stage) usernameTxt.getScene().getWindow();

        if(CCUSelected & isValidated){
            LogIn.close();
            LogIn.setScene(new Scene(FXMLLoader.load(getClass().getResource("../view/management.fxml"))));
            LogIn.show();
        }

        else if(ticketorSelected & isValidated){
            LogIn.close();
            LogIn.setScene(new Scene(FXMLLoader.load(getClass().getResource("../view/passengerHandler.fxml"))));
            LogIn.show();
        }
        else {
            System.out.println("username-pwd validation is unsuccessful or user not selected!");
        }

    }

    public void OnTicketorOptionSelected(MouseEvent mouseEvent) {
        if(CCUSelected){
            CCUSelected=false;
            CCUTick.setStyle("visibility: false");
        }
        ticketorSelected=true;
        ticketorTick.setStyle("visibility: true");
    }

    public void usernameEnteredOnAction(ActionEvent actionEvent) {
    }

    public void onKeyPressedForUsername(KeyEvent keyEvent) {
        if(validator(usrRegex,usernameTxt.getText())){
            usernameTxt.setStyle(forValid);
        }else{
            usernameTxt.setStyle(forInvalid);
        }

    }

    public void onKeyPressedForPassword(KeyEvent keyEvent) {
        if(validator(pwdRegex,pwdField.getText())){
            pwdField.setStyle(forValid);
        }else {
            pwdField.setStyle(forInvalid);
        }
    }
}
