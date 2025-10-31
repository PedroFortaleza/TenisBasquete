package org.acme.repository;

import org.acme.model.Tenis;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.persistence.Query;
import java.util.List;
import java.util.Optional;
import java.math.BigDecimal;

@ApplicationScoped
public class TenisRepository implements PanacheRepository<Tenis> {
    
    public Optional<Tenis> findByNome(String nome) {
        return find("nome", nome).firstResultOptional();
    }
    
    public List<Tenis> findAtivos() {
        return find("ativo", true).list();
    }
    
    public List<Tenis> findByNomeContaining(String nome) {
        return find("LOWER(nome) LIKE LOWER(?1)", "%" + nome + "%").list();
    }
    
    public List<Tenis> findByGenero(String genero) {
        return find("genero", genero).list();
    }
    
    public List<Tenis> findByCor(Long corId) {
        return find("cor.id", corId).list();
    }
    
    public List<Tenis> findByEsporte(Long esporteId) {
        return find("esporte.id", esporteId).list();
    }
    
    public List<Tenis> findByTamanho(String tamanho) {
        return find("?1 member of tamanhos and ativo = true", tamanho).list();
    }
    
    public List<Tenis> findByPrecoBetween(Double precoMin, Double precoMax) {
        return find("preco between ?1 and ?2 and ativo = true", 
                   BigDecimal.valueOf(precoMin), BigDecimal.valueOf(precoMax)).list();
    }
    
    public List<Tenis> findByCorAndEsporte(Long corId, Long esporteId) {
        return find("cor.id = ?1 and esporte.id = ?2 and ativo = true", corId, esporteId).list();
    }
    
    public List<Tenis> findByGeneroAndEsporte(String genero, Long esporteId) {
        return find("genero = ?1 and esporte.id = ?2 and ativo = true", genero, esporteId).list();
    }
    
    public List<Tenis> findMaisCaros(Integer limit) {
        return find("ativo = true order by preco desc")
                .range(0, limit - 1)
                .list();
    }
    
    public List<Tenis> findMaisBaratos(Integer limit) {
        return find("ativo = true order by preco asc")
                .range(0, limit - 1)
                .list();
    }
    
    public List<Tenis> findByMaterial(String material) {
        return find("LOWER(material) LIKE LOWER(?1) and ativo = true", "%" + material + "%").list();
    }
    
    public List<Tenis> findRecentes(Integer limit) {
        return find("ativo = true order by id desc")
                .range(0, limit - 1)
                .list();
    }
    
    public List<String> findGenerosDistintos() {
        Query query = getEntityManager().createQuery(
            "SELECT DISTINCT t.genero FROM Tenis t WHERE t.genero IS NOT NULL AND t.ativo = true"
        );
        return query.getResultList();
    }
    
    public List<String> findMateriaisDistintos() {
        Query query = getEntityManager().createQuery(
            "SELECT DISTINCT t.material FROM Tenis t WHERE t.material IS NOT NULL AND t.ativo = true"
        );
        return query.getResultList();
    }
    
    public Long countAtivos() {
        return count("ativo", true);
    }
    
    public Long countInativos() {
        return count("ativo", false);
    }
    
    public Long countByCor(Long corId) {
        return count("cor.id = ?1 and ativo = true", corId);
    }
    
    public Long countByEsporte(Long esporteId) {
        return count("esporte.id = ?1 and ativo = true", esporteId);
    }
    
    public Long countByGenero(String genero) {
        return count("genero = ?1 and ativo = true", genero);
    }
    
    public BigDecimal findPrecoMedio() {
        Query query = getEntityManager().createQuery(
            "SELECT AVG(t.preco) FROM Tenis t WHERE t.ativo = true"
        );
        Object result = query.getSingleResult();
        return result != null ? (BigDecimal) result : BigDecimal.ZERO;
    }
    
    public BigDecimal findPrecoMaximo() {
        Query query = getEntityManager().createQuery(
            "SELECT MAX(t.preco) FROM Tenis t WHERE t.ativo = true"
        );
        Object result = query.getSingleResult();
        return result != null ? (BigDecimal) result : BigDecimal.ZERO;
    }
    
    public BigDecimal findPrecoMinimo() {
        Query query = getEntityManager().createQuery(
            "SELECT MIN(t.preco) FROM Tenis t WHERE t.ativo = true"
        );
        Object result = query.getSingleResult();
        return result != null ? (BigDecimal) result : BigDecimal.ZERO;
    }
    
    public List<Tenis> findComDescontoVirtual(Double percentualDesconto) {
        List<Tenis> tenis = findAtivos();
        return tenis;
    }
    
    public List<Object[]> findEstatisticasPorEsporte() {
        Query query = getEntityManager().createQuery(
            "SELECT e.nome, COUNT(t), AVG(t.preco), MIN(t.preco), MAX(t.preco) " +
            "FROM Tenis t JOIN t.esporte e " +
            "WHERE t.ativo = true " +
            "GROUP BY e.id, e.nome " +
            "ORDER BY COUNT(t) DESC"
        );
        return query.getResultList();
    }
    
    public List<Object[]> findEstatisticasPorCor() {
        Query query = getEntityManager().createQuery(
            "SELECT c.nome, COUNT(t), AVG(t.preco), MIN(t.preco), MAX(t.preco) " +
            "FROM Tenis t JOIN t.cor c " +
            "WHERE t.ativo = true " +
            "GROUP BY c.id, c.nome " +
            "ORDER BY COUNT(t) DESC"
        );
        return query.getResultList();
    }
    
    public List<Object[]> findEstatisticasPorGenero() {
        Query query = getEntityManager().createQuery(
            "SELECT t.genero, COUNT(t), AVG(t.preco), MIN(t.preco), MAX(t.preco) " +
            "FROM Tenis t " +
            "WHERE t.ativo = true AND t.genero IS NOT NULL " +
            "GROUP BY t.genero " +
            "ORDER BY COUNT(t) DESC"
        );
        return query.getResultList();
    }
    
    public boolean existsByNome(String nome) {
        return count("nome", nome) > 0;
    }
    
    public List<Tenis> findComFiltrosAvancados(String nome, String genero, Long corId, Long esporteId, 
                                             BigDecimal precoMin, BigDecimal precoMax, Boolean ativo) {
        StringBuilder queryBuilder = new StringBuilder();
        java.util.Map<String, Object> params = new java.util.HashMap<>();
        
        queryBuilder.append("1=1");
        
        if (nome != null && !nome.isEmpty()) {
            queryBuilder.append(" AND LOWER(nome) LIKE LOWER(:nome)");
            params.put("nome", "%" + nome + "%");
        }
        
        if (genero != null && !genero.isEmpty()) {
            queryBuilder.append(" AND genero = :genero");
            params.put("genero", genero);
        }
        
        if (corId != null) {
            queryBuilder.append(" AND cor.id = :corId");
            params.put("corId", corId);
        }
        
        if (esporteId != null) {
            queryBuilder.append(" AND esporte.id = :esporteId");
            params.put("esporteId", esporteId);
        }
        
        if (precoMin != null) {
            queryBuilder.append(" AND preco >= :precoMin");
            params.put("precoMin", precoMin);
        }
        
        if (precoMax != null) {
            queryBuilder.append(" AND preco <= :precoMax");
            params.put("precoMax", precoMax);
        }
        
        if (ativo != null) {
            queryBuilder.append(" AND ativo = :ativo");
            params.put("ativo", ativo);
        }
        
        return find(queryBuilder.toString(), params).list();
    }
}