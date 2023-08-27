package edu.nutech.tht.util;

import lombok.extern.slf4j.Slf4j;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

@Slf4j
public class DatabaseUtilities {
    public static Connection getConnection(){
        return getConnection("jdbc:mysql://localhost:3306/nutech_tht?useLegacyDatetimeCode=false&serverTimezone=Asia/Bangkok&useSSL=false", "root", "");
    }

    private static Connection getConnection(String url, String user, String pass){
        Connection con = null;
        try {
            Class.forName("com.mysql.jdbc.Driver");
            con = DriverManager.getConnection(url, user, pass);

        } catch (ClassNotFoundException | SQLException e) {
            log.error("Error connection: {}", e.getMessage());
        }
        return con;
    }
}
