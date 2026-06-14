/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package projeto.bd.dao;

import java.sql.Connection;
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

    @Override
    public FeatureDAO getFeatureDAO() {
        return new PgFeatureDAO(this.connection);
    }

    @Override
    public DatasetDAO getDatasetDAO() {
        return new PgDatasetDAO(this.connection, new PgVersaoDatasetDAO(this.connection));
    }

    @Override
    public AcessoVersaoDAO getAcessoVersaoDAO() {
        return new PgAcessoVersaoDAO(this.connection);
    }
}
