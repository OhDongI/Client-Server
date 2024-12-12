import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.HashMap;
public class Data extends UnicastRemoteObject implements DataIF {
    private static final long serialVersionUID = 1L;
    private HashMap<String, String> studentCredentials = new HashMap<>();
    private HashMap<String, ArrayList<String>> enrolledCourses = new HashMap<>();
    protected static StudentList studentList;
    protected static CourseList courseList;
    protected Data() throws RemoteException {
        super();
        initializeCredentials();
    }
    private void initializeCredentials() {
        studentCredentials.put("student1", "1111");
        studentCredentials.put("student2", "2222");
    }
    public static void main(String[] args) throws FileNotFoundException, IOException, NullDataException {
        try {
            Data data = new Data();
            Naming.rebind("Data", data);
            System.out.println("Data is ready !!!");
            studentList = new StudentList("Students.txt");
            courseList = new CourseList("Courses.txt");
            logInitializationStatus();
        } catch (RemoteException e) {
            e.printStackTrace();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
    }
    private static void logInitializationStatus() throws NullDataException {
        System.out.println("StudentList and CourseList initialized successfully.");
        System.out.println("Number of students: " + studentList.getAllStudentRecords().size());
        System.out.println("Number of courses: " + courseList.getAllCourses().size());
    }
    @Override
    public boolean authenticate(String id, String password) throws RemoteException {
        String storedPassword = studentCredentials.get(id);
        return storedPassword != null && storedPassword.equals(password);
    }
    @Override
    public boolean addUser(String userId, String password) throws RemoteException {
        if (studentCredentials.containsKey(userId)) {
            System.out.println("User already exists: " + userId);
            return false;
        }
        studentCredentials.put(userId, password);
        System.out.println("User added successfully: " + userId);
        return true;
    }
    @Override
    public String getUserInfo(String userId) throws RemoteException {
        if (!studentCredentials.containsKey(userId)) {
            System.out.println("User not found: " + userId);
            return null;
        }
        return "User ID: " + userId + ", Password: " + studentCredentials.get(userId);
    }
    @Override
    public boolean updateUserPassword(String userId, String newPassword) throws RemoteException {
        if (!studentCredentials.containsKey(userId)) {
            System.out.println("User not found: " + userId);
            return false;
        }
        studentCredentials.put(userId, newPassword);
        System.out.println("Password updated successfully for user: " + userId);
        return true;
    }
    @Override
    public boolean deleteUser(String userId) throws RemoteException {
        if (!studentCredentials.containsKey(userId)) {
            System.out.println("User not found: " + userId);
            return false;
        }
        studentCredentials.remove(userId);
        System.out.println("User deleted successfully: " + userId);
        return true;
    }
    @Override
    public ArrayList<Student> getAllStudentData() throws RemoteException, NullDataException {
        System.out.println("getAllStudentData method called.");
        return studentList.getAllStudentRecords();
    }
    @Override
    public ArrayList<Course> getAllCourseData() throws RemoteException {
        System.out.println("getAllCourseData method called.");
        return courseList.getAllCourses();
    }
    @Override
    public boolean addStudent(String studentInfo) throws RemoteException {
        System.out.println("addStudent method called with info: " + studentInfo);
        if (studentList != null) {
            boolean result = studentList.addStudentRecords(studentInfo);
            System.out.println("Student added: " + result);
            return result;
        } else {
            System.out.println("StudentList is not initialized.");
            return false;
        }
    }
    @Override
    public boolean deleteStudent(String studentId) throws RemoteException {
        System.out.println("deleteStudent method called with ID: " + studentId);
        if (studentList != null) {
            boolean result = studentList.deleteStudentRecords(studentId);
            System.out.println("Student deleted: " + result);
            return result;
        } else {
            System.out.println("StudentList is not initialized.");
            return false;
        }
    }
    @Override
    public boolean addCourse(String courseInfo) throws RemoteException {
        System.out.println("addCourse method called with info: " + courseInfo);
        if (courseList != null) {
            boolean result = courseList.addCourseRecords(courseInfo);
            System.out.println("Course added: " + result);
            return result;
        } else {
            System.out.println("CourseList is not initialized.");
            return false;
        }
    }
    @Override
    public boolean deleteCourse(String courseId) throws RemoteException {
        System.out.println("deleteCourse method called with ID: " + courseId);
        if (courseList != null) {
            boolean result = courseList.deleteCourseRecords(courseId);
            System.out.println("Course deleted: " + result);
            return result;
        } else {
            System.out.println("CourseList is not initialized.");
            return false;
        }
    }
    @Override
    public boolean enrollCourse(String studentId, String courseId, ArrayList<String> prerequisites) throws RemoteException {
        ArrayList<String> studentCourses = studentList.getStudentCourses(studentId);
        if (studentCourses == null) {
            System.out.println("학생 ID가 유효하지 않습니다: " + studentId);
            return false;
        }
        ArrayList<String> coursePrerequisites = courseList.getCoursePrerequisites(courseId);
        if (coursePrerequisites == null) {
            System.out.println("강좌 ID가 유효하지 않습니다: " + courseId);
            return false;
        }
        for (String prerequisite : prerequisites) {
            if (!studentCourses.contains(prerequisite)) {
                System.out.println("선수과목을 충족하지 못했습니다: " + prerequisite);
                return false;
            }
        }
        ArrayList<String> enrolledList = enrolledCourses.get(studentId);
        if (enrolledList == null) {
            enrolledList = new ArrayList<>();
            enrolledCourses.put(studentId, enrolledList);
        }
        if (enrolledList.contains(courseId)) {
            System.out.println("이미 수강한 강좌입니다.");
            return false;
        }
        enrolledList.add(courseId);
        System.out.println("수강신청이 완료되었습니다.");
        return true;
    }
    @Override
    public ArrayList<String> getEnrolledCourses(String studentId) throws RemoteException {
        ArrayList<String> enrolledList = enrolledCourses.get(studentId);
        if (enrolledList == null) {
            System.out.println("학생 ID가 유효하지 않습니다: " + studentId);
            return new ArrayList<String>();
        }
        return enrolledList;
    }
}