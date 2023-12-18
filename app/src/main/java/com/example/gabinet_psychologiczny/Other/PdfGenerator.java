package com.example.gabinet_psychologiczny.Other;

import android.content.ContentResolver;
import android.content.Intent;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.content.res.Resources;
import android.net.Uri;
import android.os.Environment;

import androidx.core.content.ContextCompat;

import com.example.gabinet_psychologiczny.Model.Patient;
import com.example.gabinet_psychologiczny.Model.Service;
import com.example.gabinet_psychologiczny.Model.Visit;
import com.itextpdf.kernel.colors.ColorConstants;
import com.itextpdf.kernel.colors.DeviceRgb;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfReader;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.Style;
import com.itextpdf.layout.borders.Border;
import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.element.Text;
import com.itextpdf.layout.properties.HorizontalAlignment;
import com.itextpdf.layout.properties.TextAlignment;
import com.itextpdf.layout.properties.UnitValue;

import com.example.gabinet_psychologiczny.Database.Relations.VisitWithAnnotationsAndPatientAndService;
import com.example.gabinet_psychologiczny.R;
import com.itextpdf.layout.properties.VerticalAlignment;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.time.Duration;
import java.time.LocalDate;
import java.util.Comparator;
import java.util.List;

public class PdfGenerator {

    private static int width = 792;
    private static int height = 1120;

    private static final DeviceRgb COLOR_GRAY = new DeviceRgb(220, 220, 220);
    private static final String DEST = "./target/sandbox/tables/simple_table9.pdf";

    public static String generateBill(Visit visit, Service service, Patient patient, String paymentMethod){

        File directory = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);

        String date = CalendarUtils.formattedDateForBill(LocalDate.now());
        String billNumber = "R-"+date+"-"+visit.getId();


        int serviceLength = (int)Duration.between(visit.getStartTime(), visit.getEndTime()).toMinutes();
        double price = Math.round(((serviceLength/60.0) * service.getPricePerHour()) * 100.0) / 100.0;


        String fileName = billNumber+".pdf";
        File file = new File(directory, fileName);
        String path = file.getPath();

        if(file.exists()){
            file.delete();
        }

        PdfDocument pdfDoc = null;
        Document doc = null;

        try {
            pdfDoc = new PdfDocument(new PdfWriter(path));
            doc = new Document(pdfDoc);

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }

        Style billingNumberStyle = new Style().setFontSize(30).setBold();
        Style dateStyle = new Style().setFontSize(16);
        Style dateValuesStyle = new Style().setFontSize(16).setBold();
        Style personHeaderStyle = new Style().setFontSize(16).setBold();
        Style personValuesStyle = new Style().setFontSize(16);
        Style tableHeaderStyle = new Style().setFontSize(16).setBold();
        Style tableValueStyle = new Style().setFontSize(16);
        Cell emptyCell = new Cell().setBorder(Border.NO_BORDER);
        Paragraph emptyParagraph = new Paragraph(new Text(""));


        //Billing number
        doc.add(new Paragraph(new Text("Rachunek " + billNumber)).addStyle(billingNumberStyle).setTextAlignment(TextAlignment.CENTER));

        doc.add(emptyParagraph);
        doc.add(emptyParagraph);

        //City and date
        Table cityAndDateTable = new Table(UnitValue.createPercentArray(1)).setFixedLayout().setWidth(UnitValue.createPercentValue(40));

        cityAndDateTable.addCell(new Cell().add(new Paragraph(new Text("Miejsce wystawienia")).addStyle(dateStyle))
                .setBorder(Border.NO_BORDER).setBackgroundColor(COLOR_GRAY).setTextAlignment(TextAlignment.CENTER));

        cityAndDateTable.addCell(new Cell().add(new Paragraph(new Text("Katowice")).addStyle(dateValuesStyle))
                .setBorder(Border.NO_BORDER).setTextAlignment(TextAlignment.CENTER));

        cityAndDateTable.addCell(new Cell().add(new Paragraph(new Text("Data wystawienia")).addStyle(dateStyle))
                .setBorder(Border.NO_BORDER).setBackgroundColor(COLOR_GRAY).setTextAlignment(TextAlignment.CENTER));

        cityAndDateTable.addCell(new Cell().add(new Paragraph(new Text(date)).addStyle(dateValuesStyle))
                .setBorder(Border.NO_BORDER).setTextAlignment(TextAlignment.CENTER));

        cityAndDateTable.setHorizontalAlignment(HorizontalAlignment.RIGHT);
        doc.add(cityAndDateTable);


        doc.add(emptyParagraph);
        doc.add(emptyParagraph);
        doc.add(emptyParagraph);

        //Person info
        Table personInfoTable = new Table(UnitValue.createPercentArray(new float[] {45, 10, 45})).setFixedLayout()
                .setWidth(UnitValue.createPercentValue(100));

        personInfoTable.addCell(new Cell().add(new Paragraph(new Text("Sprzedawca")).addStyle(personHeaderStyle))
                .setBorder(Border.NO_BORDER).setBackgroundColor(COLOR_GRAY).setTextAlignment(TextAlignment.CENTER));

        personInfoTable.addCell(emptyCell);

        personInfoTable.addCell(new Cell().add(new Paragraph(new Text("Nabywca")).addStyle(personHeaderStyle))
                .setBorder(Border.NO_BORDER).setBackgroundColor(COLOR_GRAY).setTextAlignment(TextAlignment.CENTER));

        personInfoTable.addCell(new Cell(2, 1).add(new Paragraph(new Text("Gabinet psychologiczny")).addStyle(personValuesStyle))
                .setBorder(Border.NO_BORDER).setTextAlignment(TextAlignment.LEFT));

        personInfoTable.addCell(emptyCell);

        personInfoTable.addCell(new Cell(2, 1).add(new Paragraph(new Text(patient.getFirstName() +
                        " " + patient.getLastName() + "\n" + "tel. " + patient.getPhoneNumber())).addStyle(personValuesStyle))
                .setBorder(Border.NO_BORDER).setTextAlignment(TextAlignment.LEFT));


        personInfoTable.setHorizontalAlignment(HorizontalAlignment.CENTER);
        doc.add(personInfoTable);


        doc.add(emptyParagraph);
        doc.add(emptyParagraph);
        doc.add(emptyParagraph);
        doc.add(emptyParagraph);


        //Service info
        Table serviceInfoTable = new Table(UnitValue.createPercentArray(4)).setFixedLayout().setWidth(UnitValue.createPercentValue(100));

        serviceInfoTable.addCell(new Cell().add(new Paragraph(new Text("Lp.")).addStyle(tableHeaderStyle))
                .setBackgroundColor(COLOR_GRAY).setTextAlignment(TextAlignment.CENTER).setVerticalAlignment(VerticalAlignment.MIDDLE));

        serviceInfoTable.addCell(new Cell().add(new Paragraph(new Text("Nazwa uslugi")).addStyle(tableHeaderStyle))
                .setBackgroundColor(COLOR_GRAY).setTextAlignment(TextAlignment.CENTER).setVerticalAlignment(VerticalAlignment.MIDDLE));

        serviceInfoTable.addCell(new Cell().add(new Paragraph(new Text("Czas trwania uslugi")).addStyle(tableHeaderStyle))
                .setBackgroundColor(COLOR_GRAY).setTextAlignment(TextAlignment.CENTER).setVerticalAlignment(VerticalAlignment.MIDDLE));

        serviceInfoTable.addCell(new Cell().add(new Paragraph(new Text("Cena")).addStyle(tableHeaderStyle))
                .setBackgroundColor(COLOR_GRAY).setTextAlignment(TextAlignment.CENTER).setVerticalAlignment(VerticalAlignment.MIDDLE));



        serviceInfoTable.addCell(new Cell().add(new Paragraph(new Text("1")).addStyle(tableValueStyle))
                .setTextAlignment(TextAlignment.CENTER));

        serviceInfoTable.addCell(new Cell().add(new Paragraph(new Text(service.getName())).addStyle(tableValueStyle))
                .setTextAlignment(TextAlignment.CENTER));

        serviceInfoTable.addCell(new Cell().add(new Paragraph(new Text(serviceLength + " min")).addStyle(tableValueStyle))
                .setTextAlignment(TextAlignment.CENTER));

        serviceInfoTable.addCell(new Cell().add(new Paragraph(new Text(price + " zl")).addStyle(tableValueStyle))
                .setTextAlignment(TextAlignment.CENTER));

        serviceInfoTable.setHorizontalAlignment(HorizontalAlignment.CENTER);
        doc.add(serviceInfoTable);


        doc.add(emptyParagraph);
        doc.add(emptyParagraph);
        doc.add(emptyParagraph);
        doc.add(emptyParagraph);

        //Summary
        Table summaryTable = new Table(UnitValue.createPercentArray(new float[] {45, 10, 45})).setFixedLayout()
                .setWidth(UnitValue.createPercentValue(100));

        summaryTable.addCell(new Cell().add(new Paragraph(new Text("Sposob platnosci: " + paymentMethod)).addStyle(personValuesStyle))
                .setBorder(Border.NO_BORDER).setTextAlignment(TextAlignment.LEFT));

        summaryTable.addCell(emptyCell);

        summaryTable.addCell(new Cell().add(new Paragraph(new Text("Do zaplaty: " + price + " PLN")).addStyle(personHeaderStyle))
                .setBorder(Border.NO_BORDER).setTextAlignment(TextAlignment.LEFT));


        summaryTable.setHorizontalAlignment(HorizontalAlignment.CENTER);
        doc.add(summaryTable);


        doc.close();

        return path;
    }

    public static String generateVisitList(List<VisitWithAnnotationsAndPatientAndService> list, String dateHeader) {
        File directory = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);
        String fileName = dateHeader+".pdf";
        File file = new File(directory, fileName);
        String path = file.getPath();

        list.sort(new Comparator<VisitWithAnnotationsAndPatientAndService>() {
            @Override
            public int compare(VisitWithAnnotationsAndPatientAndService v1, VisitWithAnnotationsAndPatientAndService v2) {
                return v1.visit.getStartTime().compareTo(v2.visit.getStartTime());
            }
        });

        if(file.exists()){
            file.delete();
        }

        PdfDocument pdfDoc = null;
        Document doc = null;

        try {
            pdfDoc = new PdfDocument(new PdfWriter(path));
            doc = new Document(pdfDoc);

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }

        Style tableHeaderStyle = new Style().setFontSize(16).setBold();
        Style tableValueStyle = new Style().setFontSize(16);

        doc.add(new Paragraph(dateHeader).addStyle(new Style().setFontSize(30).setBold()).setTextAlignment(TextAlignment.CENTER));


        Table listTable = new Table(UnitValue.createPercentArray(3)).setFixedLayout().setWidth(UnitValue.createPercentValue(100));

        listTable.addCell(new Cell().add(new Paragraph(new Text("Godzina")).addStyle(tableHeaderStyle))
                .setBackgroundColor(COLOR_GRAY).setTextAlignment(TextAlignment.CENTER).setVerticalAlignment(VerticalAlignment.MIDDLE));

        listTable.addCell(new Cell().add(new Paragraph(new Text("Nazwa uslugi")).addStyle(tableHeaderStyle))
                .setBackgroundColor(COLOR_GRAY).setTextAlignment(TextAlignment.CENTER).setVerticalAlignment(VerticalAlignment.MIDDLE));

        listTable.addCell(new Cell().add(new Paragraph(new Text("Pacjent")).addStyle(tableHeaderStyle))
                .setBackgroundColor(COLOR_GRAY).setTextAlignment(TextAlignment.CENTER).setVerticalAlignment(VerticalAlignment.MIDDLE));



        for(VisitWithAnnotationsAndPatientAndService visit : list){
            String time = CalendarUtils.formattedTime(visit.visit.getStartTime()) + " - " + CalendarUtils.formattedTime(visit.visit.getEndTime());

            listTable.addCell(new Cell().add(new Paragraph(new Text(time)).addStyle(tableValueStyle))
                    .setTextAlignment(TextAlignment.CENTER));

            listTable.addCell(new Cell().add(new Paragraph(new Text(visit.service.getName())).addStyle(tableValueStyle))
                    .setTextAlignment(TextAlignment.CENTER));

            listTable.addCell(new Cell().add(new Paragraph(new Text(visit.patient.getFirstName() + " " + visit.patient.getLastName())).addStyle(tableValueStyle))
                    .setTextAlignment(TextAlignment.CENTER));
        }

        listTable.setHorizontalAlignment(HorizontalAlignment.CENTER);
        doc.add(listTable);
        doc.close();

        return path;
    }
}
