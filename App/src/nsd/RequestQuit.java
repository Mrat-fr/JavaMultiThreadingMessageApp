package nsd;

import org.json.simple.JSONObject;

public class RequestQuit extends Request {
    private static final String _class = "QuitRequest";
    public RequestQuit() {}

    @SuppressWarnings("unchecked")
    public Object toJSON() {
        JSONObject obj = new JSONObject();
        obj.put("_class", _class);
        return obj;
    }

    public static RequestQuit fromJSON(Object val) {
        try {
            JSONObject obj = (JSONObject)val;
            if (!_class.equals(obj.get("_class")))
                return null;
            return new RequestQuit();
        } catch (ClassCastException | NullPointerException e) {
            return null;
        }
    }
}