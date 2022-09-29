package com.example.demo.student;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import static com.example.demo.student.Gender.MALE;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@DataJpaTest
class StudentRepositoryTest {

    @Autowired
    private StudentRepository studentRepositoryUnderTest;

    @AfterEach
    void tearDown() {
        studentRepositoryUnderTest.deleteAll();
    }

    @Test
    void itShouldCheckIfStudentEmailExists() {
        // --- given
        String name = "Eugene";
        String email = "eugene@gmail.com";
        Student existingStudent = new Student(name, email, MALE);
        studentRepositoryUnderTest.save(existingStudent);

        // --- when
        boolean expected = studentRepositoryUnderTest.verifyEmailExists(existingStudent.getEmail());

        // --- then
        assertThat(expected).isTrue();
    }

    @Test
    void itShouldCheckIfStudentEmailDoesNotExist() {
        // --- given
        String name = "Henry";
        String email = "henry@gmail.com";
        Student nonExistingStudent = new Student(name, email, MALE);

        // --- when
        boolean expected = studentRepositoryUnderTest.verifyEmailExists(nonExistingStudent.getEmail());

        // --- then
        assertThat(expected).isFalse();
    }
}