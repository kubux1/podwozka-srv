package pl.edu.podwozka.podwozkasrv;

import org.springframework.stereotype.Component;

import java.io.PrintStream;

@Component
public class Greeter {
    public void greet(PrintStream to, String name) {
        to.println(createGreeting(name));
    }

    public String createGreeting(String name) {
        return "Hello, " + name + "!";
    }
}
