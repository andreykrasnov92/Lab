package client;

import client.view.View;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.UnknownHostException;
import shared.database.entities.Gruppa;
import shared.database.entities.Student;
import shared.database.model.Groups;
import shared.database.model.Students;
import shared.exceptions.WrongMessageException;
import shared.util.Message;
import static shared.util.Utils.*;

public class ClientMain {

    public static final int GROUP_COLUMNS_COUNT = 2;
    public static final String[] GROUP_COLUMNS_NAMES = {
        "Group ID", "Group name"
    };
    public static final String NO_GROUPS = "No groups in database!";
    public static final int STUDENT_COLUMNS_COUNT = 3;
    public static final String[] STUDENT_COLUMNS_NAMES = {
        "Student ID", "Student name", "Group ID"
    };
    public static final String NO_STUDENTS = "No students in database!";

    private static final int PORT = 12345, OBSERVER_PORT = 15432;
    private static final String ADDRESS = "localhost";
    private static final ThreadGroup CLIENT_OBSERVER_GROUP = new ThreadGroup("ClientObserver");
    private static Socket clientSocket;
    private static ObjectOutputStream oos;
    private static ObjectInputStream ois;
    private static View view;
    private static ClientObserver clientObserver;
    private static boolean connected = false;
    private static int clientId = -1;

    static {
        try {
            clientSocket = new Socket(ADDRESS, PORT);
            oos = new ObjectOutputStream(clientSocket.getOutputStream());
            ois = new ObjectInputStream(clientSocket.getInputStream());
            connected = true;
        } catch (UnknownHostException ex) {
            printException(ex);
        } catch (IOException ex) {
            printException(ex);
        }
    }

    public static void startClientMain() {
        startClient();
        view = View.runView(clientId);
        if (!connected) {
            view.setMenuesEnabled(false);
            view.showWarningMessage("Client disconnected!");
            return;
        }
        clientObserver = new ClientObserver();
        new Thread(CLIENT_OBSERVER_GROUP, clientObserver).start();
    }

    public static void startServer() {
        try {
            oos.writeObject(Message.StartServer);
            oos.flush();
            System.out.println("Server starting not supported yet...");
        } catch (Exception ex) {
            printException(ex);
        }
    }

    public static void stopServer() {
        try {
            oos.writeObject(Message.StopServer);
            oos.flush();
            System.out.println("Server stopping not supported yet...");
        } catch (Exception ex) {
            printException(ex);
        }
    }

    public static void startClient() {
        try {
            oos.writeObject(Message.StartClient);
            oos.flush();
            Message message = (Message) ois.readObject();
            if (message == Message.SuccessfulResult) {
                clientId = ois.readInt();
                System.out.println("Client with id " + clientId + " started");
            } else {
                throw new Exception(ois.readUTF());
            }
        } catch (Exception ex) {
            printException(ex);
        }
    }

    public static void stopClient() {
        try {
            oos.writeObject(Message.StopClient);
            oos.flush();
            Message message = (Message) ois.readObject();
            if (message == Message.SuccessfulResult) {
                ois.close();
                oos.flush();
                oos.close();
                clientSocket.close();
            } else {
                throw new Exception(ois.readUTF());
            }
        } catch (Exception ex) {
            printException(ex);
        }
    }

    public static void serializeModel(String filepath) throws Exception {
        try {
            oos.writeObject(Message.SerializeModel);
            oos.writeUTF(filepath);
            oos.flush();
            Message message = (Message) ois.readObject();
            if (message != Message.SuccessfulResult) {
                throw new Exception(ois.readUTF());
            }
        } catch (Exception ex) {
            throw ex;
        }
    }

    public static void deserializeModel(String filepath) throws Exception {
        try {
            oos.writeObject(Message.DeserializeModel);
            oos.writeUTF(filepath);
            oos.flush();
            Message message = (Message) ois.readObject();
            if (message != Message.SuccessfulResult) {
                throw new Exception(ois.readUTF());
            }
        } catch (Exception ex) {
            throw ex;
        }
    }

    public static void createGroup(int groupId, String groupName) throws Exception {
        try {
            Gruppa group = new Gruppa(groupId, groupName);
            oos.writeObject(Message.CreateGroup);
            oos.writeObject(group);
            oos.flush();
            Message message = (Message) ois.readObject();
            if (message != Message.SuccessfulResult) {
                throw new Exception(ois.readUTF());
            }
        } catch (Exception ex) {
            throw ex;
        }
    }

    public static void updateGroup(int groupId, String groupName) throws Exception {
        try {
            Gruppa group = new Gruppa(groupId, groupName);
            oos.writeObject(Message.UpdateGroup);
            oos.writeObject(group);
            oos.flush();
            Message message = (Message) ois.readObject();
            if (message != Message.SuccessfulResult) {
                throw new Exception(ois.readUTF());
            }
        } catch (Exception ex) {
            throw ex;
        }
    }

    public static void deleteGroup(int groupId) throws Exception {
        try {
            oos.writeObject(Message.DeleteGroup);
            oos.writeInt(groupId);
            oos.flush();
            Message message = (Message) ois.readObject();
            if (message != Message.SuccessfulResult) {
                throw new Exception(ois.readUTF());
            }
        } catch (Exception ex) {
            throw ex;
        }
    }

    public static Gruppa readGroup(int groupId) throws Exception {
        try {
            oos.writeObject(Message.ReadGroup);
            oos.writeInt(groupId);
            oos.flush();
            Message message = (Message) ois.readObject();
            if (message == Message.SuccessfulResult) {
                return (Gruppa) ois.readObject();
            } else {
                throw new Exception(ois.readUTF());
            }
        } catch (Exception ex) {
            throw ex;
        }
    }

    public static Groups readGroups() throws Exception {
        try {
            oos.writeObject(Message.ReadGroups);
            oos.flush();
            Message message = (Message) ois.readObject();
            if (message == Message.SuccessfulResult) {
                return (Groups) ois.readObject();
            } else {
                throw new Exception(ois.readUTF());
            }
        } catch (Exception ex) {
            throw ex;
        }
    }

    public static int readGroupsCount() throws Exception {
        try {
            oos.writeObject(Message.ReadGroupsCount);
            oos.flush();
            Message message = (Message) ois.readObject();
            if (message == Message.SuccessfulResult) {
                return ois.readInt();
            } else {
                throw new Exception(ois.readUTF());
            }
        } catch (Exception ex) {
            throw ex;
        }
    }

    public static Gruppa startGroupUpdating(int groupId) throws Exception {
        try {
            oos.writeObject(Message.StartGroupUpdating);
            oos.writeInt(groupId);
            oos.flush();
            Message message = (Message) ois.readObject();
            if (message == Message.SuccessfulResult) {
                return (Gruppa) ois.readObject();
            } else {
                throw new Exception(ois.readUTF());
            }
        } catch (Exception ex) {
            throw ex;
        }
    }

    public static void cancelGroupUpdating(int groupId) throws Exception {
        try {
            oos.writeObject(Message.CancelGroupUpdating);
            oos.writeInt(groupId);
            oos.flush();
            Message message = (Message) ois.readObject();
            if (message != Message.SuccessfulResult) {
                throw new Exception(ois.readUTF());
            }
        } catch (Exception ex) {
            throw ex;
        }
    }

    public static void createStudent(int studentId, String studentName, int groupId) throws Exception {
        try {
            Student student = new Student(studentId, studentName, groupId);
            oos.writeObject(Message.CreateStudent);
            oos.writeObject(student);
            oos.flush();
            Message message = (Message) ois.readObject();
            if (message != Message.SuccessfulResult) {
                throw new Exception(ois.readUTF());
            }
        } catch (Exception ex) {
            throw ex;
        }
    }

    public static void updateStudent(int studentId, String studentName, int groupId) throws Exception {
        try {
            Student student = new Student(studentId, studentName, groupId);
            oos.writeObject(Message.UpdateStudent);
            oos.writeObject(student);
            oos.flush();
            Message message = (Message) ois.readObject();
            if (message != Message.SuccessfulResult) {
                throw new Exception(ois.readUTF());
            }
        } catch (Exception ex) {
            throw ex;
        }
    }

    public static void deleteStudent(int studentId) throws Exception {
        try {
            oos.writeObject(Message.DeleteStudent);
            oos.writeInt(studentId);
            oos.flush();
            Message message = (Message) ois.readObject();
            if (message != Message.SuccessfulResult) {
                throw new Exception(ois.readUTF());
            }
        } catch (Exception ex) {
            throw ex;
        }
    }

    public static Student readStudent(int studentId) throws Exception {
        try {
            oos.writeObject(Message.ReadStudent);
            oos.writeInt(studentId);
            oos.flush();
            Message message = (Message) ois.readObject();
            if (message == Message.SuccessfulResult) {
                return (Student) ois.readObject();
            } else {
                throw new Exception(ois.readUTF());
            }
        } catch (Exception ex) {
            throw ex;
        }
    }

    public static Students readStudents() throws Exception {
        try {
            oos.writeObject(Message.ReadStudents);
            oos.flush();
            Message message = (Message) ois.readObject();
            if (message == Message.SuccessfulResult) {
                return (Students) ois.readObject();
            } else {
                throw new Exception(ois.readUTF());
            }
        } catch (Exception ex) {
            throw ex;
        }
    }

    public static int readStudentsCount() throws Exception {
        try {
            oos.writeObject(Message.ReadStudentsCount);
            oos.flush();
            Message message = (Message) ois.readObject();
            if (message == Message.SuccessfulResult) {
                return ois.readInt();
            } else {
                throw new Exception(ois.readUTF());
            }
        } catch (Exception ex) {
            throw ex;
        }
    }

    public static Student startStudentUpdating(int studentId) throws Exception {
        try {
            oos.writeObject(Message.StartStudentUpdating);
            oos.writeInt(studentId);
            oos.flush();
            Message message = (Message) ois.readObject();
            if (message == Message.SuccessfulResult) {
                return (Student) ois.readObject();
            } else {
                throw new Exception(ois.readUTF());
            }
        } catch (Exception ex) {
            throw ex;
        }
    }

    public static void cancelStudentUpdating(int studentId) throws Exception {
        try {
            oos.writeObject(Message.CancelStudentUpdating);
            oos.writeInt(studentId);
            oos.flush();
            Message message = (Message) ois.readObject();
            if (message != Message.SuccessfulResult) {
                throw new Exception(ois.readUTF());
            }
        } catch (Exception ex) {
            throw ex;
        }
    }

    private static class ClientObserver implements Runnable {

        private Socket clientObserverSocket;
        private ObjectOutputStream oos;
        private ObjectInputStream ois;

        public ClientObserver() {
            try {
                clientObserverSocket = new Socket(ADDRESS, OBSERVER_PORT);
                oos = new ObjectOutputStream(clientObserverSocket.getOutputStream());
                ois = new ObjectInputStream(clientObserverSocket.getInputStream());
            } catch (UnknownHostException ex) {
                printException(ex);
            } catch (IOException ex) {
                printException(ex);
            }
        }

        @Override
        public void run() {
            try {
                Message message;
                while (!Thread.currentThread().isInterrupted()) {
                    message = (Message) ois.readObject();
                    switch (message) {
                        case GroupCreated:
                        case GroupUpdated:
                        case GroupDeleted:
                        case StudentCreated:
                        case StudentUpdated:
                        case StudentDeleted: {
                            notifyChanges(message);
                            break;
                        }
                        case StopClient: {
                            ois.close();
                            oos.flush();
                            oos.close();
                            clientObserverSocket.close();
                            Thread.currentThread().interrupt();
                            break;
                        }
                        default: {
                            throw new WrongMessageException("Wrong message: " + message + "!");
                        }
                    }
                }
            } catch (ClassNotFoundException | IOException | WrongMessageException ex) {
                printException(ex);
            }
        }

        private void notifyChanges(Message message) throws IOException, WrongMessageException {
            String thisClientChanges = "", anotherClientChanges = "";
            switch (message) {
                case GroupCreated: {
                    thisClientChanges = "Group is created successfully!";
                    anotherClientChanges = "Some group was created by another client! Please reload page.";
                    break;
                }
                case GroupUpdated: {
                    thisClientChanges = "Group is updated successfully!";
                    anotherClientChanges = "Some group was updated by another client! Please reload page.";
                    break;
                }
                case GroupDeleted: {
                    thisClientChanges = "Group is deleted successfully!";
                    anotherClientChanges = "Some group was deleted by another client! Please reload page.";
                    break;
                }
                case StudentCreated: {
                    thisClientChanges = "Student is created successfully!";
                    anotherClientChanges = "Some student was created by another client! Please reload page.";
                    break;
                }
                case StudentUpdated: {
                    thisClientChanges = "Student is updated successfully!";
                    anotherClientChanges = "Some student was updated by another client! Please reload page.";
                    break;
                }
                case StudentDeleted: {
                    thisClientChanges = "Student is deleted successfully!";
                    anotherClientChanges = "Some student was deleted by another client! Please reload page.";
                    break;
                }
                default: {
                    throw new WrongMessageException("Wrong message: " + message + "!");
                }
            }
            int id = ois.readInt();
            view.showInfoMessage((clientId == id) ? thisClientChanges : anotherClientChanges);
        }
    }

    public static void main(String args[]) {
        startClientMain();
    }
}
