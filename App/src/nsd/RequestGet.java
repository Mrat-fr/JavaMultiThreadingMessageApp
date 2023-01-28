package nsd;

import org.json.simple.*;
public class RequestGet extends Request {
    private static final String _class = "GetRequest";

    private String After;
    private String Identity;


    public RequestGet(String After, String Identity) {
        this.After = After;
        this.Identity = Identity;
    }
    public String getAfter() {return After;}

    @SuppressWarnings("unchecked")
    public Object toJSON() {
        JSONObject obj = new JSONObject();
        obj.put("_class", _class);
        obj.put("After", After);
        obj.put("Identity", Identity);
        return obj;
    }
   // public int getAfter() {return After;}

    public static RequestGet fromJSON(Object val) {
        try {
            JSONObject obj = (JSONObject) val;
            if (!_class.equals(obj.get("_class")))
                return null;
            String After = (String) obj.get("After");
            String Identity = (String) obj.get("Identity");
            return new RequestGet(After, Identity);
        } catch (ClassCastException | NullPointerException e) {
            return null;
        }
    }
}
