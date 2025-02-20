package com.nicollasprado.db;

import com.nicollasprado.Exceptions.EntityNotFoundException;
import com.nicollasprado.Exceptions.TypeInferException;
import com.nicollasprado.abstraction.Entity;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.sql.Connection;
import java.sql.DriverManager;
import java.util.*;

import java.sql.*;

public class Query<T extends Entity> {

    // https://jdbc.postgresql.org/documentation/query/


    public List<String> StandartQuery(String query){
        Connection conn = this.dbConnect();

        try{
            PreparedStatement st = conn.prepareStatement(query);

            ResultSet rs = st.executeQuery();
            ResultSetMetaData resultSetMetaData = rs.getMetaData();

            List<String> result = new ArrayList<>();

            Type genericType = this.getGenericType();
            System.out.println(genericType.getTypeName());

            if(!rs.next()){
                throw new EntityNotFoundException();
            }

            for(int i=1; i <= resultSetMetaData.getColumnCount(); i++){
                System.out.println(rs.getString(i));
                result.add(rs.getString(i));
            }

            st.close();
            rs.close();
            conn.close();

            return result;
        }catch (SQLException e){
            throw new RuntimeException("Error creating statement: ", e);
        }
    }

    private Type getGenericType(){
        // return Query<TYPE>
        Type type = this.getClass().getGenericSuperclass();

        // Verify if the class is Parametizes (if have <Type>)
        if(!(type instanceof ParameterizedType)){
            throw new TypeInferException();
        }

        // Return the type from an array of types <String, int> [0] = string  [1] == int
        return ((ParameterizedType) type).getActualTypeArguments()[0];
    }

    private Connection dbConnect(){
        try{
            String url = "jdbc:postgresql://localhost:5432/vehicles_control";
            Properties props = new Properties();
            props.setProperty("user", "postgres");
            props.setProperty("password", "");

            return DriverManager.getConnection(url, props);
        }catch (SQLException e){
            throw new RuntimeException("Error connection to database: ", e);
        }
    }
}
