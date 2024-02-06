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

import org.junit.jupiter.api.Test;
import org.openrewrite.test.RecipeSpec;
import org.openrewrite.test.RewriteTest;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceTransactionManagerAutoConfiguration;
import org.springframework.boot.autoconfigure.orm.jpa.HibernateJpaAutoConfiguration;


class AddPropertyIfClassExistsTest implements RewriteTest
{
	
	@Override
	public void defaults(final RecipeSpec recipeSpec)
	{
		recipeSpec
			.recipe(new AddPropertyIfClassExists(
				HibernateJpaAutoConfiguration.class.getName(),
				"spring.autoconfigure.exclude",
				DataSourceAutoConfiguration.class.getName()
					+ ","
					+ DataSourceTransactionManagerAutoConfiguration.class.getName()
					+ ","
					+ HibernateJpaAutoConfiguration.class.getName(),
				"",
				"="));
	}
	
	@Test
	void testSimple()
	{
		this.rewriteRun
			(
				properties(
					"",
					"""
						spring.autoconfigure.exclude=org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration,org.springframework.boot.autoconfigure.jdbc.DataSourceTransactionManagerAutoConfiguration,org.springframework.boot.autoconfigure.orm.jpa.HibernateJpaAutoConfiguration
						"""
				)
			);
	}
	
	/**
	 * It's not clear if this is a desired behavior, but since the Open Rewrite Recipe
	 * {@link org.openrewrite.properties.AddProperty} is doing this, and we are only using this recipe, this is how it
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
						"""
				),
				properties(
					"",
					"""
						spring.autoconfigure.exclude=org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration,org.springframework.boot.autoconfigure.jdbc.DataSourceTransactionManagerAutoConfiguration,org.springframework.boot.autoconfigure.orm.jpa.HibernateJpaAutoConfiguration
						"""
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
					recipeSpec.recipe(new AddPropertyIfClassExists(
						"not.existing.Class",
						"spring.autoconfigure.exclude",
						"DummyValue",
						"",
						"=")),
				properties(
					""
				)
			);
	}
}
