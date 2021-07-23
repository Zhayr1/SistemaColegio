/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sistemacolegio;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.sql.Connection;
import java.sql.Date;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Objects;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.DefaultListSelectionModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.table.DefaultTableModel;
import org.jdatepicker.impl.JDatePanelImpl;
import org.jdatepicker.impl.JDatePickerImpl;
import org.jdatepicker.impl.UtilDateModel;


public class Gui extends JFrame {
    
    private final int width = 800;
    private final int height = 600;
    
    private JButton newClassRoom = new JButton("Añadir Aula");
    private JButton editClassRoom = new JButton("Modificar / Eliminar Aula");
    private JButton newTeacher = new JButton("Añadir Docente");
    private JButton editTeacher = new JButton("Modificar / Eliminar Docente");
    private JButton newParent = new JButton("Añadir Representante");
    private JButton editParent = new JButton("Modificar / Eliminar Repesentante");
    private JButton newStudent = new JButton("Añadir Alumno");
    private JButton editStudent = new JButton("Modificar / Eliminar Alumno");
    private JButton reportsButton = new JButton("Reportes");
    private DBM db;
    private Connection cnx;
    private JTable table;
    private DefaultTableModel model;
    
    ArrayList<Aula> lista_aulas;
    ArrayList<Docente> lista_docentes;
    ArrayList<Alumno> lista_alumnos;
    ArrayList<Representante> lista_representantes;
    
    
    public Gui(int width, int height) throws SQLException{
        this.setSize(width, height);
        this.setResizable(false);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setLocationByPlatform(true);
        this.setTitle("Sistema de Inscripciones");
        this.setLocation(200, 200);
        JPanel mainPanel = new JPanel();
        JPanel firstPanel = new JPanel();
        String[] columnNames = { "Seccion", "Grado", "Capacidad","Docente" };
        model = new DefaultTableModel(null, columnNames);
        table = new JTable(model);
        DefaultListSelectionModel sm = new DefaultListSelectionModel();
        sm.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        table.setSelectionModel(sm);
        mainPanel.add(firstPanel);
        this.add(mainPanel);
        
        
        try{
            db = new DBM();
            cnx = db.getConnection();
            lista_aulas = db.consultar_aulas(cnx, null);
            lista_docentes = db.consultar_docentes(cnx, null);
            lista_representantes = db.consultar_representantes(cnx, null);
            lista_alumnos = db.consultar_alumnos(cnx, null);
        } catch (SQLException ex) {
            throw new SQLException(ex);
        } catch (ClassNotFoundException ex) {
            throw new ClassCastException(ex.getMessage());
        }
        this.updateClassRoomsCaps();
        this.initComponentsFirstPane(firstPanel);
        this.initListeners();
        this.pack();
        
    }

    private void initListeners(){
        //Nuevo
        newClassRoom.addActionListener((ActionEvent e) -> {
            this.add_classroom(this);
        });
    }

    private void updateClassRoomsCaps(){
        for (int i = 0; i < lista_aulas.size(); i++) {
            for (int j = 0; j < lista_alumnos.size(); j++) {
                if(Objects.equals(lista_alumnos.get(j).getClassroomId(), lista_aulas.get(i).getId())){
                    lista_aulas.get(i).addStudents(1);
                }
            }
        }
    }
    
    private void editButton(JButton b) {
        b.setBackground(new Color(59, 89, 182));
        b.setForeground(Color.WHITE);
        b.setFocusPainted(false);
        b.setFont(new Font("Tahoma", Font.BOLD, 12));
        b.setMargin(new Insets(5, 0, 5, 0));
    }
    
    private void editButton(JButton b,Color bg_color,Color fg_color) {
        b.setBackground(bg_color);
        b.setForeground(fg_color);
        b.setFocusPainted(false);
        b.setFont(new Font("Tahoma", Font.BOLD, 12));
        b.setMargin(new Insets(5, 0, 5, 0));
    }
    
    private void updateTableValues(){
        model.setRowCount(0);
        try {
            lista_aulas = db.consultar_aulas(db.getConnection(), null);
            this.updateClassRoomsCaps();
            model.setRowCount(0);
        } catch (SQLException ex) {
            Logger.getLogger(Gui.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        for (int i = 0; i < lista_aulas.size(); i++) {
            Aula a = lista_aulas.get(i);
            boolean teacher = false;
            for (int j = 0; j < lista_docentes.size(); j++) {
                if (Objects.equals(lista_docentes.get(j).getClassroomId(), a.getId())) {
                    teacher = true;
                }
            }
            if(teacher){
                Object[] aux = {a.getSection() , a.getDegree(), a.getCurrentStudents() +" / "+ a.getLimit(), "Si" };
                model.addRow(aux);
            }else{
                Object[] aux = {a.getSection() , a.getDegree(), a.getCurrentStudents() +" / "+ a.getLimit(), "No" };
                model.addRow(aux);
            }
        }
    }
    
    private void reports(JFrame parent_panel){
        final JDialog frame = new JDialog( parent_panel, "Generacion de reportes", true);
        frame.setLocation(300, 300);
        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(6,2));
        //componentes
        JButton classroomBtn = new JButton("Reporte Aulas");
        JButton teacherBtn = new JButton("Reporte Docentes");
        JButton parentBtn = new JButton("Reporte Representantes");
        JButton studentBtn = new JButton("Reporte Alumnos");
        JButton cancelBtn = new JButton("Cerrar");
        
        this.editButton(classroomBtn);
        this.editButton(teacherBtn);
        this.editButton(parentBtn);
        this.editButton(studentBtn);
        this.editButton(cancelBtn);
        panel.add(new JLabel("Reporte de Aulas registradas"));
        panel.add(classroomBtn);
        panel.add(new JLabel("Reporte de Docentes registrados"));
        panel.add(teacherBtn);
        panel.add(new JLabel("Reporte de Representantes registrados"));
        panel.add(parentBtn);
        panel.add(new JLabel("Reporte de Alumnos inscritos"));
        panel.add(studentBtn);
        panel.add(new JLabel(""));
        panel.add(new JLabel(""));
        panel.add(new JLabel(""));
        panel.add(cancelBtn);
        //Listeners
        classroomBtn.addActionListener((ActionEvent e) -> {
            int input = JOptionPane.showConfirmDialog(null, "Desea que el reporte incluya el docente asignado y los alumnos inscritos en cada seccion?");
            String text = "REPORTE DE AULAS REGISTRADAS EN EL SISTEMA\n"+
                    "---------------------------------------------------------------------------------------------"+"\nTotal de Aulas en el sistema: "+lista_aulas.size()+
                    "\nG"
                    + "RADO ------ SECCION ---- CAPACIDAD\n";
            if(input == 0){
                for (int i = 0; i < lista_aulas.size(); i++) {
                    Aula a = lista_aulas.get(i);
                    text = text.concat(a.toString() +" -- "+ a.getCurrentStudents() + " / "+a.getLimit()+"\n");
                    for (int j = 0; j < lista_docentes.size(); j++) {
                        Docente d = lista_docentes.get(j);
                        if (Objects.equals(d.getClassroomId(), a.getId())) {
                            text = text.concat("Docente: "+d.toString() +"\n");
                        }
                    }
                    for (int j = 0; j < lista_alumnos.size(); j++) {
                        Alumno al = lista_alumnos.get(j);
                        if (Objects.equals(al.getClassroomId(), a.getId())) {
                            text = text.concat("-> "+al.toString() +"\n");
                        }
                        
                    }
                }
            }else {
                for (int i = 0; i < lista_aulas.size(); i++) {
                    Aula a = lista_aulas.get(i);
                    text = text.concat(a.toString() +" -- "+ a.getCurrentStudents() + " / "+a.getLimit()+"\n");
                }
            }
            
            if(input != 2){
                FileManager.openFS(this, text);
            }
        });
        teacherBtn.addActionListener((ActionEvent e) -> {
            String text = "REPORTE DE DOCENTES REGISTRADOS EN EL SISTEMA\n"+
                "---------------------------------------------------------------------------------------------\nTotal de docentes en el sistema: "+lista_docentes.size()+"\n";
            for (int i = 0; i < lista_docentes.size(); i++) {
                Docente d = lista_docentes.get(i);
                text = text.concat("Docente: "+d.toString2());
                for (int j = 0; j < lista_aulas.size(); j++) {
                    Aula a = lista_aulas.get(j);
                    if (Objects.equals(a.getId(), d.getClassroomId())) {
                        text = text.concat("-- Seccion: "+a.getSection()+"\n");
                    }
                }
            }
            FileManager.openFS(this, text);
        });
        studentBtn.addActionListener((ActionEvent e) -> {
            String text = "REPORTE DE ALUMNOS REGISTRADOS EN EL SISTEMA\n"+
                "---------------------------------------------------------------------------------------------\nTotal de alumnos en el sistema: "+lista_alumnos.size()+"\n";
            for (int i = 0; i < lista_alumnos.size(); i++) {
                Alumno al = lista_alumnos.get(i);
                text = text.concat(al.toString());
                for (int j = 0; j < lista_aulas.size(); j++) {
                    Aula a = lista_aulas.get(j);
                    if (Objects.equals(a.getId(), al.getClassroomId())) {
                        text = text.concat(" Grado: "+a.getDegree()+" -- Seccion: "+a.getSection()+"\n");
                    }
                }
            }
            FileManager.openFS(this, text);
        });
        parentBtn.addActionListener((ActionEvent e) -> {
            int input = JOptionPane.showConfirmDialog(null, "Desea que el reporte incluya los alumnos inscritos por cada representante?");
            String text = "REPORTE DE REPRESENTANTES REGISTRADOS EN EL SISTEMA\n"+
                "---------------------------------------------------------------------------------------------\nTotal de representantes en el sistema: "+lista_representantes.size()+"\n";
            for (int i = 0; i < lista_representantes.size(); i++) {
                Representante r = lista_representantes.get(i);
                text = text.concat(""+r.toString()+"\n");
                if (input == 0) {
                    text = text.concat("Hijos: \n");
                    for (int j = 0; j < lista_alumnos.size(); j++) {
                        Alumno a = lista_alumnos.get(j);
                        if (Objects.equals(a.getParentId(), r.getId())) {
                            text = text.concat("-> "+a.toString()+"\n");
                        }
                    }
                }
            }
            if (input != 2) {
                FileManager.openFS(this, text);
            }
        });
        cancelBtn.addActionListener((ActionEvent e) -> {
            frame.dispose();
        });
        //init
        frame.getContentPane().add(panel);
        frame.pack();
        frame.setVisible(true);
    }
    
    private void initComponentsFirstPane(JPanel panel){
        panel.setLayout(new BorderLayout());
        //Mid Panel
        JPanel leftPanel = new JPanel();
        leftPanel.setLayout(new GridLayout(14,1));

        this.editButton(reportsButton);
        this.editButton(newClassRoom);
        this.editButton(editClassRoom);
        this.editButton(newTeacher);
        this.editButton(editTeacher);
        this.editButton(newStudent);
        this.editButton(editStudent);
        this.editButton(newParent);
        this.editButton(editParent);
        
        leftPanel.add(new JLabel("Generacion de reportes"));
        leftPanel.add(reportsButton);
        leftPanel.add(new JLabel("Administrar Aulas"));
        leftPanel.add(newClassRoom);
        leftPanel.add(editClassRoom);
        leftPanel.add(new JLabel("Administrar Docentes"));
        leftPanel.add(newTeacher);
        leftPanel.add(editTeacher);
        leftPanel.add(new JLabel("Administrar Alumnos y Representantes"));
        leftPanel.add(newStudent);
        leftPanel.add(editStudent);
        leftPanel.add(newParent);
        leftPanel.add(editParent);
        JPanel bottomPanel = new JPanel();
        
        JScrollPane scrollPane = new JScrollPane();
        scrollPane.setBounds(10, 11, 560, 227);
        
        bottomPanel.add(scrollPane);
        scrollPane.setViewportView(table);
        
        this.updateTableValues();
        bottomPanel.setLayout(new FlowLayout());
        bottomPanel.add(scrollPane);
        //
        
        editClassRoom.addActionListener((ActionEvent e) -> {
            if(table.getSelectedRow() != -1){
                Aula a = lista_aulas.get(table.getSelectedRow());
                System.out.println(a.toString());
                this.edit_classroom(this,a);
            }else {
                System.out.println("Debe seleccionar un aula de la lista");
            }
        });
        newTeacher.addActionListener((ActionEvent e) -> {
            this.add_teacher(this);
        });
        editTeacher.addActionListener((ActionEvent e) -> {
            this.edit_teacher(this);
        });
        newParent.addActionListener((ActionEvent e) -> {
            this.add_parent(this);
        });
        editParent.addActionListener((ActionEvent e) -> {
            this.edit_parent(this);
        });
        newStudent.addActionListener((ActionEvent e) -> {
            this.add_student(this);
        });
        editStudent.addActionListener((ActionEvent e) -> {
            this.edit_student(this);
        });
        reportsButton.addActionListener((ActionEvent e) -> {
            this.reports(this);
        });
        panel.add(leftPanel, BorderLayout.WEST);
        panel.add(bottomPanel, BorderLayout.CENTER);
    }
    
    private void edit_classroom(JFrame parent_panel, Aula a){
        final JDialog frame = new JDialog( parent_panel, "Modificar / Eliminar Aula", true);
        frame.setLocation(300, 300);
        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(5,2));
        //componentes
        String[] grados = { "primero", "segundo", "tercero", "cuarto", "quinto", "sexto" };
        JComboBox gradosBox = new JComboBox(grados);
        JTextField sectionText = new JTextField(10);
        JTextField limitText = new JTextField(10);
        JButton saveBtn = new JButton("Guardar");
        JButton deleteBtn = new JButton("Eliminar");
        JButton cancelBtn = new JButton("Cancelar");
        
        this.editButton(saveBtn);
        this.editButton(cancelBtn);
        this.editButton(deleteBtn, Color.RED, Color.WHITE);
        panel.add(new JLabel("Grado"));
        panel.add(gradosBox);
        panel.add(new JLabel("Seccion"));
        panel.add(sectionText);
        panel.add(new JLabel("Capacidad de alumnos"));
        panel.add(limitText);
        panel.add(new JLabel(""));
        panel.add(deleteBtn);
        panel.add(saveBtn);
        panel.add(cancelBtn);
        //pre-load data
        sectionText.setText(a.getSection());
        limitText.setText(String.valueOf(a.getLimit()));
        int aux = 0;
        for (int i = 0; i < 6; i++) {
            if(a.getDegree().equals(gradosBox.getItemAt(i))){
                aux = i;
            }
        }
        gradosBox.setSelectedIndex(aux);
        //Listeners
        saveBtn.addActionListener((ActionEvent e) -> {
            Aula b = new Aula(a.getId(), (String) gradosBox.getSelectedItem(), sectionText.getText(), Integer.valueOf(limitText.getText()));
            try {
                if(!a.toString().equals(b.toString())){
                    b.save(db.getConnection(), 1);
                    this.updateTableValues();
                    JOptionPane.showMessageDialog(null, "Aula modificada exitosamente","Aula modificada",JOptionPane.INFORMATION_MESSAGE);
                    frame.dispose();
                }else{
                    System.out.println("No ha realizado ningun cambio");
                }
            } catch (SQLException ex) {
                if (ex.getErrorCode() == 0) {
                    JOptionPane.showMessageDialog(null, "La seccion que ingreso ya se encuentra registrada","Error",JOptionPane.ERROR_MESSAGE);
                }else {
                    System.out.println("Erro con la base de datos: "+ex);
                }
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(null, "La cantidad de alumnos solo puede contener numeros y no debe ser mayor a 10 digitos","Error",JOptionPane.ERROR_MESSAGE);
            }
        });
        cancelBtn.addActionListener((ActionEvent e) -> {
            frame.dispose();
        });
        deleteBtn.addActionListener((ActionEvent e) -> {
            int input = JOptionPane.showConfirmDialog(null, "Esta seguro que desea eliminar el Aula - seccion: "+a.getSection()+" Grado: "+a.getDegree()+"?");
            if (input == 0) {
                try {
                    a.delete(db.getConnection());
                    JOptionPane.showMessageDialog(null, "Aula eliminada exitosamente","Aula eliminada",JOptionPane.INFORMATION_MESSAGE);
                    this.updateTableValues();
                    frame.dispose();
                } catch (SQLException ex) {
                    JOptionPane.showMessageDialog(null, "El aula no debe tener ningun Docente ni alumno asignado antes de poder eliminarla","Error",JOptionPane.ERROR_MESSAGE);
                }
            }
        });
        
        //init
        frame.getContentPane().add(panel);
        frame.pack();
        frame.setVisible(true);
    }
    
    private void add_classroom(JFrame parent_panel){
        final JDialog frame = new JDialog( parent_panel, "Añadir Aula", true);
        frame.setLocation(300, 300);
        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(5,2));
        
        //Componentes
        
        
        String[] grados = { "primero", "segundo", "tercero", "cuarto", "quinto", "sexto" };
        JComboBox gradosBox = new JComboBox(grados);
        JTextField sectionText = new JTextField(10);
        JTextField limitText = new JTextField(10);
        JButton saveBtn = new JButton("Guardar");
        JButton cancelBtn = new JButton("Cancelar");
        
        this.editButton(saveBtn);
        this.editButton(cancelBtn);
        panel.add(new JLabel("Grado"));
        panel.add(gradosBox);
        panel.add(new JLabel("Seccion"));
        panel.add(sectionText);
        panel.add(new JLabel("Capacidad de alumnos"));
        panel.add(limitText);
        panel.add(new JLabel(""));
        panel.add(new JLabel(""));
        panel.add(saveBtn);
        panel.add(cancelBtn);
        
        //Listeners
        saveBtn.addActionListener((ActionEvent e) -> {
            try {
                Aula a = new Aula(null, (String) gradosBox.getSelectedItem(), sectionText.getText(), Integer.valueOf(limitText.getText()));
                a.save(db.getConnection(), 0);
                this.updateTableValues();
                JOptionPane.showMessageDialog(null, "Aula creada exitosamente","Operacion exitosa",JOptionPane.INFORMATION_MESSAGE);
                frame.dispose();
            } catch (SQLException ex) {
                if (ex.getErrorCode() == 0) {
                    JOptionPane.showMessageDialog(null, "La seccion que ingreso ya se encuentra registrada","Error",JOptionPane.ERROR_MESSAGE);
                }else {
                    System.out.println("Erro con la base de datos: "+ex);
                }
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(null, "La cantidad de alumnos solo puede contener numeros y no debe ser mayor a 10 digitos","Error",JOptionPane.ERROR_MESSAGE);
            }
        });
        cancelBtn.addActionListener((ActionEvent e) -> {
            frame.dispose();
        });
        
        //init
        frame.getContentPane().add(panel);
        frame.pack();
        frame.setVisible(true);
    }
    
    private void add_parent(JFrame parent_panel){
        final JDialog frame = new JDialog( parent_panel, "Añadir Representante", true);
        frame.setLocation(300, 300);
        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(7,2));
        
        //Componentes
        JTextField firstNameText = new JTextField(10);
        JTextField lastNameText = new JTextField(10);
        JTextField directionText = new JTextField(10);
        JTextField cellText = new JTextField(10);
        JTextField idText = new JTextField(10);
        JLabel errorLabel = new JLabel("");
        
        JButton saveBtn = new JButton("Guardar");
        JButton cancelBtn = new JButton("Cancelar");
        
        this.editButton(saveBtn);
        this.editButton(cancelBtn);
        panel.add(new JLabel("Cedula                   "));
        panel.add(idText);
        panel.add(new JLabel("Nombre"));
        panel.add(firstNameText);
        panel.add(new JLabel("Apellido"));
        panel.add(lastNameText);
        panel.add(new JLabel("Direccion"));
        panel.add(directionText);
        panel.add(new JLabel("Numero de telefono"));
        panel.add(cellText);
        panel.add(errorLabel);
        panel.add(new JLabel(""));
        panel.add(saveBtn);
        panel.add(cancelBtn);
        
        //Listeners
        saveBtn.addActionListener((ActionEvent e) -> {
            try {
                Representante r = new Representante(Integer.parseInt(idText.getText()), firstNameText.getText(), 
                        lastNameText.getText(), cellText.getText(), directionText.getText());
                r.save(db.getConnection(), 0);
                JOptionPane.showMessageDialog(null, "Representante añadido exitosamente","Operacion exitosa",JOptionPane.INFORMATION_MESSAGE);
                frame.dispose();
            } catch (SQLException ex) {
                System.out.println("Error con la base de datos: "+ex);
                errorLabel.setForeground(Color.red);
                errorLabel.setText("La cedula ya fue registrada");
                JOptionPane.showMessageDialog(null, "Ha ocurrido un error, verifique que la cedula no se encuentre registrada o los campos tengan los datos correspondientes","Error",JOptionPane.ERROR_MESSAGE);
                
            } catch (Exception ex){
                JOptionPane.showMessageDialog(null, "La cedula solo debe contener numeros y no debe ser mayor a 10 digitos","Error",JOptionPane.ERROR_MESSAGE);
            }
        });
        cancelBtn.addActionListener((ActionEvent e) -> {
            frame.dispose();
        });
        
        //init
        frame.getContentPane().add(panel);
        frame.pack();
        frame.setVisible(true);
    }
    
    private void edit_parent(JFrame parent_panel){
        final JDialog frame = new JDialog( parent_panel, "Modificar / Eliminar Representante", true);
        frame.setLocation(300, 300);
        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(9,2));
        
        //Componentes
        JTextField idSearchText = new JTextField(10);
        JTextField firstNameText = new JTextField(10);
        JTextField lastNameText = new JTextField(10);
        JTextField directionText = new JTextField(10);
        JTextField cellText = new JTextField(10);
        JTextField idText = new JTextField(10);
        JLabel errorLabel = new JLabel("");
        JButton saveBtn = new JButton("Guardar");
        JButton cancelBtn = new JButton("Cancelar");
        JButton deleteBtn = new JButton("Eliminar");
        JButton searchBtn = new JButton("Buscar");
        
        this.editButton(saveBtn);
        this.editButton(cancelBtn);
        this.editButton(searchBtn);
        this.editButton(deleteBtn,Color.red,Color.white);
        panel.add(new JLabel("Ingrese cedula para buscar         "));
        panel.add(idSearchText);
        panel.add(new JLabel(""));
        panel.add(searchBtn);
        panel.add(new JLabel("Cedula"));
        panel.add(idText);
        idText.setEditable(false);
        panel.add(new JLabel("Nombre"));
        panel.add(firstNameText);
        panel.add(new JLabel("Apellido"));
        panel.add(lastNameText);
        panel.add(new JLabel("Direccion"));
        panel.add(directionText);
        panel.add(new JLabel("Numero de telefono"));
        panel.add(cellText);
        panel.add(errorLabel);
        panel.add(deleteBtn);
        panel.add(saveBtn);
        panel.add(cancelBtn);
        
        //Listeners
        saveBtn.addActionListener((ActionEvent e) -> {
            try {
                Representante r = new Representante(Integer.parseInt(idText.getText()), firstNameText.getText(), 
                        lastNameText.getText(), cellText.getText(), directionText.getText());
                r.save(db.getConnection(), 1);
                JOptionPane.showMessageDialog(null, "Representante modificado exitosamente","Operacion exitosa",JOptionPane.INFORMATION_MESSAGE);
                frame.dispose();
            } catch (SQLException ex) {
                System.out.println("Error con la base de datos: "+ex);
                JOptionPane.showMessageDialog(null, "Ha ocurrido un error, verifique que la cedula no se encuentre registrada o los campos tengan los datos correspondientes","Error",JOptionPane.ERROR_MESSAGE);
                
            } catch (Exception ex){
                JOptionPane.showMessageDialog(null, "La cedula solo debe contener numeros y no debe ser mayor a 10 digitos","Error",JOptionPane.ERROR_MESSAGE);
            }
        });
        searchBtn.addActionListener((ActionEvent e) -> {
            try {
                Representante rep = db.consultar_representantes(cnx, Integer.parseInt(idSearchText.getText())).get(0);
                idText.setEditable(true);
                idText.setText(rep.getId().toString());
                idText.setEditable(false);
                firstNameText.setText(rep.getFirstName());
                lastNameText.setText(rep.getLastName());
                directionText.setText(rep.getDirection());
                cellText.setText(rep.getCell());
                errorLabel.setText("");
            } catch (SQLException ex) {
                Logger.getLogger(Gui.class.getName()).log(Level.SEVERE, null, ex);
            } catch (IndexOutOfBoundsException ex){
                errorLabel.setForeground(Color.red);
                errorLabel.setText("Representante no encontrado");
            } catch (NumberFormatException ex){
                errorLabel.setForeground(Color.red);
                errorLabel.setText("Solo debe contener numeros");
            }
        });
        deleteBtn.addActionListener((ActionEvent e) -> {
            try {
                Representante r = db.consultar_representantes(cnx, Integer.parseInt(idSearchText.getText())).get(0);
                int input = JOptionPane.showConfirmDialog(null, "Esta seguro que desea eliminar el Representante : "+r.getFirstName()+" Cedula: "+r.getId()+
                        "\nEsto causara que tambien se eliminen todos los alumnos asociados al representante");
                if (input == 0) {
                        for (int i = 0; i < lista_alumnos.size(); i++) {
                        if(Objects.equals(lista_alumnos.get(i).getParentId(), r.getId()))
                            lista_alumnos.get(i).delete(cnx);
                        }
                        r.delete(db.getConnection());
                        JOptionPane.showMessageDialog(null, "Representante eliminado exitosamente","Representante eliminado",JOptionPane.INFORMATION_MESSAGE);
                        frame.dispose();
                }
            } catch (SQLException ex) {
                Logger.getLogger(Gui.class.getName()).log(Level.SEVERE, null, ex);
            } catch (Exception ex) {
                
            }
        });
        cancelBtn.addActionListener((ActionEvent e) -> {
            frame.dispose();
        });
        
        //init
        frame.getContentPane().add(panel);
        frame.pack();
        frame.setVisible(true);
    }
    
    private void edit_student(JFrame parent_panel){
        final JDialog frame = new JDialog( parent_panel, "Modificar / Eliminar Alumno", true);
        frame.setLocation(300, 300);
        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(11,2));
        
        //Componentes
        JTextField firstNameText = new JTextField(10);
        JTextField lastNameText = new JTextField(10);
        JTextField idText = new JTextField(10);
        JLabel repLabel = new JLabel("");
        ArrayList<Aula> aulas = new ArrayList();
        ArrayList<Representante> reps = new ArrayList();
        ArrayList<Alumno> students = new ArrayList();
        for (int i = 0; i < lista_aulas.size(); i++) {
            if (lista_aulas.get(i).getCurrentStudents() < lista_aulas.get(i).getLimit()) {
                aulas.add(lista_aulas.get(i));
            }
        }
        JComboBox studentList = new JComboBox();
        JComboBox aulaText = new JComboBox(aulas.toArray());
        JButton saveBtn = new JButton("Guardar");
        JButton searchBtn = new JButton("Buscar");
        JButton cancelBtn = new JButton("Cancelar");
        JButton deleteBtn = new JButton("Eliminar");
        JButton updateBtn = new JButton("Cargar datos");
        
        UtilDateModel dateModel = new UtilDateModel();
        Properties p = new Properties();
        p.put("text.today", "Hoy");
        p.put("text.month", "Mes");
        p.put("text.year", "Año");
        JDatePanelImpl datePanel = new JDatePanelImpl(dateModel,p);
        JDatePickerImpl datePicker = new JDatePickerImpl(datePanel, new DateLabelFormatter());
        
        this.editButton(saveBtn);
        this.editButton(cancelBtn);
        this.editButton(searchBtn);
        this.editButton(updateBtn);
        this.editButton(deleteBtn, Color.red, Color.white);
        panel.add(new JLabel("Cedula del representante "));
        panel.add(idText);
        panel.add(new JLabel(""));
        panel.add(searchBtn);
        panel.add(new JLabel("Representante "));
        panel.add(repLabel);
        panel.add(new JLabel("Alumnos "));
        panel.add(studentList);
        panel.add(new JLabel(""));
        panel.add(updateBtn);
        panel.add(new JLabel("Nombre"));
        panel.add(firstNameText);
        panel.add(new JLabel("Apellido"));
        panel.add(lastNameText);
        panel.add(new JLabel("Fecha de Nacimiento"));
        panel.add(datePicker);
        panel.add(new JLabel("Aula Asignada"));
        panel.add(aulaText);
        panel.add(new JLabel(""));
        panel.add(deleteBtn);
        panel.add(saveBtn);
        panel.add(cancelBtn);
        
        //Listeners
        saveBtn.addActionListener((ActionEvent e) -> {
            try {
                String auxDate = datePicker.getJFormattedTextField().getText();
                String[] dates = auxDate.split("-");
                Date dat = new Date(Integer.valueOf(dates[0])-1900, Integer.valueOf(dates[1]), Integer.valueOf(dates[2]));
                Alumno a = new Alumno(students.get(studentList.getSelectedIndex()).getId(), firstNameText.getText(),
                        lastNameText.getText(), dat, reps.get(0).getId(), aulas.get(aulaText.getSelectedIndex()).getId());
                a.save(db.getConnection(), 1);
                lista_alumnos = db.consultar_alumnos(cnx, null);
                this.updateTableValues();
                JOptionPane.showMessageDialog(null, "Alumno modificado exitosamente","Operacion exitosa",JOptionPane.INFORMATION_MESSAGE);
                frame.dispose();
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(null, "Ha ocurrido un error, verifique que la cedula no se encuentre registrada o los campos tengan los datos correspondientes","Error",JOptionPane.ERROR_MESSAGE);
                System.out.println("Ex: "+ex);
            } catch (Exception ex){
                System.out.println("ex:" +ex);
            }
        });
        searchBtn.addActionListener((ActionEvent e) -> {
            try {
                Representante rep = db.consultar_representantes(cnx, Integer.valueOf(idText.getText())).get(0);
                reps.add(rep);
                repLabel.setText(rep.getFirstName() + " "+rep.getLastName());
                students.clear();
                studentList.removeAllItems();
                for (int i = 0; i < lista_alumnos.size(); i++) {
                    if (Objects.equals(lista_alumnos.get(i).getParentId(), rep.getId())) {
                        students.add(lista_alumnos.get(i));
                    }
                }
                for (int i = 0; i < students.size(); i++) {
                    studentList.addItem(students.get(i));
                }
            } catch (SQLException ex) {
                Logger.getLogger(Gui.class.getName()).log(Level.SEVERE, null, ex);
            } catch (IndexOutOfBoundsException ex){
                JOptionPane.showMessageDialog(null, "Representante no encontrado","No encontrado",JOptionPane.INFORMATION_MESSAGE);
            } catch (NumberFormatException ex){
                JOptionPane.showMessageDialog(null, "El numero de cedula debe tener menos de 10 digitos","Error",JOptionPane.ERROR_MESSAGE);
            } catch (Exception ex){
                JOptionPane.showMessageDialog(null, "La cedula solo debe contener numeros y no debe ser mayor a 10 digitos","Error",JOptionPane.ERROR_MESSAGE);
            }
        });
        cancelBtn.addActionListener((ActionEvent e) -> {
            frame.dispose();
        });
        deleteBtn.addActionListener((ActionEvent e) -> {
            try {
                Alumno a = students.get(studentList.getSelectedIndex());
                int input = JOptionPane.showConfirmDialog(null, "Esta seguro que desea eliminar el Alumno : "+a.getFirstName());
                if (input == 0) {
                        a.delete(db.getConnection());
                        lista_alumnos = db.consultar_alumnos(cnx, null);
                        this.updateTableValues();
                        JOptionPane.showMessageDialog(null, "Alumno eliminado exitosamente","Alumno eliminado",JOptionPane.INFORMATION_MESSAGE);
                        frame.dispose();
                }
            } catch (SQLException ex) {
                Logger.getLogger(Gui.class.getName()).log(Level.SEVERE, null, ex);
            } catch (Exception ex) {
                System.out.println("Ha ocurrido un error inesperado: "+ex);
            }
        });
        updateBtn.addActionListener((ActionEvent e) -> {
            Alumno a = students.get(studentList.getSelectedIndex());
            firstNameText.setText(a.getFirstName());
            lastNameText.setText(a.getLastName());
            datePicker.getJFormattedTextField().setText(a.getBirthDate().toString());
            aulas.clear();
            for (int i = 0; i < lista_aulas.size(); i++) {
                if(Objects.equals(lista_aulas.get(i).getId(), a.getClassroomId())){
                    aulas.add(lista_aulas.get(i));
                    aulaText.setSelectedItem(lista_aulas.get(i));
                    continue;
                }
                if (lista_aulas.get(i).getCurrentStudents() < lista_aulas.get(i).getLimit()) {
                    aulas.add(lista_aulas.get(i));
                }
                
            }
        });
        
        //init
        frame.getContentPane().add(panel);
        frame.pack();
        frame.setVisible(true);
    }
    
    private void add_student(JFrame parent_panel){
        final JDialog frame = new JDialog( parent_panel, "Añadir Alumno", true);
        frame.setLocation(300, 300);
        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(9,2));
        
        //Componentes
        JTextField firstNameText = new JTextField(10);
        JTextField lastNameText = new JTextField(10);
        JTextField idText = new JTextField(10);
        JLabel repLabel = new JLabel("");
        ArrayList<Aula> aulas = new ArrayList();
        ArrayList<Representante> reps = new ArrayList();
        for (int i = 0; i < lista_aulas.size(); i++) {
            if (lista_aulas.get(i).getCurrentStudents() < lista_aulas.get(i).getLimit()) {
                aulas.add(lista_aulas.get(i));
            }
        }
        JComboBox aulaText = new JComboBox(aulas.toArray());
        JButton saveBtn = new JButton("Guardar");
        JButton searchBtn = new JButton("Buscar");
        JButton cancelBtn = new JButton("Cancelar");
        
        UtilDateModel dateModel = new UtilDateModel();
        dateModel.setYear(2010);
        Properties p = new Properties();
        p.put("text.today", "Hoy");
        p.put("text.month", "Mes");
        p.put("text.year", "Año");
        JDatePanelImpl datePanel = new JDatePanelImpl(dateModel,p);
        JDatePickerImpl datePicker = new JDatePickerImpl(datePanel, new DateLabelFormatter());
        
        this.editButton(saveBtn);
        this.editButton(cancelBtn);
        this.editButton(searchBtn);
        panel.add(new JLabel("Cedula del representante "));
        panel.add(idText);
        panel.add(new JLabel(""));
        panel.add(searchBtn);
        panel.add(new JLabel("Representante "));
        panel.add(repLabel);
        panel.add(new JLabel("Nombre"));
        panel.add(firstNameText);
        panel.add(new JLabel("Apellido"));
        panel.add(lastNameText);
        panel.add(new JLabel("Fecha de Nacimiento"));
        panel.add(datePicker);
        panel.add(new JLabel("Aula Asignada"));
        panel.add(aulaText);
        panel.add(new JLabel(""));
        panel.add(new JLabel(""));
        panel.add(saveBtn);
        panel.add(cancelBtn);
        
        //Listeners
        saveBtn.addActionListener((ActionEvent e) -> {
            try {
                String auxDate = datePicker.getJFormattedTextField().getText();
                String[] dates = auxDate.split("-");
                Date dat = new Date(Integer.valueOf(dates[0])-1900, Integer.valueOf(dates[1]), Integer.valueOf(dates[2]));
                Alumno a = new Alumno(0, firstNameText.getText(), lastNameText.getText(), dat, reps.get(0).getId(), aulas.get(aulaText.getSelectedIndex()).getId());
                a.save(db.getConnection(), 0);
                lista_alumnos = db.consultar_alumnos(cnx, null);
                this.updateTableValues();
                JOptionPane.showMessageDialog(null, "Alumno añadido exitosamente","Operacion exitosa",JOptionPane.INFORMATION_MESSAGE);
                frame.dispose();
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(null, "Ha ocurrido un error, verifique que la cedula no se encuentre registrada o los campos tengan los datos correspondientes","Error",JOptionPane.ERROR_MESSAGE);
            } catch (Exception ex){
                JOptionPane.showMessageDialog(null, "La cedula solo debe contener numeros y no debe ser mayor a 10 digitos","Error",JOptionPane.ERROR_MESSAGE);
            }
        });
        searchBtn.addActionListener((ActionEvent e) -> {
            try {
                Representante rep = db.consultar_representantes(cnx, Integer.valueOf(idText.getText())).get(0);
                reps.add(rep);
                repLabel.setText(rep.getFirstName() + " "+rep.getLastName());
            } catch (SQLException ex) {
                Logger.getLogger(Gui.class.getName()).log(Level.SEVERE, null, ex);
            } catch (IndexOutOfBoundsException ex){
                //errorLabel.setForeground(Color.red);
                //errorLabel.setText("Docente no encontrado");
            } catch (NumberFormatException ex){
                //errorLabel.setForeground(Color.red);
                //errorLabel.setText("Solo debe contener numeros");
            }
        });
        cancelBtn.addActionListener((ActionEvent e) -> {
            frame.dispose();
        });
        
        //init
        frame.getContentPane().add(panel);
        frame.pack();
        frame.setVisible(true);
    }
    
    private void add_teacher(JFrame parent_panel){
        final JDialog frame = new JDialog( parent_panel, "Añadir Docente", true);
        frame.setLocation(300, 300);
        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(9,2));
        
        //Componentes
        JTextField firstNameText = new JTextField(10);
        JTextField lastNameText = new JTextField(10);
        JTextField directionText = new JTextField(10);
        JTextField cellText = new JTextField(10);
        JTextField idText = new JTextField(10);
        ArrayList sections = new ArrayList();
        ArrayList aulas_id = new ArrayList();
        ArrayList<Integer> aux_id = new ArrayList();
        for (int i = 0; i < lista_docentes.size(); i++) {
            aux_id.add(lista_docentes.get(i).getClassroomId());
        }
        for (int i = 0; i < lista_aulas.size(); i++) {
            if (!aux_id.contains(lista_aulas.get(i).getId())) {
                sections.add(lista_aulas.get(i).getDegree() + " - " + lista_aulas.get(i).getSection());
                aulas_id.add(lista_aulas.get(i).getId());
            }
        }
        Object[] secs = sections.toArray();
        Object[] ids = aulas_id.toArray();
        JComboBox aulaText = new JComboBox(secs);
        
        JButton saveBtn = new JButton("Guardar");
        JButton cancelBtn = new JButton("Cancelar");
        
        UtilDateModel dateModel = new UtilDateModel();
        dateModel.setYear(2000);
        Properties p = new Properties();
        p.put("text.today", "Hoy");
        p.put("text.month", "Mes");
        p.put("text.year", "Año");
        JDatePanelImpl datePanel = new JDatePanelImpl(dateModel,p);
        JDatePickerImpl datePicker = new JDatePickerImpl(datePanel, new DateLabelFormatter());
        
        this.editButton(saveBtn);
        this.editButton(cancelBtn);
        panel.add(new JLabel("Cedula"));
        panel.add(idText);
        panel.add(new JLabel("Nombre"));
        panel.add(firstNameText);
        panel.add(new JLabel("Apellido"));
        panel.add(lastNameText);
        panel.add(new JLabel("Fecha de Nacimiento"));
        panel.add(datePicker);
        panel.add(new JLabel("Direccion"));
        panel.add(directionText);
        panel.add(new JLabel("Numero de telefono"));
        panel.add(cellText);
        panel.add(new JLabel("Aula Asignada"));
        panel.add(aulaText);
        panel.add(new JLabel(""));
        panel.add(new JLabel(""));
        panel.add(saveBtn);
        panel.add(cancelBtn);
        
        //Listeners
        saveBtn.addActionListener((ActionEvent e) -> {
            try {
                String auxDate = datePicker.getJFormattedTextField().getText();
                String[] dates = auxDate.split("-");
                Date dat = new Date(Integer.valueOf(dates[0])-1900, Integer.valueOf(dates[1]), Integer.valueOf(dates[2]));
                Docente d = new Docente( Integer.valueOf(idText.getText()), firstNameText.getText(), 
                lastNameText.getText(), dat, directionText.getText(), cellText.getText(), Integer.valueOf(ids[aulaText.getSelectedIndex()].toString()));
                d.save(db.getConnection(), 0);
                lista_docentes = db.consultar_docentes(cnx, null);
                this.updateTableValues();
                JOptionPane.showMessageDialog(null, "Docente añadido exitosamente","Operacion exitosa",JOptionPane.INFORMATION_MESSAGE);
                frame.dispose();
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(null, "Ha ocurrido un error, verifique que la cedula no se encuentre registrada o los campos tengan los datos correspondientes","Error",JOptionPane.ERROR_MESSAGE);
            } catch (Exception ex){
                JOptionPane.showMessageDialog(null, "La cedula solo debe contener numeros y no debe ser mayor a 10 digitos","Error",JOptionPane.ERROR_MESSAGE);
            }
        });
        cancelBtn.addActionListener((ActionEvent e) -> {
            frame.dispose();
        });
        
        //init
        frame.getContentPane().add(panel);
        frame.pack();
        frame.setVisible(true);
    }
    private void edit_teacher(JFrame parent_panel){
        final JDialog frame = new JDialog( parent_panel, "Modificar / Eliminar Docente", true);
        frame.setLocation(300, 300);
        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(11,2));
        
        //Componentes
        JTextField firstNameText = new JTextField(10);
        JTextField lastNameText = new JTextField(10);
        JTextField directionText = new JTextField(10);
        JTextField cellText = new JTextField(10);
        JTextField idText = new JTextField(10);
        JTextField idSearchText = new JTextField(10);
        ArrayList<Integer> aux_id = new ArrayList();
        ArrayList<Docente> auxDoc = new ArrayList();
        ArrayList<Aula> auxAul = new ArrayList();
        JLabel errorLabel = new JLabel("");
        for (int i = 0; i < lista_docentes.size(); i++) {
            aux_id.add(lista_docentes.get(i).getClassroomId());
        }
        for (int i = 0; i < lista_aulas.size(); i++) {
            if (!aux_id.contains(lista_aulas.get(i).getId())) {
                auxAul.add(lista_aulas.get(i));
            }
        }
        JComboBox aulaText = new JComboBox();
        for (int i = 0; i < auxAul.size(); i++) {
            aulaText.addItem(auxAul.get(i));
        }
        
        JButton saveBtn = new JButton("Guardar");
        JButton cancelBtn = new JButton("Cancelar");
        JButton searchBtn = new JButton("Buscar");
        JButton deleteBtn = new JButton("Eliminar");
        UtilDateModel dateModel = new UtilDateModel();
        Properties p = new Properties();
        p.put("text.today", "Hoy");
        p.put("text.month", "Mes");
        p.put("text.year", "Año");
        JDatePanelImpl datePanel = new JDatePanelImpl(dateModel,p);
        JDatePickerImpl datePicker = new JDatePickerImpl(datePanel, new DateLabelFormatter());
        dateModel.setYear(2000);
        this.editButton(saveBtn);
        this.editButton(cancelBtn);
        this.editButton(searchBtn);
        this.editButton(deleteBtn, Color.RED, Color.WHITE);
        panel.add(new JLabel("Ingrese Cedula para buscar"));
        panel.add(idSearchText);
        panel.add(new JLabel(""));
        panel.add(searchBtn);
        panel.add(new JLabel("Cedula"));
        panel.add(idText);
        idText.setEditable(false);
        panel.add(new JLabel("Nombre"));
        panel.add(firstNameText);
        panel.add(new JLabel("Apellido"));
        panel.add(lastNameText);
        panel.add(new JLabel("Fecha de Nacimiento"));
        panel.add(datePicker);
        panel.add(new JLabel("Direccion"));
        panel.add(directionText);
        panel.add(new JLabel("Numero de telefono"));
        panel.add(cellText);
        panel.add(new JLabel("Aula Asignada"));
        panel.add(aulaText);
        panel.add(errorLabel);
        panel.add(deleteBtn);
        panel.add(saveBtn);
        panel.add(cancelBtn);
        
        //Listeners
        saveBtn.addActionListener((ActionEvent e) -> {
            try {
                String auxDate = datePicker.getJFormattedTextField().getText();
                String[] dates = auxDate.split("-");
                Date dat = new Date(Integer.valueOf(dates[0])-1900, Integer.valueOf(dates[1]), Integer.valueOf(dates[2]));
                Docente d = new Docente( Integer.valueOf(idText.getText()), firstNameText.getText(), 
                lastNameText.getText(), dat, directionText.getText(), cellText.getText(), auxAul.get(aulaText.getSelectedIndex()).getId() );
                if (d.toString().equals(auxDoc.get(0).toString())) {
                    System.out.println("Iguales");
                }else {
                    System.out.println("Diferentes");
                    d.save(db.getConnection(), 1);
                    lista_docentes = db.consultar_docentes(cnx, null);
                    JOptionPane.showMessageDialog(null, "Docente modificado exitosamente","Operacion exitosa",JOptionPane.INFORMATION_MESSAGE);
                    frame.dispose();
                }
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(null, "Ha ocurrido un error, verifique que la cedula no se encuentre registrada o los campos tengan los datos correspondientes","Error",JOptionPane.ERROR_MESSAGE);
            } catch (Exception ex) {
                System.out.println("Ex: "+ex);
            }
        });
        searchBtn.addActionListener((ActionEvent e) -> {
            try {
                Docente doc = db.consultar_docentes(cnx, Integer.valueOf(idSearchText.getText())).get(0);
                idText.setEditable(true);
                idText.setText(doc.getId().toString());
                idText.setEditable(false);
                firstNameText.setText(doc.getFirstName());
                lastNameText.setText(doc.getLastName());
                datePicker.getJFormattedTextField().setText(doc.getBirthDate().toString());
                directionText.setText(doc.getDirection());
                cellText.setText(doc.getCell());
                auxDoc.clear();
                auxDoc.add(doc);
                aux_id.clear();
                auxAul.clear();
                aulaText.removeAllItems();
                
                for (int i = 0; i < lista_docentes.size(); i++) {
                    aux_id.add(lista_docentes.get(i).getClassroomId());
                }
                for (int i = 0; i < lista_aulas.size(); i++) {
                    if (!aux_id.contains(lista_aulas.get(i).getId())) {
                        auxAul.add(lista_aulas.get(i));
                    }
                }
                for (int i = 0; i < auxAul.size(); i++) {
                    aulaText.addItem(auxAul.get(i));
                }
                for (int i = 0; i < lista_aulas.size(); i++) {
                    if (doc.getClassroomId() == lista_aulas.get(i).getId() ) {
                        auxAul.add(lista_aulas.get(i));
                        aulaText.addItem(lista_aulas.get(i));
                        aulaText.setSelectedItem(lista_aulas.get(i));
                    }
                }
                errorLabel.setText("");
            } catch (SQLException ex) {
                Logger.getLogger(Gui.class.getName()).log(Level.SEVERE, null, ex);
            } catch (IndexOutOfBoundsException ex){
                errorLabel.setForeground(Color.red);
                errorLabel.setText("Docente no encontrado");
            } catch (NumberFormatException ex){
                errorLabel.setForeground(Color.red);
                errorLabel.setText("Solo debe contener numeros");
            }
        });
        deleteBtn.addActionListener((ActionEvent e) -> {
            try {
                Docente d = auxDoc.get(0);
                int input = JOptionPane.showConfirmDialog(null, "Esta seguro que desea eliminar el Docente : "+d.getFirstName()+" Cedula: "+d.getId());
                if (input == 0) {
                        d.delete(db.getConnection());
                        JOptionPane.showMessageDialog(null, "Docente eliminado exitosamente","Docente eliminado",JOptionPane.INFORMATION_MESSAGE);
                        frame.dispose();
                }
            } catch (SQLException ex) {
                Logger.getLogger(Gui.class.getName()).log(Level.SEVERE, null, ex);
            } catch (Exception ex) {
                
            }
        });
        cancelBtn.addActionListener((ActionEvent e) -> {
            frame.dispose();
        });
        
        //init
        frame.getContentPane().add(panel);
        frame.pack();
        frame.setVisible(true);
    }
    
}

