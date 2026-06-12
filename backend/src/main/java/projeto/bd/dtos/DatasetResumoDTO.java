package projeto.bd.dtos;

import java.sql.Timestamp;

public class DatasetResumoDTO {
    private Integer id; 
    private String nome;  
    private String criadorCpf; 
    private Timestamp criadoEm;
    private Integer quantidadeVersoes;

    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }

    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }

    public String getCriadorCpf() { return criadorCpf; }
    public void setCriadorCpf(String criadorCpf) { this.criadorCpf = criadorCpf; }

    public Timestamp getCriadoEm() { return criadoEm; }
    public void setCriadoEm(Timestamp criadoEm) { this.criadoEm = criadoEm; }

    public Integer getQuantidadeVersoes() { return quantidadeVersoes; }
    public void setQuantidadeVersoes(Integer quantidadeVersoes) { 
        this.quantidadeVersoes = quantidadeVersoes; 
    }



}