package mef;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

//idea is our DAL would support
//
//  Computer user = DAL.findById(7);
//  Computer user2 = DAL.find().all().where("size > 10").orderBy("price").asc().get();
//see below
// Part 1 - simple fluid api for calculator
// Part 2 - builds list and executes at end
// Part 3 - mock DAL

import mef.daos.mocks.MockCompanyDAO;
import mef.daos.mocks.MockComputerDAO;
import mef.daos.mocks.MockComputerDAO;
import mef.entities.Company;
import mef.entities.Computer;
import mef.entities.Computer;

import org.junit.Test;

public class FluidTests
{
   
    //======================== PART 3 =================
    //--DAL --------
    public static class ChairWeightComparator implements Comparator<Computer>
    {
        private boolean ascending;
        public ChairWeightComparator(boolean ascending)
        {
            this.ascending = ascending;
        }
        public int compare(Computer chair1, Computer chair2)
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
       
        public List<Computer> apply(MockComputerDAO dal, List<Computer> L)
        {
            List<Computer> outL = new ArrayList<Computer>();
                   
            switch(type)
            {
            case LIKE:
                for(Computer u : dal.all())
                {
                    if (u.name.indexOf(val) >= 0) //later use reflection to find colName!!
                    {
                        outL.add(u);
                    }
                }
                break;
            case ORDER:
                for(Computer u : L)
                {
                    outL.add(u);
                }
                Collections.sort(outL, new ChairWeightComparator(val.equals("asc")));
                break;
            case WHERE:
                for(Computer u : dal.all())
                {
                    if (u.name.equalsIgnoreCase(val))
                    {
                        outL.add(u);
                    }
                }
                break;
            case WHERE_GT:
                for(Computer u : dal.all())
                {
                    if (u.name.compareToIgnoreCase(val) > 0)
                    {
                        outL.add(u);
                    }
                }
                break;
            case WHERE_GE:
                for(Computer u : dal.all())
                {
                    if (u.name.compareToIgnoreCase(val) >= 0)
                    {
                        outL.add(u);
                    }
                }
                break;
            case FETCH:
//                for(Computer u : dal.all())
//                {
//                    if (u.name.compareToIgnoreCase(val) >= 0)
//                    {
//                        outL.add(u);
//                    }
//                }
                break;
            case ALL:
                for(Computer u : dal.all())
                {
                    outL.add(u);
                }
                break;
            }
            return outL;
        }

//        private boolean evalExpr(Computer u, String val2)
//        {
//            return true; //!!
//        }
    }
   
    public static class DalRez
    {
        private ArrayList<DalExpression> opL = new ArrayList<DalExpression>();
        MockComputerDAO dal;
        private MockCompanyDAO companyDAO;
       
        public DalRez(MockComputerDAO dal, MockCompanyDAO companyDAO)
        {
            this.dal = dal;
            this.companyDAO = companyDAO;
        }
       
        public List<Computer> findAll()
        {
            List<Computer> L = new ArrayList<Computer>();
           
            for(DalExpression op : opL)
            {
                L = op.apply(dal, L);
            }
            return L;
        }
        public Computer findUnique()
        {
            List<Computer> L = findAll();
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
        MockComputerDAO dal;
        MockCompanyDAO companyDAO;
       
        public DalCalc(MockComputerDAO dal, MockCompanyDAO companyDAO)
        {
            this.dal = dal;
            this.companyDAO = companyDAO;
        }

        public List<Computer> findAll()
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
        public List<Computer> findAll(DalRez rez)
        {
            return rez.findAll();
        }
        public Computer findUnique(DalRez rez)
        {
            return rez.findUnique();
        }
    }
   
    @Test
    public void testDalCalc0()
    {
        MockComputerDAO dal = new MockComputerDAO();
        MockCompanyDAO companyDAO = new MockCompanyDAO();
        initComputer(dal, 44, "bob");
        assertEquals(1, dal.size());
       
        DalCalc calc = new DalCalc(dal, companyDAO);
        List<Computer>L = calc.findAll();
        assertEquals(1, L.size());
    }
   
    @Test
    public void testDalCalc1()
    {
        MockComputerDAO dal = new MockComputerDAO();
        MockCompanyDAO companyDAO = new MockCompanyDAO();
        initComputer(dal, 44, "bob");
        initComputer(dal, 45, "sob");
        assertEquals(2, dal.size());
       
        DalCalc calc = new DalCalc(dal, companyDAO);
        List<Computer>L = calc.like("name", "bob").findAll();
        assertEquals(1, L.size());
        L = calc.like("name", "ob").findAll();
        assertEquals(2, L.size());
        L = calc.like("name", "zzz").findAll();
        assertEquals(0, L.size());
       
        Computer u = calc.like("name", "bob").findUnique();
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
        MockComputerDAO dal = new MockComputerDAO();
        MockCompanyDAO companyDAO = new MockCompanyDAO();
        initComputer(dal, 44, "bob");
        initComputer(dal, 45, "sob");
        assertEquals(2, dal.size());
       
        DalCalc calc = new DalCalc(dal, companyDAO);
        List<Computer>L = calc.where("name", "bob").findAll();
        assertEquals(1, L.size());
        L = calc.where("name", "ob").findAll();
        assertEquals(0, L.size());
        L = calc.where("name", "zzz").findAll();
        assertEquals(0, L.size());
    }

    @Test
    public void testDalCalcWhere2()
    {
        MockComputerDAO dal = new MockComputerDAO();
        MockCompanyDAO companyDAO = new MockCompanyDAO();
        initComputer(dal, 44, "bob");
        initComputer(dal, 45, "sob");
        assertEquals(2, dal.size());
       
        DalCalc calc = new DalCalc(dal, companyDAO);
        List<Computer>L = calc.where("name").gt("bob").findAll();
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
        MockComputerDAO dal = new MockComputerDAO();
        MockCompanyDAO companyDAO = new MockCompanyDAO();
        initComputer(dal, 45, "sob");
        initComputer(dal, 44, "bob");
        initComputer(dal, 46, "bobby");
        assertEquals(3, dal.size());
       
        DalCalc calc = new DalCalc(dal, companyDAO);
        List<Computer>L = calc.like("name", "ob").orderBy("asc").findAll();
        assertEquals(3, L.size());
        chkL(L, "bob", "bobby", "sob");
       
        L = calc.like("name", "ob").orderBy("desc").findAll();
        assertEquals(3, L.size());
        chkL(L, "sob", "bobby", "bob");
       
        Computer u = calc.like("name", "ob").orderBy("asc").findUnique();
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
       
        DalCalc calc = new DalCalc(dal, companyDAO);
        List<Computer>L = calc.where("name", "bob").findAll();
        assertEquals(1, L.size());
        assertEquals("bob", L.get(0).name);
        assertEquals("def", L.get(0).company.name);
       
       
    }
   
//    @Test
//    public void testDalCalc4()
//    {
//        MockComputerDAO dal = new MockComputerDAO();
//        initComputer(dal, 45, "sob");
//        initComputer(dal, 44, "bob");
//        initComputer(dal, 46, "bobby");
//        assertEquals(3, dal.size());
//       
//        DalCalc calc = new DalCalc(dal);
//        List<Computer>L = calc.getAll().where("id > 0").get();
//        assertEquals(3, L.size());
//        chkL(L, "sob", "bob", "bobby");
//       
//    }
   
    //-------------- helper fns -------------------
    private void chkL(List<Computer> L, String val0, String val1, String val2)
    {
        assertEquals(val0, L.get(0).name);
        assertEquals(val1, L.get(1).name);
        assertEquals(val2, L.get(2).name);
    }
    
    private Company initCompany(MockCompanyDAO dal, long id, String name)
    {
    	Company t = new Company(name);
        t.id = id;
        dal.save(t);
        return t;
    }

    private Computer initComputer(MockComputerDAO dal, long id, String name)
    {
    	return initComputer( dal, id, name, null);
    }
    private Computer initComputer(MockComputerDAO dal, long id, String name, Company company)
    {
    	Computer t = new Computer(name, null, null, company);
        t.id = id;
        dal.save(t);
        return t;
    }
 
}