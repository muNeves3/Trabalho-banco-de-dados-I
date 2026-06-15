package projeto.bd.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import projeto.bd.dtos.DatasetResumoDTO;
import projeto.bd.models.Dataset;

public class PgDatasetDAO implements DatasetDAO {

    private final Connection connection;
    private final PgVersaoDatasetDAO versaoDatasetDAO;

    // --- STRINGS SQL ---
    private static final String CREATE_QUERY =
            "INSERT INTO sistema.dataset(nome, descricao, criador_cpf, criado_em) " +
            "VALUES(?, ?, ?, ?) RETURNING id;";

    private static final String READ_QUERY =
            "SELECT id, nome, descricao, criador_cpf, criado_em " +
            "FROM sistema.dataset " +
            "WHERE id = ?;";

    private static final String UPDATE_QUERY =
            "UPDATE sistema.dataset " +
            "SET nome = ?, descricao = ?, criador_cpf = ?, criado_em = ? " +
            "WHERE id = ?;";

    private static final String DELETE_QUERY =
            "DELETE FROM sistema.dataset " +
            "WHERE id = ?;";

    private static final String ALL_QUERY =
            "SELECT id, nome, descricao, criador_cpf, criado_em " +
            "FROM sistema.dataset " +
            "ORDER BY id;";

    private static final String ALL_RESUMO_QUERY =
            "SELECT d.id, d.nome, d.criador_cpf, d.criado_em, COUNT(v.numero_versao) AS quantidade_versoes " + 
            "FROM sistema.dataset d " +
            "LEFT JOIN sistema.versao_dataset v ON d.id = v.dataset_id " +
            "GROUP BY d.id, d.nome, d.criador_cpf, d.criado_em " +
            "ORDER BY d.id";

    private static final String DOWNLOAD_CSV_QUERY =
            "SELECT arquivo " +
            "FROM sistema.versao_dataset " +
            "WHERE dataset_id = ? " +
            "ORDER BY numero_versao DESC " +
            "LIMIT 1;";

    public PgDatasetDAO(Connection connection, PgVersaoDatasetDAO versaoDatasetDAO) {
        this.connection = connection;
        this.versaoDatasetDAO = versaoDatasetDAO;
    }

    @Override
    public void create(Dataset dataset) throws SQLException {
        boolean autoCommitAnterior = connection.getAutoCommit();
        try {
            connection.setAutoCommit(false);
            Integer datasetIdCriado;
            try (PreparedStatement statement = connection.prepareStatement(CREATE_QUERY)) {
                statement.setString(1, dataset.getNome());
                statement.setString(2, dataset.getDescricao());
                statement.setString(3, dataset.getCriadorCpf());

                if (dataset.getCriadoEm() != null) {
                    statement.setTimestamp(4, dataset.getCriadoEm());
                } else {
                    statement.setTimestamp(4, java.sql.Timestamp.valueOf(java.time.LocalDateTime.now()));
                }

                try (ResultSet result = statement.executeQuery()) {
                    if (!result.next()) {
                        throw new SQLException("Erro ao inserir o dataset.");
                    }
                    datasetIdCriado = result.getInt("id");
                    dataset.setId(datasetIdCriado);
                }
            }

            this.versaoDatasetDAO.criarVersaoInicialDataset(datasetIdCriado, dataset.getCriadorCpf());
            connection.commit();
        } catch (SQLException ex) {
            connection.rollback();
            Logger.getLogger(PgDatasetDAO.class.getName()).log(Level.SEVERE, "DAO", ex);
            throw new SQLException("Erro ao inserir o dataset.");
        } finally {
            connection.setAutoCommit(autoCommitAnterior);
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
            statement.setString(3, dataset.getCriadorCpf());
            statement.setTimestamp(4, dataset.getCriadoEm());
            statement.setInt(5, dataset.getId()); // O ID vai no WHERE

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

    @Override
    public List<DatasetResumoDTO> allResumo () throws SQLException {    
        List<DatasetResumoDTO> datasetList = new ArrayList<>();

        try (PreparedStatement statement = connection.prepareStatement(ALL_RESUMO_QUERY);
             ResultSet result = statement.executeQuery()) {
            while (result.next()) {
                DatasetResumoDTO dataset = new DatasetResumoDTO();
                dataset.setId(result.getInt("id"));
                dataset.setNome(result.getString("nome"));
                dataset.setCriadorCpf(result.getString("criador_cpf"));
                dataset.setCriadoEm(result.getTimestamp("criado_em"));
                dataset.setQuantidadeVersoes(result.getInt("quantidade_versoes"));

                datasetList.add(dataset);
            }
        } catch (SQLException ex) {
            Logger.getLogger(PgDatasetDAO.class.getName()).log(Level.SEVERE, "DAO", ex);
            throw new SQLException("Erro ao listar resumos de datasets.");
        }
        return datasetList;
    }

    @Override
    public byte[] downloadCsv(Integer id) throws SQLException {
        try (PreparedStatement statement = connection.prepareStatement(DOWNLOAD_CSV_QUERY)) {
            statement.setInt(1, id);
            try (ResultSet result = statement.executeQuery()) {
                if (result.next()) {
                    byte[] arquivo = result.getBytes("arquivo");
                    if (arquivo == null || arquivo.length == 0) {
                        throw new SQLException("Erro: dataset sem arquivo CSV para download.");
                    }
                    return arquivo;
                }
                throw new SQLException("Erro: versão do dataset não encontrada para download.");
            }
        } catch (SQLException ex) {
            Logger.getLogger(PgDatasetDAO.class.getName()).log(Level.SEVERE, "DAO", ex);
            throw new SQLException("Erro ao baixar dataset.");
        }
    }
}