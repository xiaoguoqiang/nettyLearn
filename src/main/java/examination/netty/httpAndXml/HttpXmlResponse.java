package examination.netty.httpAndXml;

import io.netty.handler.codec.http.FullHttpResponse;

public class HttpXmlResponse {

	private FullHttpResponse httpResponse;
	private Object body;

	public HttpXmlResponse(FullHttpResponse httpResponse, Object body) {
		this.httpResponse = httpResponse;
		this.body = body;
	}

	public final FullHttpResponse getHttpResponse() {
		return httpResponse;
	}

	public final void setHttpResponse(FullHttpResponse httpResponse) {
		this.httpResponse = httpResponse;
	}

	public final Object getBody() {
		return body;
	}

	public final void setBody(Object body) {
		this.body = body;
	}

}
