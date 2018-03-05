package ut.university.projectAI.networking;

/**
 * Basic networking unit. 
 */
public interface RequestPerformer {
	/**
	 * @param uri to be sent to
	 * @return response
	 */
	String performRequest(String uri);
}
