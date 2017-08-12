package com.clarifai.clarapplai.api;

public final class ClarifaiEmbeddingResult {
    private final double[] embeddings;

    ClarifaiEmbeddingResult(double[] embeddings) {
        this.embeddings = embeddings;
    }

    /**
     * @return the embeddings for this image. This is a 1024-element array of floating-point numbers
     */
    public double[] getEmbeddings() {
        return embeddings;
    }
}
