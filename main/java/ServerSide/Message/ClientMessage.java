package ServerSide.Message;

import Client.Account;

import java.io.Serializable;
import java.time.Clock;
import java.time.Instant;
import java.util.Date;

/**
 * Created by derkote on 05.10.2016.
 */
public class ClientMessage extends AbstractMessage implements Serializable {

    public ClientMessage(Account account) {
        this.account = account;

        Clock clock = Clock.systemDefaultZone();
        Instant instant = clock.instant();
        date = Date.from(instant);
    }

    public ClientMessage(Account account, String message) {
        this(account);
        this.message = message;
    }
    public String getNickname() {
        return account.getNickName();
    }

}
