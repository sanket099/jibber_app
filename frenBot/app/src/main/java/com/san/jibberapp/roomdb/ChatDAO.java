package com.san.jibberapp.roomdb;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.san.jibberapp.Chat;

import java.util.List;
@Dao
public interface ChatDAO {

    @Insert
    void Insert(Chat chat);

    @Update
        //(onConflict = OnConflictStrategy.REPLACE)
    void Update(Chat note);

    @Delete
    void Delete(Chat note);

    @Query("DELETE FROM chat_table")
    void DeleteAllNotes();

    @Query("SELECT * FROM chat_table Order By date Asc ")
    LiveData<List<Chat>> getAllNotes();  //updates and returns
}
