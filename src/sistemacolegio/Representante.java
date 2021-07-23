/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sistemacolegio;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;


public class Representante {
    
    private Integer id;
    private String firstName;
    private String lastName;
    private String cell;
    private String direction;
    
    public Representante(Integer id, String firstName, String lastName, String cell, String direction) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.cell = cell;
        this.direction = direction;
    }
    
    public Integer getId(){
        return id;
    }
    public String getFirstName(){
        return firstName;
    }
    public String getLastName(){
        return lastName;
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
                consulta = connection.prepareStatement("INSERT INTO representantes" + " VALUES(?, ?, ?, ?, ?)");
                consulta.setInt(1, this.getId());
                consulta.setString(2, this.firstName);
                consulta.setString(3, this.lastName);
                consulta.setString(4, this.cell);
                consulta.setString(5, direction);
            }else{
                consulta = connection.prepareStatement("UPDATE representantes" + " SET cedula = ?, nombre = ?, apellido = ?, telefono = ?, direccion = ? WHERE cedula = ?");
                consulta.setInt(1, this.getId());
                consulta.setString(2, this.firstName);
                consulta.setString(3, this.lastName);
                consulta.setString(4, this.cell);
                consulta.setString(5, direction);
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
            PreparedStatement consulta = connection.prepareStatement("DELETE FROM representantes" + " WHERE cedula = ?");
            consulta.setInt(1, this.getId());
            consulta.executeUpdate();
            System.out.println("Representante eliminado exitosamente");
        }catch(SQLException ex){
            throw new SQLException(ex);
        }
    }
    
    @Override
    public String toString(){
        return "Cedula: "+id +" - Nombre: "+ firstName + " - Apellido: "+lastName + " - Direccion:"+ direction + " - Telefono: "+cell;
    }
}
