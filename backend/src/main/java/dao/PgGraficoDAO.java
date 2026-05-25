/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import model.Grafico;

/**
 *
 * @author luana
 */
public class PgGraficoDAO implements GraficoDAO {
    private final Connection connection; 
    private static final String GRAFICO_1_QUERY =
                                "SELECT EXTRACT(MONTH FROM pc.date_precipitation) AS mes, AVG(pc.prec) AS qtd " + 
                                "FROM  public.precipitation pc " +
                                "WHERE EXTRACT(YEAR FROM pc.date_precipitation) = 2022 " +
                                "GROUP BY EXTRACT(MONTH FROM pc.date_precipitation);";
    
    private static final String GRAFICO_2_QUERY =
                                "SELECT EXTRACT(MONTH FROM pc.date_prediction) AS mes, SUM(pc.dsv) AS qtd " +
                                "FROM  public.beruski_prediction_result pc " +
                                "GROUP BY EXTRACT(MONTH FROM pc.date_prediction) " +
                                "ORDER BY mes;";
    
    private static final String GRAFICO_3_QUERY = 
                                "SELECT COUNT(date_part('year', age(nascimento))) AS qtd, date_part('year', age(nascimento)) AS idade "+
                                "FROM public.user " +
                                "GROUP BY date_part('year', age(nascimento)) " +
                                "ORDER BY idade;";
    
    private static final String GRAFICO_4_QUERY =
                                "SELECT EXTRACT(MONTH FROM b.date_prediction) AS mes, AVG(pc.prec) AS precipitacao, AVG(b.dsv) AS DSV " +
                                "FROM  public.precipitation pc , beruski_prediction_result b " +
                                "WHERE pc.segment_id = b.segment_id AND EXTRACT(MONTH FROM pc.date_precipitation) =  EXTRACT(MONTH FROM b.date_prediction) " +
                                "GROUP BY EXTRACT(MONTH FROM b.date_prediction),  EXTRACT(MONTH FROM pc.date_precipitation);";
    

    
    public PgGraficoDAO(Connection connection) {
        this.connection = connection;
    }

    @Override
    public List<Grafico> getGrafico1() throws SQLException {
        List<Grafico> ResultJSON = new ArrayList<>();
        try (PreparedStatement statement = connection.prepareStatement(GRAFICO_1_QUERY)) {

              try (ResultSet result = statement.executeQuery()) {
                    while (result.next()) {
                        Grafico grafico = new Grafico();
                        grafico.setMes(result.getInt("mes"));
                        grafico.setQuantity(result.getDouble("qtd"));
                        ResultJSON.add(grafico);
                    }
                }
            } catch (SQLException ex) {
                Logger.getLogger(PgUserDAO.class.getName()).log(Level.SEVERE, "DAO", ex);

                throw new SQLException("Erro dados");
            }     
        return ResultJSON;
    }

    
    @Override
    public List<Grafico> getGrafico2() throws SQLException {
        List<Grafico> ResultJSON = new ArrayList<>();
        try (PreparedStatement statement = connection.prepareStatement(GRAFICO_2_QUERY)) {

              try (ResultSet result = statement.executeQuery()) {
                    while (result.next()) {
                        Grafico grafico = new Grafico();
                        grafico.setMes(result.getInt("mes"));
                        grafico.setQuantity(result.getDouble("qtd"));
                        ResultJSON.add(grafico);
                    }
                }
            } catch (SQLException ex) {
                Logger.getLogger(PgUserDAO.class.getName()).log(Level.SEVERE, "DAO", ex);

                throw new SQLException("Erro dados");
            }     
        return ResultJSON;
    }
    
    @Override
    public List<Grafico> getGrafico3() throws SQLException {
        List<Grafico> ResultJSON = new ArrayList<>();
        try (PreparedStatement statement = connection.prepareStatement(GRAFICO_3_QUERY)) {

              try (ResultSet result = statement.executeQuery()) {
                    while (result.next()) {
                        Grafico grafico = new Grafico();
                        grafico.setIdade(result.getInt("idade"));
                        grafico.setQuantity(result.getDouble("qtd"));
                        ResultJSON.add(grafico);
                    }
                }
            } catch (SQLException ex) {
                Logger.getLogger(PgUserDAO.class.getName()).log(Level.SEVERE, "DAO", ex);

                throw new SQLException("Erro dados");
            }     
        return ResultJSON;
    }
    
    @Override
    public List<Grafico> getGrafico4() throws SQLException {
    List<Grafico> ResultJSON = new ArrayList<>();
        try (PreparedStatement statement = connection.prepareStatement(GRAFICO_4_QUERY)) {

              try (ResultSet result = statement.executeQuery()) {
                  System.out.println(result);
                    while (result.next()) {
                        Grafico grafico = new Grafico();
                        grafico.setMes(result.getInt("mes"));
                        grafico.setPrecipitation(result.getDouble("precipitacao"));
                        grafico.setDVS(result.getDouble("DSV"));
                        ResultJSON.add(grafico);
                    }
                }
            } catch (SQLException ex) {
                Logger.getLogger(PgUserDAO.class.getName()).log(Level.SEVERE, "DAO", ex);

                throw new SQLException("Erro dados");
            }     
        return ResultJSON;    
    }


    @Override
    public void create(Grafico t) throws SQLException {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public Grafico read(Integer id) throws SQLException {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public void update(Grafico t) throws SQLException {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public void delete(Integer id) throws SQLException {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public List<Grafico> all() throws SQLException {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }
    
}
