package org.acme.repository;

import org.acme.model.Carrinho;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;
import java.util.Optional;

@ApplicationScoped
public class CarrinhoRepository implements PanacheRepository<Carrinho> {
    
    public Optional<Carrinho> findByUsuarioId(Long usuarioId) {
        return find("usuario.id", usuarioId).firstResultOptional();
    }
}