package org.acme.repository;

import org.acme.model.Usuario;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;
import java.util.Optional;

@ApplicationScoped
public class UsuarioRepository implements PanacheRepository<Usuario> {

    public Optional<Usuario> findByEmail(String email) {
        return find("email", email).firstResultOptional();
    }

    public Optional<Usuario> findByEmailAndSenha(String email, String senha) {
        return find("email = ?1 and senha = ?2", email, senha).firstResultOptional();
    }
}