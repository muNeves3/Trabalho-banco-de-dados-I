/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model;

/**
 *
 * @author luana
 */
public class Grafico {

    private Integer Mes;
    private Double Quantity;
    private Double Precipitation;
    private Double DVS;
    private Integer Idade;
    
    
    public Integer getMes() {
        return Mes;
    }

    public void setMes(Integer Mes) {
        this.Mes = Mes;
    }

    public Double getQuantity() {
        return Quantity;
    }

    public void setQuantity(Double Quantity) {
        this.Quantity = Quantity;
    }

    public Double getPrecipitation() {
        return Precipitation;
    }

    public void setPrecipitation(Double Precipitation) {
        this.Precipitation = Precipitation;
    }

    public Double getDVS() {
        return DVS;
    }

    public void setDVS(Double DVS) {
        this.DVS = DVS;
    }
    
    public Integer getIdade(){
        return Idade;
    }
    public void setIdade(Integer Idade){
        this.Idade = Idade;
    }
}
