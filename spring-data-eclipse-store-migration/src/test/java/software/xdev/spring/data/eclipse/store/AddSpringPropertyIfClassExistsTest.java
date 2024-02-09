/*
 * Copyright © 2023 XDEV Software (https://xdev.software)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package software.xdev.spring.data.eclipse.store;

import static org.openrewrite.properties.Assertions.properties;
import static org.openrewrite.yaml.Assertions.yaml;

import org.junit.jupiter.api.Test;
import org.openrewrite.test.RecipeSpec;
import org.openrewrite.test.RewriteTest;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceTransactionManagerAutoConfiguration;
import org.springframework.boot.autoconfigure.orm.jpa.HibernateJpaAutoConfiguration;


class AddSpringPropertyIfClassExistsTest implements RewriteTest
{
	
	@Override
	public void defaults(final RecipeSpec recipeSpec)
	{
		recipeSpec
			.recipe(new AddSpringPropertyIfClassExists(
				HibernateJpaAutoConfiguration.class.getName(),
				"spring.autoconfigure.exclude",
				DataSourceAutoConfiguration.class.getName()
					+ ","
					+ DataSourceTransactionManagerAutoConfiguration.class.getName()
					+ ","
					+ HibernateJpaAutoConfiguration.class.getName(),
				"", null));
	}
	
	@Test
	void testSimpleProperties()
	{
		this.rewriteRun
			(
				properties(
					"",
					"""
						spring.autoconfigure.exclude=org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration,org.springframework.boot.autoconfigure.jdbc.DataSourceTransactionManagerAutoConfiguration,org.springframework.boot.autoconfigure.orm.jpa.HibernateJpaAutoConfiguration
						""",
					s -> s.path("application.properties")
				)
			);
	}
	
	@Test
	void testSimpleYaml()
	{
		this.rewriteRun
			(
				yaml(
					"",
					"""
						spring:
						  autoconfigure:
						    exclude: org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration,org.springframework.boot.autoconfigure.jdbc.DataSourceTransactionManagerAutoConfiguration,org.springframework.boot.autoconfigure.orm.jpa.HibernateJpaAutoConfiguration
						""",
					s -> s.path("application.yml")
				)
			);
	}
	
	@Test
	void testMultipleYamlAlsoInTestFolder()
	{
		this.rewriteRun
			(
				yaml(
					"",
					"""
						spring:
						  autoconfigure:
						    exclude: org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration,org.springframework.boot.autoconfigure.jdbc.DataSourceTransactionManagerAutoConfiguration,org.springframework.boot.autoconfigure.orm.jpa.HibernateJpaAutoConfiguration
						""",
					s -> s.path("/src/main/resources/application.yml")
				),
				yaml(
					"",
					"""
						spring:
						  autoconfigure:
						    exclude: org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration,org.springframework.boot.autoconfigure.jdbc.DataSourceTransactionManagerAutoConfiguration,org.springframework.boot.autoconfigure.orm.jpa.HibernateJpaAutoConfiguration
						""",
					s -> s.path("/src/test/resources/application.yml")
				),
				yaml(
					"",
					"""
						spring:
						  autoconfigure:
						    exclude: org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration,org.springframework.boot.autoconfigure.jdbc.DataSourceTransactionManagerAutoConfiguration,org.springframework.boot.autoconfigure.orm.jpa.HibernateJpaAutoConfiguration
						""",
					s -> s.path("application.yml")
				),
				yaml(
					"",
					s -> s.path("no-application.yml")
				)
			);
	}
	
	@Test
	void testWrongFileNameYaml()
	{
		this.rewriteRun
			(
				yaml(
					"",
					s -> s.path("random.yml")
				)
			);
	}
	
	@Test
	void testWrongFileNameProperties()
	{
		this.rewriteRun
			(
				properties(
					"",
					s -> s.path("random.properties")
				)
			);
	}
	
	/**
	 * It's not clear if this is a desired behavior, but since the Open Rewrite Recipe
	 * {@link org.openrewrite.java.spring.AddSpringProperty} is doing this, and we are only using this recipe, this is
	 * how it
	 * works now.
	 * <p>
	 * Might change in the future.
	 * </p>
	 */
	@Test
	void testMultipleProperties()
	{
		this.rewriteRun
			(
				properties(
					"",
					"""
						spring.autoconfigure.exclude=org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration,org.springframework.boot.autoconfigure.jdbc.DataSourceTransactionManagerAutoConfiguration,org.springframework.boot.autoconfigure.orm.jpa.HibernateJpaAutoConfiguration
						""",
					s -> s.path("application.properties")
				),
				yaml(
					"",
					"""
						spring:
						  autoconfigure:
						    exclude: org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration,org.springframework.boot.autoconfigure.jdbc.DataSourceTransactionManagerAutoConfiguration,org.springframework.boot.autoconfigure.orm.jpa.HibernateJpaAutoConfiguration
						""",
					s -> s.path("application.yml")
				)
			);
	}
	
	@Test
	void testExisting()
	{
		this.rewriteRun
			(
				properties(
					"""
						spring.autoconfigure.exclude=org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration,org.springframework.boot.autoconfigure.jdbc.DataSourceTransactionManagerAutoConfiguration,org.springframework.boot.autoconfigure.orm.jpa.HibernateJpaAutoConfiguration
						"""
				)
			);
	}
	
	@Test
	void testClassNotExisting()
	{
		this.rewriteRun
			(
				recipeSpec ->
					recipeSpec.recipe(new AddSpringPropertyIfClassExists(
						"not.existing.Class",
						"spring.autoconfigure.exclude",
						"DummyValue",
						"",
						null)),
				properties(
					"",
					s -> s.path("application.properties")
				)
			);
	}
}
