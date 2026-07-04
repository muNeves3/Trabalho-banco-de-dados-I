package projeto.bd.dao;

import java.sql.SQLException;
import java.util.List;
import projeto.bd.dtos.Relatorio2DTO;
import projeto.bd.dtos.Relatorio5DTO;
import projeto.bd.dtos.Relatorio3DTO;

public interface RelatorioDAO {
    public List<Relatorio2DTO> rankingDatasetsMaisAcessados() throws SQLException;
    public List<Relatorio5DTO> versoesPorDatasets() throws SQLException;
    public List<Relatorio3DTO> usuariosMaisContribuintes() throws SQLException;
    public List<Relatorio3DTO> usuariosMaisAcessos() throws SQLException;
    public List<Relatorio3DTO> usuariosMaisDownloads() throws SQLException;
}