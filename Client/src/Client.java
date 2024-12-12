import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.util.ArrayList;
public class Client {
    private static String currentUserId;
    public static void main(String[] args) throws NotBoundException, IOException, NullDataException, UserAlreadyExistsException {
        ServerIF server;
        BufferedReader objReader = new BufferedReader(new InputStreamReader(System.in));
        try {
            server = (ServerIF) Naming.lookup("Server");
            boolean isAuthenticated = false;
            while (!isAuthenticated) {
                isAuthenticated = processLoginMenu(server, objReader);
                if (!isAuthenticated) {
                    LoggerUtil.logCSVFormat(currentUserId, "Exit");
                    return;
                }
            }    
            processMainMenu(server, objReader);
        } catch (RemoteException e) {
        	 handleRemoteException("connecting to the server", e); 
        }
    }
    private static boolean processLoginMenu(ServerIF server, BufferedReader objReader) throws IOException, RemoteException, UserAlreadyExistsException { 
        boolean isRunning = true;
        while (isRunning) {
            printLoginCRUDMenu();
        String choice = objReader.readLine().trim();
        if (choice == null) {
            System.out.println("Input was null. Exiting...");
            return false;
        }
        choice = choice.trim(); 
        switch (choice) {
            case "1":
                addUser(server, objReader);
                break;
            case "2":
                getUserInfo(server, objReader);
                break;
            case "3":
                updateUserPassword(server, objReader);
                break;
            case "4":
                deleteUser(server, objReader);
                break;
            case "5":
                return login(server, objReader);
            case "X":
            	LoggerUtil.logCSVFormat(currentUserId, "Exit");
            	isRunning = false;  
                break;
            default:
                System.out.println("Invalid choice. Please try again.");
        }
        }
        return false;
    }
    private static void processMainMenu(ServerIF server, BufferedReader objReader) throws IOException, NullDataException { 
        boolean isRunning = true;
        while (isRunning) { 
            printMenu();
            String choice = objReader.readLine().trim();
            switch (choice) {
                case "1":
                    listStudents(server);
                    break;
                case "2":
                    listCourses(server); 
                    break;
                case "3":
                    addStudent(server, objReader);
                    break;
                case "4":
                    deleteStudent(server, objReader);
                    break;
                case "5":
                    addCourse(server, objReader);
                    break;
                case "6":
                    deleteCourse(server, objReader);
                    break;
                case "7":
                    makeReservation(server, objReader);
                    break;
                case "8":
                    viewEnrolledCourses(server, objReader);
                    break;
                case "X":
                    isRunning = false;
                    LoggerUtil.logCSVFormat(currentUserId, "Exit");
                    break;
                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        }
}
    private static void addUser(ServerIF server, BufferedReader objReader) throws IOException, UserAlreadyExistsException {
        System.out.println("----- Add User -----");
        String userId = getUserInput(objReader, "User ID: "); 
        String password = getUserInput(objReader, "Password: "); 
        try {
            if (server.addUser(userId, password)) {
                System.out.println("User added successfully.");
            } else {
                System.out.println("Failed to add user.");
            }
        } catch (RemoteException e) {
            handleRemoteException("adding a user", e);
        }
    }
    private static void getUserInfo(ServerIF server, BufferedReader objReader) throws IOException {
        System.out.println("----- Get User Information -----");
        String userId = getUserInput(objReader, "User ID: ");

        try {
            String userInfo = server.getUserInfo(userId);
            if (userInfo != null) {
                System.out.println("User Information: " + userInfo);
            } else {
                System.out.println("User not found.");
            }
        } catch (RemoteException e) {
            handleRemoteException("getting user information", e); 
        }
    }
    private static void updateUserPassword(ServerIF server, BufferedReader objReader) throws IOException {
        System.out.println("----- Update User Password -----");
        String userId = getUserInput(objReader, "User ID: ");
        String newPassword = getUserInput(objReader, "New Password: ");
        try {
            if (server.updateUserPassword(userId, newPassword)) {
                System.out.println("Password updated successfully.");
            } else {
                System.out.println("Failed to update password.");
            }
        } catch (RemoteException e) {
            handleRemoteException("listing courses", e); 
        }
    }
    private static void deleteUser(ServerIF server, BufferedReader objReader) throws IOException {
        System.out.println("----- Delete User -----");
        String userId = getUserInput(objReader, "User ID: "); 
        try {
            if (server.deleteUser(userId)) {
                System.out.println("User deleted successfully.");
            } else {
                System.out.println("Failed to delete user.");
            }
        } catch (RemoteException e) {
            handleRemoteException("deleting a user", e);
        }
    }
    private static void viewEnrolledCourses(ServerIF server, BufferedReader objReader) throws IOException {
        String studentId = getUserInput(objReader, "Enter Student ID to view enrolled courses: "); 
        try {
            ArrayList<String> enrolledCourses = server.getEnrolledCourses(studentId);
            if (enrolledCourses.isEmpty()) {
                System.out.println("No enrolled courses found for this student.");
            } else {
                System.out.println("Enrolled courses:");
                for (String course : enrolledCourses) {
                    System.out.println("- " + course);
                }
            }
        } catch (RemoteException e) {
            handleRemoteException("viewing enrolled courses", e);
        }
    }
    private static void makeReservation(ServerIF server, BufferedReader objReader) throws IOException {
        System.out.println("----- Make Reservation -----");
        String studentId = getUserInput(objReader, "Student ID: ");
        String courseId = getUserInput(objReader, "Course ID: "); 
        String prerequisitesInput = getUserInput(objReader, "Prerequisite Course IDs (space-separated): "); 
        String[] prerequisitesArray = prerequisitesInput.split(" ");
        ArrayList<String> prerequisites = new ArrayList<>();
        for (String prerequisite : prerequisitesArray) {
            if (!prerequisite.isEmpty()) {
                prerequisites.add(prerequisite);
            }
        }
        try {
            boolean result = server.enrollCourse(studentId, courseId, prerequisites);
            if (result) {
                System.out.println("Reservation successful.");
            } else {
                System.out.println("Reservation failed.");
            }
        } catch (Exception e) {
        	handleRemoteException("making a reservation", new RemoteException(e.getMessage()));
        }
    }
    private static boolean login(ServerIF server, BufferedReader objReader) throws IOException, RemoteException {
        System.out.println("----- Login -----");
        currentUserId = getUserInput(objReader, "Login ID: "); 
        String password = getUserInput(objReader, "Password: "); 
        boolean isAuthenticated = server.authenticate(currentUserId, password);
        if (isAuthenticated) {
            System.out.println("Login successful!");
            LoggerUtil.logCSVFormat(currentUserId, "Login");
            return true;
        } else {
            System.out.println("Login failed. Please try again.");
            return false;
        }
    }
    private static void handleRemoteException(String actionDescription, RemoteException e) {
    	System.out.println("Error occurred during " + actionDescription + ": " + e.getMessage());
        e.printStackTrace();
    }
    private static String getUserInput(BufferedReader objReader, String prompt) throws IOException {
        System.out.print(prompt);
        return objReader.readLine().trim();
    }
    private static void listStudents(ServerIF server) throws RemoteException, NullDataException {
        ArrayList<Student> students = server.getAllStudentData();
        showStudent();
        for (Student student : students) {
            printStudentInfo(student);
        }
        LoggerUtil.logCSVFormat(currentUserId, "List Students");
    }
    private static void listCourses(ServerIF server) throws RemoteException {
        ArrayList<Course> courses = server.getAllCourses();
        showCourse();
        for (Course course : courses) {
            System.out.println(course.toString());
        }
        LoggerUtil.logCSVFormat(currentUserId, "List Courses");
    }
    private static void addCourse(ServerIF server, BufferedReader objReader) throws IOException, RemoteException {
        System.out.println("-----Course Information-----");
        String courseId = getUserInput(objReader, "Course ID: "); 
        String instructor = getUserInput(objReader, "Instructor: "); 
        String courseName = getUserInput(objReader, "Course Name: ");
        String prerequisites = getUserInput(objReader, "Prerequisites: "); 
        if (server.addCourse(courseId + " " + instructor + " " + courseName + " " + prerequisites)) {
            System.out.println("SUCCESS");
            LoggerUtil.logCSVFormat(currentUserId, "Add Course");
        } else {
            System.out.println("FAIL");
        }
    }
    private static void deleteCourse(ServerIF server, BufferedReader objReader) throws RemoteException, IOException {
        String courseId = getUserInput(objReader, "Course ID: ");
        if (server.deleteCourse(courseId)) {
            System.out.println("SUCCESS");
            LoggerUtil.logCSVFormat(currentUserId, "Delete Course");
        } else {
            System.out.println("FAIL");
        }
    }
    private static void addStudent(ServerIF server, BufferedReader objReader) throws IOException, RemoteException {
        System.out.println("-----Student Information-----");
        String studentId = getUserInput(objReader, "Student ID: "); 
        String studentName = getUserInput(objReader, "Student Name: "); 
        String studentDept = getUserInput(objReader, "Student Department: "); 
        String completedCourses = getUserInput(objReader, "Student Completed Course List: ");
        if (server.addStudent(studentId + " " + studentName + " " + studentDept + " " + completedCourses)) {
            System.out.println("SUCCESS");
            LoggerUtil.logCSVFormat(currentUserId, "Add Student");
        } else {
            System.out.println("FAIL");
        }
    }
    private static void deleteStudent(ServerIF server, BufferedReader objReader) throws RemoteException, IOException {
        String studentId = getUserInput(objReader, "Student ID: "); 
        if (server.deleteStudent(studentId)) {
            System.out.println("SUCCESS");
            LoggerUtil.logCSVFormat(currentUserId, "Delete Student");
        } else {
            System.out.println("FAIL");
        }
    }
    private static void printStudentInfo(Student student) {
        System.out.printf("%-10s %-15s %-15s %-30s\n", student.match(student.studentId) ? student.studentId : "", student.getName(), student.getDepartment(), 
        		String.join(", ", student.getCompletedCourses()));
    }
    private static void showCourse() {
        System.out.printf("%-10s %-10s %-30s %s\n", "Course ID", "Instructor", "Course Name", "prerequisites");
        System.out.println("-------------------------------------------------------------");
    }
    private static void showStudent() {
        System.out.println("Server's answer:");
        System.out.printf("%-10s %-15s %-15s %-30s\n", "ID", "Name", "Department", "Courses"); 
        System.out.println("---------------------------------------------------------------");
    }
    private static void printLoginCRUDMenu() {
        System.out.println("************************ LOGIN MENU *********************");
        System.out.println("1. Add User");
        System.out.println("2. Get User Information");
        System.out.println("3. Update User Password");
        System.out.println("4. Delete User");
        System.out.println("5. Login");
        System.out.println("X. Exit");
    }
    private static void printMenu() {
        System.out.println("************************ MENU *********************");
        System.out.println("1. List Students");
        System.out.println("2. List Courses");
        System.out.println("3. Add Student");
        System.out.println("4. Delete Student");
        System.out.println("5. Add Course");
        System.out.println("6. Delete Course");
        System.out.println("7. Make Reservation");
        System.out.println("8. View Enrolled Courses");
        System.out.println("X. Exit");
    }
}


//""많으면 에러같음
//최대한 짧게