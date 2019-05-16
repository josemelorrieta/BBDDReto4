package BaseDatos;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import javax.sql.DataSource;
import java.sql.CallableStatement;

public class ConsultaBD {

	private PoolConexiones pool;
	private DataSource datasource;
	private Connection con;

	public ConsultaBD() {
		pool = new PoolConexiones();
		datasource = pool.CrearConexiones();
		con = null;
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
				statementGenerico.execute();
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
	 * Prepara el statement para su insercion a la base de datos organizando cada
	 * objeto de la forma que le corresponde
	 * 
	 * @param objetos array de Object
	 * @param clases  array de clases, debe ser paralelo a objetos
	 * @param query   query que se quiere ejecutar
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
			}
			return statementGenerico;
		} catch (SQLException e) {
			return null;
		}
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

	/**
	 * Llama al procedimiento de la base de datos para guardar la reserva. Al
	 * guardar en dos tablas el procemiento almacenado hace un rollback si hay algun
	 * error
	 * 
	 * @param idRsv    ID de la reserva
	 * @param dni      DNI del cliente
	 * @param fechaRsv Fecha en la que se realiza la reserva
	 * @param fechain  Fecha de entrada de la reserva
	 * @param fechaOut Fecha de salida de la reserva
	 * @param precio   Precio de la reserva
	 * @param idHab    ID de la habitacion reservada
	 * @return booleano de como ha ido el proceso.
	 */
	public boolean guardarReserva(int idRsv, String dni, String fechaRsv, String fechaIn, String fechaOut, double precio, int id, String tipo) {
		try {
			con = datasource.getConnection();

			CallableStatement cst = con.prepareCall("{call guardar_reserva (" + idRsv + ", '" + dni + "', '" + fechaRsv + "', '" + fechaIn + "', '" + fechaOut + "', " + precio + ", " + id + ", '" + tipo + "')}");
			return cst.execute();

		} catch (SQLException e) {
			return false;
		}
	}

	// "'dni' = '12345678R'" <-- Ejemplo de una condicion
	public boolean deleteGenerico(String tabla, String[] condiciones) {
		String statement = "DELETE FROM " + tabla + " WHERE ";
		for (String condi : condiciones) {
			statement += condi + " AND ";
		}
		statement = statement.substring(0, statement.length() - 5);

		try {
			PreparedStatement statementGenerico = this.con.prepareStatement(statement);
			return statementGenerico.execute();
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return false;
	}
}
