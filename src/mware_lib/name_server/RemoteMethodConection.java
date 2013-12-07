package mware_lib.name_server;

import mware_lib.ServantTypeAssoziation;

import java.io.Serializable;

public class RemoteMethodConection implements Serializable {
	
	private static final long serialVersionUID = -227460439822816353L;
	private final String hostname;
	private final int port;
	private final ServantTypeAssoziation servantType;

	RemoteMethodConection(String hostname, int port, ServantTypeAssoziation servantType)
	{
		this.hostname = hostname;
		this.port = port;
		this.servantType = servantType;
	}

	public String getHostname() {
		return hostname;
	}

	public int getPort() {
		return port;
	}
	
	public ServantTypeAssoziation getServantType() {
		return servantType;
	}
	
	
	
	public String toString() {
		return "RemoteMethodConection [hostname=" + hostname + ", port=" + port
				+ "]";
	}

	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((hostname == null) ? 0 : hostname.hashCode());
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
		RemoteMethodConection other = (RemoteMethodConection) obj;
		if (hostname == null) {
			if (other.hostname != null)
				return false;
		} else if (!hostname.equals(other.hostname))
			return false;
		if (port != other.port)
			return false;
		return true;
	}

}
