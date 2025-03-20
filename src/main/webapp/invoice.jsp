<!--  This code is not linked to our project right now       --> 



<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ page import="com.products.ProductDto" %>
<%@ page import="java.util.*" %>
<!DOCTYPE html>

<html>

<head>

<meta charset="UTF-8">

<meta name="viewport" content="width=device-width, initial-scale=1">

<title>Tax Invoice</title>

<link
	href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css"
	rel="stylesheet">

<style>
body {
	font-family: Arial, sans-serif;
}

.invoice-container {
	border: 1px solid #ddd;
	padding: 20px;
	background: #fff;
	max-width: 800px;
	margin: auto;
}

.header {
	background: #0d2a58;
	color: white;
	padding: 20px;
	margin: 0px;
	text-align: center;
	font-size: 18px;
}

.business-name {
	font-size: 20px;
	font-weight: bold;
	text-align: center;
}

.border-box {
	border: 1px solid #ddd;
	padding: 10px;
}

table {
	width: 100%;
	border-collapse: collapse;
}

th, td {
	border: 1px solid #ddd;
	padding: 8px;
	text-align: center;
}

.footer {
	margin-top: 20px;
	font-size: 14px;
}
</style>

<script
	src="https://cdnjs.cloudflare.com/ajax/libs/jspdf/2.5.1/jspdf.umd.min.js"></script>

<script
	src="https://cdnjs.cloudflare.com/ajax/libs/html2canvas/1.4.1/html2canvas.min.js"></script>

</head>

<body>
     <%
     List<ProductDto>  listOfProducts=null;
     if(session.getAttribute("finalProductList") != null){
     listOfProducts= (List<ProductDto>)session.getAttribute("finalProductList");} %>
	<div class="invoice-container" id="invoice"	style="border: 2px solid black">

		<div class="header d-flex justify-content-between align-items-center">

			<h2 class="text-center flex-grow-1 m-0">INVOICE</h2>
          
			<div class="text-end">

				<p class="m-0">

					<strong>INVOICE ID:<%=((String)session.getAttribute("cdate")).substring(0,4)%><%=(String)session.getAttribute("cphone")%></strong>

				</p>

				<p class="m-0">

					<strong>DATE:</strong> <%=(String)session.getAttribute("cdate") %>

				</p>

			</div>

		</div>

		<div class="business-name">Satyanandh Enterprises</div>

		<p class="text-center">

			Tech Mahindra, Vskp, AP, 530017<br>GSTIN: AAA213465 | Email:

			satyan@gmail.com | PAN: AAA132456

		</p>



		<div  class="row" style="border: 2px solid black;" >
	   <strong>Bill To:</strong><br> <%=(String)session.getAttribute("cname") %> <br>Address:<%=(String)session.getAttribute("caddress") %><br> Phone:<%=(String)session.getAttribute("cphone") %> <br>Email: <%=(String)session.getAttribute("cmail") %><br>
		</div>



		<table class="mt-3" style="border: 2px solid black">

			<thead>

				<tr style="border: 2px solid black">

					<th style="border-right: 2px solid black">PRODUCT ID</th>

					<th style="border-right: 2px solid black">PRODUCT NAME</th>

					<th style="border-right: 2px solid black">QTY</th>

					<th>AMOUNT</th>

				</tr>

			</thead>
          
			<tbody>
                <%for(ProductDto product:listOfProducts){ %>
				<tr style="border: 2px solid black">
    
					<td style="border-right: 2px solid black"><%=product.getProductId() %></td>

					<td style="border-right: 2px solid black"><%=product.getProductName() %></td>

					<td style="border-right: 2px solid black"><%=product.getProductQuantity() %></td>

					<td style="border-right: 2px solid black"><%=product.getProductQuantity()*product.getProductPrice() %></td>
                     
				</tr>
<%} %>       <%double tmt=(double)session.getAttribute("totalamt"); %>
				<tr style="border: 2px solid black">

					<td style="border-right: 2px solid black"></td>

					<td style="border-right: 2px solid black"></td>

					<td style="border-right: 2px solid black">Total :</td>

					<td style="border-right: 2px solid black"><%=tmt%></td>

				</tr>
                <% double gst,cgst,sgst; 
                gst=(tmt*18)/100;
                cgst=gst/2;
                sgst=gst/2;
                
                %>
                
				<tr style="border: 2px solid black">

					<td style="border-right: 2px solid black"></td>

					<td style="border-right: 2px solid black"></td>

					<td style="border-right: 2px solid black"></td>

					<td style="border-right: 2px solid black">
					CGST @ 9% :<Strong><%=cgst %></Strong> <br>
					SGST @ 9% :<Strong><%=sgst %></Strong> <br>
					Selling Price @ :<Strong><%=tmt-gst%></Strong><br>
      				<strong>Grand Total: <%=tmt %></strong>
      				</td>

				</tr>

			</tbody>

		</table>




		<p>

			<strong>For: Satyanandh Enterprises</strong>

		</p>

		<p class="footer">
           <p>Satya	</p>
			<em>Authorised Signatory</em>

		</p>

	</div>

	<div class="text-center mt-3">
        <% session.invalidate(); %>
		
	</div>



	<script>


	  window.onload = function () {
	    const invoice = document.getElementById("invoice");
	    
	    html2canvas(invoice).then(canvas => {
	      const imgData = canvas.toDataURL("image/png");
	      const { jsPDF } = window.jspdf;
	      const pdf = new jsPDF();

	      const imgProps = pdf.getImageProperties(imgData);
	      const pdfWidth = pdf.internal.pageSize.getWidth();
	      const pdfHeight = (imgProps.height * pdfWidth) / imgProps.width;

	      pdf.addImage(imgData, 'PNG', 0, 0, pdfWidth, pdfHeight);
	      pdf.save("Invoice.pdf");

	      // Optional: Close the window after download (if opened in new tab)
	      setTimeout(() => {
	        window.close();
	      }, 1000);
	    });
	  };
	</script>



       
  
</body>

</html>
