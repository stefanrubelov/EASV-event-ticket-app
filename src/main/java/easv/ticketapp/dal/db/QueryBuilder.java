package easv.ticketapp.dal.db;

import easv.ticketapp.dal.db.connection.DatabaseConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class QueryBuilder {

    private String selectClause = "";
    private String fromClause = "";
    private final List<String> whereClauses = new ArrayList<>();
    private final List<Object> parameters = new ArrayList<>();
    private final List<String> joinClauses = new ArrayList<>();
    private final List<String> groupByClauses = new ArrayList<>();
    private String orderByClause = "";
    private Integer top = null;
    private final List<String> unionClauses = new ArrayList<>();
    private final List<String> setClauses = new ArrayList<>();
    private boolean delete = false;
    private boolean update = false;
    private boolean get = false;
    private boolean insert = false;
    private final ArrayList<String> insertColumnsPlaceholders;
    private final DatabaseConnection dbConnection;
    final private Logger logger = Logger.getAnonymousLogger();

    public QueryBuilder() {
        this.dbConnection = DatabaseConnection.getInstance();
        insertColumnsPlaceholders = new ArrayList<>();
    }

    /**
     * @param columns String...
     * @return QueryBuilder
     */
    public QueryBuilder select(String... columns) {
        if (this.selectClause == null) {
            this.selectClause = "";
        }

        if (columns.length == 0) {
            return this;
        }

        if (columns.length == 1 && columns[0].equals("*")) {
            this.selectClause = "*";
        } else {
            String joinedColumns = String.join(", ", columns);
            if (this.selectClause.isEmpty()) {
                this.selectClause = joinedColumns;
            } else {
                this.selectClause += ", " + joinedColumns;
            }
        }
        return this;
    }

    /**
     * @param table String
     * @return QueryBuilder
     */
    public QueryBuilder from(String table) {
        this.fromClause = table;
        return this;
    }

    /**
     * @param table String
     * @return QueryBuilder
     */
    public QueryBuilder table(String table) {
        this.fromClause = table;
        return this;
    }

    /**
     * @param column String
     * @param operator String
     * @param value Object
     * @return QueryBuilder
     */
    public QueryBuilder where(String column, String operator, Object value) {
        whereClauses.add(column + " " + operator + " ?");
        parameters.add(value);
        return this;
    }

    /**
     * @param column String
     * @param operator String
     * @param value Object
     * @return QueryBuilder
     */
    public QueryBuilder orWhere(String column, String operator, Object value) {
        // If this is the first where condition, treat it as a regular WHERE
        if (whereClauses.isEmpty()) {
            return where(column, operator, value);
        }

        whereClauses.add(column + " " + operator + " ?");
        parameters.add(value);

        return this;
    }

    /**
     * @param column String
     * @param values List<Integer>
     * @return QueryBuilder
     */
    public QueryBuilder whereIn(String column, List<Integer> values) {
        if (values == null || values.isEmpty()) {
            // log
        } else {
            String placeholders = String.join(", ", Collections.nCopies(values.size(), "?"));
            whereClauses.add(column + " IN (" + placeholders + ")");
            parameters.addAll(values);
        }

        return this;
    }

    /**
     * @param column String
     * @param subquery String
     * @param subqueryParams List<Integer>
     * @return QueryBuilder
     */
    public QueryBuilder whereInSubquery(String column, String subquery, List<Integer> subqueryParams) {
        if (subquery == null || subquery.isEmpty()) {
            throw new IllegalArgumentException("Subquery cannot be null or empty");
        }

        if (subqueryParams == null || subqueryParams.isEmpty()) {
            whereClauses.add("1 = 1");
        } else {
            whereClauses.add(column + " IN (" + subquery + ")");
            parameters.addAll(subqueryParams);
        }

        return this;
    }

    /**
     * @param table String
     * @param condition String
     * @param type String
     * @return QueryBuilder
     */
    public QueryBuilder join(String table, String condition, String type) {
        if (type.isEmpty() || type.equals(" ")) {
            this.joinClauses.add("JOIN " + table + " ON " + condition);
        } else {
            this.joinClauses.add(type.toUpperCase() + " JOIN " + table + " ON " + condition);
        }
        return this;
    }

    /**
     * @param table String
     * @param condition String
     * @return QueryBuilder
     */
    public QueryBuilder innerJoin(String table, String condition) {
        return join(table, condition, "INNER");
    }

    /**
     * @param table String
     * @param condition String
     * @return QueryBuilder
     */
    public QueryBuilder leftJoin(String table, String condition) {
        return join(table, condition, "LEFT");
    }

    /**
     * @param table String
     * @param condition String
     * @return QueryBuilder
     */
    public QueryBuilder rightJoin(String table, String condition) {
        return join(table, condition, "RIGHT");
    }

    /**
     * @param otherQuery QueryBuilder
     * @return QueryBuilder
     */
    public QueryBuilder union(QueryBuilder otherQuery) {
        this.unionClauses.add(otherQuery.build());
        return this;
    }

    /**
     * @param column String
     * @param direction String
     * @return QueryBuilder
     */
    public QueryBuilder orderBy(String column, String direction) {
        this.orderByClause = column + " " + direction.toUpperCase();
        return this;
    }

    /**
     * @param top int
     * @return QueryBuilder
     */
    public QueryBuilder top(int top) {
        this.top = top;
        return this;
    }

    /**
     * @param column String
     * @param value Object
     * @return QueryBuilder
     */
    public QueryBuilder set(String column, Object value) {
        String valueString;

        if (value == null) {
            valueString = "NULL";
        } else if (value instanceof String) {
            valueString = "'" + value + "'";
        } else if (value instanceof LocalDateTime) {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
            valueString = "'" + ((LocalDateTime) value).format(formatter) + "'";
        } else {
            valueString = value.toString();
        }

        setClauses.add(column + " = " + valueString);
        return this;
    }

    /**
     * @param column String
     * @param value Object
     * @param raw boolean
     * @return QueryBuilder
     */
    public QueryBuilder set(String column, Object value, boolean raw) {
        String valueString;

        if (raw && value instanceof String) {
            valueString = value.toString(); // Use raw SQL string
        } else if (value instanceof String) {
            valueString = "'" + value + "'";
        } else if (value instanceof LocalDateTime) {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
            valueString = "'" + ((LocalDateTime) value).format(formatter) + "'";
        } else {
            valueString = value.toString();
        }

        setClauses.add(column + " = " + valueString);
        return this;
    }

    /**
     * @param column String
     * @param value Object
     * @return QueryBuilder
     */
    public QueryBuilder insert(String column, Object value) {
        return insert(new String[]{column}, new Object[]{value});
    }

    /**
     * @param columns String[]
     * @param values Object[]
     * @return QueryBuilder
     */
    public QueryBuilder insert(String[] columns, Object[] values) {
        if (columns.length != values.length) {
            throw new IllegalArgumentException("Columns and values must have the same length.");
        }
        for (int i = 0; i < columns.length; i++) {
            insertColumnsPlaceholders.add(columns[i]);
        }

        for (Object value : values) {
            parameters.add(value);
        }

        return this;
    }

    /**
     * @param columns String...
     * @return QueryBuilder
     */
    public QueryBuilder groupBy(String... columns) {
        if (columns.length == 0) {
            return this;
        }

        String joinedColumns = String.join(", ", columns);
        groupByClauses.add(joinedColumns);
        return this;
    }

    /**
     * @return String
     */
    public String build() {
        if (fromClause.isEmpty()) {
            throw new IllegalStateException("FROM clause is required.");
        }

        StringBuilder query = new StringBuilder();

        if (delete) {
            query.append("DELETE ");

            if (top != null) {
                query.append("TOP ").append(top).append(" ");
            }
            query.append(selectClause).append(" FROM ").append(fromClause);

        } else if (update) {
            query.append("UPDATE ").append(fromClause).append(" SET ").append(String.join(", ", setClauses));
        } else if (get) {
            query.append("SELECT ");

            if (top != null) {
                query.append("TOP ").append(top).append(" ");
            }
            if (selectClause.isEmpty()) {
                query.append("*").append(" FROM ").append(fromClause);
            } else {
                query.append(selectClause).append(" FROM ").append(fromClause);
            }
        } else if (insert) {
            query.append("INSERT INTO ").append(fromClause).append(" (").append(String.join(", ", insertColumnsPlaceholders)).append(") VALUES (")
                    .append(String.join(", ", Collections.nCopies(parameters.size(), "?")))
                    .append(")");
        }

        if (!joinClauses.isEmpty()) {
            query.append(" ").append(String.join(" ", joinClauses));
        }

        if (!whereClauses.isEmpty()) {
            query.append(" WHERE ");

            if (whereClauses.size() > 1) {
                query.append("(").append(String.join(" OR ", whereClauses)).append(")");
            } else {
                query.append(whereClauses.get(0));
            }
        }

        if (!groupByClauses.isEmpty()) {
            query.append(" GROUP BY ").append(String.join(", ", groupByClauses));
        }

        if (!orderByClause.isEmpty()) {
            query.append(" ORDER BY ").append(orderByClause);
        }

        if (!unionClauses.isEmpty()) {
            query.append(" ").append(String.join(" UNION ", unionClauses));
        }

        return query.toString();
    }

    /**
     * @return boolean
     */
    public boolean save() {
        this.get = false;
        this.delete = false;
        this.update = false;
        this.insert = true;

        String sql = build();

        try {
            Connection connection = dbConnection.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(sql);

            for (int i = 0; i < parameters.size(); i++) {
                preparedStatement.setObject(i + 1, parameters.get(i));
            }
            return preparedStatement.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            logger.log(Level.SEVERE, e.getMessage(), e);
            return false;
        } finally {
            resetFields();
        }
    }

    /**
     * @return boolean
     */
    public ResultSet saveAndReturn() {
        this.get = false;
        this.update = false;
        this.delete = false;
        this.insert = true;

        if (insertColumnsPlaceholders.isEmpty()) {
            throw new IllegalStateException("Insert columns must be defined before calling saveAndReturn.");
        }

        StringBuilder sql = new StringBuilder();
        sql.append("INSERT INTO ").append(fromClause)
                .append(" (").append(String.join(", ", insertColumnsPlaceholders)).append(") ")
                .append("VALUES (").append(String.join(", ", Collections.nCopies(parameters.size(), "?"))).append("); ")
                .append("SELECT SCOPE_IDENTITY() AS id;");

        try {
            Connection connection = dbConnection.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(sql.toString());

            for (int i = 0; i < parameters.size(); i++) {
                preparedStatement.setObject(i + 1, parameters.get(i));
            }

            return preparedStatement.executeQuery();
        } catch (SQLException e) {
            e.printStackTrace();
            logger.log(Level.SEVERE, e.getMessage(), e);
            return null;
        } finally {
            resetFields();
        }
    }


    /**
     * @return ResultSet
     */
    public ResultSet get() {
        this.insert = false;
        this.update = false;
        this.delete = false;
        this.get = true;
        String sql = build();

        try {
            Connection connection = dbConnection.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(sql);

            for (int i = 0; i < this.parameters.size(); i++) {
                preparedStatement.setObject(i + 1, this.parameters.get(i));
            }
            return preparedStatement.executeQuery();
        } catch (SQLException e) {
            e.printStackTrace();
            logger.log(Level.SEVERE, e.getMessage(), e);
            return null;
        } finally {
            resetFields();
        }
    }

    /**
     * @return boolean
     */
    public boolean update() {
        this.insert = false;
        this.get = false;
        this.delete = false;
        this.update = true;
        this.set("updated_at", LocalDateTime.now());

        String sql = build();

        try {
            Connection connection = dbConnection.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(sql);

            for (int i = 0; i < parameters.size(); i++) {
                preparedStatement.setObject(i + 1, parameters.get(i));
            }

            int rowsAffected = preparedStatement.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            logger.log(Level.SEVERE, e.getMessage(), e);
            return false;
        } finally {
            resetFields();
        }
    }

    /**
     * @return boolean
     */
    public boolean delete() {
        this.insert = false;
        this.update = false;
        this.get = false;
        this.delete = true;

        String sql = build();

        try {
            Connection connection = dbConnection.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(sql);

            for (int i = 0; i < parameters.size(); i++) {
                preparedStatement.setObject(i + 1, parameters.get(i));
            }
            return preparedStatement.execute();
        } catch (SQLException e) {
            e.printStackTrace();
            logger.log(Level.SEVERE, e.getMessage(), e);
            return false;
        } finally {
            resetFields();
        }
    }

    public void resetFields() {
        selectClause = "";
        fromClause = "";
        whereClauses.clear();
        parameters.clear();
        joinClauses.clear();
        setClauses.clear();
        orderByClause = "";
        top = null;
        unionClauses.clear();
        insertColumnsPlaceholders.clear();
        groupByClauses.clear();
    }
}