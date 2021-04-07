package Test;

import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;

/**
 * Servlet implementation class servlet1
 */
@WebServlet("/UploadSQL")
public class UploadSQL extends HttpServlet {
	private static final long serialVersionUID = 1L;
	// private static final String user="sa";
	// private static final String password="123456";
	// private String url = "jdbc:sqlserver://localhost:1433;databaseName=Test";
	// private Connection conn;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public UploadSQL() {
		super();
		// TODO Auto-generated constructor stub
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		// TODO Auto-generated method stub
		response.getWriter().append("Served at: ").append(request.getContextPath());

	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		// TODO Auto-generated method stub

		// doGet(request, response);

		response.setContentType("text/html");
		PrintWriter out = response.getWriter();

		String x = request.getParameter("firstName");
		String y = request.getParameter("lastName");
		InputStream inputStream = null;
		//Part filePart = request.getPart("photo");
		//if (filePart != null) {
			//fileUploadName = filePart.getName();
			//inputStream = filePart.getInputStream();
		//}
		
		try {
			KetNoiSQL a = new KetNoiSQL();

			if (a.addMembers(x, y,inputStream))
				out.print("success!");
			else
				out.print("unsuccessful!");

		} catch (ClassNotFoundException | SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

}
