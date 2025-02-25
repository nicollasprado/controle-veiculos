package com.nicollasprado.db;

import com.nicollasprado.Exceptions.InvalidQueryType;
import com.nicollasprado.abstraction.Entity;
import com.nicollasprado.utils.AnnotationHandler;
import com.nicollasprado.utils.EntityUtils;
import com.nicollasprado.utils.StatementUtils;

import java.lang.reflect.Field;
import java.util.*;

import java.sql.*;

// https://jdbc.postgresql.org/documentation/query/

public class Query<T extends Entity, R> {
    private final Class<T> entityClass;
    private final Class<R> returnClass;
    private final Field idField;
    private final String entityName;


    public void save(T entity){
        List<Field> columnFields = AnnotationHandler.getColumnFields(entityClass);

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

        String query = "INSERT INTO " + entityName + " " + columnsNames + " VALUES " + paramsEntries + ";";

        List<Object> params = new ArrayList<>();
        columnFields.forEach(field -> params.add(EntityUtils.getColumnValue(field, entity)));

        this.refinedTransactionalQuery(query, params);
    }

    public <X> R findById(X id){
        return refinedGetQuery("SELECT * FROM " + entityName + " WHERE " + idField.getName() + " = ? LIMIT 1", List.of(id));
    }



    private void refinedTransactionalQuery(String query, List<?> parameters){
        PreparedStatement statement = StatementUtils.getValidStatement(DbConnectionHandler.db, query, parameters);

        try{
            String upperQuery = query.toUpperCase();
            if(upperQuery.contains("UPDATE") || upperQuery.contains("DELETE") || upperQuery.contains("INSERT")){
                statement.executeUpdate();
                DbConnectionHandler.db.commit();
            }

            DbConnectionHandler.db.close();
            statement.close();
        } catch (SQLException e) {
            throw new RuntimeException("Error while saving entity: " + e);
        }
    }

    private R refinedGetQuery(String query, List<?> parameters){
        PreparedStatement statement = StatementUtils.getValidStatement(DbConnectionHandler.db, query, parameters);

        ReturnClassHandler<R> returnClassHandler = new ReturnClassHandler<>(returnClass);

        try{
            String upperQuery = query.toUpperCase();
            if(!upperQuery.contains("UPDATE") && !upperQuery.contains("DELETE") && !upperQuery.contains("INSERT")){
                ResultSet fetchResult = statement.executeQuery();

                returnClassHandler.databaseDataToEntityInstance(fetchResult);

                statement.close();
                fetchResult.close();

                return returnClassHandler.getReturnClassInstance();
            }

            throw new InvalidQueryType(query);
        } catch (SQLException e) {
            throw new RuntimeException("Error while fetching data: " + e.getMessage());
        }
    }

    // BECAREFUL USING THIS! MAY BE VULNARABLE TO SQL INJECTION
    public R RawQuery(String query){
        ReturnClassHandler<R> returnClassHandler = new ReturnClassHandler<>(returnClass);

        try{
            PreparedStatement st = DbConnectionHandler.db.prepareStatement(query);
            ResultSet rs = st.executeQuery();

            returnClassHandler.databaseDataToEntityInstance(rs);

            st.close();
            rs.close();

            return returnClassHandler.getReturnClassInstance();
        }catch (SQLException e){
            throw new RuntimeException("Error creating statement: ", e);
        }
    }



    @SuppressWarnings("unchecked")
    public Query(Class<T> entityClass){
        this.entityClass = entityClass;
        this.returnClass = (Class<R>) entityClass;
        this.idField = AnnotationHandler.getIdField(entityClass);
        this.entityName = AnnotationHandler.getEntityName(entityClass);
    }

    // TODO - CREATE WAY TO WORK WITH RECORDS
//    public Query(Class<T> entityClass, Class<R> returnClass){
//        this.entityClass = entityClass;
//        this.returnClass = returnClass;
//    }
}
