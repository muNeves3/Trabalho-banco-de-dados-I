package projeto.bd.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import projeto.bd.models.VersaoDataset;

public class PgVersaoDatasetDAO implements VersaoDatasetDAO {

    private final Connection connection;

    private static final String CREATE_QUERY =
        "INSERT INTO sistema.versao_dataset(dataset_id, versao_base_numero, numero_versao, criador_cpf, desc_modificacoes, arquivo) " +
        "VALUES(?, ?, ?, ?, ?, ?);"; 
        
    private static final String LIST_BY_DATASET_QUERY =
        "SELECT dataset_id, versao_base_numero, numero_versao, criador_cpf, desc_modificacoes, arquivo, criado_em " +
        "FROM sistema.versao_dataset WHERE dataset_id = ? ORDER BY numero_versao ASC;";

    private static final String ALL_QUERY = "SELECT dataset_id, versao_base_numero, numero_versao, criador_cpf, desc_modificacoes, arquivo, criado_em FROM sistema.versao_dataset ORDER BY dataset_id, numero_versao;";

    private static final String READ_QUERY = "SELECT dataset_id, versao_base_numero, numero_versao, criador_cpf, desc_modificacoes, arquivo, criado_em FROM sistema.versao_dataset WHERE dataset_id = ? and numero_versao = ? ORDER BY numero_versao DESC LIMIT 1;";

    private static final String READ_BY_DATASET_AND_VERSION_QUERY = "SELECT dataset_id, versao_base_numero, numero_versao, criador_cpf, desc_modificacoes, arquivo, criado_em FROM sistema.versao_dataset WHERE dataset_id = ? AND numero_versao = ?;";

    private static final String UPDATE_QUERY = "UPDATE sistema.versao_dataset SET versao_base_numero = ?, criador_cpf = ?, desc_modificacoes = ?, arquivo = ? WHERE dataset_id = ? AND numero_versao = ?;";

    private static final String DELETE_QUERY = "DELETE FROM sistema.versao_dataset WHERE dataset_id = ?;";

    private static final String UPLOAD_FILE_QUERY = "UPDATE sistema.versao_dataset SET arquivo = ? WHERE dataset_id = ? AND numero_versao = ?;";

    public PgVersaoDatasetDAO(Connection connection) {
        this.connection = connection;
    }

    public void create(VersaoDataset versao) throws SQLException {
        try (PreparedStatement statement = connection.prepareStatement(CREATE_QUERY)) {
            statement.setInt(1, versao.getDatasetId());
            
            if (versao.getVersaoBaseNumero() != null) {
                statement.setInt(2, versao.getVersaoBaseNumero());
            } else {
                statement.setNull(2, java.sql.Types.INTEGER);
            }
            
            statement.setInt(3, versao.getNumeroVersao());
            statement.setString(4, versao.getCriadorCpf());
            statement.setString(5, versao.getDescModificacoes());
            statement.setBytes(6, versao.getArquivo());
            
            statement.executeUpdate();
        } catch (SQLException ex) {
            Logger.getLogger(PgVersaoDatasetDAO.class.getName()).log(Level.SEVERE, "DAO", ex);
            throw new SQLException("Erro ao inserir versão do dataset.");
        }
    }

    public void criarVersaoInicialDataset(Integer datasetId, String criadorCpf) throws SQLException {
        VersaoDataset versaoInicial = new VersaoDataset();
        versaoInicial.setDatasetId(datasetId);
        versaoInicial.setNumeroVersao(1);
        versaoInicial.setVersaoBaseNumero(null);
        versaoInicial.setCriadorCpf(criadorCpf);
        versaoInicial.setDescModificacoes("Versão inicial");
        versaoInicial.setArquivo(new byte[0]);
        create(versaoInicial);
    }

    public void inserirFeaturesDatasetOriginalVersaoDatasetIncial(Integer datasetId) throws SQLException  {

    }

    @Override
    public List<VersaoDataset> all()  throws SQLException {
        List<VersaoDataset> versoes = new ArrayList<>();
        
        try (PreparedStatement statement = connection.prepareStatement(ALL_QUERY);
             ResultSet result = statement.executeQuery()) {
            while (result.next()) {
                VersaoDataset v = new VersaoDataset();
                v.setDatasetId(result.getInt("dataset_id"));
                
                int versaoBase = result.getInt("versao_base_numero");
                if (!result.wasNull()) {
                    v.setVersaoBaseNumero(versaoBase);
                }
                
                v.setNumeroVersao(result.getInt("numero_versao"));
                v.setCriadorCpf(result.getString("criador_cpf"));
                v.setDescModificacoes(result.getString("desc_modificacoes"));
                v.setArquivo(result.getBytes("arquivo"));
                v.setCriadoEm(result.getTimestamp("criado_em"));
                
                versoes.add(v);
            }
        }
        return versoes;
    }

    @Override
    public List<VersaoDataset> listarPorDataset(Integer datasetId) throws SQLException {
        List<VersaoDataset> versoes = new ArrayList<>();
        try (PreparedStatement statement = connection.prepareStatement(LIST_BY_DATASET_QUERY)) {
            statement.setInt(1, datasetId);
            try (ResultSet result = statement.executeQuery()) {
                while (result.next()) {
                    VersaoDataset v = new VersaoDataset();
                    v.setDatasetId(result.getInt("dataset_id"));
                    
                    int versaoBase = result.getInt("versao_base_numero");
                    if (!result.wasNull()) {
                        v.setVersaoBaseNumero(versaoBase);
                    }
                    
                    v.setNumeroVersao(result.getInt("numero_versao"));
                    v.setCriadorCpf(result.getString("criador_cpf"));
                    v.setDescModificacoes(result.getString("desc_modificacoes"));
                    v.setArquivo(result.getBytes("arquivo"));
                    v.setCriadoEm(result.getTimestamp("criado_em"));
                    
                    versoes.add(v);
                }
            }
        }
        return versoes;
    }

    @Override
    public VersaoDataset read(Integer id, Integer numeroVersao) throws SQLException {
        try (PreparedStatement statement = connection.prepareStatement(READ_QUERY)) {
            statement.setInt(1, id);
            statement.setInt(2, numeroVersao);
            // print read query
            System.out.println("Executing query: " + statement.toString());

            try (ResultSet result = statement.executeQuery()) {
                if (result.next()) {
                    VersaoDataset v = new VersaoDataset();
                    v.setDatasetId(result.getInt("dataset_id"));
                    
                    int versaoBase = result.getInt("versao_base_numero");
                    if (!result.wasNull()) {
                        v.setVersaoBaseNumero(versaoBase);
                    }
                    
                    v.setNumeroVersao(result.getInt("numero_versao"));
                    v.setCriadorCpf(result.getString("criador_cpf"));
                    v.setDescModificacoes(result.getString("desc_modificacoes"));
                    v.setArquivo(result.getBytes("arquivo"));
                    v.setCriadoEm(result.getTimestamp("criado_em"));
                    
                    return v;
                } else {
                    throw new SQLException("Versão não encontrada.");
                }
            }
        }
    }

    @Override
    public VersaoDataset buscarPorDatasetENumero(Integer datasetId, Integer numeroVersao) throws SQLException {
        try (PreparedStatement statement = connection.prepareStatement(READ_BY_DATASET_AND_VERSION_QUERY)) {
            statement.setInt(1, datasetId);
            statement.setInt(2, numeroVersao);
            try (ResultSet result = statement.executeQuery()) {
                if (result.next()) {
                    VersaoDataset v = new VersaoDataset();
                    v.setDatasetId(result.getInt("dataset_id"));

                    int versaoBase = result.getInt("versao_base_numero");
                    if (!result.wasNull()) {
                        v.setVersaoBaseNumero(versaoBase);
                    }

                    v.setNumeroVersao(result.getInt("numero_versao"));
                    v.setCriadorCpf(result.getString("criador_cpf"));
                    v.setDescModificacoes(result.getString("desc_modificacoes"));
                    v.setArquivo(result.getBytes("arquivo"));
                    v.setCriadoEm(result.getTimestamp("criado_em"));

                    return v;
                }
                return null;
            }
        }
    }

    @Override
    public void update(VersaoDataset t) throws SQLException {
        try (PreparedStatement statement = connection.prepareStatement(UPDATE_QUERY)) {
            if (t.getVersaoBaseNumero() != null) {
                statement.setInt(1, t.getVersaoBaseNumero());
            } else {
                statement.setNull(1, java.sql.Types.INTEGER);
            }
            
            statement.setString(2, t.getCriadorCpf());
            statement.setString(3, t.getDescModificacoes());
            statement.setBytes(4, t.getArquivo());
            statement.setInt(5, t.getDatasetId());
            statement.setInt(6, t.getNumeroVersao());
            
            statement.executeUpdate();
        }
    }

    @Override
    public void delete(Integer id, Integer numeroVersao) throws SQLException {
       try (PreparedStatement statement = connection.prepareStatement(DELETE_QUERY)) {
            statement.setInt(1, id);
            statement.setInt(2, numeroVersao);
            if (statement.executeUpdate() < 1) {
                throw new SQLException("Versão não encontrada para exclusão.");
            }
        }
    }

    @Override
    public VersaoDataset read(Integer id) throws SQLException {
        throw new UnsupportedOperationException("Chave composta. Utilize read(datasetId, numeroVersao).");
    }

    @Override
    public void delete(Integer id) throws SQLException {
        throw new UnsupportedOperationException("Chave composta. Utilize delete(datasetId, numeroVersao).");
    }    

    @Override
    public void uploadArquivo (byte[] arquivo, Integer datasetId, Integer numeroVersao) {
        try(PreparedStatement statement = connection.prepareStatement(UPLOAD_FILE_QUERY)) {
            statement.setBytes(1, arquivo); // Placeholder para o arquivo
            statement.setInt(2, datasetId);
            statement.setInt(3, numeroVersao);
            statement.executeUpdate();
        } catch (SQLException ex) {
            Logger.getLogger(PgVersaoDatasetDAO.class.getName()).log(Level.SEVERE, "DAO", ex);
        }
    }
}