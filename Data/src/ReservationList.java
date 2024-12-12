import java.util.ArrayList;
import java.util.HashMap;
public class ReservationList {
    private HashMap<String, ArrayList<String>> reservations; 
    private HashMap<String, ArrayList<String>> enrolledCourses; 
    public ReservationList() {
        this.reservations = new HashMap<String, ArrayList<String>>();  
        this.enrolledCourses = new HashMap<String, ArrayList<String>>();  
    }
    public boolean enrollCourse(String studentId, String courseId, ArrayList<String> prerequisites) throws Exception {
        if (!arePrerequisitesMet(studentId, prerequisites)) {
            throw new Exception("선수과목을 이수하지 않았습니다. 필요한 선수과목: " + prerequisites);
        }
        if (isCourseAlreadyTaken(studentId, courseId)) {
            throw new Exception("이미 수강한 강좌입니다. 강좌 ID: " + courseId);
        }
        addCourseToStudent(studentId, courseId);
        System.out.println("수강 신청이 완료되었습니다.");
        return true;
    }
    private boolean arePrerequisitesMet(String studentId, ArrayList<String> prerequisites) {
        ArrayList<String> studentCourses = getOrCreateStudentCourses(reservations, studentId);
        for (String prerequisite : prerequisites) {
            if (!studentCourses.contains(prerequisite)) {
                return false;
            }
        }
        return true;
    }
    private boolean isCourseAlreadyTaken(String studentId, String courseId) {
        ArrayList<String> studentCourses = getOrCreateStudentCourses(reservations, studentId);
        return studentCourses.contains(courseId);
    }
    public ArrayList<String> getEnrolledCourses(String studentId) {
        ArrayList<String> enrolledList = getOrCreateStudentCourses(enrolledCourses, studentId);
        if (enrolledList.isEmpty()) {
            System.out.println("해당 학생이 수강 신청한 강좌가 없습니다.");
        }
        return enrolledList;
    }
    private ArrayList<String> getOrCreateStudentCourses(HashMap<String, ArrayList<String>> courseMap, String studentId) {
        ArrayList<String> studentCourses = courseMap.get(studentId);
        if (studentCourses == null) {
            studentCourses = new ArrayList<String>();
            courseMap.put(studentId, studentCourses);
        }
        return studentCourses;
    }
    private void addCourseToStudent(String studentId, String courseId) {
        ArrayList<String> studentCourses = getOrCreateStudentCourses(reservations, studentId);
        studentCourses.add(courseId);
        ArrayList<String> enrolledList = getOrCreateStudentCourses(enrolledCourses, studentId);
        enrolledList.add(courseId);
    }
}