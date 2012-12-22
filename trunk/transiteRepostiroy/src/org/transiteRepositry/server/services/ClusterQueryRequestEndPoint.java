package org.transiteRepositry.server.services;

import org.springframework.ws.server.endpoint.AbstractMarshallingPayloadEndpoint;
import org.transiteRepositry.server.logic.DataMining;
import org.transiteRepositry.server.request.ClusterQueryRequest;
import org.transiteRepositry.server.response.ClusterQueryReponse;


public class ClusterQueryRequestEndPoint extends AbstractMarshallingPayloadEndpoint {

	private DataMining clusterHandler = null;

	/**
	 * @return the clusterHandler
	 */
	public DataMining getClusterHandler() {
		return clusterHandler;
	}

	/**
	 * @param clusterHandler the clusterHandler to set
	 */
	public void setClusterHandler(DataMining clusterHandler) {
		this.clusterHandler = clusterHandler;
	}
	
	/*
	 * (non-Javadoc)
	 * @see org.springframework.ws.server.endpoint.AbstractMarshallingPayloadEndpoint#invokeInternal(java.lang.Object)
	 */
	@Override
	protected Object invokeInternal(Object arg0) throws Exception {
		ClusterQueryRequest request = ClusterQueryRequest.class.cast(arg0);
	
		return new ClusterQueryReponse(this.getClusterHandler().findClusters(request));
	}

}
