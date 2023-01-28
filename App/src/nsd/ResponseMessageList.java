package nsd;

import java.util.*;
import org.json.simple.*;

public class ResponseMessageList extends Response {
    private static final String _class = "MessageListResponse";

    private List<Message> Messages;
    private String Type;

    public ResponseMessageList(List<Message> Messages, String type){
        if (Messages == null || Messages.contains(null))
            throw new NullPointerException();
        this.Messages = Messages;
        this.Type = type;
    }

    List<Message> getMessages() {
        return Messages;
    }
    String getType() {
        return Type;
    }

    @SuppressWarnings("unchecked")
    public Object toJSON(){
        JSONArray AllMess = new JSONArray();
        for (Message msg : Messages)
            AllMess.add(msg.toJSON());
        JSONObject obj = new JSONObject();
        obj.put("_class",_class);
        obj.put("Messages",AllMess);
        obj.put("Type",Type);
        return obj;
    }

    public static ResponseMessageList fromJSON(Object val){
        try{
            JSONObject obj = (JSONObject) val;
            if(!_class.equals(obj.get("_class")))
                return null;
            JSONArray AllMess = (JSONArray)obj.get("Messages");
            List<Message> Messages = new ArrayList<>();;
            for (Object msg_obj : AllMess)
                Messages.add(Message.fromJSON(msg_obj));
            String Type = (String) obj.get("Type");
            return new ResponseMessageList(Messages, Type);
        }catch (ClassCastException| NullPointerException e){
            return null;
        }
    }
}