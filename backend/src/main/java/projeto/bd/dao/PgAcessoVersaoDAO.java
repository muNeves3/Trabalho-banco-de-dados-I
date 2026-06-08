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
        "INSERT INTO sistema.acesso_versao(criador_cpf, dataset_id, numero_versao) " +
        "VALUES(?, ?, ?);";

    private static final String READ_QUERY = 
        "SELECT criador_cpf, dataset_id, numero_versao, acesso_em " +
        "FROM sistema.acesso_versao WHERE criador_cpf = ? AND dataset_id = ? AND numero_versao = ?;";
    
    private static final String ALL_QUERY =
        "SELECT criador_cpf, dataset_id, numero_versao, acesso_em FROM sistema.acesso_versao ORDER BY acesso_em DESC;";

    private static final String UPDATE_QUERY = 
        "UPDATE sistema.acesso_versao SET acesso_em = CURRENT_TIMESTAMP WHERE criador_cpf = ? AND dataset_id = ? AND numero_versao = ?;";
    
    private static final String DELETE_QUERY = 
        "DELETE FROM sistema.acesso_versao WHERE criador_cpf = ? AND dataset_id = ? AND numero_versao = ?;";

    public PgAcessoVersaoDAO(Connection connection) {
        this.connection = connection;
    }

    @Override
    public void create(AcessoVersao acessoVersao) throws SQLException {
        try(PreparedStatement statement = connection.prepareStatement(CREATE_QUERY)) {
            statement.setString(1, acessoVersao.getUsuarioCpf());
            statement.setInt(2, acessoVersao.getDatasetId());
            statement.setInt(3, acessoVersao.getVersaoDatasetNumVersao());

            statement.executeUpdate();
        } catch (SQLException ex) {
            Logger.getLogger(PgVersaoDatasetDAO.class.getName()).log(Level.SEVERE, "DAO", ex);
            throw new SQLException("Erro ao inserir acesso de versão");
        }
    }

    @Override
    public AcessoVersao read(String usuarioCpf, int datasetId, int versaoDatasetNumVersao) throws SQLException {
        try(PreparedStatement statement = connection.prepareStatement(READ_QUERY)) {

            statement.setString(1, usuarioCpf);
            statement.setInt(2, datasetId);
            statement.setInt(3, versaoDatasetNumVersao);

            try (ResultSet rs = statement.executeQuery()) {
                if (rs.next()) {
                    AcessoVersao acessoVersao = new AcessoVersao();
                    acessoVersao.setUsuarioCpf(rs.getString("criador_cpf"));
                    acessoVersao.setDatasetId(rs.getInt("dataset_id"));
                    acessoVersao.setVersaoDatasetNumVersao(rs.getInt("numero_versao"));

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
        try(PreparedStatement statement = connection.prepareStatement(UPDATE_QUERY)) {
            statement.setInt(1, acessoVersao.getUsuarioCpf());
            statement.setInt(2, acessoVersao.getDatasetId());
            statement.setInt(3, acessoVersao.getVersaoDatasetNumVersao());

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
                acessoVersao.setUsuarioCpf(rs.getInt("criador_cpf"));
                acessoVersao.setDatasetId(rs.getInt("dataset_id"));
                acessoVersao.setVersaoDatasetNumVersao(rs.getInt("numero_versao"));
                lista.add(acessoVersao);
            }
        } catch (SQLException ex) {
            Logger.getLogger(PgVersaoDatasetDAO.class.getName()).log(Level.SEVERE, "DAO", ex);
            throw new SQLException("Erro ao listar acessos de versão");
        }
        return lista;
    }

    public void delete(String usuarioCpf, int datasetId, int versaoDatasetNumVersao) throws SQLException {
        try(PreparedStatement statement = connection.prepareStatement(DELETE_QUERY)) {
            statement.setInt(1, Integer.parseInt(usuarioCpf));
            statement.setInt(2, datasetId);
            statement.setInt(3, versaoDatasetNumVersao);

            statement.executeUpdate();
        } catch (SQLException ex) {
            Logger.getLogger(PgVersaoDatasetDAO.class.getName()).log(Level.SEVERE, "DAO", ex);
            throw new SQLException("Erro ao deletar acesso de versão");
        }
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