package com.library.util;


import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnection{
    private static final String URL= "jdbc:mysql://localhost:3306/libraryDB";
    private static final String USERNAME= "root";
    private static final String PASSWORD= "Ayush@sql10";
    private DatabaseConnection(){}
    public static Connection getConnection(){
            try{
                Class.forName("com.mysql.cj.jdbc.Driver");
                Connection connection= DriverManager.getConnection(URL, USERNAME, PASSWORD);
                System.out.println("Database connected successfully! ");
                return connection;
            }catch (ClassNotFoundException e){
                System.out.println("MYSQL JDBC DRIVER NOT FOUND");
                e.printStackTrace();
            } catch (SQLException e) {
                System.out.println("DATABASE CONNECTION FAILED");
                e.printStackTrace();
            }
        return null;
    }

    public static void closeConnection(Connection connection){
           if(connection!=null){
               try{
                   connection.close();
                   System.out.println("Database connection closed!");
               }catch(SQLException e){
                   System.out.println("Error closing connection : " + e.getMessage());
               }
           }
    }
}