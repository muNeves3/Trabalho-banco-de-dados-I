package projeto.bd.controllers;

import projeto.bd.dao.DAOFactory;

import projeto.bd.dao.VersaoDatasetDAO;
import projeto.bd.models.VersaoDataset;
import org.springframework.http.ContentDisposition;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
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

    @GetMapping("/{datasetId}/{numeroVersao}")
    public ResponseEntity<?> buscarVersaoPorNumero(@PathVariable Integer datasetId, @PathVariable Integer numeroVersao) {
        try (DAOFactory daoFactory = DAOFactory.getInstance()) {
            VersaoDatasetDAO dao = daoFactory.getVersaoDatasetDAO();
            VersaoDataset versao = dao.buscarPorDatasetENumero(datasetId, numeroVersao);
            if (versao == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Versão não encontrada.");
            }
            return ResponseEntity.ok(versao);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                                 .body("Erro ao buscar versão: " + e.getMessage());
        }
    }

    @GetMapping("/{datasetId}/{numeroVersao}/download")
    public ResponseEntity<?> baixarVersao(@PathVariable Integer datasetId, @PathVariable Integer numeroVersao) {
        try (DAOFactory daoFactory = DAOFactory.getInstance()) {
            VersaoDatasetDAO dao = daoFactory.getVersaoDatasetDAO();
            VersaoDataset versao = dao.buscarPorDatasetENumero(datasetId, numeroVersao);
            if (versao == null || versao.getArquivo() == null || versao.getArquivo().length == 0) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Arquivo da versão não encontrado.");
            }

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.parseMediaType("text/csv"));
            headers.setContentDisposition(
                ContentDisposition.attachment().filename("dataset-" + datasetId + "-versao-" + numeroVersao + ".csv").build()
            );

            return new ResponseEntity<>(versao.getArquivo(), headers, HttpStatus.OK);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                                 .body("Erro ao baixar versão: " + e.getMessage());
        }
    }

    @PostMapping
    public ResponseEntity<String> salvarNovaVersao(@RequestBody VersaoDataset versaoDataset) {
        try (DAOFactory daoFactory = DAOFactory.getInstance()) {
            VersaoDatasetDAO dao = daoFactory.getVersaoDatasetDAO();
            if (versaoDataset.getNumeroVersao() == null) {
                List<VersaoDataset> versoesExistentes = dao.listarPorDataset(versaoDataset.getDatasetId());
                int proximaVersao = 1;
                if (!versoesExistentes.isEmpty()) {
                    proximaVersao = versoesExistentes.get(versoesExistentes.size() - 1).getNumeroVersao() + 1;
                }
                versaoDataset.setNumeroVersao(proximaVersao);
            }
            dao.create(versaoDataset);
            return ResponseEntity.status(HttpStatus.CREATED).body("Nova versão registrada com sucesso!");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                                 .body("Erro ao salvar versão: " + e.getMessage());
        }
    }
}