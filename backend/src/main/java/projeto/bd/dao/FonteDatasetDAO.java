package projeto.bd.dao;

import java.sql.SQLException;
import java.util.List;

public interface FonteDatasetDAO {
    public void create(Integer datasetId, String fonte) throws SQLException;
    public void delete(Integer datasetId, String fonte) throws SQLException;
    public List<String> listarPorDataset(Integer datasetId) throws SQLException;
}