//THIS FILE HAS BEEN AUTO-GENERATED. DO NOT MODIFY.

package mef.gen;

import org.mef.framework.entities.Entity;
import mef.gen.*;
import mef.entities.*;
import java.util.Date;
public class User_GEN extends Entity
{

        public User_GEN()
        {
        }

        public User_GEN( String name)
        {
                this.name = name;
        }

        public User_GEN(User_GEN entity)
        {
                this.id = entity.id;
                this.name = entity.name;
        }
    public Long id;

    public String name;

}
