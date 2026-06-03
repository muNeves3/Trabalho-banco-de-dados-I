package projeto.bd.dao;

import java.sql.SQLException;
import java.util.List;
import projeto.bd.models.Feature;

public interface FeatureDAO {
    
    public void create(Feature feature) throws SQLException;
    public Feature read(Integer id) throws SQLException;
    public void update(Feature feature) throws SQLException;
    public void delete(Integer id) throws SQLException;
    public List<Feature> all() throws SQLException;
    
}