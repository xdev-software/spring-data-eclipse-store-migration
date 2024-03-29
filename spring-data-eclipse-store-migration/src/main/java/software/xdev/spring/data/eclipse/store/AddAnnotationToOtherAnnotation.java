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

import java.util.Comparator;

import org.jetbrains.annotations.NotNull;
import org.openrewrite.ExecutionContext;
import org.openrewrite.Option;
import org.openrewrite.Recipe;
import org.openrewrite.TreeVisitor;
import org.openrewrite.java.AnnotationMatcher;
import org.openrewrite.java.JavaIsoVisitor;
import org.openrewrite.java.JavaParser;
import org.openrewrite.java.JavaTemplate;
import org.openrewrite.java.tree.J;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class AddAnnotationToOtherAnnotation extends Recipe
{
	@Option(displayName = "Existing annotation type",
		description = "Annotation type that is already existing. Recipe is looking for this annotation to add the new "
			+ "annotation.",
		example = "org.junit.jupiter.api.Test")
	String existingAnnotationType;
	
	@Option(displayName = "Annotation type to add",
		description = "Annotation type that should be added to the existing annotation type.",
		example = "org.junit.jupiter.api.Test")
	String annotationTypeToAdd;
	
	@Option(displayName = "Class path",
		description = "Class path that needs to be added for the new annotation. Can be empty.",
		example = "spring-data-eclipse-store",
		required = false)
	String classPath;
	
	@Option(displayName = "Simple name of annotation type to add",
		description = "Simple name of annotation type that should be added to the existing annotation type.",
		example = "Test")
	String annotationTypeToAddSimpleName;
	
	@Override
	public @NotNull String getDisplayName()
	{
		return "AddAnnotationToOtherAnnotation";
	}
	
	@Override
	public @NotNull String getDescription()
	{
		return "Add the a new annotation to an existing annotation.";
	}
	
	@Override
	public @NotNull TreeVisitor<?, ExecutionContext> getVisitor()
	{
		final AnnotationMatcher existingAnnotationMatcher = new AnnotationMatcher(this.existingAnnotationType);
		final AnnotationMatcher newAnnotationMatcher = new AnnotationMatcher(this.annotationTypeToAdd);
		return new JavaIsoVisitor<>()
		{
			@Override
			public J.@NotNull ClassDeclaration visitClassDeclaration(
				final J.@NotNull ClassDeclaration classDecl,
				final @NotNull ExecutionContext executionContext)
			{
				final J.ClassDeclaration cd = super.visitClassDeclaration(classDecl, executionContext);
				
				if(
					cd.getLeadingAnnotations()
						.stream()
						.filter(existingAnnotationMatcher::matches)
						.findAny()
						.isEmpty() || cd.getLeadingAnnotations()
						.stream()
						.anyMatch(newAnnotationMatcher::matches)
				)
				{
					return cd;
				}
				
				final JavaTemplate componentAnnotationTemplate =
					JavaTemplate
						.builder("#{}")
						.javaParser(JavaParser.fromJavaVersion()
							.classpath(AddAnnotationToOtherAnnotation.this.classPath))
						.imports(AddAnnotationToOtherAnnotation.this.annotationTypeToAdd)
						.build();
				
				this.maybeAddImport(AddAnnotationToOtherAnnotation.this.annotationTypeToAdd);
				
				return componentAnnotationTemplate.apply(
					this.getCursor(),
					cd.getCoordinates().addAnnotation(Comparator.comparing(J.Annotation::getSimpleName)),
					"@" + AddAnnotationToOtherAnnotation.this.annotationTypeToAddSimpleName
				);
			}
		};
	}
}
