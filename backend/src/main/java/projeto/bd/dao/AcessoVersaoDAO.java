package projeto.bd.dao;

import java.sql.SQLException;
import java.util.List;
import projeto.bd.models.AcessoVersao;

public interface AcessoVersaoDAO extends DAO<AcessoVersao> {
    List<AcessoVersao> listarPorUsuario(String cpf) throws SQLException;

    List<AcessoVersao> listarPorDataset(Integer datasetId) throws SQLException;
}