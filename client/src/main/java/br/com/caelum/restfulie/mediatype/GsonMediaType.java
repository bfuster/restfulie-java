package br.com.caelum.restfulie.mediatype;

import java.io.IOException;
import java.io.Writer;
import java.lang.reflect.Type;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.codehaus.jettison.json.JSONObject;

import br.com.caelum.restfulie.RestClient;

import com.google.gson.Gson;

/**
 * GSON implementation
 *
 * @author bruno fuster
 */
@SuppressWarnings("unchecked")
public class GsonMediaType implements MediaType {

	private final List<String> types = Arrays.asList("application/json", "text/json", "json");

	public GsonMediaType() {}

	public boolean answersTo(String type) {
		for (String s : types)
			if (type.startsWith(s))
				return true;
		
		return false;
	}
	
	public <T> void marshal(T payload, Writer writer) throws IOException {
		
		Map<String, Object> map = new HashMap<String, Object>();
		map.put(payload.getClass().getName(), payload);
		
		String json = new Gson().toJson(map);
		writer.write(json);
		writer.flush();
	}

	
	public <T> T unmarshal(String content, RestClient client, Type type) {

		Object o = null;
		
		try {
			
			JSONObject obj = new JSONObject(content);
			Iterator<String> keys = obj.keys();
			String identifier = null;
			while (keys.hasNext()) {
				identifier = keys.next();
				break;
			}
			
			o = obj.get(identifier);
			
			return new Gson().fromJson(o.toString(), type);
			
		} catch (Exception e) {
	
			e.printStackTrace();
			return (T) o;
			
		}
	}
	
	public <T> T unmarshal(String content, RestClient client) {

		Object o = null;
		
		try {
			
			JSONObject obj = new JSONObject(content);
			String identifier = (String) obj.keys().next();

			o = obj.get(identifier);
			return (T) o;
			
		} catch (Exception e) {
	
			e.printStackTrace();
			throw new RuntimeException(e);
			
		}
	}

	protected List<Class> getTypesToEnhance() {
		return Collections.emptyList();
	}
	protected List<String> getCollectionNames() {
		return Collections.emptyList();
	}

}
