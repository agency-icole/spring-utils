package eu.icole.springutils;

import eu.icole.springutils.docker.DockerHealthEndpoint;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.actuate.health.HealthEndpoint;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

@RunWith(SpringRunner.class)
@TestPropertySource(properties = {"management.endpoints.web.exposure.include=docker\n"})
@SpringBootTest
public class SpringUtilsApplicationTests {

    @Autowired
    DockerHealthEndpoint dockerHealthEndpoint;

    @Autowired
    private WebApplicationContext context;

    private MockMvc mvc;

    /**
     * Called before each test.
     */
    @Before
    public void setUp() {
        mvc = MockMvcBuilders.webAppContextSetup(this.context).build();
    }

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

    @Test
    public void testDockerEndpoint() throws Exception {

        mvc.perform(MockMvcRequestBuilders.get("/actuator/docker/health"))
                .andExpect(MockMvcResultMatchers.status().isOk());

    }


}
