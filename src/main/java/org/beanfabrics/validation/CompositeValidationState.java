/*
 *  Beanfabrics Framework
 *  Copyright (C) 2009 by Michael Karneim, beanfabrics.org
 *  Use is subject to license terms. See license.txt.
 */  
// TODO javadoc - remove this comment only when the class and all non-public methods and fields are documented
package org.beanfabrics.validation;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

/**
 * 
 * @author Michael Karneim
 */
public class CompositeValidationState extends ValidationState {
	private final List<ValidationState> children = new LinkedList<ValidationState>();
	
	public CompositeValidationState(String message, ValidationState... states ) throws IllegalArgumentException {
		super(message);
		for( ValidationState state: states) {
			children.add(state);
		}			
	}
	
	public CompositeValidationState(String message, Collection<ValidationState> states ) throws IllegalArgumentException {
		super(message);
		children.addAll(states);
	}

	public List<ValidationState> getChildren() {
		return children;
	}
	
}