package edu.tcu.cs.hogwartsartifactsonline.wizards;

import edu.tcu.cs.hogwartsartifactsonline.artifacts.Artifact;
import edu.tcu.cs.hogwartsartifactsonline.artifacts.ArtifactRepository;
import edu.tcu.cs.hogwartsartifactsonline.artifacts.utils.IdWorker;
import edu.tcu.cs.hogwartsartifactsonline.system.exception.ObjectNotFoundException;
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
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class WizardServiceTest {

    @Mock
    WizardRepository wizardRepository;
    @Mock
    ArtifactRepository artifactRepository;
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
        a.setDescription("An invisibility cloak is used to make the wearer invisible.");
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

    @Test
    void testSaveSuccess(){
        //Given
       Wizard newWizard = new Wizard();
       newWizard.setName("Albus Dumbledore");


//       given(idWorker.nextId()).willReturn(123456L);
       given(wizardRepository.save(newWizard)).willReturn(newWizard);


       //When
        Wizard savedWizard = wizardRepository.save(newWizard);

        //Then
//        assertThat(savedWizard.getId()).isEqualTo("123456");
        assertThat(savedWizard.getName()).isEqualTo(newWizard.getName());
        verify(wizardRepository,times(1)).save(newWizard);
    }

    @Test
    void testUpdateSuccess(){
        //Given
        Wizard oldWizard = new Wizard();
        oldWizard.setId("1");
        oldWizard.setName("Albus Dumbledore");


        Wizard update = new Wizard();
        update.setId("1");
        update.setName("Updated...");


        given(wizardRepository.findById("1")).willReturn(Optional.of(oldWizard));
        given(wizardRepository.save(oldWizard)) .willReturn(oldWizard);

        //When
        Wizard updatedWizard = wizardService.update("1",update);

        //Then
        assertThat(updatedWizard.getId()).isEqualTo(update.getId());
        verify(wizardRepository,times(1)).findById("1");
        verify(wizardRepository,times(1)).save(oldWizard);
    }
@Test
    void testDeleteSuccess(){
        //Given
        Wizard wizard = new Wizard();
        wizard.setId("1");
        wizard.setName("Albus Dumbledore");


        given(wizardRepository.findById("1")).willReturn(Optional.of(wizard));
        doNothing().when(wizardRepository).deleteById("1");

        //When
        wizardService.delete("1");

        //Then
        verify(wizardRepository,times(1)).deleteById("1");
    }

    @Test
    void testAssignArtifactSuccess(){
        //Given
        Artifact a = new Artifact();
        a.setId("1250808601744904192");
        a.setName("Invisibility Cloak");
        a.setDescription("An invisibility cloak is used to make the wearer invisible.");
        a.setImageUrl("ImageUrl");

        Wizard w = new Wizard();
        w.setId("2");
        w.setName("Harry Potter");
        w.addArtifact(a);

        Wizard w3 = new Wizard();
        w3.setId("3");
        w3.setName("Neville Longbottom");

        given(this.artifactRepository.findById("1250808601744904192")).willReturn(Optional.of(a));
        given(this.wizardRepository.findById("3")).willReturn(Optional.of(w3));

        //When
        this.wizardService.assginArtifact("3", "1250808601744904192");

        //Then
        assertThat(a.getOwner().getId()).isEqualTo("3");
        assertThat(w3.getArtifacts().contains(a));
    }

    @Test
    void testAssignArtifactErrorWithNonExistentWizardId(){
        //Given
        Artifact a = new Artifact();
        a.setId("1250808601744904192");
        a.setName("Invisibility Cloak");
        a.setDescription("An invisibility cloak is used to make the wearer invisible.");
        a.setImageUrl("ImageUrl");

        Wizard w = new Wizard();
        w.setId("2");
        w.setName("Harry Potter");
        w.addArtifact(a);


        given(this.artifactRepository.findById("1250808601744904192")).willReturn(Optional.of(a));
        given(this.wizardRepository.findById("3")).willReturn(Optional.empty());

        //When
        Throwable thrown = assertThrows(ObjectNotFoundException.class, () -> {
            this.wizardService.assginArtifact("3", "1250808601744904192");
        });
        //Then
        assertThat(thrown)
                .isInstanceOf(ObjectNotFoundException.class)
                        .hasMessage("Could not find wizard with Id 3 :(");
        assertThat(a.getOwner().getId()).isEqualTo("2");
    }
    @Test
    void testAssignArtifactErrorWithNonExistentArtifactId(){
        //Given
        given(this.artifactRepository.findById("1250808601744904192")).willReturn(Optional.empty());
        //When
        Throwable thrown = assertThrows(ObjectNotFoundException.class, () -> {
            this.wizardService.assginArtifact("3", "1250808601744904192");
        });
        //Then
        assertThat(thrown)
                .isInstanceOf(ObjectNotFoundException.class)
                .hasMessage("Could not find artifact with Id 1250808601744904192 :(");
    }
}
