package com.san.jibberapp.roomdb;

import android.app.Application;
import android.os.AsyncTask;

import androidx.lifecycle.LiveData;

import com.chaquo.python.PyObject;
import com.chaquo.python.Python;
import com.san.jibberapp.Chat;

import java.util.List;

public class ChatRepo {

    private ChatDAO chatDAO;
    private LiveData<List<Chat>> allChat;
    public String reply = "";
  //  Context context;


    public ChatRepo(Application application) { //application is subclass of context
        ChatDB database = ChatDB.getInstance(application);
        chatDAO = database.chatDAO();
        allChat = chatDAO.getAllNotes(); //cant call abstract func but since instance is there we can do this
        //result = "";
        reply = "";
      //  context = application;
    }

    //methods for database operations :-

    // these methods are api that the repo exposes to the outside
    public void insert(Chat chat) {
        new InsertNoteAsyncTask(chatDAO).execute(chat);
    }
    public void update(Chat chat) {
        new UpdateNoteAsyncTask(chatDAO).execute(chat);
    }
    public void delete(Chat chat) {
        new DeleteNoteAsyncTask(chatDAO).execute(chat);
    }
    public void deleteAllNotes() {
        new DeleteAllNotesAsyncTask(chatDAO).execute();
    }
    public LiveData<List<Chat>> getAllNotes() {
        return allChat;
    }

    public String getReply(String msg){
        ExampleAsyncTask exampleAsyncTask = new ExampleAsyncTask();
        exampleAsyncTask.execute(msg);
      //  String s = exampleAsyncTask.doInBackground(msg);
        //return result;
        return  reply;
    }

    //room doesnt allow db op in main thread so we'll do this in background by async tasks
    private static class InsertNoteAsyncTask extends AsyncTask<Chat, Void, Void> { //static : doesnt have reference to the
        // repo itself otherwise it could cause memory leak!
        private ChatDAO chatDAO;
        private InsertNoteAsyncTask(ChatDAO chatDAO) {
            this.chatDAO = chatDAO;
        }
        @Override
        protected Void doInBackground(Chat... chats) { // ...  is similar to array
            chatDAO.Insert(chats[0]); //single note
            return null;
        }
    }
    private static class UpdateNoteAsyncTask extends AsyncTask<Chat, Void, Void> {
        private ChatDAO chatDAO;
        private UpdateNoteAsyncTask(ChatDAO chatDAO) { //constructor as the class is static
            this.chatDAO = chatDAO;
        }
        @Override
        protected Void doInBackground(Chat... chats) {
            chatDAO.Update(chats[0]);
            return null;
        }
    }
    private static class DeleteNoteAsyncTask extends AsyncTask<Chat, Void, Void> {
        private ChatDAO chatDAO;
        private DeleteNoteAsyncTask(ChatDAO chatDAO) {
            this.chatDAO = chatDAO;
        }
        @Override
        protected Void doInBackground(Chat... chats) {
            chatDAO.Delete(chats[0]);
            return null;
        }
    }
    private static class DeleteAllNotesAsyncTask extends AsyncTask<Void, Void, Void> {
        private ChatDAO chatDAO;
        private DeleteAllNotesAsyncTask(ChatDAO chatDAO) {
            this.chatDAO = chatDAO;
        }
        @Override
        protected Void doInBackground(Void... voids) {
            chatDAO.DeleteAllNotes();
            return null;
        }
    }

    private static class ExampleAsyncTask extends AsyncTask<String, Void, String> {

        String result;
       // private final WeakReference<MainActivity> activityWeakReference;
        ExampleAsyncTask() {
            result = "";
        }
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
           /* MainActivity activity = activityWeakReference.get();
            if (activity == null || activity.isFinishing()) {
                return;
            }*/
           // activity.chatAdapter.notifyDataSetChanged();


        }
        @Override
        protected String doInBackground(String... strings) {

            PyObject p =  Python.getInstance().getModule("main");

            result =  p.callAttr("getResults", strings[0]).toString();



            return result;
        }
        @Override
        protected void onProgressUpdate(Void... values) {
            super.onProgressUpdate(values);
           /* MainActivity activity = activityWeakReference.get();
            if (activity == null || activity.isFinishing()) {
                return;
            }*/
            //activity.progressBar.setProgress(values[0]);
        }
        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);

            System.out.println("s = " + s);
            System.out.println("result = " + result);






           /* MainActivity activity = activityWeakReference.get();
            if (activity == null || activity.isFinishing()) {
                return;
            }*/

           /* Toast.makeText(activity, s, Toast.LENGTH_SHORT).show();
            activity.progressBar.setProgress(0);
            activity.progressBar.setVisibility(View.INVISIBLE);*/

        }
    }
}
