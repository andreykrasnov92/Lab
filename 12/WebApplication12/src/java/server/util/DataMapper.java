package server.util;

import entities.Gruppa;
import entities.Student;
import java.util.ArrayList;
import java.util.List;
import model.Model;
import shared.entities.SharedGroup;
import shared.entities.SharedStudent;
import shared.model.SharedGroups;
import shared.model.SharedModel;
import shared.model.SharedStudents;

public class DataMapper {

    public static SharedModel mapModel(Model model) {
        if (model == null) {
            return null;
        }
        SharedGroups sharedGroups = mapGroups(model.getGroups());
        SharedStudents sharedStudents = mapStudents(model.getStudents());
        SharedModel sharedModel = new SharedModel(sharedGroups.getGroups(), sharedStudents.getStudents());
        return sharedModel;
    }

    public static SharedGroups mapGroups(List<Gruppa> groups) {
        if (groups == null) {
            return null;
        }
        SharedGroups sharedGroups = new SharedGroups(new ArrayList());
        for (Gruppa group : groups) {
            sharedGroups.getGroups().add(mapGroup(group));
        }
        return sharedGroups;
    }

    public static SharedStudents mapStudents(List<Student> students) {
        if (students == null) {
            return null;
        }
        SharedStudents sharedStudents = new SharedStudents(new ArrayList());
        for (Student student : students) {
            sharedStudents.getStudents().add(mapStudent(student));
        }
        return sharedStudents;
    }

    public static SharedGroup mapGroup(Gruppa group) {
        if (group == null) {
            return null;
        }
        SharedGroup sharedGroup = new SharedGroup(group.getGroupid(), group.getGroupname());
        return sharedGroup;
    }

    public static SharedStudent mapStudent(Student student) {
        if (student == null) {
            return null;
        }
        SharedStudent sharedStudent = new SharedStudent(student.getStudentid(), student.getStudentname(), student.getGroupid().getGroupid());
        return sharedStudent;
    }

    public static Model unmapSharedModel(SharedModel sharedModel) {
        if (sharedModel == null) {
            return null;
        }
        List<Gruppa> groups = unmapSharedGroups(new SharedGroups(sharedModel.getGroups()));
        List<Student> students = unmapSharedStudents(new SharedStudents(sharedModel.getStudents()));
        Model model = new Model(groups, students);
        return model;
    }

    public static List<Gruppa> unmapSharedGroups(SharedGroups sharedGroups) {
        if (sharedGroups == null) {
            return null;
        }
        List<Gruppa> groups = new ArrayList<>();
        for (SharedGroup sharedGroup : sharedGroups.getGroups()) {
            groups.add(unmapSharedGroup(sharedGroup));
        }
        return groups;
    }

    public static List<Student> unmapSharedStudents(SharedStudents sharedStudents) {
        if (sharedStudents == null) {
            return null;
        }
        List<Student> students = new ArrayList<>();
        for (SharedStudent sharedStudent : sharedStudents.getStudents()) {
            students.add(unmapSharedStudent(sharedStudent));
        }
        return students;
    }

    public static Gruppa unmapSharedGroup(SharedGroup sharedGroup) {
        if (sharedGroup == null) {
            return null;
        }
        Gruppa group = new Gruppa(sharedGroup.getGroupId(), sharedGroup.getGroupName());
        return group;
    }

    public static Student unmapSharedStudent(SharedStudent sharedStudent) {
        if (sharedStudent == null) {
            return null;
        }
        Student student = new Student(sharedStudent.getStudentId(), sharedStudent.getStudentName());
        return student;
    }
}
