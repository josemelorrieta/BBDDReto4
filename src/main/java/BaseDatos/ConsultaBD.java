package BaseDatos;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.sql.DataSource;

public class ConsultaBD {
	
	PoolConexiones pool = new PoolConexiones();
	DataSource datasource;
	Connection con = null;
	 
	public String consultarToGson(String consulta) {
		datasource = pool.CrearConexiones();
		String resultado = "[";
		try {
			con = datasource.getConnection();
		    Statement st = con.createStatement();
		    ResultSet rs = st.executeQuery(consulta);
		    int numColumnas = rs.getMetaData().getColumnCount();
		    if(rs.isBeforeFirst()) {
			    while (rs.next()) {
			    	resultado += "{\"";
			    	for (int i=1;i<=numColumnas;i++) {
			    		if (i!=1)
			    			resultado += ",\"";
			    		if (rs.getMetaData().getColumnTypeName(i).equals("VARCHAR")) {
			    			resultado += rs.getMetaData().getColumnLabel(i) + "\":\"";
			    			resultado += rs.getString(i) + "\"";
			    		} else {
			    			resultado += rs.getMetaData().getColumnLabel(i) + "\":";
			    			resultado += rs.getString(i);
			    		}
			    		if (i==numColumnas)
			    			resultado += "},";
			    	}
			    }
			    
			    return resultado.substring(0, resultado.length()-1) + "]";
		    } else {
		    	return "";
		    }
		    
		} catch (SQLException e) {
			e.printStackTrace();
		    return null;
		} finally {
			try {
				if(con!=null)
					con.close();
			} catch (SQLException e) {
				e.printStackTrace();
				return null;
			}
		}
	}
}
