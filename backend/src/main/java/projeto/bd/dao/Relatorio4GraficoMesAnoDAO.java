package projeto.bd.dao;

import java.util.List;
import java.sql.SQLException;

import projeto.bd.models.Relatorio4GraficoMesAno;

public interface Relatorio4GraficoMesAnoDAO {
    public List<Relatorio4GraficoMesAno> all(int ano) throws Exception;
}