package projeto.bd.controllers;

import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import projeto.bd.dao.DAOFactory;
import projeto.bd.dao.RelatorioDAO;
import projeto.bd.dtos.Relatorio2DTO;
import projeto.bd.dtos.Relatorio5DTO;

@RestController
@RequestMapping("/api/graficos")
public class RelatorioController {
    
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

}
