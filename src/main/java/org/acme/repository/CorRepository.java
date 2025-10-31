package org.acme.repository;

import org.acme.model.Cor;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;
import java.util.List;
import java.util.Optional;

@ApplicationScoped
public class CorRepository implements PanacheRepository<Cor> {
    
    public Optional<Cor> findByNome(String nome) {
        return find("nome", nome).firstResultOptional();
    }
    
    public List<Cor> findAtivas() {
        return find("ativo", true).list();
    }
    
    public List<Cor> findByNomeContaining(String nome) {
        return find("LOWER(nome) LIKE LOWER(?1)", "%" + nome + "%").list();
    }
    
    public boolean existsByNome(String nome) {
        return count("nome", nome) > 0;
    }
}