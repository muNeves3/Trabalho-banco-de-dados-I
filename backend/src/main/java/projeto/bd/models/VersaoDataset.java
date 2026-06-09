package projeto.bd.models;

import java.sql.Timestamp;

public class VersaoDataset {
    private Integer datasetId; 
    private Integer numeroVersao;
    private Integer versaoBaseNumero; 
    private String criadorCpf; 
    private String descModificacoes;
    private byte[] arquivo;
    private Timestamp criadoEm;

    public Integer getDatasetId() { return datasetId; }
    public void setDatasetId(Integer datasetId) { this.datasetId = datasetId; }
    public Integer getNumeroVersao() { return numeroVersao; }
    public void setNumeroVersao(Integer numeroVersao) { this.numeroVersao = numeroVersao; }
    public Integer getVersaoBaseNumero() { return versaoBaseNumero; }
    public void setVersaoBaseNumero(Integer versaoBaseNumero) { this.versaoBaseNumero = versaoBaseNumero; }
    public String getCriadorCpf() { return criadorCpf; }
    public void setCriadorCpf(String criadorCpf) { this.criadorCpf = criadorCpf; }
    public String getDescModificacoes() { return descModificacoes; }
    public void setDescModificacoes(String descModificacoes) { this.descModificacoes = descModificacoes; }
    public byte[] getArquivo() { return arquivo; }
    public void setArquivo(byte[] arquivo) { this.arquivo = arquivo; }
    public Timestamp getCriadoEm() { return criadoEm; }
    public void setCriadoEm(Timestamp criadoEm) { this.criadoEm = criadoEm; }
}