package com.nicollasprado.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.util.HashSet;
import java.util.Properties;

import java.sql.*;
import java.util.Set;

public class Query {

    // TODO - Arrumar os retornos
    // https://jdbc.postgresql.org/documentation/query/


    public Set<Object> StandartQuery(String query){
        Connection conn = this.dbConnect();

        try{
            Statement st = conn.createStatement();
            ResultSet rs = st.executeQuery(query);

            Set<Object> result = new HashSet<>();

            while(rs.next()){
                result.add(rs.getRow());
            }

            st.close();
            rs.close();
            conn.close();

            return result;
        }catch (SQLException e){
            throw new RuntimeException("Error creating statement: ", e);
        }
    }

    private Connection dbConnect(){
        try{
            String url = "jdbc:postgresql://localhost:8080/vehicles_control";
            Properties props = new Properties();
            props.setProperty("user", "postgres");
            props.setProperty("password", "");
            props.setProperty("ssl", "true");

            return DriverManager.getConnection(url, props);
        }catch (SQLException e){
            throw new RuntimeException("Error connection to database: ", e);
        }
    }
}
