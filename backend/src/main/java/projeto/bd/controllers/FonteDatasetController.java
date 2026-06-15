package projeto.bd.controllers;

import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import projeto.bd.dao.DAOFactory;
import projeto.bd.dao.FonteDatasetDAO;

@RestController
@RequestMapping("/api/fontes")
public class FonteDatasetController {

    @GetMapping("/{datasetId}")
    public ResponseEntity<?> listarFontes(@PathVariable Integer datasetId) {
        try (DAOFactory daoFactory = DAOFactory.getInstance()) {
            FonteDatasetDAO dao = daoFactory.getFonteDatasetDAO();
            List<String> fontes = dao.listarPorDataset(datasetId);
            return ResponseEntity.ok(fontes);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                                 .body("Erro ao buscar fontes: " + e.getMessage());
        }
    }

    @PostMapping("/{datasetId}")
    public ResponseEntity<String> adicionarFonte(@PathVariable Integer datasetId, @RequestBody String fonte) {
        try (DAOFactory daoFactory = DAOFactory.getInstance()) {
            FonteDatasetDAO dao = daoFactory.getFonteDatasetDAO();
            dao.create(datasetId, fonte);
            return ResponseEntity.status(HttpStatus.CREATED).body("Fonte adicionada com sucesso!");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                                 .body("Erro ao adicionar fonte: " + e.getMessage());
        }
    }

    @DeleteMapping("/{datasetId}/{fonte}")
    public ResponseEntity<String> removerFonte(@PathVariable Integer datasetId, @PathVariable String fonte) {
        try (DAOFactory daoFactory = DAOFactory.getInstance()) {
            FonteDatasetDAO dao = daoFactory.getFonteDatasetDAO();
            dao.delete(datasetId, fonte);
            return ResponseEntity.ok("Fonte removida com sucesso!");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                                 .body("Erro ao remover fonte: " + e.getMessage());
        }
    }
}