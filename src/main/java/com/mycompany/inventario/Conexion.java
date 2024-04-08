/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.inventario;


import java.sql.DriverManager;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;
/**
 *
 * @author Dannia
 */
public class Conexion {
    Connection conexion = null;
    
    String usuario="admin";
    String contraseña="123";
    String db="DBInventario";
    String puerto="1433";
    
    public Connection obtenerConexion(){
        try {
            String cadena = "jdbc:sqlserver://localhost:" + puerto + ";encrypt=true;trustServerCertificate=true;databaseName=" + db;
            conexion = DriverManager.getConnection(cadena, usuario, contraseña);

        } catch (SQLException e) {
            System.out.println("Ha ocurrido un error en la conexion" + e.getMessage());
        }

        return conexion;
    }
    
    public void insertarArticulo( String nombre, String descripcion, String cantidad, String precio){
        try {
            Statement stmt = null;

            String insertSQL = "INSERT INTO ARTICULO (ID_ART, NOMBRE, DESCRIPCION, CANTIDAD, PRECIO) VALUES(" + "(SELECT MAX(ID_ART) FROM ARTICULO) + 1"
                    + ",'" + nombre + "','" + descripcion + "'," + cantidad + "," + precio + ")";

            stmt = obtenerConexion().createStatement();
            stmt.executeUpdate(insertSQL);
            JOptionPane.showMessageDialog(null, "Registro guardado", "Nuevo articulo", JOptionPane.INFORMATION_MESSAGE);
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, ex.getMessage());
        }
    }
    
    public void actualizarArticulo(String id, String nombre, String descripcion, String cantidad, String precio){
        Statement stmt = null;
        String updateSQL = "UPDATE ARTICULO SET NOMBRE = '" + nombre + "',DESCRIPCION = '" + descripcion + "'," + "CANTIDAD = " + cantidad + ",PRECIO = " + precio + " WHERE ID_ART = " + id;        
        try {
            stmt = obtenerConexion().createStatement();
            stmt.execute(updateSQL);
             JOptionPane.showMessageDialog(null, "Registro actualizado", "Actualizar articulo", JOptionPane.INFORMATION_MESSAGE);
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, ex.getMessage());
        }
    }
    
    public void eliminarArticulo(int idArticulo){
        Statement stmt = null;
        String deleteSQL = "DELETE FROM ARTICULO WHERE ID_ART=" + idArticulo;
        try {
            stmt = obtenerConexion().createStatement();
            stmt.executeUpdate(deleteSQL);
            JOptionPane.showMessageDialog(null, "Registro eliminado!", "Eliminar articulo", JOptionPane.WARNING_MESSAGE);
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, ex.getMessage());
        }

    }
    
    public DefaultTableModel consultaSQL(String consulta) throws SQLException{
        String[] registros = new String[5];
        String[] titulos = {"Id Articulo", "Nombre", "Descripcion", "Cantidad", "Precio"};
        DefaultTableModel modelo = new DefaultTableModel(titulos, 0);
        ResultSet rs = null;
        Statement stmt = obtenerConexion().createStatement();

        try {
            rs = stmt.executeQuery(consulta);
            while (rs.next()) {
                registros[0] = rs.getString("ID_ART");
                registros[1] = rs.getString("NOMBRE");
                registros[2] = rs.getString("DESCRIPCION");
                registros[3] = rs.getString("CANTIDAD");
                registros[4] = rs.getString("PRECIO");
                modelo.addRow(registros);
            }
            return modelo;
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error: " + e.getMessage());
            return modelo;
        }

    }
    
    
     public DefaultTableModel llenarTabla(){
        try {
            return consultaSQL("select ID_ART, NOMBRE, DESCRIPCION, CANTIDAD, PRECIO from ARTICULO");
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Ha occurido un errror" + ex.getMessage());
        }
        return null;
    }

    public DefaultTableModel busqueda(String consulta) {

        try {
            return consultaSQL("select ID_ART, NOMBRE, DESCRIPCION, CANTIDAD, PRECIO from ARTICULO WHERE NOMBRE LIKE '" + consulta + "%'");
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Ha occurido un errror" + ex.getMessage());
        }
        return null;
    }

}
