package edu.tcu.cs.hogwartsartifactsonline.artifacts.converter;

import edu.tcu.cs.hogwartsartifactsonline.artifacts.Artifact;
import edu.tcu.cs.hogwartsartifactsonline.artifacts.dto.ArtifactDto;
import edu.tcu.cs.hogwartsartifactsonline.wizard.converter.WizardToWizardDtoConverter;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component public class ArtifactToArtifactDtoConverter implements Converter<Artifact, ArtifactDto> {
    private final WizardToWizardDtoConverter wizardToWizardDtoConverter;

    public ArtifactToArtifactDtoConverter (WizardToWizardDtoConverter wizardToWizardDtoConverter) {
        this.wizardToWizardDtoConverter = wizardToWizardDtoConverter;
    }

    @Override
    public ArtifactDto convert(Artifact source) {
        ArtifactDto artifactDto = new ArtifactDto(source.getId(),source.getName(),source.getDescrition(), source.getImageUrl(),
               source.getOwner() != null ? this.wizardToWizardDtoConverter.convert(source.getOwner()):null);
        return artifactDto;
    }
}
