import java.util.*;

public class Main {
    public static void main(String[] args) {
        StudentManager sm = new StudentManager();
        Scanner sc = new Scanner(System.in);

        while (true) {
            System.out.println("\n1. Add Student");
            System.out.println("2. View Result");
            System.out.println("3. Add Marks");
            System.out.println("4. Add Subject");
            System.out.println("5. Update Marks");
            System.out.println("6. Delete Student");
            System.out.println("7. Delete Subject");
            System.out.println("8. Search Student");
            System.out.println("9. Exit");
            System.out.print("Choice: ");
            int ch = sc.nextInt();

            switch (ch) {
                case 1 -> {
                    System.out.print("ID: ");
                    String id = sc.next();
                    System.out.print("Name: ");
                    String name = sc.next();
                    System.out.print("Class: ");
                    String cls = sc.next();
                    sm.addStudent(id, name, cls);
                }
                case 2 -> {
                    System.out.print("Student ID: ");
                    String sid = sc.next();
                    sm.viewResult(sid);
                }
                case 3 -> {
                    System.out.print("Student ID: ");
                    String sid = sc.next();
                    System.out.print("Subject ID: ");
                    String subid = sc.next();
                    System.out.print("Marks: ");
                    int marks = sc.nextInt();
                    sm.addMarks(sid, subid, marks);
                }
                case 4 -> {
                    System.out.print("Subject ID: ");
                    String subid = sc.next();
                    System.out.print("Subject Name: ");
                    String subname = sc.next();
                    sm.addSubject(subid, subname);
                }
                case 5 -> {
                    System.out.print("Student ID: ");
                    String sid = sc.next();
                    System.out.print("Subject ID: ");
                    String subid = sc.next();
                    System.out.print("New Marks: ");
                    int newMarks = sc.nextInt();
                    sm.updateMarks(sid, subid, newMarks);
                }
                case 6 -> {
                    System.out.print("Enter Student ID to delete: ");
                    String sid = sc.next();
                    sm.deleteStudent(sid);
                }
                case 7 -> {
                    System.out.print("Enter Subject ID to delete: ");
                    String subid = sc.next();
                    sm.deleteSubject(subid);
                }
                case 8 -> {
                    System.out.println("Search by:");
                    System.out.println("1. Name");
                    System.out.println("2. Class");
                    System.out.print("Choice: ");
                    int searchChoice = sc.nextInt();
                    sc.nextLine(); // Clear newline

                    if (searchChoice == 1) {
                        System.out.print("Enter student name: ");
                        String name = sc.nextLine();
                        sm.searchStudent(name, false);
                    } else if (searchChoice == 2) {
                        System.out.print("Enter class name: ");
                        String cls = sc.nextLine();
                        sm.searchStudent(cls, true);
                    } else {
                        System.out.println("Invalid choice.");
                    }
                }
                case 9 -> {
                    System.out.println("Exiting...");
                    return;
                }
                default -> System.out.println("Invalid choice.");
            }
        }
    }
}
