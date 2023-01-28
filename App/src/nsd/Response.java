package nsd;

import org.json.simple.*;
public abstract class Response implements JSONAware {//This is The main class for all the responses
    public abstract Object toJSON();
    public String toJSONString() { return toJSON().toString(); }
    public String toString() { return toJSONString(); }
}



