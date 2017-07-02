package it.polimi.ingsw.pc34.View.GUI;

import javafx.scene.image.Image;

/**
 * Created by trill on 01/07/2017.
 */
class LocatedImage extends Image{
    private final String url;

    public LocatedImage(String url){
        super(url);
        this.url = url;
    }

    public LocatedImage(String url, double requestedWidth, double requestedHeight, boolean preserveRatio, boolean smooth){
        super(url, requestedWidth, requestedHeight, preserveRatio, smooth);
        this.url = url;
    }

    public String getURL(){
        return url;
    }
}
