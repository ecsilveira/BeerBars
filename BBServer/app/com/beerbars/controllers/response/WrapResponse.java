package com.beerbars.controllers.response;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;

import com.beerbars.ServerConfiguration;
import com.beerbars.controllers.helper.WrapResponseHelper;
import com.beerbars.logging.ServerLogger;
import com.beerbars.util.ServerJson;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

import play.core.j.JavaResultExtractor;
import play.libs.F;
import play.libs.Json;
import play.mvc.Http.Context;
import play.mvc.Http.Request;
import play.mvc.Http.RequestHeader;
import play.mvc.Http.Response;
import play.mvc.Results;
import play.mvc.SimpleResult;

/**
 * Classe Reponsavel pelas respostas dos Controllers Monta as saidas dos Filters tambem
 * 
 * @author B35579
 */
public class WrapResponse {

	/***
	 * Pattern should be thread safe: https://docs.oracle.com/javase/8/docs/api/java/util/regex/Pattern.html "Instances of this class are immutable and are safe for use by multiple concurrent threads. Instances of the Matcher class are not safe for such use." This pattern matches "at package.class.method(source_code:123)" Note that "source_code" can be a java, scala or js file!
	 */
	private static Pattern tracePattern = Pattern.compile("\\s*at\\s+([\\w\\.$_]+)\\.([\\w$_]+)(\\(.*\\..*)?:(\\d+)\\)(\\n|\\r\\n)");

	// This pattern matches " package.class.exception : optional message'"
	private static Pattern headLinePattern = Pattern.compile("([\\w\\.]+)(:.*)?");

	/* inspired by https://stackoverflow.com/questions/10013713/reading-and-parsing-java-exceptions */
	private List<String> tryToExtractTheStackTrace(String error) {
		Matcher traceMatcher = tracePattern.matcher(error);
		List<String> stackTrace = new ArrayList<String>();
		while (traceMatcher.find()) {
			String className = traceMatcher.group(1);
			String methodName = traceMatcher.group(2);
			String sourceFile = traceMatcher.group(3);
			int lineNum = Integer.parseInt(traceMatcher.group(4));
			stackTrace.add(new StringBuilder().append(className).append(".").append(methodName).append("(").append(sourceFile).append(":").append(lineNum).append(")").toString());
		}
		return stackTrace;
	}

	private ObjectNode prepareError(RequestHeader request, String error) {
		// List<StackTraceElement> st = this.tryToExtractTheStackTrace(error);
		List<String> st = this.tryToExtractTheStackTrace(error);

		ObjectMapper mapper = ServerJson.mapper();
		ObjectNode result = Json.newObject();
		result.put("result", "error");

		// the error is an exception or a plain message?
		if (st.size() == 0) {
			result.put("message", error);
		} else {
			Matcher headLineMatcher = headLinePattern.matcher(error);
			StringBuilder message = new StringBuilder();
			if (headLineMatcher.find()) {
				message.append(headLineMatcher.group(1));
				if (headLineMatcher.group(2) != null) {
					message.append(" ").append(headLineMatcher.group(2));
				}
			}
			result.put("message", message.toString());
			ArrayNode ston = result.putArray("stacktrace");
			for (String linha : st) {
				ston.add(linha);
			}
			result.put("full_stacktrace", error);
		}
		result.put("resource", request.path());
		result.put("method", request.method());
		result.put("request_header", (JsonNode) mapper.valueToTree(request.headers()));
		result.put("sv_version", ServerConfiguration.getServerVersion());
		result.put("db_schema_version", ServerConfiguration.getDatabaseVersion());
		this.setCallIdOnResult(request, result);
		return result;
	}

	private SimpleResult onCustomCode(HttpMessagesEnum customCode, Context ctx, String data) throws Exception {
		Request request = ctx.request();
		if (customCode.getType().equals("error")) {
			ObjectNode result = null;
			result = prepareError(request, data);
			result.put("http_code", customCode.getHttpCode());
			result.put("server_code", String.valueOf(customCode.getServerCode()));
			return Results.status(customCode.getHttpCode(), result);
		} else {
			StringBuilder toReturn = new StringBuilder("{\"http_code\":").append(customCode.getHttpCode()).append(",\"server_code\":").append(String.valueOf(customCode.getServerCode())).append(",").append(prepareOK(ctx, data)).append("}");
			return Results.status(customCode.getHttpCode(), toReturn.toString());
		}

	}

	private SimpleResult onUnauthorized(Context ctx, String error) {
		Request request = ctx.request();
		ObjectNode result = prepareError(request, error);
		result.put("http_code", 401);
		return Results.unauthorized(result);
	}

	private SimpleResult onForbidden(Context ctx, String error) {
		Request request = ctx.request();
		ObjectNode result = prepareError(request, error);
		result.put("http_code", 403);
		return Results.forbidden(result);
	}

	private SimpleResult onBadRequest(Context ctx, String error) {
		Request request = ctx.request();
		ObjectNode result = prepareError(request, error);
		return Results.badRequest(result);
	}

	private SimpleResult onResourceNotFound(Context ctx, String error) {
		Request request = ctx.request();
		ObjectNode result = prepareError(request, error);
		result.put("http_code", 404);
		return Results.notFound(result);
	}

	private SimpleResult onDefaultError(int statusCode, Context ctx, String error) {
		Request request = ctx.request();
		ObjectNode result = prepareError(request, error);
		result.put("http_code", statusCode);
		return Results.status(statusCode, result);
	}

	private String prepareOK(Context ctx, String stringBody) throws Exception {
		StringBuilder toReturn = new StringBuilder(stringBody == null ? 100 : stringBody.length() + 100);
		try {
			toReturn.append(WrapResponseHelper.preludeOk(ctx));
			if (stringBody == null) {
				toReturn.append("null");
			} else if (StringUtils.equals(stringBody, "null")) { // the body contains null (as a string with a n-u-l-l characters sequence, and this
																	// must be no wrapped into " like a normal string content
				toReturn.append("null");
			} else if (StringUtils.startsWithAny(stringBody, new String[] { "{", "[" })) { // the body to return is a JSON and must be appended as-is
				toReturn.append(stringBody);
			} else if (stringBody.startsWith("\"") && stringBody.endsWith("\"")) {
				// the body to return is a simple String already wrapped. Only quotes must be escaped (?)
				toReturn.append(stringBody/* .replace("\"","\\\"") */);
			} else if (NumberUtils.isNumber(stringBody)) {
				toReturn.append(stringBody);
			} else if ("true".equals(stringBody) || "false".equals(stringBody)) {
				toReturn.append(stringBody);
			} else {// the body is a just a simple string and must be wrapped
				toReturn.append("\"").append(stringBody/* .replace("\"","\\\"") */).append("\"");
			}
		} catch (Throwable e) {
			throw new Exception("Error parsing stringBody: " + StringUtils.abbreviate(stringBody, 100), e);
		}
		return toReturn.toString();
	}

	/**
	 * @param request
	 * @param result
	 */
	private void setCallIdOnResult(RequestHeader request, ObjectNode result) {
		String callId = request.getQueryString("call_id");
		if (!StringUtils.isEmpty(callId)) {
			result.put("call_id", callId);
		}
	}

	private void setServerTime(Response response) {
		ZonedDateTime date = ZonedDateTime.now(ZoneId.of("GMT"));
		String httpDate = DateTimeFormatter.RFC_1123_DATE_TIME.format(date);
		response.setHeader("Date", httpDate);
	}

	private SimpleResult onOk(int statusCode, Context ctx, String stringBody) throws Exception {
		StringBuilder toReturn = new StringBuilder("{").append(prepareOK(ctx, stringBody));
		stringBody = null;
		toReturn.append(WrapResponseHelper.endOk(statusCode));
		toReturn.append("}");
		return Results.status(statusCode, toReturn.toString());
	}

	/**
	 * Metodo responsavel por montar o retorno da resposta HTTP
	 * 
	 * @param ctx
	 * @param simpleResult
	 * @return
	 * @throws Throwable
	 */
	public SimpleResult wrap(Context ctx, F.Promise<SimpleResult> simpleResult) throws Throwable {
		ServerLogger.debug("WrapResponse.wrap - begin...");

		SimpleResult result = simpleResult.get(10000);
		ctx.response().setHeader("Access-Control-Allow-Methods", "POST, GET, PUT, DELETE, OPTIONS");
		ctx.response().setHeader("Access-Control-Allow-Origin", "*");
		ctx.response().setHeader("Access-Control-Allow-Headers", "X-Requested-With");

		// this is an hack because scala can't access to the http context, and we need this information for the access log
		String username = (String) ctx.args.get("username");
		if (username != null) {
			ctx.response().setHeader("X-BB-USERNAME", username);
		}

		final int statusCode = result.getWrappedSimpleResult().header().status();
		ServerLogger.debug("WrapResponse.wrap - Executed API: " + ctx.request() + " , return code " + statusCode);
		ServerLogger.debug("WrapResponse.wrap - Result type:" + result.getWrappedResult().getClass().getName() + " Response Content-Type:" + ctx.response().getHeaders().get("Content-Type"));

		if (ctx.response().getHeaders().get("Content-Type") != null && !ctx.response().getHeaders().get("Content-Type").contains("json")) {
			ServerLogger.debug("WrapResponse.wrap - The response is a file, no wrap will be applied");
			return result;
		}

		String transferEncoding = JavaResultExtractor.getHeaders(result).get(HttpConstants.Headers.TRANSFER_ENCODING);
		if (transferEncoding != null && transferEncoding.equals(HttpConstants.HttpProtocol.CHUNKED)) {
			return result;
		}

		final byte[] body = JavaResultExtractor.getBody(result);
		String stringBody = new String(body, "UTF-8");
		ServerLogger.info("WrapResponse.wrap - stringBody: " + stringBody);

		if (statusCode > 399) { // an error has occured
			switch (statusCode) {
			case 400:
				result = onBadRequest(ctx, stringBody);
				break;
			case 401:
				result = onUnauthorized(ctx, stringBody);
				break;
			case 403:
				result = onForbidden(ctx, stringBody);
				break;
			case 404:
				result = onResourceNotFound(ctx, stringBody);
				break;
			default:
				HttpMessagesEnum customCode = HttpMessagesEnum.getFromServerCode(statusCode);
				if (customCode != null) {
					result = onCustomCode(customCode, ctx, stringBody);
				} else {
					result = onDefaultError(statusCode, ctx, stringBody);
				}
				break;
			}
		} else { // status is not an error
			result = onOk(statusCode, ctx, stringBody);
		}

		if (statusCode == 204) {
			result = Results.noContent();
		}

		try {
			ServerLogger.info("WrapResponse.wrap - result: \n" + result.toString() + "\n  --> Body:\n" + new String(JavaResultExtractor.getBody(result), "UTF-8"));
		} catch (Throwable e) {
		}

		setServerTime(ctx.response());
		ctx.response().setContentType("application/json; charset=utf-8");
		ctx.response().setHeader("Content-Length", Long.toString(JavaResultExtractor.getBody(result).length));
		
		ServerLogger.debug("WrapResponse.wrap - end");
		return result;
	}// wrap

	/**
	 * Metodo responsavel por montar o retorno da resposta HTTP Assincrona
	 * 
	 * @param ctx
	 * @param simpleResult
	 * @return
	 * @throws Throwable
	 */
	public F.Promise<SimpleResult> wrapAsync(Context ctx, F.Promise<SimpleResult> simpleResult) throws Throwable {
		ServerLogger.debug("WrapResponse.wrapAsync - begin...");

		return simpleResult.map((result) -> {    
			ctx.response().setHeader("Access-Control-Allow-Origin", "*");
			ctx.response().setHeader("Access-Control-Allow-Methods", "POST, GET, PUT, DELETE, OPTIONS");
			ctx.response().setHeader("Access-Control-Allow-Headers", "X-Requested-With");
			// this is an hack because scala can't access to the http context,
			// and we need this information for the access log
			String username = (String) ctx.args.get("username");
			if (username != null)
				ctx.response().setHeader("BB-USERNAME", username);

			ServerLogger.debug("WrapResponse.wrapAsync - Wrapping the response");
			final int statusCode = result.getWrappedSimpleResult().header().status();
			ServerLogger.debug("WrapResponse.wrapAsync - Executed API: " + ctx.request() + " , return code " + statusCode);
			ServerLogger.debug("WrapResponse.wrapAsync - Result type:" + result.getWrappedResult().getClass().getName() + " Response Content-Type:" + ctx.response().getHeaders().get("Content-Type"));
			if (ctx.response().getHeaders().get("Content-Type") != null && !ctx.response().getHeaders().get("Content-Type").contains("json")) {
				ServerLogger.debug("WrapResponse.wrapAsync - The response is a file, no wrap will be applied");
				return result;
			}

			String transferEncoding = JavaResultExtractor.getHeaders(result).get(HttpConstants.Headers.TRANSFER_ENCODING);
			if (transferEncoding != null && transferEncoding.equals(HttpConstants.HttpProtocol.CHUNKED)) {
				return result;
			}

			byte[] body = JavaResultExtractor.getBody(result); // here the
																// promise
																// will be
																// resolved
			String stringBody = new String(body, "UTF-8");
			ServerLogger.debug("WrapResponse.wrapAsync - stringBody: " + stringBody);
			if (statusCode > 399) { // an error has occured
				switch (statusCode) {
				case 400:
					result = onBadRequest(ctx, stringBody);
					break;
				case 401:
					result = onUnauthorized(ctx, stringBody);
					break;
				case 403:
					result = onForbidden(ctx, stringBody);
					break;
				case 404:
					result = onResourceNotFound(ctx, stringBody);
					break;
				default:
					HttpMessagesEnum customCode = HttpMessagesEnum.getFromServerCode(statusCode);
					if (customCode != null) {
						result = onCustomCode(customCode, ctx, stringBody);
					} else {
						result = onDefaultError(statusCode, ctx, stringBody);
					}
				}
			} else { // status is not an error
				result = onOk(statusCode, ctx, stringBody);
			} // if (statusCode>399)
			if (statusCode == 204)
				result = Results.noContent();
			try {
				ServerLogger.debug("WrapResponse.wrapAsync - WrapperResponse:\n  + result: \n" + result.toString() + "\n  --> Body:\n" + new String(JavaResultExtractor.getBody(result), "UTF-8"));
			} catch (Throwable e) {
			}

			setServerTime(ctx.response());
			ctx.response().setHeader("Content-Length", Long.toString(JavaResultExtractor.getBody(result).length));
			ctx.response().setContentType("application/json; charset=utf-8");

			ServerLogger.debug("WrapResponse.wrapAsync - end");
			return result;
		}); // map
	}// wrapAsync
}