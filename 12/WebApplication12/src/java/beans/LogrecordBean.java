package beans;

import entities.Logrecord;
import javax.ejb.ActivationConfigProperty;
import javax.ejb.MessageDriven;
import javax.jms.JMSException;
import javax.jms.MapMessage;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import shared.util.Utils;

@MessageDriven(mappedName = "jms/LabQueue", activationConfig = {
    @ActivationConfigProperty(propertyName = "destinationLookup", propertyValue = "jms/LabQueue"),
    @ActivationConfigProperty(propertyName = "destinationType", propertyValue = "javax.jms.Queue")
})
public class LogrecordBean implements MessageListener {

    @PersistenceContext(unitName = "WebApplication12PU")
    private EntityManager em;

    public LogrecordBean() {
    }

    protected EntityManager getEntityManager() {
        return em;
    }

    @Override
    public void onMessage(Message message) {
        if (message instanceof MapMessage) {
            try {
                MapMessage mapMessage = (MapMessage) message;
                Logrecord logrecord = new Logrecord();
                logrecord.setEntitytype(mapMessage.getString("entitytype"));
                logrecord.setEntityid(mapMessage.getInt("entityid"));
                logrecord.setEntityname(mapMessage.getString("entityname"));
                logrecord.setLogrecordtime(mapMessage.getLong("logrecordtime"));
                getEntityManager().persist(logrecord);
            } catch (JMSException ex) {
                Utils.printException(ex);
            }
        }
    }
}
