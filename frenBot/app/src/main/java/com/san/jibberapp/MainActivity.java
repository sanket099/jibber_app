package com.san.jibberapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.chaquo.python.PyObject;
import com.chaquo.python.Python;
import com.chaquo.python.android.AndroidPlatform;

//import org.tensorflow.lite.Interpreter;

import java.lang.ref.WeakReference;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;


public class MainActivity extends AppCompatActivity implements ChatAdapter.OnNoteList {

   // Interpreter tfliteInterpreter;
    EditText editText;
    ImageView btn;
    List<Chat> chatList;
    RecyclerView recyclerView;
    ChatViewModel chatViewModel;

    ChatAdapter chatAdapter;
    AlertDialog.Builder builder;
    TextView botName, isTyping;

    SharedPreference sharedPreference;

    View view1, view2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        sharedPreference = new SharedPreference(this);

        editText = findViewById(R.id.et_input);
        btn = findViewById(R.id.iv_send);
        recyclerView = findViewById(R.id.rv_chat);
        botName = findViewById(R.id.tv_bot_name);
        isTyping = findViewById(R.id.is_typing);
        chatList = new ArrayList<>();

        builder = new AlertDialog.Builder(MainActivity.this);

        view1 = findViewById(R.id.view_back);
        chatViewModel = new ViewModelProvider(this).get(ChatViewModel.class); //init

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        if (! Python.isStarted()) {
            Python.start(new AndroidPlatform(this));
        }

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                button();

            }
        });

        botName.setText(sharedPreference.getName());

        initRv();

    }

    private String setDateTime(){
        Calendar c = Calendar.getInstance();
        System.out.println("Current time => "+c.getTime());

        SimpleDateFormat df = new SimpleDateFormat("dd-MM-yyyy HH:mm");
        String formattedDate = df.format(c.getTime());
        return formattedDate;
    }

    private void initRv(){

        if(chatList.size() == 0) {
            chatViewModel.getAllNotes().observe(this, new Observer<List<Chat>>() {
                @Override
                public void onChanged(List<Chat> chats) {
                    if (chats != null) {
                        chatList.clear();
                        chatList.addAll(chats);
                    }
                }
            });
        }
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        chatAdapter = new ChatAdapter(this,chatList,this);
        recyclerView.setAdapter(chatAdapter);
        if(chatList.size() != 0)
        recyclerView.scrollToPosition(chatList.size()-1);
    }

    private void py(String input){
        ExampleAsyncTask task = new ExampleAsyncTask(this);
        task.execute(input);
        //String s = chatViewModel.getRep(input);


      //  chatAdapter.notifyDataSetChanged();



        //initRv();

    }

    public void button() {

        btn.setEnabled(false);

        Chat chat = new Chat();
        String input = editText.getText().toString();
        editText.setText("");
        chat.setMessage(input);
        chat.setBotMessage(false);
        chat.setDate(setDateTime());
        chatList.add(chat);
        //chatAdapter.notifyDataSetChanged();
        chatViewModel.insert(chat);
        isTyping.setVisibility(View.VISIBLE);
        chatAdapter.notifyDataSetChanged();
        recyclerView.scrollToPosition(chatList.size()-1);

        InputMethodManager imm = (InputMethodManager) this.getSystemService(Activity.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(editText.getWindowToken(), 0);

        py(input);

        /*final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                // Do something after 5s = 5000ms


            }
        }, 500);*/

    }

    private static class ExampleAsyncTask extends AsyncTask<String, Void, String> {
        private final WeakReference<MainActivity> activityWeakReference;
        ExampleAsyncTask(MainActivity activity) {
            activityWeakReference = new WeakReference<MainActivity>(activity);
        }
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            MainActivity activity = activityWeakReference.get();
            if (activity == null || activity.isFinishing()) {
                return;
            }



        }
        @Override
        protected String doInBackground(String... strings) {

            PyObject p = Python.getInstance().getModule("main");

            String res = p.callAttr("getResults", strings[0]).toString();

            return res;
        }
        @Override
        protected void onProgressUpdate(Void... values) {
            super.onProgressUpdate(values);
            MainActivity activity = activityWeakReference.get();
            if (activity == null || activity.isFinishing()) {
                return;
            }
            //activity.progressBar.setProgress(values[0]);
        }
        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            MainActivity activity = activityWeakReference.get();
            if (activity == null || activity.isFinishing()) {
                return;
            }
            Chat chat = new Chat();
            chat.setBotMessage(true);
            chat.setDate(activity.setDateTime());
            chat.setMessage(s);
            activity.chatList.add(chat);//

            activity.chatViewModel.insert(chat);
            activity.chatAdapter.notifyDataSetChanged();

            activity.isTyping.setVisibility(View.GONE);
            activity.btn.setEnabled(true);
            activity.recyclerView.scrollToPosition(activity.chatList.size()-1);

           /* Toast.makeText(activity, s, Toast.LENGTH_SHORT).show();
            activity.progressBar.setProgress(0);
            activity.progressBar.setVisibility(View.INVISIBLE);*/


        }
    }

    @Override
    public void OnnoteClick(Chat userClass) {
        if(userClass.isBotMessage())
        Toast.makeText(this, "Received : " + userClass.getDate().toString(), Toast.LENGTH_SHORT).show();
        else
            Toast.makeText(this, "Sent : " + userClass.getDate().toString(), Toast.LENGTH_SHORT).show();

    }

    @Override
    public void OnLongClick(Chat downloadObject) {

    }
    /*private ArrayList<String> stemming(String sentence){
        SimpleTokenizer tokenizer = SimpleTokenizer.INSTANCE;
        PorterStemmer porterStemmer = new PorterStemmer();
        String[] tokens = tokenizer.tokenize(sentence);
        ArrayList<String> stems = new ArrayList<>();

        for(String token : tokens) {
            System.out.println(token);
            stems.add(porterStemmer.stem(token));
        }
        return stems;
    }

    private void doInference(String string){
        try {
            float[] input = new float[1];
            input[0] = Float.valueOf(string);
            float[][] out = new float[1][1];
            tfliteInterpreter.run(input, out);


            float inf = out[0][0];
            System.out.println("inf = " + inf);

            tfliteInterpreter.close();
        }
        catch (Exception e){
            System.out.println("e.getMessage() = " + e.getMessage());
        }
    }

    private MappedByteBuffer loadModelFile() throws IOException {

            AssetFileDescriptor fileDescriptor = this.getAssets().openFd("model.tflite");
            FileInputStream inputStream = new FileInputStream(fileDescriptor.getFileDescriptor());
            FileChannel fileChannel = inputStream.getChannel();
            long startOffset = fileDescriptor.getStartOffset();
            long declaredLength = fileDescriptor.getLength();
            return  fileChannel.map(FileChannel.MapMode.READ_ONLY, startOffset, declaredLength);


    }

    public String loadJSONFromAsset(String filename) {
        String json = null;
        try {
            InputStream is = this.getAssets().open(filename);
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            json = new String(buffer, "UTF-8");
        } catch (IOException ex) {
            ex.printStackTrace();
            return null;
        }
        return json;
    }
    private void read(){
        try {
            JSONObject jsonObject = new JSONObject(loadJSONFromAsset("word_dict.json"));
            JSONArray intents = jsonObject.getJSONArray("intents");

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    btn_menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PopupMenu popup = new PopupMenu(profile.this, v);
                MenuInflater inflater = popup.getMenuInflater();
                inflater.inflate(R.menu.profile, popup.getMenu());

                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        int id = item.getItemId();
                           if(id == R.id.action_active_requests){
                            Toast.makeText(profile.this, item.getTitle().toString(),Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(getApplicationContext(), ActiveRequestsActivity.class));
                        }
                        else if(id == R.id.action_previously_bought){
                            Toast.makeText(profile.this, item.getTitle().toString(),Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(getApplicationContext(), previously_bought.class));
                        }

                        return true;

                    }
                });
                popup.show();


            }
        });

*/

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        switch (id){

            case R.id.menu_info:
                startActivity(new Intent(MainActivity.this,InformationActivity.class));
                finish();
                break;
            case R.id.menu_theme:
                startActivity(new Intent(MainActivity.this,ThemeActivity.class));
                finish();
                break;

            case R.id.menu_delete:
                builder.setMessage(R.string.delete_all_chat)
                        .setCancelable(true)
                        .setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {

                                chatViewModel.deleteAllNotes();
                                chatList.clear();
                                chatAdapter.notifyDataSetChanged();
                                Toast.makeText(MainActivity.this, R.string.all_chats_del, Toast.LENGTH_SHORT).show();
                                //showCardAfterDelete();
                            }
                        })
                        .setNegativeButton(R.string.no, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                //  Action for 'NO' Button
                                dialog.cancel();
                            }
                        });
                //Creating dialog box
                AlertDialog alert = builder.create();
                //Setting the title manually
                alert.setTitle(getString(R.string.delete_all_chat));
                alert.show();

                Button positive = ((androidx.appcompat.app.AlertDialog)alert).getButton(AlertDialog.BUTTON_POSITIVE);
                positive.setBackgroundColor(Color.WHITE);
                positive.setTextColor(getColor(R.color.colorPrimaryDark));

                Button negative = ((androidx.appcompat.app.AlertDialog)alert).getButton(AlertDialog.BUTTON_NEGATIVE);
                negative.setTextColor(Color.BLACK);
                negative.setBackgroundColor(Color.WHITE);

                break;
            case R.id.menu_rate:

                break;


            case R.id.menu_share:
               /* try {

                    Bitmap icon = ((BitmapDrawable) getResources().getDrawable(R.drawable.coverpage)).getBitmap();
                    //.println("icon = " + icon);

                    Uri uri = getImageUri(OverlayActivity.this, icon);
                    //Toast.makeText(this, "Sharing...", Toast.LENGTH_SHORT).show();
                    //.println("uri = " + uri);


                    Intent share = new Intent(Intent.ACTION_SEND);
                    share.setType("image/jpeg");
                    share.putExtra(Intent.EXTRA_STREAM, uri);
                    share.putExtra(Intent.EXTRA_TEXT, getString(R.string.share_this_app) + ": http://play.google.com/store/apps/details?id=com.miskaa.recipes");
                    try {
                        share.setPackage("com.whatsapp");
                        startActivity(share);
                    } catch (Exception e) {
                        startActivity(Intent.createChooser(share, getString(R.string.share_app)));

                    }
                }
                catch (NullPointerException e){
                    Toast.makeText(this, R.string.please_try_again, Toast.LENGTH_SHORT).show();
                }*/
                break;


        }
        return super.onOptionsItemSelected(item);

    }
}

