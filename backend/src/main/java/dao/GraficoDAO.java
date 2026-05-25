/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dao;

import java.sql.SQLException;
import java.util.List;
import model.*;

/**
 *
 * @author luana
 */
public interface GraficoDAO  extends DAO<Grafico> {
    public List<Grafico> getGrafico1() throws SQLException;
    public List<Grafico> getGrafico2() throws SQLException;
    public List<Grafico> getGrafico3() throws SQLException;
    public List<Grafico> getGrafico4() throws SQLException;
}
