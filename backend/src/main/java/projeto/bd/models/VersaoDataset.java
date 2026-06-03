// model/VersaoDataset.java
package model;

import java.sql.Timestamp;

public class VersaoDataset {
    private Integer numVersao; 
    private String datasetNome; 
    private String descricao;
    private String caminhoArquivo;
    private Timestamp criadoEm;
    private String usuarioEditorCpf; 
    private Integer versaoBaseNum; 
    
   public Integer getNumVersao() { return numVersao; }
    public void setNumVersao(Integer numVersao) { this.numVersao = numVersao; }
    public String getDatasetNome() { return datasetNome; }
    public void setDatasetNome(String datasetNome) { this.datasetNome = datasetNome; }
    public String getDescricao() { return descricao; }
    public void setDescricao(String descricao) { this.descricao = descricao; }
    public String getCaminhoArquivo() { return caminhoArquivo; }
    public void setCaminhoArquivo(String caminhoArquivo) { this.caminhoArquivo = caminhoArquivo; }
    public Timestamp getCriadoEm() { return criadoEm; }
    public void setCriadoEm(Timestamp criadoEm) { this.criadoEm = criadoEm; }
    public String getUsuarioEditorCpf() { return usuarioEditorCpf; }
    public void setUsuarioEditorCpf(String usuarioEditorCpf) { this.usuarioEditorCpf = usuarioEditorCpf; }
    public Integer getVersaoBaseNum() { return versaoBaseNum; }
    public void setVersaoBaseNum(Integer versaoBaseNum) { this.versaoBaseNum = versaoBaseNum; }
}