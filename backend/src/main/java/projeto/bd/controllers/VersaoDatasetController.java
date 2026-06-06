package projeto.bd.controllers;

import projeto.bd.dao.DAOFactory;

import projeto.bd.dao.PgVersaoDatasetDAO; 
import projeto.bd.dao.VersaoDatasetDAO; 
import projeto.bd.models.VersaoDataset;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/versoes")
public class VersaoDatasetController {

    @GetMapping("/{datasetId}")
    public ResponseEntity<?> listarVersoesDoDataset(@PathVariable Integer datasetId) {
        try (DAOFactory daoFactory = DAOFactory.getInstance()) {
            VersaoDatasetDAO dao = daoFactory.getVersaoDatasetDAO();

            List<VersaoDataset> versoes = dao.listarPorDataset(datasetId); 
            return ResponseEntity.ok(versoes);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                                 .body("Erro ao buscar linhagem: " + e.getMessage());
        }
    }

    @PostMapping
    public ResponseEntity<String> salvarNovaVersao(@RequestBody VersaoDataset versaoDataset) {
        try (DAOFactory daoFactory = DAOFactory.getInstance()) {
            VersaoDatasetDAO dao = daoFactory.getVersaoDatasetDAO();
            dao.create(versaoDataset); 
            return ResponseEntity.status(HttpStatus.CREATED).body("Nova versão registrada com sucesso!");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                                 .body("Erro ao salvar versão: " + e.getMessage());
        }
    }
}