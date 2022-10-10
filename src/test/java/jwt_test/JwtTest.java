package jwt_test;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.lq.springdemo.SpringBootMain;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Calendar;
import java.util.HashMap;

@RunWith(SpringRunner.class)
@SpringBootTest(classes={SpringBootMain.class} )
@Slf4j
public class JwtTest {

    @Test
    public void test() throws InterruptedException {
        String token = "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJ1c2VybmFtZSI6IuW8oOS4iSJ9.d2p5LxaDokm2czoPoSsSvQBS-rcvXugq3-Mo4Cu2R20";
         token = getJWT();
        checkToken(token);
    }

    private String getJWT(){
        HashMap<String, Object> map = new HashMap<>();
        Calendar time = Calendar.getInstance();
        time.add(Calendar.SECOND,10000);//设置token失效时间
        String token = JWT.create()
                .withHeader(map)//heafer
                .withClaim("username", "张三,11111")
                 //.withExpiresAt(time.getTime())
                .sign(Algorithm.HMAC256("ascfDDDD"));//签名

        System.out.println(token);
        return  token;
    }


    private boolean checkToken(String token){
        DecodedJWT decodedJWT = JWT.decode(token);
        //创建验证对象
        JWTVerifier ascf = JWT.require(Algorithm.HMAC256("ascfDDDD")).build();
        DecodedJWT verify = ascf.verify(token);
        System.out.println(verify.getClaim("username"));//获取token数据
        return false;
    }
}
