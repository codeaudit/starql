/*
 * Field.java
 * Created on May 17, 2013
 *
 * Copyright 2013 Lithium Technologies, Inc. 
 * Emeryville, California, U.S.A.  All Rights Reserved.
 *
 * This software is the  confidential and proprietary information
 * of  Lithium  Technologies,  Inc.  ("Confidential Information")
 * You shall not disclose such Confidential Information and shall 
 * use  it  only in  accordance  with  the terms of  the  license 
 * agreement you entered into with Lithium.
 */

package lithium.ldn.starql.model;

import java.util.Collections;
import java.util.List;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;

import com.google.common.collect.Lists;

/**
 * @author David Esposito
 */
public class QlFieldTree {

	private final String name;
	private final List<QlFieldTree> subObjects;
	private final boolean isStar;

	public final String getName() {
		return name;
	}
	
	public final boolean isStar() {
		return isStar;
	}
	
	public final boolean hasSubObjects() {
		return subObjects != null && !subObjects.isEmpty();
	}
	
	public final List<QlFieldTree> getSubObjects() {
		return subObjects;
	}
	
	public QlFieldTree(String name) {
		this(name, null);
	}

	public QlFieldTree(String name, List<QlFieldTree> subFields) {
		this.name = name;
		this.isStar = name.equals("*");
		this.subObjects = subFields;
	}

	@Override
	public String toString() {
		return "QlField [name=" + name + ", isStar=" + isStar + "]";
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
		private List<QlFieldTree> fields = Lists.newArrayList();
		public List<QlFieldTree> getFields() {
			return fields;
		}
		public CollectionBuilder add(String name) {
			return add(name, null);
		}
		public CollectionBuilder add(String name, List<QlFieldTree> subFields) {
			return add(new QlFieldTree(name, subFields));
		}
		public CollectionBuilder add(QlFieldTree field) {
			fields.add(field);
			return this;
		}
		public List<QlFieldTree> build() {
			return Collections.unmodifiableList(fields);
		}
	}
}
