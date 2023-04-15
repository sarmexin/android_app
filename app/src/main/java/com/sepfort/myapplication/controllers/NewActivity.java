package com.sepfort.myapplication.controllers;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.sepfort.myapplication.R;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class NewActivity extends AppCompatActivity {
    private TextView text1;
    private TextView text2;
    private Button mButton1;
    private Button mButton2;
    private static final String URL = "http://10.0.2.2:8080/greeting/test";

    // вызывается при создании экземпляра сабкласса активности
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // предоставляем классу активности пользовательский интерфейс
        setContentView(R.layout.test_connect_url);

        // получение ссылок на виджеты
        text1 = findViewById(R.id.question_text_view);
        text2 = findViewById(R.id.output_result);
        mButton1 = findViewById(R.id.button_1);
        mButton2 = findViewById(R.id.button_2);

        // назначаем слушателей на кнопки
        mButton1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                text2.setText(R.string.work1);
                // обработка вызова URL
                new GetURLData().execute(URL);
            }
        });
        mButton2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                text2.setText(R.string.work2);
            }
        });

        text1.setText(R.string.description);
        text2.setText(R.string.hello);

    }

    private class GetURLData extends AsyncTask<String, String, String> {
        // метод срабатывает перед запросом
        protected void onPreExecute() {
            super.onPreExecute();
            text2.setText("Ожидайте...");
        }

        // сам вызов
        @Override
        protected String doInBackground(String... strings) {
            HttpURLConnection connection = null;
            BufferedReader reader = null;

            try {
                java.net.URL url = new URL(strings[0]);
                connection = (HttpURLConnection) url.openConnection();
                connection.connect();

                InputStream stream = connection.getInputStream();
                reader = new BufferedReader(new InputStreamReader(stream));

                StringBuffer buffer = new StringBuffer();
                String line = "";


                while ((line = reader.readLine()) != null) {
                    buffer.append(line).append("\n");
                }

                return buffer.toString();

            } catch (IOException e) {
                throw new RuntimeException(e);
            } finally {
                if (connection != null) {
                    connection.disconnect();
                }
                try {
                    if (reader != null) {
                        reader.close();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        // отрабатывает после вызова
        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

            text2.setText(result);
        }
    }
}
