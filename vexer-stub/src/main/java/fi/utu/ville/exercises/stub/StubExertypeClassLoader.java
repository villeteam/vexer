package fi.utu.ville.exercises.stub;

import java.util.List;

import fi.utu.ville.exercises.model.ExecutionSettings;
import fi.utu.ville.exercises.model.Executor;
import fi.utu.ville.exercises.model.ExerciseData;
import fi.utu.ville.exercises.model.ExerciseException;
import fi.utu.ville.exercises.model.ExerciseTypeDescriptor;
import fi.utu.ville.exercises.model.StatisticalSubmissionInfo;
import fi.utu.ville.exercises.model.SubmissionInfo;
import fi.utu.ville.exercises.model.SubmissionStatisticsGiver;
import fi.utu.ville.exercises.model.SubmissionVisualizer;
import fi.utu.ville.standardutils.Localizer;
import fi.utu.ville.standardutils.TempFilesManager;

/**
 * A class containing factory-methods for loading exercise-type implementors of different {@link fi.utu.ville.exercises.model}-interfaces with correct data for
 * testing.
 * 
 * @author Riku Haavisto
 * 
 */
final class StubExertypeClassLoader {
	
	private StubExertypeClassLoader() {
	
	}
	
	/**
	 * Loads a new {@link Executor} with {@link ExerciseData} and possibly latest {@link SubmissionInfo} associated with exercise with given name.
	 * 
	 * @param descriptor
	 *            {@link ExerciseTypeDescriptor} of the exercise-type to use
	 * @param exerName
	 *            name for the exercise to resolve given {@link ExerciseData} and {@link SubmissionInfo}s
	 * @param localizer
	 *            {@link Localizer} for localizing the UI
	 * @param fbSettings
	 *            {@link ExecutionSettings} to use when testing the {@link Executor}
	 * @param tempMan
	 *            {@link TempFilesManager} for managing temporary files
	 * @return loaded {@link Executor}
	 * @throws ExerciseException
	 *             if there is an error in loading the {@link Executor}
	 */
	public static <E extends ExerciseData, S extends SubmissionInfo>
	
	Executor<E, S> loadExecutor(ExerciseTypeDescriptor<E, S> descriptor,
			String exerName, Localizer localizer, ExecutionSettings fbSettings,
			TempFilesManager tempMan) throws ExerciseException {
		Executor<E, S> res = descriptor.newExerciseExecutor();
		
		S submData = StubDataFilesHandler.loadLatestSubmInfo(
				descriptor.getSubDataClass(), descriptor, exerName, tempMan);
				
		E exertypeData = StubDataFilesHandler.loadExerTypeData(
				descriptor.getTypeDataClass(), descriptor.newExerciseXML(),
				descriptor, exerName, tempMan);
				
		res.initialize(localizer, exertypeData, submData, tempMan, fbSettings);
		
		return res;
		
	}
	
	/**
	 * Loads a new {@link SubmissionVisualizer} with {@link ExerciseData} and latest {@link SubmissionInfo} associated with exercise with given name..
	 * 
	 * @param descriptor
	 *            {@link ExerciseTypeDescriptor} of the exercise-type to use
	 * @param exerName
	 *            name for the exercise to resolve given {@link ExerciseData} and {@link SubmissionInfo}s
	 * @param localizer
	 *            {@link Localizer} for localizing the UI
	 * @param tempMan
	 *            {@link TempFilesManager}
	 * @return loaded {@link SubmissionVisualizer}
	 * @throws ExerciseException
	 *             if there is an error in loading the {@link Executor}
	 */
	public static <E extends ExerciseData, S extends SubmissionInfo> SubmissionVisualizer<E, S> loadVisualizer(
			ExerciseTypeDescriptor<E, S> descriptor, String exerName,
			Localizer localizer, TempFilesManager tempMan)
					throws ExerciseException {
		SubmissionVisualizer<E, S> res = descriptor.newSubmissionVisualizer();
		
		S submData = StubDataFilesHandler.loadLatestSubmInfo(
				descriptor.getSubDataClass(), descriptor, exerName, tempMan);
				
		if (submData == null) {
			return null;
		}
		
		E exertypeData = StubDataFilesHandler.loadExerTypeData(
				descriptor.getTypeDataClass(), descriptor.newExerciseXML(),
				descriptor, exerName, tempMan);
				
		res.initialize(exertypeData, submData, localizer, tempMan);
		
		return res;
		
	}
	
	/**
	 * Loads a new {@link SubmissionStatisticsGiver} with {@link ExerciseData} and all {@link SubmissionInfo}s associated with exercise with given name.
	 * 
	 * @param descriptor
	 *            {@link ExerciseTypeDescriptor} of the exercise-type to use
	 * @param exerName
	 *            name for the exercise to resolve given {@link ExerciseData} and {@link SubmissionInfo}s
	 * @param localizer
	 *            {@link Localizer} for localizing the UI
	 * @param fbSettings
	 *            {@link ExecutionSettings} to use when testing the {@link Executor}
	 * @param tempMan
	 *            {@link TempFilesManager}
	 * @return loaded {@link Executor}
	 * @throws ExerciseException
	 *             if there is an error in loading the {@link Executor}
	 */
	public static <E extends ExerciseData, S extends SubmissionInfo> SubmissionStatisticsGiver<E, S> loadStatisticsGiver(
			ExerciseTypeDescriptor<E, S> descriptor, String exerName,
			Localizer localizer, TempFilesManager tempMan)
					throws ExerciseException {
		SubmissionStatisticsGiver<E, S> res = descriptor.newStatisticsGiver();
		
		List<StatisticalSubmissionInfo<S>> allSubmissions = StubDataFilesHandler
				.loadAllSubmissions(descriptor, exerName,
						descriptor.getSubDataClass(), tempMan);
						
		E exertypeData = StubDataFilesHandler.loadExerTypeData(
				descriptor.getTypeDataClass(), descriptor.newExerciseXML(),
				descriptor, exerName, tempMan);
		res.initialize(exertypeData, allSubmissions, localizer, tempMan);
		
		return res;
		
	}
	
}
