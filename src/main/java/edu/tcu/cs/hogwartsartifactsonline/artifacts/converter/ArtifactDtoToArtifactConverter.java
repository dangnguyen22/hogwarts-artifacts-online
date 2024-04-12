package edu.tcu.cs.hogwartsartifactsonline.artifacts.converter;

import edu.tcu.cs.hogwartsartifactsonline.artifacts.Artifact;
import edu.tcu.cs.hogwartsartifactsonline.artifacts.dto.ArtifactDto;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class ArtifactDtoToArtifactConverter implements Converter<ArtifactDto, Artifact> {

    @Override
    public Artifact convert(ArtifactDto source) {
        Artifact artifact = new Artifact();
        artifact.setId(source.id());
        artifact.setName(source.name());
        artifact.setDescrition(source.description());
        artifact.setImageUrl(source.imageUrl());
        return artifact;

    }
}
