package adapters;

import android.app.Activity;
import android.content.Context;
import android.database.DataSetObserver;
import android.graphics.Typeface;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.baromet.j.baroapp.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by Jim on 10/02/2016.
 */
public class JsonListAdapter implements ListAdapter{

    private static LayoutInflater inflater = null;


    private JSONArray json;
    private String listname;
    private Activity activity;
    private Context context;

    public JsonListAdapter(JSONArray json, Activity activity, Context context, String listname){
        this.activity = activity;
        this.json = json;
        this.context = context;
        this.listname = listname;

        inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }



    @Override
    public boolean areAllItemsEnabled() {
        return true;
    }

    @Override
    public boolean isEnabled(int position) {
        return true;
    }

    @Override
    public void registerDataSetObserver(DataSetObserver observer) {

    }

    @Override
    public void unregisterDataSetObserver(DataSetObserver observer) {

    }

    @Override
    public int getCount() {

        return json.length();
    }

    @Override
    public Object getItem(int position) {

        try {
            if(json!=null) {
                return json.getJSONObject(position).get(listname);
            }
            else{
                Log.d("Json is null", "null null null");
            }
        }catch(JSONException e){
            Log.d("JSON EXCEPTION","JSON Exception while while reading"+e.getMessage());
        }
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View vi = convertView;

        if (vi == null)
            vi = inflater.inflate(R.layout.vakkenlijst_body,null);

        TextView text = (TextView) vi.findViewById(R.id.vakkenlijst_body);

        try {
            text.setText(json.getJSONObject(position).get(listname).toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return vi;
    }

    @Override
    public int getItemViewType(int position) {
        return 0;
    }

    @Override
    public int getViewTypeCount() {
        return 1;
    }

    @Override
    public boolean isEmpty() {
        return false;
    }
}
