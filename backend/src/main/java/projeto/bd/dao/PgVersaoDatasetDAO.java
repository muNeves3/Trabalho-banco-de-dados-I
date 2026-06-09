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
        "INSERT INTO sistema.versao_dataset(dataset_id, versao_base_numero, numero_versao, criador_cpf, desc_modificacoes, caminho_arquivo) " +
        "VALUES(?, ?, ?, ?, ?, ?);"; 
        
    private static final String LIST_BY_DATASET_QUERY =
        "SELECT dataset_id, versao_base_numero, numero_versao, criador_cpf, desc_modificacoes, caminho_arquivo, criado_em " +
        "FROM sistema.versao_dataset WHERE dataset_id = ? ORDER BY numero_versao ASC;";

    private static final String ALL_QUERY = "SELECT dataset_id, versao_base_numero, numero_versao, criador_cpf, desc_modificacoes, caminho_arquivo, criado_em FROM sistema.versao_dataset ORDER BY dataset_id, numero_versao;";

    private static final String READ_QUERY = "SELECT dataset_id, versao_base_numero, numero_versao, criador_cpf, desc_modificacoes, caminho_arquivo, criado_em FROM sistema.versao_dataset WHERE dataset_id = ? AND numero_versao = ?;";

    private static final String UPDATE_QUERY = "UPDATE sistema.versao_dataset SET versao_base_numero = ?, criador_cpf = ?, desc_modificacoes = ?, caminho_arquivo = ? WHERE dataset_id = ? AND numero_versao = ?;";

    private static final String DELETE_QUERY = "DELETE FROM sistema.versao_dataset WHERE dataset_id = ? AND numero_versao = ?;";

    public PgVersaoDatasetDAO(Connection connection) {
        this.connection = connection;
    }

    public void create(VersaoDataset versao) throws SQLException {
        try (PreparedStatement statement = connection.prepareStatement(CREATE_QUERY)) {
            statement.setInt(1, versao.getDatasetId());
            
            // regra de negócio: um dataset começa na versão 1, cada versão nova baseada nele ou em outra versão é incrementada em 1.
            // então pode ter vários datasets com versão 1, mas só pode ter um dataset com versão 2 baseado na versão 1 criado por um usuario x, e assim por diante.
            if (versao.getVersaoBaseNumero() != null) {
                statement.setInt(2, versao.getVersaoBaseNumero());
            } else {
                statement.setNull(2, 1);
            }
            
            statement.setInt(3, versao.getNumeroVersao());
            statement.setString(4, versao.getCriadorCpf());
            statement.setString(5, versao.getDescModificacoes());
            statement.setString(6, versao.getCaminhoArquivo());
            
            statement.executeUpdate();
        } catch (SQLException ex) {
            Logger.getLogger(PgVersaoDatasetDAO.class.getName()).log(Level.SEVERE, "DAO", ex);
            throw new SQLException("Erro ao inserir versão do dataset.");
        }
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
                v.setCaminhoArquivo(result.getString("caminho_arquivo"));
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
                    v.setCaminhoArquivo(result.getString("caminho_arquivo"));
                    v.setCriadoEm(result.getTimestamp("criado_em"));
                    
                    versoes.add(v);
                }
            }
        }
        return versoes;
    }

    @Override
    public VersaoDataset read(Integer id) throws SQLException {
        try (PreparedStatement statement = connection.prepareStatement(READ_QUERY)) {
            statement.setInt(1, id);
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
                    v.setCaminhoArquivo(result.getString("caminho_arquivo"));
                    v.setCriadoEm(result.getTimestamp("criado_em"));
                    
                    return v;
                } else {
                    return null; 
                }
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
            statement.setString(4, t.getCaminhoArquivo());
            statement.setInt(5, t.getDatasetId());
            statement.setInt(6, t.getNumeroVersao());
            
            statement.executeUpdate();
        }
    }

    @Override
    public void delete(Integer id) throws SQLException {
       try (PreparedStatement statement = connection.prepareStatement(DELETE_QUERY)) {
            statement.setInt(1, id);
            statement.executeUpdate();
        }
    }
}