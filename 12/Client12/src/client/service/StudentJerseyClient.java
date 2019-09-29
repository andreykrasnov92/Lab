package client.service;

import java.text.MessageFormat;
import javax.ws.rs.ClientErrorException;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;

/**
 * Jersey REST client generated for REST resource:StudentFacadeREST
 * [entities.student]<br>
 * USAGE:
 * <pre>
 *        StudentJerseyClient client = new StudentJerseyClient();
 *        Object response = client.XXX(...);
 *        // do whatever with response
 *        client.close();
 * </pre>
 */
public class StudentJerseyClient {

    private WebTarget webTarget;
    private Client client;
    private static final String BASE_URI = "http://localhost:8079/WebApplication12/webresources";

    public StudentJerseyClient() {
        client = ClientBuilder.newClient();
        webTarget = client.target(BASE_URI).path("entities.student");
    }

    public void create(Object requestEntity) throws ClientErrorException {
        webTarget.request(MediaType.APPLICATION_XML).post(Entity.entity(requestEntity, MediaType.APPLICATION_XML));
    }

    public void edit(Object requestEntity, String id) throws ClientErrorException {
        webTarget.path(MessageFormat.format("{0}", new Object[]{id})).request(MediaType.APPLICATION_XML).put(Entity.entity(requestEntity, MediaType.APPLICATION_XML));
    }

    public void remove(String id) throws ClientErrorException {
        webTarget.path(MessageFormat.format("{0}", new Object[]{id})).request().delete();
    }

    public <T> T find(Class<T> responseType, String id) throws ClientErrorException {
        WebTarget resource = webTarget;
        resource = resource.path(MessageFormat.format("{0}", new Object[]{id}));
        return resource.request(MediaType.APPLICATION_XML).get(responseType);
    }

    public <T> T findAll(Class<T> responseType) throws ClientErrorException {
        WebTarget resource = webTarget;
        return resource.request(MediaType.APPLICATION_XML).get(responseType);
    }

    public <T> T findRange(Class<T> responseType, String from, String to) throws ClientErrorException {
        WebTarget resource = webTarget;
        resource = resource.path(MessageFormat.format("{0}/{1}", new Object[]{from, to}));
        return resource.request(MediaType.APPLICATION_XML).get(responseType);
    }

    public String countREST() throws ClientErrorException {
        WebTarget resource = webTarget;
        resource = resource.path("count");
        return resource.request(MediaType.TEXT_PLAIN).get(String.class);
    }

    public void close() {
        client.close();
    }
}
