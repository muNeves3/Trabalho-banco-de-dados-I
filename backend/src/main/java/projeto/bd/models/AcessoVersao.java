package projeto.bd.models;

import java.util.Date;

public class AcessoVersao {
    private int VersaoDatasetNumVersao;
    private String usuarioCpf;
    private int datasetId;
    private Date acessadoEm;
    private String tipoAcesso;

    public AcessoVersao() {
    }

    public int getVersaoDatasetNumVersao() {
        return VersaoDatasetNumVersao;
    }

    public int getNumeroVersao() {
        return VersaoDatasetNumVersao;
    }

    public String getUsuarioCpf() {
        return usuarioCpf;
    }

    public int getDatasetId() {
        return datasetId;
    }

    public Date getAcessadoEm() {
        return acessadoEm;
    }

    public void setVersaoDatasetNumVersao(int versaoDatasetNumVersao) {
        this.VersaoDatasetNumVersao = versaoDatasetNumVersao;
    }

    public void setNumeroVersao(int numeroVersao) {
        this.VersaoDatasetNumVersao = numeroVersao;
    }

    public void setUsuarioCpf(String usuarioCpf) {
        this.usuarioCpf = usuarioCpf;
    }

    public void setDatasetId(int datasetId) {
        this.datasetId = datasetId;
    }

    public void setAcessadoEm(Date acessadoEm) {
        this.acessadoEm = acessadoEm;
    }

    public String getTipoAcesso() {
        return tipoAcesso;
    }

    public void setTipoAcesso(String tipoAcesso) {
        this.tipoAcesso = tipoAcesso;
    }
}