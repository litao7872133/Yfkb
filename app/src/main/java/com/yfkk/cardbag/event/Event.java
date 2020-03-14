package com.yfkk.cardbag.event;

import java.util.Map;

public class Event {
    public int code;
    public String message;
    public Object object;
    public Map map;

    public Event(int code) {
        this.code = code;
    }

    public Event(int code, Object object) {
        this.code = code;
        this.object = object;
    }

    public Event(int code, String message) {
        this.code = code;
        this.message = message;
    }

    public Event(int code, Map map) {
        this.code = code;
        this.map = map;
    }

    public Event(int code, String message, Object object) {
        this.code = code;
        this.message = message;
        this.object = object;
    }

}