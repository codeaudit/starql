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

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;

/**
 * @author David Esposito
 */
public class QlField {

	private final String name;
	private final String qualifiedName;
	private final QlField subObject;
	private final boolean isStar;

	public final String getName() {
		return name;
	}
	
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
	

}
