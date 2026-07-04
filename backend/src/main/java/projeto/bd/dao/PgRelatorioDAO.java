package projeto.bd.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import projeto.bd.dtos.RelatorioRankingDTO;

public class PgRelatorioDAO implements RelatorioDAO {

    private final Connection connection;

    private static final String RANKING_QUERY =
        "SELECT d.id AS dataset_id, d.nome AS nome_dataset, " +
        "COUNT(CASE WHEN a.tipo_acesso = 'download' THEN 1 END) AS total_downloads, " +
        "COUNT(CASE WHEN a.tipo_acesso = 'visualizacao' THEN 1 END) AS total_visualizacoes, " +
        "COUNT(a.tipo_acesso) AS total_acessos " +
        "FROM sistema.dataset d " +
        "LEFT JOIN sistema.acesso_versao a ON d.id = a.dataset_id " +
        "GROUP BY d.id, d.nome " +
        "ORDER BY total_acessos DESC;";

    public PgRelatorioDAO(Connection connection) {
        this.connection = connection;
    }

    @Override
    public List<RelatorioRankingDTO> rankingDatasetsMaisAcessados() throws SQLException {
        List<RelatorioRankingDTO> ranking = new ArrayList<>();
        try (PreparedStatement statement = connection.prepareStatement(RANKING_QUERY);
             ResultSet result = statement.executeQuery()) {
            while (result.next()) {
                RelatorioRankingDTO dto = new RelatorioRankingDTO();
                dto.setDatasetId(result.getInt("dataset_id"));
                dto.setNomeDataset(result.getString("nome_dataset"));
                dto.setTotalDownloads(result.getInt("total_downloads"));
                dto.setTotalVisualizacoes(result.getInt("total_visualizacoes"));
                dto.setTotalAcessos(result.getInt("total_acessos"));
                ranking.add(dto);
            }
        } catch (SQLException ex) {
            Logger.getLogger(PgRelatorioDAO.class.getName()).log(Level.SEVERE, "DAO", ex);
            throw new SQLException("Erro ao gerar ranking de datasets.");
        }
        return ranking;
    }
}