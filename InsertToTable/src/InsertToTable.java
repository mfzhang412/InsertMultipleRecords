import java.sql.*;

import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;

public class InsertToTable {
	/**
	static String jdbcDriver = "com.mysql.jdbc.Driver";
	static String dbURL = "jdbc:mysql://localhost/STUDENTS";
	
	static String user = "username";
	static String pass = "password";
	*/
	
	public static String[] dbCred() {
		String driver = JOptionPane.showInputDialog("Insert JDBC driver name");
		String url = JOptionPane.showInputDialog("Insert database URL");
		String[] r = {driver,url};
		return r;
	}
	
	public static String[] userCred() {
		String username = JOptionPane.showInputDialog("Insert database username");
		String password = JOptionPane.showInputDialog("Insert database password");
		String[] s = {username,password};
		return s;
	}
	
	public static String getFileLocation() {
		String filename = "";
		JFileChooser fc = new JFileChooser();
		int response = fc.showOpenDialog(null);
		if (response == JFileChooser.APPROVE_OPTION) {
			filename = fc.getSelectedFile().toString();
		} else {
			System.out.println("File operation cancelled.");
			return null;
		}
		return filename;
	}
	
	public static ArrayList<String[]> getFileStuff() {
		String fileLocation = getFileLocation();
		ArrayList<String[]> file = new ArrayList<String[]>();
		try {
			BufferedReader br = new BufferedReader(new FileReader(fileLocation));
			String line;
			while ((line = br.readLine()) != null) {
			    // use comma as separator
				file.add(line.split(","));
			}
			br.close();
		} catch (Exception e) {
			System.out.println("Error");
		}
		return file;
	}
	
	public static String getTablename() {
		String n = JOptionPane.showInputDialog("Insert tablename");
		return n;
	}
	
	public static void main(String[] args) {
		String msg = "If you want a record to be stored as a VARCHAR put single quotes around it. ";
		msg += "If you want a record to be a number, don't but anything around it...i think";
		System.out.println(msg);
		String[] r = InsertToTable.dbCred();
		String jdbcDriver = r[0];
		String dbURL = r[1];
		
		String[] s = InsertToTable.userCred();
		String user = s[0];
		String pass = s[1];
		
		String tablename = InsertToTable.getTablename();
		
		ArrayList<String[]> data = InsertToTable.getFileStuff();
		
		Connection conn = null;
		Statement stmt = null;
		try {
			// register jdbc driver
			Class.forName(jdbcDriver);
			
			// open connection
			conn = DriverManager.getConnection(dbURL, user, pass);
			
			// execute queries
			stmt = conn.createStatement();
			String init = "INSERT INTO " + tablename + " VALUES (";
			String sql;
			for (int i = 0; i < data.size(); i++) {
				sql = init + "(" + String.join(", ", data.get(i)) + ")";
				stmt.executeUpdate(sql);
			}
			
			// finish
			System.out.println("Finished inserting all records.");
			
		} catch(SQLException se) {
			se.printStackTrace();
		} catch(Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if (stmt != null) conn.close();
			} catch(SQLException se) {
				// do nothing
			}
			try {
				if (conn != null) conn.close();
			} catch (SQLException se) {
				se.printStackTrace();
			}
		}
	}
}