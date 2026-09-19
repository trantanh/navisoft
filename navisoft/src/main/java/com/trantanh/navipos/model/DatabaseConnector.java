package com.trantanh.navipos.model;

import com.trantanh.navipos.config.SpringContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * @author Tuan Anh, tran.t.anh@email.cz
 */
public abstract class DatabaseConnector {

    private static final Logger logger = LoggerFactory.getLogger(DatabaseConnector.class);

    protected Connection connection;
    protected Statement statement;
    protected PreparedStatement preparedStatement;
    protected ResultSet resultSet;
    private final DataSource dataSource;

    public DatabaseConnector() {
        dataSource = SpringContext.getBean(DataSource.class);
        try {
            connection = dataSource.getConnection();
            statement = connection.createStatement();
        } catch (SQLException ex) {
            throw new IllegalStateException("Nelze se připojit k databázi NaviSoft", ex);
        }
    }

    protected Connection getDatabaseConnection() {
        try {
            return dataSource.getConnection();
        } catch (SQLException e) {
            throw new IllegalStateException("Nelze se připojit k databázi NaviSoft", e);
        }
    }

    public ResultSet getResult(String query) {
        try {
            return resultSet = statement.executeQuery(query);
        } catch (SQLException ex) {
            logger.error("Database query failed", ex);
        }
        return null;
    }

    public PreparedStatement getPreparedStatement() {
        return preparedStatement;
    }

    public void setPreparedStatement(PreparedStatement preparedStatement) {
        this.preparedStatement = preparedStatement;
    }

    public Statement getStatement() {
        return statement;
    }

    public void setStatement(Statement statement) {
        this.statement = statement;
    }

    public Connection getConnection() {
        return connection;
    }

    public void setConnection(Connection connection) {
        this.connection = connection;
    }

    public PreparedStatement getQuery() {
        return preparedStatement;
    }

    public void setQuery(PreparedStatement query) {
        this.preparedStatement = query;
    }

}
