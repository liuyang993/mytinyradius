package org.tinyradius.test;

import java.beans.PropertyVetoException;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;

import org.apache.commons.dbcp2.BasicDataSource;

public class DataSource {

    private static DataSource     datasource;
    private BasicDataSource ds;

    private DataSource() throws IOException, SQLException, PropertyVetoException {
        ds = new BasicDataSource();
        //ds.setDriverClassName("com.mysql.jdbc.Driver");

        ds.setDriverClassName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
        
        //ds.setUsername("sa");
        //ds.setPassword("Passw0rdPassw0rd123");
        //ds.setUrl("jdbc:sqlserver://hk.worldhubcom.cn:1026");
        ds.setUrl("jdbc:sqlserver://hk.worldhubcom.cn:1026;databaseName=Pack2018;user=sa;password=Passw0rdPassw0rd123");

    }

    public static DataSource getInstance() throws IOException, SQLException, PropertyVetoException {
        if (datasource == null) {
            datasource = new DataSource();
            return datasource;
        } else {
            return datasource;
        }
    }

    public Connection getConnection() throws SQLException {
        return this.ds.getConnection();
    }

}