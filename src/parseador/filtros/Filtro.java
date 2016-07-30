/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package parseador.filtros;

import java.util.Date;

/**
 *
 * @author bruno
 */
public class Filtro {
    
    private String consulta;
    private Date periodoInicio;
    private Date perdiodoFim;
    private String fase;
    private Integer codigoOS;
    private Integer codigoOrgao;
    private Integer codigoUG;
    private String codigoED;
    private String codigoFavorecido;

    public String getConsulta() {
        return consulta;
    }

    public void setConsulta(String consulta) {
        this.consulta = consulta;
    }

    public Date getPeriodoInicio() {
        return periodoInicio;
    }

    public void setPeriodoInicio(Date periodoInicio) {
        this.periodoInicio = periodoInicio;
    }

    public Date getPerdiodoFim() {
        return perdiodoFim;
    }

    public void setPerdiodoFim(Date perdiodoFim) {
        this.perdiodoFim = perdiodoFim;
    }

    public String getFase() {
        return fase;
    }

    public void setFase(String fase) {
        this.fase = fase;
    }

    public Integer getCodigoOS() {
        return codigoOS;
    }

    public void setCodigoOS(Integer codigoOS) {
        this.codigoOS = codigoOS;
    }

    public Integer getCodigoOrgao() {
        return codigoOrgao;
    }

    public void setCodigoOrgao(Integer codigoOrgao) {
        this.codigoOrgao = codigoOrgao;
    }

    public Integer getCodigoUG() {
        return codigoUG;
    }

    public void setCodigoUG(Integer codigoUG) {
        this.codigoUG = codigoUG;
    }

    public String getCodigoED() {
        return codigoED;
    }

    public void setCodigoED(String codigoED) {
        this.codigoED = codigoED;
    }

    public String getCodigoFavorecido() {
        return codigoFavorecido;
    }

    public void setCodigoFavorecido(String codigoFavorecido) {
        this.codigoFavorecido = codigoFavorecido;
    }
    
    
    
}
