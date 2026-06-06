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
import projeto.bd.dao.FeatureDAO;
import projeto.bd.models.Feature;


@RestController
@RequestMapping("/api/features")
public class FeatureController {

    @GetMapping
    public ResponseEntity<?> listarFeatures() {
        try (DAOFactory daoFactory = DAOFactory.getInstance()) {
            FeatureDAO dao = daoFactory.getFeatureDAO();
            List<Feature> features = dao.all();
            return ResponseEntity.ok(features);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                                 .body("Erro ao buscar features: " + e.getMessage());
        }
    }

    @PostMapping
    public ResponseEntity<String> criarFeatures(@RequestBody Feature feature) {
        try (DAOFactory daoFactory = DAOFactory.getInstance()) {
            FeatureDAO dao = daoFactory.getFeatureDAO();
            dao.create(feature);
            return ResponseEntity.status(HttpStatus.CREATED).body("Feature criada com sucesso!");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                                 .body("Erro ao criar feature: " + e.getMessage());
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> buscarPorId(@PathVariable Integer id) {
        try (DAOFactory daoFactory = DAOFactory.getInstance()) {
            FeatureDAO dao = daoFactory.getFeatureDAO();
            Feature feature = dao.read(id);
            return ResponseEntity.ok(feature);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                                 .body("Feature não encontrada.");
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deletarFeature(@PathVariable Integer id) {
        try (DAOFactory daoFactory = DAOFactory.getInstance()) {
            FeatureDAO dao = daoFactory.getFeatureDAO();
            dao.delete(id);
            return ResponseEntity.ok("Feature deletada com sucesso!");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                                 .body("Erro ao deletar feature: " + e.getMessage());
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<String> atualizarFeature(@PathVariable Integer id, @RequestBody Feature feature) {

        try (DAOFactory daoFactory = DAOFactory.getInstance()) {
            FeatureDAO dao = daoFactory.getFeatureDAO();

            feature.setId(id);
            dao.update(feature);
            return ResponseEntity.ok("Feature atualizada com sucesso!");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                                 .body("Erro ao atualizar feature: " + e.getMessage());
        }

    }
}