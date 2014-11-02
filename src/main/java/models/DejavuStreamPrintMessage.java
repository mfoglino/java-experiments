package models;

import static models.DejavuStreamMetadata.COLUMNS_LENGTH;
import static models.DejavuStreamMetadata.DEJAVU_RESPONSE_DATA_INDEX;
import static models.DejavuStreamMetadata.PROXY_HOST_INDEX;
import static org.apache.commons.lang3.ArrayUtils.isEmpty;
import static org.apache.commons.lang3.StringUtils.isNotBlank;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

import org.apache.commons.lang3.ArrayUtils;
import org.codehaus.jackson.JsonFactory;
import org.codehaus.jackson.JsonNode;
// import org.json.JSONArray;
// import org.json.JSONObject;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

// import play.Logger;
// import play.libs.Json;

public class DejavuStreamPrintMessage {

	private static final String DOMAINS_FIELD = "domains";

	private static final Pattern DEJAVU_PRINTS_PATTERN = Pattern.compile("mclics-clickcounter-webserver-print.*");

	private static final String DEJAVU_ENCODING = "UTF-8";

	private Map<String, String> fields;

	/**
	 * Before attempting to create an instance of {@link DejavuStreamPrintMessage}, you must invoke
	 * this method, in order to determine whether the stream contains data about a Print.
	 * <p>
	 * <b>IMPORTANT NOTE:</b>This provides a fast mechanism for discarding tons of messages we will
	 * read from a stream but that are not relevant to us. We could have managed this using
	 * exceptions on the constructor of this class but we decided against it because it would imply
	 * using Exceptions as control flow, which is highly discouraged by the good programming
	 * principles.
	 * 
	 * @param aStreamMessage
	 *            a dejavu stream message, possibly containing data about a Print.
	 * @return <code>true</code> if and only if the message contains data about a Print.
	 */
	public static boolean containsEnoughData(String aStreamMessage) {
		String[] columns = extractColumnsFrom(aStreamMessage);

		return columns != null && columns.length == COLUMNS_LENGTH && DEJAVU_PRINTS_PATTERN.matcher(columns[PROXY_HOST_INDEX]).matches()
				&& isNotBlank(columns[DEJAVU_RESPONSE_DATA_INDEX]);
	}

	/**
	 * Pre-condition: the class method {@link #containsEnoughData(String)} was invoked prior to
	 * invoking this constructor, and it returned <code>true</code>. If this pre-condition does not
	 * hold, the correct behavior of this class is not guaranteed.
	 * 
	 * @param aValidPrintStreamMessage
	 *            a valid stream message, containing print data
	 */
	public DejavuStreamPrintMessage(String aValidPrintStreamMessage) {
		super();
		String[] columns = extractColumnsFrom(aValidPrintStreamMessage);

		String printData = columns[DEJAVU_RESPONSE_DATA_INDEX];

		String[] keyValuePairs = printData.split(";");

		if (isEmpty(keyValuePairs)) {
			throw new IllegalArgumentException("Unexpected format after splitting by semi-colon: " + ArrayUtils.toString(keyValuePairs));
		}
		fields = new HashMap<String, String>();

		for (String keyValuePairAsString : keyValuePairs) {
			String[] keyValuePair = keyValuePairAsString.split("=", -1);

			if (keyValuePair.length != 2) {
				throw new IllegalArgumentException("Unexpected format after splitting by equals sign: " + ArrayUtils.toString(keyValuePair));
			}
			fields.put(urlDecode(keyValuePair[0]), urlDecode(keyValuePair[1]));
		}
	}

	public JsonNode getDomains() {
		String domainsAsJson = fields.get(DOMAINS_FIELD);

		// return isNotBlank(domainsAsJson) ? Json.parse(domainsAsJson) : null;
		return null;
	}

	public long getPrintTms() {
		return Long.valueOf(fields.get("tms"));
	}

	public List<PrintedAd> getPrintedAds() {
		List<PrintedAd> printedAds = new ArrayList<PrintedAd>();
		Date tms = new Date(getPrintTms());
		Long slot = fields.containsKey("slot") ? Long.parseLong(fields.get("slot")) : null;
		Long publisher = fields.containsKey("publisher") ? Long.parseLong(fields.get("publisher")) : null;
		JsonNode domains = getDomains();

		if (domains != null) {
			for (JsonNode domain : domains) {
				for (JsonNode adAsJson : domain.get("ads")) {
					PrintedAd printedAd = new PrintedAd();

					printedAd.setId(adAsJson.get("id").asLong());
					printedAd.setTms(tms);
					printedAd.setCustId(adAsJson.get("cust").asLong());
					printedAd.setSlotId(slot);
					printedAd.setPublisherId(publisher);

					printedAds.add(printedAd);
				}
			}
		}

		return printedAds;
	}

	// public JSONObject toJson() {
	// JSONObject printAsJson = new JSONObject();
	//
	// try {
	// printAsJson = new JSONObject(this.fields);
	// String domains = new StringBuilder(this.fields.get(DOMAINS_FIELD)).toString();
	// JSONArray newDomainsNode = new JSONArray(domains);
	// printAsJson.put(DOMAINS_FIELD, newDomainsNode);
	// } catch (Exception e) {
	// Logger.error("Error parsing json...", e);
	// }
	//
	// return printAsJson;
	// }

	public JsonObject toJson() {
		Gson gson = new GsonBuilder().create();

		JsonElement jsonTree = gson.toJsonTree(this.fields);
		String domains = this.fields.get(DOMAINS_FIELD);

		JsonParser parser = new JsonParser();
		JsonElement domainsElement = parser.parse(domains);

		jsonTree.getAsJsonObject().remove(DOMAINS_FIELD);
		jsonTree.getAsJsonObject().add(DOMAINS_FIELD, domainsElement);

		return jsonTree.getAsJsonObject();
	}

	private static String[] extractColumnsFrom(String aStreamMessage) {
		// Note the use of the overloaded version of "split", otherwise, things will get weird
		return aStreamMessage.split("\t", -1);
	}

	private static String urlDecode(String string) {
		try {
			return URLDecoder.decode(string, DEJAVU_ENCODING);
		}
		catch (UnsupportedEncodingException e) {
			throw new IllegalArgumentException("Unable to decode: " + string);
		}
	}
}
