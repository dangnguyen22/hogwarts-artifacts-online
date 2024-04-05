package edu.tcu.cs.hogwartsartifactsonline.artifacts;

import edu.tcu.cs.hogwartsartifactsonline.artifacts.utils.IdWorker;
import edu.tcu.cs.hogwartsartifactsonline.wizard.Wizard;
import jakarta.inject.Inject;
import org.hibernate.resource.transaction.backend.jta.internal.synchronization.ExceptionMapper;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import javax.swing.text.html.Option;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.catchThrowable;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ArtifactServiceTest {

    @Mock
    ArtifactRepository artifactRepository;
    @Mock
    IdWorker idWorker;
    @InjectMocks
    ArtifactService artifactService;

    List<Artifact> artifacts;

    @BeforeEach
    void setUp() {
        Artifact a1 = new Artifact();
        a1.setId("1250808601744904191");
        a1.setName("Deluminator");
        a1.setDescrition("A Deluminator is a device invented by Albus Dumbledore that resembles a cigarette lighter. It is used to remove or absorb (as well as return) the light from any light source to provide cover to the user.");
        a1.setImageUrl("ImageUrl");


        Artifact a2 = new Artifact();
        a2.setId("1250808601744904192");
        a2.setName("Invisibility Cloak");
        a2.setDescrition("An invisibility cloak is used to make the wearer invisible");
        a2.setImageUrl("ImageUrl");


        this.artifacts = new ArrayList<>();
        this.artifacts.add(a1);
        this.artifacts.add(a2);
    }

    @AfterEach
    void tearDown() {
    }

    @Test
    void testFindByIdSuccess() {
        // Given. Arrange inputs and targets. Define the behavior of Mock object artifactRepository


        Artifact a = new Artifact();
        a.setId("1250808601744904192");
        a.setName("Invisibility Cloak");
        a.setDescrition("An invisibility cloak is used to make the wearer invisible.");
        a.setImageUrl("ImageUrl");

        Wizard w = new Wizard();
        w.setId("2");
        w.setName("Harry Potter");

        a.setOwner(w);

        given(artifactRepository.findById("1250808601744904192")).willReturn(Optional.of(a)); //What the repository returns

        //When. Act on the target behavior. What steps should cover the method to be tested
        Artifact returnArtifact =  artifactService.findById("1250808601744904192"); //What the service return

        //Then. Assert expected outcomes.
        assertThat(returnArtifact.getId()).isEqualTo(a.getId()); //Check if the return values are equal
        assertThat(returnArtifact.getName()).isEqualTo(a.getName());
        assertThat(returnArtifact.getDescrition()).isEqualTo(a.getDescrition());
        assertThat(returnArtifact.getImageUrl()).isEqualTo(a.getImageUrl());

        verify(artifactRepository,times(1)).findById("1250808601744904192"); //Verify that mock object method is called once in service object

    }

    @Test
    void testFindByIdNotFound(){
        //Given
        given(artifactRepository.findById(Mockito.any(String.class))).willReturn(Optional.empty());

        //When
        Throwable thrown = catchThrowable(()->{
            Artifact returnArtifact =  artifactService.findById("1250808601744904192");

        });
        //Then
        assertThat(thrown)
                .isInstanceOf(ArtifactNotFoundException.class)
                .hasMessage("Could not find artifact with Id 1250808601744904192 :(");
        verify(artifactRepository,times(1)).findById("1250808601744904192"); //Verify that mock object method is called once in service object
    }

    @Test
    void testFindAllSuccess(){
        //Given
        given(artifactRepository.findAll()).willReturn(this.artifacts);

        //When
       List<Artifact> actualArtifact = artifactService.findAll();
        //Then
        assertThat(actualArtifact.size()).isEqualTo(this.artifacts.size());
        verify(artifactRepository,times(1)).findAll();
}
    @Test
    void testSaveSuccess(){
        //Given
        Artifact newArtifact = new Artifact();
        newArtifact.setName("Artifact 3");
        newArtifact.setDescrition("Description...");
        newArtifact.setImageUrl("ImageUrl...");

        given(idWorker.nextId()).willReturn(123456L);
        given(artifactRepository.save(newArtifact)).willReturn(newArtifact);

        //When
        Artifact savedArtifact = artifactService.save(newArtifact);

        //Then
        assertThat(savedArtifact.getId()).isEqualTo("123456");
        assertThat(savedArtifact.getName()).isEqualTo(newArtifact.getName());
        assertThat(savedArtifact.getDescrition()).isEqualTo(newArtifact.getDescrition());
        assertThat(savedArtifact.getImageUrl()).isEqualTo(newArtifact.getImageUrl());
        verify(artifactRepository,times(1)).save(newArtifact);
    }

    @Test
    void testUpdateSuccess(){
        //Given
        Artifact oldArtifact  = new Artifact();
        oldArtifact.setId("1250808601744904192");
        oldArtifact.setName("Invisibility Cloak");
        oldArtifact.setDescrition("An invisibility cloak is used to make the wearer invisible");
        oldArtifact.setImageUrl("ImageUrl");

        Artifact update = new Artifact();
        update.setId("1250808601744904192");
        update.setName("Invisibility Cloak");
        update.setDescrition("A new description.");
        update.setImageUrl("ImageUrl");

        given(artifactRepository.findById("1250808601744904192")).willReturn(Optional.of(oldArtifact));
        given(artifactRepository.save(oldArtifact)) .willReturn(oldArtifact);

        //When
        Artifact updatedArtifact = artifactService.update("1250808601744904192",update);

        //Then
        assertThat(updatedArtifact.getId()).isEqualTo(update.getId());
        assertThat(updatedArtifact.getDescrition()).isEqualTo(update.getDescrition());
        verify(artifactRepository,times(1)).findById("1250808601744904192");
        verify(artifactRepository,times(1)).save(oldArtifact);
    }

    @Test
    void testUpdateNotFound(){
        //Given
        Artifact update = new Artifact();
        update.setName("Invisibility Cloak");
        update.setDescrition("A new description.");
        update.setImageUrl("ImageUrl");

        given(artifactRepository.findById("1250808601744904192")).willReturn(Optional.empty());

        //When
        assertThrows(ArtifactNotFoundException.class, () ->{
            artifactService.update("1250808601744904192",update);
        });

        //Then
        verify(artifactRepository,times(1)).findById("1250808601744904192");

    }

    @Test
    void testDeleteSuccess(){
        //Given
        Artifact artifact = new Artifact();
        artifact.setId("1250808601744904192");
        artifact.setName("Invisibility Cloak");
        artifact.setDescrition("A new description.");
        artifact.setImageUrl("ImageUrl");

        given(artifactRepository.findById("1250808601744904192")).willReturn(Optional.of(artifact));
        doNothing().when(artifactRepository).deleteById("1250808601744904192");

        //When
        artifactService.delete("1250808601744904192");

        //Then
        verify(artifactRepository,times(1)).deleteById("1250808601744904192");

    }

    @Test
    void testDeleteNotFound(){
        //Given
        given(artifactRepository.findById("1250808601744904192")).willReturn(Optional.empty());

        //When
        assertThrows(ArtifactNotFoundException.class, () ->{
            artifactService.delete("1250808601744904192");
        });
        //Then
        verify(artifactRepository,times(1)).findById("1250808601744904192");

    }
}