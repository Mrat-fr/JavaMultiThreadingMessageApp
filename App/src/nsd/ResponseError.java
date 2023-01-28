package nsd;

import org.json.simple.*;

public class ResponseError extends Response {
    private static final String _class = "ErrorResponse";
    private String Error;
    public ResponseError(String Error) {
        if (Error == null)
            throw new NullPointerException();
        this.Error = Error;
    }
    public String getError() {
        return Error;
    }

    @SuppressWarnings("unchecked")
    public Object toJSON() {
        JSONObject obj = new JSONObject();
        obj.put("_class", _class);
        obj.put("Error", Error);
        return obj;
    }

    public static ResponseError fromJSON(Object val) {
        try {
            JSONObject obj = (JSONObject) val;
            if (!_class.equals(obj.get("_class")))
                return null;
            String Error = (String) obj.get("Error");
            return new ResponseError(Error);
        } catch (ClassCastException | NullPointerException e) {
            return null;
        }
    }
}
