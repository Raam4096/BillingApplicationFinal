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
        String customerName = (String) session.getAttribute("cname");
        String customerPhone = (String) session.getAttribute("cphone");
        String customerAddress = (String) session.getAttribute("caddress");
        String customerEmail = (String) session.getAttribute("cmail");
        String invoiceDate = (String) session.getAttribute("cdate");
        double totalAmount = (double) session.getAttribute("totalamt");

        try {
            Document document = new Document();
            PdfWriter.getInstance(document, response.getOutputStream());
            document.open();

            // Add invoice title
            PdfPTable headerTable = new PdfPTable(2); // Two columns
            headerTable.setWidthPercentage(100); // Full width
            headerTable.setWidths(new float[]{2, 1}); // Adjust width ratio

            // Invoice Title (Centered)
            Font titleFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 20, BaseColor.BLACK);
            Paragraph title = new Paragraph("INVOICE", titleFont);
            title.setAlignment(Element.ALIGN_CENTER);

            PdfPCell titleCell = new PdfPCell(title);
            titleCell.setBorder(Rectangle.NO_BORDER);
            titleCell.setHorizontalAlignment(Element.ALIGN_CENTER);
            headerTable.addCell(titleCell);

            // Invoice ID and Date (Right Aligned)
            String invoiceId = "INVOICE ID:"+((String) session.getAttribute("cdate")).substring(0,4)+(String)session.getAttribute("cphone");
            String invoiceDate1 = "DATE: "+(String)session.getAttribute("cdate");

            Font infoFont = FontFactory.getFont(FontFactory.HELVETICA, 12);
            Paragraph invoiceDetails = new Paragraph(invoiceId + "\n" + invoiceDate1, infoFont);
            invoiceDetails.setAlignment(Element.ALIGN_RIGHT);

            PdfPCell invoiceCell = new PdfPCell(invoiceDetails);
            invoiceCell.setBorder(Rectangle.NO_BORDER);
            invoiceCell.setHorizontalAlignment(Element.ALIGN_RIGHT);
            headerTable.addCell(invoiceCell);

            document.add(headerTable);
            document.add(new Paragraph("\n")); // Add spacing after header


            document.add(new Paragraph("\n"));

            // Customer details
            Paragraph billTo = new Paragraph("Bill To:",FontFactory.getFont(FontFactory.HELVETICA_BOLD));
            document.add(billTo);
            document.add(new Paragraph("Name: " + customerName));
            document.add(new Paragraph("Address: " + customerAddress));
            document.add(new Paragraph("Phone: " + customerPhone));
            document.add(new Paragraph("Email: " + customerEmail));
            document.add(new Paragraph("Date: " + invoiceDate));

            document.add(new Paragraph("\n"));

            // Product table
            PdfPTable table = new PdfPTable(4);
            table.setWidthPercentage(100);
            table.addCell(new Paragraph("Product ID",FontFactory.getFont(FontFactory.HELVETICA_BOLD)));
            table.addCell(new Paragraph("Product Name",FontFactory.getFont(FontFactory.HELVETICA_BOLD)));            
            table.addCell(new Paragraph("Quantity",FontFactory.getFont(FontFactory.HELVETICA_BOLD)));        
            table.addCell(new Paragraph("Total Price",FontFactory.getFont(FontFactory.HELVETICA_BOLD)));

            for (ProductDto product : productList) {
                table.addCell(String.valueOf(product.getProductId()));
                table.addCell(product.getProductName());
                table.addCell(String.valueOf(product.getProductQuantity()));
                table.addCell(String.valueOf(product.getProductQuantity() * product.getProductPrice()));
            }
      
            PdfPCell cell = new PdfPCell(new Phrase(""));
            cell.setColspan(2); // Spanning across 2 columns
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            table.addCell(cell);
            table.addCell("Total :");
            table.addCell(""+totalAmount);
            // Total calculations
            double gst = (totalAmount * 18) / 100;
            double cgst = gst / 2;
            double sgst = gst / 2;
            double sellingPrice = totalAmount - gst;
            PdfPCell cell1 = new PdfPCell(new Phrase(""));
            cell1.setColspan(3); // Spanning across 3 columns
            cell1.setHorizontalAlignment(Element.ALIGN_CENTER);
            table.addCell(cell1);
            table.addCell(new Phrase("CGST (9%): " + cgst +"\n"+ "SGST (9%): " + sgst +"\n"+ "Selling Price: " + sellingPrice +"\n" +"Grand Total: " + totalAmount ));

            document.add(table);
            document.add(new Paragraph("For: Satyanandh Enterprises",FontFactory.getFont(FontFactory.HELVETICA_BOLD)));
            document.add(new Paragraph("Satya",FontFactory.getFont(FontFactory.HELVETICA_BOLD)));
            document.add(new Paragraph("Authorized Signatory"));

            document.close();
        } catch (Exception e) {
            throw new IOException(e.getMessage());
        }
        session.invalidate();
	}

}
