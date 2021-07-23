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

/**
 *
 * @author Js
 */
public class Docente {
    
    private int id;
    private int classroom_id;
    private String firstName;
    private String lastName;
    private Date birthDate;
    private String direction;
    private String cell;
    
    
    public Docente(Integer id,String firstName, String lastName, Date birthDate, String direction, String cell, Integer classroom_id){
        this.id = id;
        this.classroom_id = classroom_id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.birthDate = birthDate;
        this.direction = direction;
        this.cell = cell;
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
    public String getDirection(){
        return direction;
    }
    public String getCell(){
        return cell;
    }
    public void save(Connection connection, Integer type)throws SQLException {
        try{
            PreparedStatement consulta;
            if(type == 0){
                consulta = connection.prepareStatement("INSERT INTO docentes" + " VALUES(?, ?, ?, ?, ?, ?, ?)");
                consulta.setInt(1, this.getId());
                consulta.setString(2, this.firstName);
                consulta.setString(3, this.lastName);
                consulta.setDate(4, birthDate);
                consulta.setString(5, this.direction);
                consulta.setString(6, this.cell);
                consulta.setInt(7, this.classroom_id);
            }else{
                consulta = connection.prepareStatement("UPDATE docentes" + " SET cedula = ?, nombre = ?, apellido = ?, fecha_nacimiento = ?, direccion = ?, telefono = ?, aulas_id_aula = ? WHERE cedula = ?");
                consulta.setInt(1, this.getId());
                consulta.setString(2, this.firstName);
                consulta.setString(3, this.lastName);
                consulta.setDate(4, birthDate);
                consulta.setString(5, this.direction);
                consulta.setString(6, this.cell);
                consulta.setInt(7, this.classroom_id);
                consulta.setInt(8, this.getId());
            }
            consulta.executeUpdate();
            System.out.println("Consulta ejecutada exitosamente");
        }catch(SQLException ex){
            throw new SQLException(ex);
        }
    }
    
    public void delete(Connection connection) throws SQLException {
        try{
            PreparedStatement consulta = connection.prepareStatement("DELETE FROM docentes" + " WHERE cedula = ?");
            consulta.setInt(1, this.id);
            consulta.executeUpdate();
            System.out.println("Docente eliminado exitosamente");
        }catch(SQLException ex){
            throw new SQLException(ex);
        }
    }
    
    @Override
    public String toString(){
        return firstName +" "+lastName+" Cedula: "+id;
    }
    
    public String toString2(){
        return "Cedula: "+id +" - Nombre: "+ firstName + " - Apellido: "+lastName + " - Direccion:"+ direction + " - Telefono: "+cell+" Fecha de cumpleaños: "+birthDate;
    }
}
