import java.io.*;
import java.util.*;

class Student {
    int rollNo;
    String name;
    String email;
    String course;
    double marks;

    Student(int rollNo, String name, String email, String course, double marks) {
        this.rollNo = rollNo;
        this.name = name;
        this.email = email;
        this.course = course;
        this.marks = marks;
    }

    String toFileString() {
        return rollNo + "," + name + "," + email + "," + course + "," + marks;
    }

    void display() {
        System.out.println("Roll No: " + rollNo);
        System.out.println("Name: " + name);
        System.out.println("Email: " + email);
        System.out.println("Course: " + course);
        System.out.println("Marks: " + marks);
        System.out.println();
    }
}

class FileUtil {
    static ArrayList<Student> readFile(String filename) {
        ArrayList<Student> list = new ArrayList<>();
        try {
            File f = new File(filename);
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

    static void writeFile(String filename, ArrayList<Student> list) {
        try {
            BufferedWriter bw = new BufferedWriter(new FileWriter(filename));
            for (Student s : list) bw.write(s.toFileString() + "\n");
            bw.close();
        } catch (Exception e) {}
    }

    static void randomRead(String filename) {
        try {
            RandomAccessFile raf = new RandomAccessFile(filename, "r");
            if (raf.length() > 0) {
                raf.seek(0);
                String line = raf.readLine();
            }
            raf.close();
        } catch (Exception e) {}
    }
}

class StudentManager {
    ArrayList<Student> list = new ArrayList<>();

    void load(String file) {
        list = FileUtil.readFile(file);
    }

    void save(String file) {
        FileUtil.writeFile(file, list);
    }

    void addStudent(Student s) {
        list.add(s);
    }

    void viewAll() {
        Iterator<Student> it = list.iterator();
        while (it.hasNext()) it.next().display();
    }

    void searchByName(String name) {
        boolean found = false;
        for (Student s : list) {
            if (s.name.equalsIgnoreCase(name)) {
                s.display();
                found = true;
            }
        }
        if (!found) System.out.println("No student found.\n");
    }

    void deleteByName(String name) {
        list.removeIf(s -> s.name.equalsIgnoreCase(name));
    }

    void sortByMarks() {
        list.sort(Comparator.comparingDouble(s -> s.marks));
    }
}

public class MainApp {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        StudentManager sm = new StudentManager();
        String file = "students.txt";

        sm.load(file);
        System.out.println("Loaded students from file:\n");
        sm.viewAll();

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
                sm.addStudent(new Student(r, n, e, c, m));
            }

            else if (choice == 2) {
                sm.viewAll();
            }

            else if (choice == 3) {
                System.out.print("Enter name: ");
                sm.searchByName(sc.nextLine());
            }

            else if (choice == 4) {
                System.out.print("Enter name: ");
                sm.deleteByName(sc.nextLine());
            }

            else if (choice == 5) {
                sm.sortByMarks();
                sm.viewAll();
            }

        } while (choice != 6);

        sm.save(file);
        FileUtil.randomRead(file);
        System.out.println("Saved and exiting...");
    }
}
