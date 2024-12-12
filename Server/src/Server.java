import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
public class Server extends UnicastRemoteObject implements ServerIF {
    private static final long serialVersionUID = 1L;
    private static DataIF data;
    protected Server() throws RemoteException {
        super();
    }
    public static void main(String[] args) throws NotBoundException {
        try {
            Server server = new Server();
            Naming.rebind("Server", server);
            System.out.println("Server is ready !!!");
            initializeData();
        } catch (RemoteException | MalformedURLException e) {
            handleException(e); 
        }
    }
    private static void initializeData() {
        try {
            data = (DataIF) Naming.lookup("Data");
            if (data == null) {
                System.out.println("Data lookup failed, data is null");
            } else {
                System.out.println("Data lookup succeeded");
            }
        } catch (Exception e) {
            handleException(e); 
        }
    }
    private static void handleException(Exception e) {
        if (e instanceof RemoteException) {
            System.out.println("Remote exception occurred: " + e.getMessage());
        } else if (e instanceof MalformedURLException) {
            System.out.println("Malformed URL: Please check the URL used for the RMI lookup.");
        } else {
            System.out.println("An unexpected error occurred: " + e.getMessage());
        }
        e.printStackTrace();
    }
    @Override
    public ArrayList<Student> getAllStudentData() throws RemoteException, NullDataException {
        return data.getAllStudentData();
    }
    @Override
    public ArrayList<Course> getAllCourses() throws RemoteException {
        return data.getAllCourseData();
    }
    @Override
    public boolean addStudent(String studentInfo) throws RemoteException {
        return data.addStudent(studentInfo);
    }
    @Override
    public boolean deleteStudent(String studentId) throws RemoteException {
        return data.deleteStudent(studentId);
    }
    @Override
    public boolean addCourse(String courseInfo) throws RemoteException {
        return data.addCourse(courseInfo);
    }
    @Override
    public boolean deleteCourse(String courseId) throws RemoteException {
        return data.deleteCourse(courseId);
    }
    @Override
    public boolean authenticate(String loginId, String password) throws RemoteException {
        return data.authenticate(loginId, password);
    }
    @Override
    public boolean enrollCourse(String studentId, String courseId, ArrayList<String> prerequisites) throws RemoteException, Exception {
        return data.enrollCourse(studentId, courseId, prerequisites);
    }
    @Override
    public ArrayList<String> getEnrolledCourses(String studentId) throws RemoteException {
        return data.getEnrolledCourses(studentId);
    }
    @Override
    public boolean addUser(String userId, String password) throws RemoteException, UserAlreadyExistsException {
        return data.addUser(userId, password);
    }
    @Override
    public String getUserInfo(String userId) throws RemoteException {
        return data.getUserInfo(userId);
    }
    @Override
    public boolean updateUserPassword(String userId, String newPassword) throws RemoteException {
        return data.updateUserPassword(userId, newPassword);
    }
    @Override
    public boolean deleteUser(String userId) throws RemoteException {
        return data.deleteUser(userId);
    }
}