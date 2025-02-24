package com.nicollasprado.db;

import com.nicollasprado.Exceptions.InvalidQueryType;
import com.nicollasprado.abstraction.Entity;
import com.nicollasprado.db.utils.EntityUtils;
import com.nicollasprado.db.utils.StatementUtils;

import java.lang.reflect.Field;
import java.sql.Connection;
import java.sql.DriverManager;
import java.util.*;

import java.sql.*;

// https://jdbc.postgresql.org/documentation/query/

public class Query<T extends Entity, R> {
    private final Class<T> entityClass;
    private final Class<R> returnClass;
    private final Field idField;
    private final String entityName;


    public void save(T entity){
        List<Field> columnFields = EntityUtils.getColumnFields(entityClass);

        StringBuilder columnsNames = new StringBuilder("(");
        for(int i = 0; i < columnFields.size(); i++){
            if(i == columnFields.size() - 1){
                columnsNames.append(EntityUtils.getColumnFieldName(columnFields.get(i))).append(")");
            }else{
                columnsNames.append(EntityUtils.getColumnFieldName(columnFields.get(i))).append(", ");
            }
        }

        StringBuilder paramsEntries = new StringBuilder("(");
        for(int i = 0; i < columnFields.size(); i++){
            if(i == columnFields.size() - 1){
                paramsEntries.append("?)");
            }else{
                paramsEntries.append("?, ");
            }
        }

        String query = "INSERT INTO " + this.entityName + " " + columnsNames + " VALUES " + paramsEntries + ";";

        List<Object> params = new ArrayList<>();
        columnFields.forEach(field -> params.add(EntityUtils.getColumnValue(field, entity)));

        refinedTransactionalQuery(query, params);
    }

    public <X> R findById(X id){
        return refinedGetQuery("SELECT * FROM " + this.entityName + " WHERE " + idField.getName() + " = ? LIMIT 1", List.of(id));
    }



    private void refinedTransactionalQuery(String query, List<?> parameters){
        Connection conn = this.dbConnect();

        PreparedStatement statement = StatementUtils.getValidStatement(conn, query, parameters);

        try{
            conn.setAutoCommit(false);
            String upperQuery = query.toUpperCase();
            if(upperQuery.contains("UPDATE") || upperQuery.contains("DELETE") || upperQuery.contains("INSERT")){
                statement.executeUpdate();
                conn.commit();
            }

            conn.close();
            statement.close();
        } catch (SQLException e) {
            throw new RuntimeException("Error while saving entity: " + e);
        }
    }

    private R refinedGetQuery(String query, List<?> parameters){
        Connection conn = this.dbConnect();

        PreparedStatement statement = StatementUtils.getValidStatement(conn, query, parameters);

        ReturnClassHandler<R> returnClassHandler = new ReturnClassHandler<>(returnClass);

        try{
            String upperQuery = query.toUpperCase();
            if(!upperQuery.contains("UPDATE") && !upperQuery.contains("DELETE") && !upperQuery.contains("INSERT")){
                ResultSet fetchResult = statement.executeQuery();

                returnClassHandler.databaseDataToEntityInstance(fetchResult);

                statement.close();
                fetchResult.close();
                conn.close();

                return returnClassHandler.getReturnClassInstance();
            }

            throw new InvalidQueryType(query);
        } catch (SQLException e) {
            throw new RuntimeException("Error while fetching data: " + e.getMessage());
        }
    }

    // BECAREFUL USING THIS! MAY BE VULNARABLE TO SQL INJECTION
    public R RawQuery(String query){
        Connection conn = this.dbConnect();

        ReturnClassHandler<R> returnClassHandler = new ReturnClassHandler<>(returnClass);

        try{
            PreparedStatement st = conn.prepareStatement(query);
            ResultSet rs = st.executeQuery();

            returnClassHandler.databaseDataToEntityInstance(rs);

            st.close();
            rs.close();
            conn.close();

            return returnClassHandler.getReturnClassInstance();
        }catch (SQLException e){
            throw new RuntimeException("Error creating statement: ", e);
        }
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


    @SuppressWarnings("unchecked")
    public Query(Class<T> entityClass){
        this.entityClass = entityClass;
        this.returnClass = (Class<R>) entityClass;
        this.idField = EntityUtils.getIdField(entityClass);
        this.entityName = EntityUtils.getEntityName(entityClass);
    }

    // TODO - CREATE WAY TO WORK WITH RECORDS
//    public Query(Class<T> entityClass, Class<R> returnClass){
//        this.entityClass = entityClass;
//        this.returnClass = returnClass;
//    }
}
