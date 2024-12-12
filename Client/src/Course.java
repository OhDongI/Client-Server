import java.io.Serializable;
import java.util.ArrayList;
import java.util.StringTokenizer;
public class Course implements Serializable {
    private static final long serialVersionUID = 1L;
    private String courseId;          
    private String instructor;         
    private String courseName;         
    private ArrayList<String> prerequisites;  
    public Course(String inputString) {
        StringTokenizer stringTokenizer = new StringTokenizer(inputString);
        this.courseId = stringTokenizer.nextToken();  
        this.instructor = stringTokenizer.nextToken();  
        this.courseName = stringTokenizer.nextToken();  
        this.prerequisites = new ArrayList<String>();
        while (stringTokenizer.hasMoreTokens()) {
            this.prerequisites.add(stringTokenizer.nextToken());
        }
    }
    public String getCourseId() {
        return courseId;
    }
    public String getInstructor() {
        return instructor;
    }
    public String getCourseName() {
        return courseName;
    }
    public ArrayList<String> getPrerequisites() {
        return prerequisites;
    }   
    @Override
    public String toString() {
        StringBuilder result = new StringBuilder();
        result.append(String.format("%-10s %-10s %-30s ", courseId, instructor, courseName));
        if (!prerequisites.isEmpty()) {
            result.append("Prerequisites: ");
            result.append(String.join(", ", prerequisites));
        } else {
            result.append("No prerequisites");
        }
        return result.toString();
    }
}