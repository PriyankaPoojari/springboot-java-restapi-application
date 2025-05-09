package mockitoTest;
import com.test.studentapp.controller.StudentController;
import com.test.studentapp.model.Student;
import com.test.studentapp.service.StudentService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

//@SpringBootTest
public class StudentControllerTest {
	
	    @InjectMocks
	    private StudentController studentController;

	    @Mock
	    private StudentService studentService;

	    private Student student;

	    @BeforeEach
	    void setUp() {
	        MockitoAnnotations.openMocks(this);
	        student = new Student();
	        student.setId(1);
	        student.setFirstName("John");
	        student.setLastName("Doe");
	        student.setClassName("10A");
	        student.setNationality("US");
	    }

	    @Test
	    void testGetAllStudent() {
	        when(studentService.getAllStudent()).thenReturn(Arrays.asList(student));

	        List<String> result = studentController.getAllStudent();
	        assertEquals(1, result.size());
	        assertTrue(result.get(0).contains("John"));
	    }

	    @Test
	    void testSaveStudent_New() throws Exception {
	        when(studentService.existById(student.getId())).thenReturn(false);
//	        when(student.checkNull()).thenReturn(false);

	        String result = studentController.saveStudent(student);

	        verify(studentService).saveOrUpdate(student);
	        assertTrue(result.contains("Added Successfully"));
	    }

	    @Test
	    void testSaveStudent_AlreadyExists() throws Exception {
	        when(studentService.existById(student.getId())).thenReturn(true);
//	        when(student.checkNull()).thenReturn(false);

	        String result = studentController.saveStudent(student);
	        assertTrue(result.contains("already exist"));
	    }

	    @Test
	    void testDeleteStudent_Exists() {
	        when(studentService.existById(student.getId())).thenReturn(true);

	        String result = studentController.deleteStudent(student);
	        verify(studentService).delete(student.getId());
	        assertTrue(result.contains("Deleted Successfully"));
	    }

	    @Test
	    void testDeleteStudent_NotExists() {
	        when(studentService.existById(student.getId())).thenReturn(false);

	        String result = studentController.deleteStudent(student);
	        assertTrue(result.contains("Error"));
	    }

	    @Test
	    void testUpdateStudent_Success() {
	        Student updated = new Student();
	        updated.setId(1);
	        updated.setFirstName("Jane");

	        when(studentService.existById(1)).thenReturn(true);
	        when(studentService.getStudentById(1)).thenReturn(student);

	        String result = studentController.updateStudent(updated);

	        verify(studentService).saveOrUpdate(any(Student.class));
	        assertTrue(result.contains("Updated Successfully"));
	    }

	    @Test
	    void testGetByFilter_WithId() {
	        when(studentService.existById(1)).thenReturn(true);
	        when(studentService.getStudentById(1)).thenReturn(student);

	        List<String> result = studentController.getByFilter("1", null);
	        assertEquals(1, result.size());
	        assertTrue(result.get(0).contains("John"));
	    }

//	    @Test
//	    void testGetByFilter_WithClassName() {
//	        when(studentService.getAllStudent("10A")).thenReturn(List.of(student));
//
//	        List<String> result = studentController.getByFilter(null, "10A");
//	        assertEquals(1, result.size());
//	        assertTrue(result.get(0).contains("10A"));
//	    }
	

}
