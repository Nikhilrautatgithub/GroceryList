package com.example.grocerylist;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    ListView  list_view;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Button addItem;
        EditText grocery;

        list_view=(ListView)findViewById(R.id.listview);
        addItem=(Button)findViewById(R.id.add_item);
        grocery=(EditText)findViewById(R.id.edittext);

        ArrayList<String> arrayList=new ArrayList<>();

        ArrayAdapter arrayAdapter=new ArrayAdapter(this, android.R.layout.simple_list_item_1,arrayList);
        list_view.setAdapter(arrayAdapter);

        addItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                arrayList.add(grocery.getText().toString().trim());
                arrayAdapter.notifyDataSetChanged();

            }
        });
        list_view.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {

                final int which_item=i;
                new AlertDialog.Builder(MainActivity.this).setIcon(android.R.drawable.ic_delete).setTitle("Are you Sure")
                        .setMessage("Do you want remove this item?").setPositiveButton("yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        arrayList.remove(which_item);
                        arrayAdapter.notifyDataSetChanged();
                    }
                })
                        .setNegativeButton("No",null)
                        .show();
                return true;
            }
        });

    }
}