package test;

import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Scanner;

public class EmployeeCLI {

    public static void main(String[] args) {
        String url = "jdbc:mysql://localhost:3306/firm";
        String id = "root";
        String pass = "mysql";

        try (Scanner scanner = new Scanner(System.in);
             Connection conn = DriverManager.getConnection(url, id, pass)) {

            Class.forName("com.mysql.cj.jdbc.Driver");

            Statement statement = conn.createStatement();

            boolean exit = false;

            while (!exit) {
                System.out.println("1. 전체 직원 조회");
                System.out.println("2. 특정 직원 조회");
                System.out.println("3. 직원 정보 삽입");
                System.out.println("4. 직원 정보 삭제");
                System.out.println("5. 종료");
                System.out.print("선택: ");

                int choice = scanner.nextInt();
                scanner.nextLine();

                switch (choice) {
                    case 1:
                        displayAllEmployees(statement);
                        break;
                    case 2:
                        System.out.print("직원의 ID를 입력하세요: ");
                        int employeeId = scanner.nextInt();
                        scanner.nextLine();
                        displayEmployeeById(statement, employeeId);
                        break;
                    case 3:
                        insertEmployee(statement, scanner);
                        break;
                    case 4:
                        System.out.print("삭제할 직원의 empno: ");
                        int deleteEmployeeId = scanner.nextInt();
                        scanner.nextLine();
                        deleteEmployeeById(statement, deleteEmployeeId);
                        break;
                    case 5:
                        exit = true;
                        break;
                    default:
                        System.out.println("올바른 선택이 아닙니다. 다시 선택해주세요.");
                        break;
                }
            }

            statement.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void displayAllEmployees(Statement statement) throws SQLException {
        String query = "SELECT * FROM emp";
        ResultSet resultSet = statement.executeQuery(query);

        while (resultSet.next()) {
            int empId = resultSet.getInt("empno");
            String empName = resultSet.getString("ename");
            String job = resultSet.getString("job");
            int mgr = resultSet.getInt("mgr");
            Date hiredate = resultSet.getDate("hiredate");
            double empSalary = resultSet.getDouble("sal");
            double comm = resultSet.getDouble("comm");
            int deptno = resultSet.getInt("deptno");

           System.out.printf("직원 ID: %-7d 이름: %-15s 직무: %-15s 상사: %-7d 입사일: %-15s 급여: %-10.2f 보너스: %-10.2f 부서번호: %-7d%n", empId, empName, job, mgr, hiredate, empSalary, comm, deptno);

        }

        resultSet.close();
    }

    private static void displayEmployeeById(Statement statement, int employeeId) throws SQLException {
        String query = "SELECT * FROM emp WHERE empno = " + employeeId;
        ResultSet resultSet = statement.executeQuery(query);

        if (resultSet.next()) {
            int empId = resultSet.getInt("empno");
            String empName = resultSet.getString("ename");
            String job = resultSet.getString("job");
            int mgr = resultSet.getInt("mgr");
            Date hiredate = resultSet.getDate("hiredate");
            double empSalary = resultSet.getDouble("sal");
            double comm = resultSet.getDouble("comm");
            int deptno = resultSet.getInt("deptno");

            System.out.println("직원 ID: " + empId + ", 이름: " + empName + ", 직무: " + job + ", 상사: " + mgr +
                    ", 입사일: " + hiredate + ", 급여: " + empSalary + ", 보너스: " + comm + ", 부서번호: " + deptno);
        } else {
            System.out.println("해당 ID의 직원이 존재하지 않습니다.");
        }

        resultSet.close();
    }

    private static void insertEmployee(Statement statement, Scanner scanner) throws SQLException {
        System.out.print("empno: ");
        int empno = scanner.nextInt();
        scanner.nextLine();
        System.out.print("ename: ");
        String ename = scanner.nextLine();
        System.out.print("job: ");
        String job = scanner.nextLine();
        System.out.print("mgr: ");
        int mgr = scanner.nextInt();
        scanner.nextLine();
        System.out.print("hiredate: ");
        String hiredate = scanner.nextLine();
        System.out.print("sal: ");
        double sal = scanner.nextDouble();
        scanner.nextLine();
        System.out.print("comm: ");
        double comm = scanner.nextDouble();
        scanner.nextLine();
        System.out.print("deptno: ");
        int deptno = scanner.nextInt();
        scanner.nextLine();

        String sql = "INSERT INTO emp(empno, ename, job, mgr, hiredate, sal, comm, deptno) VALUES (" +
                empno + ", '" + ename + "', '" + job + "', " + mgr + ", '" + hiredate + "', " +
                sal + ", " + comm + ", " + deptno + ")";
        int rowsAffected = statement.executeUpdate(sql);
        if (rowsAffected > 0) {
            System.out.println("직원 정보 삽입이 완료되었습니다.");
        } else {
            System.out.println("직원 정보 삽입에 실패했습니다.");
        }
    }

    private static void deleteEmployeeById(Statement statement, int deleteEmployeeId) throws SQLException {
        String sql = "DELETE FROM emp WHERE empno = " + deleteEmployeeId;
        int rowsAffected = statement.executeUpdate(sql);
        if (rowsAffected > 0) {
            System.out.println("직원 삭제가 완료되었습니다.");
        } else {
            System.out.println("해당 ID의 직원이 존재하지 않습니다.");
        }
    }
}
