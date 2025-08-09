/**
 * Copyright (C) 2024 AIDC-AI
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.alibaba.langengine.gpt.nl2sql.db;

import com.alibaba.druid.pool.DruidDataSource;
import com.alibaba.langengine.gpt.nl2sql.db.meta.Column;
import com.alibaba.langengine.gpt.nl2sql.db.meta.DatasourceConfig;
import com.alibaba.langengine.gpt.nl2sql.db.meta.ForeignKey;
import com.alibaba.langengine.gpt.nl2sql.db.meta.PrimaryKey;
import com.alibaba.langengine.gpt.nl2sql.db.meta.Table;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import java.sql.*;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * SQL引擎
 *
 * @author xiaoxuan.lp
 */
@Slf4j
@Data
public class SQLEngine {

    private String databaseUri;

    private String dialect;

    private DatasourceConfig datasourceConfig;

    private DruidDataSource druidDataSource;


    public static SQLEngine createEngine(String databaseUri, String dialect) {
        SQLEngine engine = new SQLEngine();
        engine.setDatabaseUri(databaseUri);
        engine.setDialect(dialect);
        return engine;
    }

    public static SQLEngine createEngine(DatasourceConfig datasourceConfig) {
        SQLEngine engine = new SQLEngine();
        engine.setDatabaseUri(datasourceConfig.getUrl());
        engine.setDialect(datasourceConfig.getDialect());
        engine.setDatasourceConfig(datasourceConfig);
        return engine;
    }

    public Connection getConnection() {
        try {
            // 从连接池获取连接
            if (Objects.nonNull(datasourceConfig) && Boolean.TRUE.equals(datasourceConfig.getConnFromPool())) {
                return getConnectionFromPool();
            }

            // 创建单次连接
            if ("sqlite".equals(dialect)) {
                //通过反射加载驱动
                Class.forName("org.sqlite.JDBC");
                return DriverManager.getConnection(databaseUri);
            }else if (StringUtils.startsWith(databaseUri, "jdbc:postgresql")) {
                Class.forName("org.postgresql.Driver");
                return DriverManager.getConnection(databaseUri);
            }
        } catch (Throwable e) {
            log.error("getConnection error", e);
        }
        return null;
    }


    private Connection getConnectionFromPool() throws SQLException {
        if (druidDataSource == null) {
            synchronized (this) {
                if (druidDataSource == null) {
                    druidDataSource = new DruidDataSource();
                    druidDataSource.setUrl(datasourceConfig.getUrl());
                    druidDataSource.setUsername(datasourceConfig.getUserName());
                    druidDataSource.setPassword(datasourceConfig.getPwd());
                    // 初始化连接数
                    druidDataSource.setInitialSize(datasourceConfig.getInitialSize());
                    // 最大连接数
                    druidDataSource.setMaxActive(datasourceConfig.getMaxActive());
                    // 连接等待超时时间
                    druidDataSource.setMaxWait(12000);
                    // 配置间隔多久进行一次检测，检测需要关闭的空闲连接
                    druidDataSource.setTimeBetweenEvictionRunsMillis(3000);
                    druidDataSource.setValidationQuery("SELECT 1");
                    druidDataSource.setFilters("stat");
                    druidDataSource.setTestWhileIdle(true);
                    // 配置从连接池获取连接时，是否检查连接有效性，true每次都检查；false不检查
                    druidDataSource.setTestOnBorrow(false);
                    // 配置向连接池归还连接时，是否检查连接有效性，true每次都检查；false不检查
                    druidDataSource.setTestOnReturn(false);
                    log.info("SQLEngine init druidDataSource");
                }
            }
        }
        return druidDataSource.getConnection();
    }

    public List<Table> getAllTables() {
        try (Connection connection = getConnection()) {
            DatabaseMetaData metaData = connection.getMetaData();
            ResultSet tableResultSet = metaData.getTables(null, null, null, new String[] {"TABLE"});
            List<Table> tables = new ArrayList<>();
            while (tableResultSet.next()) {
                Table table = new Table();
                tables.add(table);

                String tableName = tableResultSet.getString("TABLE_NAME");
                table.setName(tableName);

                List<Column> columns = new ArrayList<>();
                table.setColumns(columns);

                List<ForeignKey> foreignKeys = new ArrayList<>();
                table.setForeignKeys(foreignKeys);

                ResultSet columnResultSet = metaData.getColumns(null, null, tableName, null);
                while(columnResultSet.next()) {
                    Column column = new Column();
                    String columnName = columnResultSet.getString("COLUMN_NAME");

//                    Integer dataType = columnResultSet.getInt("DATA_TYPE");
//                    JDBCType jdbcType = JDBCType.valueOf(dataType);

                    String typeName = columnResultSet.getString("TYPE_NAME");
//                    String tableCat = columnResultSet.getString("TABLE_CAT");
//                    String tableSchema = columnResultSet.getString("TABLE_SCHEM");
                    Integer columnSize = columnResultSet.getInt("COLUMN_SIZE");

                    Integer nullable = columnResultSet.getInt("NULLABLE");
                    String nullableStr = nullable.equals(DatabaseMetaData.columnNoNulls) ? "NOT NULL" : "";

                    String remarks = columnResultSet.getString("REMARKS");
//                    Integer charOctetLength = columnResultSet.getInt("CHAR_OCTET_LENGTH");
//                    String isNullableStr = columnResultSet.getString("IS_NULLABLE");
//                    String isAutoincrementStr = columnResultSet.getString("IS_AUTOINCREMENT");

                    column.setName(columnName);
                    column.setType(typeName);
                    column.setLength(columnSize.toString());
                    column.setDesc(remarks);
                    column.setNullable(nullableStr);
                    columns.add(column);
                }
                columnResultSet.close();

                ResultSet primaryKeyResultSet = metaData.getPrimaryKeys(null, null, tableName);
                while (primaryKeyResultSet.next()) {
                    PrimaryKey primaryKey = new PrimaryKey();
                    table.setPrimaryKey(primaryKey);

                    String columnName = primaryKeyResultSet.getString("COLUMN_NAME");
                    primaryKey.setColumnName(columnName);
                }
                primaryKeyResultSet.close();

                ResultSet foreignKeyResultSet = metaData.getImportedKeys(null, null, tableName);
                while (foreignKeyResultSet.next()) {
                    ForeignKey foreignKey = new ForeignKey();

                    String pktableName = foreignKeyResultSet.getString("PKTABLE_NAME");
                    String pkcolumnName = foreignKeyResultSet.getString("PKCOLUMN_NAME");
                    String fktableName = foreignKeyResultSet.getString("FKTABLE_NAME");
                    String fkcolumnName = foreignKeyResultSet.getString("FKCOLUMN_NAME");

                    foreignKey.setPktableName(pktableName);
                    foreignKey.setPkcolumnName(pkcolumnName);
                    foreignKey.setFktableName(fktableName);
                    foreignKey.setFkcolumnName(fkcolumnName);

                    foreignKeys.add(foreignKey);
                }
                foreignKeyResultSet.close();
            }
            tableResultSet.close();
            return tables;
        } catch (Throwable e) {
            log.error("getAllTables error", e);
        }
        return null;
    }

    public String getCreateTable(Table table, List<String> columnNames) {
        StringBuilder builder = new StringBuilder();
        builder.append(String.format("CREATE TABLE \"%s\" (\n", table.getName()));

        for (Column column : table.getColumns()) {
            if(columnNames != null && !columnNames.contains(column.getName())) {
                continue;
            }
            builder.append(String.format("\t\"%s\" %s %s %s, \n",
                    column.getName(), getFullType(column.getType(), column.getLength()), column.getNullable(),
                    !StringUtils.isEmpty(column.getDesc()) ? "COMMENT '" + column.getDesc() + "'" : ""));
        }
        if(table.getPrimaryKey() != null) {
            builder.append(String.format("\tPRIMARY KEY (\"%s\"), \n",
                    table.getPrimaryKey().getColumnName()));
        }
        for (ForeignKey foreignKey : table.getForeignKeys()) {
            builder.append(String.format("\tFOREIGN KEY(\"%s\") REFERENCES \"%s\" (\"%s\"), \n",
                    foreignKey.getFkcolumnName(), foreignKey.getPktableName(), foreignKey.getPkcolumnName()));
        }

        builder.append(")\n");

        return builder.toString();
    }

    public String getSampleRows(Table table, List<String> columnNames, Integer limit) {
        String sqlCmd = String.format("select %s from %s limit %d", table.getColumns().stream().map(column -> column.getName())
                .collect(Collectors.joining(", ")), table.getName(), limit);

        if(table.getColumns().size() == 0) {
            return "";
        }
        StringBuilder builder = new StringBuilder();
        builder.append("/*\n");

        builder.append(String.format("%d rows from %s table:\n", limit, table.getName()));
        for (Column column : table.getColumns()) {
            if(columnNames != null && !columnNames.contains(column.getName())) {
                continue;
            }
            builder.append(column.getName() + "\t");
        }
        builder.append("\n");

        try (Connection connection = getConnection()) {
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(sqlCmd);
            while (resultSet.next()) {
                for(int i = 0; i < table.getColumns().size(); i++) {
                    Column column = table.getColumns().get(i);
                    if(columnNames != null && !columnNames.contains(column.getName())) {
                        continue;
                    }
                    builder.append(resultSet.getString(i + 1) + "\t");
                }
                builder.append("\n");
            }
            resultSet.close();
        } catch (Throwable e) {
            log.error("getSampleRows error", e);
        }

        builder.append("*/\n");

        return builder.toString();
    }

    public ResultSet executeQuery(String sqlCmd) {
        try {
            Connection connection = getConnection();
            Statement statement = connection.createStatement();
            return statement.executeQuery(sqlCmd);
        } catch (Throwable e) {
            log.error("executeQuery error", e);
        }
        return null;
    }

    private String getFullType(String type, String length) {
        if(type.equals("INTEGER")) {
            return type;
        }
        return String.format("%s(%s)", type, length);
    }

    /**
     * 将查询出来的结果集转为表结构
     *
     * @param resultSet
     * @return
     * @throws SQLException
     */
    public static List<List<String>> resultSetToTable(ResultSet resultSet) throws SQLException {
        List<List<String>> table = new LinkedList();
        ResultSetMetaData rsmd = resultSet.getMetaData();
        int tlen = rsmd.getColumnCount();
        while(resultSet.next()){
            List<String> record = new LinkedList();
            for (int i = 1; i <= tlen; i++) {
                record.add(resultSet.getString(i));
            }
            table.add(record);
        }
        return table;
    }

//    /**
//     * 执行增删改操作
//     * @param sql SQL语句
//     * @param params 参数数组
//     * @return 返回操作的表的行数
//     */
//    public static int executeUpdate(String sql,String[] params){
////        DBUtils.close();
//        int count = 0;
//        try {
//            con = getConnection();
//            stm = con.prepareStatement(sql);
//            for (int i = 0; i < params.length; i++) {
//                stm.setString(i+1,params[i]);
//            }
//            count = stm.executeUpdate();
//        } catch (Exception e) {
//            e.printStackTrace();
//        }finally {
//            close();
//        }
//        return count;
//    }
//    /**
//     * 执行增删改操作
//     * @param sql SQL语句
//     * @param params 参数数组
//     * @return 返回操作的表的行数
//     */
//    public static int executeUpdate(String sql,String[] params,byte[] face_feature){
//        int count = 0;
//        try {
//            con = getConnection();
//            stm = con.prepareStatement(sql);
//            for (int i = 0; i < params.length; i++) {
//                stm.setString((i+1),params[i]);
//            }
//            stm.setBytes(7,face_feature);//默认第7个参数位置是特征值
//            count = stm.executeUpdate();
//        } catch (Exception e) {
//            e.printStackTrace();
//        }finally {
//            close();
//        }
//        return count;
//    }
//    /**
//     * 关闭所有资源
//     */
//    public static void close(){
//        if (rs!=null){
//            try {
//                rs.close();
//            } catch (SQLException e) {
//                e.printStackTrace();
//            }
//        }
//        if (stm!=null){
//            try {
//                stm.close();
//            } catch (SQLException e) {
//                e.printStackTrace();
//            }
//        }
//        if (stmt!=null){
//            try {
//                stmt.close();
//            } catch (SQLException e) {
//                e.printStackTrace();
//            }
//        }
//        if (con!=null){
//            try {
//                con.close();
//            } catch (SQLException e) {
//                e.printStackTrace();
//            }
//        }
//    }
}