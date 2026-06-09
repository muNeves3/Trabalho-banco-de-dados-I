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
import projeto.bd.dao.DatasetDAO;
import projeto.bd.dtos.DatasetRequestDTO;
import projeto.bd.models.Dataset;


@RestController
@RequestMapping("/api/datasets")
public class DatasetController {

    @GetMapping
    public ResponseEntity<?> listarDataset() {
        try (DAOFactory daoFactory = DAOFactory.getInstance()) {
            DatasetDAO dao = daoFactory.getDatasetDAO();
            List<Dataset> dataset = dao.all();
            return ResponseEntity.ok(dataset);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                                 .body("Erro ao buscar datasets: " + e.getMessage());
        }
    }

    @PostMapping
    public ResponseEntity<String> criarDataset(@RequestBody DatasetRequestDTO dto) {
        try (DAOFactory daoFactory = DAOFactory.getInstance()) {
            Dataset novoDataset = new Dataset();
            novoDataset.setNome(dto.getNome());
            novoDataset.setDescricao(dto.getDescricao());
            novoDataset.setFontes(dto.getFontes());
            novoDataset.setCriadorCpf(dto.getCriadorCpf());


            DatasetDAO dao = daoFactory.getDatasetDAO();
            dao.create(novoDataset);

            return ResponseEntity.status(HttpStatus.CREATED).body("Dataset criado com sucesso!");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                                 .body("Erro ao criar dataset: " + e.getMessage());
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> buscarPorId(@PathVariable Integer id) {
        try (DAOFactory daoFactory = DAOFactory.getInstance()) {
            DatasetDAO dao = daoFactory.getDatasetDAO();
            Dataset dataset = dao.read(id);
            return ResponseEntity.ok(dataset);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                                 .body("Dataset não encontrado.");
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deletarDataset(@PathVariable Integer id) {
        try (DAOFactory daoFactory = DAOFactory.getInstance()) {
            DatasetDAO dao = daoFactory.getDatasetDAO();
            dao.delete(id);
            return ResponseEntity.ok("Dataset deletado com sucesso!");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                                 .body("Erro ao deletar dataset: " + e.getMessage());
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<String> atualizarDataset(@PathVariable Integer id, @RequestBody Dataset dataset) {

        try (DAOFactory daoFactory = DAOFactory.getInstance()) {
            DatasetDAO dao = daoFactory.getDatasetDAO();

            dataset.setId(id);
            dao.update(dataset);
            return ResponseEntity.ok("Dataset atualizado com sucesso!");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                                 .body("Erro ao atualizar dataset: " + e.getMessage());
        }

    }
}