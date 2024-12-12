import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
public class CourseList {
    private ArrayList<Course> courseList;
    public CourseList(String courseFileName) throws FileNotFoundException, IOException {
        courseList = new ArrayList<Course>();
        try (BufferedReader courseFile = new BufferedReader(new FileReader(courseFileName))) {
            String courseInfo;
            while ((courseInfo = courseFile.readLine()) != null) {
                if (!courseInfo.isEmpty()) {
                    courseList.add(new Course(courseInfo));
                }
            }
        }
    }
    public ArrayList<Course> getAllCourses() {
        return courseList;
    }
    public boolean addCourseRecords(String courseInfo) {
        Course course = new Course(courseInfo);
        boolean result = courseList.add(course);
        System.out.printf("Adding course: %s - Course added: %b - Current course count: %d%n", courseInfo, result, courseList.size());
        return result;
    }
    public boolean deleteCourseRecords(String courseId) {
        for (Course course : courseList) {
            if (course.getCourseId().equals(courseId)) {
                boolean result = courseList.remove(course);
                System.out.printf("Course deleted: %b - Current course count: %d%n", result, courseList.size());
                return result;
            }
        }
        return false;
    }
    public Course findCourseById(String courseId) {
        for (Course course : courseList) {
            if (course.getCourseId().equals(courseId)) {
                return course;
            }
        }
        return null;
    }
    public ArrayList<String> getCoursePrerequisites(String courseId) {
        Course course = findCourseById(courseId);
        if (course != null) {
            return course.getPrerequisites();
        }
        System.out.printf("Course ID not found: %s%n", courseId);
        return new ArrayList<String>();
    }
}