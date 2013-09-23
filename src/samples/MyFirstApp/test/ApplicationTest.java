import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.codehaus.jackson.JsonNode;
import org.junit.*;

import play.mvc.*;
import play.test.*;
// import play.data.DynamicForm;
// import play.data.validation.ValidationError;
// import play.data.validation.Constraints.RequiredValidator;
import play.data.*;
import models.*;

import play.i18n.Lang;
import play.libs.F;
import play.libs.F.*;

import static play.test.Helpers.*;
import static org.fest.assertions.Assertions.*;


/**
*
* Simple (JUnit) tests that can call all parts of a play app.
* If you are interested in mocking a whole application, see the wiki for more details.
*
*/
public class ApplicationTest {

    @Test 
    public void simpleCheck() {
        int a = 1 + 1;
        assertThat(a).isEqualTo(2);
    }
    
    @Test
    public void renderTemplate() {
         Form<Task> taskForm = Form.form(Task.class);  

         List<Task> emptyList = new ArrayList<Task>();
          Content html = views.html.index.render(emptyList, taskForm); //"Your new application is ready.");
        assertThat(contentType(html)).isEqualTo("text/html");
        assertThat(contentAsString(html)).contains("Addxxx a new task");
    }
  
   
}
