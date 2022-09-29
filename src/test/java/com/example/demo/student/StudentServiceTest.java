package com.example.demo.student;

import com.example.demo.student.exception.BadRequestException;
import com.example.demo.student.exception.StudentNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static com.example.demo.student.Gender.MALE;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class StudentServiceTest {

    @Mock
    private StudentRepository studentRepository;
    private StudentService studentServiceUnderTest;

    @BeforeEach
    void setUp() {
        studentServiceUnderTest = new StudentService(studentRepository);
    }

    @Test
    void itShouldGetAllStudentsFromDB() {
        // when
        studentServiceUnderTest.getAllStudents();

        // then
        verify(studentRepository).findAll();
    }

    @Test
    void itShouldAddSingleStudentIntoDB() {
        // --- given
        String name = "Eugene";
        String email = "eugene@gmail.com";
        Student studentToBeAdded = new Student(name, email, MALE);

        // --- when
        studentServiceUnderTest.addStudent(studentToBeAdded);

        // --- then
        ArgumentCaptor<Student> studentArgumentCaptor = ArgumentCaptor.forClass(Student.class);
        verify(studentRepository).save(studentArgumentCaptor.capture());
        Student capturedStudentArgument = studentArgumentCaptor.getValue();
        assertThat(capturedStudentArgument).isEqualTo(studentToBeAdded);
    }

    @Test
    void itShouldThrowExceptionWhenAddingStudentWithExistingEmailIntoDB() {
        // --- given
        String name = "Eugene";
        String email = "eugene@gmail.com";
        Student existingStudent = new Student(name, email, MALE);
        given(studentRepository.verifyEmailExists(email)).willReturn(true);

        // --- when

        // --- then
        assertThatThrownBy(() -> studentServiceUnderTest.addStudent(existingStudent))
                .isInstanceOf(BadRequestException.class)
                .hasMessageContaining("Email " + email + " taken");
        verify(studentRepository, never()).save(any());
    }

    @Test
    void itShouldDeleteASingleExistingStudentFromTheDB() {
        // --- given
        long studentId = 1L;
        given(studentRepository.existsById(studentId)).willReturn(true);

        // --- when
        studentServiceUnderTest.deleteStudentById(studentId);

        // --- then
        ArgumentCaptor<Long> studentIdArgumentCaptor = ArgumentCaptor.forClass(Long.class);
        verify(studentRepository).deleteById(studentIdArgumentCaptor.capture());
        Long capturedStudentIdArgument = studentIdArgumentCaptor.getValue();
        assertThat(capturedStudentIdArgument).isEqualTo(studentId);
    }

    @Test
    void itShouldThrowExceptionWhenAttemptingToDeleteStudentByNonExistingIdFromTheDB() {
        // --- given
        long studentId = 1L;
        given(studentRepository.existsById(studentId)).willReturn(false);

        // --- when

        // --- then
        assertThatThrownBy(() -> studentServiceUnderTest.deleteStudentById(studentId))
                .isInstanceOf(StudentNotFoundException.class)
                .hasMessageContaining("Student with id " + studentId + " does not exists");
        verify(studentRepository, never()).deleteById(any());
    }
}