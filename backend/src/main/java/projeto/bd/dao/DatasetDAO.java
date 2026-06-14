package projeto.bd.dao;

import java.sql.SQLException;
import java.util.List;
import projeto.bd.models.Dataset;
import projeto.bd.dtos.DatasetResumoDTO;


public interface DatasetDAO {
    
    public void create(Dataset dataset) throws SQLException;
    public Dataset read(Integer id) throws SQLException;
    public void update(Dataset dataset) throws SQLException;
    public void delete(Integer id) throws SQLException;
    public List<Dataset> all() throws SQLException;
    public List<DatasetResumoDTO> allResumo () throws SQLException;
    public byte[] downloadCsv(Integer id) throws SQLException;
    
}