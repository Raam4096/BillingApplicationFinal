<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Insert title here</title>
</head>
<body>

</body>
</html>

<tr style="border: 2px solid black">
    
					<td style="border-right: 2px solid black">No items added</td>

					<td style="border-right: 2px solid black">No items added</td>

					<td style="border-right: 2px solid black">No items added</td>

					<td style="border-right: 2px solid black">No items added</td>

				</tr>

				<tr style="border: 2px solid black">

					<td style="border-right: 2px solid black"></td>

					<td style="border-right: 2px solid black"></td>

					<td style="border-right: 2px solid black">Total :</td>

					<td style="border-right: 2px solid black"></td>

				</tr>

				<tr style="border: 2px solid black">

					<td style="border-right: 2px solid black"></td>

					<td style="border-right: 2px solid black"></td>

					<td style="border-right: 2px solid black"></td>

					<td style="border-right: 2px solid black"><p>Add: CGST @
							14% - _____</p> <br>

						<p>Add: SGST @ 14% - _____</p> <br>

						<p>Balance Received: _____</p> <br>

						<p>Balance Due: _____</p> <br> <strong>Grand Total:
							_____</strong></td>

				</tr>

				
				
				
				
				
				
				
				
				
				
				
				
				<%
	List<ProductDto> listOfProducts=null;
	if (session.getAttribute("finalProductList") != null) {
		listOfProducts = (List<ProductDto>) session.getAttribute("finalProductList");
		System.out.print("list fetched");
	}
	else{
		System.out.print("not fetched");
	}
	%>
     

				<%
				if(listOfProducts!=null && listOfProducts.isEmpty()){
				for (ProductDto product : listOfProducts) {
				%>
				<tr style="border: 2px solid black">
					<td style="border-right: 2px solid black"><%=product.getProductId()%></td>
					<td style="border-right: 2px solid black"><%=product.getProductName()%></td>
					<td style="border-right: 2px solid black"><%=product.getProductQuantity()%></td>
					<td style="border-right: 2px solid black"><%=product.getProductPrice() * product.getProductQuantity()%></td>
				</tr>
				<%
				}
				}
				%>