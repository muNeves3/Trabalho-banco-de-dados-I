package projeto.bd.dao;

import java.sql.SQLException;
import java.util.List;
import projeto.bd.dtos.RelatorioRankingDTO;

public interface RelatorioDAO {
    public List<RelatorioRankingDTO> rankingDatasetsMaisAcessados() throws SQLException;
}