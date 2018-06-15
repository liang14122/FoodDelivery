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
 * Created by 16004118 on 20/11/2017.
 */

public class OverviewCustomAdapter extends ArrayAdapter{

    Context parent_context;
    int layout_id;
    ArrayList<OrderItem> orderList;

    public OverviewCustomAdapter(Context context,
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

        if (rowView == null) {
            LayoutInflater inflater = (LayoutInflater) parent_context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            rowView = inflater.inflate(layout_id, parent, false);
        } else {
            rowView = convertView;
        }

        TextView tvName = (TextView) rowView.findViewById(R.id.tvFoodName);
        TextView tvPrice = (TextView) rowView.findViewById(R.id.tvPrice);
        TextView tvQty = (TextView) rowView.findViewById(R.id.tvQty);

        tvName.setText(orderList.get(position).getName());
        tvPrice.setText( "$" + Double.toString(orderList.get(position).getPrice()) + " per serving");
        tvQty.setText("*  " + orderList.get(position).getQuantity());

        return rowView;
    }
}
