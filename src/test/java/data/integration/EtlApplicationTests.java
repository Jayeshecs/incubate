package data.integration;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = {EtlApplication.class, EtlFatJarRouter.class})
public class EtlApplicationTests {

	@Test
	public void contextLoads() {
	}

}
