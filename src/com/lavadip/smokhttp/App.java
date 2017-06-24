package com.lavadip.smokhttp;

import java.io.IOException;
import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.Policy;
import java.util.Arrays;

import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.TrustManagerFactory;
import javax.net.ssl.X509TrustManager;

import okhttp3.OkHttpClient;
import okhttp3.Request;

public class App {

  public static void main(final String[] args) throws IOException, NoSuchAlgorithmException, KeyStoreException, KeyManagementException {
    Policy.setPolicy(LocalSecurityPolicy.getInstance());
    System.setSecurityManager(new LocalSecurityManager());

    System.out.println("Security manager installed.");

    final TrustManagerFactory trustManagerFactory = TrustManagerFactory.getInstance(
        TrustManagerFactory.getDefaultAlgorithm());
    trustManagerFactory.init((KeyStore) null);
    final TrustManager[] trustManagers = trustManagerFactory.getTrustManagers();
    if (trustManagers.length != 1 || !(trustManagers[0] instanceof X509TrustManager)) {
      throw new IllegalStateException("Unexpected default trust managers:" + Arrays.toString(trustManagers));
    }
    final X509TrustManager trustManager = (X509TrustManager) trustManagers[0];

    final SSLContext sslContext = SSLContext.getInstance("TLS");
    sslContext.init(null, new TrustManager[] { trustManager }, null);
    final SSLSocketFactory sslSocketFactory = sslContext.getSocketFactory();

    System.out.println("Creating okhttp client");
    final OkHttpClient client = new OkHttpClient.Builder()
        .sslSocketFactory(sslSocketFactory, trustManager)
        .build();

    System.out.println("Creating okhttp request");
    final Request request = new Request.Builder().url("https://square.com").build();
    client.newCall(request);

    System.out.println("All fine");
  }

}
