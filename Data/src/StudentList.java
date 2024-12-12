import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
public class StudentList {
    private ArrayList<Student> students;
    public StudentList(String studentFileName) throws FileNotFoundException, IOException {
        this.students = new ArrayList<Student>();
        loadStudentsFromFile(studentFileName);
    }
    private void loadStudentsFromFile(String studentFileName) throws FileNotFoundException, IOException {
        BufferedReader studentFile = new BufferedReader(new FileReader(studentFileName));
        String studentInfo;
        while ((studentInfo = studentFile.readLine()) != null) {
            if (!studentInfo.isEmpty()) {
                this.students.add(new Student(studentInfo));
            }
        }
        studentFile.close();
    }
    public ArrayList<Student> getAllStudentRecords() throws NullDataException {
        if (students.isEmpty()) {
            throw new NullDataException("~~~~~~~~~~~~~ student data is null ~~~~~~~~~");
        }
        return students;
    }
    public boolean addStudentRecords(String studentInfo) {
        return students.add(new Student(studentInfo));
    }
    public boolean deleteStudentRecords(String studentId) {
        Student studentToDelete = findStudentById(studentId);
        if (studentToDelete != null) {
            return students.remove(studentToDelete);
        }
        return false;
    }
    public boolean isRegisteredStudent(String studentId) {
        return findStudentById(studentId) != null;
    }
    public ArrayList<String> getStudentCourses(String studentId) {
        Student student = findStudentById(studentId);
        if (student != null) {
            return student.getCompletedCourses();
        }
        System.out.println("해당 학생 ID를 찾을 수 없습니다: " + studentId);
        return new ArrayList<String>();
    }
    public ArrayList<String> getEnrolledCourses(String studentId) {
        Student student = findStudentById(studentId);
        if (student != null) {
            return student.getEnrolledCourses();
        }
        System.out.println("해당 학생 ID를 찾을 수 없습니다: " + studentId);
        return new ArrayList<String>();
    }
    private Student findStudentById(String studentId) {
        for (Student student : students) {
            if (student.match(studentId)) {
                return student;
            }
        }
        return null;
    }
}