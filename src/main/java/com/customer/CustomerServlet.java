package com.customer;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import com.dbconnection.Connect;
import com.products.ProductDto;

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

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		// TODO Auto-generated method stub
         
	    PrintWriter pw=response.getWriter();
	    response.setContentType("text/html");
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
            String getProducts="select * from product";
            PreparedStatement ps=con.prepareStatement(addCustomer);
            PreparedStatement ps1=con.prepareStatement(getProducts);
			ps.setString(1,cd.getCustomerName());

			ps.setString(2,cd.getPhonenumber());

			ps.setString(3,cd.getAddress());

			ps.setString(4,cd.getDate());
			
			ps.setString(5,cd.getEmail());
            
			
			

			int ins=ps.executeUpdate();
			
			ResultSet rs=ps1.executeQuery();
			List<ProductDto> listOfProducts= new ArrayList<>();
			while(rs.next()) {
				ProductDto product=new ProductDto(rs.getInt(1), rs.getString(2) ,rs.getDouble(3));
				listOfProducts.add(product);
			}
			
            request.getSession().setAttribute("productsData", listOfProducts);
			if(ins>0) {
			    pw.print("<script>alert('Customer Added Successfully');</script>");
				RequestDispatcher rd= request.getRequestDispatcher("index.jsp");
				rd.include(request, response);
			}
			
			
		}
		catch(Exception e) {
			e.printStackTrace();
		}
		
		

	}



}