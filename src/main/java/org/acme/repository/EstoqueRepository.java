package org.acme.repository;

import org.acme.model.Estoque;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;
import java.util.List;

@ApplicationScoped
public class EstoqueRepository implements PanacheRepository<Estoque> {

    public List<Estoque> findByModeloId(Long modeloId) {
        return find("modelo.id", modeloId).list();
    }

    public List<Estoque> findByQuantidadeGreaterThan(Integer quantidade) {
        return find("quantidade > ?1", quantidade).list();
    }

    public List<Estoque> findDisponiveis() {
        return find("quantidade > 0").list();
    }
}