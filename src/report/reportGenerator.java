package report;

import db.DBConnection;
import javafx.scene.control.Alert;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.data.JRBeanArrayDataSource;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.engine.util.JRLoader;
import net.sf.jasperreports.view.JasperViewer;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Collection;
import java.util.HashMap;

public class reportGenerator {

    private JasperReport jrLoader(String URI) throws JRException {
        return (JasperReport) JRLoader.loadObject(this.getClass().getResource(URI));
    }

    public void reportGeneratorWithSQL(String URI){
        try {
            JasperReport compiledReport = jrLoader(URI);
            Connection connection = DBConnection.getInstance().getConnection();
            JasperPrint jasperPrint = JasperFillManager.fillReport(compiledReport, null, connection);
            JasperViewer.viewReport(jasperPrint, false);
        } catch (JRException | SQLException | ClassNotFoundException e) {
            new Alert(Alert.AlertType.ERROR,e.getMessage());
        }
    }

    // using array as a collection/array
    public void reportGeneratorWithCollections(String URI, Object collection){
        try {
            JasperReport compiledReport = jrLoader(URI);
            JasperPrint jasperPrint;
            if(collection.getClass().isArray()){
               jasperPrint  = JasperFillManager.fillReport(compiledReport, null,  new JRBeanArrayDataSource((Object[]) collection));
            }else {
                jasperPrint  = JasperFillManager.fillReport(compiledReport, null,  new JRBeanCollectionDataSource((Collection) collection));
            }
            JasperViewer.viewReport(jasperPrint, false);
        } catch (JRException e) {
            new Alert(Alert.AlertType.ERROR,e.getMessage());
        }
    }

    // using parameters
    public void reportGeneratorWithParams(String URI, HashMap map){
        try {
            JasperReport compiledReport = (JasperReport) JRLoader.loadObject(this.getClass().getResourceAsStream(URI));
            JasperPrint jasperPrint;
            jasperPrint = JasperFillManager.fillReport(compiledReport, map, new JREmptyDataSource(1));
            JasperViewer.viewReport(jasperPrint, false);
        } catch (JRException e) {
            new Alert(Alert.AlertType.ERROR,e.getMessage());
        }
    }
}
