package org.acme.repository;

import org.acme.model.UsuarioAdmin;
import java.util.List;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;
import java.util.Optional;

@ApplicationScoped
public class UsuarioAdminRepository implements PanacheRepository<UsuarioAdmin> {

    public Optional<UsuarioAdmin> findByUsername(String username) {
        return find("username", username).firstResultOptional();
    }

    public Optional<UsuarioAdmin> findByEmail(String email) {
        return find("email", email).firstResultOptional();
    }

    public Optional<UsuarioAdmin> findByUsernameAndSenha(String username, String senha) {
        return find("username = ?1 and senha = ?2 and ativo = true", username, senha).firstResultOptional();
    }

    public Optional<UsuarioAdmin> findByEmailAndSenha(String email, String senha) {
        return find("email = ?1 and senha = ?2 and ativo = true", email, senha).firstResultOptional();
    }

    public List<UsuarioAdmin> findByNivelAcesso(String nivelAcesso) {
        return find("nivelAcesso", nivelAcesso).list();
    }

    public List<UsuarioAdmin> findAtivos() {
        return find("ativo", true).list();
    }
}