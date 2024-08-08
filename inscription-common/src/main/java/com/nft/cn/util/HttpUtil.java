package com.nft.cn.util;

import com.alibaba.fastjson.JSONObject;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.config.Registry;
import org.apache.http.config.RegistryBuilder;
import org.apache.http.conn.socket.PlainConnectionSocketFactory;
import org.apache.http.conn.ssl.NoopHostnameVerifier;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.conn.ssl.TrustSelfSignedStrategy;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.message.BasicHeader;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;
import org.apache.http.ssl.SSLContextBuilder;
import org.apache.http.util.EntityUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.input.SAXBuilder;
import org.xml.sax.InputSource;

import java.io.IOException;
import java.io.StringReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;


public class HttpUtil {

	private static final Logger logger = LogManager.getLogger(HttpUtil.class);
	private static final String CHARSET = "UTF-8";

	public static String get(String url) throws ClientProtocolException, IOException {
		CloseableHttpClient httpClient = HttpClients.createDefault();
		HttpGet httpGet = new HttpGet(url);
//		 connection.setConnectTimeout(30 * 1000);
		RequestConfig requestConfig = RequestConfig.custom().setSocketTimeout(30 * 1000).setConnectTimeout(30 * 1000).build();
		httpGet.setConfig(requestConfig);
		httpGet.setHeader("user-agent","Chrome/79");
		CloseableHttpResponse response = httpClient.execute(httpGet);
		try {
			HttpEntity entity = response.getEntity();
			if (entity != null) {
				String str = EntityUtils.toString(entity, CHARSET);
				return str;
			}
		} finally {
			response.close();
			httpClient.close();
		}
		return null;
	}
	
	public static String getLaex(String url) throws ClientProtocolException, IOException {
		CloseableHttpClient httpClient = HttpClients.createDefault();
		HttpGet httpGet = new HttpGet(url);
		RequestConfig requestConfig = RequestConfig.custom().setSocketTimeout(30 * 1000).setConnectTimeout(30 * 1000).build();
		httpGet.setConfig(requestConfig);
		
		httpGet.setHeader("User-Agent","User-Agent: Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/39.0.2171.71 Safari/537.36");
		CloseableHttpResponse response = httpClient.execute(httpGet);
		try {
			HttpEntity entity = response.getEntity();
			if (entity != null) {
				String str = EntityUtils.toString(entity, CHARSET);
				return str;
			}
		} finally {
			response.close();
			httpClient.close();
		}
		return null;
	}
	
	public static String getBcex(String url) throws ClientProtocolException, IOException {
		CloseableHttpClient httpClient = HttpClients.createDefault();
		HttpGet httpGet = new HttpGet(url);
		RequestConfig requestConfig = RequestConfig.custom().setSocketTimeout(30 * 1000).setConnectTimeout(30 * 1000).build();
		httpGet.setConfig(requestConfig);
		httpGet.setHeader("User-Agent","User-Agent: Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/39.0.2171.71 Safari/537.36");
		CloseableHttpResponse response = httpClient.execute(httpGet);
		try {
			HttpEntity entity = response.getEntity();
			if (entity != null) {
				String str = EntityUtils.toString(entity, CHARSET);
				return str;
			}
		} finally {
			response.close();
			httpClient.close();
		}
		return null;
	}
	public static String postZKXT(String url, Map<String, Object> params) {
		try {
			CloseableHttpClient httpClient = HttpClients.createDefault();
			HttpPost httpPost = new HttpPost(url);
			List<NameValuePair> parameters = new ArrayList<NameValuePair>();
			for (Iterator<String> iterator = params.keySet().iterator(); iterator.hasNext();) {
				String key = iterator.next();
				parameters.add(new BasicNameValuePair(key, params.get(key).toString()));
			}
			UrlEncodedFormEntity uefEntity = new UrlEncodedFormEntity(parameters, CHARSET);
			httpPost.setEntity(uefEntity);
			CloseableHttpResponse response = httpClient.execute(httpPost);
			try {
				HttpEntity entity = response.getEntity();
				if (entity != null) {
					String str = EntityUtils.toString(entity, CHARSET);
					return xmlElements(str);
				}
			} finally {
				response.close();
				httpClient.close();
			}
		} catch (Exception e) {
			logger.error(e);
			throw new RuntimeException(e);
		}
		return null;
	}
	public String Get(String url) {
		CloseableHttpClient client= HttpClientUtil.getHttpClient();
    	HttpGet request = new HttpGet(url);
    	CloseableHttpResponse response=null;
    	RequestConfig requestConfig = RequestConfig.custom()
    	      //.setSocketTimeout(30000)
    	      .setConnectTimeout(20000)
    	      .setExpectContinueEnabled(true)
    	      .setConnectionRequestTimeout(30000)
    	      .setStaleConnectionCheckEnabled(true)
    	      .build();
    	request.setConfig(requestConfig);
    	try {
    	   response = client.execute(request);
    	   if(response.getStatusLine().getStatusCode() != HttpStatus.SC_OK) {
    	      return  null;
    	   }
    	   HttpEntity resEntity =  response.getEntity();
    	   if(resEntity==null){
    	         return  null;
    	   }
    	   String result= EntityUtils.toString(resEntity, "UTF-8");
    	   return result;
    	} catch (UnsupportedEncodingException e) {
    		logger.info(e.getMessage());
    	   return null;
    	} catch (ClientProtocolException e) {
    		logger.info(e.getMessage());
    	   return null;
    	} catch (IOException e) {
    		logger.info(e.getMessage());
    	}finally {
    	   if(response != null)
    	   {
    	      try {
    	         EntityUtils.consume(response.getEntity());
    	         response.close();
    	      } catch (IOException e) {
    	      }
    	}
    	}
    	return null;
	}
	

	public static String get(String url, Map<String, Object> params,Map<String, Object> heards) {
		try {
			CloseableHttpClient httpClient = HttpClients.createDefault();
			url = url + "?";
			for (Iterator<String> iterator = params.keySet().iterator(); iterator.hasNext();) {
				String key = iterator.next();
				String temp = key + "=" + params.get(key) + "&";
				url = url + temp;
			}
			url = url.substring(0, url.length() - 1);
			HttpGet httpGet = new HttpGet(url);
			RequestConfig requestConfig = RequestConfig.custom().setSocketTimeout(30 * 1000).setConnectTimeout(30 * 1000).build();
			httpGet.setConfig(requestConfig);
			httpGet.setHeader("Authorization",heards.get("Authorization").toString());
			CloseableHttpResponse response = httpClient.execute(httpGet);
			try {
				HttpEntity entity = response.getEntity();
				if (entity != null) {
					String str = EntityUtils.toString(entity, CHARSET);
					return str;
				}
			} finally {
				response.close();
				httpClient.close();
			}
		} catch (Exception e) {
			logger.error(e);
			throw new RuntimeException(e);
		}
		return null;
	}
	public static String get(String url, Map<String, Object> params) {
		try {
			CloseableHttpClient httpClient = HttpClients.createDefault();
			url = url + "?";
			for (Iterator<String> iterator = params.keySet().iterator(); iterator.hasNext();) {
				String key = iterator.next();
				String temp = key + "=" + params.get(key) + "&";
				url = url + temp;
			}
			url = url.substring(0, url.length() - 1);
			HttpGet httpGet = new HttpGet(url);
			CloseableHttpResponse response = httpClient.execute(httpGet);
			try {
				HttpEntity entity = response.getEntity();
				if (entity != null) {
					String str = EntityUtils.toString(entity, CHARSET);
					return str;
				}
			} finally {
				response.close();
				httpClient.close();
			}
		} catch (Exception e) {
			logger.error(e);
			throw new RuntimeException(e);
		}
		return null;
	}


	public static String send(String url, JSONObject jsonObject, String encoding) throws Exception {
		String body = "";
		//CloseableHttpClient client = HttpClients.createDefault();

		org.apache.http.client.HttpClient httpClient = new SSLClient();


		HttpPost httpPost = new HttpPost(url);

		StringEntity s = new StringEntity(jsonObject.toString(), "utf-8");
		s.setContentEncoding(new BasicHeader(HTTP.CONTENT_TYPE,
				"application/json"));
		httpPost.setEntity(s);
		httpPost.setHeader("Content-type", "application/json");
		httpPost.setHeader("User-Agent", "Mozilla/4.0 (compatible; MSIE 5.0; Windows NT; DigExt)");
		HttpResponse response = httpClient.execute(httpPost);
		HttpEntity entity = response.getEntity();
		if (entity != null) {
			body = EntityUtils.toString(entity, encoding);
		}
		EntityUtils.consume(entity);
		return body;
	}

	public static String sendNoSsl(String url, JSONObject jsonObject, String encoding) throws Exception {
		String resData = "";

			try {

			SSLContextBuilder builder = new SSLContextBuilder();

			builder.loadTrustMaterial(null, new TrustSelfSignedStrategy());

			SSLConnectionSocketFactory sslConnectionSocketFactory = new SSLConnectionSocketFactory(builder.build(), NoopHostnameVerifier.INSTANCE);

			Registry registry = RegistryBuilder.create()

					.register("http", new PlainConnectionSocketFactory())

					.register("https", sslConnectionSocketFactory)

					.build();

			PoolingHttpClientConnectionManager cm = new PoolingHttpClientConnectionManager(registry);

			cm.setMaxTotal(100);

			CloseableHttpClient httpClient = HttpClients.custom()

					.setSSLSocketFactory(sslConnectionSocketFactory)

					.setConnectionManager(cm)

					.build();


			HttpPost method = new HttpPost(url);

			StringEntity entity = new StringEntity(jsonObject.toString(),"utf-8");

			entity.setContentEncoding("UTF-8");

			entity.setContentType("application/json");

			method.setEntity(entity);
			HttpResponse result = httpClient.execute(method);

			resData = EntityUtils.toString(result.getEntity());

		} catch (UnsupportedEncodingException e) {

			e.printStackTrace();

		} catch (org.apache.http.ParseException e) {

			e.printStackTrace();

		} catch (IOException e) {

			e.printStackTrace();

		}catch(Exception e){

			e.printStackTrace();

		}
         return resData;
	}

	public static String post(String url, Map<String, Object> params) {
		try {
			CloseableHttpClient httpClient = HttpClients.createDefault();
			HttpPost httpPost = new HttpPost(url);
			List<NameValuePair> parameters = new ArrayList<NameValuePair>();
			for (Iterator<String> iterator = params.keySet().iterator(); iterator.hasNext();) {
				String key = iterator.next();
				parameters.add(new BasicNameValuePair(key, params.get(key).toString()));
			}
			UrlEncodedFormEntity uefEntity = new UrlEncodedFormEntity(parameters, CHARSET);
			httpPost.setEntity(uefEntity);
			CloseableHttpResponse response = httpClient.execute(httpPost);
			try {
				HttpEntity entity = response.getEntity();
				if (entity != null) {
					String str = EntityUtils.toString(entity, CHARSET);
					return str;
				}
			} finally {
				response.close();
				httpClient.close();
			}
		} catch (Exception e) {
			logger.error(e);
			throw new RuntimeException(e);
		}
		return null;
	}
	public static String post(String url, String params) {
		try {
			CloseableHttpClient httpClient = HttpClients.createDefault();
			HttpPost httpPost = new HttpPost(url);
			StringEntity sEntity = new StringEntity(params, CHARSET);
			httpPost.setEntity(sEntity);
			CloseableHttpResponse response = httpClient.execute(httpPost);
			try {
				HttpEntity entity = response.getEntity();
				if (entity != null) {
					return EntityUtils.toString(entity, CHARSET);
				}
			} finally {
				response.close();
				httpClient.close();
			}
		} catch (Exception e) {
			logger.error(e);
			throw new RuntimeException(e);
		}
		return null;
	}
	 public static String xmlElements(String xmlDoc) {
	        StringReader read = new StringReader(xmlDoc);
	        InputSource source = new InputSource(read);
	        SAXBuilder sb = new SAXBuilder();
	        try {
	            Document doc = sb.build(source);
	            Element root = doc.getRootElement();
	            return root.getText();
	        } catch (JDOMException e) {
	            e.printStackTrace();
	        } catch (IOException e) {
	            e.printStackTrace();
	        }
	        return null;
	    }
}
