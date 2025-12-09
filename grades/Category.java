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
            "FROM category c ";

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
    }

    /**
     * add-category Name weight
     * 
     * @param ctx
     * @param args
     * @throws Exception
     */
    public static void cmdAddCategory(ShellContext ctx, String args) throws Exception {
    }
}
