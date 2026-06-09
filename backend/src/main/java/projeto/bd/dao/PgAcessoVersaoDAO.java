package projeto.bd.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import projeto.bd.models.AcessoVersao;

public class PgAcessoVersaoDAO implements AcessoVersaoDAO {
    private final Connection connection;

    private static final String CREATE_QUERY =
        "INSERT INTO sistema.acesso_versao(usuario_cpf, dataset_id, numero_versao, tipo_acesso) " +
        "VALUES(?, ?, ?, ?);";

    private static final String READ_QUERY = 
        "SELECT usuario_cpf, dataset_id, numero_versao, tipo_acesso, acessado_em " +
        "FROM sistema.acesso_versao WHERE usuario_cpf = ? AND dataset_id = ? AND numero_versao = ? " +
        "ORDER BY acessado_em DESC LIMIT 1;";
    
    private static final String ALL_QUERY =
        "SELECT usuario_cpf, dataset_id, numero_versao, tipo_acesso, acessado_em " +
        "FROM sistema.acesso_versao ORDER BY acessado_em DESC;";

    private static final String LIST_BY_USUARIO_QUERY =
        "SELECT usuario_cpf, dataset_id, numero_versao, tipo_acesso, acessado_em " +
        "FROM sistema.acesso_versao WHERE usuario_cpf = ? ORDER BY acessado_em DESC;";

    private static final String LIST_BY_DATASET_QUERY =
        "SELECT usuario_cpf, dataset_id, numero_versao, tipo_acesso, acessado_em " +
        "FROM sistema.acesso_versao WHERE dataset_id = ? ORDER BY acessado_em DESC;";

    private static final String UPDATE_QUERY = 
        "UPDATE sistema.acesso_versao SET tipo_acesso = ? " +
        "WHERE usuario_cpf = ? AND dataset_id = ? AND numero_versao = ? AND acessado_em = ?;";
    
    private static final String DELETE_QUERY = 
        "DELETE FROM sistema.acesso_versao WHERE usuario_cpf = ? AND dataset_id = ? AND numero_versao = ? AND acessado_em = ?;";

    public PgAcessoVersaoDAO(Connection connection) {
        this.connection = connection;
    }

    @Override
    public void create(AcessoVersao acessoVersao) throws SQLException {
        try(PreparedStatement statement = connection.prepareStatement(CREATE_QUERY)) {
            statement.setString(1, acessoVersao.getUsuarioCpf());
            statement.setInt(2, acessoVersao.getDatasetId());
            statement.setInt(3, acessoVersao.getVersaoDatasetNumVersao());
            statement.setString(4, acessoVersao.getTipoAcesso());

            statement.executeUpdate();
        } catch (SQLException ex) {
            Logger.getLogger(PgVersaoDatasetDAO.class.getName()).log(Level.SEVERE, "DAO", ex);
            throw new SQLException("Erro ao inserir acesso de versão");
        }
    }

    public AcessoVersao read(String usuarioCpf, int datasetId, int versaoDatasetNumVersao) throws SQLException {
        try(PreparedStatement statement = connection.prepareStatement(READ_QUERY)) {

            statement.setString(1, usuarioCpf);
            statement.setInt(2, datasetId);
            statement.setInt(3, versaoDatasetNumVersao);

            try (ResultSet rs = statement.executeQuery()) {
                if (rs.next()) {
                    AcessoVersao acessoVersao = new AcessoVersao();
                    acessoVersao.setUsuarioCpf(rs.getString("usuario_cpf"));
                    acessoVersao.setDatasetId(rs.getInt("dataset_id"));
                    acessoVersao.setVersaoDatasetNumVersao(rs.getInt("numero_versao"));
                    acessoVersao.setTipoAcesso(rs.getString("tipo_acesso"));
                    acessoVersao.setAcessadoEm(rs.getTimestamp("acessado_em"));
                    return acessoVersao;
                }
            }
        } catch (SQLException ex) {
            Logger.getLogger(PgVersaoDatasetDAO.class.getName()).log(Level.SEVERE, "DAO", ex);
            throw new SQLException("Erro ao buscar acesso de versão específico");
        }
        return null;
    }

    @Override
    public void update(AcessoVersao acessoVersao) throws SQLException {
        if (acessoVersao.getAcessadoEm() == null) {
            throw new SQLException("Data de acesso obrigatória para atualizar o registro.");
        }

        try(PreparedStatement statement = connection.prepareStatement(UPDATE_QUERY)) {
            statement.setString(1, acessoVersao.getTipoAcesso());
            statement.setString(2, acessoVersao.getUsuarioCpf());
            statement.setInt(3, acessoVersao.getDatasetId());
            statement.setInt(4, acessoVersao.getVersaoDatasetNumVersao());
            statement.setTimestamp(5, new java.sql.Timestamp(acessoVersao.getAcessadoEm().getTime()));

            statement.executeUpdate();
        } catch (SQLException ex) {
            Logger.getLogger(PgVersaoDatasetDAO.class.getName()).log(Level.SEVERE, "DAO", ex);
            throw new SQLException("Erro ao atualizar acesso de versão");
        }
    }

    @Override
    public List<AcessoVersao> all() throws SQLException {
        List<AcessoVersao> lista = new ArrayList<>();
        try(PreparedStatement statement = connection.prepareStatement(ALL_QUERY);
            ResultSet rs = statement.executeQuery()) {
            
            while (rs.next()) {
                AcessoVersao acessoVersao = new AcessoVersao();
                acessoVersao.setUsuarioCpf(rs.getString("usuario_cpf"));
                acessoVersao.setDatasetId(rs.getInt("dataset_id"));
                acessoVersao.setVersaoDatasetNumVersao(rs.getInt("numero_versao"));
                acessoVersao.setTipoAcesso(rs.getString("tipo_acesso"));
                acessoVersao.setAcessadoEm(rs.getTimestamp("acessado_em"));
                lista.add(acessoVersao);
            }
        } catch (SQLException ex) {
            Logger.getLogger(PgVersaoDatasetDAO.class.getName()).log(Level.SEVERE, "DAO", ex);
            throw new SQLException("Erro ao listar acessos de versão");
        }
        return lista;
    }

    public void delete(String usuarioCpf, int datasetId, int versaoDatasetNumVersao) throws SQLException {
        throw new UnsupportedOperationException("Método inválido sem a data de acesso.");
    }

    @Override
    public List<AcessoVersao> listarPorUsuario(String cpf) throws SQLException {
        List<AcessoVersao> lista = new ArrayList<>();
        try (PreparedStatement statement = connection.prepareStatement(LIST_BY_USUARIO_QUERY)) {
            statement.setString(1, cpf);
            try (ResultSet rs = statement.executeQuery()) {
                while (rs.next()) {
                    AcessoVersao acessoVersao = new AcessoVersao();
                    acessoVersao.setUsuarioCpf(rs.getString("usuario_cpf"));
                    acessoVersao.setDatasetId(rs.getInt("dataset_id"));
                    acessoVersao.setVersaoDatasetNumVersao(rs.getInt("numero_versao"));
                    acessoVersao.setTipoAcesso(rs.getString("tipo_acesso"));
                    acessoVersao.setAcessadoEm(rs.getTimestamp("acessado_em"));
                    lista.add(acessoVersao);
                }
            }
        } catch (SQLException ex) {
            Logger.getLogger(PgVersaoDatasetDAO.class.getName()).log(Level.SEVERE, "DAO", ex);
            throw new SQLException("Erro ao listar acessos por usuário");
        }
        return lista;
    }

    @Override
    public List<AcessoVersao> listarPorDataset(Integer datasetId) throws SQLException {
        List<AcessoVersao> lista = new ArrayList<>();
        try (PreparedStatement statement = connection.prepareStatement(LIST_BY_DATASET_QUERY)) {
            statement.setInt(1, datasetId);
            try (ResultSet rs = statement.executeQuery()) {
                while (rs.next()) {
                    AcessoVersao acessoVersao = new AcessoVersao();
                    acessoVersao.setUsuarioCpf(rs.getString("usuario_cpf"));
                    acessoVersao.setDatasetId(rs.getInt("dataset_id"));
                    acessoVersao.setVersaoDatasetNumVersao(rs.getInt("numero_versao"));
                    acessoVersao.setTipoAcesso(rs.getString("tipo_acesso"));
                    acessoVersao.setAcessadoEm(rs.getTimestamp("acessado_em"));
                    lista.add(acessoVersao);
                }
            }
        } catch (SQLException ex) {
            Logger.getLogger(PgVersaoDatasetDAO.class.getName()).log(Level.SEVERE, "DAO", ex);
            throw new SQLException("Erro ao listar acessos por dataset");
        }
        return lista;
    }

    @Override
    public AcessoVersao read(Integer id) throws SQLException {
        throw new UnsupportedOperationException("Chave composta. Utilize o método read com três argumentos.");
    }

    @Override
    public void delete(Integer id) throws SQLException {
        throw new UnsupportedOperationException("Chave composta. Utilize o método delete com três argumentos.");
    }
}