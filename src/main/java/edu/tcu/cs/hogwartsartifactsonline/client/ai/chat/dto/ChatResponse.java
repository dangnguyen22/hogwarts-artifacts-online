package edu.tcu.cs.hogwartsartifactsonline.client.ai.chat.dto;

import java.util.List;

public record ChatResponse(List<Choice> choices) {
}
