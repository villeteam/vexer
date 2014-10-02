/**
 * <p>
 * This package contains a quick and dirty Vaadin application meant 
 * to be used as a light stub-version of ViLLE to get a more distraction 
 * free environment for developing new exercise-types for ViLLE.
 * </p>
 * <p>
 * The stub depends on interfaces on stable {@link fi.utu.ville.exercises.model}
 * -package as does the real ViLLE. Stub however implements only the 
 * functionality directly related to exercises and does that in a quick 
 * and dirty way. The current version of stub can be used reliably only 
 * for single-user testing.
 * </p>
 * <p>
 * For correct behavior in real ViLLE, exercise-types developed in the stub 
 * are not allowed to rely any implementation detail of the stub (apart from
 * the general look-and-feel). New versions of stub with more features are
 * likely.
 * </p>
 * <p>
 * Packages {@link fi.utu.ville.exercises.model}, {@link fi.utu.ville.exercises.helpers}, 
 * and {@link fi.utu.ville.standardutils} can be relied on. Those packages will mostly be 
 * updated in a backwards-compatible way, though it is possible that some more profound 
 * changes will be needed from time to time.
 * </p>
 */
package fi.utu.ville.exercises.stub;