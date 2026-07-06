package projeto.bd.dtos;

public class Relatorio1DTO {
    private Integer totalDatasets;
    private Integer totalVersoes;
    private Integer usuariosCadastrados;
    private Double mediaVersoesPorDataset;

    public Integer getTotalDatasets() { return totalDatasets; }
    public void setTotalDatasets(Integer totalDatasets) { this.totalDatasets = totalDatasets; }
    
    public Integer getTotalVersoes() { return totalVersoes; }
    public void setTotalVersoes(Integer totalVersoes) { this.totalVersoes = totalVersoes; }
    
    public Integer getUsuariosCadastrados() { return usuariosCadastrados; }
    public void setUsuariosCadastrados(Integer usuariosCadastrados) { this.usuariosCadastrados = usuariosCadastrados; }
    
    public Double getMediaVersoesPorDataset() { return mediaVersoesPorDataset; }
    public void setMediaVersoesPorDataset(Double mediaVersoesPorDataset) { this.mediaVersoesPorDataset = mediaVersoesPorDataset; }
}
