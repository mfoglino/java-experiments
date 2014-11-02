package models;

import static lombok.AccessLevel.PRIVATE;
import lombok.NoArgsConstructor;

/**
 * A class with constants containing the indices of the columns corresponding to the structure of a
 * dejavu table.
 * <p>
 * For a full and up-to-date definition of this, see the link below.
 * 
 * @see <a
 *      href="http://api.melicloud.com/interop/streams/topics/dejavu-stream/metadata">http://api.melicloud.com/interop/streams/topics/dejavu-stream/metadata</a>
 * @author Jonathan Chiocchio
 */
@NoArgsConstructor(access = PRIVATE)
public final class DejavuStreamMetadata {

	public static final int TIME_INDEX = 0;

	public static final int PROXY_HOST_INDEX = 1;

	public static final int DEJAVU_RESPONSE_DATA_INDEX = 32;

	public static final int COLUMNS_LENGTH = 39;
}