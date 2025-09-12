package org.acme.resource;

import org.acme.dto.MarcaDTO;
import org.acme.service.MarcaService;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import java.util.List;

@Path("/marcas")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class MarcaResource {
    
    @Inject
    MarcaService marcaService;
    
    @GET
    public Response listAll() {
        List<MarcaDTO> marcas = marcaService.listAll();
        return Response.ok(marcas).build();
    }
    
    @GET
    @Path("/ativas")
    public Response listAtivas() {
        List<MarcaDTO> marcas = marcaService.listAtivas();
        return Response.ok(marcas).build();
    }
    
    @GET
    @Path("/{id}")
    public Response findById(@PathParam("id") Long id) {
        MarcaDTO marca = marcaService.findById(id);
        if (marca != null) {
            return Response.ok(marca).build();
        }
        return Response.status(Response.Status.NOT_FOUND)
                .entity("Marca não encontrada com ID: " + id)
                .build();
    }
    
    @GET
    @Path("/nome/{nome}")
    public Response findByNome(@PathParam("nome") String nome) {
        MarcaDTO marca = marcaService.findByNome(nome);
        if (marca != null) {
            return Response.ok(marca).build();
        }
        return Response.status(Response.Status.NOT_FOUND)
                .entity("Marca não encontrada com nome: " + nome)
                .build();
    }
    
    @GET
    @Path("/pais/{pais}")
    public Response findByPais(@PathParam("pais") String pais) {
        List<MarcaDTO> marcas = marcaService.findByPais(pais);
        return Response.ok(marcas).build();
    }
    
    @GET
    @Path("/search/{nome}")
    public Response searchByNome(@PathParam("nome") String nome) {
        List<MarcaDTO> marcas = marcaService.searchByNome(nome);
        return Response.ok(marcas).build();
    }
    
    @POST
    public Response create(MarcaDTO marcaDTO) {
        try {
            MarcaDTO createdMarca = marcaService.create(marcaDTO);
            return Response.status(Response.Status.CREATED).entity(createdMarca).build();
        } catch (RuntimeException e) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity(e.getMessage())
                    .build();
        }
    }
    
    @PUT
    @Path("/{id}")
    public Response update(@PathParam("id") Long id, MarcaDTO marcaDTO) {
        try {
            MarcaDTO updatedMarca = marcaService.update(id, marcaDTO);
            if (updatedMarca != null) {
                return Response.ok(updatedMarca).build();
            }
            return Response.status(Response.Status.NOT_FOUND)
                    .entity("Marca não encontrada com ID: " + id)
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
        boolean deleted = marcaService.delete(id);
        if (deleted) {
            return Response.noContent().build();
        }
        return Response.status(Response.Status.NOT_FOUND)
                .entity("Marca não encontrada com ID: " + id)
                .build();
    }
    
    @PATCH
    @Path("/{id}/desativar")
    public Response desativar(@PathParam("id") Long id) {
        boolean desativada = marcaService.desativar(id);
        if (desativada) {
            return Response.ok("Marca desativada com sucesso").build();
        }
        return Response.status(Response.Status.NOT_FOUND)
                .entity("Marca não encontrada com ID: " + id)
                .build();
    }
}