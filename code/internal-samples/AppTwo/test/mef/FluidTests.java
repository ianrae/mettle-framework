package mef;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

//idea is our DAL would support
//
//  User user = DAL.findById(7);
//  User user2 = DAL.find().all().where("size > 10").orderBy("price").asc().get();
//see below
// Part 1 - simple fluid api for calculator
// Part 2 - builds list and executes at end
// Part 3 - mock DAL

import mef.daos.mocks.MockCompanyDAO;
import mef.daos.mocks.MockComputerDAO;
import mef.daos.mocks.MockUserDAO;
import mef.entities.Company;
import mef.entities.Computer;
import mef.entities.User;

import org.junit.Test;

public class FluidTests
{
   
    //======================== PART 3 =================
    //--DAL --------
    public static class ChairWeightComparator implements Comparator<User>
    {
        private boolean ascending;
        public ChairWeightComparator(boolean ascending)
        {
            this.ascending = ascending;
        }
        public int compare(User chair1, User chair2)
        {
            if (ascending)
            {
                return chair1.name.compareTo(chair2.name);
            }
            else
            {
                return chair2.name.compareTo(chair1.name);
            }
        }
    }   
   
    public static class DalExpression
    {
        public static final int LIKE = 1;
        public static final int ORDER = 2;
        public static final int WHERE = 3;
        public static final int ALL = 4;
        public static final int WHERE_GT = 5;
        public static final int WHERE_GE = 6;
        public static final int FETCH = 7;
       
        //actually FETCH not needed because entities already ready have their assoc populated
        //and the other objects are already in their DAL
       
        int type; //1=+,2=-,3=*,4=/
        String colName;
        String val;

        public DalExpression(int type, String colName, String val)
        {
            this.type = type;
            this.colName = colName;
            this.val = val;
        }
       
        public List<User> apply(MockUserDAO dal, List<User> L)
        {
            List<User> outL = new ArrayList<User>();
                   
            switch(type)
            {
            case LIKE:
                for(User u : dal.all())
                {
                    if (u.name.indexOf(val) >= 0) //later use reflection to find colName!!
                    {
                        outL.add(u);
                    }
                }
                break;
            case ORDER:
                for(User u : L)
                {
                    outL.add(u);
                }
                Collections.sort(outL, new ChairWeightComparator(val.equals("asc")));
                break;
            case WHERE:
                for(User u : dal.all())
                {
                    if (u.name.equalsIgnoreCase(val))
                    {
                        outL.add(u);
                    }
                }
                break;
            case WHERE_GT:
                for(User u : dal.all())
                {
                    if (u.name.compareToIgnoreCase(val) > 0)
                    {
                        outL.add(u);
                    }
                }
                break;
            case WHERE_GE:
                for(User u : dal.all())
                {
                    if (u.name.compareToIgnoreCase(val) >= 0)
                    {
                        outL.add(u);
                    }
                }
                break;
            case FETCH:
//                for(User u : dal.all())
//                {
//                    if (u.name.compareToIgnoreCase(val) >= 0)
//                    {
//                        outL.add(u);
//                    }
//                }
                break;
            case ALL:
                for(User u : dal.all())
                {
                    outL.add(u);
                }
                break;
            }
            return outL;
        }

//        private boolean evalExpr(User u, String val2)
//        {
//            return true; //!!
//        }
    }
   
    public static class DalRez
    {
        private ArrayList<DalExpression> opL = new ArrayList<DalExpression>();
        MockUserDAO dal;
        private MockCompanyDAO companyDAO;
       
        public DalRez(MockUserDAO dal, MockCompanyDAO companyDAO)
        {
            this.dal = dal;
            this.companyDAO = companyDAO;
        }
       
        public List<User> findAll()
        {
            List<User> L = new ArrayList<User>();
           
            for(DalExpression op : opL)
            {
                L = op.apply(dal, L);
            }
            return L;
        }
        public User findUnique()
        {
            List<User> L = findAll();
            if (L.size() == 0)
            {
                return null;
            }
            return L.get(0);
        }
       
       
        //fluid API
        public DalRez like(String colName, String val)
        {
            opL.add(new DalExpression(DalExpression.LIKE, colName, val));
            return this;
        }
        public DalRez where(String colName, String val)
        {
            opL.add(new DalExpression(DalExpression.WHERE, colName, val));
            return this;
        }
        public DalRez gt(String val)
        {
            replacePrevious(DalExpression.WHERE_GT, val);
            return this;
        }
        public DalRez ge(String val)
        {
            replacePrevious(DalExpression.WHERE_GE, val);
            return this;
        }
        private void replacePrevious(int type, String val)
        {
            DalExpression prev = opL.get(opL.size() - 1);
            if (prev.type == DalExpression.WHERE)
            {
                opL.remove(prev);
                opL.add(new DalExpression(type, prev.colName, val));
            }
        }
       
        public DalRez fetch(String colName)
        {
            opL.add(new DalExpression(DalExpression.FETCH, colName, ""));
            return this;
        }
       
       
        public DalRez orderBy(String orderType)
        {
            boolean asc = (orderType.trim().toLowerCase() == "asc");
            opL.add(new DalExpression(2, "", (asc) ? "asc" : "desc"));
            return this;
        }
//        public DalRez where(String expr)
//        {
//            opL.add(new DalExpression(3, "", expr));
//            return this;
//        }
       
        //not public
        DalRez all()
        {
            opL.add(new DalExpression(4, "", ""));
            return this;
        }
    }
    public static class DalCalc
    {
        MockUserDAO dal;
        MockCompanyDAO companyDAO;
       
        public DalCalc(MockUserDAO dal, MockCompanyDAO companyDAO)
        {
            this.dal = dal;
            this.companyDAO = companyDAO;
        }

        public List<User> findAll()
        {
            DalRez rez = new DalRez(dal, companyDAO);
            rez = rez.all();
            return rez.findAll();
        }
        public DalRez like(String colName, String val)
        {
            DalRez rez = new DalRez(dal, companyDAO);
            rez = rez.like(colName, val);
            return rez;
        }
        public DalRez where(String colName, String val)
        {
            DalRez rez = new DalRez(dal, companyDAO);
            rez = rez.where(colName, val);
            return rez;
        }
        public DalRez where(String colName)
        {
            DalRez rez = new DalRez(dal, companyDAO);
            rez = rez.where(colName, "");
            return rez;
        }
        public DalRez fetch(String colName)
        {
            DalRez rez = new DalRez(dal, companyDAO);
            rez = rez.fetch(colName);
            return rez;
        }
        public List<User> findAll(DalRez rez)
        {
            return rez.findAll();
        }
        public User findUnique(DalRez rez)
        {
            return rez.findUnique();
        }
    }
   
    @Test
    public void testDalCalc0()
    {
        MockUserDAO dal = new MockUserDAO();
        MockCompanyDAO companyDAO = new MockCompanyDAO();
        initUser(dal, 44, "bob");
        assertEquals(1, dal.size());
       
        DalCalc calc = new DalCalc(dal, companyDAO);
        List<User>L = calc.findAll();
        assertEquals(1, L.size());
    }
   
    @Test
    public void testDalCalc1()
    {
        MockUserDAO dal = new MockUserDAO();
        MockCompanyDAO companyDAO = new MockCompanyDAO();
        initUser(dal, 44, "bob");
        initUser(dal, 45, "sob");
        assertEquals(2, dal.size());
       
        DalCalc calc = new DalCalc(dal, companyDAO);
        List<User>L = calc.like("name", "bob").findAll();
        assertEquals(1, L.size());
        L = calc.like("name", "ob").findAll();
        assertEquals(2, L.size());
        L = calc.like("name", "zzz").findAll();
        assertEquals(0, L.size());
       
        User u = calc.like("name", "bob").findUnique();
        assertEquals("bob", u.name);
        u = calc.like("name", "ob").findUnique();
        assertEquals("bob", u.name);
        u = calc.like("name", "sob").findUnique();
        assertEquals("sob", u.name);
        u = calc.like("name", "zzz").findUnique();
        assertEquals(null, u);
    }

    @Test
    public void testDalCalcWhere()
    {
        MockUserDAO dal = new MockUserDAO();
        MockCompanyDAO companyDAO = new MockCompanyDAO();
        initUser(dal, 44, "bob");
        initUser(dal, 45, "sob");
        assertEquals(2, dal.size());
       
        DalCalc calc = new DalCalc(dal, companyDAO);
        List<User>L = calc.where("name", "bob").findAll();
        assertEquals(1, L.size());
        L = calc.where("name", "ob").findAll();
        assertEquals(0, L.size());
        L = calc.where("name", "zzz").findAll();
        assertEquals(0, L.size());
    }

    @Test
    public void testDalCalcWhere2()
    {
        MockUserDAO dal = new MockUserDAO();
        MockCompanyDAO companyDAO = new MockCompanyDAO();
        initUser(dal, 44, "bob");
        initUser(dal, 45, "sob");
        assertEquals(2, dal.size());
       
        DalCalc calc = new DalCalc(dal, companyDAO);
        List<User>L = calc.where("name").gt("bob").findAll();
        assertEquals(1, L.size());
        assertEquals("sob", L.get(0).name);
       
        L = calc.where("name").ge("bob").findAll();
        assertEquals(2, L.size());
        assertEquals("bob", L.get(0).name);
        assertEquals("sob", L.get(1).name);
       
    }
   
    @Test
    public void testDalCalc3()
    {
        MockUserDAO dal = new MockUserDAO();
        MockCompanyDAO companyDAO = new MockCompanyDAO();
        initUser(dal, 45, "sob");
        initUser(dal, 44, "bob");
        initUser(dal, 46, "bobby");
        assertEquals(3, dal.size());
       
        DalCalc calc = new DalCalc(dal, companyDAO);
        List<User>L = calc.like("name", "ob").orderBy("asc").findAll();
        assertEquals(3, L.size());
        chkL(L, "bob", "bobby", "sob");
       
        L = calc.like("name", "ob").orderBy("desc").findAll();
        assertEquals(3, L.size());
        chkL(L, "sob", "bobby", "bob");
       
        User u = calc.like("name", "ob").orderBy("asc").findUnique();
        assertEquals("bob", u.name);
    }

    @Test
    public void testDalCalcFetch()
    {
        MockComputerDAO dal = new MockComputerDAO();
        MockCompanyDAO companyDAO = new MockCompanyDAO();
        
        initCompany(companyDAO, 10, "abc");
        initCompany(companyDAO, 11, "def");
        assertEquals(2, companyDAO.size());
        
        initComputer(dal, 1, "andy", companyDAO.findById(10));
        initComputer(dal, 2, "bob", companyDAO.findById(11));
        assertEquals(2, dal.size());
       
//        DalCalc calc = new DalCalc(dal, companyDAO);
//        List<User>L = calc.where("name", "bob").fetch("company").findAll();
//        assertEquals(1, L.size());
//        assertEquals("sob", L.get(0).name);
       
       
    }
   
//    @Test
//    public void testDalCalc4()
//    {
//        MockUserDAO dal = new MockUserDAO();
//        initUser(dal, 45, "sob");
//        initUser(dal, 44, "bob");
//        initUser(dal, 46, "bobby");
//        assertEquals(3, dal.size());
//       
//        DalCalc calc = new DalCalc(dal);
//        List<User>L = calc.getAll().where("id > 0").get();
//        assertEquals(3, L.size());
//        chkL(L, "sob", "bob", "bobby");
//       
//    }
   
    //-------------- helper fns -------------------
    private void chkL(List<User> L, String val0, String val1, String val2)
    {
        assertEquals(val0, L.get(0).name);
        assertEquals(val1, L.get(1).name);
        assertEquals(val2, L.get(2).name);
    }
    private User initUser(MockUserDAO dal, long id, String name)
    {
        User t = new User();
        t.id = id;
        t.name = name;
        dal.save(t);
        return t;
    }
    
    private Company initCompany(MockCompanyDAO dal, long id, String name)
    {
    	Company t = new Company(name);
        t.id = id;
        dal.save(t);
        return t;
    }

    private Computer initComputer(MockComputerDAO dal, long id, String name, Company company)
    {
    	Computer t = new Computer(name, null, null, company);
        t.id = id;
        dal.save(t);
        return t;
    }
 
}