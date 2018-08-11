package pl.edu.podwozka.podwozkasrv.example;

import org.springframework.stereotype.Component;

import javax.inject.Inject;
import java.io.PrintStream;

@Component
public class Greeter {

    private PhraseBuilder phraseBuilder;

    @Inject
    public Greeter(PhraseBuilder phraseBuilder) {
        this.phraseBuilder = phraseBuilder;
    }

    public void greet(PrintStream to, String name) {
        to.println(createGreeting(name));
    }

    public String createGreeting(String name) {
        return phraseBuilder.buildPhrase("hello", name);
    }
}
