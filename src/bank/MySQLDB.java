/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package bank;

import java.sql.*;

/**
 *
 * @author Lenovo
 */
public class MySQLDB {
    private int portno = 3306;
    private String dbName = "bankmanagementdb";
    private String dbUserName = "root";
    private String dbPassword = "";
    private String dbConnURL = "jdbc:mysql://localhost:" + portno + "/" + dbName;
    private Connection mysqlConn;
    private Statement stStatement;
    private PreparedStatement psExecuteNonQuery;
    private ResultSet mysqlResult;
    
    public MySQLDB() {
        try {
            mysqlConn = DriverManager.getConnection(dbConnURL, dbUserName, dbPassword);
        } catch (SQLException ex) { System.out.println("Connection Failed!!" + ex.toString()); }
    }
    
    public void ExecuteNonQuery(String strQuery) {
        try {
            psExecuteNonQuery = mysqlConn.prepareStatement(strQuery);
            psExecuteNonQuery.executeUpdate();
        } catch (SQLException ex) { System.out.println(ex); }
    }
    
    public String ExecuteScalar(String strQuery) {
        try {
            stStatement = mysqlConn.createStatement();
            mysqlResult = stStatement.executeQuery(strQuery);
            while(mysqlResult.next()) {
                return mysqlResult.getString(1);
            }
        } catch (SQLException ex) { System.out.println(ex); }
        return "";
    }
    
    public ResultSet ExecuteDataAdapter(String strQuery) {
        try {
            stStatement = mysqlConn.createStatement();
            mysqlResult = stStatement.executeQuery(strQuery);
            return mysqlResult;
        } catch (SQLException ex) { System.out.println(ex); }
        return null;
    }
}
