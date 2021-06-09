package com.example.firestore_example;

public class Note {
    private String documentId;
    private String titre;
    private String note;

    public Note() {
    }

    public Note(String titre, String note) {
        this.titre = titre;
        this.note = note;
    }

    public String getDocumentId() {
        return documentId;
    }

    public String getTitre() {
        return titre;
    }

    public String getNote() {
        return note;
    }

    public void setDocumentId(String documentId) {
        this.documentId = documentId;
    }

    public void setTitre(String titre) {
        this.titre = titre;
    }

    public void setNote(String note) {
        this.note = note;
    }
}
