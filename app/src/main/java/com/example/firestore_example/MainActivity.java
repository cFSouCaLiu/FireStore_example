package com.example.firestore_example;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;

import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {
    // var globales :
    private static final String TAG = "MainActivity";
    private static final String KEY_TITRE = "titre";
    private static final String KEY_NOTE = "note";
    //attrib globaux :
    private EditText et_titre, et_note;
    private TextView tv_saveNote, tv_showNote;
    //connex a Firestore :
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    //ajout de la ref a la collection : fgfg
    private DocumentReference noteRef = db.document("ListeDesNotes/Ma première note");

    /**
     * Methode initUI()
     **/
    public void initUI() {
        et_titre = (EditText) findViewById(R.id.et_titre);
        et_note = (EditText) findViewById(R.id.et_note);
        tv_saveNote = (TextView) findViewById(R.id.tv_saveNote);
        tv_showNote = (TextView) findViewById(R.id.tv_showNote);
    }

    public void saveNote(View view) {
        String titre = et_titre.getText().toString();
        String note = et_note.getText().toString();
        /** Containeur pour transmettre ces donnees a Firestore **/
// Map fonctionne sur un modeLe cLe vaLeurs
// Ici on donne Le tpye de donnee puis un object comme ça on peut tout passer
        Map<String, Object> contenuNote = new HashMap<>();
        contenuNote.put(KEY_TITRE, titre);
        contenuNote.put(KEY_NOTE, note);
        /*• Envoi des donnees dans FireStore **/
        noteRef.set(contenuNote)
                // Ajout du addOnSuccessListener pour verifier que tout c'est bien passe
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Toast.makeText(MainActivity.this, "Note enregistrée", Toast.LENGTH_SHORT).show();
                    }
                })
                // Ajout du addOnFaiLureListener qui affichera L'erreur dans Le Log d
//      //et d'un taost pour L'UX
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull @org.jetbrains.annotations.NotNull Exception e) {
                        Toast.makeText(MainActivity.this, "Erreur lors de l'envoi",
                                Toast.LENGTH_SHORT).show();
                        Log.d(TAG, e.toString());
                    }
                });
    }

    public void showNote(View view) {
        noteRef.get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        if (documentSnapshot.exists()) {
                            /** Pour recuperer nos donnees on cree une string qui va recevoir
                             les donnees Liees aux cLes**/
                            String titre = documentSnapshot.getString(KEY_TITRE);
                            String note = documentSnapshot.getString(KEY_NOTE);
                            // On peut aussi creer une Map avec Les resuLtats
                            // Map<String, Object> note = documentSnapshot.getData();
                            tv_saveNote.setText("Titre de la note : " + titre + "\n" + note);
                        } else {
                            Toast.makeText(MainActivity.this, "Le doc n'existe pas ! ",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull @NotNull Exception e) {
                        Toast.makeText(MainActivity.this,
                                "Erreur lors de la lecture de la db ! ", Toast.LENGTH_SHORT).show();
                        Log.d(TAG, e.toString());
                    }
                });



    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initUI();
    }

    @Override
    protected void onStart() {
        noteRef.addSnapshotListener(this, new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable @org.jetbrains.annotations.Nullable DocumentSnapshot value,
                                @Nullable @org.jetbrains.annotations.Nullable FirebaseFirestoreException error) {
                if(error != null){
                    Toast.makeText(MainActivity.this, "Erreur de chargment !",
                            Toast.LENGTH_SHORT).show();
                    Log.d(TAG, error.toString());
                    return;
                }

                if(value.exists()){
                    String titre = value.getString(KEY_TITRE);
                    String note = value.getString(KEY_NOTE);
                    tv_showNote.setText("Titre de la note : "+ titre + "\n" + "Note : " + note);
                } else {
                    tv_showNote.setText("");
                }
            }
        });
        super.onStart();
    }

}