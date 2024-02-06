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

import org.jetbrains.annotations.NotNull;
import org.openrewrite.ExecutionContext;
import org.openrewrite.Option;
import org.openrewrite.Recipe;
import org.openrewrite.TreeVisitor;

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
public class AddAttributeToAnnotationIfClassExists extends Recipe
{
	@Option(displayName = "::::TODO:::Existing annotation type",
		description =
			"::::TODO:::Annotation type that is already existing. Recipe is looking for this annotation to add the "
				+ "new "
				+ "annotation.",
		example = "software.xdev")
	String className;
	
	@Option(displayName = "Annotation Type",
		description = "The fully qualified name of the annotation.",
		example = "org.junit.Test")
	String annotationType;
	
	@Option(displayName = "Attribute name",
		description = "The name of attribute to change. If omitted defaults to 'value'.",
		required = false,
		example = "timeout")
	String attributeName;
	
	@Option(displayName = "Attribute value",
		description = "The value to set the attribute to. Set to `null` to remove the attribute.",
		example = "500")
	String attributeValue;
	
	@Override
	public @NotNull String getDisplayName()
	{
		return "AddValueToAnnotationIfDependencyExists";
	}
	
	@Override
	public @NotNull String getDescription()
	{
		return "::::TODO:::Add the a new annotation to an existing annotation.";
	}
	
	@Override
	public @NotNull TreeVisitor<?, ExecutionContext> getVisitor()
	{
		if(!this.doesClasspathContainClass(this.className))
		{
			return TreeVisitor.noop();
		}
		return new AddOrUpdateAnnotationAttribute(
			this.annotationType,
			this.attributeName,
			this.attributeValue,
			false
		).getVisitor();
	}
	
	private boolean doesClasspathContainClass(final String classToCheck)
	{
		try
		{
			Class.forName(classToCheck, false, this.getClass().getClassLoader());
			return true;
		}
		catch(final ClassNotFoundException e)
		{
			return false;
		}
	}
}
