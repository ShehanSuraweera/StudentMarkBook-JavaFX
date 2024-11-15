package tm;

public class StudentTM {
    private int studentNo;
    private String studentName;
    private String StudentClass;
    private String teacherID;
    private int totalMarks;
    private double average;
    private int position;
    private int religion;
    private int firstLanguage;
    private int english;
    private int mathematics;
    private int science;
    private int history;
    private int commerce;
    private int art;
    private int IT;

    public StudentTM() {
    }

    public StudentTM(int studentNo, String studentName, String studentClass, String teacherID, int totalMarks, double average, int position, int religion, int firstLanguage, int english, int mathematics, int science, int history, int commerce, int art, int IT) {
        this.studentNo = studentNo;
        this.studentName = studentName;
        this.StudentClass = studentClass;
        this.teacherID = teacherID;
        this.totalMarks = totalMarks;
        this.average = average;
        this.position = position;
        this.religion = religion;
        this.firstLanguage = firstLanguage;
        this.english = english;
        this.mathematics = mathematics;
        this.science = science;
        this.history = history;
        this.commerce = commerce;
        this.art = art;
        this.IT = IT;
    }

    public int getStudentNo() {
        return studentNo;
    }

    public void setStudentNo(int studentNo) {
        this.studentNo = studentNo;
    }

    public String getStudentName() {
        return studentName;
    }

    public void setStudentName(String studentName) {
        this.studentName = studentName;
    }

    public String getStudentClass() {
        return StudentClass;
    }

    public void setStudentClass(String studentClass) {
        this.StudentClass = studentClass;
    }

    public String getTeacherID() {
        return teacherID;
    }

    public void setTeacherID(String teacherID) {
        this.teacherID = teacherID;
    }

    public int getTotalMarks() {
        return totalMarks;
    }

    public void setTotalMarks(int totalMarks) {
        this.totalMarks = totalMarks;
    }

    public double getAverage() {
        return average;
    }

    public void setAverage(double average) {
        this.average = average;
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    public int getReligion() {
        return religion;
    }

    public void setReligion(int religion) {
        this.religion = religion;
    }

    public int getFirstLanguage() {
        return firstLanguage;
    }

    public void setFirstLanguage(int firstLanguage) {
        this.firstLanguage = firstLanguage;
    }

    public int getEnglish() {
        return english;
    }

    public void setEnglish(int english) {
        this.english = english;
    }

    public int getMathematics() {
        return mathematics;
    }

    public void setMathematics(int mathematics) {
        this.mathematics = mathematics;
    }

    public int getScience() {
        return science;
    }

    public void setScience(int science) {
        this.science = science;
    }

    public int getHistory() {
        return history;
    }

    public void setHistory(int history) {
        this.history = history;
    }

    public int getCommerce() {
        return commerce;
    }

    public void setCommerce(int commerce) {
        this.commerce = commerce;
    }

    public int getArt() {
        return art;
    }

    public void setArt(int art) {
        this.art = art;
    }

    public int getIT() {
        return IT;
    }

    public void setIT(int IT) {
        this.IT = IT;
    }
}
