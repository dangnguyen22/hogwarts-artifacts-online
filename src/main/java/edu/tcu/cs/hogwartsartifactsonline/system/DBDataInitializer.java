package edu.tcu.cs.hogwartsartifactsonline.system;

import edu.tcu.cs.hogwartsartifactsonline.artifacts.Artifact;
import edu.tcu.cs.hogwartsartifactsonline.artifacts.ArtifactRepository;
import edu.tcu.cs.hogwartsartifactsonline.hogwartsuser.HogwartsUser;
import edu.tcu.cs.hogwartsartifactsonline.hogwartsuser.UserRepository;
import edu.tcu.cs.hogwartsartifactsonline.wizard.Wizard;
import edu.tcu.cs.hogwartsartifactsonline.wizard.WizardRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class DBDataInitializer implements CommandLineRunner {

    private final ArtifactRepository artifactRepository;
    private final WizardRepository wizardRepository;
    private final UserRepository userRepository;

    public DBDataInitializer(ArtifactRepository artifactRepository, WizardRepository wizardRepository, UserRepository userRepository) {
        this.artifactRepository = artifactRepository;
        this.wizardRepository = wizardRepository;
        this.userRepository = userRepository;
    }

    @Override
    public void run(String... args) throws Exception {
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

        Artifact a3 = new Artifact();
        a3.setId("1250808601744904193");
        a3.setName("Elder Wand");
        a3.setDescrition("The Elder Wand, known throughout history as the Deathstick or the Wand of Destiny, is an extremely powerful wand made of elder wood with a core of Thestral tail hair.");
        a3.setImageUrl("ImageUrl");

        Artifact a4 = new Artifact();
        a4.setId("1250808601744904194");
        a4.setName("The Maurader's Map");
        a4.setDescrition("A magical map of Hogwarts created by Remus Lupin, Peter Pettigrew, Sirius Black, and James Potter while they were students at Hogwarts.");
        a4.setImageUrl("ImageUrl");

        Artifact a5 = new Artifact();
        a5.setId("1250808601744904195");
        a5.setName("The Sword of Gryffindor");
        a5.setDescrition("A goblin-made sword adorned with large rubies on the pommel. It was once owned by Godric Gryffindor, one of the medieval founders of Hogwarts.");
        a5.setImageUrl("ImageUrl");

        Artifact a6 = new Artifact();
        a6.setId("1250808601744904196");
        a6.setName("Resurrection Stone");
        a6.setDescrition("The Resurrection Stone allows the holder to bring back the deceased loved ones, in a semi-physical form, and communicate with them.");
        a6.setImageUrl("ImageUrl");

        Wizard w1 = new Wizard();
        w1.setId("1");
        w1.setName("Albus Dumbledore");
        w1.addArtifact(a1);
        w1.addArtifact(a3);

        Wizard w2 = new Wizard();
        w2.setId("2");
        w2.setName("Harry Potter");
        w2.addArtifact(a2);
        w2.addArtifact(a4);

        Wizard w3 = new Wizard();
        w3.setId("3");
        w3.setName("Neville Longbottom");
        w3.addArtifact(a5);

        HogwartsUser u1 = new HogwartsUser();
        u1.setId(1);
        u1.setUsername("admin");
        u1.setPassword("admin");
        u1.setRole("admin user");
        u1.setEnabled(true);

        HogwartsUser u2 = new HogwartsUser();
        u2.setId(2);
        u2.setUsername("eric");
        u2.setPassword("123456");
        u2.setRole("user");
        u2.setEnabled(true);

        HogwartsUser u3 = new HogwartsUser();
        u3.setId(3);
        u3.setUsername("tom");
        u3.setPassword("qwerty");
        u3.setRole("user");
        u3.setEnabled(false);

        this.userRepository.save(u1);
        this.userRepository.save(u2);
        this.userRepository.save(u3);


        wizardRepository.save(w1);
        wizardRepository.save(w2);
        wizardRepository.save(w3);
        //Don't need to save artifact 1-5 since they are associated with the wizards saved above.
        artifactRepository.save(a6);


    }
}
