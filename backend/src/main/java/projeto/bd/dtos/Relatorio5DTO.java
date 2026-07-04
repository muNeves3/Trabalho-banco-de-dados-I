package projeto.bd.dtos;

public class Relatorio5DTO {
    private Integer datasetId;
    private String nomeDataset;
    private Integer totalVersoes;
    private String tempoMedio;

    public Integer getDatasetId() { return datasetId; }
    public void setDatasetId(Integer datasetId) { this.datasetId = datasetId; }
    public String getNomeDataset() { return nomeDataset; }
    public void setNomeDataset(String nomeDataset) { this.nomeDataset = nomeDataset; }
    public Integer getTotalVersoes() { return totalVersoes; }
    public void setTotalVersoes(Integer totalVersoes) { this.totalVersoes = totalVersoes; }
    public String getTempoMedio() { return tempoMedio; }
    public void setTempoMedio(String tempoMedio) { this.tempoMedio = tempoMedio; }
    
}
