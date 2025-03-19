package com.customer;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import com.DbConnection.Connect;
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
            PreparedStatement ps=con.prepareStatement(addCustomer);
			ps.setString(1,cd.getCustomerName());

			ps.setString(2,cd.getPhonenumber());

			ps.setString(3,cd.getAddress());

			ps.setString(4,cd.getDate());
			
			ps.setString(5,cd.getEmail());   
			
			

			int ins=ps.executeUpdate();
			
		
			
			if(ins>0) {
			   
				RequestDispatcher rd= request.getRequestDispatcher("index.jsp");
				rd.include(request, response);
			}
			
			
		}
		catch(Exception e) {
			e.printStackTrace();
		}
		
		

	}



}