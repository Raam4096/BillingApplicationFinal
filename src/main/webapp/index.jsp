<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="java.util.*" %>
<%@ page import="com.products.ProductDto" %>
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
                <input type="text" placeholder="Customer Name" name="customername">
                <input type="text" placeholder="Phone Number" name="phonenumber">
                <input type="email" placeholder="Email" name="email">
            </div>
            <div class="form-row">
                <input type="text" placeholder="Address" name="address">
                <input type="date" placeholder="Date" name="date">
            </div>
            <div class="form-row">
                <button type="submit">Add Customer</button>
            </div>
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
            </tbody>
        </table>
    </div>

    <div class="totals">
        <label>Total:</label>
        <input type="text" readonly value="<%= total %>">
    </div>
   <% session.setAttribute("totalamt",total);%> 
    <div class="download">
    <form action="invoice.jsp">
        <% session.setAttribute("finalProductList",productList);  %>
        <input type="submit" value="Download Invoice" class="download-btn">
        </form>
    </div>

</div>

</body>
</html>