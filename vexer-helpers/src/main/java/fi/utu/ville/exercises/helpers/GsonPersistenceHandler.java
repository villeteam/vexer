package fi.utu.ville.exercises.helpers;

import java.io.UnsupportedEncodingException;
import java.lang.reflect.Type;
import java.util.Hashtable;
import java.util.Map;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import com.google.gson.TypeAdapter;

import fi.utu.ville.exercises.model.ExerciseData;
import fi.utu.ville.exercises.model.ExerciseException;
import fi.utu.ville.exercises.model.ExerciseTypeDescriptor;
import fi.utu.ville.exercises.model.PersistenceHandler;
import fi.utu.ville.exercises.model.SubmissionInfo;
import fi.utu.ville.standardutils.AbstractFile;
import fi.utu.ville.standardutils.TempFilesManager;

/**
 * <p>
 * Helper class for implementing {@link PersistenceHandler} by utilizing {@link Gson} to convert {@link ExerciseData} and {@link SubmissionInfo} objects to
 * JSON.
 * </p>
 * <p>
 * In addition to normally Gson-convertable fields, the {@link ExerciseData} (possibly later also {@link SubmissionInfo}) can contain {@link AbstractFile}
 * -fields which will be saved and loaded by reference utilizing {@link ByRefSaver} and {@link ByRefLoader}.
 * </p>
 * <p>
 * Utilizing this class is the recommended method of implementing {@link PersistenceHandler} when possible (when {@link ExerciseData} and {@link SubmissionInfo}
 * classes comply to {@link Gson}-requirements).
 * </p>
 * 
 * <p>
 * For fields that are not Gson-convertable, {@link TypeAdapter} can be registered to handle the field.
 * </p>
 * 
 * @author Riku Haavisto
 * 
 * @param <E>
 *            used {@link ExerciseData}-implementor
 * @param <S>
 *            used {@link SubmissionInfo}-implementor
 */
public class GsonPersistenceHandler<E extends ExerciseData, S extends SubmissionInfo>
		
		implements PersistenceHandler<E, S> {
		
	/**
	 * 
	 */
	private static final long serialVersionUID = -2601025744571919374L;
	
	private final Class<E> exerDataClass;
	private final Class<S> submInfoClass;
	private final Hashtable<Type, Object> typeAdapters = new Hashtable<Type, Object>();
	
	/**
	 * Generates a new {@link GsonPersistenceHandler} that can be returned from {@link ExerciseTypeDescriptor #newExerciseXML()}-method. The returned class is
	 * stateless, meaning there is no need to instantiate more than one instance per exercise-type (though that's only a minor performance improvement).
	 * 
	 * @param exerDataClass
	 *            {@link Class}-object representing the {@link ExerciseData} -implementor
	 * @param submInfoClass
	 *            {@link Class}-object representing the {@link SubmissionInfo} -implementor
	 */
	public GsonPersistenceHandler(Class<E> exerDataClass, Class<S> submInfoClass) {
		this.exerDataClass = exerDataClass;
		this.submInfoClass = submInfoClass;
	}
	
	/**
	 * Registers a type adapter to use with Json conversion. If a type isn't converted or loaded from Json properly this method can be used to register a type
	 * adapter for said type.
	 * 
	 * @param type
	 *            type to register the adapter to
	 * @param typeAdapter
	 *            typeadapter to use. This object must implement at least one of the InstanceCreator, JsonSerializer, and a JsonDeserializer interfaces.
	 */
	public void registerTypeAdapter(Type type, Object typeAdapter) {
		typeAdapters.put(type, typeAdapter);
	}
	
	@Override
	public byte[] saveExerData(E etd, TempFilesManager tempManager,
			ByRefSaver refSaver) throws ExerciseException {
		GsonBuilder gsonBuilder = getGsonSaver(refSaver);
		Gson ser = gsonBuilder.create();
		String json = ser.toJson(etd);
		try {
			return json.getBytes("UTF-8");
		} catch (UnsupportedEncodingException e) {
			// Won't happen
			throw new IllegalStateException("UTF-8 missing", e);
		}
	}
	
	@Override
	public E loadExerData(byte[] dataPres, TempFilesManager tempManager,
			ByRefLoader refLoader) throws ExerciseException {
		try {
			String src = new String(dataPres, "UTF-8");
			GsonBuilder gsonBuilder = getGsonLoader(refLoader);
			
			Gson ser = gsonBuilder.create();
			E res = ser.fromJson(src, exerDataClass);
			return res;
		} catch (UnsupportedEncodingException e) {
			// Won't happen
			throw new IllegalStateException("UTF-8 missing", e);
		}
	}
	
	@Override
	public byte[] saveSubmission(S subm, TempFilesManager tempManager)
			throws ExerciseException {
			
		Gson ser = getSubmissionGson();
		
		String json = ser.toJson(subm);
		try {
			return json.getBytes("UTF-8");
		} catch (UnsupportedEncodingException e) {
			// Won't happen
			throw new IllegalStateException("UTF-8 missing", e);
		}
	}
	
	@Override
	public S loadSubmission(byte[] dataPres, boolean forStatGiver,
			TempFilesManager tempManager) throws ExerciseException {
		try {
			String src = new String(dataPres, "UTF-8");
			Gson ser = getSubmissionGson();
			S res = ser.fromJson(src, submInfoClass);
			return res;
		} catch (UnsupportedEncodingException e) {
			// Won't happen
			throw new IllegalStateException("UTF-8 missing", e);
		}
	}
	
	private GsonBuilder getGsonLoader(ByRefLoader refLoader) {
		GsonBuilder gsonBuilder = new GsonBuilder();
		ByRefDeserializer brSer = new ByRefDeserializer(refLoader);
		gsonBuilder.registerTypeAdapter(AbstractFile.class, brSer);
		registerCustomAdapters(gsonBuilder);
		return gsonBuilder;
	}
	
	private GsonBuilder getGsonSaver(ByRefSaver refSaver) {
		GsonBuilder gsonBuilder = new GsonBuilder();
		ByRefSerializer brSer = new ByRefSerializer(refSaver);
		gsonBuilder.registerTypeAdapter(AbstractFile.class, brSer);
		registerCustomAdapters(gsonBuilder);
		return gsonBuilder;
	}
	
	private Gson getSubmissionGson() {
		GsonBuilder gsonBuilder = new GsonBuilder();
		registerCustomAdapters(gsonBuilder);
		return gsonBuilder.create();
	}
	
	private void registerCustomAdapters(GsonBuilder builder) {
		for (Map.Entry<Type, Object> adapter : typeAdapters.entrySet()) {
			builder.registerTypeAdapter(adapter.getKey(), adapter.getValue());
		}
	}
	
	/**
	 * {@link JsonSerializer}-implementor that can be used to automatically save {@link AbstractFile}s by reference in {@link Gson}-conversion utilizing the
	 * provided {@link ByRefSaver}.
	 * 
	 * @author Riku Haavisto
	 * 
	 */
	public static class ByRefSerializer implements JsonSerializer<AbstractFile> {
		
		private final ByRefSaver refSaver;
		
		public ByRefSerializer(ByRefSaver refSaver) {
			this.refSaver = refSaver;
		}
		
		@Override
		public JsonElement serialize(AbstractFile src, Type typeOfSrc,
				JsonSerializationContext context) {
			String refStr = "";
			try {
				refStr = refSaver.saveByReference(src);
			} catch (ExerciseException e) {
				e.printStackTrace();
			}
			return new JsonPrimitive(refStr);
		}
		
	}
	
	/**
	 * {@link JsonDeserializer}-implementor that can be used to automatically load {@link AbstractFile}s saved by reference in {@link Gson}-conversion utilizing
	 * the provided {@link ByRefLoader}. The 'load'-context of {@link ByRefLoader} must match the 'save'-context {@link ByRefSaver} used when the the
	 * {@link Gson}-conversion was made.
	 * 
	 * @author Riku Haavisto
	 * 
	 */
	public static class ByRefDeserializer implements
			JsonDeserializer<AbstractFile> {
			
		private final ByRefLoader refLoader;
		
		public ByRefDeserializer(ByRefLoader refLoader) {
			this.refLoader = refLoader;
		}
		
		@Override
		public AbstractFile deserialize(JsonElement json, Type typeOfT,
				JsonDeserializationContext context) throws JsonParseException {
			AbstractFile res = null;
			try {
				res = refLoader.loadByReference(json.getAsString());
			} catch (ExerciseException e) {
				e.printStackTrace();
			}
			return res;
		}
	}
}
