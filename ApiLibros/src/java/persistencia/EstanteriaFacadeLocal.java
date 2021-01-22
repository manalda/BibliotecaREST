package persistencia;

import java.util.List;
import javax.ejb.Local;
import modelo.Estanteria;

/**
 *
 * @author manalda
 */
@Local
public interface EstanteriaFacadeLocal {

    void create(Estanteria estanteria);

    void edit(Estanteria estanteria);

    void remove(Estanteria estanteria);

    Estanteria find(Object id);

    List<Estanteria> findAll();

    List<Estanteria> findRange(int[] range);

    int count();
    
}
