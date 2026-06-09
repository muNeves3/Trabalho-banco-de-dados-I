package projeto.bd.models;

import java.util.Date;

public class AcessoVersao {
    private int VersaoDatasetNumVersao;
    private String usuarioCpf;
    private int datasetId;
    private Date acessadoEm;

    public AcessoVersao(int versaoDatasetNumVersao, String usuarioCpf, int datasetId, Date acessadoEm) {
        this.VersaoDatasetNumVersao = versaoDatasetNumVersao;
        this.usuarioCpf = usuarioCpf;
        this.datasetId = datasetId;
        this.acessadoEm = acessadoEm;
    }

    public AcessoVersao() {
    }

    public int getVersaoDatasetNumVersao() {
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

    public void setUsuarioCpf(String usuarioCpf) {
        this.usuarioCpf = usuarioCpf;
    }

    public void setDatasetId(int datasetId) {
        this.datasetId = datasetId;
    }
}