package ru.nikita.security.services;

import org.springframework.stereotype.Service;
import ru.nikita.security.exceptions.StudentNotFoundException;
import ru.nikita.security.models.Student;

import java.util.ArrayList;
import java.util.List;

@Service
public class StudentService {

    private final List<Student> students = new ArrayList<>(List.of(
            Student.builder()
                    .id(1L)
                    .firstName("James")
                    .lastName("Bond")
                    .age(35)
                    .build(),
            Student.builder()
                    .id(2L)
                    .firstName("Ditta")
                    .lastName("Fon Tiss")
                    .age(43)
                    .build(),
            Student.builder()
                    .id(3L)
                    .firstName("Kiso")
                    .lastName("Babu")
                    .age(18)
                    .build()
    ));

    public List<Student> getStudents() {
        return students;
    }

    public Student getStudent(Long userId) {
        return students
                .stream()
                .filter(student -> student.getId().equals(userId))
                .findFirst()
                .orElseThrow(StudentNotFoundException::new);
    }

    public Student addStudent(Student student) {
        student.setId(Double.valueOf(Math.random() * 1000).longValue()); //FIXME
        students.add(student);
        return student;
    }

    public Student updateStudent(Student student) {
        Student studentFromDB = students
                .stream()
                .filter(stud -> stud.getId().equals(student.getId()))
                .findFirst()
                .orElseThrow(StudentNotFoundException::new);

        studentFromDB.setFirstName(student.getFirstName());
        studentFromDB.setLastName(student.getLastName());
        studentFromDB.setAge(student.getAge());

        return studentFromDB;
    }

    public void deleteStudent(Long studentId) {
        Student studentFromDB = students
                .stream()
                .filter(stud -> stud.getId().equals(studentId))
                .findFirst()
                .orElseThrow(StudentNotFoundException::new);

        students.remove(studentFromDB);
    }
}
