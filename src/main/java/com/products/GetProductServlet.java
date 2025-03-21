

package com.products;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import com.dbconnection.Connect;
@WebServlet("/GetProductServlet")
public class GetProductServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    public GetProductServlet() {
        super();

    }
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		try {
			Connection con= Connect.connect();
			 String getProducts="select * from product";
			 PreparedStatement ps1=con.prepareStatement(getProducts);
			 ResultSet rs=ps1.executeQuery();
			 List<ProductDto> listOfProducts= new ArrayList<>();
				while(rs.next()) {
					ProductDto product=new ProductDto(rs.getInt(1), rs.getString(2) ,rs.getDouble(3));
					listOfProducts.add(product);
				}
				
	            request.getSession().setAttribute("productsData", listOfProducts);
	            
	            
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

}
