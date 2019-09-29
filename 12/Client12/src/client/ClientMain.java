package client;

import client.entities.Gruppa;
import client.entities.Student;
import client.model.Groups;
import client.model.Students;
import client.service.GruppaJerseyClient;
import client.service.StudentJerseyClient;
import client.util.ClientDataMapper;
import client.view.View;
import shared.entities.SharedGroup;
import shared.entities.SharedStudent;
import shared.model.SharedGroups;
import shared.model.SharedStudents;

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

    public static void startClientMain() {
        View.runView();
    }

    public static void createGroup(int groupId, String groupName) throws Exception {
        try {
            GruppaJerseyClient client = new GruppaJerseyClient();
            client.create(ClientDataMapper.mapGroup(new Gruppa(groupId, groupName)));
            client.close();
        } catch (Exception ex) {
            throw ex;
        }
    }

    public static void updateGroup(int groupId, String groupName) throws Exception {
        try {
            GruppaJerseyClient client = new GruppaJerseyClient();
            client.edit(ClientDataMapper.mapGroup(new Gruppa(groupId, groupName)), Integer.toString(groupId));
            client.close();
        } catch (Exception ex) {
            throw ex;
        }
    }

    public static void deleteGroup(int groupId) throws Exception {
        try {
            GruppaJerseyClient client = new GruppaJerseyClient();
            client.remove(Integer.toString(groupId));
            client.close();
        } catch (Exception ex) {
            throw ex;
        }
    }

    public static Gruppa readGroup(int groupId) throws Exception {
        try {
            GruppaJerseyClient client = new GruppaJerseyClient();
            Gruppa group = ClientDataMapper.unmapSharedGroup((SharedGroup) client.find(SharedGroup.class, Integer.toString(groupId)));
            client.close();
            return group;
        } catch (Exception ex) {
            throw ex;
        }
    }

    public static Groups readGroups() throws Exception {
        try {
            GruppaJerseyClient client = new GruppaJerseyClient();
            Groups groups = ClientDataMapper.unmapSharedGroups((SharedGroups) client.findAll(SharedGroups.class));
            client.close();
            return groups;
        } catch (Exception ex) {
            throw ex;
        }
    }

    public static int readGroupsCount() throws Exception {
        try {
            GruppaJerseyClient client = new GruppaJerseyClient();
            int count = Integer.parseInt(client.countREST());
            client.close();
            return count;
        } catch (Exception ex) {
            throw ex;
        }
    }

    public static void createStudent(int studentId, String studentName, int groupId) throws Exception {
        try {
            StudentJerseyClient client = new StudentJerseyClient();
            client.create(ClientDataMapper.mapStudent(new Student(studentId, studentName, groupId)));
            client.close();
        } catch (Exception ex) {
            throw ex;
        }
    }

    public static void updateStudent(int studentId, String studentName, int groupId) throws Exception {
        try {
            StudentJerseyClient client = new StudentJerseyClient();
            client.edit(ClientDataMapper.mapStudent(new Student(studentId, studentName, groupId)), Integer.toString(studentId));
            client.close();
        } catch (Exception ex) {
            throw ex;
        }
    }

    public static void deleteStudent(int studentId) throws Exception {
        try {
            StudentJerseyClient client = new StudentJerseyClient();
            client.remove(Integer.toString(studentId));
            client.close();
        } catch (Exception ex) {
            throw ex;
        }
    }

    public static Student readStudent(int studentId) throws Exception {
        try {
            StudentJerseyClient client = new StudentJerseyClient();
            Student student = ClientDataMapper.unmapSharedStudent((SharedStudent) client.find(SharedStudent.class, Integer.toString(studentId)));
            client.close();
            return student;
        } catch (Exception ex) {
            throw ex;
        }
    }

    public static Students readStudents() throws Exception {
        try {
            StudentJerseyClient client = new StudentJerseyClient();
            Students students = ClientDataMapper.unmapSharedStudents((SharedStudents) client.findAll(SharedStudents.class));
            client.close();
            return students;
        } catch (Exception ex) {
            throw ex;
        }
    }

    public static int readStudentsCount() throws Exception {
        try {
            StudentJerseyClient client = new StudentJerseyClient();
            int count = Integer.parseInt(client.countREST());
            client.close();
            return count;
        } catch (Exception ex) {
            throw ex;
        }
    }

    public static Gruppa startGroupUpdating(int groupId) throws Exception {
        return readGroup(groupId);
    }

    public static void cancelGroupUpdating(int groupId) throws Exception {
    }

    public static Student startStudentUpdating(int studentId) throws Exception {
        return readStudent(studentId);
    }

    public static void cancelStudentUpdating(int studentId) throws Exception {
    }

    public static void serializeModel(String filepath) throws Exception {
        throw new UnsupportedOperationException("Model serialization does not supported here!");
    }

    public static void deserializeModel(String filepath) throws Exception {
        throw new UnsupportedOperationException("Model deserialization does not supported here!");
    }

    public static void main(String args[]) {
        startClientMain();
    }
}
