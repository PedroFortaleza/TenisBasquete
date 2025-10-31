package org.acme.resource;

import org.acme.dto.TenisDTO;
import org.acme.dto.CriarTenisDTO;
import org.acme.service.TenisService;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import java.util.List;

@Path("/tenis")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class TenisResource {
    
    @Inject
    TenisService tenisService;
    
    @GET
    public Response listAll() {
        try {
            List<TenisDTO> tenis = tenisService.listAll();
            return Response.ok(tenis).build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("Erro ao listar tênis: " + e.getMessage())
                    .build();
        }
    }
    
    @GET
    @Path("/ativos")
    public Response listAtivos() {
        try {
            List<TenisDTO> tenis = tenisService.listAtivos();
            return Response.ok(tenis).build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("Erro ao listar tênis ativos: " + e.getMessage())
                    .build();
        }
    }
    
    @GET
    @Path("/{id}")
    public Response findById(@PathParam("id") Long id) {
        try {
            TenisDTO tenis = tenisService.findById(id);
            if (tenis != null) {
                return Response.ok(tenis).build();
            }
            return Response.status(Response.Status.NOT_FOUND)
                    .entity("Tênis não encontrado com ID: " + id)
                    .build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("Erro ao buscar tênis: " + e.getMessage())
                    .build();
        }
    }
    
    @GET
    @Path("/search/{nome}")
    public Response searchByNome(@PathParam("nome") String nome) {
        try {
            List<TenisDTO> tenis = tenisService.searchByNome(nome);
            return Response.ok(tenis).build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("Erro ao buscar tênis por nome: " + e.getMessage())
                    .build();
        }
    }
    
    @GET
    @Path("/genero/{genero}")
    public Response findByGenero(@PathParam("genero") String genero) {
        try {
            List<TenisDTO> tenis = tenisService.findByGenero(genero);
            return Response.ok(tenis).build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("Erro ao buscar tênis por gênero: " + e.getMessage())
                    .build();
        }
    }
    
    @GET
    @Path("/cor/{corId}")
    public Response findByCor(@PathParam("corId") Long corId) {
        try {
            List<TenisDTO> tenis = tenisService.findByCor(corId);
            return Response.ok(tenis).build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("Erro ao buscar tênis por cor: " + e.getMessage())
                    .build();
        }
    }
    
    @GET
    @Path("/esporte/{esporteId}")
    public Response findByEsporte(@PathParam("esporteId") Long esporteId) {
        try {
            List<TenisDTO> tenis = tenisService.findByEsporte(esporteId);
            return Response.ok(tenis).build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("Erro ao buscar tênis por esporte: " + e.getMessage())
                    .build();
        }
    }
    
    @GET
    @Path("/tamanho/{tamanho}")
    public Response findByTamanho(@PathParam("tamanho") String tamanho) {
        try {
            List<TenisDTO> tenis = tenisService.findByTamanho(tamanho);
            return Response.ok(tenis).build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("Erro ao buscar tênis por tamanho: " + e.getMessage())
                    .build();
        }
    }
    
    @GET
    @Path("/filtros")
    public Response buscarComFiltros(
            @QueryParam("nome") String nome,
            @QueryParam("genero") String genero,
            @QueryParam("corId") Long corId,
            @QueryParam("esporteId") Long esporteId,
            @QueryParam("tamanho") String tamanho,
            @QueryParam("ativo") Boolean ativo) {
        try {
            List<TenisDTO> tenis = tenisService.listAll();
            
            if (nome != null && !nome.isEmpty()) {
                tenis = tenis.stream()
                        .filter(t -> t.getNome().toLowerCase().contains(nome.toLowerCase()))
                        .toList();
            }
            
            if (genero != null && !genero.isEmpty()) {
                tenis = tenis.stream()
                        .filter(t -> genero.equalsIgnoreCase(t.getGenero()))
                        .toList();
            }
            
            if (corId != null) {
                tenis = tenis.stream()
                        .filter(t -> corId.equals(t.getCorId()))
                        .toList();
            }
            
            if (esporteId != null) {
                tenis = tenis.stream()
                        .filter(t -> esporteId.equals(t.getEsporteId()))
                        .toList();
            }
            
            if (tamanho != null && !tamanho.isEmpty()) {
                tenis = tenis.stream()
                        .filter(t -> t.getTamanhos() != null && t.getTamanhos().contains(tamanho))
                        .toList();
            }
            
            if (ativo != null) {
                tenis = tenis.stream()
                        .filter(t -> ativo.equals(t.getAtivo()))
                        .toList();
            }
            
            return Response.ok(tenis).build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("Erro ao buscar tênis com filtros: " + e.getMessage())
                    .build();
        }
    }
    
    @GET
    @Path("/preco")
    public Response buscarPorFaixaPreco(
            @QueryParam("min") Double precoMin,
            @QueryParam("max") Double precoMax) {
        try {
            List<TenisDTO> tenis = tenisService.listAtivos();
            
            if (precoMin != null) {
                tenis = tenis.stream()
                        .filter(t -> t.getPreco().doubleValue() >= precoMin)
                        .toList();
            }
            
            if (precoMax != null) {
                tenis = tenis.stream()
                        .filter(t -> t.getPreco().doubleValue() <= precoMax)
                        .toList();
            }
            
            return Response.ok(tenis).build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("Erro ao buscar tênis por faixa de preço: " + e.getMessage())
                    .build();
        }
    }
    
    @GET
    @Path("/destaques")
    public Response buscarDestaques(@QueryParam("limit") @DefaultValue("10") Integer limit) {
        try {
            List<TenisDTO> tenis = tenisService.listAtivos();
            
            List<TenisDTO> destaques = tenis.stream()
                    .sorted((t1, t2) -> t2.getPreco().compareTo(t1.getPreco()))
                    .limit(limit)
                    .toList();
            
            return Response.ok(destaques).build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("Erro ao buscar tênis em destaque: " + e.getMessage())
                    .build();
        }
    }
    
    @POST
    public Response create(CriarTenisDTO tenisDTO) {
        try {
            TenisDTO createdTenis = tenisService.create(tenisDTO);
            return Response.status(Response.Status.CREATED).entity(createdTenis).build();
        } catch (RuntimeException e) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity(e.getMessage())
                    .build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("Erro ao criar tênis: " + e.getMessage())
                    .build();
        }
    }
    
    @PUT
    @Path("/{id}")
    public Response update(@PathParam("id") Long id, CriarTenisDTO tenisDTO) {
        try {
            TenisDTO updatedTenis = tenisService.update(id, tenisDTO);
            if (updatedTenis != null) {
                return Response.ok(updatedTenis).build();
            }
            return Response.status(Response.Status.NOT_FOUND)
                    .entity("Tênis não encontrado com ID: " + id)
                    .build();
        } catch (RuntimeException e) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity(e.getMessage())
                    .build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("Erro ao atualizar tênis: " + e.getMessage())
                    .build();
        }
    }
    
    @PATCH
    @Path("/{id}/preco")
    public Response atualizarPreco(@PathParam("id") Long id, Double novoPreco) {
        try {
            TenisDTO tenisExistente = tenisService.findById(id);
            if (tenisExistente == null) {
                return Response.status(Response.Status.NOT_FOUND)
                        .entity("Tênis não encontrado com ID: " + id)
                        .build();
            }
            
            CriarTenisDTO dto = new CriarTenisDTO();
            dto.setNome(tenisExistente.getNome());
            dto.setDescricao(tenisExistente.getDescricao());
            dto.setPreco(java.math.BigDecimal.valueOf(novoPreco));
            dto.setGenero(tenisExistente.getGenero());
            dto.setMaterial(tenisExistente.getMaterial());
            dto.setTamanhos(tenisExistente.getTamanhos());
            dto.setCorId(tenisExistente.getCorId());
            dto.setEsporteId(tenisExistente.getEsporteId());
            
            TenisDTO updatedTenis = tenisService.update(id, dto);
            return Response.ok(updatedTenis).build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("Erro ao atualizar preço do tênis: " + e.getMessage())
                    .build();
        }
    }
    
    @DELETE
    @Path("/{id}")
    public Response delete(@PathParam("id") Long id) {
        try {
            boolean deleted = tenisService.delete(id);
            if (deleted) {
                return Response.noContent().build();
            }
            return Response.status(Response.Status.NOT_FOUND)
                    .entity("Tênis não encontrado com ID: " + id)
                    .build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("Erro ao deletar tênis: " + e.getMessage())
                    .build();
        }
    }
    
    @PATCH
    @Path("/{id}/ativar")
    public Response ativar(@PathParam("id") Long id) {
        try {
            boolean ativado = tenisService.ativar(id);
            if (ativado) {
                return Response.ok("Tênis ativado com sucesso").build();
            }
            return Response.status(Response.Status.NOT_FOUND)
                    .entity("Tênis não encontrado com ID: " + id)
                    .build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("Erro ao ativar tênis: " + e.getMessage())
                    .build();
        }
    }
    
    @PATCH
    @Path("/{id}/desativar")
    public Response desativar(@PathParam("id") Long id) {
        try {
            boolean desativado = tenisService.delete(id);
            if (desativado) {
                return Response.ok("Tênis desativado com sucesso").build();
            }
            return Response.status(Response.Status.NOT_FOUND)
                    .entity("Tênis não encontrado com ID: " + id)
                    .build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("Erro ao desativar tênis: " + e.getMessage())
                    .build();
        }
    }
    
    @GET
    @Path("/{id}/status")
    public Response verificarStatus(@PathParam("id") Long id) {
        try {
            TenisDTO tenis = tenisService.findById(id);
            if (tenis != null) {
                return Response.ok(tenis.getAtivo()).build();
            }
            return Response.status(Response.Status.NOT_FOUND)
                    .entity("Tênis não encontrado com ID: " + id)
                    .build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("Erro ao verificar status do tênis: " + e.getMessage())
                    .build();
        }
    }
    
    @GET
    @Path("/count")
    public Response contarTenis() {
        try {
            List<TenisDTO> tenis = tenisService.listAll();
            long total = tenis.size();
            long ativos = tenis.stream().filter(TenisDTO::getAtivo).count();
            long inativos = total - ativos;
            
            var estatisticas = new EstatisticasTenis(total, ativos, inativos);
            return Response.ok(estatisticas).build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("Erro ao contar tênis: " + e.getMessage())
                    .build();
        }
    }
    
    @GET
    @Path("/generos")
    public Response listarGeneros() {
        try {
            List<TenisDTO> tenis = tenisService.listAll();
            List<String> generos = tenis.stream()
                    .map(TenisDTO::getGenero)
                    .distinct()
                    .filter(genero -> genero != null && !genero.isEmpty())
                    .toList();
            
            return Response.ok(generos).build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("Erro ao listar gêneros: " + e.getMessage())
                    .build();
        }
    }
    
    public static class EstatisticasTenis {
        public Long total;
        public Long ativos;
        public Long inativos;
        
        public EstatisticasTenis(Long total, Long ativos, Long inativos) {
            this.total = total;
            this.ativos = ativos;
            this.inativos = inativos;
        }
    }
}