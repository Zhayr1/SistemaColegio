/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sistemacolegio;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;


public class Aula{
    private Integer classroom_id;
    private Integer limit;
    private String degree;
    private String section;
    private Integer currentStudents;
    private int sortVar;
    
    public Aula(Integer id,String degree, String section, Integer limit){
        this.classroom_id = id;
        this.degree = degree;
        this.section = section;
        this.limit = limit;
        this.currentStudents = 0;
        switch(degree){
            case "primero":
                sortVar = 1;
                break;
            case "segundo":
                sortVar = 2;
                break;
            case "tercero":
                sortVar = 3;
                break;
            case "cuarto":
                sortVar = 4;
                break;
            case "quinto":
                sortVar = 5;
                break;
            case "sexto":
                sortVar = 6;
                break;    
        }
    }
    
    public Integer getId(){
        return classroom_id;
    }
    
    public Integer getCurrentStudents(){
        return currentStudents;
    }
    public void addStudents(Integer i) {
        currentStudents += i;
    }
    
    public void save(Connection connection, Integer type)throws SQLException {
        try{
            PreparedStatement consulta;
            if(type == 0){
                consulta = connection.prepareStatement("INSERT INTO aulas (grado, seccion, capacidad_alumnos)" + " VALUES(?, ?, ?)");
                consulta.setString(1, this.degree);
                consulta.setString(2, this.section);
                consulta.setInt(3, limit);
            }else{
                consulta = connection.prepareStatement("UPDATE aulas SET grado = ?, seccion = ?, capacidad_alumnos = ? WHERE id_aula = ?");
                consulta.setString(1, this.degree);
                consulta.setString(2, this.section);
                consulta.setInt(3, limit);
                consulta.setInt(4, this.getId());
                System.out.println("sql update preparado");
            }
            consulta.executeUpdate();
            System.out.println("Consulta ejecutada ");
        }catch(SQLException ex){
            throw new SQLException(ex);
        }
    }
    
    public void delete(Connection connection) throws SQLException {
        try{
            PreparedStatement consulta = connection.prepareStatement("DELETE FROM aulas" + " WHERE id_aula = ?");
            consulta.setInt(1, this.getId());
            consulta.executeUpdate();
            System.out.println("Aula eliminada exitosamente");
        }catch(SQLException ex){
            throw new SQLException(ex);
        }
    }
    
    public String getSection(){
        return this.section;
    }
    public String getDegree(){
        return this.degree;
    }
    public Integer getLimit(){
        return this.limit;
    }
    
    @Override
    public String toString(){
        return degree+" - Seccion: "+ section;
    }

}
