package edu.tcu.cs.hogwartsartifactsonline.wizard;

import edu.tcu.cs.hogwartsartifactsonline.artifacts.Artifact;
import edu.tcu.cs.hogwartsartifactsonline.artifacts.dto.ArtifactDto;
import edu.tcu.cs.hogwartsartifactsonline.system.Result;
import edu.tcu.cs.hogwartsartifactsonline.system.StatusCode;
import edu.tcu.cs.hogwartsartifactsonline.wizard.converter.WizardToWizardDtoConverter;
import edu.tcu.cs.hogwartsartifactsonline.wizard.dto.WizardDto;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/wizards")
public class WizardController {
    private final WizardToWizardDtoConverter wizardToWizardDtoConverter;
    private final WizardService wizardService;
    public WizardController(WizardToWizardDtoConverter wizardToWizardDtoConverter, WizardService wizardService) {
        this.wizardToWizardDtoConverter = wizardToWizardDtoConverter;
        this.wizardService = wizardService;
    }

    @GetMapping
    public Result findAllWizards(){
        List<Wizard> foundWizard = this.wizardService.findAll();
        //Convert foundWizard to WizardDtos
        List<WizardDto> wizardDtos = foundWizard.stream().map(this.wizardToWizardDtoConverter::convert)
                .collect(Collectors.toList());

        return new Result(true, StatusCode.SUCCESS,"Find All Success", wizardDtos);
    }

    @GetMapping("/{wizardId}")
    public Result findWizardById(@PathVariable String wizardId){
        Wizard foundWizard = this.wizardService.findById(wizardId);
        WizardDto wizardDto = this.wizardToWizardDtoConverter.convert(foundWizard);
        return new Result(true, StatusCode.SUCCESS,"Find One Success", wizardDto);
    }
}
