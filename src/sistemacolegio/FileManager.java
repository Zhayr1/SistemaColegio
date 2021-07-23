/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sistemacolegio;

import java.awt.Component;
import java.io.File;
import java.io.FileWriter;
import java.io.PrintWriter;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.filechooser.FileNameExtensionFilter;

public class FileManager {
    
    public static void saveFile(String savePath, String text){
        FileWriter fichero = null;
        PrintWriter pw = null;
        try
        {
            fichero = new FileWriter(savePath);
            pw = new PrintWriter(fichero);
            pw.write(text);

        } catch (Exception ex) {
            System.out.println("Ocurrio un error al guardar el archivo: "+ex);
        } finally {
            try {
            // Nuevamente aprovechamos el finally para 
            // asegurarnos que se cierra el fichero.
            if (null != fichero){
                JOptionPane.showMessageDialog(null, "Archivo Guardado Exitosamente");
                fichero.close();
            }
            } catch (Exception ex) {
                System.out.println("Error inesperado: "+ex);
            }
        }        
    }
    public static void openFS(Component p, String text){
        JFileChooser fileChooser;
        fileChooser = new JFileChooser();
        fileChooser.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
        fileChooser.setApproveButtonText("Guardar");
        FileNameExtensionFilter fileFilter; 
        fileFilter = new FileNameExtensionFilter("Archivos de texto .txt", "txt");
        fileChooser.setFileFilter(fileFilter);
        int result = fileChooser.showOpenDialog(p);
        if (result != JFileChooser.CANCEL_OPTION) {
            File fileName = fileChooser.getSelectedFile();
            if ((fileName == null) || (fileName.getName().equals(""))) {
                //text.setText("...");
            } else {
                String fn = fileName.getAbsolutePath();
                if (!fn.endsWith(".txt")) {
                    fn += ".txt";
                }
                FileManager.saveFile(fn ,text);
            }
        };
    }
}
