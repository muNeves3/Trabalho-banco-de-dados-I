package projeto.bd.dao;

import java.sql.SQLException;
import java.util.List;
import projeto.bd.dtos.Relatorio2DTO;
import projeto.bd.dtos.Relatorio5DTO;;

public interface RelatorioDAO {
    public List<Relatorio2DTO> rankingDatasetsMaisAcessados() throws SQLException;
    public List<Relatorio5DTO> versoesPorDatasets() throws SQLException;
}