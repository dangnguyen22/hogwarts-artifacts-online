package edu.tcu.cs.hogwartsartifactsonline.artifacts;

import edu.tcu.cs.hogwartsartifactsonline.wizard.Wizard;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;

import java.io.Serializable;

@Entity
public class Artifact implements Serializable {

    @Id
    private String id;
    private String name;

    private String descrition;

    private String imageUrl;

    @ManyToOne
    private Wizard owner;
    public Artifact(){

    }

    public Wizard getOwner() {
        return owner;
    }

    public void setOwner(Wizard owner) {
        this.owner = owner;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public String getDescrition() {
        return descrition;
    }

    public void setDescription(String descrition) {
        this.descrition = descrition;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }
}
