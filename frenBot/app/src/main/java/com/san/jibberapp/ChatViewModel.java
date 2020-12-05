package com.san.jibberapp;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.san.jibberapp.roomdb.ChatRepo;

import java.util.List;

public class ChatViewModel extends AndroidViewModel {

    // difference : android vm : passed application , you should never store context in the view mmodel ;
    // results in memory leak!

    private ChatRepo repository ;
    private LiveData<List<Chat>> allNotes;
  //  private String reply;
    public ChatViewModel(@NonNull Application application) {
        super(application);
        repository = new ChatRepo(application);
        allNotes = repository.getAllNotes();
       // reply = repository.getReply();
    }



    public void insert(Chat note) {
        repository.insert(note);
    }
    public void update(Chat note) {
        repository.update(note);
    }
    public void delete(Chat note) {
        repository.delete(note);
    }
    public void deleteAllNotes() {
        repository.deleteAllNotes();
    }
    public LiveData<List<Chat>> getAllNotes() {
        return allNotes;
    }
    /*public String getRep(String s){
        return repository.getReply(s);
    }*/

}
