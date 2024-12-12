import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.ArrayList;
public interface DataIF extends Remote {
	 ArrayList<Student> getAllStudentData() throws RemoteException,NullDataException;
	 ArrayList<Course> getAllCourseData() throws RemoteException;
	 boolean authenticate(String loginId, String password) throws RemoteException;
	 boolean addStudent(String studentInfo) throws RemoteException;
	 boolean deleteStudent(String studentId) throws RemoteException;
	 boolean addCourse(String courseInfo)throws RemoteException;
	 boolean deleteCourse(String courseId) throws RemoteException; 
	 boolean enrollCourse(String studentId, String courseId, ArrayList<String> prerequisites) throws RemoteException, Exception;
	    ArrayList<String> getEnrolledCourses(String studentId) throws RemoteException;
	    boolean addUser(String userId, String password) throws RemoteException, UserAlreadyExistsException;
		String getUserInfo(String userId) throws RemoteException;
		boolean updateUserPassword(String userId, String newPassword) throws RemoteException;
		boolean deleteUser(String userId) throws RemoteException;
	}