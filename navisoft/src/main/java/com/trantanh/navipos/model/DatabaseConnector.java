package com.trantanh.navipos.model;

import com.mysql.jdbc.Connection;

import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;

import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;

/**
 * @author Tuan Anh, tran.t.anh@email.cz
 */
public abstract class DatabaseConnector {

    protected Connection connection;
    protected Statement statement;
    protected PreparedStatement preparedStatement;
    protected ResultSet resultSet;
    private final String url = "jdbc:mysql://localhost/pricetags";
    private final String user = "root";
    private final String password = "";

    public DatabaseConnector() {
        try {
            connection = (Connection) DriverManager.getConnection(url, user, password);
            statement = connection.createStatement();
        } catch (SQLException ex) {
            Alert alert = new Alert(AlertType.WARNING);
            alert.setTitle("Pozor!");
            alert.setHeaderText("Nelze připojit se k databazi");
            alert.setContentText("Zapnete databaze pomocí programu XAMPP poté spuste program znova");
            Optional<ButtonType> result = alert.showAndWait();
            if (result.get() == ButtonType.OK) {
                System.exit(0);
            }
        }
    }

    protected Connection getDatabaseConnection() {
        try {
            return (Connection) DriverManager.getConnection(url, user, password);
        } catch (SQLException e) {
            Alert alert = new Alert(AlertType.WARNING);
            alert.setTitle("Pozor!");
            alert.setHeaderText("Nelze připojit se k databazi");
            alert.setContentText("Zapnete databaze pomocí programu XAMPP poté spuste program znova");
            Optional<ButtonType> result = alert.showAndWait();
            if (result.isPresent()) {
                if (result.get() == ButtonType.OK) {
                    System.exit(0);
                }
            }
            throw new RuntimeException(e);
        }
    }

    public ResultSet getResult(String query) {
        try {
            return resultSet = statement.executeQuery(query);
        } catch (SQLException ex) {
            Logger.getLogger(DatabaseConnector.class.getName()).log(Level.SEVERE, null, ex);
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
