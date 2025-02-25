package com.nicollasprado.db;

import com.nicollasprado.Exceptions.EntityNotFoundException;
import com.nicollasprado.Exceptions.InvalidQueryType;
import com.nicollasprado.abstraction.Persistence;
import com.nicollasprado.utils.AnnotationHandler;
import com.nicollasprado.utils.StatementUtils;

import java.lang.reflect.Field;
import java.util.*;

import java.sql.*;

// https://jdbc.postgresql.org/documentation/query/

public class Query<T, R> implements Persistence<T, R> {
    private final Class<T> entityClass;
    private final Class<R> returnClass;
    private final Field entityId;
    private final String entityName;


    @Override
    public void save(T entity){
        List<Field> columnFields = AnnotationHandler.getColumnFields(entityClass);
        String query = StatementUtils.prepareRefinedQueryNoId(columnFields, entityName);
        List<Object> params = StatementUtils.getValidParamsNoId(columnFields, entity);

        this.refinedTransactionalQuery(query, params);
    }

    @Override
    public <X> R findById(X id){
        return refinedGetQuery("SELECT * FROM " + entityName + " WHERE " + entityId.getName() + " = ? LIMIT 1", List.of(id));
    }

    @Override
    public List<R> findAll() {
        ReturnClassHandler<R> returnClassHandler = new ReturnClassHandler<>(returnClass);

        try{
            Statement statement = DbConnectionHandler.db.createStatement();
            statement.setFetchSize(50);
            ResultSet resultSet = statement.executeQuery("SELECT * FROM " + entityName + ";");

            returnClassHandler.resolveMany(resultSet);

            return returnClassHandler.getReturnClassList();
        } catch (SQLException e) {
            throw new RuntimeException("Error while running findAll: " + e);
        }
    }

    public R findBy(List<?> params, List<?> values){
        StringBuilder query = new StringBuilder("Select * FROM " + entityName + " ");
        for(int i=0; i < params.size(); i++){
            if(i == 0){
                query.append("WHERE ")
                        .append(params.get(i))
                        .append("=? ");
            } else {
                query.append("AND ")
                        .append(params.get(i))
                        .append("=? ");
            }

        }
        query.append("LIMIT 1");

        return refinedGetQuery(query.toString(), values);
    }

    public List<R> findAllBy(List<?> params, List<?> values){
        StringBuilder query = new StringBuilder("Select * FROM " + entityName + " ");
        for(int i=0; i < params.size(); i++){
            if(i == 0){
                query.append("WHERE ")
                        .append(params.get(i))
                        .append("=? ");
            } else {
                query.append("AND ")
                        .append(params.get(i))
                        .append("=? ");
            }

        }

        ReturnClassHandler<R> returnClassHandler = new ReturnClassHandler<>(returnClass);
        try{
            Statement statement = DbConnectionHandler.db.createStatement();
            statement.setFetchSize(50);
            ResultSet resultSet = statement.executeQuery(query.toString());

            returnClassHandler.resolveMany(resultSet);

            return returnClassHandler.getReturnClassList();
        } catch (SQLException e) {
            throw new RuntimeException("Error while running findAllBy: " + e);
        }
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

                if (!fetchResult.next()) {
                    throw new EntityNotFoundException();
                }

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
    public R rawGetQuery(String query){
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
        this.entityId = AnnotationHandler.getIdField(entityClass);
        this.entityName = AnnotationHandler.getEntityName(entityClass);
    }

}
