import java.util.List;

import mef.core.MettleInitializer;
import mef.core.UserEbeanQueryProcessor;
import mef.daos.IUserDAO;
import mef.entities.User;
import models.UserModel;

import org.junit.*;
import org.mef.framework.sfx.SfxContext;

import play.mvc.*;
import play.test.*;
import play.libs.F.*;

import static play.test.Helpers.*;
import static org.fest.assertions.Assertions.*;

import static org.fluentlenium.core.filter.FilterConstructor.*;

public class IntegrationTest {

    /**
     * add your integration test here
     * in this example we just check if the welcome page is being shown
     */
    @Test
    public void test() {
    	log("starttest 0");
    	Global global = new Global();
        running(testServer(3333, fakeApplication(inMemoryDatabase(), global)), HTMLUNIT, new Callback<TestBrowser>() {
            public void invoke(TestBrowser browser) {
            	log("starttest 1");
                browser.goTo("http://localhost:3333");
                assertThat(browser.pageSource()).contains("app6: bob");
                
                log("xx");
                List<UserModel> all = UserModel.find.all();
                log(String.format("usermodel: count %d", all.size()));
                
                buildTable();
                
                log("xx2");
                all = UserModel.find.all();
                log(String.format("usermodel: count %d", all.size()));
                for(UserModel m : all)
                {
                	log(String.format("%d: %s", m.getId(), m.getName()));
                }
                
                SfxContext ctx = MettleInitializer.theCtx;
//                UserEbeanQueryProcessor qproc = new UserEbeanQueryProcessor(ctx);
                
                log("dao");
                IUserDAO dao = (IUserDAO) MettleInitializer.getDAO(IUserDAO.class);
                assertThat(dao).isNotNull();
                
                List<User> userlist = dao.query().findMany();
                dumpList(userlist, "", 3);
                
        		userlist = dao.query().where("name").eq("joe").findMany();
        		dumpList(userlist, "2", 1);

        		userlist = dao.query().where("name").eq("nobody").findMany();
        		dumpList(userlist, "0", 0);

        		userlist = dao.query().where("name").eq("joe").or("name").eq("billy").findMany();
        		dumpList(userlist, "3", 2);

        		userlist = dao.query().where("name").le("billy").findMany();
        		dumpList(userlist, "4", 2);

        		userlist = dao.query().orderBy("name", "desc").findMany();
        		dumpList(userlist, "5", 3);
            }
        });
    }

    private void dumpList(List<User> userlist, String prefix, int expectedSize)
    {
	    log(String.format("userlist%s: count %d", prefix, userlist.size()));
	    for(User u: userlist)
	    {
	    	log(String.format("%d: %s", u.id, u.name));
	    }
	    assertThat(userlist.size()).isEqualTo(expectedSize);
    }
    
    private void buildTable()
    {
        UserModel m = new UserModel();
        m.setName("billy");
        m.save();
        m = new UserModel();
        m.setName("joe");
        m.save();
        m = new UserModel();
        m.setName("ally");
        m.save();
  	
    }
    
    //--helpers--
    private void log(String s)
    {
    	System.out.println(s);
    }

}
