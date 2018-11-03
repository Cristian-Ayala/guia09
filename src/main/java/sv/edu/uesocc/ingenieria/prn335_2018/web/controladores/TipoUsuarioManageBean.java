/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sv.edu.uesocc.ingenieria.prn335_2018.web.controladores;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.inject.Named;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.faces.view.ViewScoped;
import org.primefaces.context.RequestContext;
import org.primefaces.event.SelectEvent;
import org.primefaces.event.UnselectEvent;
import org.primefaces.model.LazyDataModel;
import org.primefaces.model.SortOrder;
import sv.edu.uesocc.ingenieria.prn335_2018.datos.acceso.TipoUsuarioFacadeLocal;
import sv.edu.uesocc.ingenieria.prn335_2018.flota.datos.definicion.TipoUsuario;

/**
 *
 * @author cristian
 */
@Named(value = "tipoUsuarioManageBean")
@ViewScoped
public class TipoUsuarioManageBean implements Serializable {

    @EJB
    TipoUsuarioFacadeLocal tipoUsrbd;

    private LazyDataModel<TipoUsuario> lazyModelo;
    private TipoUsuario tipoUsuario = new TipoUsuario();
    private List<TipoUsuario> tipoUsuarioList;
    private EstadoCRUD estado;

    public EstadoCRUD getEstado() {
        return estado;
    }

    public void setEstado(EstadoCRUD estado) {
        this.estado = estado;
    }

    public TipoUsuarioManageBean() {

    }

    public LazyDataModel<TipoUsuario> getLazyModel() {
        return lazyModelo;
    }

    public void setModel(LazyDataModel<TipoUsuario> lazyModel) {
        this.lazyModelo = lazyModel;
    }

    public void btnCancelarHandler(ActionEvent ae) {
//        post();
//        org.primefaces.event.UnselectEvent hola;
//        hola = new UnselectEvent(lazyModelo, UnselectEvent, ae);
        this.estado = EstadoCRUD.NUEVO;
        this.tipoUsuario = new TipoUsuario();
    }

    //Si había una fila seleccionada se deselecciona, y cuando se seleccionan los botones hace que se actualice despues de presionarlos
    public static void clearSelection() {
        RequestContext context = RequestContext.getCurrentInstance();
        context.execute("wdgList.clearSelection()");
    }

    public void btnEditarHandler(ActionEvent ae) {
        try {
            if (this.tipoUsuario != null && this.tipoUsrbd != null) {
                this.tipoUsrbd.edit(tipoUsuario);
                this.tipoUsuario = new TipoUsuario();
                post();
                addMessage("Modificado:", "El registro ha sido actualizado exitosamente");
            }
        } catch (Exception e) {
            Logger.getLogger(getClass().getName()).log(Level.SEVERE, e.getMessage(), e);
        }
    }

    public void btnEliminarHandler(ActionEvent ae) {
        try {
            if (this.tipoUsuario != null && this.tipoUsrbd != null) {
                this.tipoUsrbd.remove(tipoUsuario);
                post();
                //Para mostrar el mensaje 
                addMessage("¡Exito!", "El registro ha sido eliminado exitosamente");
            }
        } catch (Exception e) {
            Logger.getLogger(getClass().getName()).log(Level.SEVERE, e.getMessage(), e);
        }
    }

    public void addMessage(String summary, String detail) {
        FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, summary, detail);
        FacesContext.getCurrentInstance().addMessage(null, message);
    }

    @PostConstruct
    public void post() {
        iniciarTipoUsuario();
        LDM();
        this.estado = EstadoCRUD.NUEVO;
        this.lazyModelo.setRowIndex(-1);
    }

    public void LDM() {
        try {
            this.lazyModelo = new LazyDataModel<TipoUsuario>() {

                @Override
                public Object getRowKey(TipoUsuario object) {
                    if (object != null) {
                        return object.getIdTipoUsuario();
                    }
                    return null;
                }

                @Override
                public TipoUsuario getRowData(String rowKey) {
                    if (rowKey != null && !rowKey.isEmpty() && this.getWrappedData() != null) {
                        try {
                            Integer search = new Integer(rowKey);
                            for (TipoUsuario tu : (List<TipoUsuario>) getWrappedData()) {
                                if (tu.getIdTipoUsuario().compareTo(search) == 0) {
                                    return tu;
                                }
                            }
                        } catch (Exception e) {
                            Logger.getLogger(getClass().getName()).log(Level.SEVERE, e.getMessage(), e);
                        }
                    }
                    return null;
                }

                @Override
                public List<TipoUsuario> load(int first, int pageSize, String sortField, SortOrder sortOrder, Map<String, Object> filters) {
                    tipoUsuarioList = new ArrayList<>();
                    try {
                        if (tipoUsrbd != null) {
                            this.setRowCount(tipoUsrbd.count());
                            tipoUsuarioList = tipoUsrbd.findRange(first, pageSize);
                        }
                    } catch (Exception e) {
                        System.out.println("Excepcion" + e.getMessage());
                    }
                    return tipoUsuarioList;
                }

                @Override
                public int getRowIndex() {
                    return super.getRowIndex(); //To change body of generated methods, choose Tools | Templates.
                }

            };

        } catch (Exception e) {
            Logger.getLogger(getClass().getName()).log(Level.SEVERE, e.getMessage(), e);
        }

    }

    public void onRowSelect(SelectEvent event) {
        tipoUsuario = (TipoUsuario) event.getObject();
        this.estado = EstadoCRUD.EDITAR;
    }

    public void onRowDeselect(UnselectEvent event) {
        this.estado = EstadoCRUD.NUEVO;
        this.tipoUsuario = new TipoUsuario();
        this.lazyModelo.setRowIndex(-1);

    }

    public TipoUsuario obtenerTipoUsuario() {
        return tipoUsrbd.find(tipoUsuario);
    }

    public void crearTipoUsuario() {
        tipoUsrbd.create(tipoUsuario);
        post();
    }

    public List<TipoUsuario> getTipoUsuarioList() {
        return tipoUsuarioList;
    }

    public TipoUsuario getTipoUsuario() {
        return tipoUsuario;
    }

    public void setTipoUsuario(TipoUsuario tipoUsuario) {
        this.tipoUsuario = tipoUsuario;
    }

    public void iniciarTipoUsuario() {
        this.tipoUsuario = new TipoUsuario();
    }

    public void btnAgregarHandler(ActionEvent ae) {
        tipoUsrbd.create(tipoUsuario);
        this.iniciarTipoUsuario();
        LDM();
        FacesMessage mensaje = new FacesMessage();
        mensaje.setSeverity(FacesMessage.SEVERITY_INFO);
        mensaje.setSummary("EXITO");
        mensaje.setDetail("Se agrego un registro con éxito");
        FacesContext.getCurrentInstance().addMessage(null, mensaje);
    }
}
