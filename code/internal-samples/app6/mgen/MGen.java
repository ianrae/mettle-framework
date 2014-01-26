import static org.junit.Assert.*;

import org.junit.Test;
import org.mef.tools.mgen.codegen.MGenBase;


public class MGen extends MGenBase
{
    @Test
    public void test() throws Exception 
    {
    	this.useNewImpl = true;
        this.runCodeGeneration();
    }
}