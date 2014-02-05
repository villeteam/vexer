package edu.vserver.exercises.helpers;

import java.io.UnsupportedEncodingException;
import java.lang.reflect.Type;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

import edu.vserver.exercises.model.ExerciseData;
import edu.vserver.exercises.model.ExerciseException;
import edu.vserver.exercises.model.ExerciseTypeDescriptor;
import edu.vserver.exercises.model.PersistenceHandler;
import edu.vserver.exercises.model.SubmissionInfo;
import edu.vserver.standardutils.AbstractFile;
import edu.vserver.standardutils.TempFilesManager;

/**
 * <p>
 * Helper class for implementing {@link PersistenceHandler} by utilizing
 * {@link Gson} to convert {@link ExerciseData} and {@link SubmissionInfo}
 * objects to JSON.
 * </p>
 * <p>
 * In addition to normally Gson-convertable fields, the {@link ExerciseData}
 * (possibly later also {@link SubmissionInfo}) can contain {@link AbstractFile}
 * -fields which will be saved and loaded by reference utilizing
 * {@link ByRefSaver} and {@link ByRefLoader}.
 * </p>
 * <p>
 * Utilizing this class is the recommended method of implementing
 * {@link PersistenceHandler} when possible (when {@link ExerciseData} and
 * {@link SubmissionInfo} classes comply to {@link Gson}-requirements).
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

	/**
	 * Generates a new {@link GsonPersistenceHandler} that can be returned from
	 * {@link ExerciseTypeDescriptor #newExerciseXML()}-method. The returned
	 * class is stateless, meaning there is no need to instantiate more than one
	 * instance per exercise-type (though that's only a minor performance
	 * improvement).
	 * 
	 * @param exerDataClass
	 *            {@link Class}-object representing the {@link ExerciseData}
	 *            -implementor
	 * @param submInfoClass
	 *            {@link Class}-object representing the {@link SubmissionInfo}
	 *            -implementor
	 */
	public GsonPersistenceHandler(Class<E> exerDataClass, Class<S> submInfoClass) {
		this.exerDataClass = exerDataClass;
		this.submInfoClass = submInfoClass;
	}

	@Override
	public byte[] saveExerData(E etd, TempFilesManager tempManager,
			ByRefSaver refSaver) throws ExerciseException {
		GsonBuilder gsonBuilder = new GsonBuilder();
		ByRefSerializer brSer = new ByRefSerializer(refSaver);

		gsonBuilder.registerTypeAdapter(AbstractFile.class, brSer);

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
			GsonBuilder gsonBuilder = new GsonBuilder();
			ByRefDeserializer brSer = new ByRefDeserializer(refLoader);

			gsonBuilder.registerTypeAdapter(AbstractFile.class, brSer);

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
		Gson ser = new Gson();
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
			Gson ser = new Gson();
			S res = ser.fromJson(src, submInfoClass);
			return res;
		} catch (UnsupportedEncodingException e) {
			// Won't happen
			throw new IllegalStateException("UTF-8 missing", e);
		}
	}

	/**
	 * {@link JsonSerializer}-implementor that can be used to automatically save
	 * {@link AbstractFile}s by reference in {@link Gson}-conversion utilizing
	 * the provided {@link ByRefSaver}.
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
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return new JsonPrimitive(refStr);
		}

	}

	/**
	 * {@link JsonDeserializer}-implementor that can be used to automatically
	 * load {@link AbstractFile}s saved by reference in {@link Gson}-conversion
	 * utilizing the provided {@link ByRefLoader}. The 'load'-context of
	 * {@link ByRefLoader} must match the 'save'-context {@link ByRefSaver} used
	 * when the the {@link Gson}-conversion was made.
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
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return res;
		}
	}

}
