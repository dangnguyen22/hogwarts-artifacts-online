package edu.tcu.cs.hogwartsartifactsonline.artifacts;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import edu.tcu.cs.hogwartsartifactsonline.artifacts.dto.ArtifactDto;
import edu.tcu.cs.hogwartsartifactsonline.artifacts.utils.IdWorker;
import edu.tcu.cs.hogwartsartifactsonline.client.ai.chat.ChatClient;
import edu.tcu.cs.hogwartsartifactsonline.client.ai.chat.dto.ChatRequest;
import edu.tcu.cs.hogwartsartifactsonline.client.ai.chat.dto.ChatResponse;
import edu.tcu.cs.hogwartsartifactsonline.client.ai.chat.dto.Message;
import edu.tcu.cs.hogwartsartifactsonline.system.exception.ObjectNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Transactional
public class ArtifactService {

    private final ArtifactRepository artifactRepository;

    private final IdWorker idWorker;

    private final ChatClient chatClient;

    public ArtifactService(ArtifactRepository artifactRepository, IdWorker idWorker, ChatClient chatClient) {
        this.artifactRepository = artifactRepository;
        this.idWorker = idWorker;
        this.chatClient = chatClient;
    }


    public Artifact findById(String artifactId){
        return this.artifactRepository.findById(artifactId)
                .orElseThrow(()-> new ObjectNotFoundException("artifact",artifactId));

    }

    public List<Artifact> findAll(){
        return this.artifactRepository.findAll();
    }

    public Artifact save(Artifact newArtifact){
        newArtifact.setId(idWorker.nextId() + "");
        return this.artifactRepository.save(newArtifact);
    }

    public Artifact update(String artifactId, Artifact update){
     return  this.artifactRepository.findById(artifactId)
               .map(oldArtifact -> {
                    oldArtifact.setName(update.getName());
                    oldArtifact.setDescription(update.getDescrition());
                    oldArtifact.setImageUrl(update.getImageUrl());
                    return this.artifactRepository.save(oldArtifact);
               })
               .orElseThrow(() -> new ObjectNotFoundException("artifact",artifactId));
    }

    public void delete(String artifactId){
        this.artifactRepository.findById(artifactId)
                .orElseThrow(()-> new ObjectNotFoundException("artifact",artifactId));
        this.artifactRepository.deleteById(artifactId);
    }

    public String summarize(List<ArtifactDto> artifactDtos) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        String jsonArray = objectMapper.writeValueAsString(artifactDtos);

        //Prepare the messages for summarizing
        List<Message> messages = List.of(
                new Message("system","Your task is to generate a short summary of a given json array in at most 100 words. The summary must includes the number of artifacts, each artifact's description, the owner information. Don't mention that the summary is from a given json array"),
                new Message("user",jsonArray)
        );
        ChatRequest chatRequest = new ChatRequest("gpt-4", messages);

        ChatResponse chatResponse = this.chatClient.generate(chatRequest); //Tell chat client to generate the summary based on a given chat request

        return chatResponse.choices().get(0).message().content();
    }
}
