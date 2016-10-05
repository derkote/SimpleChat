package Client;

import java.io.Serializable;
import java.time.Clock;
import java.time.Instant;
import java.util.Date;

/**
 * Created by derkote on 05.10.2016.
 */
public class Message implements Serializable {

    String message;
    Account account;
    Date date;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Message(Account account) {
        this.account = account;

        Clock clock = Clock.systemDefaultZone();
        Instant instant = clock.instant();
        date = Date.from(instant);
    }


}
