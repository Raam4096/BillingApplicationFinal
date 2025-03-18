package com.products;

public class ProductDto {

	private int productId;

	private String productName;

	private double productPrice;

	private int productQuantity;

	public ProductDto() {

		super();

		// TODO Auto-generated constructor stub

	}

	public ProductDto(int productId, String productName,  double productPrice) {
       

		this.productId = productId;

		this.productName = productName;

		this.productPrice = productPrice;

		

	}

	public int getProductId() {

		return productId;

	}

	public void setProductId(int productId) {

		this.productId = productId;

	}

	public String getProductName() {

		return productName;

	}

	public void setProductName(String productName) {

		this.productName = productName;

	}

	public double getProductPrice() {

		return productPrice;

	}

	public void setProductPrice(double productPrice) {

		this.productPrice = productPrice;

	}

	public int getProductQuantity() {

		return productQuantity;

	}

	public void setProductQuantity(int productQuantity) {

		this.productQuantity = productQuantity;

	}

}