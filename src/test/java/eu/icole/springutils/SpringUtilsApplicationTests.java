package eu.icole.springutils;

import eu.icole.springutils.docker.DockerHealthEndpoint;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@TestPropertySource(properties = {"management.endpoints.web.exposure.include=docker\n"})
@SpringBootTest
public class SpringUtilsApplicationTests {

    @Autowired
    DockerHealthEndpoint dockerHealthEndpoint;


    @Test
    public void contextLoads() {
        Assert.assertNotNull(dockerHealthEndpoint);
    }

    @Test
    public void testDockerHealthStatusList() {

        ResponseEntity response = dockerHealthEndpoint.dockerHealthEndpoint();
        Assert.assertNotNull(response);

        Assert.assertEquals(HttpStatus.OK, response.getStatusCode());
    }


}
