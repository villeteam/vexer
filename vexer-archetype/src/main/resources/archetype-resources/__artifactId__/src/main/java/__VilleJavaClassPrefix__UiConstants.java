#set( $nameSpace = $artifactId.toUpperCase() )
package ${package};

public class ${VilleJavaClassPrefix}UiConstants {

	private static final String PREFIX = "${nameSpace}" + ".";

	public static final String NAME = PREFIX + "NAME";

	public static final String DESC = PREFIX + "DESC";

	public static final String QUESTION = PREFIX + "QUESTION";
	
	public static final String ANSWER = PREFIX + "ANSWER";
	
	public static final String SUBM_EXPORT = PREFIX + "SUBM_EXPORT";
	
	public static final String ANSWER_COL_DESC = PREFIX + "ANSWER_COL_DESC";
	
}
