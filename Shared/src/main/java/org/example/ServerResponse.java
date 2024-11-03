package org.example;

import java.io.Serial;
import java.io.Serializable;

public class ServerResponse implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;
    public enum Type{
        ERR, INFO;
    }
    public Type type;
    public String message;

    public ServerResponse(Type type, String msg){
        this.type = type;
        this.message = msg;
    }
}
