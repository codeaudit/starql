package com.lithium.ldn.starql.models;

import java.util.Collections;
import java.util.List;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;

import com.google.common.collect.Lists;

/**
 * Recursive definition for a field of a resource.
 * 
 * @author David Esposito
 */
public class QlField {

	private final String name;
	private final String qualifiedName;
	private final QlField subObject;
	private final boolean isStar;

	/**
	 * Get the name of the current field. Parent and sub fields are not included in the string.
	 *  
	 * @return Get the name.
	 */
	public final String getName() {
		return name;
	}
	
	/**
	 * Get the fully qualified name treating this field as root ancestor. Parent field names are
	 * not included in this string.
	 * 
	 * @return Name of this field and all sub-fields.
	 */
	public final String getQualifiedName() {
		return qualifiedName;
	}
	
	public final boolean isStar() {
		return isStar;
	}
	
	public final boolean hasSubObject() {
		return subObject != null;
	}
	
	public final QlField getSubObject() {
		return subObject;
	}

	public QlField(String name, String...subObjectNames) {
		this(name, 0, subObjectNames);
	}

	private QlField(String name, int index, String...subObjectNames) {
		this.name = name;
		this.isStar = name.equals("*");
		this.subObject = index < subObjectNames.length
				? new QlField(subObjectNames[index], index+1, subObjectNames)
				: null;
		this.qualifiedName = hasSubObject()
						? name + "." + subObject.getQualifiedName()
						: name;
	}

	@Override
	public String toString() {
		return "QlField [name=" + name + ", subObject=" + subObject + ", isStar=" + isStar + "]";
	}

	@Override
	public int hashCode() {
		return HashCodeBuilder.reflectionHashCode(this);
	}

	@Override
	public boolean equals(Object obj) {
		return EqualsBuilder.reflectionEquals(this, obj);
	}
	
	public static class CollectionBuilder {
		private List<QlField> fields = Lists.newArrayList();
		public List<QlField> getFields() {
			return fields;
		}
		public CollectionBuilder add(String name, String...subFieldNames) {
			return add(new QlField(name, subFieldNames));
		}
		public CollectionBuilder add(QlField field) {
			fields.add(field);
			return this;
		}
		/**
		 * 
		 * @return An unmutable representation of the current build. Never {@code null}.
		 */
		public List<QlField> build() {
			return Collections.unmodifiableList(fields);
		}
	}
}
