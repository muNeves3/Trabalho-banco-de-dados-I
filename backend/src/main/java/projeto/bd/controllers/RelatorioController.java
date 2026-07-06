package projeto.bd.controllers;

import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.PathVariable;
import projeto.bd.dao.DAOFactory;
import projeto.bd.dao.RelatorioDAO;
import projeto.bd.dtos.Relatorio1DTO;
import projeto.bd.dtos.Relatorio2DTO;
import projeto.bd.dtos.Relatorio5DTO;
import projeto.bd.dtos.Relatorio6DTO;
import projeto.bd.dtos.Relatorio3DTO;
import projeto.bd.dao.Relatorio4GraficoMesAnoDAO;
import projeto.bd.models.Relatorio4GraficoMesAno;

@RestController
@RequestMapping("/api/graficos")
public class RelatorioController {
    
    @GetMapping("/relatorio1")
    public ResponseEntity<?> relatorio1() {
        try (DAOFactory daoFactory = DAOFactory.getInstance()) {
            RelatorioDAO dao = daoFactory.getRelatorioDAO();
            Relatorio1DTO relatorio1 = dao.relatorio1();
            return ResponseEntity.ok(relatorio1);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                                 .body("Erro ao buscar relatório: " + e.getMessage());
        }
    }
    
    @GetMapping("/ranking")
    public ResponseEntity<?> relatorio2() {
        try (DAOFactory daoFactory = DAOFactory.getInstance()) {
            RelatorioDAO dao = daoFactory.getRelatorioDAO();
            List<Relatorio2DTO> relatorio2 = dao.rankingDatasetsMaisAcessados();
            return ResponseEntity.ok(relatorio2);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                                 .body("Erro ao buscar relatório: " + e.getMessage());
        }
    }

    @GetMapping("/versoes")
    public ResponseEntity<?> relatorio5() {
        try (DAOFactory daoFactory = DAOFactory.getInstance()) {
            RelatorioDAO dao = daoFactory.getRelatorioDAO();
            List<Relatorio5DTO> relatorio5 = dao.versoesPorDatasets();
            return ResponseEntity.ok(relatorio5);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                                 .body("Erro ao buscar relatório: " + e.getMessage());
        }
    }

    @GetMapping("/usuarios/contribuintes")
    public ResponseEntity<?> relatorio3() {
        try (DAOFactory daoFactory = DAOFactory.getInstance()) {
            RelatorioDAO dao = daoFactory.getRelatorioDAO();
            List<Relatorio3DTO> relatorio3 = dao.usuariosMaisContribuintes();
            return ResponseEntity.ok(relatorio3);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                                 .body("Erro ao buscar relatório: " + e.getMessage());
        }
    }

    @GetMapping("/usuarios/acessos")
    public ResponseEntity<?> relatorio4() {
        try (DAOFactory daoFactory = DAOFactory.getInstance()) {
            RelatorioDAO dao = daoFactory.getRelatorioDAO();
            List<Relatorio3DTO> relatorio4 = dao.usuariosMaisAcessos();
            return ResponseEntity.ok(relatorio4);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                                 .body("Erro ao buscar relatório: " + e.getMessage());
        }
    }

    @GetMapping("/usuarios/downloads")
    public ResponseEntity<?> relatorio3Downloads() {
        try (DAOFactory daoFactory = DAOFactory.getInstance()) {
            RelatorioDAO dao = daoFactory.getRelatorioDAO();
            List<Relatorio3DTO> relatorio6 = dao.usuariosMaisDownloads();
            return ResponseEntity.ok(relatorio6);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                                 .body("Erro ao buscar relatório: " + e.getMessage());
        }
    }

    @GetMapping("/relatorio4/{ano}")
    public ResponseEntity<?> getRelatorio4GraficoMesAno(@PathVariable int ano) {
        try (DAOFactory daoFactory = DAOFactory.getInstance()) {
            Relatorio4GraficoMesAnoDAO dao = daoFactory.getRelatorio4GraficoMesAnoDAO();
            List<Relatorio4GraficoMesAno> relatorio = dao.all(ano);
            return ResponseEntity.ok(relatorio);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                                 .body("Erro ao buscar relatório: " + e.getMessage());
        }
    }

    @GetMapping("/horarios")
    public ResponseEntity<?> relatorio6() {
        try (DAOFactory daoFactory = DAOFactory.getInstance()) {
            RelatorioDAO dao = daoFactory.getRelatorioDAO();
            List<Relatorio6DTO> relatorio6 = dao.horariosPicoAcesso();
            return ResponseEntity.ok(relatorio6);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                                .body("Erro ao buscar relatório: " + e.getMessage());
        }
    }
}
