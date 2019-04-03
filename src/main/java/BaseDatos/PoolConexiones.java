package BaseDatos;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;

import javax.sql.DataSource;
import org.apache.commons.dbcp2.BasicDataSource;
import org.apache.commons.dbcp2.BasicDataSourceFactory;

public class PoolConexiones {
	//BasicDataSource basicDataSource = new BasicDataSource();
	DataSource dataSource;
	
	public DataSource CrearConexiones() {	
//		basicDataSource.setDriverClassName("com.mysql.cj.jdbc.Driver");
//		basicDataSource.setUsername("hr");
//		basicDataSource.setPassword("PepeJeans");
//		basicDataSource.setUrl("jdbc:mysql://localhost/termibus?useUnicode=true&useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&serverTimezone=Europe/Madrid");
		Properties propiedades = new Properties();
		try {
			propiedades.load(new FileInputStream("src/main/config/datasource_config.properties"));
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	
		try {
			dataSource = BasicDataSourceFactory.createDataSource(propiedades);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return dataSource;

	}
	
	public ResultSet generarConsulta(Connection con, String query) {
		Statement st = null;
		ResultSet rs = null;
		
		try {
			st = con.createStatement();
			rs = st.executeQuery(query);		
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return rs;
	}
}
