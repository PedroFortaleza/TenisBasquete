package org.acme.resource;

import org.acme.dto.ModeloDTO;
import org.acme.service.ModeloService;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import java.util.List;

@Path("/modelos")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class ModeloResource {
    
    @Inject
    ModeloService modeloService;
    
    @GET
    public Response listAll() {
        try {
            List<ModeloDTO> modelos = modeloService.listAll();
            return Response.ok(modelos).build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("Erro ao listar modelos: " + e.getMessage())
                    .build();
        }
    }
    
    @GET
    @Path("/{id}")
    public Response findById(@PathParam("id") Long id) {
        try {
            ModeloDTO modelo = modeloService.findById(id);
            if (modelo != null) {
                return Response.ok(modelo).build();
            }
            return Response.status(Response.Status.NOT_FOUND)
                    .entity("Modelo não encontrado com ID: " + id)
                    .build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("Erro ao buscar modelo: " + e.getMessage())
                    .build();
        }
    }
    
    @POST
    public Response create(ModeloDTO modeloDTO) {
        try {
            System.out.println("Recebendo criação de modelo: " + modeloDTO.getNome());
            
            if (modeloDTO.getNome() == null || modeloDTO.getNome().trim().isEmpty()) {
                return Response.status(Response.Status.BAD_REQUEST)
                        .entity("Nome é obrigatório")
                        .build();
            }
            
            ModeloDTO createdModelo = modeloService.create(modeloDTO);
            return Response.status(Response.Status.CREATED).entity(createdModelo).build();
        } catch (Exception e) {
            e.printStackTrace();
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity("Erro ao criar modelo: " + e.getMessage())
                    .build();
        }
    }
    
    @PUT
    @Path("/{id}")
    public Response update(@PathParam("id") Long id, ModeloDTO modeloDTO) {
        try {
            ModeloDTO updatedModelo = modeloService.update(id, modeloDTO);
            if (updatedModelo != null) {
                return Response.ok(updatedModelo).build();
            }
            return Response.status(Response.Status.NOT_FOUND)
                    .entity("Modelo não encontrado com ID: " + id)
                    .build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("Erro ao atualizar modelo: " + e.getMessage())
                    .build();
        }
    }
    
    @DELETE
    @Path("/{id}")
    public Response delete(@PathParam("id") Long id) {
        try {
            boolean deleted = modeloService.delete(id);
            if (deleted) {
                return Response.noContent().build();
            }
            return Response.status(Response.Status.NOT_FOUND)
                    .entity("Modelo não encontrado com ID: " + id)
                    .build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("Erro ao deletar modelo: " + e.getMessage())
                    .build();
        }
    }
}