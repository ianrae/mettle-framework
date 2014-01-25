import mef.core.MettleInitializer;
import play.*;


public class Global extends GlobalSettings 
{

    public void onStart(Application app) 
    {
        Logger.info("Application has started. Mettle version " + org.mef.framework.Version.version);
        MettleInitializer initializer = new MettleInitializer();
        initializer.createContext();
        initializer.onStart();
    }

    public void onStop(Application app) 
    {
        Logger.info("Application shutdown...");
    }

}
