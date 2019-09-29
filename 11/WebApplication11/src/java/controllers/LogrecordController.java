package controllers;

import beans.DAOFacadeBean;
import controllers.utils.JsfUtils;
import controllers.utils.PaginationHelper;
import entities.Logrecord;
import javax.inject.Named;
import javax.enterprise.context.SessionScoped;
import java.io.Serializable;
import javax.ejb.EJB;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;
import javax.faces.model.ArrayDataModel;
import javax.faces.model.DataModel;
import javax.faces.model.ListDataModel;
import javax.faces.model.SelectItem;

@Named(value = "logrecordController")
@SessionScoped
public class LogrecordController implements Serializable {

    private Logrecord current;
    private DataModel items = null;
    @EJB
    private DAOFacadeBean ejbFacade;
    private PaginationHelper pagination;

    public LogrecordController() {
    }

    public Logrecord getSelected() {
        if (current == null) {
            current = new Logrecord();
        }
        return current;
    }

    private DAOFacadeBean getFacade() {
        return ejbFacade;
    }

    public PaginationHelper getPagination() {
        if (pagination == null) {
            pagination = new PaginationHelper(10) {

                @Override
                public int getItemsCount() {
                    return getFacade().readLogrecordsCount();
                }

                @Override
                public DataModel createPageDataModel() {
                    return new ListDataModel(getFacade().readLogrecords().subList(getPageFirstItem(), Math.min(getPageFirstItem() + getPageSize(), getFacade().readLogrecords().size())));
                }
            };
        }
        return pagination;
    }

    public String prepareList() {
        recreateModel();
        return "List";
    }

    public String prepareView() {
        current = (Logrecord) getItems().getRowData();
        return "View";
    }

    public String prepareFindByName() {
        current = (Logrecord) getItems().getRowData();
        items = new ArrayDataModel(getFacade().readLogrecordsByName(current.getEntityname()).toArray());
        return "LogrecordsByName";
    }

    public String indexAfterFindByName() {
        recreateModel();
        return "index";
    }

    public DataModel getItems() {
        if (items == null) {
            items = getPagination().createPageDataModel();
        }
        return items;
    }

    private void recreateModel() {
        items = null;
    }

    public String next() {
        getPagination().nextPage();
        recreateModel();
        return "List";
    }

    public String previous() {
        getPagination().previousPage();
        recreateModel();
        return "List";
    }

    public SelectItem[] getItemsAvailableSelectMany() {
        return JsfUtils.getSelectItems(getFacade().readLogrecords(), false);
    }

    public SelectItem[] getItemsAvailableSelectOne() {
        return JsfUtils.getSelectItems(getFacade().readLogrecords(), true);
    }

    public Logrecord getLogrecord(Integer id) {
        return getFacade().readLogrecord(id);
    }

    @FacesConverter(forClass = Logrecord.class)
    public static class LogrecordControllerConverter implements Converter {

        @Override
        public Object getAsObject(FacesContext facesContext, UIComponent component, String value) {
            if (value == null || value.length() == 0) {
                return null;
            }
            LogrecordController controller = (LogrecordController) facesContext.getApplication().getELResolver().
                    getValue(facesContext.getELContext(), null, "logrecordController");
            return controller.getLogrecord(getKey(value));
        }

        Integer getKey(String value) {
            Integer key = Integer.valueOf(value);
            return key;
        }

        String getStringKey(Integer value) {
            StringBuilder sb = new StringBuilder();
            sb.append(value);
            return sb.toString();
        }

        @Override
        public String getAsString(FacesContext facesContext, UIComponent component, Object object) {
            if (object == null) {
                return null;
            }
            if (object instanceof Logrecord) {
                Logrecord o = (Logrecord) object;
                return getStringKey(o.getLogrecordid());
            } else {
                throw new IllegalArgumentException("object " + object + " is of type " + object.getClass().getName() + "; expected type: " + Logrecord.class.getName());
            }
        }
    }
}
