package org.transiteRepositry.server.services;

import org.springframework.ws.server.endpoint.AbstractMarshallingPayloadEndpoint;
import org.transiteRepositry.server.logic.StopQuery;
import org.transiteRepositry.server.request.CircleQueryRequest;
import org.transiteRepositry.server.response.TransiteStopQueryResponse;


public class CircleQueryRequestEndpoint  extends AbstractMarshallingPayloadEndpoint  {
	
	private StopQuery circleQuderyRequuestHandler = null;
	
	/**
	 * @return the circleQuderyRequuestHandler
	 */
	public StopQuery getCircleQuderyRequuestHandler() {
		return circleQuderyRequuestHandler;
	}

	/**
	 * @param circleQuderyRequuestHandler the circleQuderyRequuestHandler to set
	 */
	public void setCircleQuderyRequuestHandler(StopQuery circleQuderyRequuestHandler) {
		this.circleQuderyRequuestHandler = circleQuderyRequuestHandler;
	}

	/*
	 * (non-Javadoc)
	 * @see org.springframework.ws.server.endpoint.AbstractMarshallingPayloadEndpoint#invokeInternal(java.lang.Object)
	 */
	@Override
	protected Object invokeInternal(Object arg0) throws Exception {
		
		CircleQueryRequest request = CircleQueryRequest.class.cast(arg0);

		return new TransiteStopQueryResponse(this.getCircleQuderyRequuestHandler().query(request));
	}

}
