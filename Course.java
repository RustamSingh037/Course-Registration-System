import java.io.*;
import java.util.*;

class Students
 {
    int rollno;
    String name;
    String[] enrolledSubjects = new String[5];
    int enrolledCount = 0;
 }

class Course 
{
    static Students[] students;
    static String[] subjects;
    static int[] subjectIds;

    public static void loadSubjects(File file) 
	{
        try 
		{
            Scanner f = new Scanner(file);
            int n = f.nextInt();
            subjects = new String[n];
            subjectIds = new int[n];
            for (int i = 0; i < n; i++) 
			{
                subjectIds[i] = f.nextInt();
                subjects[i] = f.next();
            }
            f.close();
        }
		catch (Exception e) 
		{
            System.out.println("Error loading subjects.");
        }
    }

    public static void loadStudents(File file) 
	{
        try 
		{
            Scanner f = new Scanner(file);
            int n = f.nextInt();
            students = new Students[n];
            for (int i = 0; i < n; i++) 
			{
                students[i] = new Students();
                students[i].rollno = f.nextInt();
                students[i].name = f.next();
            }
            f.close();
        } 
		catch (Exception e) 
		{
            System.out.println("Error loading students.");
        }
    }

    public static Students searchStudent(int roll)
	{
        for (Students s : students) if (s.rollno == roll) return s;
        return null;
    }

    public static void showAllSubjects() 
	{
        System.out.println("\n--- AVAILABLE SUBJECTS ---");
        for (int i = 0; i < subjects.length; i++)
            System.out.printf("%2d - %s%n", subjectIds[i], subjects[i]);
    }

    public static void showEnrolledSubjects(Students s) 
	{
        if (s.enrolledCount == 0) 
		{
            System.out.println("\nNo subjects enrolled yet.");
            return;
        }
        for (int i = 0; i < s.enrolledCount; i++)
            System.out.printf("%d - %s%n", i + 1, s.enrolledSubjects[i]);
    }

    public static void assignSubjects(Students s, Scanner sc)
	{
        while (s.enrolledCount < 5) 
		{
            showAllSubjects();
            System.out.print("Enter subject ID to enroll (" + (s.enrolledCount + 1) + "/5): ");
            int id = sc.nextInt();
            boolean found = false;
			
            for (int i = 0; i < subjectIds.length; i++) 
			{
                if (subjectIds[i] == id) 
				{
                    String subName = subjects[i];
                    boolean already = false;
                    for (int j = 0; j < s.enrolledCount; j++)
					{
                        if (s.enrolledSubjects[j].equals(subName)) 
						{
							already = true;
						}
					}	
                    if (!already) 
					{
                        s.enrolledSubjects[s.enrolledCount] = subName;
                        s.enrolledCount++;
                        System.out.println(subName + " enrolled successfully!");
                    } 
					else 
					{System.out.println("Already enrolled in this subject.");
					}
                    found = true;
                    break;
                }
            }
			
            if (!found) System.out.println("Invalid subject ID.");
            if (s.enrolledCount == 5) System.out.println("Enrollment complete.");
        }
    }

    public static void removeSubject(Students s, Scanner sc) 
	{
        if (s.enrolledCount == 0) 
		{
            System.out.println("No subjects to remove.");
            return;
        }
        showEnrolledSubjects(s);
        System.out.print("Enter the id of the subject to remove: ");
        int choice = sc.nextInt();
        if (choice < 1 || choice > s.enrolledCount) {
            System.out.println("Invalid choice.");
            return;
        }
        for (int i = choice - 1; i < s.enrolledCount - 1; i++)
        {
            s.enrolledSubjects[i] = s.enrolledSubjects[i + 1];
		}
        s.enrolledCount--;
        System.out.println("Subject removed successfully.");
    }

    public static void showDetails(Students s) {
        System.out.println("\nRoll No: " + s.rollno);
        System.out.println("Name: " + s.name);
        showEnrolledSubjects(s);
    }

    public static void saveStudent(File file, Students s) 
	{
        try 
		{
            BufferedWriter bw = new BufferedWriter(new FileWriter(file, true));
			
            bw.write(s.rollno + " " + s.name+"-");
			
            for (int i = 0; i < s.enrolledCount; i++)
			{
                bw.write(" " + s.enrolledSubjects[i]);
				if(i!=s.enrolledCount-1)
				{
					bw.write(",");
				}	
			}
            bw.newLine();
            bw.close();
			
        }
		catch (IOException e) 
		{
            System.out.println("Error writing to file.");
        }
    }

    public static void main(String args[]) 
	{
        Scanner sc = new Scanner(System.in);
        File subjectsFile = new File("subjects.txt");
        File studentsFile = new File("students.txt");
        File enrolledFile = new File("enrolled.txt");

        loadSubjects(subjectsFile);
        loadStudents(studentsFile);

        int mainChoice;
        do 
		{
            System.out.println("\n===== MAIN MENU =====");
            System.out.println("1 - Enter Roll Number");
            System.out.println("2 - Exit Program");
            System.out.println("=====================");
            System.out.print("Enter choice: ");
            mainChoice = sc.nextInt();

            if (mainChoice == 1) 
			{
                System.out.print("\nEnter your roll number: ");
                int roll = sc.nextInt();
                Students s = searchStudent(roll);
                if (s == null) 
				{
                    System.out.println("Roll number not found!");
                    continue;
                }
                System.out.println("\nWelcome, " + s.name + "!");
                int choice;
                do 
				{
                    System.out.println("\n-------- MENU --------");
                    System.out.println("1 - Assign Subjects");
                    System.out.println("2 - Remove Subject");
                    System.out.println("3 - Show My Details");
                    System.out.println("4 - Exit");
                    System.out.println("----------------------");
                    System.out.print("Enter choice: ");
                    choice = sc.nextInt();
                    switch (choice) 
					{
                        case 1 -> assignSubjects(s, sc);
                        case 2 -> removeSubject(s, sc);
                        case 3 -> showDetails(s);
                        case 4 -> saveStudent(enrolledFile, s);
                        default -> System.out.println("Invalid choice.");
                    }
                } while (choice != 4);
            }
        } while (mainChoice != 2);
        sc.close();
        System.out.println("Program closed.");
    }
}
