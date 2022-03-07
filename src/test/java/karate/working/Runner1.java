package karate.working;

import com.intuit.karate.junit5.Karate;

public class Runner1 {
    @Karate.Test
    Karate testAll() {
        return Karate.run().relativeTo(getClass());
    }
}
