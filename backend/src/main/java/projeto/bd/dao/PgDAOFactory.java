/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package projeto.bd.dao;

import java.sql.Connection;
import projeto.bd.dao.PgUsuarioDAO;
/**
 *
 * @author dskaster
 */
public class PgDAOFactory extends DAOFactory {
    
    public PgDAOFactory(Connection connection) {
        this.connection = connection;
    }

    @Override
    public UsuarioDAO getUsuarioDAO() {
        return new PgUsuarioDAO(this.connection);
    }    

    @Override
    public VersaoDatasetDAO getVersaoDatasetDAO() {
        return new PgVersaoDatasetDAO(this.connection);
    }
}
