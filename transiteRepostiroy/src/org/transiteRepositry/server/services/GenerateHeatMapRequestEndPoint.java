package org.transiteRepositry.server.services;

import org.springframework.ws.server.endpoint.AbstractMarshallingPayloadEndpoint;
import org.transiteRepositry.server.logic.DataMining;
import org.transiteRepositry.server.request.GenerateHeatMapRequest;
import org.transiteRepositry.server.response.GenerateHeatMapReponse;


public class GenerateHeatMapRequestEndPoint extends AbstractMarshallingPayloadEndpoint {

	private DataMining generateHeatMapHandler = null;

	/**
	 * @return the generateHeatMapHandler
	 */
	public DataMining getGenerateHeatMapHandler() {
		return generateHeatMapHandler;
	}

	/**
	 * @param generateHeatMapHandler the generateHeatMapHandler to set
	 */
	public void setGenerateHeatMapHandler(DataMining generateHeatMapHandler) {
		this.generateHeatMapHandler = generateHeatMapHandler;
	}

	/*
	 * (non-Javadoc)
	 * @see org.springframework.ws.server.endpoint.AbstractMarshallingPayloadEndpoint#invokeInternal(java.lang.Object)
	 */
	@Override
	protected Object invokeInternal(Object arg0) throws Exception {
		GenerateHeatMapRequest request = GenerateHeatMapRequest.class.cast(arg0);
		
		return new GenerateHeatMapReponse(this.getGenerateHeatMapHandler().createHeatMap(request));
	}

}
