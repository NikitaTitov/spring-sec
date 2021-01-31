package ru.nikita.security.controllers;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import ru.nikita.security.models.Student;
import ru.nikita.security.services.StudentService;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/v1/students")
@RequiredArgsConstructor
public class StudentController {

    private final StudentService studentService;

    @GetMapping
    public List<Student> getStudentList() {
        return studentService.getStudents();
    }

    @GetMapping("/{studentId}")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_OBSERVER')")
    public Student getStudent(@PathVariable Long studentId) {
        return studentService.getStudent(studentId);
    }

    @PostMapping
    @ResponseBody
    @PreAuthorize("hasAuthority('WRITE')")
    public ResponseEntity<String> addUser(@RequestBody Student student) {
        log.info("Adding student to db {}", student);
        studentService.addStudent(student);
        return ResponseEntity.ok().body("Success added");
    }

    @PutMapping
    @ResponseBody
    @PreAuthorize("hasAuthority('UPDATE')")
    public ResponseEntity<String> updateStudent(@RequestBody Student student) {
        log.info("Updating student in db {}", student);
        studentService.updateStudent(student);
        return ResponseEntity.ok().body("Success updated");
    }

    @DeleteMapping("/{studentId}")
    @ResponseBody
    @PreAuthorize("hasAuthority('DELETE')")
    public ResponseEntity<String> deleteStudent(@PathVariable Long studentId) {
        studentService.deleteStudent(studentId);
        return ResponseEntity.ok().body("Success deleted");
    }
}
