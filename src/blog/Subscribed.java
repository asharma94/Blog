
package blog;

import com.google.appengine.api.users.User;
import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Id;


@Entity
public class Subscribed {
    @Id Long id;
    User user;
    
    private Subscribed() {
    	
    }


    public Subscribed(User user) {
        this.user = user;
    }

    public User getUser() {
        return user;
    }


}