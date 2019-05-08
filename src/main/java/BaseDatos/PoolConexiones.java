package BaseDatos;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;
import javax.sql.DataSource;
import org.apache.commons.dbcp2.BasicDataSourceFactory;

public class PoolConexiones {
	//BasicDataSource basicDataSource = new BasicDataSource();
	DataSource dataSource;
	
	public DataSource CrearConexiones() {	
		Properties propiedades = new Properties();
		
		try {
			propiedades.load(new FileInputStream("datasource_config.properties"));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			return null;
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
	
		try {
			dataSource = BasicDataSourceFactory.createDataSource(propiedades);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
		
		return dataSource;

	}
	
}
