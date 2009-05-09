/*
 *  Beanfabrics Framework
 *  Copyright (C) 2009 by Michael Karneim, beanfabrics.org
 *  Use is subject to license terms. See license.txt.
 */  
// TODO javadoc - remove this comment only when the class and all non-public methods and fields are documented
package org.beanfabrics.swing;

import javax.swing.JMenuItem;

import org.beanfabrics.IModelProvider;
import org.beanfabrics.Link;
import org.beanfabrics.ModelProvider;
import org.beanfabrics.ModelSubscriber;
import org.beanfabrics.Path;
import org.beanfabrics.model.IOperationPM;
import org.beanfabrics.swing.internal.OperationPMMenuItem;

/**
 * The <code>BnMenuItem</code> is a {@link JMenuItem} that can subscribe to an {@link IOperationPM}.
 * 
 * @author Max Gensthaler
 * 
 * @beaninfo
 */
@SuppressWarnings("serial")
public class BnMenuItem extends OperationPMMenuItem implements ModelSubscriber {
	private final Link link = new Link(this);

	/**
	 * Constructs a <code>BnMenuItem</code>.
	 */
	public BnMenuItem() {
	}
	
	/**
	 * Constructs a <code>BnMenuItem</code> and binds it 
	 * to the specified model.
	 * @param pModel the model
	 */
	public BnMenuItem(IOperationPM pModel) {
		super( pModel);
	}
	
	/**
	 * Constructs a <code>BnMenuItem</code> and subscribes it 
	 * for the model at the specified Path provided by the given provider.
	 * @param provider
	 * @param path
	 */
	public BnMenuItem(ModelProvider provider, Path path) {
		this.setModelProvider(provider);
		this.setPath(path);
	}

	/**
	 * Constructs a <code>BnMenuItem</code> and subscribes it 
	 * for the model at the root level provided by the given provider.
	 * @param provider
	 */
	public BnMenuItem(ModelProvider provider) {
		this.setModelProvider(provider);
		this.setPath(new Path());
	}
	
	/** {@inheritDoc} */
	public IModelProvider getModelProvider() {
		return link.getModelProvider();
	}

	/** {@inheritDoc} */
	public void setModelProvider(IModelProvider provider) {
		this.link.setModelProvider(provider);
	}

	/** {@inheritDoc} */
	public Path getPath() {
		return link.getPath();
	}

	/** {@inheritDoc} */
	public void setPath(Path path) {
		this.link.setPath(path);
	}
}