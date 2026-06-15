package projeto.bd.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class PgFonteDatasetDAO implements FonteDatasetDAO {

    private final Connection connection;

    private static final String CREATE_QUERY =
            "INSERT INTO sistema.fonte_dataset(dataset_id, fonte) VALUES(?, ?);";

    private static final String DELETE_QUERY =
            "DELETE FROM sistema.fonte_dataset WHERE dataset_id = ? AND fonte = ?;";

    private static final String LIST_BY_DATASET_QUERY =
            "SELECT fonte FROM sistema.fonte_dataset WHERE dataset_id = ? ORDER BY fonte;";

    public PgFonteDatasetDAO(Connection connection) {
        this.connection = connection;
    }

    @Override
    public void create(Integer datasetId, String fonte) throws SQLException {
        try (PreparedStatement statement = connection.prepareStatement(CREATE_QUERY)) {
            statement.setInt(1, datasetId);
            statement.setString(2, fonte);
            statement.executeUpdate();
        } catch (SQLException ex) {
            Logger.getLogger(PgFonteDatasetDAO.class.getName()).log(Level.SEVERE, "DAO", ex);
            throw new SQLException("Erro ao inserir fonte do dataset.");
        }
    }

    @Override
    public void delete(Integer datasetId, String fonte) throws SQLException {
        try (PreparedStatement statement = connection.prepareStatement(DELETE_QUERY)) {
            statement.setInt(1, datasetId);
            statement.setString(2, fonte);
            if (statement.executeUpdate() < 1) {
                throw new SQLException("Fonte não encontrada para exclusão.");
            }
        } catch (SQLException ex) {
            Logger.getLogger(PgFonteDatasetDAO.class.getName()).log(Level.SEVERE, "DAO", ex);
            throw new SQLException("Erro ao excluir fonte do dataset.");
        }
    }

    @Override
    public List<String> listarPorDataset(Integer datasetId) throws SQLException {
        List<String> fontes = new ArrayList<>();
        try (PreparedStatement statement = connection.prepareStatement(LIST_BY_DATASET_QUERY)) {
            statement.setInt(1, datasetId);
            try (ResultSet result = statement.executeQuery()) {
                while (result.next()) {
                    fontes.add(result.getString("fonte"));
                }
            }
        } catch (SQLException ex) {
            Logger.getLogger(PgFonteDatasetDAO.class.getName()).log(Level.SEVERE, "DAO", ex);
            throw new SQLException("Erro ao listar fontes do dataset.");
        }
        return fontes;
    }
}