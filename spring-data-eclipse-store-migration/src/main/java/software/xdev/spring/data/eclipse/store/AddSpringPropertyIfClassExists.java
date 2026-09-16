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

import java.util.List;
import java.util.Objects;

import org.jetbrains.annotations.NotNull;
import org.openrewrite.ExecutionContext;
import org.openrewrite.Option;
import org.openrewrite.Recipe;
import org.openrewrite.TreeVisitor;
import org.openrewrite.internal.lang.Nullable;
import org.openrewrite.java.spring.AddSpringProperty;


public class AddSpringPropertyIfClassExists extends Recipe
{
	@Option(displayName = "Class that must exist in the classpath to add the property",
		description = "Name of the class that must exist in the classpath to execute the "
			+ "recipe to add a property in the properties file.",
		example = "software.xdev")
	private String className;
	
	@Option(
		displayName = "Property key",
		description = "The property key to add.",
		example = "management.metrics.enable.process.files"
	)
	private String property;
	@Option(
		displayName = "Property value",
		description = "The value of the new property key.",
		example = "true"
	)
	private String value;
	@Option(
		displayName = "Optional comment to be prepended to the property",
		description = "A comment that will be added to the new property.",
		required = false,
		example = "This is a comment"
	)
	private @Nullable String comment;
	@Option(
		displayName = "Optional list of file path matcher",
		description = "Each value in this list represents a glob expression that is used to match which files will be "
			+ "modified. If this value is not present, this recipe will query the execution context for reasonable "
			+ "defaults. (\"**/application.yml\", \"**/application.yml\", and \"**/application.properties\".",
		required = false,
		example = "[\"**/application.yml\"]"
	)
	private @Nullable List<String> pathExpressions;
	
	public AddSpringPropertyIfClassExists()
	{
	}
	
	public AddSpringPropertyIfClassExists(
		final String className,
		final String property,
		final String value,
		final String comment,
		final List<String> pathExpressions)
	{
		this.className = className;
		this.property = property;
		this.value = value;
		this.comment = comment;
		this.pathExpressions = pathExpressions;
	}
	
	// region Accessors
	public String getClassName()
	{
		return this.className;
	}
	
	public void setClassName(final String className)
	{
		this.className = className;
	}
	
	public String getProperty()
	{
		return this.property;
	}
	
	public void setProperty(final String property)
	{
		this.property = property;
	}
	
	public String getValue()
	{
		return this.value;
	}
	
	public void setValue(final String value)
	{
		this.value = value;
	}
	
	public String getComment()
	{
		return this.comment;
	}
	
	public void setComment(final String comment)
	{
		this.comment = comment;
	}
	
	public List<String> getPathExpressions()
	{
		return this.pathExpressions;
	}
	
	public void setPathExpressions(final List<String> pathExpressions)
	{
		this.pathExpressions = pathExpressions;
	}
	// endregion
	
	@Override
	public @NotNull String getDisplayName()
	{
		return "AddSpringPropertyIfClassExists";
	}
	
	@Override
	public @NotNull String getDescription()
	{
		return "Add a spring property to the properties file if a specific class exists.";
	}
	
	@Override
	public @NotNull TreeVisitor<?, ExecutionContext> getVisitor()
	{
		if(!this.doesClasspathContainClass(this.className))
		{
			return TreeVisitor.noop();
		}
		return new AddSpringProperty(
			this.property, this.value, this.comment, this.pathExpressions
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
	
	@Override
	public boolean equals(final Object o)
	{
		if(!(o instanceof final AddSpringPropertyIfClassExists that))
		{
			return false;
		}
		if(!super.equals(o))
		{
			return false;
		}
		return Objects.equals(this.getClassName(), that.getClassName()) && Objects.equals(
			this.getProperty(),
			that.getProperty()) && Objects.equals(this.getValue(), that.getValue()) && Objects.equals(
			this.getComment(),
			that.getComment()) && Objects.equals(this.getPathExpressions(), that.getPathExpressions());
	}
	
	@Override
	public int hashCode()
	{
		return Objects.hash(
			super.hashCode(),
			this.getClassName(),
			this.getProperty(),
			this.getValue(),
			this.getComment(),
			this.getPathExpressions());
	}
}
