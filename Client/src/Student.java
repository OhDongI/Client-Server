import java.io.Serializable;
import java.util.ArrayList;
import java.util.StringTokenizer;
public class Student implements Serializable {
    private static final long serialVersionUID = 1L;
    protected String studentId;
    protected String name;
    protected String department;
    protected ArrayList<String> completedCoursesList;
    public Student(String inputString) {
        StringTokenizer stringTokenizer = new StringTokenizer(inputString);
        this.studentId = stringTokenizer.nextToken();
        this.name = stringTokenizer.nextToken() + " " + stringTokenizer.nextToken();  // 성과 이름을 함께 저장
        this.department = stringTokenizer.nextToken();
        this.completedCoursesList = new ArrayList<String>();
        while (stringTokenizer.hasMoreTokens()) {
            this.completedCoursesList.add(stringTokenizer.nextToken());
        }
    }
    public boolean match(String studentId) {
        return this.studentId.equals(studentId);
    }
    public String getName() {
        return this.name;
    }
    public String getDepartment() {
        return this.department;  
    }
    public ArrayList<String> getCompletedCourses() {
        return this.completedCoursesList;
    }
    public String toString() {
        StringBuilder stringReturn = new StringBuilder();
        stringReturn.append(String.format("%-10s %-15s %-15s ", this.studentId, this.name, this.department));
        for (int i = 0; i < this.completedCoursesList.size(); i++) {
            stringReturn.append(this.completedCoursesList.get(i));
            if (i < this.completedCoursesList.size() - 1) {
                stringReturn.append(", ");  
            }
        }
        return stringReturn.toString();
    }
}