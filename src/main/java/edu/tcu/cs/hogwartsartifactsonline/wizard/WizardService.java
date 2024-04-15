package edu.tcu.cs.hogwartsartifactsonline.wizard;

import edu.tcu.cs.hogwartsartifactsonline.artifacts.Artifact;
import edu.tcu.cs.hogwartsartifactsonline.artifacts.ArtifactRepository;
import edu.tcu.cs.hogwartsartifactsonline.artifacts.utils.IdWorker;
import edu.tcu.cs.hogwartsartifactsonline.system.exception.ObjectNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Transactional
public class WizardService {
    private final WizardRepository wizardRepository;
    private final IdWorker idWorker;
    private final ArtifactRepository artifactRepository;


    public WizardService(WizardRepository wizardRepository, IdWorker idWorker, ArtifactRepository artifactRepository) {
        this.wizardRepository = wizardRepository;
        this.idWorker = idWorker;
        this.artifactRepository = artifactRepository;
    }

    public List<Wizard> findAll(){return this.wizardRepository.findAll();}

    public Wizard findById(String wizardId){
        return this.wizardRepository.findById(wizardId)
                .orElseThrow(() -> new ObjectNotFoundException("wizard",wizardId));
    }

    public Wizard save(Wizard newWizard){
        newWizard.setId(idWorker.nextId()+ "");
        return this.wizardRepository.save(newWizard);
    }

    public Wizard update(String wizardId, Wizard update){
        return this.wizardRepository.findById(wizardId)
                .map(oldWizard -> {
                    oldWizard.setName(update.getName());
                    return this.wizardRepository.save(oldWizard);
                })
                .orElseThrow(() -> new ObjectNotFoundException("wizard",wizardId));
    }

    public void delete(String wizardId){
        this.wizardRepository.findById(wizardId)
                .orElseThrow(() -> new ObjectNotFoundException("wizard",wizardId));
        this.wizardRepository.deleteById(wizardId);
    }

    public void assginArtifact(String wizardId, String artifactId){
        //Find Artifact by id from database
        Artifact artifactToBeAssigned = this.artifactRepository.findById(artifactId).orElseThrow(() -> new ObjectNotFoundException("artifact",artifactId));

        //Find Wizard by id from database
        Wizard wizard = this.wizardRepository.findById(wizardId).orElseThrow(() -> new ObjectNotFoundException("wizard",wizardId));

        //Check if the artifact is already assigned to the wizard
        if(artifactToBeAssigned.getOwner() != null){
            artifactToBeAssigned.getOwner().removeArtifact(artifactToBeAssigned);
        }

        wizard.addArtifact(artifactToBeAssigned);

    }
}
