package com.nvandenbreemen.kb.server;

import org.apache.log4j.Logger;
import spark.Spark;

import static spark.Spark.port;

public class MainServer {

    private static final Logger logger = Logger.getLogger(MainServer.class);

    private static MainServer server;
    public static MainServer get() {
        if(server == null) {
            server = new MainServer();
        }
        return server;
    }

    public void startup() throws Exception {

        port(8888);
        Spark.get("/", (request, response)->{
            return "Hello from Spark";
        });


    }

    public static void main(String[] args) {
        try {
            MainServer.get().startup();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
