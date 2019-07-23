package com.arfeenkhan.recyclerviewfillter;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.SearchView;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity implements SearchView.OnQueryTextListener {

    RecyclerView tagRecycler;
    ArrayList<SelectTagModel> taglist;
    SelectTagAdapter tagAdapter;
    TextView loadingTxt;
    String data_url = "http://magicconversion.com/barcodescanner/tagdata.php";

    StringRequest request;
    ProgressDialog progressDialog;

    String place;
    LinearLayoutManager llm;

    EditText search_edit;
    Button doneBtn;
    SearchView search_view;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        progressDialog = new ProgressDialog(this);
        search_view = findViewById(R.id.searchView);

        search_view.setOnQueryTextListener(this);

//        search_edit.addTextChangedListener(new TextWatcher() {
//            @Override
//            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
//
//            }
//
//            @Override
//            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
//                ArrayList<SelectTagModel> newList = new ArrayList<>();
//                for (SelectTagModel tgm : newList) {
//                    String name = tgm.getName().toLowerCase();
//                    if (name.contains(charSequence))
//                        newList.add(tgm);
//                }
//                tagAdapter.setFilter(newList);
//            }
//
//            @Override
//            public void afterTextChanged(Editable editable) {
//
//            }
//        });

//        doneBtn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                String text = search_edit.getText().toString();
//                text.toLowerCase();
//                ArrayList<SelectTagModel> newList = new ArrayList<>();
//                for (SelectTagModel tgm : newList) {
//                    String name = tgm.getName().toLowerCase();
//                    if (name.contains(name))
//                        newList.add(tgm);
//                }
//                tagAdapter.setFilter(newList);
//            }
//        });


        taglist = new ArrayList<>();
        getData();
        tagRecycler = findViewById(R.id.select_tags);
        tagAdapter = new SelectTagAdapter(this, taglist);
        llm = new LinearLayoutManager(this);
        tagRecycler.setLayoutManager(llm);
        tagRecycler.setAdapter(tagAdapter);
        tagRecycler.setHasFixedSize(true);
    }

    private void getData() {
        progressDialog.setMessage("Please wait...");
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.show();
        taglist.clear();
        RequestQueue queue = Volley.newRequestQueue(this);
        StringRequest sr = new StringRequest(Request.Method.POST, data_url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONArray arr = new JSONArray(response);
                    JSONObject c = null;
                    for (int i = 0; i < arr.length(); i++) {
                        c = arr.getJSONObject(i);
                        String tag = c.getString("tagno");
                        String place = c.getString("place");
                        String name = c.getString("name");
                        String ctf = c.getString("ctf");
                        String time = c.getString("time");
                        String date = c.getString("date");
                        String tf = c.getString("tf");
                        String sessionname = c.getString("ss_name");
                        SelectTagModel stm = new SelectTagModel(name, place, tag, time, ctf, date, tf, sessionname);
                        taglist.add(stm);
                        progressDialog.dismiss();
//                        loadingTxt.setVisibility(View.INVISIBLE);
                        tagAdapter.notifyDataSetChanged();

                    }

//                    JSONObject c1=arr.getJSONObject(0);
//                    String message = c1.getString("message");
//
////                    String message = c.getString("message");
//                    Toast.makeText(SelectTags.this, message, Toast.LENGTH_SHORT).show();

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("place", "Mumbai");
                return params;

            }
        };
        queue.add(sr);

    }

    @Override
    public boolean onQueryTextSubmit(String s) {
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        newText = newText.toLowerCase();
        ArrayList<SelectTagModel> newList = new ArrayList<>();
        for (SelectTagModel tgm : taglist) {
            String name = tgm.getName().toLowerCase();
            String time = tgm.getTime().toLowerCase();
            String ctf = tgm.getCtf().toLowerCase();
            String tagno = tgm.getTagno().toLowerCase();
            String date = tgm.getDate().toLowerCase();
            if (name.contains(newText)) {
                newList.add(tgm);
            } else if (time.contains(newText)) {
                newList.add(tgm);
            } else if (ctf.contains(newText)) {
                newList.add(tgm);
            } else if (tagno.contains(newText)) {
                newList.add(tgm);
            } else if (date.contains(newText)) {
                newList.add(tgm);
            }
        }
        tagAdapter.setFilter(newList);
        return true;
    }
}
