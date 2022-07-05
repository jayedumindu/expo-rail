package controller;

import db.DataSet;
import javafx.event.ActionEvent;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TextArea;
import javafx.scene.effect.GaussianBlur;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import model.Station;

import java.util.ArrayList;

public class stSelectionController {

    public static TextArea txtStations;

    public static  AnchorPane managementPane;
    public AnchorPane mainPane;

    Stage selector;

    public Button addStopBtn;

    public CheckBox s1;
    public CheckBox s13;
    public CheckBox s6;
    public CheckBox s8;
    public CheckBox s9;
    public CheckBox s11;
    public CheckBox s15;
    public CheckBox s18;
    public CheckBox s19;
    public CheckBox s23;
    public CheckBox s2;
    public CheckBox s4;
    public CheckBox s5;
    public CheckBox s7;
    public CheckBox s17;
    public CheckBox s10;
    public CheckBox s12;
    public CheckBox s14;
    public CheckBox s21;
    public CheckBox s16;
    public CheckBox s3;
    public CheckBox s20;
    public CheckBox s22;

    ArrayList<CheckBox> stations = new ArrayList<>();

    public void initialize(){
        stations.add(s1);
        stations.add(s2);
        stations.add(s3);
        stations.add(s4);
        stations.add(s5);
        stations.add(s6);
        stations.add(s7);
        stations.add(s8);
        stations.add(s9);
        stations.add(s10);
        stations.add(s11);
        stations.add(s12);
        stations.add(s13);
        stations.add(s14);
        stations.add(s15);
        stations.add(s16);
        stations.add(s17);
        stations.add(s18);
        stations.add(s19);
        stations.add(s20);
        stations.add(s21);
        stations.add(s22);
        stations.add(s23);

        managingCheckBoxes(ManagementController.beginSt,ManagementController.endSt);

//        mainPane.getScene().getWindow().setOnCloseRequest(e->{
//            ActionEvent ac = new ActionEvent();
//            addStopsOnAction(ac);
//        });

        GaussianBlur gb = new GaussianBlur();
        if(managementPane!=null){
            managementPane.setEffect(gb);
        }


    }

    public void addStopsOnAction(ActionEvent actionEvent) {
        txtStations.setText(null);
        DataSet.selectedStNos.clear();
        String selectedStations = "";
        for (CheckBox cb: stations) {
            if (cb.isSelected()){
                for (Station s: DataSet.stations) {
                    if(Integer.parseInt(cb.getText())==s.getSt_no()){
                        selectedStations+=s.getTag()+"\n";
                        DataSet.selectedStNos.add(s.getSt_no());
                    }
                }
            }
        }

        txtStations.setText(selectedStations);
        System.out.println(DataSet.selectedStNos);

        selector = (Stage) mainPane.getScene().getWindow();
        selector.close();
        managementPane.setEffect(null);

    }

    private void managingCheckBoxes(int start, int end){
        ArrayList<Integer> toBeFree = new ArrayList<>();

        // selecting and disabling start and end
        for (CheckBox c: stations) {
            if(Integer.parseInt(c.getText())==start){
                c.setSelected(true);
                c.setDisable(true);
            }else if(Integer.parseInt(c.getText())==end){
                c.setSelected(true);
                c.setDisable(true);
            }
        }

        if(end>start){
            while (start!=end){
                toBeFree.add(start++);
            }
        }else if(start>end){
            while (start!=end){
                toBeFree.add(start--);
            }
        }

        for (CheckBox c: stations) {
            if(!toBeFree.contains(Integer.parseInt(c.getText()))){
                c.setDisable(true);
            }
        }

    }

}
