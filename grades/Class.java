package grades;

public class Class {
 
    /**
     * new-class Course Term Section 
     *  creates a new class with the given course number, term, section number, and description.
     * @param ctx
     * @param args
     * @throws Exception
     */
    public static void cmdNewClass(ShellContext ctx, String args) throws Exception {
        String[] parts = args.split("\\s+", 4);
        if (parts.length < 4) {
            System.out.println("Usage: new-class COURSE TERM SECTION \"Description\"");
            return;
        }

        String courseNumber = parts[0];
        String term = parts[1];

        int sectionNumber;
        try {
            sectionNumber = Integer.parseInt(parts[2]);
        } catch (NumberFormatException e) {
            System.out.println("Invalid section number: " + parts[2]);
            return;
        }

        String description = parts[3].trim();
        if (description.startsWith("\"") && description.endsWith("\"") && description.length() >= 2) {
            description = description.substring(1, description.length() - 1);
        }

        String sql = "INSERT INTO class (course_number, term, section_number, description) " + "VALUES (?, ?, ?, ?)";

        Connection conn = ctx.getConnection();
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, courseNumber);
            ps.setString(2, term);
            ps.setInt(3, sectionNumber);
            ps.setString(4, description);
            ps.executeUpdate();
        }

        System.out.println("Created class " + courseNumber + " " + term + " " + sectionNumber + " - " + description);
    }

    /**
     * list-classes
     *  List classes, with the # of students in each
     * @param ctx
     * @param args
     * @throws Exception
     */
    public static void cmdListClasses(ShellContext ctx, String args) throws Exception {
        String sql =
            "SELECT c.class_id, " +
            "       c.course_number, " +
            "       c.term, " +
            "       c.section_number, " +
            "       c.description, " +
            "       COUNT(cs.student_id) AS num_students " +
            "FROM class c " +
            "LEFT JOIN class_student cs ON c.class_id = cs.class_id " +
            "GROUP BY c.class_id, c.course_number, c.term, c.section_number, c.description " +
            "ORDER BY c.course_number, c.term, c.section_number";

        Connection conn = ctx.getConnection();
        try (PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            boolean any = false;
            while (rs.next()) {
                any = true;
                int id = rs.getInt("class_id");
                String course = rs.getString("course_number");
                String term = rs.getString("term");
                int section = rs.getInt("section_number");
                String desc = rs.getString("description");
                int numStudents = rs.getInt("num_students");

                System.out.printf("%d: %s %s %d (%d students) - %s%n", id, course, term, section, numStudents, desc);
            }

            if (!any) {
                System.out.println("No classes found.");
            }
        }
    }

    /**
     * select-class Course
     *  selects the only section of "Course" in the most recent term, if there is only one such section; if there are multiple sections it fails.
     * 
     * select-class Course Term
     *  selects the only section of "Course" in "Term"; if there are multiple such sections, it fails.
     * 
     * select-class Course Term Section
     *  selects a specific section
     * @param ctx
     * @param args
     * @throws Exception
     */
    public static void cmdSelectClass(ShellContext ctx, String args) throws Exception {
        String[] parts = args.split("\\s+");
        if (parts.length < 1 || parts[0].isEmpty()) {
            System.out.println("Usage: select-class COURSE [TERM [SECTION]]");
            return;
        }

        String courseNumber = parts[0];
        Connection conn = ctx.getConnection();

        if (parts.length == 1) {
            // select-class CS410
            selectByCourseOnly(ctx, conn, courseNumber);
        } else if (parts.length == 2) {
            // select-class CS410 Sp20
            String term = parts[1];
            selectByCourseAndTerm(ctx, conn, courseNumber, term);
        } else {
            // select-class CS410 Sp20 1
            String term = parts[1];
            int sectionNumber;
            try {
                sectionNumber = Integer.parseInt(parts[2]);
            } catch (NumberFormatException e) {
                System.out.println("Invalid section number: " + parts[2]);
                return;
            }
            selectByCourseTermSection(ctx, conn, courseNumber, term, sectionNumber);
        }
    }

    /**
     * selectByCourseTermSection
     * 
     * specifically for when only the course name is provided, no term or section
     * @param ctx
     * @param conn
     * @param courseNumber
     * @throws SQLException
     */
    private static void selectByCourseOnly(ShellContext ctx, Connection conn, String courseNumber)
            throws SQLException {

        String sql =
            "SELECT class_id, course_number, term, section_number, description " +
            "FROM class " +
            "WHERE course_number = ? " +
            "ORDER BY term DESC, section_number ASC";

        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, courseNumber);
            try (ResultSet rs = ps.executeQuery()) {

                if (!rs.next()) {
                    System.out.println("No classes found for course " + courseNumber);
                    return;
                }

                int id = rs.getInt("class_id");
                String mostRecentTerm = rs.getString("term");
                int sectionNumber = rs.getInt("section_number");
                String desc = rs.getString("description");

                int countInMostRecentTerm = 1;
                while (rs.next()) {
                    String t = rs.getString("term");
                    if (!t.equals(mostRecentTerm)) {
                        break;
                    }
                    countInMostRecentTerm++;
                }

                if (countInMostRecentTerm > 1) {
                    System.out.println("Multiple sections of " + courseNumber + " in the most recent term (" + mostRecentTerm + "); select-class is ambiguous.");
                    return;
                }

                ctx.setActiveClass(id, courseNumber, mostRecentTerm, sectionNumber, desc);
                System.out.println("Active class set to " + courseNumber + " " + mostRecentTerm + " " + sectionNumber);
            }
        }
    }

    /**
     * selectByCourseAndTerm
     * 
     * specifically for when course and term are provided, no section
     * @param ctx
     * @param conn
     * @param courseNumber
     * @param term
     * @throws SQLException
     */
    private static void selectByCourseAndTerm(ShellContext ctx, Connection conn, String courseNumber, String term) throws SQLException {

        String sql =
            "SELECT class_id, course_number, term, section_number, description " +
            "FROM class " +
            "WHERE course_number = ? AND term = ? " +
            "ORDER BY section_number";

        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, courseNumber);
            ps.setString(2, term);
            try (ResultSet rs = ps.executeQuery()) {

                if (!rs.next()) {
                    System.out.println("No classes found for " + courseNumber + " in term " + term);
                    return;
                }

                int id = rs.getInt("class_id");
                int sectionNumber = rs.getInt("section_number");
                String desc = rs.getString("description");

                if (rs.next()) {
                    System.out.println("Multiple sections of " + courseNumber + " in term " + term + "; select-class is ambiguous.");
                    return;
                }

                ctx.setActiveClass(id, courseNumber, term, sectionNumber, desc);
                System.out.println("Active class set to " + courseNumber + " " + term + " " + sectionNumber);
            }
        }
    }

    /**
     * selectByCourseTermSection
     * 
     * specifically for when course, term, and section are all provided
     * @param ctx
     * @param conn
     * @param courseNumber
     * @param term
     * @param sectionNumber
     * @throws SQLException
     */
    private static void selectByCourseTermSection(ShellContext ctx, Connection conn, String courseNumber, String term, int sectionNumber) 
        throws SQLException {

        String sql =
            "SELECT class_id, course_number, term, section_number, description " +
            "FROM class " +
            "WHERE course_number = ? AND term = ? AND section_number = ?";

        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, courseNumber);
            ps.setString(2, term);
            ps.setInt(3, sectionNumber);
            try (ResultSet rs = ps.executeQuery()) {

                if (!rs.next()) {
                    System.out.println("No class found for " + courseNumber + " " + term + " section " + sectionNumber);
                    return;
                }

                int id = rs.getInt("class_id");
                String desc = rs.getString("description");

                ctx.setActiveClass(id, courseNumber, term, sectionNumber, desc);
                System.out.println("Active class set to " + courseNumber + " " + term + " " + sectionNumber);
            }
        }
    }

    /**
     * show-class
     *  shows the currently-active class
     * @param ctx
     * @param args
     * @throws Exception
     */
    public static void cmdShowClass(ShellContext ctx, String args) throws Exception {
        if (!ctx.hasActiveClass()) {
            System.out.println("No active class selected. Use select-class first.");
            return;
        }

        System.out.println("Current class:");
        System.out.println("  " + ctx.getActiveCourseNumber() + " " + ctx.getActiveTerm() + " " + ctx.getActiveSectionNumber());
        System.out.println("  " + ctx.getActiveDescription());
    }
}
