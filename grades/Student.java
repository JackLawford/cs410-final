package grades;

public class Student {

    /**
     * add-student username studentid Last First
     *  adds a student and enrolls them in the current class. If the student already
     *  exists, enroll them in the class; if the name provided does not match their stored
     *  name, update the name but print a warning that the name is being changed.
     * 
     * add-student username
     *  enrolls an already-existing student in the current class.
     *  If the specified student does not exist, report an error.
     * @param ctx
     * @param args
     * @throws Exception
     */
    public static void cmdAddStudent(ShellContext ctx, String args) throws Exception {

    }

    /**
     * show-students
     *  shows all students in the current class.
     * 
     * show-students string
     *  show all students with ‘string’ in their name or username (case-insensitive)
     * @param ctx
     * @param args
     * @throws Exception
     */
    public static void cmdShowStudents(ShellContext ctx, String args) throws Exception {
    }

    /**
     * student-grades username
     *  show student’s current grade: all assignments, visually
     *  grouped by category, with the student’s grade (if they have one). 
     *  Show subtotals for each category, along with the overall grade in the class.
     * @param ctx
     * @param args
     * @throws Exception
     */
    public static void cmdStudentGrades(ShellContext ctx, String args) throws Exception {
    }

    /**
     * gradebook
     *  show the current class’s gradebook: students (username, student ID, and
     *  name), along with their total grades in the class
     * @param ctx
     * @param args
     * @throws Exception
     */
    public static void cmdGradebook(ShellContext ctx, String args) throws Exception {
    }
}
