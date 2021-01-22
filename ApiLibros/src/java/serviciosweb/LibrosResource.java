package serviciosweb;

import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.Singleton;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.UriInfo;
import javax.ws.rs.Consumes;
import javax.ws.rs.Produces;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PUT;
import javax.ws.rs.PathParam;
import javax.ws.rs.QueryParam;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerResponseContext;
import javax.ws.rs.container.ContainerResponseFilter;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.ResponseBuilder;
import modelo.Libro;
import persistencia.LibroFacadeLocal;

/**
 * REST Web Service
 *
 * @author manue
 */
@Path("libros")
public class LibrosResource implements ContainerResponseFilter{
    @Context
    private UriInfo context;
    private final LibroFacadeLocal libroFacade = lookupLibroFacadeLocal();

    public LibrosResource() {
    }
    
    
    @Override
    public void filter(ContainerRequestContext requestContext, ContainerResponseContext response) {
        response.getHeaders().putSingle("Access-Control-Allow-Origin", "*");
        response.getHeaders().putSingle("Access-Control-Allow-Methods", "OPTIONS, GET, POST, PUT, DELETE");
        response.getHeaders().putSingle("Access-Control-Allow-Headers", "Content-Type");
    }

    @GET
    @Produces("application/json")
    public Response getLibros() {
        ResponseBuilder respuesta = Response.status(Response.Status.OK);
        List<Libro> libros = libroFacade.findAll();
        
        if(libros == null) {
            return respuesta.status(Response.Status.NOT_FOUND).build();
        }
        
        respuesta.entity(libros.toArray(new Libro[0]));
        return respuesta.build();
    }
    
    @GET
    @Path("{id}")
    @Produces("application/json")
    public Response getLibroById(@PathParam("id") short id) {
        ResponseBuilder respuesta = Response.status(Response.Status.OK);
        
        try {
            Libro l = libroFacade.find(id);
            respuesta.entity(l);
            return respuesta.build();
        } catch(Exception e) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
    }
    
    @GET
    @Produces("application/json")
    public Response getLibroByNombre(@QueryParam("nombre") String nombre) {
        ResponseBuilder respuesta = Response.status(Response.Status.OK);
        
        try {
            List<Libro> libros = libroFacade.findByNombre(nombre);
            respuesta.entity(libros.toArray(new Libro[0]));
            return respuesta.build();
        } catch(Exception e) {
            e.printStackTrace();
            return Response.status(Response.Status.NOT_FOUND).build();
        }
    }

//    @POST
//    @Consumes("application/json")
//    public Response createLibro(JsonObject libro) {
//        try {
//            Libro l = new Libro();
//            l.setId((short)(libroFacade.count() + 1));
//            l.setNombre();
//        }
//    }
    
    @PUT
    @Consumes("application/json")
    public void putJson(String content) {
    }

    private LibroFacadeLocal lookupLibroFacadeLocal() {
        try {
            javax.naming.Context c = new InitialContext();
            return (LibroFacadeLocal) c.lookup("java:global/ApiLibros/LibroFacade!persistencia.LibroFacadeLocal");
        } catch (NamingException ne) {
            Logger.getLogger(getClass().getName()).log(Level.SEVERE, "exception caught", ne);
            throw new RuntimeException(ne);
        }
    }
}