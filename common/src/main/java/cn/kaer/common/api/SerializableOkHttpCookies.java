package cn.kaer.common.api;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

import okhttp3.Cookie;


public class SerializableOkHttpCookies implements Serializable {
    private final transient Cookie cookies;
    private transient Cookie clientCookies;

    public SerializableOkHttpCookies(Cookie cookies)
    {
        this.cookies = cookies;
    }

    public Cookie getCookies()
    {
        Cookie bestCookies = this.cookies;
        if (this.clientCookies != null) {
            bestCookies = this.clientCookies;
        }
        return bestCookies;
    }

    private void writeObject(ObjectOutputStream out)
            throws IOException
    {
        out.writeObject(this.cookies.name());
        out.writeObject(this.cookies.value());
        out.writeLong(this.cookies.expiresAt());
        out.writeObject(this.cookies.domain());
        out.writeObject(this.cookies.path());
        out.writeBoolean(this.cookies.secure());
        out.writeBoolean(this.cookies.httpOnly());
        out.writeBoolean(this.cookies.hostOnly());
        out.writeBoolean(this.cookies.persistent());
    }

    private void readObject(ObjectInputStream in)
            throws IOException, ClassNotFoundException
    {
        String name = (String)in.readObject();
        String value = (String)in.readObject();
        long expiresAt = in.readLong();
        String domain = (String)in.readObject();
        String path = (String)in.readObject();
        boolean secure = in.readBoolean();
        boolean httpOnly = in.readBoolean();
        boolean hostOnly = in.readBoolean();
        boolean persistent = in.readBoolean();
        Cookie.Builder builder = new Cookie.Builder();
        builder = builder.name(name);
        builder = builder.value(value);
        builder = builder.expiresAt(expiresAt);
        builder = hostOnly ? builder.hostOnlyDomain(domain) : builder.domain(domain);
        builder = builder.path(path);
        builder = secure ? builder.secure() : builder;
        builder = httpOnly ? builder.httpOnly() : builder;
        this.clientCookies = builder.build();
    }
}
