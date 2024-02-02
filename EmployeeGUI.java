package test;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;

public class EmployeeGUI extends JFrame {

    JTextField tf1 = new JTextField(5);
    JTextField tf2 = new JTextField(5);
    JTextField tf3 = new JTextField(5);
    JTextField tf4 = new JTextField(5);
    JTextField tf5 = new JTextField(5);
    JTextField tf6 = new JTextField(5);
    JTextField tf7 = new JTextField(5);
    JTextField tf8 = new JTextField(5);

    JButton bt1 = new JButton("전체내용");
    JButton bt2 = new JButton("추가");
    JButton bt3 = new JButton("이름검색");
    JButton bt4 = new JButton("수정");
    JButton bt5 = new JButton("삭제");

    JTextArea ta = new JTextArea(10, 40);

    Connection conn;
    Statement stmt;

    public EmployeeGUI() {
        String url = "jdbc:mysql://localhost:3306/firm";
        String id = "root";
        String pass = "mysql";

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            conn = DriverManager.getConnection(url, id, pass);
            stmt = conn.createStatement();
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }

        JLabel lb1 = new JLabel("사원넘버:");
        JLabel lb2 = new JLabel("이름:");
        JLabel lb3 = new JLabel("직무:");
        JLabel lb4 = new JLabel("상사넘버:");
        JLabel lb5 = new JLabel("입사날짜:");
        JLabel lb6 = new JLabel("연봉:");
        JLabel lb7 = new JLabel("보너스:");
        JLabel lb8 = new JLabel("부서넘버:");

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        Container con = getContentPane();
        con.setLayout(new BorderLayout());

        JPanel jp1 = new JPanel(new FlowLayout());
        jp1.add(bt1);
        jp1.add(bt2);
        jp1.add(bt3);
        jp1.add(bt4);
        jp1.add(bt5);
        con.add(jp1, BorderLayout.SOUTH);

        JScrollPane scroll = new JScrollPane(ta);
        JPanel jp2 = new JPanel(new FlowLayout());
        jp2.add(scroll);
        con.add(jp2, BorderLayout.CENTER);

        JPanel jp3 = new JPanel(new FlowLayout());
        con.add(jp3, BorderLayout.NORTH);
        jp3.add(lb1);
        jp3.add(tf1);
        jp3.add(lb2);
        jp3.add(tf2);
        jp3.add(lb3);
        jp3.add(tf3);
        jp3.add(lb4);
        jp3.add(tf4);
        jp3.add(lb5);
        jp3.add(tf5);
        jp3.add(lb6);
        jp3.add(tf6);
        jp3.add(lb7);
        jp3.add(tf7);
        jp3.add(lb8);
        jp3.add(tf8);

        setTitle("emp 관리");
        setLocation(500, 400);
        setSize(1000, 300);
        setVisible(true);

        bt1.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                select();
            }
        });

        bt2.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                insert();
            }
        });

        bt3.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                search();
            }
        });

        bt4.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                update();
            }
        });

        bt5.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                delete();
            }
        });
    }

    private void clearTextField() {
        tf1.setText("");
        tf2.setText("");
        tf3.setText("");
        tf4.setText("");
        tf5.setText("");
        tf6.setText("");
        tf7.setText("");
        tf8.setText("");
    }

    public void select() {
        String sql = "select empno, ename, job, mgr, hiredate, sal, comm, deptno from emp";
        try {
            ResultSet rs = stmt.executeQuery(sql);
            ta.setText("");
            while (rs.next()) {
                int empno = rs.getInt("empno");
                String ename = rs.getString("ename");
                String job = rs.getString("job");
                int mgr = rs.getInt("mgr");
                Date hiredate = rs.getDate("hiredate");
                double sal = rs.getDouble("sal");
                double comm = rs.getDouble("comm");
                int deptno = rs.getInt("deptno");

                String str = String.format("%-7d%-15s%-15s%-7d%-15s%-10.2f%-10.2f%-7d\n",
                        empno, ename, job, mgr, hiredate, sal, comm, deptno);
                ta.append(str);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void insert() {
        int empno = Integer.parseInt(tf1.getText());
        String ename = tf2.getText();
        String job = tf3.getText();
        int mgr = Integer.parseInt(tf4.getText());
        String hiredate = tf5.getText();
        double sal = Double.parseDouble(tf6.getText());
        double comm = Double.parseDouble(tf7.getText());
        int deptno = Integer.parseInt(tf8.getText());

        String sql = String.format("insert into emp values(%d, '%s', '%s', %d, '%s', %.2f, %.2f, %d)",
                empno, ename, job, mgr, hiredate, sal, comm, deptno);
        try {
            int res = stmt.executeUpdate(sql);
            if (res > 0) {
                JOptionPane.showMessageDialog(null, "추가되었습니다.");
                clearTextField();
                select();
            } else {
                JOptionPane.showMessageDialog(null, "추가할 데이터가 없습니다.");
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    public void search() {
        String ename = tf2.getText();
        String sql = String.format("select * from emp where ename = '%s'", ename);
        try {
            ResultSet rs = stmt.executeQuery(sql);
            ta.setText("");
            while (rs.next()) {
                int empno = rs.getInt("empno");
                String job = rs.getString("job");
                int mgr = rs.getInt("mgr");
                Date hiredate = rs.getDate("hiredate");
                double sal = rs.getDouble("sal");
                double comm = rs.getDouble("comm");
                int deptno = rs.getInt("deptno");

                String str = String.format("사원넘버: %d\n이름: %s\n직무: %s\n상사넘버: %d\n입사날짜: %s\n연봉: %.2f\n보너스: %.2f\n부서넘버: %d\n\n",
                        empno, ename, job, mgr, hiredate, sal, comm, deptno);
                ta.append(str);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void update() {
        int empno = Integer.parseInt(tf1.getText());
        String ename = tf2.getText();
        String job = tf3.getText();
        int mgr = Integer.parseInt(tf4.getText());
        String hiredate = tf5.getText();
        double sal = Double.parseDouble(tf6.getText());
        double comm = Double.parseDouble(tf7.getText());
        int deptno = Integer.parseInt(tf8.getText());

        String sql = String.format("update emp set ename='%s', job='%s', mgr=%d, hiredate='%s', sal=%.2f, comm=%.2f, deptno=%d where empno=%d",
                ename, job, mgr, hiredate, sal, comm, deptno, empno);
        try {
            int res = stmt.executeUpdate(sql);
            if (res > 0) {
                JOptionPane.showMessageDialog(null, "수정되었습니다.");
                clearTextField();
                select();
            } else {
                JOptionPane.showMessageDialog(null, "수정할 데이터가 없습니다.");
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    public void delete() {
        int empno = Integer.parseInt(tf1.getText());
        String sql = String.format("delete from emp where empno=%d", empno);
        try {
            int res = stmt.executeUpdate(sql);
            if (res > 0) {
                JOptionPane.showMessageDialog(null, "삭제되었습니다.");
                clearTextField();
                select();
            } else {
                JOptionPane.showMessageDialog(null, "삭제할 데이터가 없습니다.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        new EmployeeGUI();
    }
}
