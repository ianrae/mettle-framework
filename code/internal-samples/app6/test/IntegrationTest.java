import java.util.List;

import models.UserModel;

import org.junit.*;

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
                
                UserModel m = new UserModel();
                m.setName("billy");
                m.save();

                log("xx2");
                all = UserModel.find.all();
                log(String.format("usermodel: count %d", all.size()));
                
            }
        });
    }
    
    //--helpers--
    private void log(String s)
    {
    	System.out.println(s);
    }

}
