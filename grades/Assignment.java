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
    }
}
