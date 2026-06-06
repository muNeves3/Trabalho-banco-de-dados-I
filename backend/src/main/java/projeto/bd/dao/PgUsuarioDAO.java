package projeto.bd.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import projeto.bd.models.Usuario;

public class PgUsuarioDAO implements UsuarioDAO {

    private final Connection connection;

    private static final String CREATE_QUERY =
        "INSERT INTO sistema.usuario(cpf, nome, email, senha_hash) VALUES(?, ?, ?, md5(?));"; 
    
    private static final String READ_QUERY =
        "SELECT cpf, nome, email, criado_em FROM sistema.usuario WHERE cpf = ?;";

    private static final String ALL_QUERY =
        "SELECT cpf, nome, email, criado_em FROM sistema.usuario ORDER BY nome;";

    private static final String DELETE_QUERY = "DELETE FROM sistema.usuario WHERE cpf = ?;";

    private static final String AUTH_QUERY = "SELECT cpf, nome FROM sistema.usuario WHERE email = ? AND senha_hash = md5(?);";

    private static final String LOGIN_QUERY = "SELECT cpf, nome FROM sistema.usuario WHERE email = ? AND senha_hash = md5(?);";

    private static final String READ_BY_ID_STRING = "SELECT cpf, nome, email, criado_em FROM sistema.usuario WHERE id = ?;";

    private static final String UPDATE_QUERY = "UPDATE sistema.usuario SET nome = ?, email = ?, senha_hash = md5(?) WHERE cpf = ?;";

    private static final String DELETE_BY_ID_STRING = "DELETE FROM sistema.usuario WHERE id = ?;";

    public PgUsuarioDAO(Connection connection) {
        this.connection = connection;
    }

    @Override
    public void create(Usuario usuario) throws SQLException {
        try (PreparedStatement statement = connection.prepareStatement(CREATE_QUERY)) {
            statement.setString(1, usuario.getCpf());
            statement.setString(2, usuario.getNome());
            statement.setString(3, usuario.getEmail());
            statement.setString(4, usuario.getSenhaHash());
            statement.executeUpdate();
        } catch (SQLException ex) {
            Logger.getLogger(PgUsuarioDAO.class.getName()).log(Level.SEVERE, "DAO", ex);
            throw new SQLException("Erro ao inserir usuário.");
        }
    }

    @Override
    public Usuario readByCpf(String cpf) throws SQLException {
        Usuario user = new Usuario();
        try (PreparedStatement statement = connection.prepareStatement(READ_QUERY)) {
            statement.setString(1, cpf);
            try (ResultSet result = statement.executeQuery()) {
                if (result.next()) {
                    user.setCpf(result.getString("cpf"));
                    user.setNome(result.getString("nome"));
                    user.setEmail(result.getString("email"));
                    user.setCriadoEm(result.getTimestamp("criado_em"));
                } else {
                    throw new SQLException("Usuário não encontrado.");
                }
            }
        }
        return user;
    }
    
    @Override
    public void deleteByCpf(String cpf) throws SQLException {
       
        try (PreparedStatement statement = connection.prepareStatement(DELETE_QUERY)) {
            statement.setString(1, cpf);
            if (statement.executeUpdate() < 1) {
                throw new SQLException("Erro ao excluir: usuário não encontrado.");
            }
        }
    }

    @Override
    public List<Usuario> all() throws SQLException {
        List<Usuario> list = new ArrayList<>();
        try (PreparedStatement statement = connection.prepareStatement(ALL_QUERY);
             ResultSet result = statement.executeQuery()) {
            while (result.next()) {
                Usuario u = new Usuario();
                u.setCpf(result.getString("cpf"));
                u.setNome(result.getString("nome"));
                u.setEmail(result.getString("email"));
                u.setCriadoEm(result.getTimestamp("criado_em"));
                list.add(u);
            }
        }
        return list;
    }

    @Override
    public void authenticate(Usuario usuario) throws SQLException, SecurityException {
        try (PreparedStatement statement = connection.prepareStatement(AUTH_QUERY)) {
            statement.setString(1, usuario.getEmail());
            statement.setString(2, usuario.getSenhaHash());
            try (ResultSet result = statement.executeQuery()) {
                if (result.next()) {
                    usuario.setCpf(result.getString("cpf"));
                    usuario.setNome(result.getString("nome"));
                } else {
                    throw new SecurityException("Email ou senha incorretos.");
                }
            }
        }
    }

    @Override public Usuario read(Integer id) throws SQLException { 
        try (PreparedStatement statement = connection.prepareStatement(READ_BY_ID_STRING)) {
            statement.setInt(1, id);
            try (ResultSet result = statement.executeQuery()) {
                if (result.next()) {
                    Usuario u = new Usuario();
                    u.setCpf(result.getString("cpf"));
                    u.setNome(result.getString("nome"));
                    u.setEmail(result.getString("email"));
                    u.setCriadoEm(result.getTimestamp("criado_em"));
                    return u;
                } else {
                    throw new SQLException("Usuário não encontrado.");
                }
            }
        }    
    }
    @Override public void update(Usuario t) throws SQLException {
        try (PreparedStatement statement = connection.prepareStatement(UPDATE_QUERY)) {
            statement.setString(1, t.getNome());
            statement.setString(2, t.getEmail());
            statement.setString(3, t.getSenhaHash());
            statement.setString(4, t.getCpf());
            if (statement.executeUpdate() < 1) {
                throw new SQLException("Erro ao atualizar: usuário não encontrado.");
            }
        }
    }
    @Override public void delete(Integer id) throws SQLException {
        try (PreparedStatement statement = connection.prepareStatement(DELETE_BY_ID_STRING)) {
            statement.setInt(1, id);
            if (statement.executeUpdate() < 1) {
                throw new SQLException("Erro ao excluir: usuário não encontrado ou possui vínculos em outras tabelas.");
            }
        }
    }

    @Override
    public void login(Usuario usuario) throws SQLException, SecurityException {
        try (PreparedStatement statement = connection.prepareStatement(LOGIN_QUERY)) {
            statement.setString(1, usuario.getEmail());
            statement.setString(2, usuario.getSenhaHash());
            try (ResultSet result = statement.executeQuery()) {
                if (result.next()) {
                    usuario.setCpf(result.getString("cpf"));
                    usuario.setNome(result.getString("nome"));
                } else {
                    throw new SecurityException("Email ou senha incorretos.");
                }
            }
        }
    }
}