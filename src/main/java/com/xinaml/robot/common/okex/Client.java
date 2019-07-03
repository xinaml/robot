package com.xinaml.robot.common.okex;

import com.xinaml.robot.common.custom.exception.ActException;
import com.xinaml.robot.common.okex.constant.APIConstants;
import com.xinaml.robot.common.okex.enums.ContentTypeEnum;
import com.xinaml.robot.common.okex.enums.HttpHeadersEnum;
import com.xinaml.robot.common.okex.enums.I18nEnum;
import com.xinaml.robot.common.okex.utils.DateUtils;
import com.xinaml.robot.common.okex.utils.HmacSHA256Base64Utils;
import com.xinaml.robot.entity.user.User;
import okhttp3.*;
import okio.Buffer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.security.InvalidKeyException;

/**
 * @Author: [lgq]
 * @Date: [19-6-13 上午11:26]
 * @Description:
 * @Version: [1.0.0]
 * @Copy: [com.xinaml]
 */
public class Client {
    static Logger LOG = LoggerFactory.getLogger(Client.class);
    //        config.setEndpoint("https://www.okex.com");
//        config.setApiKey("c7de75ae-a28c-433d-84da-70ab55506428");
//        config.setSecretKey("6B6640690B9A6A0D85D6D6BE872BA557");
//        config.setPassphrase("yyguai199411");
    private static String baseUrl = "https://www.okex.me";
    public static final MediaType MT = MediaType.parse("application/json;charset=utf-8");


    public static String httpGet(String url, User user) {
        OkHttpClient httpClient = new OkHttpClient();
        Request request = new Request.Builder()
                .url(baseUrl + url.trim())
                .build();
        String timestamp = DateUtils.getUnixTime();
        request = initHeaders(request, timestamp, user);
        try {
            Response response = httpClient.newCall(request).execute();
            return response.body().string(); // 返回的是string 类型，json的mapper可以直接处理
        } catch (IOException e) {
            LOG.error("网络连接异常！");
            return null;
        }
    }

    public static String httpPost(String url, String json, User user) {
        OkHttpClient httpClient = new OkHttpClient();
        RequestBody requestBody = RequestBody.create(MT, json);
        Request request = new Request.Builder()
                .url(baseUrl + url.trim())
                .post(requestBody)
                .build();
        String timestamp = DateUtils.getUnixTime();
        request = initHeaders(request, timestamp, user);
        try {
            Response response = httpClient.newCall(request).execute();
            return response.body().string(); // 返回的是string 类型，json的mapper可以直接处理
        } catch (IOException e) {
            LOG.error("网络连接异常！");
            return null;
        }
    }


    private static Request initHeaders(final Request request, final String timestamp, User user) {
        if (null == user) {
            user = new User();
            user.setApiKey("c7de75ae-a28c-433d-84da-70ab55506428");
            user.setSecretKey("6B6640690B9A6A0D85D6D6BE872BA557");
            user.setPassPhrase("yyguai199411");
        }
        return request.newBuilder().addHeader(APIConstants.ACCEPT, ContentTypeEnum.APPLICATION_JSON.contentType())
                .addHeader(APIConstants.ACCEPT, ContentTypeEnum.APPLICATION_JSON.contentType())
                .addHeader(APIConstants.CONTENT_TYPE, ContentTypeEnum.APPLICATION_JSON_UTF8.contentType())
                .addHeader(APIConstants.COOKIE, getCookie())
                .addHeader(HttpHeadersEnum.OK_ACCESS_KEY.header(), user.getApiKey())
                .addHeader(HttpHeadersEnum.OK_ACCESS_SIGN.header(), sign(request, timestamp, user.getSecretKey()))
                .addHeader(HttpHeadersEnum.OK_ACCESS_TIMESTAMP.header(), timestamp)
                .addHeader(HttpHeadersEnum.OK_ACCESS_PASSPHRASE.header(), user.getPassPhrase())
                .addHeader("Accept-Encoding", "identity").build();

    }

    private static String getCookie() {
        final StringBuilder cookie = new StringBuilder();
        cookie.append(APIConstants.LOCALE).append(I18nEnum.ENGLISH.i18n());
        return cookie.toString();
    }

    private static String sign(final Request request, final String timestamp, String secretKey) {
        final String sign;
        try {
            sign = HmacSHA256Base64Utils.sign(timestamp, method(request), requestPath(request),
                    queryString(request), body(request), secretKey);
        } catch (final IOException e) {
            throw new ActException("Request get body io exception.");
        } catch (final CloneNotSupportedException e) {
            throw new ActException("Hmac SHA256 Base64 Signature clone not supported exception.");
        } catch (final InvalidKeyException e) {
            throw new ActException("Hmac SHA256 Base64 Signature invalid key exception.");
        }
        return sign;
    }


    private static String url(final Request request) {
        return request.url().toString();
    }

    private static String method(final Request request) {
        return request.method().toUpperCase();
    }

    private static String requestPath(final Request request) {
        String url = url(request);
        url = url.replace(baseUrl, APIConstants.EMPTY);
        String requestPath = url;
        if (requestPath.contains(APIConstants.QUESTION)) {
            requestPath = requestPath.substring(0, url.lastIndexOf(APIConstants.QUESTION));
        }
        if (baseUrl.endsWith(APIConstants.SLASH)) {
            requestPath = APIConstants.SLASH + requestPath;
        }
        return requestPath;
    }

    private static String queryString(final Request request) {
        final String url = url(request);
        String queryString = APIConstants.EMPTY;
        if (url.contains(APIConstants.QUESTION)) {
            queryString = url.substring(url.lastIndexOf(APIConstants.QUESTION) + 1);
        }
        return queryString;
    }

    private static String body(final Request request) throws IOException {
        final RequestBody requestBody = request.body();
        String body = APIConstants.EMPTY;
        if (requestBody != null) {
            final Buffer buffer = new Buffer();
            requestBody.writeTo(buffer);
            body = buffer.readString(APIConstants.UTF_8);
        }
        return body;
    }


}
