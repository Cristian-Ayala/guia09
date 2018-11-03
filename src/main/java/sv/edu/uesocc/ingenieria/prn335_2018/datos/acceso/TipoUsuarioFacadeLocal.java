/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sv.edu.uesocc.ingenieria.prn335_2018.datos.acceso;

import java.util.List;
import javax.ejb.Local;
import sv.edu.uesocc.ingenieria.prn335_2018.flota.datos.definicion.TipoUsuario;

/**
 *
 * @author cristian
 */
@Local
public interface TipoUsuarioFacadeLocal {

    void create(TipoUsuario tipoUsuario);

    void edit(TipoUsuario tipoUsuario);

    void remove(TipoUsuario tipoUsuario);

    List<TipoUsuario> findAll();

    TipoUsuario find(Object id);
    
    List<TipoUsuario> findRange(int from, int to);

    int count();

}
