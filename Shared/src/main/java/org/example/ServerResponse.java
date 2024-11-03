package org.example;

import java.io.Serial;
import java.io.Serializable;

public class ServerResponse implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;
    public enum Type{
        ERR, INFO, DATA;
    }
    public Type type;
    public String message;
    public Object data;

    public ServerResponse(Type type, String msg){
        this.type = type;
        this.message = msg;
    }

    public ServerResponse(Object data){
        this.type = Type.DATA;
        this.data = data;
    }
}
