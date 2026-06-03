package projeto.bd.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import projeto.bd.models.Feature;

public class PgFeatureDAO implements FeatureDAO {

    private final Connection connection;

    private static final String CREATE_QUERY =
            "INSERT INTO sistema.feature(nome, tipo, descricao, dataset_id, numero_versao) " +
            "VALUES(?, ?, ?, ?, ?);";

    private static final String READ_QUERY =
            "SELECT id, nome, tipo, descricao, dataset_id, numero_versao " +
            "FROM sistema.feature " +
            "WHERE id = ?;";

    private static final String UPDATE_QUERY =
            "UPDATE sistema.feature " +
            "SET nome = ?, tipo = ?, descricao = ?, dataset_id = ?, numero_versao = ? " +
            "WHERE id = ?;";

    private static final String DELETE_QUERY =
            "DELETE FROM sistema.feature " +
            "WHERE id = ?;";

    private static final String ALL_QUERY =
            "SELECT id, nome, tipo, descricao, dataset_id, numero_versao " +
            "FROM sistema.feature " +
            "ORDER BY id;";

    public PgFeatureDAO(Connection connection) {
        this.connection = connection;
    }

    @Override
    public void create(Feature feature) throws SQLException {
        try (PreparedStatement statement = connection.prepareStatement(CREATE_QUERY)) {
            statement.setString(1, feature.getNome());
            statement.setString(2, feature.getTipo());
            statement.setString(3, feature.getDescricao());
            statement.setInt(4, feature.getDatasetId());
            statement.setInt(5, feature.getNumeroVersao());

            statement.executeUpdate();
        } catch (SQLException ex) {
            Logger.getLogger(PgFeatureDAO.class.getName()).log(Level.SEVERE, "DAO", ex);
            throw new SQLException("Erro ao inserir a feature.");
        }
    }

    @Override
    public Feature read(Integer id) throws SQLException {
        Feature feature = new Feature();

        try (PreparedStatement statement = connection.prepareStatement(READ_QUERY)) {
            statement.setInt(1, id);
            try (ResultSet result = statement.executeQuery()) {
                if (result.next()) {
                    feature.setId(result.getInt("id"));
                    feature.setNome(result.getString("nome"));
                    feature.setTipo(result.getString("tipo"));
                    feature.setDescricao(result.getString("descricao"));
                    feature.setDatasetId(result.getInt("dataset_id"));
                    feature.setNumeroVersao(result.getInt("numero_versao"));
                } else {
                    throw new SQLException("Erro: feature não encontrada.");
                }
            }
        } catch (SQLException ex) {
            Logger.getLogger(PgFeatureDAO.class.getName()).log(Level.SEVERE, "DAO", ex);
            throw new SQLException("Erro ao visualizar a feature.");
        }
        return feature;
    }

    @Override
    public void update(Feature feature) throws SQLException {
        try (PreparedStatement statement = connection.prepareStatement(UPDATE_QUERY)) {
            statement.setString(1, feature.getNome());
            statement.setString(2, feature.getTipo());
            statement.setString(3, feature.getDescricao());
            statement.setInt(4, feature.getDatasetId());
            statement.setInt(5, feature.getNumeroVersao());
            statement.setInt(6, feature.getId()); 

            if (statement.executeUpdate() < 1) {
                throw new SQLException("Erro: feature não encontrada para edição.");
            }
        } catch (SQLException ex) {
            Logger.getLogger(PgFeatureDAO.class.getName()).log(Level.SEVERE, "DAO", ex);
            throw new SQLException("Erro ao editar a feature.");
        }
    }

    @Override
    public void delete(Integer id) throws SQLException {
        try (PreparedStatement statement = connection.prepareStatement(DELETE_QUERY)) {
            statement.setInt(1, id);
            if (statement.executeUpdate() < 1) {
                throw new SQLException("Erro: feature não encontrada para exclusão.");
            }
        } catch (SQLException ex) {
            Logger.getLogger(PgFeatureDAO.class.getName()).log(Level.SEVERE, "DAO", ex);
            throw new SQLException("Erro ao excluir a feature.");
        }
    }

    @Override
    public List<Feature> all() throws SQLException {
        List<Feature> featureList = new ArrayList<>();

        try (PreparedStatement statement = connection.prepareStatement(ALL_QUERY);
             ResultSet result = statement.executeQuery()) {
            while (result.next()) {
                Feature feature = new Feature();
                feature.setId(result.getInt("id"));
                feature.setNome(result.getString("nome"));
                feature.setTipo(result.getString("tipo"));
                feature.setDescricao(result.getString("descricao"));
                feature.setDatasetId(result.getInt("dataset_id"));
                feature.setNumeroVersao(result.getInt("numero_versao"));

                featureList.add(feature);
            }
        } catch (SQLException ex) {
            Logger.getLogger(PgFeatureDAO.class.getName()).log(Level.SEVERE, "DAO", ex);
            throw new SQLException("Erro ao listar features.");
        }
        return featureList;
    }
}