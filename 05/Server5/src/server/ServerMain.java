package server;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.EOFException;
import java.io.IOException;
import java.net.BindException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;
import java.util.Observable;
import java.util.Observer;
import server.controller.DBController;
import server.util.ObserverMessage;
import shared.database.entities.Gruppa;
import shared.database.entities.Student;
import shared.database.model.Groups;
import shared.database.model.Students;
import shared.exceptions.DataUpdatingException;
import shared.exceptions.IllegalOrphanException;
import shared.exceptions.NonexistentEntityException;
import shared.exceptions.PreexistingEntityException;
import shared.exceptions.WrongMessageException;
import shared.util.Message;
import static shared.util.MessageController.*;
import static shared.util.Utils.*;

public class ServerMain {

    private static final int PORT = 12345, OBSERVER_PORT = 15432;
    private static final ThreadGroup CLIENTS_GROUP = new ThreadGroup("Clients");
    private static boolean FLAG = true;
    private static ServerSocket serverSocket, serverObserverSocket;
    private static final DBController controller = new DBController();
    private static int maxClientId = 0;

    private static class Client implements Runnable {

        public boolean OBSERVER_FLAG = false;
        private final Socket clientSocket;
        private DataOutputStream dos;
        private DataInputStream dis;
        private int clientId = -1;
        private boolean clientIdInitialized = false;

        public Client(Socket clientSocket) {
            this.clientSocket = clientSocket;
            try {
                dos = new DataOutputStream(clientSocket.getOutputStream());
                dis = new DataInputStream(clientSocket.getInputStream());
            } catch (UnknownHostException ex) {
                printException(ex);
            } catch (IOException ex) {
                printException(ex);
            }
        }

        public void setClientId() {
            if (!clientIdInitialized) {
                this.clientIdInitialized = true;
                this.clientId = maxClientId++;
            }
        }

        @Override
        public void run() {
            try {
                Message message;
                while (!Thread.currentThread().isInterrupted()) {
                    try {
                        message = readMessageFromInputStream(dis);
                        switch (message) {
                            case StartServer: {
                                FLAG = true;
                                System.out.println("Starting server...");
                                break;
                            }
                            case StopServer: {
                                FLAG = false;
                                System.out.println("Shutdown server...");
                                CLIENTS_GROUP.interrupt();
                                break;
                            }
                            case StartClient: {
                                setClientId();
                                writeMessageToOutputStream(dos, Message.SuccessfulResult);
                                writeIntegerToOutputStream(dos, clientId);
                                break;
                            }
                            case StopClient: {
                                controller.notifyObservers(new ObserverMessage(message, clientId));
                                writeMessageToOutputStream(dos, Message.SuccessfulResult);
                                dis.close();
                                dos.flush();
                                dos.close();
                                clientSocket.close();
                                Thread.currentThread().interrupt();
                                break;
                            }
                            case SerializeModel: {
                                String filepath = readStringFromInputStream(dis);
                                controller.serializeModel(filepath);
                                writeMessageToOutputStream(dos, Message.SuccessfulResult);
                                break;
                            }
                            case DeserializeModel: {
                                String filepath = readStringFromInputStream(dis);
                                controller.deserializeModel(filepath);
                                writeMessageToOutputStream(dos, Message.SuccessfulResult);
                                break;
                            }
                            case CreateGroup: {
                                Gruppa group = readGroupFromInputStream(dis);
                                int groupId = group.getGroupId();
                                String groupName = group.getGroupName();
                                controller.createGroup(groupId, groupName, clientId);
                                writeMessageToOutputStream(dos, Message.SuccessfulResult);
                                break;
                            }
                            case UpdateGroup: {
                                Gruppa group = readGroupFromInputStream(dis);
                                int groupId = group.getGroupId();
                                String groupName = group.getGroupName();
                                controller.updateGroup(groupId, groupName, clientId);
                                writeMessageToOutputStream(dos, Message.SuccessfulResult);
                                break;
                            }
                            case DeleteGroup: {
                                int groupId = readIntegerFromInputStream(dis);
                                controller.deleteGroup(groupId, clientId);
                                writeMessageToOutputStream(dos, Message.SuccessfulResult);
                                break;
                            }
                            case ReadGroup: {
                                int groupId = readIntegerFromInputStream(dis);
                                Gruppa group = controller.readGroup(groupId);
                                writeMessageToOutputStream(dos, Message.SuccessfulResult);
                                writeGroupToOutputStream(dos, group);
                                break;
                            }
                            case ReadGroups: {
                                Groups groups = controller.readGroups();
                                writeMessageToOutputStream(dos, Message.SuccessfulResult);
                                writeGroupsToOutputStream(dos, groups);
                                break;
                            }
                            case ReadGroupsCount: {
                                int groupsCount = controller.readGroupsCount();
                                writeMessageToOutputStream(dos, Message.SuccessfulResult);
                                writeIntegerToOutputStream(dos, groupsCount);
                                break;
                            }
                            case StartGroupUpdating: {
                                int groupId = readIntegerFromInputStream(dis);
                                Gruppa group = controller.startGroupUpdating(groupId);
                                writeMessageToOutputStream(dos, Message.SuccessfulResult);
                                writeGroupToOutputStream(dos, group);
                                break;
                            }
                            case CancelGroupUpdating: {
                                int groupId = readIntegerFromInputStream(dis);
                                controller.cancelGroupUpdating(groupId);
                                writeMessageToOutputStream(dos, Message.SuccessfulResult);
                                break;
                            }
                            case CreateStudent: {
                                Student student = readStudentFromInputStream(dis);
                                int studentId = student.getStudentId();
                                String studentName = student.getStudentName();
                                int groupId = student.getGroupId();
                                controller.createStudent(studentId, studentName, groupId, clientId);
                                writeMessageToOutputStream(dos, Message.SuccessfulResult);
                                break;
                            }
                            case UpdateStudent: {
                                Student student = readStudentFromInputStream(dis);
                                int studentId = student.getStudentId();
                                String studentName = student.getStudentName();
                                int groupId = student.getGroupId();
                                controller.updateStudent(studentId, studentName, groupId, clientId);
                                writeMessageToOutputStream(dos, Message.SuccessfulResult);
                                break;
                            }
                            case DeleteStudent: {
                                int studentId = readIntegerFromInputStream(dis);
                                controller.deleteStudent(studentId, clientId);
                                writeMessageToOutputStream(dos, Message.SuccessfulResult);
                                break;
                            }
                            case ReadStudent: {
                                int studentId = readIntegerFromInputStream(dis);
                                Student student = controller.readStudent(studentId);
                                writeMessageToOutputStream(dos, Message.SuccessfulResult);
                                writeStudentToOutputStream(dos, student);
                                break;
                            }
                            case ReadStudents: {
                                Students students = controller.readStudents();
                                writeMessageToOutputStream(dos, Message.SuccessfulResult);
                                writeStudentsToOutputStream(dos, students);
                                break;
                            }
                            case ReadStudentsCount: {
                                int studentsCount = controller.readStudentsCount();
                                writeMessageToOutputStream(dos, Message.SuccessfulResult);
                                writeIntegerToOutputStream(dos, studentsCount);
                                break;
                            }
                            case StartStudentUpdating: {
                                int studentId = readIntegerFromInputStream(dis);
                                Student student = controller.startStudentUpdating(studentId);
                                writeMessageToOutputStream(dos, Message.SuccessfulResult);
                                writeStudentToOutputStream(dos, student);
                                break;
                            }
                            case CancelStudentUpdating: {
                                int studentId = readIntegerFromInputStream(dis);
                                controller.cancelStudentUpdating(studentId);
                                writeMessageToOutputStream(dos, Message.SuccessfulResult);
                                break;
                            }
                            default: {
                                throw new WrongMessageException("Wrong message: " + message + "!");
                            }
                        }
                    } catch (BindException | EOFException | WrongMessageException ex) {
                        printException(ex);
                    } catch (NullPointerException | IOException | ClassNotFoundException |
                            NonexistentEntityException | PreexistingEntityException |
                            IllegalOrphanException | DataUpdatingException ex) {
                        try {
                            message = Message.valueOf(ex.getClass().getSimpleName());
                            writeMessageToOutputStream(dos, message);
                            writeStringToOutputStream(dos, ex.getMessage());
                            printException(ex);
                        } catch (IllegalArgumentException e) {
                            throw e;
                        }
                    } catch (Exception ex) {
                        message = Message.UnknownError;
                        writeMessageToOutputStream(dos, message);
                        writeStringToOutputStream(dos, ex.getMessage());
                        printException(ex);
                    }
                }
            } catch (Exception ex) {
                printException(ex);
            }
        }
    }

    private static class ClientObserver implements Observer {

        private final Socket clientObserverSocket;
        private Client client;
        private DataInputStream dis;
        private DataOutputStream dos;

        public ClientObserver(Socket clientObserverSocket, Client client) {
            this.clientObserverSocket = clientObserverSocket;
            this.client = client;
            try {
                this.dos = new DataOutputStream(this.clientObserverSocket.getOutputStream());
                this.dis = new DataInputStream(this.clientObserverSocket.getInputStream());
            } catch (IOException ex) {
                printException(ex);
            }
        }

        public static void startClientObserver(Socket clientObserverSocket, Client client) {
            ClientObserver clientObserver = new ClientObserver(clientObserverSocket, client);
            clientObserver.client.setClientId();
            controller.addObserver(clientObserver);
        }

        @Override
        public void update(Observable o, Object arg) {
            try {
                ObserverMessage observerMessage = (ObserverMessage) arg;
                Message message = observerMessage.getMessage();
                int id = observerMessage.getClientId();
                switch (message) {
                    case GroupCreated:
                    case GroupUpdated:
                    case GroupDeleted:
                    case StudentCreated:
                    case StudentUpdated:
                    case StudentDeleted: {
                        writeMessageToOutputStream(dos, message);
                        writeIntegerToOutputStream(dos, id);
                        break;
                    }
                    case StopClient: {
                        if (client.clientId == id) {
                            controller.deleteObserver(this);
                            writeMessageToOutputStream(dos, message);
                            dis.close();
                            dos.flush();
                            dos.close();
                            clientObserverSocket.close();
                        }
                        break;
                    }
                    default: {
                        throw new WrongMessageException("Wrong message: " + message + "!");
                    }
                }
            } catch (IOException | WrongMessageException ex) {
                printException(ex);
            }
        }
    }

    public static void main(String[] args) {
        try {
            System.out.println("Server started");
            serverSocket = new ServerSocket(PORT);
            serverSocket.setSoTimeout(1000);
            serverObserverSocket = new ServerSocket(OBSERVER_PORT);
            serverObserverSocket.setSoTimeout(100);
            while (FLAG) {
                try {
                    synchronized (serverSocket) {
                        Socket clientSocket = serverSocket.accept();
                        Client client = new Client(clientSocket);
                        new Thread(CLIENTS_GROUP, client).start();
                        while (!client.OBSERVER_FLAG) {
                            try {
                                Socket clientObserverSocket = serverObserverSocket.accept();
                                ClientObserver.startClientObserver(clientObserverSocket, client);
                                client.OBSERVER_FLAG = true;
                            } catch (SocketTimeoutException ex) {
                            } catch (IOException ex) {
                                printException(ex);
                            }
                        }
                    }
                } catch (SocketTimeoutException ex) {
                }
            }
        } catch (IOException ex) {
            printException(ex);
        }
    }
}
