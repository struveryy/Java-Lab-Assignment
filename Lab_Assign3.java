import java.util.*;

class StudentNotFoundException extends Exception {
    public StudentNotFoundException(String msg) {
        super(msg);
    }
}

class Loader implements Runnable {
    public void run() {
        try {
            System.out.println("Loading.....");
            Thread.sleep(1500);
        } catch (Exception e) {}
    }
}

class Student {
    Integer rollNo;
    String name;
    String email;
    String course;
    Double marks;
    char grade;

    Student(Integer rollNo, String name, String email, String course, Double marks) {
        this.rollNo = rollNo;
        this.name = name;
        this.email = email;
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

    void display() {
        System.out.println("Roll No: " + rollNo);
        System.out.println("Name: " + name);
        System.out.println("Email: " + email);
        System.out.println("Course: " + course);
        System.out.println("Marks: " + marks);
        System.out.println("Grade: " + grade);
    }
}

class StudentManager {
    Map<Integer, Student> map = new HashMap<>();

    void addStudent(Student s) {
        map.put(s.rollNo, s);
    }

    Student searchStudent(int rollNo) throws StudentNotFoundException {
        Student s = map.get(rollNo);
        if (s == null) throw new StudentNotFoundException("Student not found");
        return s;
    }
}

public class MainApp {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        StudentManager manager = new StudentManager();

        try {
            System.out.print("Enter Roll No (Integer): ");
            Integer roll = Integer.valueOf(sc.nextLine());

            System.out.print("Enter Name: ");
            String name = sc.nextLine();
            if (name.trim().isEmpty()) throw new Exception("Name cannot be empty");

            System.out.print("Enter Email: ");
            String email = sc.nextLine();

            System.out.print("Enter Course: ");
            String course = sc.nextLine();
            if (course.trim().isEmpty()) throw new Exception("Course cannot be empty");

            System.out.print("Enter Marks: ");
            Double marks = Double.valueOf(sc.nextLine());
            if (marks < 0 || marks > 100) throw new Exception("Invalid marks");

            Thread t = new Thread(new Loader());
            t.start();
            t.join();

            Student s = new Student(roll, name, email, course, marks);
            manager.addStudent(s);
            s.display();

        } catch (StudentNotFoundException e) {
            System.out.println(e.getMessage());
        } catch (NumberFormatException e) {
            System.out.println("Invalid number");
        } catch (Exception e) {
            System.out.println(e.getMessage());
        } finally {
            System.out.println("Program execution completed.");
        }
    }
}
