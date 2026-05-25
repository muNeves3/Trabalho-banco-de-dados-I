/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package controller;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import dao.DAOFactory;
import dao.GraficoDAO;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import model.*;

/**
 *
 * @author luana
 */

@WebServlet(name = "DadosController", 
        urlPatterns = {
            "/dados/grafico1",
            "/dados/grafico2",
            "/dados/grafico3",
            "/dados/grafico4",            
        })

public class DadosController extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        GraficoDAO dao;
        response.addHeader("Access-Control-Allow-Origin", "*");
        response.addHeader("Access-Control-Allow-Methods", "POST, GET, OPTIONS, PUT, DELETE, HEAD");
        response.addHeader("Access-Control-Allow-Headers", "X-PINGOTHER, Origin, X-Requested-With, Content-Type, Accept");
        response.addHeader("Access-Control-Max-Age", "1728000");
        
        switch (request.getServletPath()) {
            case "/dados/grafico1": {
                try (DAOFactory daoFactory = DAOFactory.getInstance()){
                    dao = daoFactory.getGraficoDAO();
                    Gson gson = new GsonBuilder().setDateFormat("dd/MM/yyyy").create();
                                        
                    List<Grafico> dadosList = dao.getGrafico1();
                    String json = gson.toJson(dadosList);

                    response.getOutputStream().print(json);
                } catch (ClassNotFoundException | IOException | SQLException ex) {
                    request.getSession().setAttribute("error", ex.getMessage());
                }
                break;
            }
            
            case "/dados/grafico2": {
                try (DAOFactory daoFactory = DAOFactory.getInstance()){
                    dao = daoFactory.getGraficoDAO();
                    Gson gson = new GsonBuilder().setDateFormat("dd/MM/yyyy").create();
                                        
                    List<Grafico> dadosList = dao.getGrafico2();
                    String json = gson.toJson(dadosList);
                    response.getOutputStream().print(json);
                } catch (ClassNotFoundException | IOException | SQLException ex) {
                    request.getSession().setAttribute("error", ex.getMessage());
                }
                break;
            }
            case "/dados/grafico3": {
                try (DAOFactory daoFactory = DAOFactory.getInstance()){
                    dao = daoFactory.getGraficoDAO();
                    Gson gson = new GsonBuilder().setDateFormat("dd/MM/yyyy").create();
                                        
                    List<Grafico> dadosList = dao.getGrafico3();
                    String json = gson.toJson(dadosList);
                    response.getOutputStream().print(json);
                } catch (ClassNotFoundException | IOException | SQLException ex) {
                    request.getSession().setAttribute("error", ex.getMessage());
                }
                break;
            }
            case "/dados/grafico4": {
                try (DAOFactory daoFactory = DAOFactory.getInstance()){
                    dao = daoFactory.getGraficoDAO();
                    Gson gson = new GsonBuilder().setDateFormat("dd/MM/yyyy").create();
                                        
                    List<Grafico> dadosList = dao.getGrafico4();
                    String json = gson.toJson(dadosList);
                    response.getOutputStream().print(json);
                } catch (ClassNotFoundException | IOException | SQLException ex) {
                    request.getSession().setAttribute("error", ex.getMessage());
                }
                break;
            }
        }
    }
}
