package com.boat.bp.middleware.service;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Map;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;

import org.springframework.stereotype.Service;

import com.boat.bp.middleware.data.Account;
import com.boat.bp.middleware.data.CodeValue;
import com.boat.bp.middleware.data.Field;
import com.boat.bp.middleware.data.HookConfig;
import com.boat.bp.middleware.data.LoanDetails;
import com.boat.bp.middleware.data.LoanProduct;
import com.boat.bp.middleware.data.SavingsProduct;
import com.boat.bp.middleware.helper.ResponseMessage;
import com.boat.bp.middleware.helper.HTTPresponse;
import com.boat.bp.middleware.helper.Settings;
import com.boat.bp.middleware.helper.Type;
import com.boat.bp.middleware.responses.GeneralResponse;
import com.boat.bp.middleware.responses.ListResponse;
import com.boat.bp.middleware.responses.PagedResponse;
import com.boat.bp.middleware.responses.TokenResponse;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;


@Service
public class Endpoint<T, V> {

	private static ObjectMapper mapper = new ObjectMapper();

	Endpoint() {
		mapper.setSerializationInclusion(Include.NON_EMPTY);
		mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
	}

	@SuppressWarnings("unchecked")
	public V parseResponse(String result) {
		GeneralResponse<V> resp = new GeneralResponse<V>();
        System.out.println(result);
		try {

			resp = mapper.readValue(result, GeneralResponse.class);

			if (resp.getErrors() != null || resp.getError() != null)
				resp.setStatus(HTTPresponse.HTTPINVALID.getCode());
			else {
				resp.setStatus(HTTPresponse.HTTPSUCCESS.getCode());
				resp.setMessage("Action processed succesfully");
			}
			//System.out.println(Arrays.asList(resp).toString());	
			return (V) resp;

		} catch (JsonMappingException e) {
			resp.setStatus(HTTPresponse.HTTPERROR.getCode());
			//e.printStackTrace();
		} catch (JsonProcessingException e) {
			resp.setStatus(HTTPresponse.HTTPERROR.getCode());
			//e.printStackTrace();
		}

		return null;
	}


	@SuppressWarnings("unchecked")
	public V parsePageResponse(String result) {
		PagedResponse resp = new PagedResponse();

		try {

			resp = mapper.readValue(result, PagedResponse.class);

			System.out.println(Arrays.asList(resp).toString());	
			return (V) resp;

		} catch (JsonMappingException e) {
			
			e.printStackTrace();
		} catch (JsonProcessingException e) {
			
			e.printStackTrace();
		}

		return null;
	}

	public Account parseBalance(String result) {

		Account resp = new Account();

		try {

			resp = mapper.readValue(result, Account.class);
			return resp;

		} catch (JsonMappingException e) {

			e.printStackTrace();
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		}

		return null;
	}

	public LoanProduct parseLoanProduct(String result) {

		LoanProduct resp = new LoanProduct();

		System.out.println(result);

		try {

			resp = mapper.readValue(result, LoanProduct.class);
			return resp;

		} catch (JsonMappingException e) {

			e.printStackTrace();
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		}

		return null;
	}

	public LoanDetails parseLoanDetails(String result) {

		LoanDetails resp = new LoanDetails();

		try {

			resp = mapper.readValue(result, LoanDetails.class);
			return resp;

		} catch (JsonMappingException e) {

			e.printStackTrace();
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		}

		return null;
	}


	

	public String parseHook(String result) {
		String hook = null;
		try {

			HookConfig resp = mapper.readValue(result, HookConfig.class);

			if (resp.getConfig().size() > 0) {
				for (Field f : resp.getConfig()) {
					if (f.getFieldName().equals("Payload URL")) {
						hook = f.getFieldValue();
						break;
					}
				}

				return hook;
			} else
				return null;

		} catch (JsonMappingException e) {

			e.printStackTrace();
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		}

		return null;
	}

	@SuppressWarnings("unchecked")
	public V parseList(String result, String type) {

		try {

			ListResponse<V> resp = new ListResponse<V>();
			if (type.equals(Settings.SAVINGS))
				resp.setData((V[]) mapper.readValue(result, SavingsProduct[].class));
			else if (type.equals(Settings.COUNTRY) || type.equals(Settings.STATE_PROVINCE))
				resp.setData((V[]) mapper.readValue(result, CodeValue[].class));
			
		

			resp.setStatus("200");
			resp.setMessage(resp.getData().length + "(s) Found");

			return (V) resp;

		} catch (JsonMappingException e) {
			e.printStackTrace();
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		}

		return null;
	}


	

	public TokenResponse parseTokenResponse(String result) {
		TokenResponse resp = new TokenResponse();

		try {

			resp = mapper.readValue(result, TokenResponse.class);

			if (resp.getError() != null)
				resp.setStatus(HTTPresponse.HTTPINVALID.getCode());
			else
				resp.setStatus(HTTPresponse.HTTPSUCCESS.getCode());

			return resp;

		} catch (JsonMappingException e) {
			resp.setStatus(HTTPresponse.HTTPERROR.getCode());
			e.printStackTrace();
		} catch (JsonProcessingException e) {
			resp.setStatus(HTTPresponse.HTTPERROR.getCode());
			e.printStackTrace();
		}

		return null;
	}

	public String sendHTTPRequest(final T request, final Type type, final String urlString,
			final Map<String, String> headers, boolean async, int connectTimeout, int ReadtimeOut, String token) {
		String output = "";

		try {

			URL url = new URL(urlString);

			// test code to be removed for production
			HttpsURLConnection con = (HttpsURLConnection) url.openConnection();
			SSLContext sc = SSLContext.getInstance("SSL");
			sc.init(null, new TrustManager[] { new TrustAnyTrustManager() }, new java.security.SecureRandom());
			// Create all-trusting host name verifier
			HostnameVerifier allHostsValid = new HostnameVerifier() {
				public boolean verify(String hostname, SSLSession session) {
					return true;
				}
			};
			con.setHostnameVerifier(allHostsValid);
			con.setSSLSocketFactory(sc.getSocketFactory());
			// end test code
			 //System.out.println(urlString);
			con.setRequestMethod(type.getName());
			for (Map.Entry<String, String> entry : headers.entrySet())
				con.setRequestProperty(entry.getKey(), entry.getValue());
			if (token != null)
				con.setRequestProperty("Authorization", token);
			con.setConnectTimeout(connectTimeout);
			con.setReadTimeout(ReadtimeOut);

			if (null != request && !"".equals(request)) {
				con.setDoOutput(true);
				DataOutputStream out = new DataOutputStream(con.getOutputStream());
				if (request instanceof String)
					out.write(((String) request).getBytes(StandardCharsets.UTF_8));
				else
					out.writeBytes(mapper.writeValueAsString(request));
				out.flush();
				out.close();
			} 

			if (con.getResponseCode() == 200) {
				if (!async) {
					BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
					String inputLine;
					StringBuffer content = new StringBuffer();
					while ((inputLine = in.readLine()) != null) {
						content.append(inputLine);
					}
					output = content.toString();

				}
			} else if (con.getResponseCode() == 500) {
				output = ResponseMessage.MIFOS_FIVE_HUNDRED.getMessage();
			} else if (con.getErrorStream() != null) {
				BufferedReader in = new BufferedReader(new InputStreamReader(con.getErrorStream()));
				String inputLine;
				StringBuffer content = new StringBuffer();
				while ((inputLine = in.readLine()) != null) {
					content.append(inputLine);
				}
				output = content.toString();
			}

			con.disconnect();

		} catch (Exception e) {
			e.printStackTrace();
		}

		return output;

	}

}
