package mware_lib;

import mware_lib.networking.MethodCaller;
import mware_lib.networking.Skeleton;

import java.io.IOException;
import java.io.Serializable;
import java.util.concurrent.atomic.AtomicInteger;

public abstract class Servant implements MethodCaller {
	public static final int FIRST_SERVER_PORT = 8192;

	private static final AtomicInteger nextPort = new AtomicInteger(FIRST_SERVER_PORT);
	protected final Object servant;
	protected final Skeleton server;

	public Servant(final Object servant) throws IOException {
		this.servant = servant;
		this.server = new Skeleton(nextPort.getAndAdd(1), this, true);
	}

	public final String getHost() {
		return server.getHost();
	}

	public final int getPort() {
		return server.getPort();
	}

	public abstract Serializable callMethod(final String methodName, final Serializable parametersObject);

}
