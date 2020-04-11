package me.math.grid.tiled;

import me.database.mongo.IDocument;

public interface IGridDocument extends IDocument {

	String INDEX = "index";
	String MBR = "MBR";
	String TILE_INDEX = "tileIndex";
	String GRID = "grid";
	String CORNER = "corner";
	
}
