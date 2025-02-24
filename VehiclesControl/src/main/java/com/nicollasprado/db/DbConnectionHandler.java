package com.nicollasprado.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class DbConnectionHandler {
    public static final Connection db = dbConnect();

    private static Connection dbConnect(){
        try{
            String url = "jdbc:postgresql://localhost:5432/vehicles_control";
            Properties props = new Properties();
            props.setProperty("user", "postgres");
            props.setProperty("password", "");

            Connection conn = DriverManager.getConnection(url, props);
            conn.setAutoCommit(false);
            return conn;
        }catch (SQLException e){
            throw new RuntimeException("Error connection to database: ", e);
        }
    }
}
