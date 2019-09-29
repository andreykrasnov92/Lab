package client.service;

import javax.ws.rs.ClientErrorException;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;

/**
 * Jersey REST client generated for REST resource:ModelFacadeREST [model]<br>
 * USAGE:
 * <pre>
 *        ModelJerseyClient client = new ModelJerseyClient();
 *        Object response = client.XXX(...);
 *        // do whatever with response
 *        client.close();
 * </pre>
 */
public class ModelJerseyClient {

    private WebTarget webTarget;
    private Client client;
    private static final String BASE_URI = "http://localhost:8079/WebApplication12/webresources";

    public ModelJerseyClient() {
        client = ClientBuilder.newClient();
        webTarget = client.target(BASE_URI).path("model");
    }

    public <T> T getModel(Class<T> responseType) throws ClientErrorException {
        WebTarget resource = webTarget;
        return resource.request(MediaType.APPLICATION_XML).get(responseType);
    }

    public void close() {
        client.close();
    }
}
