package mware_lib.name_server;

import mware_lib.ServantTypeAssoziation;

import java.io.Serializable;

public class RebindRequest implements Serializable {

	private static final long serialVersionUID = 63911907370711106L;
	
	private final String hostname;
	private final int port;
	private final String methodName;
	private final ServantTypeAssoziation servantType;
	
	public RebindRequest(String hostname, int port, String methodName, ServantTypeAssoziation servantType){
		this.hostname = hostname;
		this.port = port;
		this.methodName = methodName;
		this.servantType = servantType;
	}

	public String getHostname() {
		return hostname;
	}

	public int getPort() {
		return port;
	}

	public String getMethodName() {
		return methodName;
	}
	
	public ServantTypeAssoziation getServantType() {
		return servantType;
	}

	
	public String toString() {
		return "RebindRequest [hostname=" + hostname + ", port=" + port
				+ ", methodName=" + methodName + "]";
	}

	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((hostname == null) ? 0 : hostname.hashCode());
		result = prime * result
				+ ((methodName == null) ? 0 : methodName.hashCode());
		result = prime * result + port;
		return result;
	}

	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		RebindRequest other = (RebindRequest) obj;
		if (hostname == null) {
			if (other.hostname != null)
				return false;
		} else if (!hostname.equals(other.hostname))
			return false;
		if (methodName == null) {
			if (other.methodName != null)
				return false;
		} else if (!methodName.equals(other.methodName))
			return false;
		if (port != other.port)
			return false;
		return true;
	}
	
	

}
