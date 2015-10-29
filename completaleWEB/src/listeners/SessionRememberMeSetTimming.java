package listeners;

import javax.servlet.annotation.WebListener;
import javax.servlet.http.HttpSessionAttributeListener;
import javax.servlet.http.HttpSessionBindingEvent;

@WebListener
public class SessionRememberMeSetTimming implements HttpSessionAttributeListener {

    public SessionRememberMeSetTimming() {
    }

    public void attributeAdded(HttpSessionBindingEvent event)  { 
    	if((event.getName().equals("rememberMe")==true)&&((boolean)event.getValue()==true)){
    		event.getSession().setMaxInactiveInterval(43200);
    	}
    }

    public void attributeRemoved(HttpSessionBindingEvent event)  { 
    }

    public void attributeReplaced(HttpSessionBindingEvent event)  { 
    }
	
}
