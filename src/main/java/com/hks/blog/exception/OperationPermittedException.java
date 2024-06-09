package com.hks.blog.exception;

public class OperationPermittedException extends RuntimeException{
    public OperationPermittedException(String msg){
        super(msg);
    }
}
