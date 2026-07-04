package projeto.bd.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import projeto.bd.models.Relatorio4GraficoMesAno;

public class PgRelatorio4GraficoMesAnoDAO implements Relatorio4GraficoMesAnoDAO {
    private final Connection connection;

    private static final String ALL_QUERY =
            "SELECT SUM(" +
                "CASE " +
                   "WHEN tipo_acesso = 'download' THEN 1 " +
                "ELSE 0 " +
                "END " +
            ") AS total_downloads_mes, " +
            "SUM( " +
            "CASE " +
                "WHEN tipo_acesso = 'visualizacao' THEN 1 " +
                "ELSE 0 " +
            "END " +
            ") AS total_visualizacao_mes, " +
            "CASE " +
                "WHEN EXTRACT(MONTH FROM acessado_em) = 1 THEN 'Janeiro' " +
                "WHEN EXTRACT(MONTH FROM acessado_em) = 2 THEN 'Fevereiro' " +
                "WHEN EXTRACT(MONTH FROM acessado_em) = 3 THEN 'Março' " +
                "WHEN EXTRACT(MONTH FROM acessado_em) = 4 THEN 'Abril' " +
                "WHEN EXTRACT(MONTH FROM acessado_em) = 5 THEN 'Maio' " +
                "WHEN EXTRACT(MONTH FROM acessado_em) = 6 THEN 'Junho' " +
                "WHEN EXTRACT(MONTH FROM acessado_em) = 7 THEN 'Julho' " +
                "WHEN EXTRACT(MONTH FROM acessado_em) = 8 THEN 'Agosto' " +
                "WHEN EXTRACT(MONTH FROM acessado_em) = 9 THEN 'Setembro' " +
                "WHEN EXTRACT(MONTH FROM acessado_em) = 10 THEN 'Outubro' " +
                "WHEN EXTRACT(MONTH FROM acessado_em) = 11 THEN 'Novembro' " +
                "WHEN EXTRACT(MONTH FROM acessado_em) = 12 THEN 'Dezembro' " +
            "END AS mes " +
            "FROM sistema.acesso_versao " +
            "WHERE EXTRACT(YEAR FROM acessado_em) = ? " +
            "GROUP BY EXTRACT(MONTH FROM acessado_em) " +
            "ORDER BY EXTRACT(MONTH FROM acessado_em);";

    public PgRelatorio4GraficoMesAnoDAO(Connection connection) {
        this.connection = connection;
    }

    @Override
    public List<Relatorio4GraficoMesAno> all(int ano) throws Exception {
        List<Relatorio4GraficoMesAno> relatorios = new ArrayList<>();
        try (PreparedStatement statement = connection.prepareStatement(ALL_QUERY)) {
            statement.setInt(1, ano);
            try (ResultSet result = statement.executeQuery()) {
                while (result.next()) {
                    int totalDownloadsMes = result.getInt("total_downloads_mes");
                    int totalVisualizacaoMes = result.getInt("total_visualizacao_mes");
                    String mes = result.getString("mes");
                    relatorios.add(new Relatorio4GraficoMesAno(totalDownloadsMes, totalVisualizacaoMes, mes));
                }
            }
        } catch (SQLException ex) {
            Logger.getLogger(PgRelatorio4GraficoMesAnoDAO.class.getName()).log(Level.SEVERE, "DAO", ex);
            throw new Exception("Erro ao listar relatório 4 gráfico mês/ano.");
        }
        return relatorios;
    }
}