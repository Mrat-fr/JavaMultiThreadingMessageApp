package nsd;

import org.json.simple.*;
public class ResponseSuccess extends Response {
    private static final String _class = "Success";

    public ResponseSuccess() {
        System.out.println("Success");
    }

    @SuppressWarnings("unchecked")
    public Object toJSON() {
        JSONObject obj = new JSONObject();
        obj.put("_class", _class);
        return obj;
    }

    public static ResponseSuccess fromJSON(Object val) {
        try {
            JSONObject obj = (JSONObject) val;
            if (!_class.equals(obj.get("_class")))
                return null;
            return new ResponseSuccess();
        } catch (ClassCastException | NullPointerException e) {
            return null;
        }
    }
}