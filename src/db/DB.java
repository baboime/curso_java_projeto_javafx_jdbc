package db;

import java.io.FileInputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;

public class DB {
	
	private static Connection conexao = null;
	
	//iniciar conexão com o banco de dados
	public static Connection getConnection() {
		if (conexao == null) {
			try {
				Properties prop = carregarPropriedades();
				String url = prop.getProperty("dburl");
				conexao = DriverManager.getConnection(url, prop);
			}
			catch (SQLException e) {
				throw new DbException(e.getMessage());
			}
		}
		return conexao;
	}
	
	//encerrar conexão com o banco de dados
	public static void closeConnection() {
		if (conexao != null) {
			try {
				conexao.close();
			}
			catch (SQLException e) {
				throw new DbException(e.getMessage());
			}
		}
	}
	
	private static Properties carregarPropriedades() { 
		try (FileInputStream fs = new FileInputStream("db.properties")){
			Properties prop = new Properties();
			prop.load(fs);
			return prop;
		} 
		catch (IOException e) {
			throw new DbException(e.getMessage());
		}
	}
	
	public static void closeStatement(Statement st) {
		if (st != null) {
			try {
				st.close();
			} 
			catch (SQLException e) {
				throw new DbException(e.getMessage());
			}
		}
	}
	
	public static void closeResultSet(ResultSet rs) {
		if (rs != null) {
			try {
				rs.close();
			} 
			catch (SQLException e) {
				throw new DbException(e.getMessage());
			}
		}
	}
}