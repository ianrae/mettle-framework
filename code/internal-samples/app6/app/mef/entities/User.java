//THIS FILE HAS BEEN AUTO-GENERATED. DO NOT MODIFY.

package mef.entities;

import org.mef.framework.entities.Entity;
import mef.gen.*;
import models.*;
import mef.entities.*;
import java.util.Date;
public class User extends User_GEN
{

        public User()
        {
			super();
        }
        public User(UserModel model)
        {
			super(model);
        }


        public User( String name)
        {
                this.setName(name);
        }

        public User(User entity)
        {
                this.setId(entity.getId());
                this.setName(entity.getName());
        }
}
