package co.com.uniandes.arquitectura.jdbc.connection;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;


public final class  OracleJDBCConnection {
	
    public Connection conn;
    private Statement statement;
    public static OracleJDBCConnection db;
    private OracleJDBCConnection() {
        String url= "jdbc:oracle:thin:@localhost:1521:xe";
        String driver = "oracle.jdbc.driver.OracleDriver";
        String userName = "svebanistmo";
        String password = "abc123";
        try {
            Class.forName(driver).newInstance();
            this.conn = (Connection)DriverManager.getConnection(url,userName,password);
        }
        catch (Exception sqle) {
            sqle.printStackTrace();
        }
    }
    /**
     *
     * @return MysqlConnect Database connection object
     */
    public static synchronized OracleJDBCConnection getDbCon() {
        if ( db == null ) {
            db = new OracleJDBCConnection();
        }
        return db;
 
    }
    /**
     *
     * @param query String The query to be executed
     * @return a ResultSet object containing the results or null if not available
     * @throws SQLException
     */
    public ResultSet query(String query) throws SQLException{
        statement = db.conn.createStatement();
        ResultSet res = statement.executeQuery(query);
        return res;
    }
    /**
     * @desc Method to insert data to a table
     * @param insertQuery String The Insert query
     * @return boolean
     * @throws SQLException
     */
    public int  insert(String insertQuery){
        
        int result = 0;
		try {
			if(statement != null && !statement.isClosed())statement.close();
			statement = db.conn.createStatement();
			result = statement.executeUpdate(insertQuery);
			statement.close();
			return result;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			try {
				statement.close();
			} catch (SQLException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}finally {
			
		}
        return result;
 
    }
 
}
