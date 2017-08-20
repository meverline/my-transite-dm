package me.math.grid.tiled;

import me.database.mongo.IDocument;

public interface IGridDocument extends IDocument {

	public static final String INDEX = "index";
	public static final String MBR = "MBR";
	public static final String TILE_INDEX = "tileIndex";
	public static final String GRID = "grid";
	public static final String CORNER = "corner";
	
}
