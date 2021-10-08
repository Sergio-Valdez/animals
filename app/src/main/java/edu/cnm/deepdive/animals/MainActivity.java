package edu.cnm.deepdive.animals;

import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import edu.cnm.deepdive.animals.model.Animal;
import edu.cnm.deepdive.animals.service.WebServiceProxy;
import java.io.IOException;
import java.util.List;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

  private Spinner animal_Selector;
  private ArrayAdapter<Animal> adapter;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);
    animal_Selector = findViewById(R.id.animal_selector);
    new Retriever().start();
  }

  private class Retriever extends Thread {

    @Override
    public void run() {

      try {
        Response<List<Animal>> response = WebServiceProxy.getInstance()
            .getAnimals()
            .execute();
        if (response.isSuccessful()) {
          Log.d(getClass().getSimpleName(), response.body().toString());
          List<Animal> animals = response.body();
          runOnUiThread(new Runnable() {
            @Override
            public void run() {
              adapter = new ArrayAdapter<>(MainActivity.this, R.layout.item_animal_spinner,
                  animals);
              adapter.setDropDownViewResource(android.R.layout.simple_spinner_item);
              animal_Selector.setAdapter(adapter);
            }
          });
        } else {
          Log.e(getClass().getName(), response.message());
        }

      } catch (IOException e) {
        Log.e(getClass().getName(), e.getMessage(), e);
      }
    }
  }
}