package edu.tcu.cs.hogwartsartifactsonline.wizards;

import com.fasterxml.jackson.databind.ObjectMapper;
import edu.tcu.cs.hogwartsartifactsonline.artifacts.Artifact;
import edu.tcu.cs.hogwartsartifactsonline.artifacts.dto.ArtifactDto;
import edu.tcu.cs.hogwartsartifactsonline.system.StatusCode;
import edu.tcu.cs.hogwartsartifactsonline.wizard.Wizard;
import edu.tcu.cs.hogwartsartifactsonline.wizard.WizardService;
import edu.tcu.cs.hogwartsartifactsonline.wizard.dto.WizardDto;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import static org.mockito.BDDMockito.given;

import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

import java.util.ArrayList;
import java.util.List;

@SpringBootTest
@AutoConfigureMockMvc
 class WizardControllerTest {
    @Autowired
    MockMvc mockMvc;

    @MockBean
    WizardService wizardService;

    @Autowired
    ObjectMapper objectMapper;

    List<Wizard> wizards;

    @Value("${api.endpoint.base-url}")
    String baseUrl;

    @BeforeEach
    void setUp() {
        wizards = new ArrayList<>();
        Wizard w1 = new Wizard();
        w1.setId("1");
        w1.setName("Albus Dumbledore");
        this.wizards.add(w1);

        Wizard w2 = new Wizard();
        w2.setId("2");
        w2.setName("Harry Potter");
        this.wizards.add(w2);

        Wizard w3 = new Wizard();
        w3.setId("3");
        w3.setName("Nevill Longbottom");
        this.wizards.add(w3);
    }

    @AfterEach
    void tearDown() {
    }

    @Test
    void testFindAllWizardsSuccess() throws Exception {
        //Given
        given(this.wizardService.findAll()).willReturn(this.wizards);

        //When and then
        this.mockMvc.perform(get("/api/v1/wizards").accept(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.flag").value(true))
                .andExpect(jsonPath("$.code").value(StatusCode.SUCCESS))
                .andExpect(jsonPath("$.message").value("Find All Success"))
                .andExpect(jsonPath("$.data", Matchers.hasSize(this.wizards.size())))
                .andExpect(jsonPath("$.data[0].id").value("1"))
                .andExpect(jsonPath("$.data[0].name").value("Albus Dumbledore"))
                .andExpect(jsonPath("$.data[1].id").value("2"))
                .andExpect(jsonPath("$.data[1].name").value("Harry Potter"));
    }

    @Test
    void testFindWizardByIdSuccess() throws Exception {
        //Given
        given(this.wizardService.findById("1")).willReturn(this.wizards.get(0));
        //When
        this.mockMvc.perform(get(this.baseUrl+ "/wizards/1").accept(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.flag").value(true))
                .andExpect(jsonPath("$.code").value(StatusCode.SUCCESS))
                .andExpect(jsonPath("$.message").value("Find One Success"))
                .andExpect(jsonPath("$.data.id").value("1"))
                .andExpect(jsonPath("$.data.name").value("Albus Dumbledore"));

    }

    @Test
    void testUpdateWizardSuccess() throws Exception {
//Given
        WizardDto wizardDto = new WizardDto(eq("1"),"Albus Dumbledore",2);
        String json = this.objectMapper.writeValueAsString(wizardDto);

        Wizard updatedWizard = new Wizard();
        updatedWizard.setId("1");
        updatedWizard.setName("Updated....");

        given(this.wizardService.update("1",Mockito.any(Wizard.class))).willReturn(updatedWizard);

        //When and then
        this.mockMvc.perform(put(this.baseUrl+"/wizards/1").contentType(MediaType.APPLICATION_JSON).content(json).accept(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.flag").value(true))
                .andExpect(jsonPath("$.code").value(StatusCode.SUCCESS))
                .andExpect(jsonPath("$.message").value("Update Success"))
                .andExpect(jsonPath("$.data.id").value("1"))
                .andExpect(jsonPath("$.data.name").value(updatedWizard.getName()));
    }

    @Test
    void testDeleteWizardSuccessful() throws Exception {
        //Given
        doNothing().when(this.wizardService).delete("1");

        //When and then
        this.mockMvc.perform(delete(this.baseUrl+"/wizards/1").accept(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.flag").value(true))
                .andExpect(jsonPath("$.code").value(StatusCode.SUCCESS))
                .andExpect(jsonPath("$.message").value("Delete Success"))
                .andExpect(jsonPath("$.data").isEmpty());
    }
}

