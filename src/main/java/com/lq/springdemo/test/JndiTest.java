//package com.lq.springdemo.test;
//
//import javax.naming.Context;
//import javax.naming.InitialContext;
//import java.util.Properties;
//
//public class JndiTest {
//    public static void main(String[] args) {
//        Properties env = new Properties();
//
//         env.put(Context.INITIAL_CONTEXT_FACTORY, "com.ibm.websphere.naming.WsnInitialContextFactory");
//        env.put(Context.PROVIDER_URL, "iiop://11.24.115.30:2809");
//        DataSource ds = (DataSource) ctx.lookup("jdbc/TntDB");
//        InitialContext initialContext = new InitialContext(env);
//        HelloInterface helloInterface = (HelloInterface) initialContext.lookup(uri);
//        log.info(helloInterface.says("hello"));
//    }
//}
