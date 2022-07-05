package controller;

import javafx.concurrent.Task;
import javafx.scene.input.KeyEvent;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.util.JRLoader;
import net.sf.jasperreports.view.JasperViewer;
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
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import model.Passenger;
import model.Reservation;
import model.Station;
import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.concurrent.CountDownLatch;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static java.lang.Thread.sleep;

public class passengerHandlerController {

    public AnchorPane mainPane;

    public TableView<Passenger> passengerTable;
    public TableView<Reservation> reservationTable;

    // reserve tab
    public TextField nicTxt;
    public TextField nameTxt;
    public TextField tpTxt;
    public TextArea addressTxt;

    public ComboBox startCombo;
    public ComboBox endCombo;
    public Label vehicleTagLabel;
    public Label valueLabel;
    public ComboBox vehicleCombo;
    public Label timeLabel;
    public Label vehicleTypeLabel;
    public Spinner c2Spinner;
    public Spinner c1Spinner;
    public Spinner c3Spinner;

    // passenger table
    public TableColumn pasNic;
    public TableColumn pasName;
    public TableColumn pasAddress;
    public TableColumn pasTpNo;
    public TableColumn passOptions;

    // reservation table
    public TableColumn resNIC;
    public TableColumn vehicleRes;
    public TableColumn trainRes;
    public TableColumn seatsRes;
    public TableColumn inRes;
    public TableColumn outRes;
    public TableColumn valueRes;
    public TableColumn optionsRes;

    // date and time
    public Label lblDate;
    public Label lblTime;
    public Label amLbl;

    // dashboard
    public Label noOfTrainsLbl;
    public Label noOfPassengersLabel;
    public Label noOfBookingsLbl;
    public Button printReceiptBtn;

    // receipt
    public Label recName;
    public Label recCargo;
    public Label recTotal;
    public Label recTo;
    public Label recFrom;
    public Label recC1;
    public Label recC2;
    public Label recC3;

    public Label nameTxtLabel;
    public Label timeTxtLabel;

    private String NameRegex = "^[A-Z]\\.[A-Z]\\.[A-Z][a-z]{3,15}$";
    private String idRegex = "^[0-9]{12}$";
    private String tpRegex = "^[0-9]{10}$";
    private String addressRegex = "(^[0-9]{1,3}\\/[A-Z][a-z]{3,15}\\/[A-Z][a-z]{3,15}$)|(^[0-9]{1,3}[A-Z]\\/[A-Z][a-z]{3,15}\\/[A-Z][a-z]{3,15}$)";
    private String forValid = "-fx-text-inner-color: green;";
    private String forInvalid = "-fx-text-inner-color: red;";

    public Button reserveBtn;

    Stage passengerHandler;

    static int startStNo = 0;
    static int endStNo = 0;

    static float c1Value = 0;
    static float c2Value = 0;
    static float c3value = 0;

    private boolean isIdValidated = false;
    private boolean isNameValidated = false;
    private boolean isTpValidated = false;
    private boolean isAddressValidated = false;

    private HashMap<String,String> trainLoader() throws SQLException, ClassNotFoundException {
        HashMap<String ,String> trains = new HashMap<>();
        ResultSet rs = CrudUtil.execute("SELECT Tag,Departure FROM Schedule");
        while(rs.next()){
            trains.put(rs.getString(1),rs.getString(2));
        }
        return trains;
    }

    private void addButtonStyles(Button b){
        b.setOnMouseEntered(e -> b.setStyle("-fx-text-fill: white; -fx-background-color: #5e3315; -fx-cursor: hand"));
        b.setOnMouseExited(e -> b.setStyle(null));
    }

    public boolean validator(String pattern, String matcher){
        Pattern pat = Pattern.compile(pattern);
        Matcher mat = pat.matcher(matcher);
        boolean matchFound = mat.find();
        return matchFound;
    }


    // printing the e-receipt
    public void printReceiptOnAction(ActionEvent actionEvent) throws JRException {

        int seats = Integer.parseInt(recC1.getText()) + Integer.parseInt(recC2.getText()) + Integer.parseInt(recC3.getText());
        HashMap map = new HashMap();
        map.put("too",recTo.getText());
        map.put("passengerName",recName.getText());
        map.put("cargo",recCargo.getText());
        map.put("date",lblDate.getText());
        map.put("time",lblTime.getText());
        map.put("total",recTotal.getText());
        map.put("seats",String.valueOf(seats));
        map.put("from",recFrom.getText());

        JasperReport compiledReport = (JasperReport) JRLoader.loadObject(this.getClass().getResource("../view/reportData/ticket.jasper"));
        JasperPrint jasperPrint;
        jasperPrint = JasperFillManager.fillReport(compiledReport, map, new JREmptyDataSource(1));
        JasperViewer.viewReport(jasperPrint, false);
    }

    public void nicKeyReleased(KeyEvent keyEvent) {
        if(validator(idRegex,nicTxt.getText())){
            nicTxt.setStyle(forValid);
            isIdValidated=true;
        }else{
            nicTxt.setStyle(forInvalid);
            isIdValidated=false;
        }
        checker();
    }

    public void nameKeyReleased(KeyEvent keyEvent) {
        if(validator(NameRegex,nameTxt.getText())){
            nameTxt.setStyle(forValid);
            isNameValidated=true;
        }else{
            nameTxt.setStyle(forInvalid);
            isNameValidated=false;
        }
        checker();
    }

    public void tpKeyReleased(KeyEvent keyEvent) {
        if(validator(tpRegex,tpTxt.getText())){
            tpTxt.setStyle(forValid);
            isTpValidated=true;
        }else{
            tpTxt.setStyle(forInvalid);
            isTpValidated=false;
        }
        checker();
    }

    public void addressKeyreleased(KeyEvent keyEvent) {
        if(validator(addressRegex,addressTxt.getText())){
            addressTxt.setStyle(forValid);
            isAddressValidated=true;
        }else{
            addressTxt.setStyle(forInvalid);
            isAddressValidated=false;
        }
        checker();
    }

    Task<Void> task1 = new Task<Void>() {
        @Override
        protected Void call() throws Exception {
           Platform.runLater(() -> {
                try {
                    HashMap<String,String> trainMap = trainLoader();
                   //while (true){
                        for (Map.Entry<String,String> entry : trainMap.entrySet()) {

                            timeTxtLabel.setText(entry.getValue());
                            nameTxtLabel.setText(entry.getKey());
                            System.out.println(entry.getValue());
                            System.out.println(entry.getKey());
                            break;
                        }
                    //}
                } catch (SQLException e) {
                    e.printStackTrace();
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                }

           });
            return null;
        }
    };


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
                                lblDate.setText(dt);
                                count++;
                            }else if(count==1){
                                lblTime.setText(dt);
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

    public void initialize() throws SQLException, ClassNotFoundException {

        Thread trainSetter = new Thread(task1);

        //Platform.runLater(trainSetter);

        timerThread timer = new timerThread();

        //Platform.runLater(timer);

        timer.start();
        trainSetter.start();


        reserveBtn.setDisable(true);

        settingDashboard();

        // passenger table - columns
        pasNic.setCellValueFactory(new PropertyValueFactory<>("NIC"));
        pasName.setCellValueFactory(new PropertyValueFactory<>("Name"));
        pasTpNo.setCellValueFactory(new PropertyValueFactory<>("TP_Number"));
        pasAddress.setCellValueFactory(new PropertyValueFactory<>("Address"));
        passOptions.setCellValueFactory(new PropertyValueFactory<>("delete"));

        // reservation table - columns
        resNIC.setCellValueFactory(new PropertyValueFactory<>("NIC"));
        vehicleRes.setCellValueFactory(new PropertyValueFactory<>("Reg_No"));
        trainRes.setCellValueFactory(new PropertyValueFactory<>("tag"));
        seatsRes.setCellValueFactory(new PropertyValueFactory<>("seating"));
        inRes.setCellValueFactory(new PropertyValueFactory<>("begin"));
        outRes.setCellValueFactory(new PropertyValueFactory<>("end"));
        valueRes.setCellValueFactory(new PropertyValueFactory<>("value"));
        optionsRes.setCellValueFactory(new PropertyValueFactory<>("delete"));


        // adding spinner data
        SpinnerValueFactory<Integer> c1Factory = new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 10, 0);
        SpinnerValueFactory<Integer> c2Factory = new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 10, 0);
        SpinnerValueFactory<Integer> c3Factory = new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 10, 0);
        c1Spinner.setValueFactory(c1Factory);
        c2Spinner.setValueFactory(c2Factory);
        c3Spinner.setValueFactory(c3Factory);

        c1Spinner.setDisable(true);
        c2Spinner.setDisable(true);
        c3Spinner.setDisable(true);

        loadAllStations();
        loadPassengersAndReservations();

        ObservableList<String> lst = FXCollections.observableArrayList();
        for (Station s: DataSet.stations){
            lst.add(s.getTag());
        }
        startCombo.setItems(lst);
        endCombo.setItems(lst);

        // adding a listener to fx-spinners
        c1Spinner.valueProperty().addListener((obs, oldValue, newValue) -> {
            toDoInListener((int)newValue,1);
            }
        );

        c2Spinner.valueProperty().addListener((obs, oldValue, newValue) -> {
            toDoInListener((int)newValue,2);
            }
        );

        c3Spinner.valueProperty().addListener((obs, oldValue, newValue) -> {
                    toDoInListener((int)newValue,3);
                }
                );
    }

    private void toDoInListener(int newVal, int spinner){
        if(spinner==1){
            int c1 = (int) c1Value * newVal;
            int c2 = (int) c2Value * (int) c2Spinner.getValue();
            int c3 = (int) c3value * (int) c3Spinner.getValue();
            valueLabel.setText(c1+c2+c3+".00");
        }
        else if(spinner==2){
            int c2 = (int) c2Value * newVal;
            int c1 = (int) c1Value * (int) c1Spinner.getValue();
            int c3 = (int) c3value * (int) c3Spinner.getValue();
            valueLabel.setText(c1+c2+c3+".00");
        }
        else {
            int c3 = (int) c3value * newVal;
            int c1 = (int) c1Value * (int) c1Spinner.getValue();
            int c2 = (int) c2Value * (int) c2Spinner.getValue();
            valueLabel.setText(c1+c2+c3+".00");
        }
    }

    private void settingDashboard() throws SQLException, ClassNotFoundException {
        ResultSet bCount = CrudUtil.execute("SELECT COUNT(Vehicle_regNo) FROM Reservation");
        if(bCount.next()){
            noOfBookingsLbl.setText(String.valueOf(bCount.getInt(1)));
        }
        ResultSet psCount = CrudUtil.execute("SELECT COUNT(NIC) FROM Passenger");
        if(psCount.next()){
            noOfPassengersLabel.setText(String.valueOf(psCount.getInt(1)));
        }
        ResultSet tCount = CrudUtil.execute("SELECT COUNT(Reg_No) FROM Locomotive");
        if(tCount.next()){
            noOfTrainsLbl.setText(String.valueOf(tCount.getInt(1)));
        }
    }

    public void logOut(ActionEvent actionEvent) throws IOException {
        passengerHandler = (Stage) mainPane.getScene().getWindow();
        passengerHandler.close();
        passengerHandler.setScene(new Scene(FXMLLoader.load(getClass().getResource("../view/login.fxml"))));
        passengerHandler.show();
    }

    public void vehicleSelectedOnAction(ActionEvent actionEvent) throws SQLException, ClassNotFoundException {
        c1Spinner.setDisable(true);
        c2Spinner.setDisable(true);
        c3Spinner.setDisable(true);
        // adding data to the labels
        ResultSet rs = CrudUtil.execute("SELECT * FROM Schedule WHERE Vehicle_Num=?",vehicleCombo.getValue());
        if(rs.next()){
           vehicleTagLabel.setText(rs.getString(2));
           vehicleTypeLabel.setText(rs.getString(7));
           timeLabel.setText(rs.getString(4));
            enablingSpinners(rs.getString(1));
        }
    }

    private double settingTicketPrices(int start, int end, String cls) throws SQLException, ClassNotFoundException {
        double val = 0;
        ResultSet value1 = CrudUtil.execute("SELECT " + cls + " FROM Station WHERE St_No=?",start);
        ResultSet value2 = CrudUtil.execute("SELECT " + cls + " FROM Station WHERE St_No=?",end);
        if(value1.next() & value2.next()) {
            if (value1.getInt(1) > value2.getInt(1)) {
                val = value1.getInt(1) - value2.getInt(1);
            } else {
                val = value2.getInt(1) - value1.getInt(1);
            }
        }
        return val;
    }

    private void loadPassengersAndReservations() throws SQLException, ClassNotFoundException {

        ObservableList<Passenger> psg = FXCollections.observableArrayList();
        // adding data to the passenger table
        ResultSet passengers = CrudUtil.execute("SELECT * FROM Passenger");
        while (passengers.next()){
            Passenger ps = new Passenger(
                    passengers.getString(1),
                    passengers.getString(2),
                    passengers.getString(3),
                    passengers.getString(4)
            );
            addButtonStyles(ps.getDelete());
            ps.getDelete().setOnAction(e->{
                try {
                    CrudUtil.execute("DELETE FROM Passenger WHERE NIC=?",ps.getNIC());
                    // load all passengers again
                    loadPassengersAndReservations();
                    settingDashboard();
                } catch (SQLException throwables) {
                    throwables.printStackTrace();
                } catch (ClassNotFoundException classNotFoundException) {
                    classNotFoundException.printStackTrace();
                }
            });

            psg.add(ps);

        }
        passengerTable.setItems(psg);

        ObservableList<Reservation> rsv = FXCollections.observableArrayList();
        ResultSet reservations = CrudUtil.execute("SELECT * FROM Reservation");
        while (reservations.next()){
            String startSt = null;
            String endSt = null;
            for (Station s : DataSet.stations) {
                if(s.getSt_no()==reservations.getInt(5)) startSt=s.getTag();
                if(s.getSt_no()==reservations.getInt(6)) endSt=s.getTag();
            }
            Reservation rs = new Reservation(
                    reservations.getString(1),
                    reservations.getString(2),
                    reservations.getString(3),
                    reservations.getInt(4),
                    startSt,
                    endSt,
                    reservations.getDouble(7)
            );
            addButtonStyles(rs.getDelete());
            rs.getDelete().setOnAction(e->{
                try {
                    ResultSet rS = CrudUtil.execute("SELECT Seating FROM Locomotive WHERE Reg_No=?", rs.getReg_No());
                    int seats;
                    if(rS.next()){
                        seats = rS.getInt(1);
                    }else {seats = 0;}

                    CrudUtil.execute("UPDATE Locomotive SET Seating=? WHERE Reg_No=?",seats+rs.getSeating(),rs.getReg_No());

                    CrudUtil.execute("DELETE FROM Reservation WHERE Passenger_NIC=? AND Vehicle_regNo=?",rs.getNIC(),rs.getReg_No());


                    // load all passengers again
                    loadPassengersAndReservations();
                    settingDashboard();
                } catch (SQLException throwables) {
                    throwables.printStackTrace();
                } catch (ClassNotFoundException classNotFoundException) {
                    classNotFoundException.printStackTrace();
                }
            });

            rsv.add(rs);
        }
        reservationTable.setItems(rsv);



    }

    public void reserveOnAction(ActionEvent actionEvent) throws SQLException, ClassNotFoundException {
        ResultSet rs = CrudUtil.execute("SELECT Seating FROM Locomotive WHERE Reg_No=?", vehicleCombo.getValue().toString());
        int seats;
        if(rs.next()){
            seats = rs.getInt(1);
        }else {seats = 0;}

        int seatings = 0;
        seatings += (int)c1Spinner.getValue() + (int)c2Spinner.getValue() + (int)c3Spinner.getValue();

        if(seats-seatings>=0){
            try {

                CrudUtil.execute("UPDATE Locomotive SET Seating=? WHERE Reg_No=?" ,seats-seatings,vehicleCombo.getValue().toString());
            } catch (SQLException | ClassNotFoundException | NumberFormatException e) {
                new Alert(Alert.AlertType.ERROR,e.getMessage()).show();
                return;
            }
            try {
                CrudUtil.execute("INSERT INTO Passenger VALUES(?,?,?,?)",
                        nicTxt.getText(),
                        nameTxt.getText(),
                        tpTxt.getText(),
                        addressTxt.getText()
                );
            } catch (SQLException | ClassNotFoundException e) {
                new Alert(Alert.AlertType.ERROR,e.getMessage()).show();
            }


            try {
                CrudUtil.execute("INSERT INTO Reservation VALUES(?,?,?,?,?,?,?)",
                        nicTxt.getText(),
                        vehicleCombo.getValue(),
                        vehicleTagLabel.getText(),
                        seatings,
                        startStNo,
                        endStNo,
                        Double.parseDouble(valueLabel.getText())
                );
            } catch (SQLException | ClassNotFoundException | NumberFormatException e) {
                new Alert(Alert.AlertType.ERROR,e.getMessage()).show();
                return;
            }


            ResultSet tag = CrudUtil.execute("SELECT Tag FROM Locomotive WHERE Reg_No=?",vehicleCombo.getValue().toString());
            if(tag.next()){
                recCargo.setText(tag.getString(1));
            }
            recName.setText(nameTxt.getText());
            recFrom.setText(startCombo.getValue().toString());
            recTo.setText(endCombo.getValue().toString());
            recC1.setText(c1Spinner.getValue().toString());
            recC2.setText(c2Spinner.getValue().toString());
            recC3.setText(c3Spinner.getValue().toString());
            recTotal.setText(valueLabel.getText());




            // loading reservation,passenger tables
            loadPassengersAndReservations();

            // disabling - enabling
            startCombo.setValue(null);
            endCombo.setValue(null);
            vehicleCombo.setItems(null);
            c1Spinner.setDisable(true);
            c2Spinner.setDisable(true);
            c3Spinner.setDisable(true);

            // adding spinner data
            SpinnerValueFactory<Integer> c1Factory = new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 10, 0);
            SpinnerValueFactory<Integer> c2Factory = new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 10, 0);
            SpinnerValueFactory<Integer> c3Factory = new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 10, 0);

            c1Spinner.setValueFactory(c1Factory);
            c2Spinner.setValueFactory(c2Factory);
            c3Spinner.setValueFactory(c3Factory);

            vehicleTagLabel.setText(null);
            vehicleTypeLabel.setText(null);
            timeLabel.setText(null);

            settingDashboard();
        }else {
            new Alert(Alert.AlertType.WARNING,"train is full....");
            return;
        }

    }

    public void startStSelectedOnAction(ActionEvent actionEvent) {
        if(startCombo.getValue()!=null){
            for (Station s: DataSet.stations) {
                if (startCombo.getValue().equals(s.getTag())){
                    startStNo = s.getSt_no();
                }
            }
        }
    }

    public void endStSelectedOnAction(ActionEvent actionEvent) throws SQLException, ClassNotFoundException {

        if(endCombo.getValue()!=null){
            for (Station s: DataSet.stations) {
                if (endCombo.getValue().equals(s.getTag())){
                    endStNo = s.getSt_no();
                }
            }
        }


        // load trains according to destination selected
        boolean found1 = false;
        boolean found2 = true;
        ResultSet rs = CrudUtil.execute("SELECT * FROM Schedule");
        ObservableList<String> idLst = FXCollections.observableArrayList();

        while (rs.next()){

            String[] splits =   rs.getObject(8).toString().replace("[","").replace(" ","").replace("]","").split(",");
            ArrayList<String> stationLst = new ArrayList<>(Arrays.asList(splits));
            System.out.println(stationLst);

            for (String no : stationLst) {
                if(Integer.parseInt(no)==startStNo){
                    found1=true;
                }
                if(Integer.parseInt(no)==endStNo){
                    found2=true;
                }
            }

            // setting the vehicle to combo
            if(found1 & found2) {
                idLst.add(rs.getString(1));
            }

           }

        vehicleCombo.setItems(idLst);

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

    private void enablingSpinners(String id) throws SQLException, ClassNotFoundException {
        ResultSet classes = CrudUtil.execute("SELECT Classes FROM Locomotive WHERE Reg_No=?",id);
        if(classes.next()){
            String[] splits =   classes.getObject(1).toString().replace("[","").replace(" ","").replace("]","").split(",");
            ArrayList<String> classesLst = new ArrayList<>(Arrays.asList(splits));
            if(classesLst.contains("1")){
                c1Value = (float) settingTicketPrices(startStNo,endStNo,"C1");
                System.out.println(c1Value);
                c1Spinner.setDisable(false);
            }
            if(classesLst.contains("2")){
                c2Value = (float) settingTicketPrices(startStNo,endStNo,"C2");
                System.out.println(c2Value);
                c2Spinner.setDisable(false);
            }
            if(classesLst.contains("3")){
                c3value = (float) settingTicketPrices(startStNo,endStNo,"C3");
                System.out.println(c3value);
                c3Spinner.setDisable(false);
            }
        }
    }

    private void checker(){
        if(isIdValidated & isAddressValidated & isNameValidated & isTpValidated){
            reserveBtn.setDisable(false);
        }else {
            reserveBtn.setDisable(true);
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
