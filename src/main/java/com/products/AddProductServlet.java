package com.products;

import jakarta.servlet.ServletException;

import jakarta.servlet.annotation.WebServlet;

import jakarta.servlet.http.HttpServlet;

import jakarta.servlet.http.HttpServletRequest;

import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

import java.util.ArrayList;

import java.util.List;

@WebServlet("/AddProductServlet")
public class AddProductServlet extends HttpServlet {

	private static final long serialVersionUID = 1L;

	public AddProductServlet() {

		super();

	}

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		// Get product details from request parameters (this will be coming from your
		// form)

		String name = request.getParameter("product");

		int qty = Integer.parseInt(request.getParameter("quantity"));

		double price = Double.parseDouble(request.getParameter("price"));

		int id = Integer.parseInt(request.getParameter("productId"));

		List<ProductDto> productList = (List<ProductDto>) request.getSession().getAttribute("productList");

		// If the product list is null, initialize it

		if (productList == null) {

			productList = new ArrayList<>();

		}

		ProductDto newProduct = new ProductDto(id,name,price);
        newProduct.setProductQuantity(qty);
		productList.add(newProduct);

		// Store the product list in the session

		request.getSession().setAttribute("productList", productList);

		// Forward to the JSP page to render the updated list

		request.getRequestDispatcher("index.jsp").forward(request, response);

	}

}