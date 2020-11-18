package me.math.kdtree;

import me.math.grid.SpatialGridPoint;

public interface INodeCreator {

	INode create(SpatialGridPoint loc, INode.Direction dir, INode parent, int depth);
}
