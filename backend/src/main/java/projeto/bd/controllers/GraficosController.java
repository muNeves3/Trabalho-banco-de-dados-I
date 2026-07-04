package projeto.bd.controllers;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import projeto.bd.dao.DAOFactory;
import projeto.bd.dao.Relatorio4GraficoMesAnoDAO;
import projeto.bd.models.Relatorio4GraficoMesAno;

@RestController
@RequestMapping("/api/graficos")
public class GraficosController {
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
}