package com.invoice;

import java.io.IOException;
import java.util.List;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.*;
import com.itextpdf.text.pdf.draw.LineSeparator;
import com.products.ProductDto;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

@WebServlet("/InvoiceServlet")
public class InvoiceServlet extends HttpServlet {
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("application/pdf");
        response.setHeader("Content-Disposition", "attachment; filename=Invoice.pdf");

        HttpSession session = request.getSession();
        List<ProductDto> listOfProducts = (List<ProductDto>) session.getAttribute("finalProductList");

        String cname = (String) session.getAttribute("cname");
        String cphone = (String) session.getAttribute("cphone");
        String cmail = (String) session.getAttribute("cmail");
        String cdate = (String) session.getAttribute("cdate");
        String caddress = (String) session.getAttribute("caddress");
        double totalAmount = (double) session.getAttribute("totalamt");

        double gst = totalAmount * 0.18;
        double cgst = gst / 2;
        double sgst = gst / 2;
        double sellingPrice = totalAmount - gst;

        try {
            Document doc = new Document(PageSize.A4, 50, 50, 50, 50);
            PdfWriter.getInstance(doc, response.getOutputStream());
            doc.open();

            // Fonts
            Font titleFont = new Font(Font.FontFamily.HELVETICA, 22, Font.BOLD, BaseColor.DARK_GRAY);
            Font subTitleFont = new Font(Font.FontFamily.HELVETICA, 12, Font.BOLD);
            Font normalFont = new Font(Font.FontFamily.HELVETICA, 11);
            Font smallFont = new Font(Font.FontFamily.HELVETICA, 10, Font.ITALIC, BaseColor.GRAY);

            // Title
            Paragraph title = new Paragraph("INVOICE", titleFont);
            title.setAlignment(Element.ALIGN_CENTER);
            doc.add(title);
            doc.add(new LineSeparator());
            doc.add(new Paragraph("\n"));

            // Invoice Info
            Paragraph invoiceInfo = new Paragraph("Invoice ID: " + cdate.substring(0, 4) + cphone + "\nDate: " + cdate, normalFont);
            doc.add(invoiceInfo);
            doc.add(new Paragraph("\n"));

            // Business Info
            Paragraph businessInfo = new Paragraph(
                "Satyanandh Enterprises\nTech Mahindra, Vskp, AP - 530017\nGSTIN: AAA213465 | PAN: AAA132456\nEmail: satyan@gmail.com", 
                normalFont
            );
            doc.add(businessInfo);
            doc.add(new Paragraph("\n"));

            // Customer Info
            Paragraph customerInfo = new Paragraph("Bill To:", subTitleFont);
            customerInfo.add(new Paragraph(cname, normalFont));
            customerInfo.add(new Paragraph("Phone: " + cphone, normalFont));
            customerInfo.add(new Paragraph("Email: " + cmail, normalFont));
            customerInfo.add(new Paragraph("Address: " + caddress, normalFont));
            doc.add(customerInfo);
            doc.add(new Paragraph("\n"));

            // Table
            PdfPTable table = new PdfPTable(4);
            table.setWidthPercentage(100);
            table.setSpacingBefore(10f);
            table.setSpacingAfter(10f);
            table.setWidths(new float[]{2f, 4f, 2f, 3f});

            BaseColor headerBg = new BaseColor(230, 230, 250); // Light purple

            // Table Headers
            String[] headers = {"Product ID", "Product Name", "Qty", "Amount"};
            for (String h : headers) {
                PdfPCell headerCell = new PdfPCell(new Phrase(h, subTitleFont));
                headerCell.setHorizontalAlignment(Element.ALIGN_CENTER);
                headerCell.setBackgroundColor(headerBg);
                headerCell.setPadding(5);
                table.addCell(headerCell);
            }

            // Table Rows
            for (ProductDto product : listOfProducts) {
                table.addCell(new PdfPCell(new Phrase(String.valueOf(product.getProductId()), normalFont)));
                table.addCell(new PdfPCell(new Phrase(product.getProductName(), normalFont)));
                table.addCell(new PdfPCell(new Phrase(String.valueOf(product.getProductQuantity()), normalFont)));
                double amt = product.getProductQuantity() * product.getProductPrice();
                PdfPCell amtCell = new PdfPCell(new Phrase("₹" + amt, normalFont));
                amtCell.setHorizontalAlignment(Element.ALIGN_RIGHT);
                table.addCell(amtCell);
            }

            doc.add(table);

            // Totals Section
            PdfPTable totalsTable = new PdfPTable(2);
            totalsTable.setWidths(new float[]{8, 2});
            totalsTable.setWidthPercentage(60);
            totalsTable.setHorizontalAlignment(Element.ALIGN_RIGHT);

            addTotalRow(totalsTable, "Subtotal", sellingPrice, normalFont);
            addTotalRow(totalsTable, "CGST @9%", cgst, normalFont);
            addTotalRow(totalsTable, "SGST @9%", sgst, normalFont);
            addTotalRow(totalsTable, "Grand Total", totalAmount, subTitleFont);

            doc.add(totalsTable);

            // Footer
            doc.add(new Paragraph("\n\n"));
            Paragraph footer = new Paragraph("For: Satyanandh Enterprises\nSatya\n(Authorised Signatory)", smallFont);
            footer.setAlignment(Element.ALIGN_RIGHT);
            doc.add(footer);
            doc.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        
    }

    private void addTotalRow(PdfPTable table, String label, double amount, Font font) {
        PdfPCell labelCell = new PdfPCell(new Phrase(label, font));
        labelCell.setBorder(Rectangle.NO_BORDER);
        PdfPCell valueCell = new PdfPCell(new Phrase("₹" + String.format("%.2f", amount), font));
        valueCell.setBorder(Rectangle.NO_BORDER);
        valueCell.setHorizontalAlignment(Element.ALIGN_RIGHT);
        table.addCell(labelCell);
        table.addCell(valueCell);
    }
}
