import java.io.*;
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
        System.out.println("Roll No: " + rollNo);
        System.out.println("Name: " + name);
        System.out.println("Email: " + email);
        System.out.println("Course: " + course);
        System.out.println("Marks: " + marks);
        System.out.println("Grade: " + grade);
        System.out.println();
    }

    String toFileString() {
        return rollNo + "," + name + "," + email + "," + course + "," + marks;
    }
}

interface RecordActions {
    void addStudent(Student s);
    void deleteStudent(String name);
    void updateStudent(Student s);
    Student searchStudent(String name);
    void viewAllStudents();
}

class StudentNotFoundException extends Exception {
    public StudentNotFoundException(String msg) {
        super(msg);
    }
}

class Loader implements Runnable {
    public void run() {
        try {
            System.out.println("Loading...");
            Thread.sleep(1000);
        } catch (Exception e) {}
    }
}

class FileUtil {
    static ArrayList<Student> load(String file) {
        ArrayList<Student> list = new ArrayList<>();
        try {
            File f = new File(file);
            if (!f.exists()) f.createNewFile();
            BufferedReader br = new BufferedReader(new FileReader(f));
            String line;
            while ((line = br.readLine()) != null) {
                String[] p = line.split(",");
                if (p.length == 5) {
                    int r = Integer.parseInt(p[0]);
                    String n = p[1];
                    String e = p[2];
                    String c = p[3];
                    double m = Double.parseDouble(p[4]);
                    list.add(new Student(r, n, e, c, m));
                }
            }
            br.close();
        } catch (Exception e) {}
        return list;
    }

    static void save(String file, ArrayList<Student> list) {
        try {
            BufferedWriter bw = new BufferedWriter(new FileWriter(file));
            for (Student s : list) bw.write(s.toFileString() + "\n");
            bw.close();
        } catch (Exception e) {}
    }
}

class StudentManager implements RecordActions {
    ArrayList<Student> list = new ArrayList<>();

    public void addStudent(Student s) {
        for (Student st : list) {
            if (st.rollNo == s.rollNo)
                return;
        }
        list.add(s);
    }

    public void deleteStudent(String name) {
        list.removeIf(s -> s.name.equalsIgnoreCase(name));
    }

    public void updateStudent(Student s) {
        for (int i = 0; i < list.size(); i++) {
            if (list.get(i).rollNo == s.rollNo) {
                list.set(i, s);
                return;
            }
        }
    }

    public Student searchStudent(String name) {
        for (Student s : list) {
            if (s.name.equalsIgnoreCase(name)) return s;
        }
        return null;
    }

    public void viewAllStudents() {
        Iterator<Student> it = list.iterator();
        while (it.hasNext()) it.next().displayInfo();
    }

    void sortByMarksDesc() {
        list.sort((a, b) -> Double.compare(b.marks, a.marks));
    }
}

public class MainApp {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        StudentManager sm = new StudentManager();
        String file = "students.txt";

        sm.list = FileUtil.load(file);
        System.out.println("Loaded students from file:\n");
        sm.viewAllStudents();

        int choice;
        do {
            System.out.println("===== Capstone Student Menu =====");
            System.out.println("1. Add Student");
            System.out.println("2. View All Students");
            System.out.println("3. Search by Name");
            System.out.println("4. Delete by Name");
            System.out.println("5. Sort by Marks");
            System.out.println("6. Save and Exit");
            System.out.print("Enter choice: ");
            choice = Integer.parseInt(sc.nextLine());

            if (choice == 1) {
                Thread t = new Thread(new Loader());
                t.start();
                try { t.join(); } catch (Exception e) {}

                System.out.print("Enter Roll No: ");
                int r = Integer.parseInt(sc.nextLine());
                System.out.print("Enter Name: ");
                String n = sc.nextLine();
                System.out.print("Enter Email: ");
                String e = sc.nextLine();
                System.out.print("Enter Course: ");
                String c = sc.nextLine();
                System.out.print("Enter Marks: ");
                double m = Double.parseDouble(sc.nextLine());
                Student s = new Student(r, n, e, c, m);
                sm.addStudent(s);
            }

            else if (choice == 2) {
                sm.viewAllStudents();
            }

            else if (choice == 3) {
                System.out.print("Enter name: ");
                String n = sc.nextLine();
                Student s = sm.searchStudent(n);
                if (s != null) s.displayInfo();
                else System.out.println("Student not found\n");
            }

            else if (choice == 4) {
                System.out.print("Enter name: ");
                sm.deleteStudent(sc.nextLine());
                System.out.println("Student record deleted.\n");
            }

            else if (choice == 5) {
                sm.sortByMarksDesc();
                sm.viewAllStudents();
            }

        } while (choice != 6);

        FileUtil.save(file, sm.list);
        System.out.println("Saved and exiting.");
    }
}
