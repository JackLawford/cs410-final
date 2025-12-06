package edu.boisestate.grades;

import java.sql.Connection;

public class Main {
    public static void main(String[] args) throws Exception {
        try (Connection conn = Database.getConnection()) {
            ShellContext ctx = new ShellContext(conn);
            Shell shell = new Shell(ctx);
            shell.run();
        }
    }
}
