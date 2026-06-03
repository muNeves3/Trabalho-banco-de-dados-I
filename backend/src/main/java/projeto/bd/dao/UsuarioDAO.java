package dao;

import java.sql.SQLException;
import model.Usuario;

public interface UsuarioDAO extends DAO<Usuario> {
    public void authenticate(Usuario usuario) throws SQLException, SecurityException;
    public Usuario readByCpf(String cpf) throws SQLException;
    public void deleteByCpf(String cpf) throws SQLException;
}