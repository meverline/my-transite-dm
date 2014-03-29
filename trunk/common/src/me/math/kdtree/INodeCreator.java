package me.math.kdtree;

import me.math.grid.AbstractSpatialGridPoint;

public interface INodeCreator {

	INode create(AbstractSpatialGridPoint loc, INode.Direction dir, INode parent, int depth);
}
