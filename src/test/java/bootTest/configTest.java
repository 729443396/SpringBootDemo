package bootTest;

import com.lq.springdemo.SpringBootMain;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest(classes={SpringBootMain.class} )
@Slf4j
public class configTest {

    @Value("${log.path}")
    private String logPath;

    @Value("${project.name}")
    private String name;

    @Test
    public void test(){
        System.out.println(logPath);
        System.out.println(name);

    }
}
