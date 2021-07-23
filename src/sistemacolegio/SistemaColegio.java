/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sistemacolegio;

import java.sql.SQLException;


public class SistemaColegio {

    /**
     * @param args the command line arguments
     * @throws java.sql.SQLException
     * @throws java.lang.ClassNotFoundException
     */
    public static void main(String[] args)  throws SQLException, ClassNotFoundException {
        Gui root = new Gui(800,600);
        root.setVisible(true);
    }
}
