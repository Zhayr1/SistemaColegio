/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sistemacolegio;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class Alumno {
    private int id;
    private int classroom_id;
    private int parent_id;
    private String firstName;
    private String lastName;
    private Date birthDate;
    
    
    public Alumno(Integer id,String firstName, String lastName, Date birthDate, Integer parent_id,Integer classroom_id){
        this.id = id;
        this.classroom_id = classroom_id;
        this.parent_id = parent_id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.birthDate = birthDate;
    }
    
    public Integer getId(){
        return id;
    }
    public Integer getClassroomId(){
        return classroom_id;
    }
    public String getFirstName(){
        return firstName;
    }
    public String getLastName(){
        return lastName;
    }
    public Date getBirthDate(){
        return birthDate;
    }
    public Integer getParentId(){
        return parent_id;
    }
    public void save(Connection connection, Integer type)throws SQLException {
        try{
            PreparedStatement consulta;
            if(type == 0){
                consulta = connection.prepareStatement("INSERT INTO alumnos (nombre, apellido, fecha_nacimiento, representante_cedula, aulas_id_aula)" + " VALUES(?, ?, ?, ?, ?)");
                consulta.setString(1, this.firstName);
                consulta.setString(2, this.lastName);
                consulta.setDate(3, birthDate);
                consulta.setInt(4, this.parent_id);
                consulta.setInt(5, this.classroom_id);
            }else{
                consulta = connection.prepareStatement("UPDATE alumnos" + " SET nombre = ?, apellido = ?, fecha_nacimiento = ?, representante_cedula = ?, aulas_id_aula = ? WHERE id_alumno = ?");
                consulta.setString(1, this.firstName);
                consulta.setString(2, this.lastName);
                consulta.setDate(3, birthDate);
                consulta.setInt(4, this.parent_id);
                consulta.setInt(5, this.classroom_id);
                consulta.setInt(6, this.getId());
            }
            consulta.executeUpdate();
            System.out.println("Consulta ejecutada exitosamente");
        }catch(SQLException ex){
            throw new SQLException(ex);
        }
    }
    
    public void delete(Connection connection) throws SQLException {
        try{
            PreparedStatement consulta = connection.prepareStatement("DELETE FROM alumnos" + " WHERE id_alumno = ?");
            consulta.setInt(1, this.id);
            consulta.executeUpdate();
            System.out.println("Alumno eliminado exitosamente");
        }catch(SQLException ex){
            throw new SQLException(ex);
        }
    }
    
    @Override
    public String toString(){
        return firstName + " " + lastName;
    }
}
