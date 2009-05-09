/*
 *  Beanfabrics Framework
 *  Copyright (C) 2009 by Michael Karneim, beanfabrics.org
 *  Use is subject to license terms. See license.txt.
 */
// TODO javadoc - remove this comment only when the class and all non-public methods and fields are documented
package org.beanfabrics.model;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.beanfabrics.Path;
import org.beanfabrics.PathEvaluation;
import org.beanfabrics.util.OrderPreservingMap;

/**
 * Internal helper class for sorting models by one ore more of their properties.
 *
 * @author Michael Karneim
 */
class SortingHelper {


	public <K,V extends PresentationModel> void sortBy(OrderPreservingMap<K,V> map, SortKey ... sortKeys) {
		if ( map.isEmpty()) {
			return;
		}
		List<Map.Entry<K, V>> list = new ArrayList<Map.Entry<K, V>>(map.entrySet());
		List<SortKey> reverseKeys = Arrays.asList(sortKeys);
		Collections.reverse(reverseKeys);
		for( SortKey key: reverseKeys) {
			Path path = key.getSortPath();
			boolean ascending = key.isAscending();
			Collections.sort(list,
					new EntryComparator(
							new PathComparator(
									new ModelComparatorImpl( ascending?1:-1)
									, path)
							)
			);
		}
		Collection<K> keys = new LinkedList<K>();
		for( Map.Entry<K, V> entry: list) {
			keys.add( entry.getKey());
		}
		map.reorder(keys);
	}

	public <T extends PresentationModel> void sortBy( List<T> list, SortKey ... sortKeys) {
		if ( list.isEmpty()) {
			return; // nothing to do
		}
		List<SortKey> reverseKeys = Arrays.asList(sortKeys);
		Collections.reverse(reverseKeys);
		for( SortKey key: reverseKeys) {
			Path path = key.getSortPath();
			boolean ascending = key.isAscending();
			Collections.sort(list,
					new RowComparator(
							new PathComparator(
									new ModelComparatorImpl( ascending?1:-1)
									, path)
							)
			);
		}
	}

	private static class ModelComparatorImpl implements Comparator<PresentationModel> {
		final int orderFactor;

		public ModelComparatorImpl(int orderFactor) {
			this.orderFactor = orderFactor;
		}
		public int compare(PresentationModel o1, PresentationModel o2) {
			if ( o1 instanceof IValuePM && o2 instanceof IValuePM) {
				return compare((IValuePM)o1,(IValuePM)o2);
			} else {
				// TODO (mk) we will support also comparison between other model types
				// if they implement a special interface that has a getComparable() method
				// or that is Comparable  itself
				throw new UnsupportedOperationException("comparing objects of this type is not supported yet");
			}
		}
		public int compare(IValuePM pModel1, IValuePM pModel2) {
            final int result;
            if (pModel1 == null) {
            	result = -1;
            } else {
            	Comparable c1 = pModel1.getComparable();
                if (c1 == null) {
                	result = -1;
                } else if (pModel2 == null) {
                	result = +1;
                } else {
                	Comparable c2 = pModel2.getComparable();
                	if (c2 == null) {
	                	result = +1;
	                } else {
	                	result = c1.compareTo(c2);
	                }
                }
            }
			return result * orderFactor;
		}
	}

	private static class PathComparator implements Comparator<PresentationModel> {
		private Path path;
		private Comparator<PresentationModel> delegate;

		public PathComparator(Comparator<PresentationModel> delegate, Path path) {
			this.delegate = delegate;
			this.path = path;
		}

		public int compare(PresentationModel c1, PresentationModel c2) {
			PresentationModel pModel1 = PathEvaluation.evaluateOrNull(c1, path);
            PresentationModel pModel2 = PathEvaluation.evaluateOrNull(c2, path);

            if ( pModel1!=null && pModel1 instanceof IValuePM==false) {
            	throw new IllegalArgumentException("PresentationModel at path "+path.toString()+" must be instance of "+IValuePM.class.getName()+" but was "+pModel1.getClass().getName());
            }
            if ( pModel2!=null && pModel2 instanceof IValuePM==false) {
            	throw new IllegalArgumentException("PresentationModel at path "+path.toString()+" must be instance of "+IValuePM.class.getName()+" but was "+pModel2.getClass().getName());
            }
            IValuePM valueMdl1 = (IValuePM)pModel1;
            IValuePM valueMdl2 = (IValuePM)pModel2;
            return delegate.compare(valueMdl1, valueMdl2);
		}
	}

	private static class EntryComparator implements Comparator {
		final PathComparator delegate;

		public EntryComparator(PathComparator delegate) {
			this.delegate = delegate;
		}
		public int compare(Object o1, Object o2) {
			Map.Entry e1 = (Map.Entry)o1;
			Map.Entry e2 = (Map.Entry)o2;
			PresentationModel row1 = (PresentationModel) e1.getValue();
			PresentationModel row2 = (PresentationModel) e2.getValue();

            return delegate.compare(row1, row2);
		}
	}

	private static class RowComparator implements Comparator {
		private final PathComparator delegate;

    	public RowComparator(PathComparator delegate) {
    		this.delegate = delegate;
    	}
        public int compare(Object o1, Object o2) {
            PresentationModel row1 = (PresentationModel) o1;
            PresentationModel row2 = (PresentationModel) o2;

            return delegate.compare(row1, row2);
        }
    }
}