package projeto.bd.dtos;

public class AcessoVersaoRequestDTO {

    private String usuarioCpf;
    private int datasetId;
    private int numeroVersao;
    private String tipoAcesso;

    public AcessoVersaoRequestDTO() {
    }

    public AcessoVersaoRequestDTO(String usuarioCpf, int datasetId, int numeroVersao, String tipoAcesso) {
        this.usuarioCpf = usuarioCpf;
        this.datasetId = datasetId;
        this.numeroVersao = numeroVersao;
        this.tipoAcesso = tipoAcesso;
    }

    public String getUsuarioCpf() {
        return usuarioCpf;
    }

    public void setUsuarioCpf(String usuarioCpf) {
        this.usuarioCpf = usuarioCpf;
    }

    public int getDatasetId() {
        return datasetId;
    }

    public void setDatasetId(int datasetId) {
        this.datasetId = datasetId;
    }

    public int getNumeroVersao() {
        return numeroVersao;
    }

    public void setNumeroVersao(int numeroVersao) {
        this.numeroVersao = numeroVersao;
    }

    public String getTipoAcesso() {
        return tipoAcesso;
    }

    public void setTipoAcesso(String tipoAcesso) {
        this.tipoAcesso = tipoAcesso;
    }
}