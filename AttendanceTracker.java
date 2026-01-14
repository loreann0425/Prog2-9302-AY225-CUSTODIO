
import java.util.Scanner;   
public class attendancetracker {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Enter the number of students: ");
        int numStudents = scanner.nextInt();
        scanner.nextLine(); // Consume newline

        String[] studentNames = new String[numStudents];
        boolean[] attendance = new boolean[numStudents];

        for (int i = 0; i < numStudents; i++) {
            System.out.print("Enter name of student " + (i + 1) + ": ");
            studentNames[i] = scanner.nextLine();
            System.out.print("Is " + studentNames[i] + " present? (true/false): ");
            attendance[i] = scanner.nextBoolean();
            scanner.nextLine(); // Consume newline
        }

        System.out.println("\nAttendance Report:");
        for (int i = 0; i < numStudents; i++) {
            System.out.println(studentNames[i] + ": " + (attendance[i] ? "Present" : "Absent"));
        }
    }
}