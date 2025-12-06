package grades;

public class Assignment {

    /**
     * show-assignment
     *  list the assignments with their point values, grouped by category
     * @param ctx
     * @param args
     * @throws Exception
     */
    public static void cmdShowAssignments(ShellContext ctx, String args) throws Exception {
    }

    /**
     * add-assignment name Category Description points
     *  add a new assignment
     * @param ctx
     * @param args
     * @throws Exception
     */
    public static void cmdAddAssignment(ShellContext ctx, String args) throws Exception {
    }

    /**
     * grade assignmentname username grade
     *  assign the grade ‘grade’ for student with user name ‘username’ for assignment ‘assignmentname’. 
     *  If the student already has a grade for that assignment, replace it. If the number of points
     *  exceeds the number of points configured for the assignment, print a warning (showing the number
     *  of points configured).
     * @param ctx
     * @param args
     * @throws Exception
     */
    public static void cmdGrade(ShellContext ctx, String args) throws Exception {
    }
}
