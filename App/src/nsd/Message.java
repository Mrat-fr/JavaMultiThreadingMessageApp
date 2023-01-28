package nsd;

import org.json.simple.*;
public class Message {
    private static final String _class = Message.class.getSimpleName();//name used for _class
    private final String From;
    private final String Body;
    private final long When;

    public Message(String From, long TimeStamp, String Body){//initialise values of class
        if (Body ==null || From == null)
            new NullPointerException();
        this.From = From;
        this.When = TimeStamp;
        this.Body = Body;
    }

    public String getFrom() {return From;}//getters for when needed
    public long getTimeStamp() {return When;}
    public String getBody() {return Body;}
    public String toString() {return From + " (" + When + "): " + Body ;}//message display to clients

    // Serializes this object into a JSONObject for storage
    @SuppressWarnings("unchecked")
    public Object toJSON(){
        JSONObject obj = new JSONObject();
        obj.put("_class",_class);
        obj.put("From",From);
        obj.put("When",When);
        obj.put("Body",Body);
        return obj;
    }

    //deserialize object to be used
    public static Message fromJSON(Object val){
        try{
            JSONObject obj = (JSONObject) val;
            if(!_class.equals(obj.get("_class")))
                return null;
            String From = (String)obj.get("From");
            long When = (long)obj.get("When");
            String Body = (String)obj.get("Body");
            return  new Message(From, When, Body);
        }catch (ClassCastException| NullPointerException e){
            return null;
        }
    }
}
