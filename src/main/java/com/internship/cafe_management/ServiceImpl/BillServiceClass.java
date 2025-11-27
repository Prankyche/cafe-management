package com.internship.cafe_management.ServiceImpl;

import com.internship.cafe_management.Constants.CafeConstants;
import com.internship.cafe_management.Entity.Bill;
import com.internship.cafe_management.JWT.JwtFilter;
import com.internship.cafe_management.Repository.BillRepository;
import com.internship.cafe_management.Service.BillService;
import com.internship.cafe_management.Util.CafeUtils;
import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import org.json.JSONArray;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.parameters.P;
import org.springframework.stereotype.Service;

import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

import static com.itextpdf.text.FontFactory.getFont;

@Service
public class BillServiceClass implements BillService {

    @Autowired
    BillRepository billRepository;

    @Autowired
    JwtFilter jwtFilter;

    @Override
    public ResponseEntity<String> generateReport(Map<String, Object> requestmap) {
        try {
            String fileName;
            if (validateRequestMap(requestmap)) {
                if (requestmap.containsKey("isGenerate") && !(Boolean) requestmap.get("isGenerate")) {
                    fileName = (String) requestmap.get("uuid");
                } else {
                    fileName = CafeUtils.getUUID();
                    requestmap.put("uuid", fileName);
                    insertBill(requestmap);
                }

                String data = "Name : " + requestmap.get("name") + "\n" + "Contact Number : " + requestmap.get("contact") + "\n" + "Email :" + requestmap.get("email") + "\n" + "Payment Method : " + requestmap.get("paymentMethod");
                Document document = new Document();
                PdfWriter.getInstance(document, new FileOutputStream(CafeConstants.STORE_LOCATION + "\\" + fileName + ".pdf"));
                document.open();
                setRectangleinPdf(document);
                Paragraph chunk = new Paragraph("Cafe Management System", getFont("Header"));
                chunk.setAlignment(Element.ALIGN_CENTER);
                document.add(chunk);
                Paragraph paragraph = new Paragraph(data + " \n \n ", getFont("Data"));
                document.add(paragraph);
                PdfPTable table = new PdfPTable(5);
                table.setWidthPercentage(80);
                addTableHeader(table);
                JSONArray jsonArray = CafeUtils.getJsonArrayFromString((String) requestmap.get("productDetails"));
                for (int i = 0; i< jsonArray.length();i++){
                    addTableRow(table,CafeUtils.getMapFromJSon(jsonArray.getString(i)));
                }
                document.add(table);
                Paragraph footer = new Paragraph("Total : "+requestmap.get("totalAmount")+"\n"+"Thank you for visiting.",getFont("data"));
                document.add(footer);
                document.close();
                return new ResponseEntity<>("{\"uuid\""+fileName+"}\"",HttpStatus.OK);

            } else {
                return CafeUtils.getResponseEntity("Required data not present", HttpStatus.BAD_REQUEST);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return CafeUtils.getResponseEntity(CafeConstants.SOMETHING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @Override
    public ResponseEntity<List<Bill>> getBills() {
        List<Bill> list = new ArrayList<>();
        if (jwtFilter.isAdmin()){
            list = billRepository.getAllBills();
        }else{
            list = billRepository.getBillByUserName(jwtFilter.getCurrentUser());
        }
        return new ResponseEntity<>(list,HttpStatus.INTERNAL_SERVER_ERROR);
    }

    private void addTableRow(PdfPTable table, Map<String, Object> data) {
        table.addCell((String) data.get("name"));
        table.addCell((String) data.get("category"));
        table.addCell((String) data.get("quantity"));
        table.addCell(Double.toString((Double) data.get("price")));
        table.addCell(Double.toString((Double) data.get("total")));
    }

    private void addTableHeader(PdfPTable table) {
        Stream.of("Name", "Category", "Quantity", "Price", "Subtotal")
                .forEach(columnTitle -> {
            PdfPCell header = new PdfPCell();
            header.setBackgroundColor(BaseColor.LIGHT_GRAY);
            header.setBorderWidth(2);
            header.setPhrase(new Phrase(columnTitle));
            header.setBackgroundColor(BaseColor.DARK_GRAY);
            header.setHorizontalAlignment(Element.ALIGN_CENTER);
            header.setVerticalAlignment(Element.ALIGN_CENTER);
            table.addCell(header);
        });
    }

    private void setRectangleinPdf(Document document) throws DocumentException {
        Rectangle rectangle = new Rectangle(577, 825, 15);
        rectangle.enableBorderSide(1);
        rectangle.enableBorderSide(2);
        rectangle.enableBorderSide(4);
        rectangle.enableBorderSide(8);
        rectangle.setBorderColor(BaseColor.BLACK);
        rectangle.setBorderWidth(1);
        document.add(rectangle);

    }

    private void insertBill(Map<String, Object> requestmap) {
        try {
            Bill bill = new Bill();
            bill.setUuid((String) requestmap.get("uuid"));
            bill.setName((String) requestmap.get("name"));
            bill.setEmail((String) requestmap.get("email"));
            bill.setContact((String) requestmap.get("contact"));
            bill.setPaymentMethod((String) requestmap.get("paymentMethod"));
            bill.setTotal((Integer) requestmap.get("total"));
            bill.setProductDetails((String) requestmap.get("productDetails"));
            bill.setCreatedBy(jwtFilter.getCurrentUser());
            billRepository.save(bill);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private boolean validateRequestMap(Map<String, Object> requestmap) {
        return requestmap.containsKey("name") && requestmap.containsKey("contact") && requestmap.containsKey("email") && requestmap.containsKey("paymentMethod") && requestmap.containsKey("productDetails") && requestmap.containsKey("totalAmount");
    }
}
