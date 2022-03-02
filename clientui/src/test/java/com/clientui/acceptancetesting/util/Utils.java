package com.clientui.acceptancetesting.util;

import org.apache.maven.shared.invoker.*;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Arrays;
import java.util.Properties;
import java.util.concurrent.Callable;
import java.util.concurrent.FutureTask;

public class Utils {

    public static boolean isAllMicroServicesUp(String url, String[] namesToVerify) throws Exception{
        boolean allMicroServicesUP = false;
        String response = sendGetRequest(url);
        allMicroServicesUP = containsWords(response, namesToVerify);
        return allMicroServicesUP;
    }

    private static String sendGetRequest(String url) throws Exception {
        String responseRequest = "";
        URL urlGetRequest = new URL(url);
        String readLine = null;
        HttpURLConnection connection = (HttpURLConnection) urlGetRequest.openConnection();
        connection.setRequestMethod("GET");
        int responseCode = connection.getResponseCode();

        if (responseCode == HttpURLConnection.HTTP_OK) {
            BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            StringBuffer response = new StringBuffer();
            while ((readLine = in.readLine()) != null) {
                response.append(readLine);
            }
            in.close();
            responseRequest = response.toString();
        }
        return responseRequest;
    }

    private static boolean containsWords(String inputString, String[] items) {
        boolean found = true;
        for (String item : items) {
            if (!inputString.contains(item)) {
                found = false;
                break;
            }
        }
        return found;
    }

    public static void stopSpringbootMicroservice(String pomFilePath) throws Exception{
        InvocationRequest request = new DefaultInvocationRequest();
        request.setPomFile( new File( pomFilePath ) );
        request.setGoals(Arrays.asList("spring-boot:stop"));

        Invoker invoker = new DefaultInvoker();
        invoker.setMavenHome(new File(System.getenv().get("MAVEN_HOME")));
        invoker.execute(request);
    }

    public static void startSpringbootMicroservice(String pomFilePath, String eurekaServerUrl) throws Exception{
        InvocationRequest request = new DefaultInvocationRequest();
        request.setPomFile(new File(pomFilePath));
        request.setGoals(Arrays.asList("spring-boot:start"));
        Properties eurekaServerPortProperty = new Properties();
        eurekaServerPortProperty.put("spring-boot.run.arguments","--eureka.client.serviceUrl.defaultZone="+ eurekaServerUrl + "/eureka");
        request.setProperties(eurekaServerPortProperty);

        Invoker invoker = new DefaultInvoker();
        invoker.setMavenHome(new File(System.getenv().get("MAVEN_HOME")));
        Callable<InvocationResult> c = () -> invoker.execute(request);
        FutureTask<InvocationResult> f1 = new FutureTask<InvocationResult>(c);
        Thread t = new Thread(f1);
        t.start();
    }
}
