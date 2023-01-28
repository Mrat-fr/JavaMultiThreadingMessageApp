package nsd;

import org.json.simple.*;
public class RequestPublish extends Request {
    private static final String _class = "PublishRequest";
    private String Message;
    private String Identity;

    public RequestPublish(String Message, String Identity) {
        if (Message == null)
            new NullPointerException();
        this.Message = Message;
        this.Identity = Identity;
    }

    public String getMessage() {
        return Message;
    }

    @SuppressWarnings("unchecked")
    public Object toJSON() {
        JSONObject obj = new JSONObject();
        obj.put("_class", _class);
        obj.put("Message", Message);
        obj.put("Identity", Identity);
        return obj;
    }

    public static RequestPublish fromJSON(Object val) {
        try {
            JSONObject obj = (JSONObject) val;
            if (!_class.equals(obj.get("_class")))
                return null;
            String Message = (String) obj.get("Message");
            String Identity = (String) obj.get("Identity");
            return new RequestPublish(Message, Identity);
        } catch (ClassCastException | NullPointerException e) {
            return null;
        }
    }
}