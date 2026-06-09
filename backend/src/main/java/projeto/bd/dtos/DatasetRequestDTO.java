package projeto.bd.dtos;

public class DatasetRequestDTO {
    private String nome;
    private String descricao;
    private String fontes;
    private String criadorCpf;

    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }
    
    public String getDescricao() { return descricao; }
    public void setDescricao(String descricao) { this.descricao = descricao; }
    
    public String getFontes() { return fontes; }
    public void setFontes(String fontes) { this.fontes = fontes; }

    public String getCriadorCpf() { return criadorCpf; }
    public void setCriadorCpf(String criadorCpf) { this.criadorCpf = criadorCpf; }
}