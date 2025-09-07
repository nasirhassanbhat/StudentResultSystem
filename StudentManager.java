import java.sql.*;

public class StudentManager {

    public void addStudent(String id, String name, String cls) {
        try {
            Connection con = DBConnect.connect();
            PreparedStatement ps = con.prepareStatement("INSERT INTO student VALUES (?, ?, ?)");
            ps.setString(1, id);
            ps.setString(2, name);
            ps.setString(3, cls);
            ps.executeUpdate();
            System.out.println("Student Added.");
            con.close();
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    public void viewResult(String studentId) {
    try {
        Connection con = DBConnect.connect();
        String query = """
            SELECT s.name AS subject, m.marks
            FROM marks m
            JOIN subject s ON m.subject_id = s.subject_id
            WHERE m.student_id = ?
        """;
        PreparedStatement ps = con.prepareStatement(query);
        ps.setString(1, studentId);
        ResultSet rs = ps.executeQuery();

        boolean found = false;
        while (rs.next()) {
            found = true;
            System.out.println(rs.getString("subject") + " - " + rs.getInt("marks"));
        }

        if (!found) {
            System.out.println("No result found for student ID: " + studentId);
        }

        con.close();
    } catch (Exception e) {
        System.out.println(e);
    }
}

public void addMarks(String studentId, String subjectId, int marks) {
    try {
        Connection con = DBConnect.connect();
        PreparedStatement ps = con.prepareStatement("INSERT INTO marks VALUES (?, ?, ?)");
        ps.setString(1, studentId);
        ps.setString(2, subjectId);
        ps.setInt(3, marks);
        ps.executeUpdate();
        System.out.println("Marks added successfully.");
        con.close();
    } catch (SQLIntegrityConstraintViolationException e) {
        System.out.println("Invalid student ID or subject ID, or marks already exist.");
    } catch (Exception e) {
        System.out.println("Error: " + e);
    }
}

public void addSubject(String subjectId, String subjectName) {
    try {
        Connection con = DBConnect.connect();
        PreparedStatement ps = con.prepareStatement("INSERT INTO subject VALUES (?, ?)");
        ps.setString(1, subjectId);
        ps.setString(2, subjectName);
        ps.executeUpdate();
        System.out.println("Subject added successfully.");
        con.close();
    } catch (SQLIntegrityConstraintViolationException e) {
        System.out.println("Subject ID already exists.");
    } catch (Exception e) {
        System.out.println("Error: " + e);
    }
}

public void updateMarks(String studentId, String subjectId, int newMarks) {
    try {
        Connection con = DBConnect.connect();
        String query = "UPDATE marks SET marks = ? WHERE student_id = ? AND subject_id = ?";
        PreparedStatement ps = con.prepareStatement(query);
        ps.setInt(1, newMarks);
        ps.setString(2, studentId);
        ps.setString(3, subjectId);
        int rows = ps.executeUpdate();

        if (rows > 0) {
            System.out.println("Marks updated successfully.");
        } else {
            System.out.println("No existing marks found for this student and subject.");
        }

        con.close();
    } catch (Exception e) {
        System.out.println("Error: " + e);
    }
}

public void deleteStudent(String studentId) {
    try {
        Connection con = DBConnect.connect();
        PreparedStatement ps = con.prepareStatement("DELETE FROM student WHERE student_id = ?");
        ps.setString(1, studentId);
        int rows = ps.executeUpdate();

        if (rows > 0) {
            System.out.println("Student deleted successfully.");
        } else {
            System.out.println("Student not found.");
        }

        con.close();
    } catch (Exception e) {
        System.out.println("Error: " + e);
    }
}

public void deleteSubject(String subjectId) {
    try {
        Connection con = DBConnect.connect();
        PreparedStatement ps = con.prepareStatement("DELETE FROM subject WHERE subject_id = ?");
        ps.setString(1, subjectId);
        int rows = ps.executeUpdate();

        if (rows > 0) {
            System.out.println("Subject deleted successfully.");
        } else {
            System.out.println("Subject not found.");
        }

        con.close();
    } catch (Exception e) {
        System.out.println("Error: " + e);
    }
}

public void searchStudent(String keyword, boolean byClass) {
    try {
        Connection con = DBConnect.connect();
        String query = byClass
            ? "SELECT * FROM student WHERE class = ?"
            : "SELECT * FROM student WHERE LOWER(name) LIKE ?";
        PreparedStatement ps = con.prepareStatement(query);

        if (byClass) {
            ps.setString(1, keyword);
        } else {
            ps.setString(1, "%" + keyword.toLowerCase() + "%");
        }

        ResultSet rs = ps.executeQuery();
        boolean found = false;

        while (rs.next()) {
            found = true;
            System.out.println("ID: " + rs.getString("student_id") +
                               ", Name: " + rs.getString("name") +
                               ", Class: " + rs.getString("class"));
        }

        if (!found) {
            System.out.println("No students found.");
        }

        con.close();
    } catch (Exception e) {
        System.out.println("Error: " + e);
    }
}


}
