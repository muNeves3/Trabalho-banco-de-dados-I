package projeto.bd.controllers;

import projeto.bd.dao.DAOFactory;
import projeto.bd.dao.UsuarioDAO;
import projeto.bd.models.Usuario;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/usuarios")
public class UsuarioController {

    @GetMapping
    public ResponseEntity<?> listarUsuarios() {
        try (DAOFactory daoFactory = DAOFactory.getInstance()) {
            UsuarioDAO dao = daoFactory.getUsuarioDAO();
            List<Usuario> usuarios = dao.all();
            return ResponseEntity.ok(usuarios);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                                 .body("Erro ao buscar usuários: " + e.getMessage());
        }
    }

    @PostMapping
    public ResponseEntity<String> criarUsuario(@RequestBody Usuario usuario) {
        try (DAOFactory daoFactory = DAOFactory.getInstance()) {
            UsuarioDAO dao = daoFactory.getUsuarioDAO();
            dao.create(usuario);
            return ResponseEntity.status(HttpStatus.CREATED).body("Usuário criado com sucesso!");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                                 .body("Erro ao criar usuário: " + e.getMessage());
        }
    }

    @GetMapping("/{cpf}")
    public ResponseEntity<?> buscarPorCpf(@PathVariable String cpf) {
        try (DAOFactory daoFactory = DAOFactory.getInstance()) {
            UsuarioDAO dao = daoFactory.getUsuarioDAO();
            Usuario usuario = dao.readByCpf(cpf);
            return ResponseEntity.ok(usuario);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                                 .body("Usuário não encontrado.");
        }
    }

    @DeleteMapping("/{cpf}")
    public ResponseEntity<String> deletarUsuario(@PathVariable String cpf) {
        try (DAOFactory daoFactory = DAOFactory.getInstance()) {
            UsuarioDAO dao = daoFactory.getUsuarioDAO();
            dao.deleteByCpf(cpf);
            return ResponseEntity.ok("Usuário deletado com sucesso!");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                                 .body("Erro ao deletar usuário: " + e.getMessage());
        }
    }
}