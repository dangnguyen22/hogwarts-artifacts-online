package edu.tcu.cs.hogwartsartifactsonline.wizard;

import edu.tcu.cs.hogwartsartifactsonline.artifacts.ArtifactRepository;
import edu.tcu.cs.hogwartsartifactsonline.artifacts.utils.IdWorker;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Transactional
public class WizardService {
    private final WizardRepository wizardRepository;
    private final IdWorker idWorker;


    public WizardService(WizardRepository wizardRepository, IdWorker idWorker) {
        this.wizardRepository = wizardRepository;
        this.idWorker = idWorker;
    }

    public List<Wizard> findAll(){return this.wizardRepository.findAll();}

    public Wizard findById(String wizardId){
        return this.wizardRepository.findById(wizardId)
                .orElseThrow(() -> new WizardNotFoundException(wizardId));
    }
}
