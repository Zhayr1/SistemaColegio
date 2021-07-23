/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sistemacolegio;

import java.sql.*;
import java.util.ArrayList;

public class DBM {
    private static final String DB_NAME = "colegio";
    private static final String DB_USER = "root";
    private static final String DB_PASS = "";
    private Connection cnx = null;
    
    
    public DBM() throws SQLException, ClassNotFoundException {
        this.startConnection();
    }
   
    public void startConnection() throws SQLException, ClassNotFoundException {
      if (cnx == null) {
         try {
            Class.forName("com.mysql.jdbc.Driver");
            cnx = (Connection) DriverManager.getConnection("jdbc:mysql://localhost/"+DB_NAME, DB_USER, DB_PASS);
             System.out.println("Conexion establecida");
         } catch (SQLException ex) {
            throw new SQLException(ex);
         } catch (ClassNotFoundException ex) {
            throw new ClassCastException(ex.getMessage());
         }
      }
    }
   
    public Connection getConnection() {
       return cnx;
    }
   
    public void closeConnection() throws SQLException {
      if (cnx != null) {
         cnx.close();
      }
    }
   
    public ArrayList<Docente> consultar_docentes(Connection connection, Integer id) throws SQLException{
        ArrayList<Docente> list = new ArrayList();
        try{
            PreparedStatement consulta;
            if(id != null){
                consulta = connection.prepareStatement("SELECT * FROM " + "docentes WHERE cedula = "+id);
                ResultSet r = consulta.executeQuery();
                while(r.next()){
                    list.add(new Docente(r.getInt("cedula"), r.getString("nombre"), r.getString("apellido"), r.getDate("fecha_nacimiento"), r.getString("direccion"), r.getString("telefono"), r.getInt("aulas_id_aula")));
                }
            }else {
                consulta = connection.prepareStatement("SELECT * FROM " + "docentes");
                ResultSet r = consulta.executeQuery();
                while(r.next()){
                    list.add(new Docente(r.getInt("cedula"), r.getString("nombre"), r.getString("apellido"), r.getDate("fecha_nacimiento"), r.getString("direccion"), r.getString("telefono"), r.getInt("aulas_id_aula")));
                }
            }
        }catch(SQLException ex){
            throw new SQLException(ex);
        }
        return list;
    }
    public ArrayList<Aula> consultar_aulas(Connection connection, Integer id) throws SQLException{
        ArrayList<Aula> list = new ArrayList();
        try{
            PreparedStatement consulta;
            if(id != null){
                consulta = connection.prepareStatement("SELECT * FROM " + "aulas WHERE id_aula = "+id);
                ResultSet r = consulta.executeQuery();
                while(r.next()){
                    list.add(new Aula(r.getInt("id_aula"), r.getString("grado"), r.getString("seccion"), r.getInt("capacidad_alumnos")));
                }
            }else {
                consulta = connection.prepareStatement("SELECT * FROM " + "aulas ORDER BY seccion");
                ResultSet r = consulta.executeQuery();
                while(r.next()){
                    list.add(new Aula(r.getInt("id_aula"), r.getString("grado"), r.getString("seccion"), r.getInt("capacidad_alumnos")));
                }
            }
        }catch(SQLException ex){
            throw new SQLException(ex);
        }
        return list;
    }
    public ArrayList<Representante> consultar_representantes(Connection connection, Integer id) throws SQLException{
        ArrayList<Representante> list = new ArrayList();
        try{
            PreparedStatement consulta;
            if(id != null){
                consulta = connection.prepareStatement("SELECT * FROM " + "representantes WHERE cedula = "+id);
                ResultSet r = consulta.executeQuery();
                while(r.next()){
                    list.add(new Representante(r.getInt("cedula"), r.getString("nombre"), r.getString("apellido"), r.getString("telefono"), r.getString("direccion")));
                }
            }else {
                consulta = connection.prepareStatement("SELECT * FROM " + "representantes");
                ResultSet r = consulta.executeQuery();
                while(r.next()){
                    list.add(new Representante(r.getInt("cedula"), r.getString("nombre"), r.getString("apellido"), r.getString("telefono"), r.getString("direccion")));
                }
            }
        }catch(SQLException ex){
            throw new SQLException(ex);
        }
        return list;
    }
    public ArrayList<Alumno> consultar_alumnos(Connection connection, Integer id) throws SQLException{
        ArrayList<Alumno> list = new ArrayList();
        try{
            PreparedStatement consulta;
            if(id != null){
                consulta = connection.prepareStatement("SELECT * FROM " + "alumnos WHERE id_alumno = "+id);
                ResultSet r = consulta.executeQuery();
                while(r.next()){
                    list.add(new Alumno(r.getInt("id_alumno"), r.getString("nombre"), r.getString("apellido"), r.getDate("fecha_nacimiento"), r.getInt("representante_cedula"), r.getInt("aulas_id_aula")));
                }
            }else {
                consulta = connection.prepareStatement("SELECT * FROM " + "alumnos");
                ResultSet r = consulta.executeQuery();
                while(r.next()){
                    list.add(new Alumno(r.getInt("id_alumno"), r.getString("nombre"), r.getString("apellido"), r.getDate("fecha_nacimiento"), r.getInt("representante_cedula"), r.getInt("aulas_id_aula")));
                }
            }
        }catch(SQLException ex){
            throw new SQLException(ex);
        }
        return list;
    }
}