package br.com.caelum.restfulie.mediatype;

import java.io.IOException;
import java.io.Writer;
import java.lang.reflect.Type;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import br.com.caelum.restfulie.RestClient;
import br.com.caelum.restfulie.client.DefaultLinkConverter;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.json.JettisonMappedXmlDriver;

/**
 * A xstream + jettison based media type implementation.
 *
 * @author guilherme silveira
 */
@SuppressWarnings("unchecked")
public class JsonMediaType implements MediaType {

	private final List<String> types = Arrays.asList("application/json", "text/json", "json");

	private final XStreamHelper helper = new XStreamHelper(
			new JettisonMappedXmlDriver());

	private final XStream xstream;

	public JsonMediaType() {
		this.xstream = helper.getXStream(getTypesToEnhance(), getCollectionNames());
		configure(xstream);
	}

	/**
	 * Allows xstream further configuration.
	 */
	protected void configure(XStream xstream) {
	}

	public boolean answersTo(String type) {
		return types.contains(type);
	}

	public <T> void marshal(T payload, Writer writer) throws IOException {
		xstream.toXML(payload, writer);
		writer.flush();
	}

	public <T> T unmarshal(String content, RestClient client) {
		xstream.registerConverter(new DefaultLinkConverter(client));
		return (T) xstream.fromXML(content);
	}

	public <T> T unmarshal(String content, RestClient client, Type type) {
		//no type implementation available for xstream, use @xstreamalias
		return null;
	}
	
	protected List<Class> getTypesToEnhance() {
		return Collections.emptyList();
	}
	protected List<String> getCollectionNames() {
		return Collections.emptyList();
	}

}