package ba.unsa.etf.rpr;

import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.export.ooxml.JRDocxExporter;
import net.sf.jasperreports.export.*;
import net.sf.jasperreports.swing.JRViewer;

import javax.swing.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.HashMap;

public class GradoviReport extends JFrame {


        public void showReport(Connection conn) throws  net.sf.jasperreports.engine.JRException {
            String reportSrcFile = getClass().getResource("/reports/gradovi.jrxml").getFile();
            String reportsDir = getClass().getResource("/reports/").getFile();

            JasperReport jasperReport = JasperCompileManager.compileReport(reportSrcFile);
            // Fields for resources path
            HashMap<String, Object> parameters = new HashMap<String, Object>();
            parameters.put("reportsDirPath", reportsDir);
            ArrayList<HashMap<String, Object>> list = new ArrayList<HashMap<String, Object>>();
            list.add(parameters);
            JasperPrint print = JasperFillManager.fillReport(jasperReport, parameters, conn);
            JRViewer viewer = new JRViewer(print);
            viewer.setOpaque(true);
            viewer.setVisible(true);
            this.add(viewer);
            this.setSize(700, 700);
            this.setVisible(true);
        }

    public void showReportGradovi(Connection conn,String id) throws  net.sf.jasperreports.engine.JRException {
        String reportSrcFile = getClass().getResource("/reports/gradoviDrzava.jrxml").getFile();
        String reportsDir = getClass().getResource("/reports/").getFile();

        JasperReport jasperReport = JasperCompileManager.compileReport(reportSrcFile);
        // Fields for resources path
        HashMap<String, Object> parameters = new HashMap<String, Object>();
        parameters.put("reportsDirPath", reportsDir);
        parameters.put("id",id);
        ArrayList<HashMap<String, Object>> list = new ArrayList<HashMap<String, Object>>();
        list.add(parameters);
        JasperPrint print = JasperFillManager.fillReport(jasperReport, parameters, conn);
        JRViewer viewer = new JRViewer(print);
        viewer.setOpaque(true);
        viewer.setVisible(true);
        this.add(viewer);
        this.setSize(700, 700);
        this.setVisible(true);
    }

        public void saveAs(Connection conn, String format) throws JRException {
            String reportSrcFile = getClass().getResource("/reports/gradovi.jrxml").getFile();
            String reportsDir = getClass().getResource("/reports/").getFile();

            JasperReport jasperReport = JasperCompileManager.compileReport(reportSrcFile);
            // Fields for resources path
            HashMap<String, Object> parameters = new HashMap<String, Object>();
            parameters.put("reportsDirPath", reportsDir);
            ArrayList<HashMap<String, Object>> list = new ArrayList<HashMap<String, Object>>();
            list.add(parameters);
            JasperPrint print = JasperFillManager.fillReport(jasperReport, parameters, conn);
            File file=new File(format);
            try {
                FileOutputStream izlaz=new FileOutputStream(file);
                if(format.contains(".pdf")){
                    JasperExportManager.exportReportToPdfStream(print, izlaz);
                }
                else if(format.contains(".docx")) {
                   // JasperPrint jasperPrint = JasperFillManager.fillReport("myReport.jasper", reportParameters, dataSource);

                    Exporter exporter = new JRDocxExporter();
                    exporter.setExporterInput(new SimpleExporterInput(print));


                    SimpleDocxExporterConfiguration konfiguracija=new SimpleDocxExporterConfiguration();
                    exporter.setConfiguration(konfiguracija);
                    exporter.setExporterOutput(new SimpleOutputStreamExporterOutput(file));

                    exporter.exportReport();

                }

                else if(format.contains(".xlsx")){

                    Exporter exporter = new JRDocxExporter();
                    exporter.setExporterInput(new SimpleExporterInput(print));


                    SimpleXlsExporterConfiguration konfiguracija=new SimpleXlsExporterConfiguration();
                    exporter.setConfiguration(konfiguracija);
                    exporter.setExporterOutput(new SimpleOutputStreamExporterOutput(file));

                    exporter.exportReport();



                }

            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }


        }
    }


