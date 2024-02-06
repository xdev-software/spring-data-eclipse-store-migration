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

import static org.openrewrite.java.Assertions.java;

import org.junit.jupiter.api.Test;
import org.openrewrite.java.Java17Parser;
import org.openrewrite.test.RecipeSpec;
import org.openrewrite.test.RewriteTest;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceTransactionManagerAutoConfiguration;
import org.springframework.boot.autoconfigure.orm.jpa.HibernateJpaAutoConfiguration;


class AddAttributeToAnnotationIfClassExistsTest implements RewriteTest
{
	
	@Override
	public void defaults(final RecipeSpec recipeSpec)
	{
		recipeSpec
			.recipe(new AddAttributeToAnnotationIfClassExists(
				HibernateJpaAutoConfiguration.class.getName(),
				SpringBootApplication.class.getName(),
				"exclude",
				"{"
					+ DataSourceAutoConfiguration.class.getName()
					+ ".class,"
					+ DataSourceTransactionManagerAutoConfiguration.class.getName()
					+ ".class,"
					+ HibernateJpaAutoConfiguration.class.getName()
					+ ".class}"))
			.parser(Java17Parser.builder()
				.logCompilationWarningsAndErrors(true)
				.classpath("spring-boot-autoconfigure"));
	}
	
	@Test
	void testSimpleSingle()
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
							
							@SpringBootApplication(exclude = {org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration.class,org.springframework.boot.autoconfigure.jdbc.DataSourceTransactionManagerAutoConfiguration.class,org.springframework.boot.autoconfigure.orm.jpa.HibernateJpaAutoConfiguration.class})
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
