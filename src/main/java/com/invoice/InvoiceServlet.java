

package com.invoice;

import java.io.IOException;
import java.util.List;

import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Document;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.FontFactory;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import com.products.ProductDto;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@WebServlet("/InvoiceServlet")
public class InvoiceServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
   
    public InvoiceServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("application/pdf");
        response.setHeader("Content-Disposition", "attachment; filename=invoice.pdf");

        HttpSession session = request.getSession();
        
        List<ProductDto> productList = (List<ProductDto>) session.getAttribute("finalProductList");
        if (productList == null) {
            throw new ServletException("Error: Product list is missing in session.");
        }

        String customerName = (String) session.getAttribute("cname");
        String customerPhone = (String) session.getAttribute("cphone");
        String customerAddress = (String) session.getAttribute("caddress");
        String customerEmail = (String) session.getAttribute("cmail");
        String invoiceDate = (String) session.getAttribute("cdate");
        
        Object totalAmtObj = session.getAttribute("totalamt");
        double totalAmount = (totalAmtObj != null) ? Double.parseDouble(totalAmtObj.toString()) : 0.0;

        if (invoiceDate == null || customerPhone == null) {
            throw new ServletException("Error: Required customer details missing in session.");
        }

        int invoiceId = Integer.parseInt(session.getAttribute("Invoice_Id").toString());

        try {
            Document document = new Document();
            PdfWriter.getInstance(document, response.getOutputStream());
            document.open();

            // Invoice title
            PdfPTable headerTable = new PdfPTable(2);
            headerTable.setWidthPercentage(100);
            headerTable.setWidths(new float[]{2, 1});

            Font titleFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 20, BaseColor.BLACK);
            Paragraph title = new Paragraph("INVOICE", titleFont);
            title.setAlignment(Element.ALIGN_CENTER);

            PdfPCell titleCell = new PdfPCell(title);
            titleCell.setBorder(Rectangle.NO_BORDER);
            titleCell.setHorizontalAlignment(Element.ALIGN_CENTER);
            headerTable.addCell(titleCell);

            Font infoFont = FontFactory.getFont(FontFactory.HELVETICA, 12);
            Paragraph invoiceDetails = new Paragraph("INVOICE ID: " + invoiceId + "\nDATE: " + invoiceDate, infoFont);
            invoiceDetails.setAlignment(Element.ALIGN_RIGHT);

            PdfPCell invoiceCell = new PdfPCell(invoiceDetails);
            invoiceCell.setBorder(Rectangle.NO_BORDER);
            invoiceCell.setHorizontalAlignment(Element.ALIGN_RIGHT);
            headerTable.addCell(invoiceCell);

            document.add(headerTable);
            document.add(new Paragraph("\n"));

            // Customer details
            document.add(new Paragraph("Bill To:", FontFactory.getFont(FontFactory.HELVETICA_BOLD)));
            document.add(new Paragraph("Name: " + customerName));
            document.add(new Paragraph("Address: " + customerAddress));
            document.add(new Paragraph("Phone: " + customerPhone));
            document.add(new Paragraph("Email: " + customerEmail));
            document.add(new Paragraph("Date: " + invoiceDate));
            document.add(new Paragraph("\n"));

            // Product table
            PdfPTable table = new PdfPTable(4);
            table.setWidthPercentage(100);
            table.addCell(new Paragraph("Product ID", FontFactory.getFont(FontFactory.HELVETICA_BOLD)));
            table.addCell(new Paragraph("Product Name", FontFactory.getFont(FontFactory.HELVETICA_BOLD)));
            table.addCell(new Paragraph("Quantity", FontFactory.getFont(FontFactory.HELVETICA_BOLD)));
            table.addCell(new Paragraph("Total Price", FontFactory.getFont(FontFactory.HELVETICA_BOLD)));

            for (ProductDto product : productList) {
                table.addCell(String.valueOf(product.getProductId()));
                table.addCell(product.getProductName());
                table.addCell(String.valueOf(product.getProductQuantity()));
                table.addCell(String.valueOf(product.getProductQuantity() * product.getProductPrice()));
            }

            // GST calculations
            double gst = (totalAmount * 18) / 100;
            double cgst = gst / 2;
            double sgst = gst / 2;
            double sellingPrice = totalAmount - gst;

            PdfPCell emptyCell = new PdfPCell(new Phrase(""));
            emptyCell.setColspan(2);
            emptyCell.setHorizontalAlignment(Element.ALIGN_CENTER);
            table.addCell(emptyCell);

            table.addCell("Total:");
            table.addCell("" + totalAmount);

            PdfPCell taxCell = new PdfPCell(new Phrase("CGST (9%): " + cgst + "\nSGST (9%): " + sgst + "\nSelling Price: " + sellingPrice + "\nGrand Total: " + totalAmount));
            taxCell.setColspan(3);
            taxCell.setHorizontalAlignment(Element.ALIGN_CENTER);
            table.addCell(taxCell);

            document.add(table);
            document.add(new Paragraph("\nFor: Satyanandh Enterprises", FontFactory.getFont(FontFactory.HELVETICA_BOLD)));
            document.add(new Paragraph("Satya", FontFactory.getFont(FontFactory.HELVETICA_BOLD)));
            document.add(new Paragraph("Authorized Signatory"));

            document.close();
        } catch (Exception e) {
            throw new ServletException("Error generating PDF: " + e.getMessage());
        }

      
    }

}
