package Test;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.sql.Blob;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;

//@WebServlet("/servlet")
@MultipartConfig(maxFileSize = 16177215)

public class servlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private static final int BUFFER_SIZE = 4096;
	private static final String SAVE_DIR = "images";
	private String dbURL = "jdbc:sqlserver://localhost:1433;databaseName=UploadFileServletDB";
	private String dbUser = "sa";
	private String dbPass = "1234567890";

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		String firstName = request.getParameter("firstName");
		String lastName = request.getParameter("lastName");
		InputStream inputStream = null;

		Part filePart = request.getPart("photo");
		//String fileUploadName = "";
		String fileUploadName = extractFileName(filePart);
		if (filePart != null) {
			fileUploadName = filePart.getName();

			inputStream = filePart.getInputStream();
		}
		Connection conn = null;
		String message = null;
		String filePath = "E:" + fileUploadName + ".jpg";

		try {

			DriverManager.registerDriver(new com.microsoft.sqlserver.jdbc.SQLServerDriver());
			conn = DriverManager.getConnection(dbURL, dbUser, dbPass);

			String sql = "INSERT INTO contacts (first_name, last_name, photo) values (?, ?, ?)";
			PreparedStatement statement = conn.prepareStatement(sql);
			statement.setString(1, firstName);
			statement.setString(2, lastName);
			if (inputStream != null) {
				statement.setBlob(3, inputStream);
			}
			int row = statement.executeUpdate();

			if (row > 0) {
				message = "File uploaded and saved into database";
			}

			String sql1 = "SELECT photo FROM contacts WHERE first_name=? AND last_name=?";

			statement = conn.prepareStatement(sql1);
			statement.setString(1, firstName);
			statement.setString(2, lastName);
			ResultSet result = statement.executeQuery();
			if (result.next()) {
				Blob blob = result.getBlob("photo");
				inputStream = blob.getBinaryStream();
				OutputStream outputStream = new FileOutputStream(filePath);
				int bytesRead = -1;
				byte[] buffer = new byte[BUFFER_SIZE];
				while ((bytesRead = inputStream.read(buffer)) != -1) {
					outputStream.write(buffer, 0, bytesRead);
				}
				inputStream.close();
				outputStream.close();
			}
		} catch (SQLException ex) {
			message = "ERROR: " + ex.getMessage();
			ex.printStackTrace();
		} finally {
			if (conn != null) {
				try {
					conn.close();
				} catch (SQLException ex) {
					ex.printStackTrace();
				}
			}
			request.setAttribute("Message", message);
			getServletContext().getRequestDispatcher("MessageServlet").forward(request, response);
		}
	
	
	
	
	
//upload	
	
		
		//for (Part part : request.getParts()) {
		//      String fileName = extractFileName(part);
		      // refines the fileName in case it is an absolute path
		//      fileName = new File(fileName).getName();
		     // part.write(this.getFolderUpload().getAbsolutePath() + File.separator + fileName);
		//    }
		    
		 //   response.setContentType("text/html");
		 //   PrintWriter out=response.getWriter();
		 //   out.print("Upload File Success!");
	
	
	}



	
	
	private String extractFileName(Part part) {
	       // form-data; name="file"; filename="C:\file1.zip"
	       // form-data; name="file"; filename="C:\Note\file2.zip"
	       String contentDisp = part.getHeader("content-disposition");
	       String[] items = contentDisp.split(";");
	       for (String s : items) {
	           if (s.trim().startsWith("filename")) {
	               // C:\file1.zip
	               // C:\Note\file2.zip
	               String clientFileName = s.substring(s.indexOf("=") + 2, s.length() - 1);
	               clientFileName = clientFileName.replace("\\", "/");
	               int i = clientFileName.lastIndexOf('/');
	               // file1.zip
	               // file2.zip
	               return clientFileName.substring(i + 1);
	           }
	       }
	       return null;
	   }
	
}
