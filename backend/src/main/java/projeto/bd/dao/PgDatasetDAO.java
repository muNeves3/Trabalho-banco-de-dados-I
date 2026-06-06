package projeto.bd.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import projeto.bd.models.Dataset;

public class PgDatasetDAO implements DatasetDAO {

    private final Connection connection;

    // --- STRINGS SQL ---
    private static final String CREATE_QUERY =
            "INSERT INTO sistema.dataset(nome, descricao, fontes, criador_cpf, criado_em) " +
            "VALUES(?, ?, ?, ?, ?);";

    private static final String READ_QUERY =
            "SELECT id, nome, descricao, fontes, criador_cpf, criado_em " +
            "FROM sistema.dataset " +
            "WHERE id = ?;";

    private static final String UPDATE_QUERY =
            "UPDATE sistema.dataset " +
            "SET nome = ?, descricao = ?, fontes = ?, criador_cpf = ?, criado_em = ? " +
            "WHERE id = ?;";

    private static final String DELETE_QUERY =
            "DELETE FROM sistema.dataset " +
            "WHERE id = ?;";

    private static final String ALL_QUERY =
            "SELECT id, nome, descricao, fontes, criador_cpf, criado_em " +
            "FROM sistema.dataset " +
            "ORDER BY id;";

    // Construtor
    public PgDatasetDAO(Connection connection) {
        this.connection = connection;
    }

    @Override
    public void create(Dataset dataset) throws SQLException {
        try (PreparedStatement statement = connection.prepareStatement(CREATE_QUERY)) {
            statement.setString(1, dataset.getNome());
            statement.setString(2, dataset.getDescricao());
            statement.setString(3, dataset.getFontes());
            statement.setString(4, dataset.getCriadorCpf());

            if (dataset.getCriadoEm() != null) {
                statement.setTimestamp(5, dataset.getCriadoEm());
            } else {
                statement.setTimestamp(5, java.sql.Timestamp.valueOf(java.time.LocalDateTime.now()));
            }

            statement.executeUpdate();
        } catch (SQLException ex) {
            Logger.getLogger(PgDatasetDAO.class.getName()).log(Level.SEVERE, "DAO", ex);
            throw new SQLException("Erro ao inserir o dataset.");
        }
    }

    @Override
    public Dataset read(Integer id) throws SQLException {
        Dataset dataset = new Dataset();

        try (PreparedStatement statement = connection.prepareStatement(READ_QUERY)) {
            statement.setInt(1, id);
            try (ResultSet result = statement.executeQuery()) {
                if (result.next()) {
                    dataset.setId(result.getInt("id"));
                    dataset.setNome(result.getString("nome"));
                    dataset.setDescricao(result.getString("descricao"));
                    dataset.setFontes(result.getString("fontes"));
                    dataset.setCriadorCpf(result.getString("criador_cpf"));
                    dataset.setCriadoEm(result.getTimestamp("criado_em"));
                } else {
                    throw new SQLException("Erro: dataset não encontrado.");
                }
            }
        } catch (SQLException ex) {
            Logger.getLogger(PgDatasetDAO.class.getName()).log(Level.SEVERE, "DAO", ex);
            throw new SQLException("Erro ao visualizar o dataset.");
        }
        return dataset;
    }

    @Override
    public void update(Dataset dataset) throws SQLException {
        try (PreparedStatement statement = connection.prepareStatement(UPDATE_QUERY)) {
            statement.setString(1, dataset.getNome());
            statement.setString(2, dataset.getDescricao());
            statement.setString(3, dataset.getFontes());
            statement.setString(4, dataset.getCriadorCpf());
            statement.setTimestamp(5, dataset.getCriadoEm());
            statement.setInt(6, dataset.getId()); // O ID vai no WHERE

            if (statement.executeUpdate() < 1) {
                throw new SQLException("Erro: dataset não encontrado para edição.");
            }
        } catch (SQLException ex) {
            Logger.getLogger(PgDatasetDAO.class.getName()).log(Level.SEVERE, "DAO", ex);
            throw new SQLException("Erro ao editar o dataset.");
        }
    }

    @Override
    public void delete(Integer id) throws SQLException {
        try (PreparedStatement statement = connection.prepareStatement(DELETE_QUERY)) {
            statement.setInt(1, id);
            if (statement.executeUpdate() < 1) {
                throw new SQLException("Erro: dataset não encontrado para exclusão.");
            }
        } catch (SQLException ex) {
            Logger.getLogger(PgDatasetDAO.class.getName()).log(Level.SEVERE, "DAO", ex);
            throw new SQLException("Erro ao excluir o dataset.");
        }
    }

    @Override
    public List<Dataset> all() throws SQLException {
        List<Dataset> datasetList = new ArrayList<>();

        try (PreparedStatement statement = connection.prepareStatement(ALL_QUERY);
             ResultSet result = statement.executeQuery()) {
            while (result.next()) {
                Dataset dataset = new Dataset();
                dataset.setId(result.getInt("id"));
                dataset.setNome(result.getString("nome"));
                dataset.setDescricao(result.getString("descricao"));
                dataset.setFontes(result.getString("fontes"));
                dataset.setCriadorCpf(result.getString("criador_cpf"));
                dataset.setCriadoEm(result.getTimestamp("criado_em"));

                datasetList.add(dataset);
            }
        } catch (SQLException ex) {
            Logger.getLogger(PgDatasetDAO.class.getName()).log(Level.SEVERE, "DAO", ex);
            throw new SQLException("Erro ao listar datasets.");
        }
        return datasetList;
    }
}