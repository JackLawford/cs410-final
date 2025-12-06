package edu.boisestate.grades;

import java.util.Scanner;

public class Shell {
    private final ShellContext ctx;

    public Shell(ShellContext ctx) {
        this.ctx = ctx;
    }

    public void run() throws Exception {
        Scanner sc = new Scanner(System.in);
        while (true) {
            System.out.print("> ");
            if (!sc.hasNextLine()) break;
            String line = sc.nextLine().trim();
            if (line.isEmpty()) continue;

            String[] parts = line.split("\\s+", 2);
            String cmd = parts[0];
            String args = parts.length > 1 ? parts[1] : "";

            if (cmd.equals("quit") || cmd.equals("exit")) {
                break;
            }

            dispatch(cmd, args);
        }
    }

    private void dispatch(String cmd, String args) throws Exception {
        switch (cmd) {
            case "new-class"      -> Class.cmdNewClass(ctx, args);
            case "list-classes"   -> Class.cmdListClasses(ctx, args);
            case "select-class"   -> Class.cmdSelectClass(ctx, args);
            case "show-class"     -> Class.cmdShowClass(ctx, args);

            case "show-categories"-> Category.cmdShowCategories(ctx, args);
            case "add-category"   -> Category.cmdAddCategory(ctx, args);

            case "show-assignment"-> Assignment.cmdShowAssignments(ctx, args);
            case "add-assignment" -> Assignment.cmdAddAssignment(ctx, args);
            case "grade"          -> Assignment.cmdGrade(ctx, args);

            case "add-student"    -> Student.cmdAddStudent(ctx, args);
            case "show-students"  -> Student.cmdShowStudents(ctx, args);
            case "student-grades" -> Student.cmdStudentGrades(ctx, args);
            case "gradebook"      -> Student.cmdGradebook(ctx, args);

            default -> System.out.println("Unknown command: " + cmd);
        }
    }
}
