package com.miempresa.autores;

import androidx.appcompat.app.AppCompatActivity;

import android.icu.text.StringSearch;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.lang.ref.WeakReference;

public class MainActivity extends AppCompatActivity {

    private EditText inputBook;
    private TextView bookTitle;
    private TextView bookAuthor;
    private String imagen;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        inputBook = (EditText)findViewById(R.id.inputbook);
        bookTitle = (TextView)findViewById(R.id.booktitle);
        bookAuthor = (TextView)findViewById(R.id.bookAuthor);



    }

    public void searchBook(View view) {
        String searchString = inputBook.getText().toString();
        //requerir el uso del servicio externo
        new GetBook(bookTitle,bookAuthor).execute(searchString);
    }
    //Android 9 en adelante ya se vuelve absoleta
    //STRING : VOID : STRING: resultado
    public class GetBook extends AsyncTask<String,Void,String> {

        //constructores
        public GetBook(TextView mTextTitle,TextView mTextAuthor) {
            this.mTextTitle = new WeakReference<>(mTextTitle);
            this.mTextAuthor = new WeakReference<>(mTextAuthor);
        }

        private WeakReference<TextView> mTextTitle;
        private WeakReference<TextView> mTextAuthor;


        @Override
        protected String doInBackground(String... strings) {
            return NetUtilities.getBookInfo(strings[0]);
        }

        @Override
        protected void onPostExecute(String s){

            super.onPostExecute(s);
            //cargando al array los datos del json
            try {
                JSONObject jsonObject =  new JSONObject(s);
                JSONArray itemsArray = jsonObject.getJSONArray("items");
                int i= 0;
                String title = null;
                String author = null;
                while (i<itemsArray.length() && (title == null && author ==null)){
                    JSONObject book = itemsArray.getJSONObject(i);
                    JSONObject volumenInfo = book.getJSONObject("volumeInfo");
                    try {
                        title = volumenInfo.getString("title");
                        author = volumenInfo.getString("authors");

                    }catch (JSONException e){
                        e.printStackTrace();
                    }
                    i++;
                }
                if (title !=null && author!=null){
                    mTextTitle.get().setText(title);
                    mTextAuthor.get().setText(author);
                }
                else{
                    mTextTitle.get().setText("No existen resultas para la consulta");
                    mTextAuthor.get().setText("author");
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

    }
}