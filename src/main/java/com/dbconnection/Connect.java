package com.dbconnection;
import java.sql.Connection;
import java.sql.DriverManager;

public class Connect {

	public static Connection connect() throws Exception {
		Class.forName("com.mysql.cj.jdbc.Driver");
	 Connection con=DriverManager.getConnection("jdbc:mysql://localhost:3306/grocery_app","root","root");
	 return con;
}
}

