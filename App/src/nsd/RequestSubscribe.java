package nsd;

import org.json.simple.JSONObject;

public class RequestSubscribe extends Request {
    private static final String _class = "SubscribeRequest";
    private String Channel;
    private String Identity;


    public RequestSubscribe(String Channel, String Identity) {
        if (Channel == null) {
            new NullPointerException();
        }
        this.Channel = Channel;
        this.Identity = Identity;
    }

    public String getChannel() {return Channel;}

    @SuppressWarnings("unchecked")
    public Object toJSON() {
        JSONObject obj = new JSONObject();
        obj.put("_class", _class);
        obj.put("Channel", Channel);
        obj.put("Identity", Identity);
        return obj;
    }

    public static RequestSubscribe fromJSON(Object val) {
        try {
            JSONObject obj = (JSONObject) val;
            if (!_class.equals(obj.get("_class")))
                return null;
            String Channel = (String) obj.get("Channel");
            String Identity = (String) obj.get("Identity");
            return new RequestSubscribe( Channel, Identity);
        } catch (ClassCastException | NullPointerException e) {
            return null;
        }
    }
}