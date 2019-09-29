package controllers;

import beans.DAOFacadeBean;
import controllers.utils.JsfUtils;
import controllers.utils.PaginationHelper;
import entities.Student;
import java.io.Serializable;
import java.util.ResourceBundle;
import javax.ejb.EJB;
import javax.enterprise.context.SessionScoped;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;
import javax.faces.model.ArrayDataModel;
import javax.faces.model.DataModel;
import javax.faces.model.ListDataModel;
import javax.faces.model.SelectItem;
import javax.inject.Named;
import shared.exceptions.DataUpdatingException;
import shared.exceptions.NonexistentEntityException;
import shared.exceptions.PreexistingEntityException;

@Named("studentController")
@SessionScoped
public class StudentController implements Serializable {

    private Student current;
    private DataModel items = null;
    @EJB
    private DAOFacadeBean ejbFacade;
    private PaginationHelper pagination;
    private int selectedItemIndex;

    public StudentController() {
    }

    public Student getSelected() {
        if (current == null) {
            current = new Student();
            selectedItemIndex = -1;
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
                    return getFacade().readStudentsCount();
                }

                @Override
                public DataModel createPageDataModel() {
                    return new ListDataModel(getFacade().readStudents().subList(getPageFirstItem(), Math.min(getPageFirstItem() + getPageSize(), getFacade().readStudents().size())));
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
        current = (Student) getItems().getRowData();
        selectedItemIndex = pagination.getPageFirstItem() + getItems().getRowIndex();
        return "View";
    }

    public String prepareFindByName() {
        current = (Student) getItems().getRowData();
        selectedItemIndex = pagination.getPageFirstItem() + getItems().getRowIndex();
        items = new ArrayDataModel(getFacade().readStudentsByName(current.getStudentname()).toArray());
        return "StudentsByName";
    }

    public String prepareCreate() {
        current = new Student();
        selectedItemIndex = -1;
        return "Create";
    }

    public String create() {
        try {
            getFacade().createStudent(current.getStudentid(), current.getStudentname(), current.getGroupid().getGroupid());
            JsfUtils.addSuccessMessage(ResourceBundle.getBundle("/Bundle").getString("StudentCreated"));
            return prepareCreate();
        } catch (NonexistentEntityException | PreexistingEntityException ex) {
            JsfUtils.addErrorMessage(ex, ResourceBundle.getBundle("/Bundle").getString("PersistenceErrorOccured"));
            return null;
        }
    }

    public String prepareEdit() {
        try {
            current = (Student) getItems().getRowData();
            getFacade().startStudentUpdating(current.getStudentid());
            selectedItemIndex = pagination.getPageFirstItem() + getItems().getRowIndex();
            return "Edit";
        } catch (DataUpdatingException | NonexistentEntityException ex) {
            JsfUtils.addErrorMessage(ex, ResourceBundle.getBundle("/Bundle").getString("PersistenceErrorOccured"));
            return null;
        }
    }

    public String update() {
        try {
            getFacade().updateStudent(current.getStudentid(), current.getStudentname(), current.getGroupid().getGroupid());
            JsfUtils.addSuccessMessage(ResourceBundle.getBundle("/Bundle").getString("StudentUpdated"));
            return "View";
        } catch (DataUpdatingException | NonexistentEntityException ex) {
            JsfUtils.addErrorMessage(ex, ResourceBundle.getBundle("/Bundle").getString("PersistenceErrorOccured"));
            return null;
        }
    }

    public String destroy() {
        current = (Student) getItems().getRowData();
        selectedItemIndex = pagination.getPageFirstItem() + getItems().getRowIndex();
        performDestroy();
        recreatePagination();
        recreateModel();
        return "List";
    }

    public String destroyAndView() {
        performDestroy();
        recreateModel();
        updateCurrentItem();
        if (selectedItemIndex >= 0) {
            return "View";
        } else {
            // all items were removed - go back to list
            recreateModel();
            return "List";
        }
    }

    private void performDestroy() {
        try {
            getFacade().deleteStudent(current.getStudentid());
            JsfUtils.addSuccessMessage(ResourceBundle.getBundle("/Bundle").getString("StudentDeleted"));
        } catch (NonexistentEntityException ex) {
            JsfUtils.addErrorMessage(ex, ResourceBundle.getBundle("/Bundle").getString("PersistenceErrorOccured"));
        }
    }

    private void updateCurrentItem() {
        int count = getFacade().readStudentsCount();
        if (selectedItemIndex >= count) {
            // selected index cannot be bigger than number of items:
            selectedItemIndex = count - 1;
            // go to previous page if last page disappeared:
            if (pagination.getPageFirstItem() >= count) {
                pagination.previousPage();
            }
        }
        if (selectedItemIndex >= 0) {
            current = getFacade().readStudents().subList(selectedItemIndex, selectedItemIndex + 1).get(0);
        }
    }

    public String indexAfterFindByName() {
        recreateModel();
        return "index";
    }

    public String prepareEditAfterPrepareView() {
        try {
            getFacade().startStudentUpdating(current.getStudentid());
            return "Edit";
        } catch (DataUpdatingException | NonexistentEntityException ex) {
            JsfUtils.addErrorMessage(ex, ResourceBundle.getBundle("/Bundle").getString("PersistenceErrorOccured"));
            return null;
        }
    }

    public String prepareViewAfterPrepareEdit() {
        try {
            getFacade().cancelStudentUpdating(current.getStudentid());
            return "View";
        } catch (DataUpdatingException | NonexistentEntityException ex) {
            JsfUtils.addErrorMessage(ex, ResourceBundle.getBundle("/Bundle").getString("PersistenceErrorOccured"));
            return null;
        }
    }

    public String prepareListAfterPrepareEdit() {
        try {
            getFacade().cancelStudentUpdating(current.getStudentid());
            return prepareList();
        } catch (DataUpdatingException | NonexistentEntityException ex) {
            JsfUtils.addErrorMessage(ex, ResourceBundle.getBundle("/Bundle").getString("PersistenceErrorOccured"));
            return null;
        }
    }

    public String indexAfterPrepareEdit() {
        try {
            getFacade().cancelStudentUpdating(current.getStudentid());
            return "index";
        } catch (DataUpdatingException | NonexistentEntityException ex) {
            JsfUtils.addErrorMessage(ex, ResourceBundle.getBundle("/Bundle").getString("PersistenceErrorOccured"));
            return null;
        }
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

    private void recreatePagination() {
        pagination = null;
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
        return JsfUtils.getSelectItems(getFacade().readStudents(), false);
    }

    public SelectItem[] getItemsAvailableSelectOne() {
        return JsfUtils.getSelectItems(getFacade().readStudents(), true);
    }

    public Student getStudent(Integer id) {
        return getFacade().readStudent(id);
    }

    @FacesConverter(forClass = Student.class)
    public static class StudentControllerConverter implements Converter {

        @Override
        public Object getAsObject(FacesContext facesContext, UIComponent component, String value) {
            if (value == null || value.length() == 0) {
                return null;
            }
            StudentController controller = (StudentController) facesContext.getApplication().getELResolver().
                    getValue(facesContext.getELContext(), null, "studentController");
            return controller.getStudent(getKey(value));
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
            if (object instanceof Student) {
                Student o = (Student) object;
                return getStringKey(o.getStudentid());
            } else {
                throw new IllegalArgumentException("object " + object + " is of type " + object.getClass().getName() + "; expected type: " + Student.class.getName());
            }
        }
    }
}
