package org.acme.resource;

import org.acme.dto.EsporteDTO;
import org.acme.dto.CriarEsporteDTO;
import org.acme.service.EsporteService;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import java.util.List;

@Path("/esportes")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class EsporteResource {
    
    @Inject
    EsporteService esporteService;
    
    @GET
    public Response listAll() {
        List<EsporteDTO> esportes = esporteService.listAll();
        return Response.ok(esportes).build();
    }
    
    @GET
    @Path("/ativos")
    public Response listAtivos() {
        List<EsporteDTO> esportes = esporteService.listAtivos();
        return Response.ok(esportes).build();
    }
    
    @GET
    @Path("/{id}")
    public Response findById(@PathParam("id") Long id) {
        EsporteDTO esporte = esporteService.findById(id);
        if (esporte != null) {
            return Response.ok(esporte).build();
        }
        return Response.status(Response.Status.NOT_FOUND)
                .entity("Esporte não encontrado com ID: " + id)
                .build();
    }
    
    @GET
    @Path("/search/{nome}")
    public Response searchByNome(@PathParam("nome") String nome) {
        List<EsporteDTO> esportes = esporteService.searchByNome(nome);
        return Response.ok(esportes).build();
    }
    
    @POST
    public Response create(CriarEsporteDTO esporteDTO) {
        try {
            EsporteDTO createdEsporte = esporteService.create(esporteDTO);
            return Response.status(Response.Status.CREATED).entity(createdEsporte).build();
        } catch (RuntimeException e) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity(e.getMessage())
                    .build();
        }
    }
    
    @PUT
    @Path("/{id}")
    public Response update(@PathParam("id") Long id, CriarEsporteDTO esporteDTO) {
        try {
            EsporteDTO updatedEsporte = esporteService.update(id, esporteDTO);
            if (updatedEsporte != null) {
                return Response.ok(updatedEsporte).build();
            }
            return Response.status(Response.Status.NOT_FOUND)
                    .entity("Esporte não encontrado com ID: " + id)
                    .build();
        } catch (RuntimeException e) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity(e.getMessage())
                    .build();
        }
    }
    
    @DELETE
    @Path("/{id}")
    public Response delete(@PathParam("id") Long id) {
        boolean deleted = esporteService.delete(id);
        if (deleted) {
            return Response.noContent().build();
        }
        return Response.status(Response.Status.NOT_FOUND)
                .entity("Esporte não encontrado com ID: " + id)
                .build();
    }
    
    @PATCH
    @Path("/{id}/ativar")
    public Response ativar(@PathParam("id") Long id) {
        boolean ativado = esporteService.ativar(id);
        if (ativado) {
            return Response.ok("Esporte ativado com sucesso").build();
        }
        return Response.status(Response.Status.NOT_FOUND)
                .entity("Esporte não encontrado com ID: " + id)
                .build();
    }
}