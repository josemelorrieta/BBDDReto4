package BaseDatos;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.sql.DataSource;
import org.apache.commons.dbcp2.BasicDataSource;

public class PoolConexiones {
	BasicDataSource basicDataSource = new BasicDataSource();
	
	public DataSource CrearConexiones() {	
		basicDataSource.setDriverClassName("com.mysql.cj.jdbc.Driver");
		basicDataSource.setUsername("hr");
		basicDataSource.setPassword("PepeJeans");
		basicDataSource.setUrl("jdbc:mysql://localhost/termibus?useUnicode=true&useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&serverTimezone=Europe/Madrid");
	
		// Opcional. Sentencia SQL que le puede servir a BasicDataSource
		// para comprobar que la conexion es correcta.
		basicDataSource.setValidationQuery("select 1");
		
		return basicDataSource;

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
