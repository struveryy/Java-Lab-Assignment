import java.util.*;

abstract class Person {
    String name;
    String email;
    Person() {}
    Person(String name, String email) {
        this.name = name;
        this.email = email;
    }
    public abstract void displayInfo();
}

class Student extends Person {
    int rollNo;
    String course;
    double marks;
    char grade;

    Student() {}

    Student(int rollNo, String name, String email, String course, double marks) {
        super(name, email);
        this.rollNo = rollNo;
        this.course = course;
        this.marks = marks;
        calculateGrade();
    }

    void calculateGrade() {
        if (marks >= 90) grade = 'A';
        else if (marks >= 75) grade = 'B';
        else if (marks >= 60) grade = 'C';
        else grade = 'D';
    }

    public void displayInfo() {
        System.out.println("Student Info:");
        System.out.println("Roll No: " + rollNo);
        System.out.println("Name: " + name);
        System.out.println("Email: " + email);
        System.out.println("Course: " + course);
        System.out.println("Marks: " + marks);
        System.out.println("Grade: " + grade);
    }

    public void displayInfo(String message) {
        System.out.println(message);
        displayInfo();
    }
}

interface RecordActions {
    void addStudent(Student s);
    void deleteStudent(int rollNo);
    void updateStudent(Student s);
    Student searchStudent(int rollNo);
    void viewAllStudents();
}

class StudentManager implements RecordActions {
    Map<Integer, Student> map = new HashMap<>();

    public void addStudent(Student s) {
        if (map.containsKey(s.rollNo)) {
            System.out.println("Roll number already exists. Student not added.");
            return;
        }
        map.put(s.rollNo, s);
        System.out.println("Student added.");
    }

    public void deleteStudent(int rollNo) {
        if (map.remove(rollNo) != null) System.out.println("Student removed.");
        else System.out.println("Student not found.");
    }

    public void updateStudent(Student s) {
        if (!map.containsKey(s.rollNo)) {
            System.out.println("Student not found. Update failed.");
            return;
        }
        map.put(s.rollNo, s);
        System.out.println("Student updated.");
    }

    public Student searchStudent(int rollNo) {
        return map.get(rollNo);
    }

    public void viewAllStudents() {
        if (map.isEmpty()) {
            System.out.println("No students available.");
            return;
        }
        for (Student s : map.values()) {
            s.displayInfo();
        }
    }
}

public class StudentApp {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        StudentManager manager = new StudentManager();
        int choice;

        do {
            System.out.println("===== Student Record Menu =====");
            System.out.println("1. Add Student");
            System.out.println("2. Delete Student");
            System.out.println("3. Update Student");
            System.out.println("4. Search Student");
            System.out.println("5. View All Students");
            System.out.println("6. Exit");
            System.out.print("Enter choice: ");
            while (!sc.hasNextInt()) {
                sc.next();
                System.out.print("Enter a valid choice: ");
            }
            choice = sc.nextInt();
            sc.nextLine();

            if (choice == 1) {
                System.out.print("Roll No: ");
                int r = readInt(sc);
                System.out.print("Name: ");
                String n = sc.nextLine();
                System.out.print("Email: ");
                String e = sc.nextLine();
                System.out.print("Course: ");
                String c = sc.nextLine();
                System.out.print("Marks: ");
                double m = readDouble(sc);
                Student s = new Student(r, n, e, c, m);
                manager.addStudent(s);
            }

            else if (choice == 2) {
                System.out.print("Enter roll number: ");
                int r = readInt(sc);
                manager.deleteStudent(r);
            }

            else if (choice == 3) {
                System.out.print("Roll No: ");
                int r = readInt(sc);
                System.out.print("Name: ");
                String n = sc.nextLine();
                System.out.print("Email: ");
                String e = sc.nextLine();
                System.out.print("Course: ");
                String c = sc.nextLine();
                System.out.print("Marks: ");
                double m = readDouble(sc);
                Student s = new Student(r, n, e, c, m);
                manager.updateStudent(s);
            }

            else if (choice == 4) {
                System.out.print("Enter roll number: ");
                int r = readInt(sc);
                Student s = manager.searchStudent(r);
                if (s != null) s.displayInfo();
                else System.out.println("Student not found.");
            }

            else if (choice == 5) {
                manager.viewAllStudents();
            }

            else if (choice != 6) {
                System.out.println("Invalid choice.");
            }

        } while (choice != 6);

        System.out.println("Exiting application. Goodbye!");
        sc.close();
    }

    static int readInt(Scanner sc) {
        while (!sc.hasNextInt()) {
            sc.next();
            System.out.print("Enter a valid integer: ");
        }
        int v = sc.nextInt();
        sc.nextLine();
        return v;
    }

    static double readDouble(Scanner sc) {
        while (!sc.hasNextDouble()) {
            sc.next();
            System.out.print("Enter a valid number: ");
        }
        double v = sc.nextDouble();
        sc.nextLine();
        return v;
    }
}
