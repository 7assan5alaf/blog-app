package com.hks.blog.exception;

public class EntityNotFound extends  RuntimeException{
    public EntityNotFound(String msg){
        super(msg);
    }
}
