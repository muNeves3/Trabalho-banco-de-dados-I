package projeto.bd.dao;

import java.sql.SQLException;
import projeto.bd.models.VersaoDataset;
import java.util.List;

public interface VersaoDatasetDAO extends DAO<VersaoDataset> {
    public List<VersaoDataset> listarPorDataset(Integer datasetId) throws SQLException;
    public VersaoDataset buscarPorDatasetENumero(Integer datasetId, Integer numeroVersao) throws SQLException;
}