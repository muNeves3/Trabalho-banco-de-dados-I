package projeto.bd.controllers;

import projeto.bd.dao.DAOFactory;

import projeto.bd.dao.VersaoDatasetDAO; 
import projeto.bd.models.VersaoDataset;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.multipart.MultipartFile;

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
    public ResponseEntity<String> salvarNovaVersao(
            @RequestParam("datasetId") Integer datasetId,
            @RequestParam("numeroVersao") Integer numeroVersao,
            @RequestParam(value = "versaoBaseNumero", required = false) Integer versaoBaseNumero,
            @RequestParam("criadorCpf") String criadorCpf,
            @RequestParam("descModificacoes") String descModificacoes,
            @RequestPart("arquivo") MultipartFile arquivo) {
        try (DAOFactory daoFactory = DAOFactory.getInstance()) {
            VersaoDataset versao = new VersaoDataset();
            versao.setDatasetId(datasetId);
            versao.setNumeroVersao(numeroVersao);
            versao.setVersaoBaseNumero(versaoBaseNumero);
            versao.setCriadorCpf(criadorCpf);
            versao.setDescModificacoes(descModificacoes);
            versao.setArquivo(arquivo.getBytes());

            VersaoDatasetDAO dao = daoFactory.getVersaoDatasetDAO();
            dao.create(versao);
            return ResponseEntity.status(HttpStatus.CREATED).body("Nova versão registrada com sucesso!");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                                .body("Erro ao salvar versão: " + e.getMessage());
        }
    }

    @GetMapping("/{datasetId}/{numeroVersao}/download")
    public ResponseEntity<byte[]> downloadArquivo(
            @PathVariable Integer datasetId,
            @PathVariable Integer numeroVersao) {
        try (DAOFactory daoFactory = DAOFactory.getInstance()) {
            VersaoDatasetDAO dao = daoFactory.getVersaoDatasetDAO();
            VersaoDataset versao = dao.read(datasetId, numeroVersao);

            if (versao.getArquivo() == null) {
                return ResponseEntity.notFound().build();
            }

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.parseMediaType("text/csv"));
            headers.setContentDispositionFormData("attachment", 
                    "dataset_" + datasetId + "_v" + numeroVersao + ".csv");

            return new ResponseEntity<>(versao.getArquivo(), headers, HttpStatus.OK);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}