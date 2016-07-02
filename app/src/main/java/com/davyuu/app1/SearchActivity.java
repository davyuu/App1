package com.davyuu.app1;

import android.content.Intent;
import android.speech.RecognitionListener;
import android.speech.RecognizerIntent;
import android.speech.SpeechRecognizer;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import static android.R.layout.simple_list_item_1;

public class SearchActivity extends AppCompatActivity implements RecognitionListener {

    private EditText searchEditText;
    private ImageButton searchButton;
    private ListView searchListView;
    private List<String> searchList;
    private ArrayAdapter searchArrayAdapter;
    private SpeechRecognizer speech;
    private Intent recognizerIntent;
    private String LOG_TAG = "SearchActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        final DatabaseHelper dbHelper = new DatabaseHelper(this);

        searchEditText = (EditText) findViewById(R.id.editText_search);
        searchButton = (ImageButton) findViewById(R.id.button_search);
        searchListView = (ListView) findViewById(R.id.listView_search);
        searchButton.setImageResource(R.drawable.microphone);

        searchList = dbHelper.getName();

        searchArrayAdapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, searchList);
        searchListView.setAdapter(searchArrayAdapter);

        searchEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i2, int i3) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i2, int i3) {
                searchList();
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                displaySpeechRecognizer();
            }
        });

        searchListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                String name = ((TextView) view).getText().toString();
                String surname = dbHelper.getSurname(name);
                String mark = dbHelper.getMark(name);
                String studentId = dbHelper.getId(name);

                StringBuffer buffer = new StringBuffer();
                buffer.append("Id: " + studentId + "\n");
                buffer.append("Name: " + name + "\n");
                buffer.append("Surname: " + surname + "\n");
                buffer.append("Mark: " + mark);

                showMessage("Data", buffer.toString());
            }
        });

        createSpeechRecognizer();
    }

    private void createSpeechRecognizer() {
        speech = SpeechRecognizer.createSpeechRecognizer(this);
        speech.setRecognitionListener(this);

        recognizerIntent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        recognizerIntent.putExtra(RecognizerIntent.EXTRA_SPEECH_INPUT_COMPLETE_SILENCE_LENGTH_MILLIS, 60000);
        recognizerIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        recognizerIntent.putExtra(RecognizerIntent.EXTRA_MAX_RESULTS, 3);

        speech.startListening(recognizerIntent);
    }

    public void showMessage(String title, String msg) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setCancelable(true);
        builder.setTitle(title);
        builder.setMessage(msg);
        builder.show();
    }

    private void searchList() {
        String searchText = searchEditText.getText().toString();
        searchList = new ArrayList<>();

        DatabaseHelper helper = new DatabaseHelper(this);
        List<String> nameList = helper.getName();

        for (String name : nameList) {
            if (name.toLowerCase().contains(searchText.toLowerCase())) {
                searchList.add(name);
            }
        }

        searchArrayAdapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, searchList);
        searchListView.setAdapter(searchArrayAdapter);
    }

    private static final int SPEECH_REQUEST_CODE = 0;

    private void displaySpeechRecognizer() {
        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        startActivityForResult(intent, SPEECH_REQUEST_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode,
                                    Intent data) {
        if (requestCode == SPEECH_REQUEST_CODE && resultCode == RESULT_OK) {
            List<String> results = data.getStringArrayListExtra(
                    RecognizerIntent.EXTRA_RESULTS);
            String spokenText = results.get(0);
            searchEditText.setText(spokenText);
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        Log.i(LOG_TAG, "paused");
        if (speech != null) {
            speech.destroy();
            Log.i(LOG_TAG, "destroy");
        }
        //speech = null;
        super.onPause();
    }

    @Override
    public void onBeginningOfSpeech() {
        Log.i(LOG_TAG, "onBeginningOfSpeech");
    }

    @Override
    public void onBufferReceived(byte[] buffer) {
        Log.i(LOG_TAG, "onBufferReceived: " + buffer);
    }


    @Override
    public void onEndOfSpeech() {
        Log.i(LOG_TAG, "onEndOfSpeech");
    }

    @Override
    public void onError(int errorCode) {
        Log.d(LOG_TAG, "FAILED " + errorCode);
    }

    @Override
    public void onEvent(int arg0, Bundle arg1) {
        Log.i(LOG_TAG, "onEvent");
    }

    @Override
    public void onPartialResults(Bundle arg0) {
        Log.i(LOG_TAG, "onPartialResults");
    }

    @Override
    public void onReadyForSpeech(Bundle arg0) {
        Log.i(LOG_TAG, "onReadyForSpeech");
    }

    @Override
    public void onRmsChanged(float rmsdB) {
        Log.i(LOG_TAG, "onRmsChanged: " + rmsdB);
    }

    @Override
    public void onResults(Bundle results) {
        speech.stopListening();
        Log.i(LOG_TAG, "onResults");
        ArrayList<String> matches = results.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION);

        for (String result : matches) {
            if (result.equalsIgnoreCase("Ok Google")) {
                Toast.makeText(SearchActivity.this, "Ok Google Detected", Toast.LENGTH_LONG).show();
                displaySpeechRecognizer();
                return;
            }
        }
    }
}