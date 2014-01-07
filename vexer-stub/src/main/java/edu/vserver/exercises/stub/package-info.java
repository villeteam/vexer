/**
 * <p>
 * This package contains a quick and dirty Vaadin application meant 
 * to be used as a light stub-version of ViLLE to get a more distraction 
 * free environment for developing new exercise-types for ViLLE.
 * </p>
 * <p>
 * The stub depends on interfaces on stable {@link edu.vserver.exercises.model}
 * -package as does the real ViLLE. Stub however implements only the 
 * functionality directly related to exercises and does that in a quick 
 * and dirty way. The current version of stub can reliable be used only 
 * for single-user testing.
 * </p>
 * <p>
 * For correct behavior in real ViLLE, exercise-types developed in the stub 
 * are not allowed to rely any implementation detail of the stub (apart from
 * the general look-and-feel). Reversions of stub with more features are
 * likely.
 * </p>
 * <p>
 * Packages {@link edu.vserver.exercises.model}, {@link edu.vserver.exercises.helpers}, 
 * and {@link edu.vserver.standardutils} can be relied on. Those packages should 
 * see only new features preserving full back-wards compatibility.
 * </p>
 */
package edu.vserver.exercises.stub;