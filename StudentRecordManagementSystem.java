import java.util.*;
import java.io.*;

class Student {
    private int id;
    private String name;
    private String department;
    private String email;
    private double cgpa;

    public Student(int id, String name, String department,
                   String email, double cgpa) {
        this.id = id;
        this.name = name;
        this.department = department;
        this.email = email;
        this.cgpa = cgpa;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public double getCgpa() {
        return cgpa;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setCgpa(double cgpa) {
        this.cgpa = cgpa;
    }

    @Override
    public String toString() {
        return "ID: " + id +
                " | Name: " + name +
                " | Department: " + department +
                " | Email: " + email +
                " | CGPA: " + cgpa;
    }

    public String toFileString() {
        return id + "," + name + "," + department +
                "," + email + "," + cgpa;
    }
}

public class StudentRecordManagementSystem {

    static Scanner sc = new Scanner(System.in);

    static ArrayList<Student> students = new ArrayList<>();
    static HashMap<Integer, Student> studentMap = new HashMap<>();

    static final String FILE_NAME = "students.txt";

    public static void main(String[] args) {

        loadStudents();

        while (true) {
            System.out.println("\n===== STUDENT RECORD MANAGEMENT =====");
            System.out.println("1. Add Student");
            System.out.println("2. View Students");
            System.out.println("3. Search Student");
            System.out.println("4. Update Student");
            System.out.println("5. Delete Student");
            System.out.println("6. Sort by Name");
            System.out.println("7. Sort by CGPA");
            System.out.println("8. Save Records");
            System.out.println("9. Exit");

            int choice = readInt("Enter your choice: ");

            switch (choice) {
                case 1:
                    addStudent();
                    break;
                case 2:
                    viewStudents();
                    break;
                case 3:
                    searchStudent();
                    break;
                case 4:
                    updateStudent();
                    break;
                case 5:
                    deleteStudent();
                    break;
                case 6:
                    sortByName();
                    break;
                case 7:
                    sortByCgpa();
                    break;
                case 8:
                    saveStudents();
                    break;
                case 9:
                    saveStudents();
                    System.out.println("Thank you! Application closed.");
                    return;
                default:
                    System.out.println("Invalid choice. Try again.");
            }
        }
    }

    static int readInt(String message) {
        while (true) {
            System.out.print(message);
            try {
                return Integer.parseInt(sc.nextLine().trim());
            } catch (NumberFormatException e) {
                System.out.println("Please enter a valid number.");
            }
        }
    }

    static double readCgpa() {
        while (true) {
            System.out.print("Enter CGPA (0-10): ");
            try {
                double cgpa = Double.parseDouble(sc.nextLine().trim());

                if (cgpa >= 0 && cgpa <= 10) {
                    return cgpa;
                }

                System.out.println("CGPA must be between 0 and 10.");
            } catch (NumberFormatException e) {
                System.out.println("Enter a valid CGPA.");
            }
        }
    }

    static void addStudent() {

        int id = readInt("Enter Student ID: ");

        if (id <= 0 || studentMap.containsKey(id)) {
            System.out.println("Invalid or duplicate student ID.");
            return;
        }

        System.out.print("Enter Name: ");
        String name = sc.nextLine().trim();

        System.out.print("Enter Department: ");
        String department = sc.nextLine().trim();

        System.out.print("Enter Email: ");
        String email = sc.nextLine().trim();

        if (name.isEmpty() || department.isEmpty() ||
                email.isEmpty() || !email.contains("@")) {
            System.out.println("Invalid student details.");
            return;
        }

        double cgpa = readCgpa();

        Student s = new Student(id, name, department, email, cgpa);

        students.add(s);
        studentMap.put(id, s);

        saveStudents();

        System.out.println("Student added successfully!");
    }

    static void viewStudents() {

        if (students.isEmpty()) {
            System.out.println("No student records found.");
            return;
        }

        System.out.println("\n----- STUDENT DETAILS -----");

        for (Student s : students) {
            System.out.println(s);
        }
    }

    static void searchStudent() {

        int id = readInt("Enter Student ID to search: ");

        Student s = studentMap.get(id);

        if (s != null) {
            System.out.println(s);
        } else {
            System.out.println("Student not found.");
        }
    }

    static void updateStudent() {

        int id = readInt("Enter Student ID to update: ");

        Student s = studentMap.get(id);

        if (s == null) {
            System.out.println("Student not found.");
            return;
        }

        System.out.print("Enter New Name: ");
        String name = sc.nextLine().trim();

        System.out.print("Enter New Department: ");
        String department = sc.nextLine().trim();

        System.out.print("Enter New Email: ");
        String email = sc.nextLine().trim();

        if (name.isEmpty() || department.isEmpty() ||
                email.isEmpty() || !email.contains("@")) {
            System.out.println("Invalid student details. Update cancelled.");
            return;
        }

        double cgpa = readCgpa();

        s.setName(name);
        s.setDepartment(department);
        s.setEmail(email);
        s.setCgpa(cgpa);

        saveStudents();

        System.out.println("Student updated successfully!");
    }

    static void deleteStudent() {

        int id = readInt("Enter Student ID to delete: ");

        Student s = studentMap.remove(id);

        if (s == null) {
            System.out.println("Student not found.");
            return;
        }

        students.remove(s);

        saveStudents();

        System.out.println("Student deleted successfully!");
    }

    static void sortByName() {

        students.sort(
                Comparator.comparing(Student::getName,
                        String.CASE_INSENSITIVE_ORDER)
        );

        System.out.println("\n----- STUDENTS SORTED BY NAME -----");
        viewStudents();
    }

    static void sortByCgpa() {

        students.sort(
                Comparator.comparingDouble(Student::getCgpa).reversed()
        );

        System.out.println("\n----- STUDENTS SORTED BY CGPA -----");
        viewStudents();
    }

    static void saveStudents() {

        try (BufferedWriter bw =
                     new BufferedWriter(new FileWriter(FILE_NAME))) {

            for (Student s : students) {
                bw.write(s.toFileString());
                bw.newLine();
            }

        } catch (IOException e) {
            System.out.println("Error saving student records.");
        }
    }

    static void loadStudents() {

        File file = new File(FILE_NAME);

        if (!file.exists()) {
            return;
        }

        try (BufferedReader br =
                     new BufferedReader(new FileReader(file))) {

            String line;

            while ((line = br.readLine()) != null) {

                String[] data = line.split(",", -1);

                if (data.length != 5) {
                    continue;
                }

                try {
                    int id = Integer.parseInt(data[0]);
                    String name = data[1];
                    String department = data[2];
                    String email = data[3];
                    double cgpa = Double.parseDouble(data[4]);

                    if (id <= 0 || cgpa < 0 || cgpa > 10 ||
                            studentMap.containsKey(id)) {
                        continue;
                    }

                    Student s = new Student(
                            id, name, department, email, cgpa
                    );

                    students.add(s);
                    studentMap.put(id, s);

                } catch (NumberFormatException e) {
                    System.out.println("Skipping an invalid record.");
                }
            }

        } catch (IOException e) {
            System.out.println("Error loading student records.");
        }
    }
}
