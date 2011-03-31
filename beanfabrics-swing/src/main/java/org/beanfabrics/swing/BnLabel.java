/*
 * Beanfabrics Framework Copyright (C) 2011 by Michael Karneim, beanfabrics.org
 * Use is subject to license terms. See license.txt.
 */
package org.beanfabrics.swing;

import java.awt.Graphics;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import javax.swing.JLabel;
import javax.swing.SwingConstants;

import org.beanfabrics.IModelProvider;
import org.beanfabrics.Link;
import org.beanfabrics.ModelProvider;
import org.beanfabrics.ModelSubscriber;
import org.beanfabrics.Path;
import org.beanfabrics.View;
import org.beanfabrics.event.WeakPropertyChangeListener;
import org.beanfabrics.model.AbstractPM;
import org.beanfabrics.model.IIconPM;
import org.beanfabrics.model.ITextPM;
import org.beanfabrics.model.IValuePM;

/**
 * The <code>BnLabel</code> is a {@link JLabel} that can subscribe to an
 * {@link ITextPM}.
 * 
 * @author Michael Karneim
 * @author Marcel Eyke
 * @beaninfo
 */
@SuppressWarnings("serial")
public class BnLabel extends JLabel implements View<ITextPM>, ModelSubscriber {
    private final Link link = new Link(this);

    private transient final PropertyChangeListener listener = new WeakPropertyChangeListener() {
        public void propertyChange(PropertyChangeEvent evt) {
            refresh();
        }
    };
    private ITextPM pModel;
    private ErrorIconPainter errorIconPainter = createDefaultErrorIconPainter();

    /**
     * Constructs a <code>BnLabel</code>.
     */
    public BnLabel() {
    }

    /**
     * Constructs a <code>BnLabel</code> and binds it to the specified model.
     * 
     * @param pModel the model
     */
    public BnLabel(ITextPM pModel) {
        this();
        setPresentationModel(pModel);
    }

    /**
     * Constructs a <code>BnLabel</code> and subscribes it for the model at the
     * specified Path provided by the given provider.
     * 
     * @param provider
     * @param path
     */
    public BnLabel(ModelProvider provider, Path path) {
        this.setModelProvider(provider);
        this.setPath(path);
    }

    /**
     * Constructs a <code>BnLabel</code> and subscribes it for the model at the
     * root level provided by the given provider.
     * 
     * @param provider
     */
    public BnLabel(ModelProvider provider) {
        this.setModelProvider(provider);
        this.setPath(new Path());
    }

    /**
     * Returns whether this component is connected to the target
     * {@link AbstractPM} to synchronize with.
     * 
     * @return <code>true</code> when this component is connected, else
     *         <code>false</code>
     */
    boolean isConnected() {
        return this.pModel != null;
    }

    /**
     * Configures this component depending on the target {@link AbstractPM} s
     * attributes.
     */
    private void refresh() {
        refreshText();
        refreshIcon();
        refreshToolTip();
    }

    /**
     * Configures the tool tip of this component on the depending on the target
     * {@link IValuePM}s attributes.
     */
    private void refreshToolTip() {
        if (this.pModel == null) {
            this.setToolTipText(null);
        } else {
            this.setToolTipText(pModel.isValid() == false ? pModel.getValidationState().getMessage() : pModel.getDescription());
        }
    }

    /**
     * Configures the tool tip of this component on the depending on the target
     * {@link IIconPM}s attributes.
     */
    private void refreshIcon() {
        if (this.pModel instanceof IIconPM) {
            IIconPM iconEditor = (IIconPM)this.pModel;
            this.setIcon(iconEditor.getIcon());
        } else {
            this.setIcon(null);
        }
    }

    /**
     * Configures the text of this component on the depending on the target
     * {@link ITextPM}s attributes.
     */
    private void refreshText() {
        if (this.pModel == null) {
            this.setText("");
        } else {
            this.setText(pModel.getText());
        }
    }

    private ErrorIconPainter createDefaultErrorIconPainter() {
        ErrorIconPainter result = new ErrorIconPainter();
        result.setHorizontalAlignment(invertHorizontalAlignment(getHorizontalAlignment()));
        return result;
    }

    public ErrorIconPainter getErrorIconPainter() {
        return errorIconPainter;
    }

    public void setErrorIconPainter(ErrorIconPainter aErrorIconPainter) {
        if (aErrorIconPainter == null) {
            throw new IllegalArgumentException("aErrorIconPainter == null");
        }
        this.errorIconPainter = aErrorIconPainter;
    }

    /** {@inheritDoc} */
    @Override
    public void paintChildren(Graphics g) {
        super.paintChildren(g);
        if (shouldPaintErrorIcon()) {
            errorIconPainter.paint(g, this);
        }
    }

    private boolean shouldPaintErrorIcon() {
        ITextPM pModel = this.getPresentationModel();
        if (pModel == null) {
            return false;
        }
        return (pModel.isValid() == false);
    }

    @Override
    public void setHorizontalAlignment(int alignment) {
        super.setHorizontalAlignment(alignment);
        if (errorIconPainter != null) {
            this.errorIconPainter.setHorizontalAlignment(invertHorizontalAlignment(alignment));
        }
    }

    private int invertHorizontalAlignment(int alignment) {
        switch (alignment) {
            case SwingConstants.LEFT:
                return SwingConstants.RIGHT;
            case SwingConstants.RIGHT:
                return SwingConstants.LEFT;
            case SwingConstants.LEADING:
                return SwingConstants.TRAILING;
            case SwingConstants.TRAILING:
                return SwingConstants.LEADING;
            default:
                return alignment;
        }
    }

    /** {@inheritDoc} */
    public ITextPM getPresentationModel() {
        return pModel;
    }

    /** {@inheritDoc} */
    public void setPresentationModel(ITextPM newModel) {
        ITextPM oldModel = this.pModel;
        if (this.pModel != null) {
            this.pModel.removePropertyChangeListener(listener);
        }
        this.pModel = newModel;
        if (this.pModel != null) {
            this.pModel.addPropertyChangeListener(listener);
        }
        refresh();
        this.firePropertyChange("presentationModel", oldModel, newModel);
    }

    /** {@inheritDoc} */
    public void setModelProvider(IModelProvider provider) {
        this.link.setModelProvider(provider);
    }

    /** {@inheritDoc} */
    public IModelProvider getModelProvider() {
        return link.getModelProvider();
    }

    /** {@inheritDoc} */
    public void setPath(Path path) {
        this.link.setPath(path);
    }

    /** {@inheritDoc} */
    public Path getPath() {
        return link.getPath();
    }
}