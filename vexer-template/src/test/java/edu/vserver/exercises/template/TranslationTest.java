package edu.vserver.exercises.template;

import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.IOException;
import java.util.Collections;

import org.junit.Test;

import edu.vserver.exercises.testutils.VilleTranslationsFileTester;

public class TranslationTest {

	@Test
	public void test() {
		boolean testRes = false;
		try {
			testRes = VilleTranslationsFileTester
					.testLangFiles(
							TemplateUiConstants.class,
							new File(
									"target/classes/META-INF/resources/VILLE/language/extensions/template.trl"),
							Collections.singletonList("en"));
		} catch (IOException e) {
			e.printStackTrace();
		}
		assertTrue(testRes);
	}
}
