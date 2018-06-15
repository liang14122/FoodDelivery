package sg.edu.rp.c346.fooddelivery.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import sg.edu.rp.c346.fooddelivery.Object.OrderItem;
import sg.edu.rp.c346.fooddelivery.R;

/**
 * Created by 16004118 on 18/11/2017.
 */

public class AlertCustomAdapter extends ArrayAdapter{
    Context parent_context;
    int layout_id;

    ArrayList<OrderItem> orderList;

    public AlertCustomAdapter(Context context,
                                int resource,
                                ArrayList<OrderItem> objects) {
        super(context, resource, objects);
        parent_context = context;
        layout_id = resource;
        orderList = objects;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        View rowView = convertView;

        if (rowView == null){
            LayoutInflater inflater = (LayoutInflater) parent_context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            rowView = inflater.inflate(layout_id, parent, false);
        }else{
            rowView = convertView;
        }

        TextView tvfoodName = (TextView) rowView.findViewById(R.id.textViewAlertFoodName);
        TextView tvprice = (TextView) rowView.findViewById(R.id.textViewAlertPrice);
        TextView tvQty = (TextView) rowView.findViewById(R.id.textViewAlertQty);

        OrderItem currentItem = orderList.get(position);
        tvfoodName.setText(currentItem.getName());
        tvprice.setText("$" + Double.toString(currentItem.getPrice()) + "  *");
        tvQty.setText(Integer.toString(currentItem.getQuantity()) + " order");

        return rowView;
    }
}
