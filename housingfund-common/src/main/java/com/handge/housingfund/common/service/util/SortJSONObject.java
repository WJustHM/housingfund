package com.handge.housingfund.common.service.util;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedHashMap;

/**
 * Created by Liujuhao on 2017/11/2.
 */
public class SortJSONObject extends JSONObject {

    private final LinkedHashMap<Object, Object> sortHashMap;

    public SortJSONObject() {
        this.sortHashMap = new LinkedHashMap<>();
    }

    public SortJSONObject(String source) {
        this(new JSONTokener(source));
    }

    public SortJSONObject(JSONTokener x) throws JSONException {
        this();
        if(x.nextClean() != 123) {
            throw x.syntaxError("A JSONObject text must begin with '{'");
        } else {
            while(true) {
                char c = x.nextClean();
                switch(c) {
                    case '\u0000':
                        throw x.syntaxError("A JSONObject text must end with '}'");
                    case '}':
                        return;
                    default:
                        x.back();
                        String key = x.nextValue().toString();
                        c = x.nextClean();
                        if(c != 58) {
                            throw x.syntaxError("Expected a ':' after a key");
                        }

                        this.putOnce(key, x.nextValue());
                        switch(x.nextClean()) {
                            case ',':
                            case ';':
                                if(x.nextClean() == 125) {
                                    return;
                                }

                                x.back();
                                break;
                            case '}':
                                return;
                            default:
                                throw x.syntaxError("Expected a ',' or '}'");
                        }
                }
            }
        }
    }

    @Override
    public JSONObject put(String key, Object value) throws JSONException {

        if (key == null) {
            throw new JSONException("Null key.");
        }
        if (value != null) {
            testValidity(value);
            this.sortHashMap.put(key, value);
        } else {
            remove(key);
        }
        return this;
    }

    @Override
    public JSONObject put(String key, Collection<?> value) throws JSONException {

        if (key == null) {
            throw new JSONException("Null key.");
        }
        if (value != null) {
            testValidity(value);
            this.sortHashMap.put(key, value);
        } else {
            remove(key);
        }
        return this;
    }

    @Override
    public String optString(String key, String defaultValue) {
        Object object = this.opt(key);
        return NULL.equals(object) ? defaultValue : object.toString();
    }

    @Override
    public JSONArray optJSONArray(String key) {
        Object o = this.opt(key);
        return o instanceof JSONArray ? (JSONArray) o : null;
    }

    @Override
    public Object opt(String key) {
        return key == null ? null : this.sortHashMap.get(key);
    }

    @Override
    public String toString() {
        try {
            Iterator<Object> keys = sortHashMap.keySet().iterator();

            if (!keys.hasNext())
                return null;

            StringBuffer sb = new StringBuffer("{");

            while (keys.hasNext()) {
                if (sb.length() > 1) {
                    sb.append(',');
                }
                Object o = keys.next();
                sb.append(quote(o.toString()));
                sb.append(':');
                sb.append(valueToString(sortHashMap.get(o)));
            }
            sb.append('}');
            return sb.toString();
        } catch (Exception e) {
            return null;
        }
    }


}
