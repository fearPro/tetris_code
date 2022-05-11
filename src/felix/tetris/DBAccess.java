package felix.tetris;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class DBAccess {

	final private Connection connection;
	private Object[][] data;

	public DBAccess() throws SQLException {
		this.connection = establishConnection();
	}

	public Object[][] getData() {
		return this.data;
	}

	public Connection establishConnection() throws SQLException {
		String dbPath = "jdbc:sqlite:highscores.db";
		return DriverManager.getConnection(dbPath);
	}

	private int rowCounter() throws SQLException {
		String sql = "SELECT count(*) FROM tblHighscores";
		Statement statement = this.connection.createStatement();
		ResultSet result = statement.executeQuery(sql);
		result.next();

		System.out.println(result.getInt(1));
		return result.getInt(1);
	}

	public void selectAll() throws SQLException {
		data = new String[rowCounter()][2];

		String sql = "SELECT * FROM tblHighscores";
		Statement statement = this.connection.createStatement();
		ResultSet result = statement.executeQuery(sql);

		int z = 0;
		while (result.next()) {
			data[z][0] = result.getString("name");

			int score = result.getInt("score");
			data[z][1] = score + "";
			z++;
		}

	}

	public void insert(String name, int score) throws SQLException {
		String sql = "INSERT INTO tblHighscores(name,score) VALUES ('" + name + "','" + score + "')";
		Statement statement = this.connection.createStatement();
		statement.executeUpdate(sql);
	}

	public void deleteEverything() throws SQLException {
		String sql = "DELETE FROM tblHighscores";
		Statement statement = this.connection.createStatement();
		statement.executeUpdate(sql);

	}
	
	public void closeConnection() throws SQLException { 
		this.connection.close();
	}

}
