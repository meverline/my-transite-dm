package org.transiteRepositry.server.services;

import org.springframework.ws.server.endpoint.AbstractMarshallingPayloadEndpoint;
import org.transiteRepositry.server.logic.StopQuery;
import org.transiteRepositry.server.request.TransiteStopQueryRequest;
import org.transiteRepositry.server.response.TransiteStopQueryResponse;


public class TransiteStopQueryRequestEndPoint extends
		AbstractMarshallingPayloadEndpoint {

	private StopQuery stopQueryHandler = null;
	
	/*
	 * (non-Javadoc)
	 * @see org.springframework.ws.server.endpoint.AbstractMarshallingPayloadEndpoint#invokeInternal(java.lang.Object)
	 */
	
	/**
	 * @return the stopQueryHandler
	 */
	public StopQuery getStopQueryHandler() {
		return stopQueryHandler;
	}

	/**
	 * @param stopQueryHandler the stopQueryHandler to set
	 */
	public void setStopQueryHandler(StopQuery stopQueryHandler) {
		this.stopQueryHandler = stopQueryHandler;
	}

	@Override
	protected Object invokeInternal(Object arg0) throws Exception {
		TransiteStopQueryRequest request = TransiteStopQueryRequest.class.cast(arg0);
		
		return new TransiteStopQueryResponse( this.getStopQueryHandler().query(request));
	}

}
