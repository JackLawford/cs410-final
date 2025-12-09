package grades;

public class Category {
    
    /**
     * show-categories
     *  list the categories with their weights
     * @param ctx
     * @param args
     * @throws Exception
     */
    public static void cmdShowCategories(ShellContext ctx, String args) throws Exception {
        String sql =
            "SELECT c.category_name, " +
                    "       c.weight " +
            "FROM class_category c " +
            "WHERE c.class_id = " + ctx.getActiveClassID();

        Connection conn = ctx.getConnection();
        try (PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            boolean any = false;
            while (rs.next()) {
                any = true;
                String name = rs.getString("category_name");
                int weight = rs.getInt("weight");

                System.out.printf("%s: %d%%%n", name, weight);
            }

            if (!any) {
                System.out.println("No categories found.");
            }
        }
    }

    /**
     * add-category Name weight
     *
     * @param ctx
     * @param args
     * @throws Exception
     */
    public static void cmdAddCategory(ShellContext ctx, String args) throws Exception {
        String[] parts = args.split("\\s+", 2);
        if (parts.length != 2) {
            System.out.println("Usage: add-category NAME WEIGHT");
            return;
        }

        String name = parts[0];

        int weight;
        try {
            weight = Integer.parseInt(parts[1]);
        } catch (NumberFormatException e) {
            System.out.println("Invalid weight value: " + parts[1]);
            return;
        }

        //Insert category_name into category table if it doesn't already exist

        String sql = "INSERT IGNORE INTO category (name) " + "VALUES (?)";

        Connection conn = ctx.getConnection();
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, name);
            ps.executeUpdate();
        }

        sql = "INSERT INTO class_category (class_id, category_name, weight) " + "VALUES (?, ?, ?)";

        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, ctx.getActiveClassID());
            ps.setString(2, name);
            ps.setInt(3, weight);
            ps.executeUpdate();
        }

        System.out.println("Added category " + name + "with weight " + weight);
    }

}
