package org.sale;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import java.util.List;

@Path("/pharmaps")
public class PharmacyResource {

    private final EntityManagerFactory emf = Persistence.createEntityManagerFactory("default");
    private final EntityManager em = emf.createEntityManager();

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response addPharmacy(Pharmacy pharmacy) {
        em.getTransaction().begin();
        em.persist(pharmacy);
        em.getTransaction().commit();
        em.close();
        return Response.ok(pharmacy).build();
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAllPharmacies() {
        List<Pharmacy> pharmacies = em.createQuery("select p from Pharmacy p", Pharmacy.class).getResultList();
        em.close();
        return Response.ok(pharmacies).build();
    }

    @GET
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getPharmacyById(@PathParam("id") Long id) {
        Pharmacy pharmacy = em.find(Pharmacy.class, id);
        em.close();
        if (pharmacy == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        return Response.ok(pharmacy).build();
    }

    @PUT
    @Path("/{id}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response updatePharmacy(@PathParam("id") Long id, Pharmacy updatedpharmacy) {
        Pharmacy existingpharmacy = em.find(Pharmacy.class, id);

        if (existingpharmacy == null) {
            em.close();
            return Response.status(Response.Status.NOT_FOUND).build();
        }

        em.getTransaction().begin();
        existingpharmacy.setName(updatedpharmacy.getName());
        existingpharmacy.setLat(updatedpharmacy.getLat());
        existingpharmacy.setLon(updatedpharmacy.getLon());
        existingpharmacy.setOnDuty(updatedpharmacy.isOnDuty());
        em.getTransaction().commit();
        em.close();

        return Response.ok(updatedpharmacy).build();
    }

    @DELETE
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response deletePharmacy(@PathParam("id") Long id) {
        Pharmacy pharmacy = em.find(Pharmacy.class, id);

        if (pharmacy == null) {
            em.close();
            return Response.status(Response.Status.NOT_FOUND).build();
        }

        em.getTransaction().begin();
        em.remove(pharmacy);
        em.getTransaction().commit();
        em.close();

        return Response.ok().build();
    }
}
