package projeto.bd.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import projeto.bd.dtos.Relatorio1DTO;
import projeto.bd.dtos.Relatorio2DTO;
import projeto.bd.dtos.Relatorio5DTO;
import projeto.bd.dtos.Relatorio6DTO;
import projeto.bd.dtos.Relatorio3DTO;

public class PgRelatorioDAO implements RelatorioDAO {

    private final Connection connection;

    private static final String RANKING_QUERY =
        "SELECT d.id AS dataset_id, d.nome AS nome_dataset, " +
        "COUNT(CASE WHEN a.tipo_acesso = 'download' THEN 1 END) AS total_downloads, " +
        "COUNT(CASE WHEN a.tipo_acesso = 'visualizacao' THEN 1 END) AS total_visualizacoes, " +
        "COUNT(a.tipo_acesso) AS total_acessos " +
        "FROM sistema.dataset d " +
        "LEFT JOIN sistema.acesso_versao a ON d.id = a.dataset_id " +
        "GROUP BY d.id, d.nome " +
        "ORDER BY total_acessos DESC " +
        "FETCH FIRST 7 ROWS ONLY;";

    private static final String VERSOES_QUERY = 
        "SELECT d.id AS dataset_id, d.nome AS nome_dataset, " +
        "COUNT(v.numero_versao) AS total_versoes, " +
        "CASE WHEN COUNT(v.numero_versao) > 1 " +
        "THEN (MAX(v.criado_em) - MIN(v.criado_em)) / (COUNT(v.numero_versao) - 1) " +
        "ELSE NULL END AS tempo_medio " +
        "FROM sistema.dataset d " +
        "JOIN sistema.versao_dataset v ON d.id = v.dataset_id " +
        "GROUP BY d.id, d.nome " +
        "ORDER BY total_versoes DESC;";

    private static final String USUARIOS_MAIS_CONTRIBUINTES = 
    "SELECT u.nome, COUNT(v.numero_versao) as count FROM sistema.usuario u " +
    "JOIN sistema.versao_dataset v ON v.criador_cpf = u.cpf " +
    "GROUP BY u.cpf, u.nome " +
    "ORDER BY COUNT(v.numero_versao) DESC " +
    "FETCH FIRST 5 ROWS ONLY";

    private static final String USUARIOS_MAIS_ACESSOS =
    "SELECT u.nome, COUNT(*) as count FROM sistema.acesso_versao a " +
    "JOIN sistema.usuario u ON u.cpf = a.usuario_cpf " +
    "WHERE a.tipo_acesso = 'visualizacao' " +
    "GROUP BY u.cpf, u.nome " +
    "ORDER BY COUNT(*) DESC " +
    "FETCH FIRST 5 ROWS ONLY";

    private static final String USUARIOS_MAIS_DOWNLOADS =
    "SELECT u.nome, COUNT(*) as count FROM sistema.acesso_versao a " +
    "JOIN sistema.usuario u ON u.cpf = a.usuario_cpf " +
    "WHERE a.tipo_acesso = 'download' " +
    "GROUP BY u.cpf, u.nome " +
    "ORDER BY COUNT(*) DESC " +
    "FETCH FIRST 5 ROWS ONLY";

    private static final String HORARIOS_QUERY =
    "SELECT EXTRACT(HOUR FROM acessado_em) AS hora, " +
    "COUNT(CASE WHEN tipo_acesso = 'visualizacao' THEN 1 END) AS total_visualizacoes, " +
    "COUNT(CASE WHEN tipo_acesso = 'download' THEN 1 END) AS total_downloads, " +
    "COUNT(*) AS total_acessos " +
    "FROM sistema.acesso_versao " +
    "GROUP BY EXTRACT(HOUR FROM acessado_em) " +
    "ORDER BY hora;";
    
    private static final String RELATORIO_1_QUERY =
    "SELECT " +
    "(SELECT COUNT(*) FROM sistema.dataset) AS total_datasets, " +
    "(SELECT COUNT(*) FROM sistema.versao_dataset) AS total_versoes, " +
    "(SELECT COUNT(*) FROM sistema.usuario) AS usuarios_cadastrados, " +
    "CAST((SELECT COUNT(*) FROM sistema.versao_dataset)::float / NULLIF((SELECT COUNT(*) FROM sistema.dataset), 0) AS decimal(10, 2)) AS media_versoes";

    public PgRelatorioDAO(Connection connection) {
        this.connection = connection;
    }

    @Override
    public Relatorio1DTO relatorio1() throws SQLException {
        Relatorio1DTO relatorio = new Relatorio1DTO();
        try (PreparedStatement statement = connection.prepareStatement(RELATORIO_1_QUERY);
             ResultSet result = statement.executeQuery()) {
            if (result.next()) {
                relatorio.setTotalDatasets(result.getInt("total_datasets"));
                relatorio.setTotalVersoes(result.getInt("total_versoes"));
                relatorio.setUsuariosCadastrados(result.getInt("usuarios_cadastrados"));
                relatorio.setMediaVersoesPorDataset(result.getDouble("media_versoes"));
            }
        } catch (SQLException ex) {
            Logger.getLogger(PgRelatorioDAO.class.getName()).log(Level.SEVERE, "DAO", ex);
            throw new SQLException("Erro ao gerar relatório 1.");
        }
        return relatorio;
    }

    @Override
    public List<Relatorio2DTO> rankingDatasetsMaisAcessados() throws SQLException {
        List<Relatorio2DTO> ranking = new ArrayList<>();
        try (PreparedStatement statement = connection.prepareStatement(RANKING_QUERY);
             ResultSet result = statement.executeQuery()) {
            while (result.next()) {
                Relatorio2DTO dto = new Relatorio2DTO();
                dto.setDatasetId(result.getInt("dataset_id"));
                dto.setNomeDataset(result.getString("nome_dataset"));
                dto.setTotalDownloads(result.getInt("total_downloads"));
                dto.setTotalVisualizacoes(result.getInt("total_visualizacoes"));
                dto.setTotalAcessos(result.getInt("total_acessos"));
                ranking.add(dto);
            }
        } catch (SQLException ex) {
            Logger.getLogger(PgRelatorioDAO.class.getName()).log(Level.SEVERE, "DAO", ex);
            throw new SQLException("Erro ao gerar ranking de datasets.");
        }
        return ranking;
    }

    @Override
    public List<Relatorio5DTO> versoesPorDatasets() throws SQLException {
        List<Relatorio5DTO> ranking = new ArrayList<>();
        try (PreparedStatement statement = connection.prepareStatement(VERSOES_QUERY);
             ResultSet result = statement.executeQuery()) {
            while (result.next()) {
                Relatorio5DTO dto = new Relatorio5DTO();
                dto.setDatasetId(result.getInt("dataset_id"));
                dto.setNomeDataset(result.getString("nome_dataset"));
                dto.setTotalVersoes(result.getInt("total_versoes"));
                dto.setTempoMedio(result.getString("tempo_medio"));
                ranking.add(dto);
            }
        } catch (SQLException ex) {
            Logger.getLogger(PgRelatorioDAO.class.getName()).log(Level.SEVERE, "DAO", ex);
            throw new SQLException("Erro ao gerar relatorio de datasets.");
        }
        return ranking;
    }

    @Override
    public List<Relatorio3DTO> usuariosMaisContribuintes() throws SQLException {
        List<Relatorio3DTO> ranking = new ArrayList<>();
        try (PreparedStatement statement = connection.prepareStatement(USUARIOS_MAIS_CONTRIBUINTES);
             ResultSet result = statement.executeQuery()) {
            while (result.next()) {
                Relatorio3DTO dto = new Relatorio3DTO(result.getString("nome"), result.getInt("count"));
                ranking.add(dto);
            }
        } catch (SQLException ex) {
            Logger.getLogger(PgRelatorioDAO.class.getName()).log(Level.SEVERE, "DAO", ex);
            throw new SQLException("Erro ao gerar relatorio de usuarios mais contribuintes.");
        }
        return ranking;
    }

    @Override
    public List<Relatorio3DTO> usuariosMaisAcessos() throws SQLException {
        List<Relatorio3DTO> ranking = new ArrayList<>();
        try (PreparedStatement statement = connection.prepareStatement(USUARIOS_MAIS_ACESSOS);
             ResultSet result = statement.executeQuery()) {
            while (result.next()) {
                Relatorio3DTO dto = new Relatorio3DTO(result.getString("nome"), result.getInt("count"));
                ranking.add(dto);
            }
        } catch (SQLException ex) {
            Logger.getLogger(PgRelatorioDAO.class.getName()).log(Level.SEVERE, "DAO", ex);
            throw new SQLException("Erro ao gerar relatorio de usuarios mais acessos.");
        }
        return ranking;
    }

    @Override
    public List<Relatorio3DTO> usuariosMaisDownloads() throws SQLException {
        List<Relatorio3DTO> ranking = new ArrayList<>();
        try (PreparedStatement statement = connection.prepareStatement(USUARIOS_MAIS_DOWNLOADS);
             ResultSet result = statement.executeQuery()) {
            while (result.next()) {
                Relatorio3DTO dto = new Relatorio3DTO(result.getString("nome"), result.getInt("count"));
                ranking.add(dto);
            }
        } catch (SQLException ex) {
            Logger.getLogger(PgRelatorioDAO.class.getName()).log(Level.SEVERE, "DAO", ex);
            throw new SQLException("Erro ao gerar relatorio de usuarios mais downloads.");
        }
        return ranking;
    }

    @Override
    public List<Relatorio6DTO> horariosPicoAcesso() throws SQLException {
        List<Relatorio6DTO> lista = new ArrayList<>();
        try (PreparedStatement statement = connection.prepareStatement(HORARIOS_QUERY);
            ResultSet result = statement.executeQuery()) {
            while (result.next()) {
                Relatorio6DTO dto = new Relatorio6DTO();
                dto.setHora(result.getInt("hora"));
                dto.setTotalVisualizacoes(result.getInt("total_visualizacoes"));
                dto.setTotalDownloads(result.getInt("total_downloads"));
                dto.setTotalAcessos(result.getInt("total_acessos"));
                lista.add(dto);
            }
        } catch (SQLException ex) {
            Logger.getLogger(PgRelatorioDAO.class.getName()).log(Level.SEVERE, "DAO", ex);
            throw new SQLException("Erro ao gerar relatório de horários de pico.");
        }
        return lista;
    }
}