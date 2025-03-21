

package com.customer;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import com.dbconnection.Connect;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@WebServlet("/CustomerServlet")
public class CustomerServlet extends HttpServlet {

	private static final long serialVersionUID = 1L;

    

    public CustomerServlet() {

        super();

        

    }

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		// TODO Auto-generated method stub
		HttpSession hs=request.getSession();
		
		String customername=request.getParameter("customername");

		String phonenumber=request.getParameter("phonenumber");

		String address=request.getParameter("address");

		String email=(String)request.getParameter("email");

		String date=(String)request.getParameter("date");
		
		hs.setAttribute("cname",customername);
		hs.setAttribute("cphone",phonenumber);
		hs.setAttribute("caddress",address);
		hs.setAttribute("cmail",email);
		hs.setAttribute("cdate",date);

		CustomerDto cd=new CustomerDto(customername,phonenumber,address,date,email);
        
		try {
			
			Connection con= Connect.connect();
			String addCustomer="insert into customer (name,phone,address,date,email) values(?,?,?,?,?)";
			String getCustomerId="select customer_id from customer where phone=?";
            PreparedStatement ps=con.prepareStatement(addCustomer);
            PreparedStatement ps1=con.prepareStatement(getCustomerId);
			ps.setString(1,cd.getCustomerName());

			ps.setString(2,cd.getPhonenumber());

			ps.setString(3,cd.getAddress());

			ps.setString(4,cd.getDate());
			
			ps.setString(5,cd.getEmail());   
			
			ps1.setString(1,cd.getPhonenumber());
			

			int ins=ps.executeUpdate();
			ResultSet rs=ps1.executeQuery();
			if(rs.next()) {
				hs.setAttribute("Invoice_Id", rs.getInt(1)+1000);
			}
		
			
			if(ins>0) {
			   
				RequestDispatcher rd= request.getRequestDispatcher("InvoiceServlet");
				rd.forward(request, response);
			}
			
			
		}
		catch(Exception e) {
			e.printStackTrace();
		}
		
		

	}



}
