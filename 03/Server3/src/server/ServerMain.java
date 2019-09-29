package server;

import java.io.EOFException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
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
        private ObjectOutputStream oos;
        private ObjectInputStream ois;
        private int clientId = -1;
        private boolean clientIdInitialized = false;

        public Client(Socket clientSocket) {
            this.clientSocket = clientSocket;
            try {
                oos = new ObjectOutputStream(clientSocket.getOutputStream());
                ois = new ObjectInputStream(clientSocket.getInputStream());
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
                        message = (Message) ois.readObject();
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
                                oos.writeObject(Message.SuccessfulResult);
                                oos.writeInt(clientId);
                                oos.flush();
                                break;
                            }
                            case StopClient: {
                                controller.notifyObservers(new ObserverMessage(message, clientId));
                                oos.writeObject(Message.SuccessfulResult);
                                ois.close();
                                oos.flush();
                                oos.close();
                                clientSocket.close();
                                Thread.currentThread().interrupt();
                                break;
                            }
                            case SerializeModel: {
                                String filepath = ois.readUTF();
                                controller.serializeModel(filepath);
                                oos.writeObject(Message.SuccessfulResult);
                                oos.flush();
                                break;
                            }
                            case DeserializeModel: {
                                String filepath = ois.readUTF();
                                controller.deserializeModel(filepath);
                                oos.writeObject(Message.SuccessfulResult);
                                oos.flush();
                                break;
                            }
                            case CreateGroup: {
                                Gruppa group = (Gruppa) ois.readObject();
                                int groupId = group.getGroupId();
                                String groupName = group.getGroupName();
                                controller.createGroup(groupId, groupName, clientId);
                                oos.writeObject(Message.SuccessfulResult);
                                oos.flush();
                                break;
                            }
                            case UpdateGroup: {
                                Gruppa group = (Gruppa) ois.readObject();
                                int groupId = group.getGroupId();
                                String groupName = group.getGroupName();
                                controller.updateGroup(groupId, groupName, clientId);
                                oos.writeObject(Message.SuccessfulResult);
                                oos.flush();
                                break;
                            }
                            case DeleteGroup: {
                                int groupId = ois.readInt();
                                controller.deleteGroup(groupId, clientId);
                                oos.writeObject(Message.SuccessfulResult);
                                oos.flush();
                                break;
                            }
                            case ReadGroup: {
                                int groupId = ois.readInt();
                                Gruppa group = controller.readGroup(groupId);
                                oos.writeObject(Message.SuccessfulResult);
                                oos.writeObject(group);
                                oos.flush();
                                break;
                            }
                            case ReadGroups: {
                                Groups groups = controller.readGroups();
                                oos.writeObject(Message.SuccessfulResult);
                                oos.writeObject(groups);
                                oos.flush();
                                break;
                            }
                            case ReadGroupsCount: {
                                int groupsCount = controller.readGroupsCount();
                                oos.writeObject(Message.SuccessfulResult);
                                oos.writeInt(groupsCount);
                                oos.flush();
                                break;
                            }
                            case StartGroupUpdating: {
                                int groupId = ois.readInt();
                                Gruppa group = controller.startGroupUpdating(groupId);
                                oos.writeObject(Message.SuccessfulResult);
                                oos.writeObject(group);
                                oos.flush();
                                break;
                            }
                            case CancelGroupUpdating: {
                                int groupId = ois.readInt();
                                controller.cancelGroupUpdating(groupId);
                                oos.writeObject(Message.SuccessfulResult);
                                oos.flush();
                                break;
                            }
                            case CreateStudent: {
                                Student student = (Student) ois.readObject();
                                int studentId = student.getStudentId();
                                String studentName = student.getStudentName();
                                int groupId = student.getGroupId();
                                controller.createStudent(studentId, studentName, groupId, clientId);
                                oos.writeObject(Message.SuccessfulResult);
                                oos.flush();
                                break;
                            }
                            case UpdateStudent: {
                                Student student = (Student) ois.readObject();
                                int studentId = student.getStudentId();
                                String studentName = student.getStudentName();
                                int groupId = student.getGroupId();
                                controller.updateStudent(studentId, studentName, groupId, clientId);
                                oos.writeObject(Message.SuccessfulResult);
                                oos.flush();
                                break;
                            }
                            case DeleteStudent: {
                                int studentId = ois.readInt();
                                controller.deleteStudent(studentId, clientId);
                                oos.writeObject(Message.SuccessfulResult);
                                oos.flush();
                                break;
                            }
                            case ReadStudent: {
                                int studentId = ois.readInt();
                                Student student = controller.readStudent(studentId);
                                oos.writeObject(Message.SuccessfulResult);
                                oos.writeObject(student);
                                oos.flush();
                                break;
                            }
                            case ReadStudents: {
                                Students students = controller.readStudents();
                                oos.writeObject(Message.SuccessfulResult);
                                oos.writeObject(students);
                                oos.flush();
                                break;
                            }
                            case ReadStudentsCount: {
                                int studentsCount = controller.readStudentsCount();
                                oos.writeObject(Message.SuccessfulResult);
                                oos.writeInt(studentsCount);
                                oos.flush();
                                break;
                            }
                            case StartStudentUpdating: {
                                int studentId = ois.readInt();
                                Student student = controller.startStudentUpdating(studentId);
                                oos.writeObject(Message.SuccessfulResult);
                                oos.writeObject(student);
                                oos.flush();
                                break;
                            }
                            case CancelStudentUpdating: {
                                int studentId = ois.readInt();
                                controller.cancelStudentUpdating(studentId);
                                oos.writeObject(Message.SuccessfulResult);
                                oos.flush();
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
                            oos.writeObject(message);
                            oos.writeUTF(ex.getMessage());
                            oos.flush();
                            printException(ex);
                        } catch (IllegalArgumentException e) {
                            throw e;
                        }
                    } catch (Exception ex) {
                        message = Message.UnknownError;
                        oos.writeObject(message);
                        oos.writeUTF(ex.getMessage());
                        oos.flush();
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
        private ObjectInputStream ois;
        private ObjectOutputStream oos;

        public ClientObserver(Socket clientObserverSocket, Client client) {
            this.clientObserverSocket = clientObserverSocket;
            this.client = client;
            try {
                this.oos = new ObjectOutputStream(this.clientObserverSocket.getOutputStream());
                this.ois = new ObjectInputStream(this.clientObserverSocket.getInputStream());
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
                        oos.writeObject(message);
                        oos.writeInt(id);
                        oos.flush();
                        break;
                    }
                    case StopClient: {
                        if (client.clientId == id) {
                            controller.deleteObserver(this);
                            oos.writeObject(message);
                            ois.close();
                            oos.flush();
                            oos.close();
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
