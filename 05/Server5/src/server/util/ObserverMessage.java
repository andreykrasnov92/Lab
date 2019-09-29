package server.util;

import shared.util.Message;

public class ObserverMessage {

    private Message message;
    private int clientId;

    public ObserverMessage() {
        this.message = Message.SuccessfulResult;
    }

    public ObserverMessage(Message message) {
        this.message = message;
        this.clientId = -1;
    }

    public ObserverMessage(Message message, int clientId) {
        this.message = message;
        this.clientId = clientId;
    }

    public Message getMessage() {
        return message;
    }

    public int getClientId() {
        return clientId;
    }
}
