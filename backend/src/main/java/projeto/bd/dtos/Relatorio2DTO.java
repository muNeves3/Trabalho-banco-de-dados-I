package projeto.bd.dtos;

public class Relatorio2DTO {
    private Integer datasetId;
    private String nomeDataset;
    private Integer totalDownloads;
    private Integer totalVisualizacoes;
    private Integer totalAcessos;

    public Integer getDatasetId() { return datasetId; }
    public void setDatasetId(Integer datasetId) { this.datasetId = datasetId; }
    public String getNomeDataset() { return nomeDataset; }
    public void setNomeDataset(String nomeDataset) { this.nomeDataset = nomeDataset; }
    public Integer getTotalDownloads() { return totalDownloads; }
    public void setTotalDownloads(Integer totalDownloads) { this.totalDownloads = totalDownloads; }
    public Integer getTotalVisualizacoes() { return totalVisualizacoes; }
    public void setTotalVisualizacoes(Integer totalVisualizacoes) { this.totalVisualizacoes = totalVisualizacoes; }
    public Integer getTotalAcessos() { return totalAcessos; }
    public void setTotalAcessos(Integer totalAcessos) { this.totalAcessos = totalAcessos; }
}