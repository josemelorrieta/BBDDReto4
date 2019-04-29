package BaseDatos;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.sql.DataSource;

public class ConsultaBD {

	PoolConexiones pool = new PoolConexiones();
	DataSource datasource;
	Connection con = null;

	public ConsultaBD() {
		datasource = pool.CrearConexiones();
	}

	public String consultarToGson(String consulta) {
		String resultado = "[";
		try {
			con = datasource.getConnection();
			Statement st = con.createStatement();
			ResultSet rs = st.executeQuery(consulta);
			int numColumnas = rs.getMetaData().getColumnCount();
			if (rs.isBeforeFirst()) {
				while (rs.next()) {
					resultado += "{\"";
					for (int i = 1; i <= numColumnas; i++) {
						if (i != 1)
							resultado += ",\"";
						if (rs.getMetaData().getColumnTypeName(i).equals("VARCHAR")) {
							resultado += rs.getMetaData().getColumnLabel(i) + "\":\"";
							resultado += rs.getString(i) + "\"";
						} else {
							resultado += rs.getMetaData().getColumnLabel(i) + "\":";
							resultado += rs.getString(i);
						}
						if (i == numColumnas)
							resultado += "},";
					}
				}

				return resultado.substring(0, resultado.length() - 1) + "]";
			} else {
				return "";
			}

		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		} finally {
			try {
				if (con != null)
					con.close();
			} catch (SQLException e) {
				e.printStackTrace();
				return null;
			}
		}
	}

	/**
	 * Insert generico a la base de datos
	 * 
	 * @param objetos     array de Object, si se quiere meter un objeto se debe
	 *                    transformar en un array de Object, no pueden meterse
	 *                    matrizes, utilizar un for para eso
	 * @param nombreTabla nombre de la tabla a la cual se desea realizar un insert
	 * 
	 * @return booleano que indica si se han guardado bien los registros
	 */
	public boolean insertGenerico(Object[] objetos, String nombreTabla) {
		try {
			con = datasource.getConnection();
			Class[] clasesObj = arrayClases(objetos);
			String query = prepararQuery(objetos.length, nombreTabla);
			PreparedStatement statementGenerico = generarStatement(objetos, clasesObj, query);
			if (statementGenerico != null) {
				statementGenerico.executeUpdate();
			}
			return true;
		} catch (SQLException e1) {
			return false;
		} finally {
			try {
				if (con != null)
					con.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * Prepara el statement para su insercion a la base de datos organizando cada objeto de la forma que le corresponde
	 * @param objetos array de Object
	 * @param clases  array de clases, debe ser paralelo a objetos
	 * @param query	  query que se quiere ejecutar
	 * @return Prepared statement listo para ejecutar
	 */
	public PreparedStatement generarStatement(Object[] objetos, Class[] clases, String query) {
		try {
			PreparedStatement statementGenerico = this.con.prepareStatement(query);
			for (int i = 0; i < objetos.length; i++) {
				if (clases[i] == String.class) {
					statementGenerico.setString(i + 1, (String) objetos[i]);
				} else if (clases[i] == Float.class) {
					statementGenerico.setFloat(i + 1, (Float) objetos[i]);
				} else if (clases[i] == Double.class) {
					statementGenerico.setDouble(i + 1, (Double) objetos[i]);
				} else if (clases[i] == Integer.class) {
					statementGenerico.setInt(i + 1, (int) objetos[i]);
				} else if (clases[i] == java.util.Date.class) {
					statementGenerico.setDate(i + 1, new java.sql.Date(((java.util.Date) objetos[i]).getTime()));
				} else {
					statementGenerico.setString(i + 1, (String) objetos[i]);
				}
				return statementGenerico;
			}
		} catch (SQLException e) {
			return null;
		}
		return null;
	}

	/**
	 * Crea un array de clases paralelo al array que se le pasa
	 * 
	 * @param objetos
	 * @return array de clases
	 */
	public Class[] arrayClases(Object[] objetos) {
		Class[] clasesObj = new Class[objetos.length];
		for (int i = 0; i < objetos.length; i++) {
			clasesObj[i] = objetos[i].getClass();
		}
		return clasesObj;
	}

	/**
	 * Prepara el query
	 * 
	 * @param num
	 * @param tabla
	 * @return
	 */
	public String prepararQuery(int num, String tabla) {
		String query = "insert into " + tabla + " values(";
		for (int i = 0; i < num; i++) {
			query += "?,";
		}
		query = (query.substring(0, query.length() - 1)) + ");";
		return query;
	}
}
