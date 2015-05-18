package edu.vserver.exercises.math.essentials.level;

import fi.utu.ville.exercises.model.SubmissionInfo;

public interface LevelSubmissionInfo extends SubmissionInfo {

	void setDiffLevel(DiffLevel usedLevel);

	DiffLevel getDiffLevel();

}
