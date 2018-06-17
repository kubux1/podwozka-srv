package pl.edu.podwoozka.app;

import pl.edu.podwoozka.rest.MessageRestService;

import java.util.HashSet;
import java.util.Set;

import javax.ws.rs.core.Application;


public class MessageApplication extends Application {
    private Set<Object> singletons = new HashSet<>();

    public MessageApplication() {
        singletons.add(new MessageRestService());
    }

    @Override
    public Set<Object> getSingletons() {
        return singletons;
    }
}
