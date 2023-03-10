package com.tcf.sma.Retrofit;

import android.content.Context;

import com.localebro.okhttpprofiler.OkHttpProfilerInterceptor;
import com.tcf.sma.Models.AppConstants;
import com.tcf.sma.Models.AppModel;
import com.tcf.sma.R;

import java.io.IOException;
import java.security.cert.CertificateException;
import java.util.Arrays;
import java.util.concurrent.TimeUnit;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Protocol;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by waqas on 2/2/2017.
 */

public class ApiClient {

    private static final String BASE_URL = "https://api.github.com/users/hadley/";
    private static Retrofit retrofit = null;
    private static OkHttpClient okHttpClient = null;
    private static HttpLoggingInterceptor httpLoggingInterceptor = null;

    private static long timeOut = 3;
    private static TimeUnit timeUnit = TimeUnit.MINUTES;

    public static Retrofit getClient() {
        Retrofit retrofitForbaseUrl = null;

        httpLoggingInterceptor = new HttpLoggingInterceptor();
        httpLoggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        try {
            final TrustManager[] trustAllCerts = new TrustManager[]{
                    new X509TrustManager() {
                        @Override
                        public void checkClientTrusted(java.security.cert.X509Certificate[] chain,
                                                       String authType) throws CertificateException {
                            try {
                                if (chain != null){
                                    chain[0].checkValidity();
                                }
                            } catch (Exception e) {
                                throw new CertificateException("Client Certificate not valid or trusted.");
                            }
                        }


                        @Override
                        public void checkServerTrusted(java.security.cert.X509Certificate[] chain,
                                                       String authType) throws CertificateException {
                            try {
                                if (chain != null){
                                    chain[0].checkValidity();
                                }
                            } catch (Exception e) {
                                throw new CertificateException("Certificate not valid or trusted.");
                            }
                        }


                        @Override
                        public java.security.cert.X509Certificate[] getAcceptedIssuers() {
                            return new java.security.cert.X509Certificate[]{};
                        }
                    }
            };

            // Install the all-trusting trust manager
            final SSLContext sslContext = SSLContext.getInstance("SSL");
            sslContext.init(null, trustAllCerts, new java.security.SecureRandom());
            // Create an ssl socket factory with our all-trusting manager
            final SSLSocketFactory sslSocketFactory = sslContext.getSocketFactory();


            okHttpClient = new OkHttpClient.Builder()
                    .sslSocketFactory(sslSocketFactory, (X509TrustManager) trustAllCerts[0])
                    .hostnameVerifier(new HostnameVerifier() {
                        @Override
                        public boolean verify(String s, SSLSession sslSession) {
                            return true;
                        }
                    })
                    .readTimeout(timeOut, timeUnit)
                    .writeTimeout(timeOut, timeUnit)
                    .connectTimeout(timeOut, timeUnit)
                    .addInterceptor(new Interceptor() {
                        @Override
                        public Response intercept(Chain chain) throws IOException {
                            Request original = chain.request();
                            Request.Builder ongoing = chain.request().newBuilder();
                            ongoing.header("Content-Type", "application/json");
                            ongoing.method(original.method(), original.body());

//                                Request request = original.newBuilder()
//                                        .header("Content-Type", "application/json")
//                                        .method(original.method(), original.body())
//                                        .build();
//                                return chain.proceed(request);
                            return chain.proceed(ongoing.build());
                        }
                    })
                    .addInterceptor(httpLoggingInterceptor)
                    .addInterceptor(new OkHttpProfilerInterceptor())
                    .build();

            retrofitForbaseUrl = new Retrofit.Builder()
                    .baseUrl("https://www.xyz123.com") //Dummy url
                    .client(okHttpClient)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();

            retrofit = null;  //null because every time the login hit fresh retrofit will be returned

        } catch (Exception e) {
            e.printStackTrace();
        }
        return retrofitForbaseUrl;
    }

    public static Retrofit getClient(Context context) {

        if (retrofit == null) {

            httpLoggingInterceptor = new HttpLoggingInterceptor();
            httpLoggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
            try {
                final TrustManager[] trustAllCerts = new TrustManager[]{
                        new X509TrustManager() {
                            @Override
                            public void checkClientTrusted(java.security.cert.X509Certificate[] chain,
                                                           String authType) throws CertificateException {
                                try {
                                    if (chain != null){
                                        chain[0].checkValidity();
                                    }
                                } catch (Exception e) {
                                    throw new CertificateException("Client Certificate not valid or trusted.");
                                }
                            }


                            @Override
                            public void checkServerTrusted(java.security.cert.X509Certificate[] chain,
                                                           String authType) throws CertificateException {
                                try {
                                    if (chain != null){
                                        chain[0].checkValidity();
                                    }
                                } catch (Exception e) {
                                    throw new CertificateException("Certificate not valid or trusted.");
                                }
                            }


                            @Override
                            public java.security.cert.X509Certificate[] getAcceptedIssuers() {
                                return new java.security.cert.X509Certificate[]{};
                            }
                        }
                };

                // Install the all-trusting trust manager
                final SSLContext sslContext = SSLContext.getInstance("SSL");
                sslContext.init(null, trustAllCerts, new java.security.SecureRandom());
                // Create an ssl socket factory with our all-trusting manager
                final SSLSocketFactory sslSocketFactory = sslContext.getSocketFactory();


                okHttpClient = new OkHttpClient.Builder()
                        .sslSocketFactory(sslSocketFactory, (X509TrustManager) trustAllCerts[0])
                        .hostnameVerifier(new HostnameVerifier() {
                            @Override
                            public boolean verify(String s, SSLSession sslSession) {
                                return true;
                            }
                        })
                        .readTimeout(timeOut, timeUnit)
                        .writeTimeout(timeOut, timeUnit)
                        .connectTimeout(timeOut, timeUnit)
                        .addInterceptor(new Interceptor() {
                            @Override
                            public Response intercept(Chain chain) throws IOException {
                                Request original = chain.request();
                                Request.Builder ongoing = chain.request().newBuilder();
                                ongoing.header("Content-Type", "application/json");
                                ongoing.method(original.method(), original.body());

//                                Request request = original.newBuilder()
//                                        .header("Content-Type", "application/json")
//                                        .method(original.method(), original.body())
//                                        .build();
//                                return chain.proceed(request);
                                return chain.proceed(ongoing.build());
                            }
                        })
                        .addInterceptor(httpLoggingInterceptor)
                        .addInterceptor(new OkHttpProfilerInterceptor())
                        .build();

                String burl = AppModel.getInstance().readFromSharedPreferences(context, AppConstants.baseurlkey);
                if (burl.isEmpty())
                    burl = context.getString(R.string.BASE_URL);/*AppConstants.BASE_URL;*/

                retrofit = new Retrofit.Builder()
                        .baseUrl(burl)
                        .client(okHttpClient)
                        .addConverterFactory(GsonConverterFactory.create())
                        .build();

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return retrofit;
    }


    public static Retrofit getClient(int timeout) {

//        if (retrofit == null) {

        Retrofit customRetro = null;

        try {
            HttpLoggingInterceptor httpLoggingInterceptor = new HttpLoggingInterceptor();
            httpLoggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
            OkHttpClient client = new OkHttpClient.Builder().readTimeout(timeout, TimeUnit.MINUTES)
                    .writeTimeout(timeout, TimeUnit.MINUTES)
                    .connectTimeout(timeout, TimeUnit.MINUTES)
                    .addInterceptor(httpLoggingInterceptor)
                    .addInterceptor(new OkHttpProfilerInterceptor()).build();


            customRetro = new Retrofit.Builder()
                    .baseUrl("https://reqres.in")
                    .addConverterFactory(GsonConverterFactory.create())
                    .client(client)
                    .build();


//            return retrofit;


        } catch (Exception e) {
            e.printStackTrace();
        }
//        }
        return customRetro;
    }


    public static Retrofit getClient(int timeout, int s, Context context) {

        Retrofit retrofit2 = null;
        HttpLoggingInterceptor httpLoggingInterceptor = new HttpLoggingInterceptor();
        httpLoggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        try {
            final TrustManager[] trustAllCerts = new TrustManager[]{
                    new X509TrustManager() {
                        @Override
                        public void checkClientTrusted(java.security.cert.X509Certificate[] chain,
                                                       String authType) throws CertificateException {
                            try {
                                if (chain != null){
                                    chain[0].checkValidity();
                                }
                            } catch (Exception e) {
                                throw new CertificateException("Client Certificate not valid or trusted.");
                            }
                        }


                        @Override
                        public void checkServerTrusted(java.security.cert.X509Certificate[] chain,
                                                       String authType) throws CertificateException {
                            try {
                                if (chain != null){
                                    chain[0].checkValidity();
                                }
                            } catch (Exception e) {
                                throw new CertificateException("Certificate not valid or trusted.");
                            }
                        }


                        @Override
                        public java.security.cert.X509Certificate[] getAcceptedIssuers() {
                            return new java.security.cert.X509Certificate[]{};
                        }
                    }
            };

            // Install the all-trusting trust manager
            final SSLContext sslContext = SSLContext.getInstance("SSL");
            sslContext.init(null, trustAllCerts, new java.security.SecureRandom());
            // Create an ssl socket factory with our all-trusting manager
            final SSLSocketFactory sslSocketFactory = sslContext.getSocketFactory();


            OkHttpClient okHttpClient = new OkHttpClient.Builder()
                    .sslSocketFactory(sslSocketFactory, (X509TrustManager) trustAllCerts[0])
                    .hostnameVerifier(new HostnameVerifier() {
                        @Override
                        public boolean verify(String s, SSLSession sslSession) {
                            return true;
                        }
                    })
                    .protocols(Arrays.asList(Protocol.HTTP_2, Protocol.HTTP_1_1))
                    .readTimeout(timeout, TimeUnit.MINUTES)
                    .writeTimeout(timeout, TimeUnit.MINUTES)
                    .connectTimeout(timeout, TimeUnit.MINUTES)
                    .addInterceptor(new Interceptor() {
                        @Override
                        public Response intercept(Chain chain) throws IOException {
                            Request original = chain.request();
                            Request.Builder ongoing = chain.request().newBuilder();
                            ongoing.header("Content-Type", "application/json");
                            ongoing.method(original.method(), original.body());

//                                Request request = original.newBuilder()
//                                        .header("Content-Type", "application/json")
//                                        .method(original.method(), original.body())
//                                        .build();
//                                return chain.proceed(request);
                            return chain.proceed(ongoing.build());
                        }
                    })
                    .addInterceptor(httpLoggingInterceptor)
                    .addInterceptor(new OkHttpProfilerInterceptor())
                    .retryOnConnectionFailure(true)
                    .build();

            String burl = AppModel.getInstance().readFromSharedPreferences(context, AppConstants.baseurlkey);
            if (burl.isEmpty())
                burl = context.getString(R.string.BASE_URL);/*AppConstants.BASE_URL*/;

            retrofit2 = new Retrofit.Builder()
                    .baseUrl(burl)
                    .client(okHttpClient)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();

        } catch (Exception e) {
            e.printStackTrace();
        }

        return retrofit2;
    }


}
