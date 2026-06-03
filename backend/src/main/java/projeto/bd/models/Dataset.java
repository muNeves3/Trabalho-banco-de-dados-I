package projeto.bd.models;

import java.sql.Timestamp;

public class Dataset {
    private Integer id; 
    private String nome; 
    private String descricao;
    private String fontes;
    private String criadorCpf; 
    private Timestamp criadoEm;
 
    
   public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }
    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }
    public String getDescricao() { return descricao; }
    public void setDescricao(String descricao) { this.descricao = descricao; }
    public String getFontes() { return fontes; }
    public void setFontes(String fontes) { this.fontes = fontes; }
    public String getCriadorCpf() { return criadorCpf; }
    public void setCriadorCpf(String criadorCpf) { this.criadorCpf = criadorCpf; }
    public Timestamp getCriadoEm() { return criadoEm; }
    public void setCriadoEm(Timestamp criadoEm) { this.criadoEm = criadoEm; }
}
