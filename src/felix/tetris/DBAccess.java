package felix.tetris;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class DBAccess {

	private Connection connection;
	private Object[][] data;

	public Object[][] getData() {
		return this.data;
	}

	public void establishConnection() throws SQLException {
//		String dbPath = "jdbc:sqlite:highscores.db";
		String dbPath = "jdbc:sqlite:assets/highscores.db";
		this.connection = DriverManager.getConnection(dbPath);
	}
	
	public void closeConnection() throws SQLException { 
		this.connection.close();
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
		establishConnection();
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
		closeConnection();

	}

	public void insert(String name, int score) throws SQLException {
		establishConnection();
		String sql = "INSERT INTO tblHighscores(name,score) VALUES ('" + name + "','" + score + "')";
		Statement statement = this.connection.createStatement();
		statement.executeUpdate(sql);
		closeConnection();
	}

	public void deleteEverything() throws SQLException {
		establishConnection();
		String sql = "DELETE FROM tblHighscores";
		Statement statement = this.connection.createStatement();
		statement.executeUpdate(sql);
		closeConnection();

	}

}
