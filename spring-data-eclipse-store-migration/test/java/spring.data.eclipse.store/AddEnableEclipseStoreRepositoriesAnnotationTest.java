package spring.data.eclipse.store;

import static org.openrewrite.java.Assertions.java;

import org.junit.jupiter.api.Test;
import org.openrewrite.java.Java17Parser;
import org.openrewrite.test.RecipeSpec;
import org.openrewrite.test.RewriteTest;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import software.xdev.spring.data.eclipse.store.AddAnnotationToOtherAnnotation;
import software.xdev.spring.data.eclipse.store.repository.config.EnableEclipseStoreRepositories;


public class AddEnableEclipseStoreRepositoriesAnnotationTest implements RewriteTest
{
	
	@Override
	public void defaults(final RecipeSpec recipeSpec)
	{
		recipeSpec
			.recipe(new AddAnnotationToOtherAnnotation(
				SpringBootApplication.class.getName(),
				EnableEclipseStoreRepositories.class.getName(),
				"spring-data-eclipse-store",
				EnableEclipseStoreRepositories.class.getSimpleName()))
			.parser(Java17Parser.builder()
				.logCompilationWarningsAndErrors(true)
				.classpath("spring-boot-autoconfigure", "spring-data-eclipse-store"));
	}
	
	@Test
	void test()
	{
		this.rewriteRun
			(
				java
					(
						"""
							import org.springframework.boot.autoconfigure.SpringBootApplication;
							
							@SpringBootApplication
							class A
							{
							    public A()
							    {
							    }
							}
							""",
						"""
							import org.springframework.boot.autoconfigure.SpringBootApplication;
							import software.xdev.spring.data.eclipse.store.repository.config.EnableEclipseStoreRepositories;
							
							@EnableEclipseStoreRepositories
							@SpringBootApplication
							class A
							{
							    public A()
							    {
							    }
							}
							"""
					)
			);
	}
}
