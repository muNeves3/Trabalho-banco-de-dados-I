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
import projeto.bd.dao.VersaoDatasetDAO;
import projeto.bd.dtos.DatasetRequestDTO;
import projeto.bd.dtos.AcessoVersaoRequestDTO;
import projeto.bd.dao.AcessoVersaoDAO;
import projeto.bd.models.AcessoVersao;
import projeto.bd.models.Dataset;

@RestController
@RequestMapping("/api/acessos-versao")
public class AcessoVersaoController {

    @GetMapping
    public ResponseEntity<?> listarAcessoVersao() {
        try (DAOFactory daoFactory = DAOFactory.getInstance()) {
            AcessoVersaoDAO dao = daoFactory.getAcessoVersaoDAO();
            List<AcessoVersao> acessos = dao.all();
            return ResponseEntity.ok(acessos);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                                 .body("Erro ao buscar acessos de versão: " + e.getMessage());
        }
    }

    @PostMapping
    public ResponseEntity<String> criarAcessoVersao(@RequestBody AcessoVersaoRequestDTO dto) {
        try (DAOFactory daoFactory = DAOFactory.getInstance()) {
            
            AcessoVersao acesso = new AcessoVersao();
            acesso.setUsuarioCpf(dto.getUsuarioCpf());
            acesso.setDatasetId(dto.getDatasetId());

            acesso.setNumeroVersao(dto.getNumeroVersao()); 
            acesso.setTipoAcesso(dto.getTipoAcesso());        
                
            AcessoVersaoDAO dao = daoFactory.getAcessoVersaoDAO();
            dao.create(acesso); 

            return ResponseEntity.status(HttpStatus.CREATED).body("Acesso registrado com sucesso!");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                                 .body("Erro ao registrar acesso: " + e.getMessage());
        }
    }

    @GetMapping("/usuario/{cpf}")
    public ResponseEntity<?> listarPorUsuario(@PathVariable String cpf) {
        try (DAOFactory daoFactory = DAOFactory.getInstance()) {
            AcessoVersaoDAO dao = daoFactory.getAcessoVersaoDAO();

            List<AcessoVersao> acessos = dao.listarPorUsuario(cpf); 
            return ResponseEntity.ok(acessos);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                                 .body("Erro ao buscar acessos do usuário: " + e.getMessage());
        }
    }

    @GetMapping("/dataset/{datasetId}")
    public ResponseEntity<?> listarPorDataset(@PathVariable Integer datasetId) {
        try (DAOFactory daoFactory = DAOFactory.getInstance()) {
            AcessoVersaoDAO dao = daoFactory.getAcessoVersaoDAO();

            List<AcessoVersao> acessos = dao.listarPorDataset(datasetId); 
            return ResponseEntity.ok(acessos);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                                 .body("Erro ao buscar acessos do dataset: " + e.getMessage());
        }
    }
}
