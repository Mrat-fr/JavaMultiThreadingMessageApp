package nsd;

import org.json.simple.JSONObject;

public class RequestSearch extends Request {
    private static final String _class = "SearchRequest";

    private String Search;
    private String Identity;
    public RequestSearch(String Search, String Identity) {
        if (Search == null) {
            new NullPointerException();
        }
        this.Search = Search;
        this.Identity = Identity;
    }

    @SuppressWarnings("unchecked")
    public Object toJSON() {
        JSONObject obj = new JSONObject();
        obj.put("_class", _class);
        obj.put("Search", Search);
        obj.put("Identity", Identity);
        return obj;
    }
    public String getSearch() {
        return Search;
    }

    public static RequestSearch fromJSON(Object val) {
        try {
            JSONObject obj = (JSONObject) val;
            if (!_class.equals(obj.get("_class")))
                return null;
            String Search = (String) obj.get("Search");
            String Identity = (String) obj.get("Identity");
            return new RequestSearch(Search, Identity);
        } catch (ClassCastException | NullPointerException e) {
            return null;
        }
    }
}
