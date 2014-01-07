package edu.vserver.exercises.stub;

import java.util.List;

import edu.vserver.exercises.model.ExecutionSettings;
import edu.vserver.exercises.model.Executor;
import edu.vserver.exercises.model.ExerciseData;
import edu.vserver.exercises.model.ExerciseException;
import edu.vserver.exercises.model.ExerciseTypeDescriptor;
import edu.vserver.exercises.model.StatisticalSubmissionInfo;
import edu.vserver.exercises.model.SubmissionInfo;
import edu.vserver.exercises.model.SubmissionStatisticsGiver;
import edu.vserver.exercises.model.SubmissionVisualizer;
import edu.vserver.standardutils.Localizer;
import edu.vserver.standardutils.TempFilesManager;

/**
 * A class containing factory-methods for loading exercise-type implementors of
 * different {@link edu.vserver.exercises.model}-interfaces with correct data
 * for testing.
 * 
 * @author Riku Haavisto
 * 
 */
final class StubExertypeClassLoader {

	private StubExertypeClassLoader() {

	}

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
