package org.acme.repository;

import org.acme.model.ItemCarrinho;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;
import java.util.List;
import java.util.Optional;

@ApplicationScoped
public class ItemCarrinhoRepository implements PanacheRepository<ItemCarrinho> {
    
    public List<ItemCarrinho> findByCarrinhoId(Long carrinhoId) {
        return find("carrinho.id", carrinhoId).list();
    }
    
    public Optional<ItemCarrinho> findByCarrinhoAndTenis(Long carrinhoId, Long tenisId) {
        return find("carrinho.id = ?1 and tenis.id = ?2", carrinhoId, tenisId).firstResultOptional();
    }
    
    public boolean deleteByCarrinhoAndTenis(Long carrinhoId, Long tenisId) {
        return delete("carrinho.id = ?1 and tenis.id = ?2", carrinhoId, tenisId) > 0;
    }
    
    public boolean deleteByCarrinhoId(Long carrinhoId) {
        return delete("carrinho.id", carrinhoId) > 0;
    }
}