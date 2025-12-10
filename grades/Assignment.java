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
        String sql =
                "SELECT a.name, " +
                "       a.category_name " +
                "       a.point_value " +
                "FROM assignment a" +
                "WHERE a.class_id = " + ctx.getActiveClassID() +
                "ORDER BY a.category_name";

        Connection conn = ctx.getConnection();
        try (PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            boolean any = false;
            while (rs.next()) {
                any = true;
                String name = rs.getString("name");
                String category = rs.getString("category_name");
                int point_value = rs.getInt("point_value");

                System.out.printf("%s: %s, %d points %n", name, category, point_value);
            }

            if (!any) {
                System.out.println("No assignments found.");
            }
        }
    }

    /**
     * add-assignment name Category Description points
     *  add a new assignment
     * @param ctx
     * @param args
     * @throws Exception
     */
    public static void cmdAddAssignment(ShellContext ctx, String args) throws Exception {
        String[] parts = args.split("\\s+", 4);
        if (parts.length != 4) {
            System.out.println("Usage: add-assignment NAME CATEGORY DESCRIPTION POINTS");
            return;
        }

        String name = parts[0];
        String category = parts[1];
        String description = parts[2];

        int points;
        try {
            points = Integer.parseInt(parts[3]);
        } catch (NumberFormatException e) {
            System.out.println("Invalid point value: " + parts[3]);
            return;
        }

        sql = "INSERT INTO assignment (class_id, category_name, name, point_value, description) " + "VALUES (?, ?, ?, ?, ?)";

        Connection conn = ctx.getConnection();
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, ctx.getActiveClassID());
            ps.setString(2, category);
            ps.setString(3, name);
            ps.setInt(4, points);
            ps.setString(5, description);
            ps.executeUpdate();
        }

        System.out.println("Added assignment " + name + " " +  category + " " + points + " " + description);
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
        String[] parts = args.split("\\s+", 3);
        if (parts.length != 3) {
            System.out.println("Usage: add-assignment ASSIGNMENTNAME USERNAME GRADE");
            return;
        }

        String assignmentName = parts[0];
        String username = parts[1];

        int grade;
        try {
            grade = Integer.parseInt(parts[2]);
        } catch (NumberFormatException e) {
            System.out.println("Invalid point value: " + parts[3]);
            return;
        }

        //Find student ID from student username
        String sql =
                "SELECT s.student_id " +
                "FROM student s" +
                "WHERE s.username = " + username;

        int studentID;

        Connection conn = ctx.getConnection();
        try (PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            boolean any = false;
            while (rs.next()) {
                any = true;
                studentID = rs.getInt("student_id");
            }

            if (!any) {
                System.out.println("No students with username: " + username + " found.");
            }
        }

        //Find assignment_id from assinment name
        sql =
            "SELECT a.assignment_id, " +
            "       a.point_value " +
            "FROM assignment a" +
            "WHERE a.name = " + assignmentName;

        int assignmentID;
        int pointValue;

        Connection conn = ctx.getConnection();
        try (PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            boolean any = false;
            while (rs.next()) {
                any = true;
                assignmentID = rs.getInt("assignment_id");
                pointValue = rs.getInt("point_value")
            }

            if (!any) {
                System.out.println("No assignments with name: " + assignmentName + " found.");
            }
        }

        sql = "INSERT INTO assignment_student (assignment_id, student_id, grade) " +
                "VALUES (?, ?, ?) ON DUPLICATE KEY UPDATE grade = " + grade;

        Connection conn = ctx.getConnection();
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, assignmentID);
            ps.setString(2, studentID);
            ps.setString(3, grade);
            ps.executeUpdate();
        }

        System.out.println("Added assignment " + name + " " +  category + " " + points + " " + description);

        //Let user know if the grade entered exceeds the points configured for the assignment
        if (grade > pointValue) {
            System.out.println("WARNING: The grade you gave this assignment is greater than the points" +
                    " configured for the assignment: " + grade + "/" + pointValue);
        }
    }
}
