package nsd;

import org.json.simple.*;

public abstract class Request implements JSONAware {//This is The main class for all the Requests
    public abstract Object toJSON();
    public String toJSONString() { return toJSON().toString(); }
    public String toString() { return toJSONString(); }
}


