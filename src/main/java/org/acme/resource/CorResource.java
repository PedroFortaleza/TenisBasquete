package org.acme.resource;

import org.acme.dto.CorDTO;
import org.acme.dto.CriarCorDTO;
import org.acme.service.CorService;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import java.util.List;

@Path("/cores")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class CorResource {
    
    @Inject
    CorService corService;
    
    @GET
    public Response listAll() {
        List<CorDTO> cores = corService.listAll();
        return Response.ok(cores).build();
    }
    
    @GET
    @Path("/ativas")
    public Response listAtivas() {
        List<CorDTO> cores = corService.listAtivas();
        return Response.ok(cores).build();
    }
    
    @GET
    @Path("/{id}")
    public Response findById(@PathParam("id") Long id) {
        CorDTO cor = corService.findById(id);
        if (cor != null) {
            return Response.ok(cor).build();
        }
        return Response.status(Response.Status.NOT_FOUND)
                .entity("Cor não encontrada com ID: " + id)
                .build();
    }
    
    @GET
    @Path("/search/{nome}")
    public Response searchByNome(@PathParam("nome") String nome) {
        List<CorDTO> cores = corService.searchByNome(nome);
        return Response.ok(cores).build();
    }
    
    @POST
    public Response create(CriarCorDTO corDTO) {
        try {
            CorDTO createdCor = corService.create(corDTO);
            return Response.status(Response.Status.CREATED).entity(createdCor).build();
        } catch (RuntimeException e) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity(e.getMessage())
                    .build();
        }
    }
    
    @PUT
    @Path("/{id}")
    public Response update(@PathParam("id") Long id, CriarCorDTO corDTO) {
        try {
            CorDTO updatedCor = corService.update(id, corDTO);
            if (updatedCor != null) {
                return Response.ok(updatedCor).build();
            }
            return Response.status(Response.Status.NOT_FOUND)
                    .entity("Cor não encontrada com ID: " + id)
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
        boolean deleted = corService.delete(id);
        if (deleted) {
            return Response.noContent().build();
        }
        return Response.status(Response.Status.NOT_FOUND)
                .entity("Cor não encontrada com ID: " + id)
                .build();
    }
    
    @PATCH
    @Path("/{id}/ativar")
    public Response ativar(@PathParam("id") Long id) {
        boolean ativada = corService.ativar(id);
        if (ativada) {
            return Response.ok("Cor ativada com sucesso").build();
        }
        return Response.status(Response.Status.NOT_FOUND)
                .entity("Cor não encontrada com ID: " + id)
                .build();
    }
}