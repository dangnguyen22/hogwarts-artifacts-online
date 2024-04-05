package edu.tcu.cs.hogwartsartifactsonline.wizards;

import edu.tcu.cs.hogwartsartifactsonline.artifacts.Artifact;
import edu.tcu.cs.hogwartsartifactsonline.artifacts.ArtifactRepository;
import edu.tcu.cs.hogwartsartifactsonline.artifacts.utils.IdWorker;
import edu.tcu.cs.hogwartsartifactsonline.wizard.Wizard;
import edu.tcu.cs.hogwartsartifactsonline.wizard.WizardRepository;
import edu.tcu.cs.hogwartsartifactsonline.wizard.WizardService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;


import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.catchThrowable;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class WizardServiceTest {

    @Mock
    WizardRepository wizardRepository;
    @Mock
    IdWorker idWorker;
    @InjectMocks
    WizardService wizardService;
    List<Wizard> wizards;

    @BeforeEach
    void setUp() {
        Wizard w1 = new Wizard();
        w1.setId("1");
        w1.setName("Albus Dumbledore");

        Wizard w2 = new Wizard();
        w2.setId("2");
        w2.setName("Harry Potter");

        this.wizards = new ArrayList<>();
        this.wizards.add(w1);
        this.wizards.add(w2);
    }

    @AfterEach
    void tearDown() {}
    @Test
    void testFindAllSuccess(){
        //Given
        given(wizardRepository.findAll()).willReturn(this.wizards);
        //When
        List<Wizard> actualWizard = wizardService.findAll();
        //Then
        assertThat(actualWizard.size()).isEqualTo(this.wizards.size());
        verify(wizardRepository,times(1)).findAll();
    }
    @Test
    void testFindByIdSuccess(){
        //Given
        Artifact a = new Artifact();
        a.setId("1250808601744904192");
        a.setName("Invisibility Cloak");
        a.setDescrition("An invisibility cloak is used to make the wearer invisible.");
        a.setImageUrl("ImageUrl");

        Wizard w = new Wizard();
        w.setId("2");
        w.setName("Harry Potter");

        a.setOwner(w);

        given(wizardRepository.findById("2")).willReturn(Optional.of(w));

        //When
        Wizard returnWizard = wizardService.findById("2");

        //Then
        assertThat(returnWizard.getId()).isEqualTo(w.getId());
        assertThat(returnWizard.getName()).isEqualTo(w.getName());

        verify(wizardRepository,times(1)).findById("2");

    }
}
