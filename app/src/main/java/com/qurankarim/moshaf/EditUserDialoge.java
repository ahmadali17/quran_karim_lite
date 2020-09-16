package com.qurankarim.moshaf;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDialogFragment;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.List;

public class EditUserDialoge extends AppCompatDialogFragment {
    private EditText editTextUsername;
    private EditText editTextEmail;
    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.activity_edit_user_dialoge, null);

        editTextUsername = view.findViewById(R.id.edit_username);
        editTextEmail = view.findViewById(R.id.edit_user_email);

        builder.setView(view)
                .setTitle("تعديل البيانات")
                .setNegativeButton("cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                    }
                })
                .setPositiveButton("Save", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        final String username = editTextUsername.getText().toString();
                        final String userEmail = editTextEmail.getText().toString();

                        App.mDb.collection("users")
                                .whereEqualTo("userAndroidId",App.androidId)
                                .get()
                                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                    @Override
                                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                        List<DocumentSnapshot> documentSnapshots = task.getResult().getDocuments();
                                        if(documentSnapshots.size()>0){
                                            DocumentSnapshot documentSnapshot = documentSnapshots.get(0);
                                            DocumentReference userDoc = documentSnapshot.getReference();
                                            if(!userEmail.isEmpty()){
                                                userDoc.update("userEmail",userEmail);
                                            }
                                            if(!username.isEmpty()){
                                                userDoc.update("userName",username);
                                            }

                                        }
                                    }
                                });
                    }
                });

        return builder.create();
    }
}