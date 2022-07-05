package controller;

import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXTextField;
import com.jfoenix.controls.JFXTimePicker;
import db.CrudUtil;
import db.DataSet;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.effect.BoxBlur;
import javafx.scene.effect.GaussianBlur;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import model.Driver;
import model.Locomotive;
import model.Schedule;
import model.Station;

import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class ManagementController {

    static int beginSt;
    static int endSt;

    public AnchorPane mainPane;

    // driver detail handlers
    public TextField nicTXT;
    public TextField idTXT;
    public TextField ageTXT;
    public TextField nameTXT;
    public TextArea addressTXT;
    public TextField contactTXT;

    // buttons
    public Button updateDriverBTN;
    public Button addDriverBTN;

    // vehicle management tab
    public CheckBox fClassCheck;
    public CheckBox tClassCheck;
    public CheckBox sClassCheck;

    public JFXTextField vNumTxt;
    public JFXTextField locoTxt;

    public JFXComboBox<String> typeTxt;
    public Spinner<Integer> compartmentsSpinner;

    public Button addVehicleBtn;
    public Button updateVehicleBtn;

    // schedule tab
    public Label vehicleLabel;
    public Label driverLabel;
    public TextArea stopsTxtArea;
    public ComboBox toCombo;
    public JFXTimePicker timePicker;
    public ComboBox fromCombo;
    public ComboBox driversForScheduleCombo;
    public ComboBox trainsForScheduleCombo;
    public Button loadStationBtn;

    // +++++++++ end ++++++++++++

    static Stage Management;
    static Stage stSelection =  new Stage();

    // vehicle data
    public TableView<Locomotive> vehicleTable;
    public TableColumn vNumber;
    public TableColumn tag;
    public TableColumn type;
    public TableColumn compartments;
    public TableColumn passengers;
    public TableColumn classes;
    public TableColumn vehicleOptions;

    // driver data
    public TableView<Driver> driverTable;
    public TableColumn nic;
    public TableColumn id;
    public TableColumn age;
    public TableColumn name;
    public TableColumn contact;
    public TableColumn address;
    public TableColumn driverOptions;

    // schedule table components
    public TableView<Schedule> scheduleTbl;
    public TableColumn sVNum;
    public TableColumn sTag;
    public TableColumn sDriver;
    public TableColumn sDeparture;
    public TableColumn sStart;
    public TableColumn sEnd;
    public TableColumn sType;
    public TableColumn sPassengers;
    public TableColumn sOptions;

    // date and time 
    public Label dateLbl;
    public Label timeLbl;
    public Label amLbl;

    private String vNameRegex = "[A-Z][a-z[^0-9]]{3,10}\\s[A-Z][a-z[^0-9]]{3,10}";
    private String vNumRegex = "T[0-9]{3}";
    private String dIDRegex = "D[0-9]{3}";
    private String dNicRegex = "[0-9]{12}";
    private String forValid = "-fx-text-inner-color: green;";
    private String forInvalid = "-fx-text-inner-color: red;";

    private boolean isVnameValidated = false;
    private boolean isVNumValidated = false;
    private boolean isDnameValidated = false;
    private boolean isDIdValidated = false;

    public boolean validator(String pattern, String matcher){
        Pattern pat = Pattern.compile(pattern);
        Matcher mat = pat.matcher(matcher);
        boolean matchFound = mat.find();
        return matchFound;
    }

    private void driverChecker(){
        if(isDIdValidated & isDnameValidated){
            addDriverBTN.setDisable(false);
//            if(updateDriverBTN.isDisabled()){
//                updateDriverBTN.setDisable(false);
//            }
        }else {
            addDriverBTN.setDisable(true);
        }
    }

    private void vehicleChecker(){
        if(isVnameValidated & isVNumValidated){
            addVehicleBtn.setDisable(false);
        }else {
            addVehicleBtn.setDisable(true);
        }
    }

    public void vNameOnKeyPressed(KeyEvent keyEvent) {
        if(validator(vNameRegex,locoTxt.getText())){
            locoTxt.setStyle(forValid);
            isVnameValidated = true;
        }else{
            locoTxt.setStyle(forInvalid);
            isVnameValidated = false;
        }
        vehicleChecker();

    }

    public void vNumOnKeyPressed(KeyEvent keyEvent) {
        if(validator(vNumRegex,vNumTxt.getText())){
            vNumTxt.setStyle(forValid);
            isVNumValidated = true;
        }else{
            vNumTxt.setStyle(forInvalid);
            isVNumValidated = false;
        }
        vehicleChecker();
    }

    public void dNicOnKeyPressed(KeyEvent keyEvent) {
        if(validator(dNicRegex,nicTXT.getText())){
            nicTXT.setStyle(forValid);
            isDnameValidated = true;
        }else{
            nicTXT.setStyle(forInvalid);
            isDnameValidated = false;
        }
        driverChecker();
    }

    public void dIdOnKeyPressed(KeyEvent keyEvent) {
        if(validator(dIDRegex,idTXT.getText())){
            idTXT.setStyle(forValid);
            isDIdValidated = true;
        }else{
            idTXT.setStyle(forInvalid);
            isDIdValidated = false;
        }
        driverChecker();
    }

    class timerThread extends Thread {
        public void run() {
            Timer timer = new Timer();
            int begin = 0;
            int timeInterval = 1000;
            timer.schedule(new TimerTask() {
                @Override
                public void run() {
                    Platform.runLater(() -> {
                        int count = 0;
                        for (String dt:calDateAndTime()) {
                            if(count==0){
                                dateLbl.setText(dt);
                                count++;
                            }else if(count==1){
                                timeLbl.setText(dt);
                                count++;
                            }else{
                                amLbl.setText(dt);
                            }
                        }
                    });
                }
            }, begin, timeInterval);
        }
    }

    public void initialize(){

        timerThread timer = new timerThread();
        timer.start();

        toCombo.setDisable(true);
        loadStationBtn.setDisable(true);

        // disabling buttons
        updateDriverBTN.setDisable(true);
        updateVehicleBtn.setDisable(true);

        // adding combo-box data
        ArrayList<String> list = new ArrayList();
        list.add("Intercity");
        list.add("Express");
        ObservableList types = FXCollections.observableArrayList(list);
        typeTxt.setItems(types);

        // adding spinner data
        SpinnerValueFactory<Integer> valueFactory = new SpinnerValueFactory.IntegerSpinnerValueFactory(1, 10, 1);
        compartmentsSpinner.setValueFactory(valueFactory);

        sVNum.setCellValueFactory(new PropertyValueFactory<>("v_num"));
        sTag.setCellValueFactory(new PropertyValueFactory<>("tag"));
        sDriver.setCellValueFactory(new PropertyValueFactory<>("driver"));
        sDeparture.setCellValueFactory(new PropertyValueFactory<>("departure"));
        sStart.setCellValueFactory(new PropertyValueFactory<>("begin"));
        sEnd.setCellValueFactory(new PropertyValueFactory<>("end"));
        sType.setCellValueFactory(new PropertyValueFactory<>("type"));
        sPassengers.setCellValueFactory(new PropertyValueFactory<>("passengers"));
        sOptions.setCellValueFactory(new PropertyValueFactory<>("delete"));


        vNumber.setCellValueFactory(new PropertyValueFactory("Reg_No"));
        tag.setCellValueFactory(new PropertyValueFactory("Tag"));
        type.setCellValueFactory(new PropertyValueFactory("Type"));
        compartments.setCellValueFactory(new PropertyValueFactory("Compartments"));
        passengers.setCellValueFactory(new PropertyValueFactory("Seating"));
        classes.setCellValueFactory(new PropertyValueFactory("Classes"));
        vehicleOptions.setCellValueFactory(new PropertyValueFactory("delete"));

        nic.setCellValueFactory(new PropertyValueFactory("NIC"));
        id.setCellValueFactory(new PropertyValueFactory("ID"));
        age.setCellValueFactory(new PropertyValueFactory("Age"));
        name.setCellValueFactory(new PropertyValueFactory("Name"));
        contact.setCellValueFactory(new PropertyValueFactory("TP_Number"));
        address.setCellValueFactory(new PropertyValueFactory("Address"));
        driverOptions.setCellValueFactory(new PropertyValueFactory("delete"));

        try{
            loadAllVehicles();
            loadAllDrivers();
            loadAllStations();
            loadAllSchedules();
            fillStationCombos();
        } catch (SQLException e) {
            System.out.println(e);
        } catch (ClassNotFoundException e) {
            System.out.println(e);
        }

    }

    public void logOut(ActionEvent actionEvent) throws IOException {
        Management = (Stage) mainPane.getScene().getWindow();
        Management.close();
        Management.setScene(new Scene(FXMLLoader.load(getClass().getResource("../view/login.fxml"))));
        Management.show();
    }

    private void loadAllVehicles() throws SQLException, ClassNotFoundException {

        ResultSet result = CrudUtil.execute("SELECT * FROM locomotive");
        ObservableList<Locomotive> obList = FXCollections.observableArrayList();
        DataSet.vehicles.clear();

        while (result.next()){
            Locomotive v = new Locomotive(
                    result.getString(1),
                    result.getString(2),
                    result.getString(3),
                    result.getInt(4),
                    result.getInt(5),
                    result.getString(6)
            );

            Button delBtn = v.getDelete();
            addButtonStyles(delBtn);
            delBtn.setOnAction(e->{
                try {
                    CrudUtil.execute("DELETE FROM Locomotive WHERE Reg_No=?",v.getReg_No());
                    loadAllVehicles();
                    loadAllSchedules();
                } catch (SQLException | ClassNotFoundException ex) {
                    ex.printStackTrace();
                }
            });

            // check whether driver is scheduled or not
            ResultSet r = CrudUtil.execute("SELECT COUNT(Vehicle_Num) FROM Schedule WHERE Schedule.Vehicle_Num=?",v.getReg_No());
            if (r.next()) {
                if(r.getInt(1)==1){
                    v.setScheduled(true);
                }
            }
            DataSet.vehicles.add(v);
            obList.add(v);
        }
        vehicleTable.setItems(obList);
        fillTrainCombo();
    }

    private void loadAllDrivers() throws SQLException, ClassNotFoundException {

        ResultSet result = CrudUtil.execute("SELECT * FROM Driver");
        ObservableList<Driver> obList = FXCollections.observableArrayList();
        DataSet.drivers.clear();

        while (result.next()){

            Driver d = new Driver(
                    result.getString(1),
                    result.getString(2),
                    result.getInt(3),
                    result.getString(4),
                    result.getString(5),
                    result.getString(6)
            );

            Button delBtn = d.getDelete();
            addButtonStyles(delBtn);
            delBtn .setOnAction(e->{
                try {
                    CrudUtil.execute("DELETE FROM Driver WHERE ID=?",d.getID());
                    loadAllDrivers();
                    loadAllSchedules();
                } catch (SQLException | ClassNotFoundException ex) {
                    ex.printStackTrace();
                }
            });

            // check whether driver is scheduled or not
            ResultSet r = CrudUtil.execute("SELECT COUNT(Driver) FROM Schedule WHERE Schedule.Driver=?",d.getID());
            if (r.next()) {
                 if(r.getInt(1)==1){
                     d.setScheduled(true);
                 }
            }

            DataSet.drivers.add(d);
            obList.add(d);

        }

        driverTable.setItems(obList);
        fillDriverCombo();

    }

    private void loadAllStations() throws SQLException, ClassNotFoundException {
        ResultSet st = CrudUtil.execute("SELECT * FROM Station");
        while (st.next()){
            DataSet.stations.add(
                    new Station(
                            st.getString(1),
                            st.getInt(2),
                            st.getDouble(3),
                            st.getDouble(4),
                            st.getDouble(5)
                    )
            );
        }
    }

    private void loadAllSchedules() throws SQLException, ClassNotFoundException {
        ResultSet schedules = CrudUtil.execute("SELECT * FROM Schedule");
        ObservableList<Schedule> sch = FXCollections.observableArrayList();

        while(schedules.next()){
            String begin = ""; String end = ""; String type = ""; int passengers = 0;
            for (Locomotive l: DataSet.vehicles) {
                if(l.getReg_No().equals(schedules.getString(1))){
                    type = l.getType();
                    passengers = l.getSeating();
                }
            }
            for (Station s: DataSet.stations) {
                if(schedules.getInt(5)==s.getSt_no()){
                    begin = s.getTag();
                }
                if(schedules.getInt(6)==s.getSt_no()){
                    end = s.getTag();
                }
            }
            Schedule s = new Schedule(
                    schedules.getString(1),
                    schedules.getString(2),
                    schedules.getString(3),
                    schedules.getString(4),
                    begin,
                    end,
                    type,
                    passengers
            );

            Button delBtn = s.getDelete();
            addButtonStyles(delBtn);
            delBtn.setOnAction(e->{
                try {
                    CrudUtil.execute("DELETE FROM Schedule WHERE Vehicle_Num=?",s.getV_num());
                    loadAllSchedules();
                    loadAllDrivers();
                    loadAllVehicles();

                } catch (SQLException | ClassNotFoundException ex) {
                    ex.printStackTrace();
                }
            });

            sch.add(s);
        }
        scheduleTbl.setItems(sch);
    }

    //==========================================================================================

    public void updateDriverOnAction(ActionEvent actionEvent) throws SQLException, ClassNotFoundException {
        updateDriverBTN.setDisable(true);
        try{
            CrudUtil.execute("UPDATE Driver SET ID=?,Age=?,Name=?,TP_Number=?,Address=? WHERE NIC=?",
                    idTXT.getText(),
                    Integer.parseInt(ageTXT.getText()),
                    nameTXT.getText(),
                    contactTXT.getText(),
                    addressTXT.getText(),
                    nicTXT.getText());
        } catch (Exception e) {
            new Alert(Alert.AlertType.ERROR,e.getMessage()).show();
        }
        loadAllDrivers();
        loadAllSchedules();
        updateDriverBTN.setDisable(true);
    }

    public void addDriverOnAction(ActionEvent actionEvent) throws SQLException, ClassNotFoundException {
        try{
            CrudUtil.execute("INSERT INTO Driver VALUES (?,?,?,?,?,?)",
                    nicTXT.getText(),
                    idTXT.getText(),
                    Integer.parseInt(ageTXT.getText()),
                    nameTXT.getText(),
                    contactTXT.getText(),
                    addressTXT.getText());
        } catch (Exception e) {
            new Alert(Alert.AlertType.ERROR,e.getMessage()).show();
        }
        loadAllDrivers();
    }

    public void nicEnteredOnAction(ActionEvent actionEvent) throws SQLException, ClassNotFoundException {
        ResultSet result = CrudUtil.execute("SELECT * FROM Driver WHERE NIC=?",nicTXT.getText());
        if(result.next()){
            idTXT.setText(result.getString(2));
            ageTXT.setText(result.getString(3));
            nameTXT.setText(result.getString(4));
            contactTXT.setText(result.getString(5));
            addressTXT.setText(result.getString(6));

            // disabling buttons
            updateDriverBTN.setDisable(false);
        }
    }

    //==========================================================================================

    public void addVehicleOnAction(ActionEvent actionEvent) throws SQLException, ClassNotFoundException {
        try{
            ArrayList<Integer> cls = new ArrayList<>();
            if(fClassCheck.isSelected()){
                cls.add(1);
            }
            if(sClassCheck.isSelected()){
                cls.add(2);
            }
            if(tClassCheck.isSelected()){
                cls.add(3);
            }
            if(cls.size()==0){
                new Alert(Alert.AlertType.WARNING,"please select at least one compartment-class").show();
                return;
            }else{
                CrudUtil.execute("INSERT INTO Locomotive VALUES(?,?,?,?,?,?)",
                        vNumTxt.getText(),
                        locoTxt.getText(),
                        typeTxt.getValue(),
                        compartmentsSpinner.getValue(),
                        compartmentsSpinner.getValue()*72,
                        cls.toString()
                );
            }
        } catch (Exception e) {
            new Alert(Alert.AlertType.ERROR,e.getMessage()).show();
        }
        loadAllVehicles();
    }

    public void updateVehicleOnAction(ActionEvent actionEvent) throws SQLException, ClassNotFoundException {
        ResultSet r = CrudUtil.execute("SELECT COUNT(Vehicle_Num) FROM Schedule WHERE Vehicle_Num=?",vNumTxt.getText());
        if(r.next()){
            if(r.getInt(1)==0){
                try{
                    ArrayList<Integer> cls = new ArrayList<>();
                    if(fClassCheck.isSelected()){
                        cls.add(1);
                    }
                    if(sClassCheck.isSelected()){
                        cls.add(2);
                    }
                    if(tClassCheck.isSelected()){
                        cls.add(3);
                    }
                    if(cls.size()==0){
                        new Alert(Alert.AlertType.WARNING,"please select at least one compartment-class").show();
                        return;
                    }else{
                        CrudUtil.execute("UPDATE Locomotive SET Tag=?,Type=?,Compartments=?,Seating=?,Classes=? WHERE Reg_No=?",
                                locoTxt.getText(),
                                typeTxt.getValue(),
                                compartmentsSpinner.getValue(),
                                compartmentsSpinner.getValue()*72,
                                cls.toString(),
                                vNumTxt.getText()
                        );
                    }
                } catch (Exception e) {
                    new Alert(Alert.AlertType.ERROR,e.getMessage()).show();
                }
                loadAllVehicles();
                loadAllSchedules();
                updateVehicleBtn.setDisable(true);
            }else{
                new Alert(Alert.AlertType.WARNING,"Cannot update a vehicle which is on schedule").show();
                return;
            }
        }
    }

    public void vehicleSearchOnAction(ActionEvent actionEvent) throws SQLException, ClassNotFoundException {
        ResultSet result = CrudUtil.execute("SELECT * FROM Locomotive WHERE Reg_No=?",vNumTxt.getText());
        if(result.next()){
            locoTxt.setText(result.getString(2));
            if(result.getString(3).equals("Intercity")){
                typeTxt.setValue("Intercity");
            }else{
                typeTxt.setValue("Express");
            }

            SpinnerValueFactory<Integer> valueFactory = new SpinnerValueFactory.IntegerSpinnerValueFactory(1, 10, result.getInt(4));
            compartmentsSpinner.setValueFactory(valueFactory);

            char[] cls = result.getString(6).toCharArray();
            for (char c:
                 cls) {
                int x = Character.getNumericValue(c);
                if(x==1){
                    fClassCheck.setSelected(true);
                }
                if(x==2){
                    sClassCheck.setSelected(true);
                }
                if(x==3){
                    tClassCheck.setSelected(true);
                }
            }

            // enabling buttons
            updateVehicleBtn.setDisable(false);
        }
    }

    //==========================================================================================

    private void addButtonStyles(Button b){
        b.setOnMouseEntered(e -> b.setStyle("-fx-text-fill: white; -fx-background-color: #5e3315; -fx-cursor: hand"));
        b.setOnMouseExited(e -> b.setStyle(null));
    }

    //==========================================================================================

    // filling combos
    private void fillTrainCombo(){
        ObservableList<String> trainLst = FXCollections.observableArrayList();
        // vehicle combo
        for (Locomotive l: DataSet.vehicles) {
            if(!l.isScheduled()) {
                trainLst.add(l.getReg_No());
            }
        }
        if(trainsForScheduleCombo.getItems().isEmpty()){
            trainsForScheduleCombo.setItems(trainLst);
        }else {
            trainsForScheduleCombo.getItems().clear();
            trainsForScheduleCombo.setItems(trainLst);
        }

    }

    private void fillDriverCombo(){

        // driver combo
        ObservableList<String> driverLst = FXCollections.observableArrayList();
        for (Driver d: DataSet.drivers) {
            if(!d.isScheduled()){
                driverLst.add(d.getID());
            }
        }

        if(driversForScheduleCombo.getItems().isEmpty()){
            driversForScheduleCombo.setItems(driverLst);
        }else{
            driversForScheduleCombo.getItems().clear();
            driversForScheduleCombo.setItems(driverLst);
        }

    }

    public void fillStationCombos(){
        // from - to combos
        ObservableList<String> stLst = FXCollections.observableArrayList();
        for (Station s: DataSet.stations) {
            stLst.add(s.getTag());
        }
        fromCombo.setItems(stLst);
    }

    //==========================================================================================

    public void scheduleOnAction(ActionEvent actionEvent) throws SQLException, ClassNotFoundException {

        String type = null;
        int passengers = 0;
        for (Locomotive l: DataSet.vehicles) {
            if(l.getReg_No().equals(trainsForScheduleCombo.getValue())){
                type = l.getType();
                passengers = l.getSeating();
            }
        }

        try{
            CrudUtil.execute("INSERT INTO Schedule VALUES (?,?,?,?,?,?,?,?,? )",
                    trainsForScheduleCombo.getValue(),
                    vehicleLabel.getText(),
                    driversForScheduleCombo.getValue(),
                    timePicker.getValue(),
                    beginSt,
                    endSt,
                    type,
                    DataSet.selectedStNos.toString(),
                    passengers
                    );
        } catch (Exception e) {
            new Alert(Alert.AlertType.ERROR,e.getMessage()).show();
        }


        // disabling / enabling
        toCombo.setDisable(true);

         // refreshing
        toCombo.setValue(null);
        fromCombo.setValue(null);
        stopsTxtArea.setText(null);
        trainsForScheduleCombo.setValue(null);
        driversForScheduleCombo.setValue(null);
        vehicleLabel.setText(null);
        driverLabel.setText(null);
        timePicker.setValue(null);

        loadAllSchedules();
        loadAllVehicles();
        loadAllDrivers();

    }

    public void trainSelectedOnAction(ActionEvent actionEvent) {
        if(trainsForScheduleCombo.getValue()!=null){
            for (Locomotive l:
                    DataSet.vehicles) {
                if(l.getReg_No().equals(trainsForScheduleCombo.getValue().toString())){
                    vehicleLabel.setText(l.getTag());
                }
            }
        }
    }

    public void driverSelectedOnAction(ActionEvent actionEvent) {
        if(driversForScheduleCombo.getValue()!=null){
            for (Driver d:
                    DataSet.drivers) {
                if(d.getID().equals(driversForScheduleCombo.getValue().toString())){
                    driverLabel.setText(d.getName());
                }
            }
        }
    }

    public void loadStationsOnAction(ActionEvent actionEvent) throws IOException {
        stSelection.setScene(new Scene(FXMLLoader.load(getClass().getResource("../view/st-selection.fxml"))));
        //stSelection.initStyle(StageStyle.UNDECORATED);
        stSelection.show();
        stSelectionController.txtStations = stopsTxtArea;
        stSelectionController.managementPane = mainPane;
        GaussianBlur gb = new GaussianBlur();
        mainPane.setEffect(gb);
    }

    // ------------------------------------------------------------------------------------------------------

    public void fromStationSelectedOnAction(ActionEvent actionEvent) {
        ObservableList<String> stLst = FXCollections.observableArrayList();
        String toStation = null;
        if (fromCombo.getValue()!=null){
            toStation = fromCombo.getValue().toString();
            System.out.println(toStation);
        }
        if(toStation!=null){
            for (Station s: DataSet.stations) {
                if(!toStation.equals(s.getTag())){
                    stLst.add(s.getTag());
                }
            }
        }
        
        toCombo.setItems(stLst);
        toCombo.setDisable(false);
    }

    public void toStationSelectedOnAction(ActionEvent actionEvent) {
        loadStationBtn.setDisable(false);
        for (Station s: DataSet.stations) {
            if(s.getTag().equals(fromCombo.getValue())){
                beginSt = s.getSt_no();
            }
            if(s.getTag().equals(toCombo.getValue())){
                endSt = s.getSt_no();
            }
        }

    }

    private String[] calDateAndTime(){
        LocalDateTime myDateObj = LocalDateTime.now();
        DateTimeFormatter myFormatObj = DateTimeFormatter.ofPattern("LL-c-yyyy HH:mm:ss a");
        String formattedDate = myDateObj.format(myFormatObj);
        String[] dateAndTime = formattedDate.split(" ");
        return dateAndTime;
    }



}
