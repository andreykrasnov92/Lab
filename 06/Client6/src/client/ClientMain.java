package client;

import client.view.View;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;
import shared.database.entities.Gruppa;
import shared.database.entities.Student;
import shared.database.model.Groups;
import shared.database.model.Students;
import shared.exceptions.WrongMessageException;
import shared.util.Message;
import static shared.util.MessageController.*;
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
    private static DataOutputStream dos;
    private static DataInputStream dis;
    private static View view;
    private static ClientObserver clientObserver;
    private static boolean connected = false;
    private static int clientId = -1;

    static {
        try {
            clientSocket = new Socket(ADDRESS, PORT);
            dos = new DataOutputStream(clientSocket.getOutputStream());
            dis = new DataInputStream(clientSocket.getInputStream());
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
            writeMessageToOutputStream(dos, Message.StartServer);
            System.out.println("Server starting not supported yet...");
        } catch (Exception ex) {
            printException(ex);
        }
    }

    public static void stopServer() {
        try {
            writeMessageToOutputStream(dos, Message.StopServer);
            System.out.println("Server stopping not supported yet...");
        } catch (Exception ex) {
            printException(ex);
        }
    }

    public static void startClient() {
        try {
            writeMessageToOutputStream(dos, Message.StartClient);
            Message message = readMessageFromInputStream(dis);
            if (message == Message.SuccessfulResult) {
                clientId = readIntegerFromInputStream(dis);
                System.out.println("Client with id " + clientId + " started");
            } else {
                throw new Exception(readStringFromInputStream(dis));
            }
        } catch (Exception ex) {
            printException(ex);
        }
    }

    public static void stopClient() {
        try {
            writeMessageToOutputStream(dos, Message.StopClient);
            Message message = readMessageFromInputStream(dis);
            if (message == Message.SuccessfulResult) {
                dis.close();
                dos.flush();
                dos.close();
                clientSocket.close();
            } else {
                throw new Exception(readStringFromInputStream(dis));
            }
        } catch (Exception ex) {
            printException(ex);
        }
    }

    public static void serializeModel(String filepath) throws Exception {
        try {
            writeMessageToOutputStream(dos, Message.SerializeModel);
            writeStringToOutputStream(dos, filepath);
            Message message = readMessageFromInputStream(dis);
            if (message != Message.SuccessfulResult) {
                throw new Exception(readStringFromInputStream(dis));
            }
        } catch (Exception ex) {
            throw ex;
        }
    }

    public static void deserializeModel(String filepath) throws Exception {
        try {
            writeMessageToOutputStream(dos, Message.DeserializeModel);
            writeStringToOutputStream(dos, filepath);
            Message message = readMessageFromInputStream(dis);
            if (message != Message.SuccessfulResult) {
                throw new Exception(readStringFromInputStream(dis));
            }
        } catch (Exception ex) {
            throw ex;
        }
    }

    public static void createGroup(int groupId, String groupName) throws Exception {
        try {
            Gruppa group = new Gruppa(groupId, groupName);
            writeMessageToOutputStream(dos, Message.CreateGroup);
            writeGroupToOutputStream(dos, group);
            Message message = readMessageFromInputStream(dis);
            if (message != Message.SuccessfulResult) {
                throw new Exception(readStringFromInputStream(dis));
            }
        } catch (Exception ex) {
            throw ex;
        }
    }

    public static void updateGroup(int groupId, String groupName) throws Exception {
        try {
            Gruppa group = new Gruppa(groupId, groupName);
            writeMessageToOutputStream(dos, Message.UpdateGroup);
            writeGroupToOutputStream(dos, group);
            Message message = readMessageFromInputStream(dis);
            if (message != Message.SuccessfulResult) {
                throw new Exception(readStringFromInputStream(dis));
            }
        } catch (Exception ex) {
            throw ex;
        }
    }

    public static void deleteGroup(int groupId) throws Exception {
        try {
            writeMessageToOutputStream(dos, Message.DeleteGroup);
            writeIntegerToOutputStream(dos, groupId);
            Message message = readMessageFromInputStream(dis);
            if (message != Message.SuccessfulResult) {
                throw new Exception(readStringFromInputStream(dis));
            }
        } catch (Exception ex) {
            throw ex;
        }
    }

    public static Gruppa readGroup(int groupId) throws Exception {
        try {
            writeMessageToOutputStream(dos, Message.ReadGroup);
            writeIntegerToOutputStream(dos, groupId);
            Message message = readMessageFromInputStream(dis);
            if (message == Message.SuccessfulResult) {
                return readGroupFromInputStream(dis);
            } else {
                throw new Exception(readStringFromInputStream(dis));
            }
        } catch (Exception ex) {
            throw ex;
        }
    }

    public static Groups readGroups() throws Exception {
        try {
            writeMessageToOutputStream(dos, Message.ReadGroups);
            Message message = readMessageFromInputStream(dis);
            if (message == Message.SuccessfulResult) {
                return readGroupsFromInputStream(dis);
            } else {
                throw new Exception(readStringFromInputStream(dis));
            }
        } catch (Exception ex) {
            throw ex;
        }
    }

    public static int readGroupsCount() throws Exception {
        try {
            writeMessageToOutputStream(dos, Message.ReadGroupsCount);
            Message message = readMessageFromInputStream(dis);
            if (message == Message.SuccessfulResult) {
                return readIntegerFromInputStream(dis);
            } else {
                throw new Exception(readStringFromInputStream(dis));
            }
        } catch (Exception ex) {
            throw ex;
        }
    }

    public static Gruppa startGroupUpdating(int groupId) throws Exception {
        try {
            writeMessageToOutputStream(dos, Message.StartGroupUpdating);
            writeIntegerToOutputStream(dos, groupId);
            Message message = readMessageFromInputStream(dis);
            if (message == Message.SuccessfulResult) {
                return readGroupFromInputStream(dis);
            } else {
                throw new Exception(readStringFromInputStream(dis));
            }
        } catch (Exception ex) {
            throw ex;
        }
    }

    public static void cancelGroupUpdating(int groupId) throws Exception {
        try {
            writeMessageToOutputStream(dos, Message.CancelGroupUpdating);
            writeIntegerToOutputStream(dos, groupId);
            Message message = readMessageFromInputStream(dis);
            if (message != Message.SuccessfulResult) {
                throw new Exception(readStringFromInputStream(dis));
            }
        } catch (Exception ex) {
            throw ex;
        }
    }

    public static void createStudent(int studentId, String studentName, int groupId) throws Exception {
        try {
            Student student = new Student(studentId, studentName, groupId);
            writeMessageToOutputStream(dos, Message.CreateStudent);
            writeStudentToOutputStream(dos, student);
            Message message = readMessageFromInputStream(dis);
            if (message != Message.SuccessfulResult) {
                throw new Exception(readStringFromInputStream(dis));
            }
        } catch (Exception ex) {
            throw ex;
        }
    }

    public static void updateStudent(int studentId, String studentName, int groupId) throws Exception {
        try {
            Student student = new Student(studentId, studentName, groupId);
            writeMessageToOutputStream(dos, Message.UpdateStudent);
            writeStudentToOutputStream(dos, student);
            Message message = readMessageFromInputStream(dis);
            if (message != Message.SuccessfulResult) {
                throw new Exception(readStringFromInputStream(dis));
            }
        } catch (Exception ex) {
            throw ex;
        }
    }

    public static void deleteStudent(int studentId) throws Exception {
        try {
            writeMessageToOutputStream(dos, Message.DeleteStudent);
            writeIntegerToOutputStream(dos, studentId);
            Message message = readMessageFromInputStream(dis);
            if (message != Message.SuccessfulResult) {
                throw new Exception(readStringFromInputStream(dis));
            }
        } catch (Exception ex) {
            throw ex;
        }
    }

    public static Student readStudent(int studentId) throws Exception {
        try {
            writeMessageToOutputStream(dos, Message.ReadStudent);
            writeIntegerToOutputStream(dos, studentId);
            Message message = readMessageFromInputStream(dis);
            if (message == Message.SuccessfulResult) {
                return readStudentFromInputStream(dis);
            } else {
                throw new Exception(readStringFromInputStream(dis));
            }
        } catch (Exception ex) {
            throw ex;
        }
    }

    public static Students readStudents() throws Exception {
        try {
            writeMessageToOutputStream(dos, Message.ReadStudents);
            Message message = readMessageFromInputStream(dis);
            if (message == Message.SuccessfulResult) {
                return readStudentsFromInputStream(dis);
            } else {
                throw new Exception(readStringFromInputStream(dis));
            }
        } catch (Exception ex) {
            throw ex;
        }
    }

    public static int readStudentsCount() throws Exception {
        try {
            writeMessageToOutputStream(dos, Message.ReadStudentsCount);
            Message message = readMessageFromInputStream(dis);
            if (message == Message.SuccessfulResult) {
                return readIntegerFromInputStream(dis);
            } else {
                throw new Exception(readStringFromInputStream(dis));
            }
        } catch (Exception ex) {
            throw ex;
        }
    }

    public static Student startStudentUpdating(int studentId) throws Exception {
        try {
            writeMessageToOutputStream(dos, Message.StartStudentUpdating);
            writeIntegerToOutputStream(dos, studentId);
            Message message = readMessageFromInputStream(dis);
            if (message == Message.SuccessfulResult) {
                return readStudentFromInputStream(dis);
            } else {
                throw new Exception(readStringFromInputStream(dis));
            }
        } catch (Exception ex) {
            throw ex;
        }
    }

    public static void cancelStudentUpdating(int studentId) throws Exception {
        try {
            writeMessageToOutputStream(dos, Message.CancelStudentUpdating);
            writeIntegerToOutputStream(dos, studentId);
            Message message = readMessageFromInputStream(dis);
            if (message != Message.SuccessfulResult) {
                throw new Exception(readStringFromInputStream(dis));
            }
        } catch (Exception ex) {
            throw ex;
        }
    }

    private static class ClientObserver implements Runnable {

        private Socket clientObserverSocket;
        private DataOutputStream dos;
        private DataInputStream dis;

        public ClientObserver() {
            try {
                clientObserverSocket = new Socket(ADDRESS, OBSERVER_PORT);
                dos = new DataOutputStream(clientObserverSocket.getOutputStream());
                dis = new DataInputStream(clientObserverSocket.getInputStream());
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
                    message = readMessageFromInputStream(dis);
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
                            dis.close();
                            dos.flush();
                            dos.close();
                            clientObserverSocket.close();
                            Thread.currentThread().interrupt();
                            break;
                        }
                        default: {
                            throw new WrongMessageException("Wrong message: " + message + "!");
                        }
                    }
                }
            } catch (IOException | WrongMessageException ex) {
                printException(ex);
            }
        }

        private void notifyChanges(Message message) throws WrongMessageException {
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
            int id = readIntegerFromInputStream(dis);
            view.showInfoMessage((clientId == id) ? thisClientChanges : anotherClientChanges);
        }
    }

    public static void main(String args[]) {
        startClientMain();
    }
}
