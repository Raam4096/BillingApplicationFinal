package com.customer;



public class CustomerDto {
private int customerId;
private String customerName;
private String phonenumber;
private String address;
private String date;
private String email;
public CustomerDto(String customerName, String phonenumber, String address, String invoiceDate, String email) {
	super();
	this.customerName = customerName;
	this.phonenumber = phonenumber;
	this.address = address;
	this.date = invoiceDate;
	this.email = email;
}
public int getCustomerId() {
	return customerId;
}
public void setCustomerId(int customerId) {
	this.customerId = customerId;
}
public String getCustomerName() {
	return customerName;
}
public void setCustomerName(String customerName) {
	this.customerName = customerName;
}
public String getPhonenumber() {
	return phonenumber;
}
public void setPhonenumber(String phonenumber) {
	this.phonenumber = phonenumber;
}
public String getAddress() {
	return address;
}
public void setAddress(String address) {
	this.address = address;
}
public String getDate() {
	return date;
}
public void setDate(String date) {
	this.date = date;
}
public String getEmail() {
	return email;
}
public void setEmail(String email) {
	this.email = email;
}

}
