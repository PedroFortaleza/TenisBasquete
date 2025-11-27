package org.acme.resource;

import org.acme.dto.TenisDTO;
import org.acme.dto.CriarTenisDTO;
import org.acme.service.TenisService;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.jboss.resteasy.annotations.providers.multipart.MultipartForm;
import org.jboss.resteasy.plugins.providers.multipart.MultipartFormDataInput;

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
            List<TenisDTO> tenis = tenisService.buscarComFiltros(nome, genero, corId, esporteId, tamanho, ativo);
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
            List<TenisDTO> tenis = tenisService.findByPrecoBetween(precoMin, precoMax);
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
            List<TenisDTO> tenis = tenisService.findMaisCaros(limit);
            return Response.ok(tenis).build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("Erro ao buscar tênis em destaque: " + e.getMessage())
                    .build();
        }
    }
    
    @GET
    @Path("/baratos")
    public Response buscarMaisBaratos(@QueryParam("limit") @DefaultValue("10") Integer limit) {
        try {
            List<TenisDTO> tenis = tenisService.findMaisBaratos(limit);
            return Response.ok(tenis).build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("Erro ao buscar tênis mais baratos: " + e.getMessage())
                    .build();
        }
    }
    
    @GET
    @Path("/recentes")
    public Response buscarRecentes(@QueryParam("limit") @DefaultValue("10") Integer limit) {
        try {
            List<TenisDTO> tenis = tenisService.findRecentes(limit);
            return Response.ok(tenis).build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("Erro ao buscar tênis recentes: " + e.getMessage())
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
    @Consumes(MediaType.APPLICATION_JSON)
    public Response atualizarPreco(@PathParam("id") Long id, Double novoPreco) {
        try {
            TenisDTO tenisAtualizado = tenisService.atualizarPreco(id, java.math.BigDecimal.valueOf(novoPreco));
            return Response.ok(tenisAtualizado).build();
        } catch (RuntimeException e) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity(e.getMessage())
                    .build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("Erro ao atualizar preço do tênis: " + e.getMessage())
                    .build();
        }
    }
    
    @POST
    @Path("/{id}/upload-imagem")
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    public Response uploadImagemTenis(
            @PathParam("id") Long id,
            @MultipartForm MultipartFormDataInput input) {
        
        try {
            TenisDTO tenisAtualizado = tenisService.processarUploadImagem(id, input);
            return Response.ok(tenisAtualizado).build();
            
        } catch (RuntimeException e) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity(e.getMessage())
                    .build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("Erro ao fazer upload da imagem: " + e.getMessage())
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
            boolean desativado = tenisService.desativar(id);
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
    
    @DELETE
    @Path("/{id}/imagem")
    public Response removerImagem(@PathParam("id") Long id) {
        try {
            TenisDTO tenisAtualizado = tenisService.removerImagem(id);
            return Response.ok(tenisAtualizado).build();
        } catch (RuntimeException e) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity(e.getMessage())
                    .build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("Erro ao remover imagem do tênis: " + e.getMessage())
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
            TenisService.EstatisticasTenis estatisticas = tenisService.getEstatisticas();
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
            List<String> generos = tenisService.findGenerosDistintos();
            return Response.ok(generos).build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("Erro ao listar gêneros: " + e.getMessage())
                    .build();
        }
    }
    
    @GET
    @Path("/estatisticas/completas")
    public Response getEstatisticasCompletas() {
        try {
            TenisService.EstatisticasCompletas estatisticas = tenisService.getEstatisticasCompletas();
            return Response.ok(estatisticas).build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("Erro ao buscar estatísticas: " + e.getMessage())
                    .build();
        }
    }
    
    @GET
    @Path("/material/{material}")
    public Response findByMaterial(@PathParam("material") String material) {
        try {
            List<TenisDTO> tenis = tenisService.findByMaterial(material);
            return Response.ok(tenis).build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("Erro ao buscar tênis por material: " + e.getMessage())
                    .build();
        }
    }
    
    @GET
    @Path("/cor-esporte")
    public Response findByCorAndEsporte(
            @QueryParam("corId") Long corId,
            @QueryParam("esporteId") Long esporteId) {
        try {
            List<TenisDTO> tenis = tenisService.findByCorAndEsporte(corId, esporteId);
            return Response.ok(tenis).build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("Erro ao buscar tênis por cor e esporte: " + e.getMessage())
                    .build();
        }
    }
    
    @GET
    @Path("/genero-esporte")
    public Response findByGeneroAndEsporte(
            @QueryParam("genero") String genero,
            @QueryParam("esporteId") Long esporteId) {
        try {
            List<TenisDTO> tenis = tenisService.findByGeneroAndEsporte(genero, esporteId);
            return Response.ok(tenis).build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("Erro ao buscar tênis por gênero e esporte: " + e.getMessage())
                    .build();
        }
    }
}