package persistencia;

import java.util.List;
import javax.ejb.Local;
import modelo.Libro;

/**
 *
 * @author manalda
 */
@Local
public interface LibroFacadeLocal {

    void create(Libro libro);

    void edit(Libro libro);

    void remove(Libro libro);

    Libro find(Object id);

    List<Libro> findAll();

    List<Libro> findRange(int[] range);

    int count();
    
    List<Libro> findByNombre(String nombre);
    
    List<Libro> findByAutor(String autor);
    
}
