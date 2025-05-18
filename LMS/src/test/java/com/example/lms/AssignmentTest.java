package com.example.lms;
import com.example.lms.controllers.AssignmentController;
import com.example.lms.models.Assignment;
import com.example.lms.repositories.AssignmentRepository;
import com.example.lms.services.AssignmentService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import java.util.Optional;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.times;

@ExtendWith(MockitoExtension.class)
 class AssignmentTest {
    @InjectMocks
    private AssignmentController assignmentController;
    @Mock
    private AssignmentService assignmentService;  // Service being tested

    @Mock
    private AssignmentRepository assignmentRepository;  // Mock repository

    private Assignment testAssignment;  // Regular assignment object

    @BeforeEach
    void setUp() {
        // Create a real Assignment object
        testAssignment = new Assignment();
        testAssignment.setAssignmentID(1);
        testAssignment.setTitle("Test Assignment");
        testAssignment.setDescription("Test Description");
        testAssignment.setDeadline("31/12/2024");
    }

    @Test
    void testCreateAssignment() {
        // Mock the service to save the assignment (no return value)
        doNothing().when(assignmentService).createAssignment(testAssignment);

        // Call the controller method
        assignmentController.createAssignment(testAssignment);

        // Verify that the service was called once to create the assignment
        verify(assignmentService, times(1)).createAssignment(testAssignment);
    }

    @Test
    void testSubmitAssignment() {
        // Mock the service to return the submitted assignment after saving it
        when(assignmentService.submitAssignment(testAssignment)).thenReturn(testAssignment);

        // Call the controller method
        Assignment submittedAssignment = assignmentController.submitAssignment(testAssignment).getBody();

        // Verify that the service was called and the returned assignment is correct
        verify(assignmentService, times(1)).submitAssignment(testAssignment);
        assertEquals(testAssignment, submittedAssignment);
    }
    @Test
    void testGradeAssignment() {
        // Mock the service to return the graded assignment
        when(assignmentService.gradeAssignment(1, 3.0, "Excellent")).thenReturn(testAssignment);

        // Call the controller method
        Assignment gradedAssignment = assignmentController.gradeAssignment(1, 3.0, "Excellent").getBody();

        // Verify that the service was called and the assignment was graded
        verify(assignmentService, times(1)).gradeAssignment(1, 3.0, "Excellent");
        assertEquals(testAssignment, gradedAssignment);
    }

    @Test
    void testGetAssignment() {
        // Mock the repository to return the assignment when the ID matches
        when(assignmentRepository.findById(1)).thenReturn(Optional.of(testAssignment));

        // Call the controller method
        Assignment fetchedAssignment = assignmentController.getAssignment(1).getBody();

        // Verify that the repository was called and the assignment was fetched
        verify(assignmentRepository, times(1)).findById(1);
        assertEquals(testAssignment, fetchedAssignment);
    }

}
