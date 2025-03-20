<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="java.util.*" %>
<%@ page import="com.products.ProductDto" %>
<%
  
	RequestDispatcher dispatcher=request.getRequestDispatcher("GetProductServlet");
	dispatcher.include(request,response);
%>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Invoice Generator</title>
    <link rel="stylesheet" type="text/css" href="style.css">
</head>
<body>

<div class="container">
    <div class="logo">
        <h2>Billing Application</h2>
    </div>

    <h2 class="heading">Invoice Generator</h2>

    <div class="section">
        <h3>Customer Details</h3>

        <!-- Separate Customer Form -->
        <form method="post" action="CustomerServlet">
            <div class="form-row">
                <input type="text" placeholder="Customer Name" name="customername" value="<%=(String)session.getAttribute("cname")!=null?(String)session.getAttribute("cname"):"" %>">
                <input type="text" placeholder="Phone Number" name="phonenumber" value="<%=(String)session.getAttribute("cphone")!=null?(String)session.getAttribute("cphone"):"" %>">
                <input type="email" placeholder="Email" name="email" value="<%=(String)session.getAttribute("cmail")!=null?(String)session.getAttribute("cmail"):"" %>">
            </div>
            <div class="form-row">
                <input type="text" placeholder="Address" name="address" value="<%=(String)session.getAttribute("caddress")!=null?(String)session.getAttribute("caddress"):"" %>">
                <input type="date" placeholder="Date" name="date" value="<%=(String)session.getAttribute("cdate")!=null?(String)session.getAttribute("cdate"):"" %>">
       
           <input type="submit" style="display:none;">
        </form>
    </div>

    <%
        List<ProductDto> products = null;
        if(session.getAttribute("productsData") != null){
            products = (List<ProductDto>) session.getAttribute("productsData");
        }

        String selectedProductIdStr = request.getParameter("selectedProductId");
        int selectedProductId = 0;
        double selectedProductPrice = 0.0;
        String pname="";
        if (selectedProductIdStr != null) {
            selectedProductId = Integer.parseInt(selectedProductIdStr);
            if (products != null) {
                for (ProductDto product : products) {
                    if (product.getProductId() == selectedProductId) {
                        selectedProductPrice = product.getProductPrice();
                        pname=product.getProductName();
                        break;
                    }
                }
            }
        }
    %>

    <div class="section">
        <h3>Product Details</h3>

        <!-- Separate Product Selection Form -->
        <form method="post" action="index.jsp">
            <div class="form-row">
           
                <select name="selectedProductId" onchange="this.form.submit()">
                    <option>Select Product</option>
                    <%
                        if (products != null && !products.isEmpty()) {
                            for (ProductDto product : products) {
                    %>
                        <option value="<%= product.getProductId() %>" <%= (product.getProductId() == selectedProductId) ? "selected" : "" %> >
                            <%= product.getProductName() %>
                             
                        </option>
                      
                    <%
                            }
                        }
                    %>
                </select>
            </div>
        </form>

        <!-- Product Quantity & Add Button Form -->
        <form action="AddProductServlet">
            <div class="form-row">
                <input type="hidden" name="product" value="<%=pname%>">
                <input type="text" placeholder="Item ID" name="productId" value="<%= selectedProductId != 0 ? selectedProductId : "" %>" readonly>
                <input type="number" placeholder="Quantity" name="quantity" required>
                <input type="text" placeholder="Price" name="price" value="<%= selectedProductPrice %>" readonly>
                <input type="submit" class="add-btn" value="Add Product">
            </div>
        </form>
    </div>

    <div class="section">
        <h2>Product List</h2>
        <table border="1">
            <thead>
                <tr>
                    <th>ID</th>
                    <th>Name</th>
                    <th>Quantity</th>
                    <th>Price</th>
                </tr>
            </thead>
            <tbody>
                <%
                    List<ProductDto> productList = (List<ProductDto>) session.getAttribute("productList");
                    if (productList != null && !productList.isEmpty()) {
                        for (ProductDto product : productList) {
                %>
                <tr>
                    <td><%= product.getProductId() %></td>
                    <td><%= product.getProductName() %></td>
                    <td><%= product.getProductQuantity() %></td>
                    <td><%= product.getProductPrice()*product.getProductQuantity()  %></td>
                </tr>
                <%
                        }
                    } else {
                %>
                <tr>
                    <td colspan="4">No products available.</td>
                </tr>
                <%
                    }
                    double total = 0;
                    if (productList != null && !productList.isEmpty()) {
                        for (ProductDto product : productList) {
                            total += product.getProductPrice()*product.getProductQuantity();
                        }
                    }
                %>
                  <%double gst=(total*18)/100;
         double sgst=gst/2;
         double cgst=gst/2;
         double sp=total-gst;
         %>
                	<tr>

					<td  colspan=3></td>

				

					<td>
					CGST @ 9% :<Strong><%=cgst %></Strong> <br>
					SGST @ 9% :<Strong><%=sgst %></Strong> <br>
					Selling Price @ :<Strong><%=total-gst%></Strong><br>
      				<strong>Grand Total: <%=total %></strong>
      				</td>

				</tr>
                
            </tbody>
        </table>
    </div>

    
   <% session.setAttribute("totalamt",total);%> 
    <div class="download">
    <form action="InvoiceServlet"  onsubmit="setTimeout(() => { window.location.href='index.jsp'; }, 1000);">
        <% session.setAttribute("finalProductList",productList);  %>
        <input type="submit" value="Download Invoice" class="download-btn" >
        </form>
    </div>

</div>

</body>
</html>