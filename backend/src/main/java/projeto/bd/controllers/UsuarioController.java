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
import projeto.bd.dao.UsuarioDAO;
import projeto.bd.models.Usuario;

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

    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody Usuario usuario) {
        try (DAOFactory daoFactory = DAOFactory.getInstance()) {
            UsuarioDAO dao = daoFactory.getUsuarioDAO();
            dao.login(usuario);
            return ResponseEntity.ok("Login bem-sucedido!");
        } catch (SecurityException se) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                                    .body("Credenciais inválidas.");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                                    .body("Erro ao processar login: " + e.getMessage());    
        }
    }

    @GetMapping("/id/{id}")
    public ResponseEntity<?> buscarPorId(@PathVariable Integer id) {
        try (DAOFactory daoFactory = DAOFactory.getInstance()) {
            UsuarioDAO dao = daoFactory.getUsuarioDAO();
            Usuario usuario = dao.read(id);
            return ResponseEntity.ok(usuario);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                                 .body("Usuário não encontrado.");
        }
    }

    @PutMapping("/{cpf}")
    public ResponseEntity<String> atualizarUsuario(@PathVariable String cpf, @RequestBody Usuario usuario) {
        try (DAOFactory daoFactory = DAOFactory.getInstance()) {
            UsuarioDAO dao = daoFactory.getUsuarioDAO();
            usuario.setCpf(cpf);
            dao.update(usuario);
            return ResponseEntity.ok("Usuário atualizado com sucesso!");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                                 .body("Erro ao atualizar usuário: " + e.getMessage());
        }
    }

    @DeleteMapping("/id/{id}")
    public ResponseEntity<String> deletarUsuarioPorId(@PathVariable Integer id) {
        try (DAOFactory daoFactory = DAOFactory.getInstance()) {
            UsuarioDAO dao = daoFactory.getUsuarioDAO();
            dao.delete(id);
            return ResponseEntity.ok("Usuário deletado com sucesso!");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                                 .body("Erro ao deletar usuário: " + e.getMessage());
        }
    }

    @PostMapping("/authenticate")
    public ResponseEntity<String> authenticate(@RequestBody Usuario usuario) {
        try (DAOFactory daoFactory = DAOFactory.getInstance()) {
            UsuarioDAO dao = daoFactory.getUsuarioDAO();
            dao.authenticate(usuario);
            return ResponseEntity.ok("Autenticado com sucesso!");
        } catch (SecurityException se) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                                 .body("Credenciais inválidas.");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                                 .body("Erro ao autenticar: " + e.getMessage());
        }
    }
}