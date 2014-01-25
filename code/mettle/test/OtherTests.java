import static org.junit.Assert.*;

import java.io.InputStream;

import org.junit.Test;
import org.mef.framework.sfx.SfxFileUtils;

import tools.BaseTest;


public class OtherTests extends BaseTest
{

	@Test
	public void test() throws Exception 
	{
		String dir = this.getCurrentDirectory();
		log(dir);
		dir = dir.replace("\\.", "");
		log(dir);
		
		//test using \ as separater
		SfxFileUtils utils = new SfxFileUtils();
		String path = utils.PathCombine(dir, "test");
		path = utils.PathCombine(path, "OtherTests.java");
		log(path);
		assertTrue(utils.fileExists(path));
		
		//test using / as separater
		log("now /");
		dir = dir.replace('\\', '/');
		log(dir);
		path = utils.PathCombine(dir, "test");
		path = utils.PathCombine(path, "OtherTests.java");
		log(path);
		assertTrue(utils.fileExists(path));
		
		log("and resources..");
		String baseDir = "mgen/resources/presenter/reply.stg";
		InputStream stream = this.getClass().getResourceAsStream(baseDir);
		assertNull(stream); //not available in unit tests?
	}

}
