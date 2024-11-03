package org.example;

public class ServerResponse {
    public enum Type{
        ERR, OK;
    }
    public Type type;
    public String message;

    ServerResponse(Type type, String msg){
        this.type = type;
        this.message = msg;
    }
}
