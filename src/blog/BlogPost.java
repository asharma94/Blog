
package blog;
import java.util.Calendar;
import java.util.Date;

 

import java.util.GregorianCalendar;

import com.google.appengine.api.users.User;
import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Id;


@Entity
public class BlogPost implements Comparable<BlogPost> {
    @Id Long id;
    User user;
    String title;
    String content;
    Date date;
    
    private BlogPost() {}

    public BlogPost(User user, String title, String content) {
        this.user = user;
        this.title = title;
        this.content = content;
        date = new Date();
    }

    public User getUser() {
        return user;
    }

    public String getContent() {
        return content;
    }
    
    public String getTitle() {
        return title;
    }

    @Override

    public int compareTo(BlogPost other) {

        if (date.after(other.date)) {
            return 1;
        } else if (date.before(other.date)) {
            return -1;
        }
        return 0;
    }
    
    public boolean compareDate(Date d) {
    	GregorianCalendar gc = new GregorianCalendar();
    	gc.setTime(d);
    	gc.add(Calendar.DAY_OF_YEAR, -1);
    	Date result = gc.getTime();
    	
    	if(date.after(d) && date.before(result)) return true;
    	else return false;
    }
    

}