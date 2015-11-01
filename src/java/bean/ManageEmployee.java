/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bean;

import java.io.Serializable;
import java.util.List;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.AnnotationConfiguration;
import org.hibernate.cfg.Configuration;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;

/**
 *
 * @author Sergey
 */
@ManagedBean
@SessionScoped
public class ManageEmployee implements Serializable {

    private int result = 0;
    long time = 0;

    private static SessionFactory factory = new AnnotationConfiguration().
            configure().addAnnotatedClass(Employee.class).buildSessionFactory();
    ManageEmployee me;

    public ManageEmployee() {
    }

    public void start() {
        me = new ManageEmployee();
        addRows();
    }

    public void addRows() {
        Integer empID1 = me.addEmployee("Zara5", "Ali", 2000);
        Integer empID2 = me.addEmployee("Daisy5", "Das", 5000);
        Integer empID3 = me.addEmployee("John5", "Paul", 5000);
        Integer empID4 = me.addEmployee("Mohd5", "Yasee", 3000);
    }

    private Integer addEmployee(String firstName, String lastName, int salary) {
        System.out.println("firstName: " + firstName);
        Session session = factory.openSession();
        Transaction tx = null;
        Integer employeeID = null;
        try {
            tx = session.beginTransaction();
            Employee employee = new Employee(firstName, lastName, salary);
            employeeID = (Integer) session.save(employee);
            tx.commit();
        } catch (HibernateException he) {
            System.out.println("" + he.getMessage());
            tx.rollback();
        } finally {
            session.close();
        }
        return employeeID;
    }

    public void deleteObjects() {
        Session session = factory.openSession();
        List<Employee> list = null;
        Transaction tx = null;
        int valume = 9;
        try {
            tx = session.beginTransaction();
            time = System.currentTimeMillis();
            for ( int i = 4013812; i < 4013810 + 9999; i++ ) {
                Employee e = (Employee) session.get(Employee.class, i);
                session.delete(e);
            }
            time = System.currentTimeMillis() - time;
            tx.commit();
        } catch (HibernateException he) {
            tx.rollback();
            System.out.println("");
        } finally {
            session.close();
        }
    }

    public void createObjects() {
        Session session = factory.openSession();
        Transaction tx = null;
        Integer employeeID = null;
        try {
            tx = session.beginTransaction();
            for (int i = 0; i < 100000; i++) {
                String fname = "First Name " + i;
                String lname = "Last Name " + i;
                Integer salary = i;
                Employee employee = new Employee(fname, lname, salary);
                session.save(employee);
                if (i % 50 == 0) {
                    session.flush();
                    session.clear();
                }
            }
            tx.commit();
        } catch (HibernateException e) {
            if (tx != null) {
                tx.rollback();
            }
            e.printStackTrace();
        } finally {
            session.close();
        }
        return;
    }

    public List<Employee> listEntity() {
        Session session = factory.openSession();
        List<Employee> list = null;
        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            String sql = "select * from EMPLOYEE WHERE salary < 9999";
            SQLQuery query = session.createSQLQuery(sql);
            query
                    .addEntity(Employee.class
                    );
            query.setCacheable(
                    true);
            query.setCacheRegion(
                    "Employee");
            list = query.list();
        } catch (HibernateException he) {
            System.out.println("" + he.getMessage());
        } finally {
            session.close();
        }
        return list;
    }

    public void countRow() {
        Session session = factory.openSession();
        Transaction tx = null;
        List list;
        try {
            tx = session.beginTransaction();
            Criteria cr = session.createCriteria(Employee.class);
            cr.setProjection(Projections.rowCount());
            list = cr.list();
            result = Integer.valueOf(list.get(0).toString());
        } catch (HibernateException he) {
            System.out.println("" + he.getMessage());
        } finally {
            session.close();
        }
    }

    public int getResult() {
        return result;
    }

    public void setResult(int result) {
        this.result = result;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }

}
