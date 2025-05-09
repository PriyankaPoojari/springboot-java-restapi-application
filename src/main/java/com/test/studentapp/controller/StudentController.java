package com.test.studentapp.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.HttpRequestHandler;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.test.studentapp.StringUtil;
import com.test.studentapp.model.Student;
import com.test.studentapp.service.StudentService;

import javassist.tools.web.BadHttpRequest;

@RestController
public class StudentController {
	@Autowired
	StudentService studentService;  
	
	//creating a get mapping that retrieves all the students detail from the database   
	@GetMapping("/student")
	public List<String> getAllStudent(){  
		List<String> studentsString = new ArrayList<>();
		List<Student> students = studentService.getAllStudent();  
		students.forEach(student -> studentsString.add(student.toString()));
		return studentsString;
	}  
	
		
	//delete an existing record on the basis of unique ID 
	@DeleteMapping("/student")
	public String deleteStudent(@RequestBody Student student)  {  
		if(studentService.existById(student.getId())) {
			studentService.delete(student.getId());  
			return "Deleted Successfully : ID="+student.getId();
		}else {
			return "Error: Record doesnt exists by ID="+student.getId() ;
		}
	}  
	
	//creating add a new record  
	@PostMapping("/student")
	public String saveStudent(@RequestBody Student student) throws IllegalAccessException, BadHttpRequest{ 
		System.out.println("student="+student);
		if(student.checkNull()) {
			return "Error: Request Body has BAD_Format data";
		}
		
		if(!studentService.existById(student.getId())) {
			studentService.saveOrUpdate(student);  
			return "Added Successfully : ID="+student.getId();
		}else {
			return "Error: Record already exist!!! ID="+student.getId();
		}
	}  
	
	//update an existing record on the basis of unique ID
	@PutMapping("/student")
	public String updateStudent(@RequestBody Student newStudent)  {  
		if(!studentService.existById(newStudent.getId())) {
			return "Error: No ID found. ID="+newStudent.getId();
		}
		Student updatedStudent = studentService.getStudentById(newStudent.getId());
		if(!StringUtil.isEmpty(newStudent.getFirstName())) updatedStudent.setFirstName(newStudent.getFirstName());
		if(!StringUtil.isEmpty(newStudent.getLastName())) updatedStudent.setLastName(newStudent.getLastName());
		if(!StringUtil.isEmpty(newStudent.getClassName())) updatedStudent.setClassName(newStudent.getClassName());	     
		if(!StringUtil.isEmpty(newStudent.getNationality())) updatedStudent.setNationality(newStudent.getNationality());
		
		studentService.saveOrUpdate(updatedStudent); 
		return "Updated Successfully : "+updatedStudent.getId();
		 
	}  		
	
//		//get mapping that retrieves the detail of a specific class students  
//		@GetMapping("/fetchStudents/{class}")  
//		private Student getStudentByClass(@PathVariable("class") String className){  
//			return null;//studentService.getStudentByClass(className);  
//		} 
		
		//get mapping that retrieves the detail of a specific class students  
//		@GetMapping("/fetchStudents")  
//		private Student getStudentById(@Context UriInfo ui){   
//			return studentService.getStudentById(id);  
//		} 
		
		@RequestMapping(value = "/fetchStudents", method = RequestMethod.GET)
		@GetMapping
		public List<String> getByFilter(@RequestParam(value="Id", required=false) String id,@RequestParam(value="class", required= false)String className){
			List<String> studentsString = new ArrayList<>();
			if(StringUtils.isNotEmpty(id) && studentService.existById(Integer.parseInt(id))){
				Student student = studentService.getStudentById(Integer.parseInt(id));
				studentsString.add(student.toString());
			}else if(StringUtils.isNotEmpty(className)) {
				List<Student> students = studentService.getAllStudent(className);
				students.forEach(student -> studentsString.add(student.toString())); 
			}
			return studentsString;
		}
		
		
//		//creating a get mapping that retrieves the detail of a specific student  
//		@GetMapping("/fetchStudents/{id}")  
//		private Student getStudent(@PathVariable("id") int id){  
//			return studentService.getStudentById(id);  
//		} 
		
}
