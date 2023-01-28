package nsd;

import org.json.simple.*;

public class RequestOpen extends Request { //login and create message board
    private static final String _class = "OpenRequest";
    private String Identity;

    public RequestOpen(String Identity) {
        if (Identity == null)
            new NullPointerException();
        this.Identity = Identity;
    }

    public String getIdentity() {
        return Identity;
    }

    @SuppressWarnings("unchecked")
    public Object toJSON() {
        JSONObject obj = new JSONObject();
        obj.put("_class", _class);
        obj.put("Identity", Identity);
        return obj;
    }

    public static RequestOpen fromJSON(Object val) {
        try {
            JSONObject obj = (JSONObject) val;
            if (!_class.equals(obj.get("_class")))
                return null;
            String Identity = (String) obj.get("Identity");
            return new RequestOpen(Identity);
        } catch (ClassCastException | NullPointerException e) {
            return null;
        }
    }
}
